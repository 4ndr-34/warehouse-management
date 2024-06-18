package com.backend.warehouse_management.utils;


import com.backend.warehouse_management.dto.client.AddItemToOrderRequest;
import com.backend.warehouse_management.dto.client.OrderDTO;
import com.backend.warehouse_management.dto.client.RemoveOrderItemRequest;
import com.backend.warehouse_management.dto.client.UpdateOrderItemRequest;
import com.backend.warehouse_management.entity.Order;
import com.backend.warehouse_management.entity.OrderItem;
import com.backend.warehouse_management.entity.Product;
import com.backend.warehouse_management.entity.User;
import com.backend.warehouse_management.enums.OrderStatus;
import com.backend.warehouse_management.exception.ItemAlreadyExistsException;
import com.backend.warehouse_management.exception.NotFoundException;
import com.backend.warehouse_management.exception.OrderCannotBeProcessedException;
import com.backend.warehouse_management.mapper.CustomOrderMapper;
import com.backend.warehouse_management.repository.OrderItemRepository;
import com.backend.warehouse_management.repository.OrderRepository;
import com.backend.warehouse_management.repository.ProductRepository;
import com.backend.warehouse_management.repository.UserRepository;
import com.backend.warehouse_management.state.AwaitingApprovalState;
import com.backend.warehouse_management.state.CancelState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OrderUtils {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final AwaitingApprovalState awaitingApprovalState;
    private final CancelState cancelState;


    public OrderDTO createOrder(Long userId) {
        if(orderRepository.findByUserIdAndOrderStatus(userId, OrderStatus.CREATED).isPresent()){
            throw new OrderCannotBeProcessedException();
        } else {
            Order order = new Order();
            order.setOrderStatus(OrderStatus.CREATED);
            order.setOrderNumber(UUID.randomUUID());
            order.setTotalPrice(0.0);
            order.setSubmittedDate(LocalDate.now());
            order.setUser(userRepository.findById(userId).get());
            order.setOrderStatus(OrderStatus.CREATED);
            return CustomOrderMapper.basicMapOrderToOrderDTO(orderRepository.save(order));
        }
    }

    public OrderDTO addItemToOrder(Long userId, AddItemToOrderRequest request) {
        Optional<Order> optionalOrder = orderRepository.findByUserIdAndOrderStatus(userId, OrderStatus.CREATED);

        if (optionalOrder.isPresent() && DataUtils.isOrderEditable(optionalOrder.get())) {
            return addItem(userId, request, optionalOrder.get());
        }
        else {
            throw new NotFoundException();
        }
    }



    private OrderDTO addItem(Long userId, AddItemToOrderRequest itemRequest, Order order) {
        Optional<Order> userOrder = orderRepository.findByUserIdAndOrderStatus(userId, OrderStatus.CREATED);

        Optional<OrderItem> optionalOrderItem = orderItemRepository.findByProductIdAndOrderId(
                itemRequest.getProductId(), userOrder.get().getId());


        if (!optionalOrderItem.isPresent()) {
            Optional<Product> optionalProduct = productRepository.findById(itemRequest.getProductId());
            Optional<User> optionalUser = userRepository.findById(userId);

            if (optionalProduct.isPresent() && optionalUser.isPresent()) {
                Product product = optionalProduct.get();
                OrderItem orderItem = new OrderItem();

                orderItem.setItemName(product.getItemName());
                orderItem.setProduct(product);
                orderItem.setQuantity(itemRequest.getQuantity());
                orderItem.setPrice(product.getPrice() * itemRequest.getQuantity());
                orderItem.setOrder(order);
                OrderItem updatedOrderItem = orderItemRepository.save(orderItem);
                order.setTotalPrice(order.getTotalPrice() + updatedOrderItem.getPrice());
                order.getOrderItems().add(updatedOrderItem);

                return CustomOrderMapper.basicMapOrderToOrderDTO(orderRepository.save(order));
            }

            else {
                throw new NotFoundException();
            }
        } else {
            throw new ItemAlreadyExistsException("This item already exists in this order");
        }
    }



    public OrderDTO updateItemQuantity(UpdateOrderItemRequest request) {

        Optional<OrderItem> optionalOrderItem = orderItemRepository.findById(request.getOrderItemId());

        if(optionalOrderItem.isPresent()){
            Optional<Order> optionalOrder = orderRepository.findById(request.getOrderId());

            if (optionalOrder.isPresent() && DataUtils.isOrderEditable(optionalOrder.get())) {
                Double initialPrice = optionalOrderItem.get().getPrice();

                double priceDifference = (request.getQuantity() * optionalOrderItem.get().getProduct().getPrice()) - initialPrice;                             ;

                optionalOrder.get().setTotalPrice(
                        optionalOrder.get().getTotalPrice() + priceDifference);
                optionalOrderItem.get().setQuantity(request.getQuantity());
                optionalOrderItem.get().setPrice(optionalOrderItem.get().getProduct().getPrice() * request.getQuantity());

                orderItemRepository.save(optionalOrderItem.get());

                return CustomOrderMapper.basicMapOrderToOrderDTO(
                        orderRepository.save(optionalOrder.get()));

            }
            else {
                throw new OrderCannotBeProcessedException();
            }
        }
        else {
            throw new NotFoundException();
        }
    }

    public OrderDTO removeItemFromOrder(RemoveOrderItemRequest request) {
        Optional<OrderItem> optionalOrderItem = orderItemRepository.findById(request.getOrderItemId());

        if (optionalOrderItem.isPresent()) {
            Optional<Order> optionalOrder = orderRepository.findById(request.getOrderId());

            if (optionalOrder.isPresent() && DataUtils.isOrderEditable(optionalOrder.get())) {

                optionalOrder.get().setTotalPrice(optionalOrder.get().getTotalPrice() - optionalOrderItem.get().getPrice());
                orderItemRepository.deleteById(request.getOrderItemId());

                return CustomOrderMapper.basicMapOrderToOrderDTO(optionalOrder.get());
            }
            else {
                throw new OrderCannotBeProcessedException();
            }
        }
        else {
            throw new NotFoundException();
        }

    }

    public OrderDTO submitOrder(Long orderId) {
        Optional<Order> optionalOrder = orderRepository.findByIdAndUserId(orderId, orderRepository.findById(orderId).get().getUser().getId());

        if (optionalOrder.isPresent() && DataUtils.isOrderEditable(optionalOrder.get())) {
            return awaitingApprovalState.processOrder(optionalOrder.get());
        }
        else {
            throw new OrderCannotBeProcessedException();
        }
    }

    public OrderDTO cancelOrder(Long orderId) {
        Optional<Order> optionalOrder = orderRepository.findByIdAndUserId(orderId, orderRepository.findById(orderId).get().getUser().getId());

        if (optionalOrder.isPresent() && DataUtils.isOrderCancellable(optionalOrder.get())) {
            return cancelState.processOrder(optionalOrder.get());
        }
        else {
            throw new OrderCannotBeProcessedException();
        }
    }

}
