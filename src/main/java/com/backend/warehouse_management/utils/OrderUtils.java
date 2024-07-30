package com.backend.warehouse_management.utils;


import com.backend.warehouse_management.dto.client.*;
import com.backend.warehouse_management.dto.manager.CreateDeliveryRequest;
import com.backend.warehouse_management.dto.manager.DeclineOrderRequest;
import com.backend.warehouse_management.dto.manager.DeliveryDTO;
import com.backend.warehouse_management.entity.*;
import com.backend.warehouse_management.enums.OrderStatus;
import com.backend.warehouse_management.exception.*;
import com.backend.warehouse_management.mapper.CustomDeliveryMapper;
import com.backend.warehouse_management.mapper.CustomOrderMapper;
import com.backend.warehouse_management.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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


    public OrderDTO clientCreateOrder(Long userId) {
        log.info("creating order, searching if it already exists...");
        if(orderRepository.findByUserIdAndOrderStatus(userId, OrderStatus.CREATED).isPresent()){
            log.error("Order already exists!");
            throw new OrderCannotBeProcessedException();
        } else {
            Order order = new Order();
            order.setOrderNumber(UUID.randomUUID());
            order.setTotalPrice(0.0);
            order.setSubmittedDate(LocalDate.now());
            order.setUser(userRepository.findById(userId).get());
            order.setOrderStatus(OrderStatus.CREATED);
            log.info("Created and saving order...");
            return CustomOrderMapper.basicMapOrderToOrderDTO(orderRepository.save(order));
        }
    }

    public OrderDTO clientAddItemToOrder(AddItemToOrderRequest request) {
        log.info("finding order with userID " + request.getUserId() + "and CREATED status");
        Optional<Order> optionalOrder = orderRepository.findByUserIdAndOrderStatus(request.getUserId(), OrderStatus.CREATED);

        if (optionalOrder.isPresent() && DataUtils.isOrderEditable(optionalOrder.get())) {
            log.info("adding item "+request.getProductId()+ "to order with id: "+request.getOrderId());
            return addItem(request, optionalOrder.get());
        }
        else {
            throw new NotFoundException();
        }
    }



    private OrderDTO addItem(AddItemToOrderRequest itemRequest, Order order) {
        Optional<OrderItem> optionalOrderItem = orderItemRepository.findByProductIdAndOrderId(
                itemRequest.getProductId(), order.getId());


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
                log.error("The product or user is not found!");
                throw new NotFoundException();
            }
        } else {
            log.error("This item is already added in the order");
            throw new AlreadyExistsException();
        }
    }



    public OrderDTO clientUpdateItemQuantity(UpdateOrderItemRequest request) {
        log.info("searching for order item with id:"+request.getOrderItemId()+" ...");
        Optional<OrderItem> optionalOrderItem = orderItemRepository.findById(request.getOrderItemId());

        if(optionalOrderItem.isPresent()){
            Optional<Order> optionalOrder = orderRepository.findById(optionalOrderItem.get().getOrder().getId());
            log.info("checking if you can edit this order...");
            if (optionalOrder.isPresent() && DataUtils.isOrderEditable(optionalOrder.get())) {
                log.info("updating order item quantity to "+request.getQuantity());
                Double initialPrice = optionalOrderItem.get().getPrice();

                double priceDifference = (request.getQuantity() * optionalOrderItem.get().getProduct().getPrice()) - initialPrice;

                optionalOrder.get().setTotalPrice(
                        optionalOrder.get().getTotalPrice() + priceDifference);
                optionalOrderItem.get().setQuantity(request.getQuantity());
                optionalOrderItem.get().setPrice(optionalOrderItem.get().getProduct().getPrice() * request.getQuantity());

                orderItemRepository.save(optionalOrderItem.get());
                log.info("saving updated order...");
                return CustomOrderMapper.basicMapOrderToOrderDTO(
                        orderRepository.save(optionalOrder.get()));

            }
            else {
                log.error("You cannot edit this order!");
                throw new OrderCannotBeProcessedException();
            }
        }
        else {
            log.error("Couldn't find this order item!");
            throw new NotFoundException();
        }
    }

    public OrderDTO clientRemoveItemFromOrder(RemoveOrderItemRequest request) {
        log.info("searching for order item with id:"+request.getOrderItemId()+" ...");
        Optional<OrderItem> optionalOrderItem = orderItemRepository.findById(request.getOrderItemId());

        if (optionalOrderItem.isPresent()) {
            Optional<Order> optionalOrder = orderRepository.findById(request.getOrderId());
            log.info("checking if you can edit this order...");
            if (optionalOrder.isPresent() && DataUtils.isOrderEditable(optionalOrder.get())) {

                optionalOrder.get().setTotalPrice(optionalOrder.get().getTotalPrice() - optionalOrderItem.get().getPrice());
                log.info("deleting order item...");
                orderItemRepository.deleteById(request.getOrderItemId());
                log.info("saving updated order...");
                return CustomOrderMapper.basicMapOrderToOrderDTO(optionalOrder.get());
            }
            else {
                log.error("You cannot edit this order!");
                throw new OrderCannotBeProcessedException();
            }
        }
        else {
            log.error("Couldn't find this order item!");
            throw new NotFoundException();
        }

    }

    public OrderDTO clientSubmitOrder(Long orderId) {
        log.info(MessageFormat.format("finding order with id:{0}", orderId));
        Optional<Order> optionalOrder = orderRepository.findByIdAndUserId(orderId, orderRepository.findById(orderId).get().getUser().getId());
        log.info("checking if you can edit this order...");
        if (optionalOrder.isPresent() && DataUtils.isOrderEditable(optionalOrder.get())) {
            log.info("updating and saving order...");
            optionalOrder.get().setOrderStatus(OrderStatus.AWAITING_APPROVAL);
            return CustomOrderMapper.basicMapOrderToOrderDTO(orderRepository.save(optionalOrder.get()));
        }
        else {
            log.error("You cannot edit this order!");
            throw new OrderCannotBeProcessedException();
        }
    }

    public OrderDTO clientCancelOrder(Long orderId) {
        log.info(MessageFormat.format("finding order with id:{0}", orderId));
        Optional<Order> optionalOrder = orderRepository.findByIdAndUserId(orderId, orderRepository.findById(orderId).get().getUser().getId());

        log.info("checking if you can edit this order...");
        if (optionalOrder.isPresent() && DataUtils.isOrderCancellable(optionalOrder.get())) {
            log.info("updating and saving order...");
            optionalOrder.get().setOrderStatus(OrderStatus.CANCELLED);
            return CustomOrderMapper.basicMapOrderToOrderDTO(orderRepository.save(optionalOrder.get()));
        }
        else {
            log.error("You cannot edit this order!");
            throw new OrderCannotBeProcessedException();
        }
    }


    public List<OrderDTO> managerGetAllOrders() {
        log.info("retrieving all orders...");
        List<Order> orders = orderRepository.findAll(Sort.by(Sort.Direction.DESC, "submittedDate"));
        return orders.stream()
                .map(CustomOrderMapper::managerMapOrderToOrderDTOGeneralDetails)
                .collect(Collectors.toList());
    }


    public OrderDTO managerGetDetailedOrder(Long orderId) {
        log.info(MessageFormat.format("finding order with id:{0}", orderId));
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            return CustomOrderMapper.managerMapOrderToOrderDTODetailed(optionalOrder.get());
        }
        else {
            log.error("Cannot find this order!");
            throw new NotFoundException();
        }
    }


    public OrderDTO managerApproveOrder(Long orderId) {
        log.info(MessageFormat.format("finding order with id:{0}", orderId));
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            log.info("approving and saving order...");
            optionalOrder.get().setOrderStatus(OrderStatus.APPROVED);

            return CustomOrderMapper.basicMapOrderToOrderDTO(
                    orderRepository.save(optionalOrder.get()));
        }
        else {
            log.error("Cannot find this order!");
            throw new NotFoundException();
        }
    }


    public OrderDTO managerDeclineOrder(DeclineOrderRequest request) {
        log.info(MessageFormat.format("finding order with id:{0}", request.getOrderId()));
        Optional<Order> optionalOrder = orderRepository.findById(request.getOrderId());

        if (optionalOrder.isPresent() && (optionalOrder.get().getOrderStatus().equals(OrderStatus.AWAITING_APPROVAL))) {
            log.info("declining and saving order...");
            optionalOrder.get().setOrderStatus(OrderStatus.DECLINED);
            optionalOrder.get().setDecliningReason(request.getReason());

            return CustomOrderMapper.basicMapOrderToOrderDTO(
                    orderRepository.save(optionalOrder.get()));
        }

        else {
            log.error("Cannot find this order!");
            throw new NotFoundException();
        }
    }


    public List<DeliveryDTO> managerCheckAvailableDeliveryDates() {
        int value = configRepository.findByConfigName("deliveryDays").get().getConfigValue();
        LocalDate upperLimit = LocalDate.now().plusDays(value);
        log.info("finding all available deliveries in the upcoming "+value+" days...");
        List<Delivery> availableDeliveries = deliveryRepository.findAllByScheduledDateBetween(LocalDate.now(), upperLimit);
        return availableDeliveries.stream()
                .map(CustomDeliveryMapper::managerMapDeliveryToDeliveryDTO)
                .collect(Collectors.toList());
    }


    public DeliveryDTO managerCreateDeliveryWithTruck(CreateDeliveryRequest deliveryRequest) {
        LocalDate upperLimit = LocalDate.now().plusDays(configRepository.findByConfigName("deliveryDays").get().getConfigValue());
        log.info("checking if this date is a valid delivery day");
        if (DataUtils.isWithinDateRange(deliveryRequest.getScheduledDate(), LocalDate.now(), upperLimit)
                && !DataUtils.isWeekend(deliveryRequest.getScheduledDate())) {
            log.info("checking if a delivery already exists for this date and truck...");
            if (!deliveryRepository.existsByScheduledDateAndTruckId(deliveryRequest.getScheduledDate(), deliveryRequest.getTruckId())) {
                log.info(MessageFormat.format("Creating and saving a new delivery with date {0} and truck id {1}", deliveryRequest.getScheduledDate(), deliveryRequest.getTruckId()));
                Delivery newDelivery = new Delivery();
                newDelivery.setScheduledDate(deliveryRequest.getScheduledDate());
                newDelivery.setTruck(truckRepository.findById(deliveryRequest.getTruckId()).get());
                newDelivery.setRemainingSpace(truckRepository.findById(deliveryRequest.getTruckId()).get().getCapacity());

                return CustomDeliveryMapper.managerMapDeliveryToDeliveryDTO(
                        deliveryRepository.save(newDelivery));
            }

            else {
                log.error("A delivery with this date and truck already exists!");
                throw new AlreadyExistsException();
            }
        }
        else {
            log.error("This day is not a valid delivery day!");
            throw new DeliveryDateException();
        }
    }



    public DeliveryDTO managerAddOrderToDelivery(Long orderId, Long deliveryId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        Optional<Delivery> optionalDelivery = deliveryRepository.findById(deliveryId);
        //TODO
        log.info(MessageFormat.format("checking if order {0} and delivery {1} exist...", orderId, deliveryId));
        if (optionalOrder.isPresent() && optionalDelivery.isPresent()) {
            log.info("checking if order status is appropriate...");
            if ((optionalOrder.get().getOrderStatus() == OrderStatus.APPROVED) ||
                    (optionalOrder.get().getOrderStatus() != OrderStatus.UNDER_DELIVERY)) {

                log.info("Checking if the truck has enough space for this order...");
                optionalOrder.get().setDeadline(optionalDelivery.get().getScheduledDate().plusDays(1));
                optionalOrder.get().setDelivery(optionalDelivery.get());

                double orderRequiredSpace = getOrderTotalSpace(optionalOrder.get().getOrderItems());
                if (orderRequiredSpace <= optionalDelivery.get().getRemainingSpace()) {
                    optionalDelivery.get().setRemainingSpace(
                            optionalDelivery.get().getRemainingSpace() - orderRequiredSpace);
                }
                else {
                    log.error("There's no more space in the truck for this order!");
                    throw new NoRemainingSpaceException();
                }
                log.info("Saving order, updating product quantities...");
                checkAndUpdateProductQuantities(optionalOrder.get());
                optionalOrder.get().setOrderStatus(OrderStatus.UNDER_DELIVERY);
                Order updatedOrder = orderRepository.save(optionalOrder.get());
                optionalDelivery.get().getOrders().add(updatedOrder);
                return CustomDeliveryMapper.managerMapDeliveryToDeliveryDTO(
                        deliveryRepository.save(optionalDelivery.get()));
            }
            else {
                log.error("You cannot edit this order!");
                throw new OrderCannotBeProcessedException();
            }
        }
        else {
            log.error("This order or delivery does not exist!");
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

    private void checkAndUpdateProductQuantities(Order order) {
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


    public List<DeliveryDTO> completeDeliveries(){
        log.info("Finding today's deliveries...");
        List<Delivery> scheduledDeliveries = deliveryRepository.findAllByScheduledDate(LocalDate.now());
        List<DeliveryDTO> returnList = new ArrayList<>();
        for(Delivery delivery : scheduledDeliveries){
            List<Order> deliveryOrders = orderRepository.findAllByDeliveryId(delivery.getId());
            for(Order order : deliveryOrders){
                log.info("Fulfilling orders...");
                order.setOrderStatus(OrderStatus.FULFILLED);
                orderRepository.save(order);
            }
            returnList.add(CustomDeliveryMapper.managerMapDeliveryToDeliveryDTO(deliveryRepository.save(delivery)));
        }
        return returnList;
    }


    public List<OrderDTO> getAllOrdersForClientId(Long userId) {
        log.info(MessageFormat.format("Finding all orders for user {0}", userId));
        List<Order> ordersForUser = orderRepository.findAllByUserId(userId);
        List<OrderDTO> orderListForReturn = new ArrayList<>();
        for(Order order : ordersForUser) {
            orderListForReturn.add(CustomOrderMapper.basicMapOrderToOrderDTO(order));
        }
        return orderListForReturn;
    }


    public List<OrderDTO> getOrdersByStatusAndClientId(GetOrdersByStatusRequest request) {
        log.info(MessageFormat.format("Finding all orders for user {0} with status {1}", request.getUserId(), request.getStatus()));
        List<Order> ordersOfStatus = orderRepository.findAllByUserIdAndOrderStatus(request.getUserId(), request.getStatus());
        List<OrderDTO> orderListForReturn = new ArrayList<>();
        for(Order order : ordersOfStatus) {
            orderListForReturn.add(CustomOrderMapper.basicMapOrderToOrderDTO(order));
        }
        return orderListForReturn;
    }


}
