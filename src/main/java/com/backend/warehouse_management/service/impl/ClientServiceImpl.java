package com.backend.warehouse_management.service.impl;

import com.backend.warehouse_management.dto.client.AddItemToOrderRequest;
import com.backend.warehouse_management.dto.client.OrderDTO;
import com.backend.warehouse_management.entity.Order;
import com.backend.warehouse_management.entity.OrderItem;
import com.backend.warehouse_management.entity.Product;
import com.backend.warehouse_management.entity.User;
import com.backend.warehouse_management.enums.OrderStatus;
import com.backend.warehouse_management.mapper.CustomOrderMapper;
import com.backend.warehouse_management.mapper.OrderItemMapper;
import com.backend.warehouse_management.mapper.OrderMapper;
import com.backend.warehouse_management.repository.OrderItemRepository;
import com.backend.warehouse_management.repository.OrderRepository;
import com.backend.warehouse_management.repository.ProductRepository;
import com.backend.warehouse_management.repository.UserRepository;
import com.backend.warehouse_management.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final OrderUtils orderUtils;

    @Override
    public OrderDTO createOrder(Long userId) throws Exception {
        return orderUtils.createOrder(userId);
    }




    @Override
    public OrderDTO addItemToOrder(Long userId, AddItemToOrderRequest itemRequest) {
        return orderUtils.addItemToOrder(userId, itemRequest);
    }

    private OrderDTO addItem(Long userId, AddItemToOrderRequest itemRequest, Order order) throws Exception {
        Optional<Order> userOrder = orderRepository.findByUserIdAndOrderStatus(userId, OrderStatus.CREATED);

        Optional<OrderItem> optionalOrderItem = orderItemRepository.findByProductIdAndOrderId(
                itemRequest.getProductId(), userOrder.get().getId());

        //if item is not already present in the order then create it
        if (!optionalOrderItem.isPresent()) {
            Optional<Product> optionalProduct = productRepository.findById(itemRequest.getProductId());
            Optional<User> optionalUser = userRepository.findById(userId);

            //if product or user are existent, then add the respective product to the order of the user
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

                //save order and return it
                return CustomOrderMapper.basicMapOrderToOrderDTO(orderRepository.save(order));
            }

            //otherwise throw an exception that these two are not found
            else {
                throw new Exception("This product or user doesn't exist.");
            }
        } else {
            throw new Exception("This item already exists in this order");
        }
    }




    @Override
    public OrderDTO updateItemQuantity(UpdateOrderItemRequest request) {
        return orderUtils.updateItemQuantity(request);
    }


    @Override
    public OrderDTO removeItemFromOrder(Long orderItemId, Long orderId) throws Exception {
        //find item
        Optional<OrderItem> optionalOrderItem = orderItemRepository.findById(orderItemId);
        //check if it exists
        if (optionalOrderItem.isPresent()) {
            //then retrieve order
            Optional<Order> optionalOrder = orderRepository.findById(orderId);
            //if the order exists and its status is acceptable, we can edit
            if (optionalOrder.isPresent() && (optionalOrder.get().getOrderStatus().equals(OrderStatus.CREATED)
                    || optionalOrder.get().getOrderStatus().equals(OrderStatus.DECLINED))) {
                //reduce the item price from the total price of the order
                optionalOrder.get().setTotalPrice(optionalOrder.get().getTotalPrice() - optionalOrderItem.get().getPrice());
                //delete the item
                orderItemRepository.deleteById(orderItemId);

                return CustomOrderMapper.basicMapOrderToOrderDTO(optionalOrder.get());
            }
            //if order doesn't exist or is non-editable, throw exception
            else {
                throw new Exception("Order doesn't exist or you're not allowed to update it");
            }
        }
        //if item does not exist, throw exception
        else {
            throw new Exception("This item does not exist");
        }

    }

    @Override
    public OrderDTO submitOrder(Long userId, Long orderId) throws Exception {
        //retrieve order
        Optional<Order> optionalOrder = orderRepository.findByIdAndUserId(orderId, userId);
        //if the order exists and its status is acceptable, we can edit
        if (optionalOrder.isPresent() && (optionalOrder.get().getOrderStatus().equals(OrderStatus.CREATED) ||
                optionalOrder.get().getOrderStatus().equals(OrderStatus.DECLINED))) {
            optionalOrder.get().setOrderStatus(OrderStatus.AWAITING_APPROVAL);
            //save order and return it with the new status
            return CustomOrderMapper.basicMapOrderToOrderDTO(
                    orderRepository.save(optionalOrder.get()));
        }
        //if order doesn't exist or is non-editable, throw exception
        else {
            throw new Exception("Order doesn't exist or you're not allowed to update it");
        }
    }

    @Override
    public OrderDTO cancelOrder(Long orderId) {
        return orderUtils.cancelOrder(orderId);
    }


}
