package com.backend.warehouse_management.utils;


import com.backend.warehouse_management.dto.client.AddItemToOrderRequest;
import com.backend.warehouse_management.dto.client.OrderDTO;
import com.backend.warehouse_management.dto.client.RemoveOrderItemRequest;
import com.backend.warehouse_management.dto.client.UpdateOrderItemRequest;
import com.backend.warehouse_management.dto.manager.CreateDeliveryRequest;
import com.backend.warehouse_management.dto.manager.DeliveryDTO;
import com.backend.warehouse_management.entity.*;
import com.backend.warehouse_management.enums.OrderStatus;
import com.backend.warehouse_management.exception.*;
import com.backend.warehouse_management.mapper.CustomDeliveryMapper;
import com.backend.warehouse_management.mapper.CustomOrderMapper;
import com.backend.warehouse_management.repository.*;
import com.backend.warehouse_management.state.AwaitingApprovalState;
import com.backend.warehouse_management.state.CancelState;
import com.backend.warehouse_management.state.CreateState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderUtils {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final DeliveryRepository deliveryRepository;
    private final TruckRepository truckRepository;
    private final ConfigRepository configRepository;
    private final CreateState createState;
    private final AwaitingApprovalState awaitingApprovalState;
    private final CancelState cancelState;


    public OrderDTO clientCreateOrder(Long userId) {
        if(orderRepository.findByUserIdAndOrderStatus(userId, OrderStatus.CREATED).isPresent()){
            throw new OrderCannotBeProcessedException();
        } else {
            Order order = new Order();
            return createState.createOrder(userId, order);
        }
    }

    public OrderDTO clientAddItemToOrder(AddItemToOrderRequest request) {
        Optional<Order> optionalOrder = orderRepository.findByUserIdAndOrderStatus(request.getUserId(), OrderStatus.CREATED);

        if (optionalOrder.isPresent() && DataUtils.isOrderEditable(optionalOrder.get())) {
            return addItem(request, optionalOrder.get());
        }
        else {
            throw new NotFoundException();
        }
    }



    private OrderDTO addItem(AddItemToOrderRequest itemRequest, Order order) {
        Optional<Order> userOrder = orderRepository.findByUserIdAndOrderStatus(itemRequest.getUserId(), OrderStatus.CREATED);

        Optional<OrderItem> optionalOrderItem = orderItemRepository.findByProductIdAndOrderId(
                itemRequest.getProductId(), userOrder.get().getId());


        if (optionalOrderItem.isEmpty()) {
            Optional<Product> optionalProduct = productRepository.findById(itemRequest.getProductId());
            Optional<User> optionalUser = userRepository.findById(itemRequest.getUserId());

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
            throw new AlreadyExistsException();
        }
    }



    public OrderDTO clientUpdateItemQuantity(UpdateOrderItemRequest request) {

        Optional<OrderItem> optionalOrderItem = orderItemRepository.findById(request.getOrderItemId());

        if(optionalOrderItem.isPresent()){
            Optional<Order> optionalOrder = orderRepository.findById(request.getOrderId());

            if (optionalOrder.isPresent() && DataUtils.isOrderEditable(optionalOrder.get())) {
                Double initialPrice = optionalOrderItem.get().getPrice();

                double priceDifference = (request.getQuantity() * optionalOrderItem.get().getProduct().getPrice()) - initialPrice;

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

    public OrderDTO clientRemoveItemFromOrder(RemoveOrderItemRequest request) {
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

    public OrderDTO clientSubmitOrder(Long orderId) {
        Optional<Order> optionalOrder = orderRepository.findByIdAndUserId(orderId, orderRepository.findById(orderId).get().getUser().getId());

        if (optionalOrder.isPresent() && DataUtils.isOrderEditable(optionalOrder.get())) {
            return awaitingApprovalState.processOrder(optionalOrder.get());
        }
        else {
            throw new OrderCannotBeProcessedException();
        }
    }

    public OrderDTO clientCancelOrder(Long orderId) {
        Optional<Order> optionalOrder = orderRepository.findByIdAndUserId(orderId, orderRepository.findById(orderId).get().getUser().getId());

        if (optionalOrder.isPresent() && DataUtils.isOrderCancellable(optionalOrder.get())) {
            return cancelState.processOrder(optionalOrder.get());
        }
        else {
            throw new OrderCannotBeProcessedException();
        }
    }


    public List<OrderDTO> managerGetAllOrders() {
        List<Order> orders = orderRepository.findAll(Sort.by(Sort.Direction.DESC, "submittedDate"));
        return orders.stream()
                .map(CustomOrderMapper::managerMapOrderToOrderDTOGeneralDetails)
                .collect(Collectors.toList());
    }


    public OrderDTO managerGetDetailedOrder(Long orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            return CustomOrderMapper.managerMapOrderToOrderDTODetailed(optionalOrder.get());
        }
        else {
            throw new NotFoundException();
        }
    }


    public OrderDTO managerApproveOrder(Long orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            optionalOrder.get().setOrderStatus(OrderStatus.APPROVED);

            return CustomOrderMapper.basicMapOrderToOrderDTO(
                    orderRepository.save(optionalOrder.get()));
        }
        else {
            throw new NotFoundException();
        }
    }


    public OrderDTO managerDeclineOrder(Long orderId, String declineReason) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);

        if (optionalOrder.isPresent() && (optionalOrder.get().getOrderStatus().equals(OrderStatus.AWAITING_APPROVAL))) {
            optionalOrder.get().setOrderStatus(OrderStatus.DECLINED);
            optionalOrder.get().setDecliningReason(declineReason);

            return CustomOrderMapper.basicMapOrderToOrderDTO(
                    orderRepository.save(optionalOrder.get()));
        }

        else {
            throw new NotFoundException();
        }
    }


    public List<DeliveryDTO> managerCheckAvailableDeliveryDates() {
        LocalDate upperLimit = LocalDate.now().plusDays(configRepository.findByConfigName("deliveryDays").get().getConfigValue());
        List<Delivery> availableDeliveries = deliveryRepository.findAllByScheduledDateBetween(LocalDate.now(), upperLimit);
        return availableDeliveries.stream()
                .map(CustomDeliveryMapper::managerMapDeliveryToDeliveryDTO)
                .collect(Collectors.toList());
    }


    public DeliveryDTO managerCreateDeliveryWithTruck(CreateDeliveryRequest deliveryRequest, Long truckId) {
        LocalDate upperLimit = LocalDate.now().plusDays(configRepository.findByConfigName("deliveryDays").get().getConfigValue());

        if (DataUtils.isWithinDateRange(deliveryRequest.getScheduledDate(), LocalDate.now(), upperLimit)
                && !DataUtils.isWeekend(deliveryRequest.getScheduledDate())) {

            if (!deliveryRepository.existsByScheduledDateAndTruckId(deliveryRequest.getScheduledDate(), truckId)) {

                Delivery newDelivery = new Delivery();
                newDelivery.setScheduledDate(deliveryRequest.getScheduledDate());
                newDelivery.setTruck(truckRepository.findById(truckId).get());
                newDelivery.setRemainingSpace(truckRepository.findById(truckId).get().getCapacity());

                return CustomDeliveryMapper.managerMapDeliveryToDeliveryDTO(
                        deliveryRepository.save(newDelivery));
            }

            else {
                throw new AlreadyExistsException();
            }
        }
        else {
            throw new DeliveryDateException();
        }
    }



    public DeliveryDTO managerAddOrderToDelivery(Long orderId, Long deliveryId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        Optional<Delivery> optionalDelivery = deliveryRepository.findById(deliveryId);
        //TODO
        //log.info("checking if order {?1} and delivery {1} exist...", orderId, deliveryId);
        if (optionalOrder.isPresent() && optionalDelivery.isPresent()) {
            log.info("checking if order status is appropriate...");
            if ((optionalOrder.get().getOrderStatus() == OrderStatus.APPROVED) ||
                    (optionalOrder.get().getOrderStatus() != OrderStatus.UNDER_DELIVERY)) {

                optionalOrder.get().setOrderStatus(OrderStatus.UNDER_DELIVERY);
                optionalOrder.get().setDeadline(optionalDelivery.get().getScheduledDate().plusDays(1));
                optionalOrder.get().setDelivery(optionalDelivery.get());

                double orderRequiredSpace = getOrderTotalSpace(optionalOrder.get().getOrderItems());
                if (orderRequiredSpace <= optionalDelivery.get().getRemainingSpace()) {
                    optionalDelivery.get().setRemainingSpace(
                            optionalDelivery.get().getRemainingSpace() - orderRequiredSpace);
                }
                else {
                    throw new NoRemainingSpaceException();
                }
                Order updatedOrder = orderRepository.save(optionalOrder.get());
                updateProductQuantitiesAfterOrderConfirmation(updatedOrder);
                optionalDelivery.get().getOrders().add(updatedOrder);
                return CustomDeliveryMapper.managerMapDeliveryToDeliveryDTO(
                        deliveryRepository.save(optionalDelivery.get()));
            }
            else {
                throw new OrderCannotBeProcessedException();
            }
        }
        else {
            throw new NotFoundException();
        }
    }



    private Double getOrderTotalSpace(List<OrderItem> orderItems) {
        double orderTotalSpace = 0.0;
        for (OrderItem orderItem : orderItems) {
            orderTotalSpace += (orderItem.getProduct().getVolume() * orderItem.getQuantity());
        }
        return orderTotalSpace;
    }

    private void updateProductQuantitiesAfterOrderConfirmation(Order order) {
        for(OrderItem orderItem : order.getOrderItems()) {
            Optional<Product> product = productRepository.findById(orderItem.getProduct().getId());

            if(product.get().getQuantity() >= orderItem.getQuantity()) {

                product.get().setQuantity(
                        orderItem.getProduct().getQuantity() - orderItem.getQuantity());

                productRepository.save(product.get());
            }
            else {
                throw new ProductQuantityException();
            }
        }
    }


    //TODO
    public List<DeliveryDTO> managerRemoveOrderFromDelivery(Long orderId, Long deliveryId) {


        return null;
    }


}
