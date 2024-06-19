package com.backend.warehouse_management.utils;


import com.backend.warehouse_management.dto.client.AddItemToOrderRequest;
import com.backend.warehouse_management.dto.client.OrderDTO;
import com.backend.warehouse_management.dto.client.RemoveOrderItemRequest;
import com.backend.warehouse_management.dto.client.UpdateOrderItemRequest;
import com.backend.warehouse_management.dto.manager.CreateDeliveryRequest;
import com.backend.warehouse_management.dto.manager.DeliveryDTO;
import com.backend.warehouse_management.entity.*;
import com.backend.warehouse_management.enums.OrderStatus;
import com.backend.warehouse_management.exception.ItemAlreadyExistsException;
import com.backend.warehouse_management.exception.NotFoundException;
import com.backend.warehouse_management.exception.OrderCannotBeProcessedException;
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

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
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

    public OrderDTO clientAddItemToOrder(Long userId, AddItemToOrderRequest request) {
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



    public OrderDTO clientUpdateItemQuantity(UpdateOrderItemRequest request) {

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
        //retrieve orders in descending order to re-order them in the return list
        List<Order> orders = orderRepository.findAll(Sort.by(Sort.Direction.DESC, "submittedDate"));
        //map the orders to a new list and return them
        return orders.stream()
                .map(CustomOrderMapper::managerMapOrderToOrderDTOGeneralDetails)
                .collect(Collectors.toList());
    }


    public OrderDTO managerGetDetailedOrder(Long orderId) throws Exception {
        //retrieve order
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        //check if it exists
        if (optionalOrder.isPresent()) {
            //return order with all details
            return CustomOrderMapper.managerMapOrderToOrderDTODetailed(optionalOrder.get());
        }
        //if order doesn't exist, throw exception
        else {
            throw new Exception("Order with ID: " + orderId + ", does not exist");
        }
    }


    public OrderDTO managerApproveOrder(Long orderId) throws Exception {
        //retrieve order
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        //check if order exists
        if (optionalOrder.isPresent()) {
            //change order status
            optionalOrder.get().setOrderStatus(OrderStatus.APPROVED);
            //save the order with the new status and return the dto
            return CustomOrderMapper.basicMapOrderToOrderDTO(
                    orderRepository.save(optionalOrder.get()));
        }
        //if order doesn't exist, throw exception
        else {
            throw new Exception("Order with ID: " + orderId + ", does not exist");
        }
    }


    public OrderDTO managerDeclineOrder(Long orderId, String declineReason) throws Exception {
        //retrieve order
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        //check if order exists and the status is awaiting approval
        if (optionalOrder.isPresent() && (optionalOrder.get().getOrderStatus().equals(OrderStatus.AWAITING_APPROVAL))) {
            //change order status
            optionalOrder.get().setOrderStatus(OrderStatus.DECLINED);
            //set the declining reason
            optionalOrder.get().setDecliningReason(declineReason);
            //return the dto of the order with the new values
            return CustomOrderMapper.basicMapOrderToOrderDTO(
                    //save the order with the new values
                    orderRepository.save(optionalOrder.get()));
        }
        //if order doesn't exist, throw exception
        else {
            throw new Exception("Order with ID: " + orderId + ", does not exist");
        }
    }


    public List<DeliveryDTO> managerCheckAvailableDeliveryDates() {
        //get the upper limit of available delivery dates config
        LocalDate upperLimit = LocalDate.now().plusDays(configRepository.findByConfigName("deliveryDays").get().getConfigValue());
        //find all available deliveries from today til the limit
        //List<Delivery> availableDeliveries = deliveryRepository.findDeliveriesByScheduledDateBetween(LocalDate.now(), upperLimit);
        List<Delivery> availableDeliveries = deliveryRepository.findAllByScheduledDateBetween(LocalDate.now(),
                LocalDate.now().plusDays(configRepository.findByConfigName("deliveryDays").get().getConfigValue()));
        return availableDeliveries.stream()
                .map(CustomDeliveryMapper::managerMapDeliveryToDeliveryDTO)
                .collect(Collectors.toList());
    }


    public DeliveryDTO managerCreateDeliveryWithTruck(CreateDeliveryRequest deliveryRequest, Long truckId) throws Exception {
        //retrieve upper date limit
        LocalDate upperLimit = LocalDate.now().plusDays(configRepository.findByConfigName("deliveryDays").get().getConfigValue());
        //check if you can add a delivery on this date (it is within the accepted range limit)
        //also check if it is a weekday
        if (DataUtils.isWithinDateRange(deliveryRequest.getScheduledDate(), LocalDate.now(), upperLimit) && !isWeekend(deliveryRequest.getScheduledDate())) {
            //if a delivery for this truck on this date doesn't exist, create one
            if (!deliveryRepository.existsByScheduledDateAndTruckId(deliveryRequest.getScheduledDate(), truckId)) {
                //create new delivery and assign values
                Delivery newDelivery = new Delivery();
                newDelivery.setScheduledDate(deliveryRequest.getScheduledDate());
                newDelivery.setTruck(truckRepository.findById(truckId).get());
                newDelivery.setRemainingSpace(truckRepository.findById(truckId).get().getCapacity());
                //return delivery after saving
                return CustomDeliveryMapper.managerMapDeliveryToDeliveryDTO(
                        //save new delivery
                        deliveryRepository.save(newDelivery));
            }
            //if there is a delivery for this truck, throw exception
            else {
                throw new Exception("There already is a delivery for this truck");
            }
        }
        //if the delivery request date is not within accepted date range, throw exception
        else {
            throw new Exception("The delivery date is not within the allowed date range or it is not a weekday");
        }
    }

    private boolean isWithinDateRange(LocalDate requestedDate, LocalDate startDate, LocalDate endDate) {
        return (requestedDate.isEqual(startDate) || requestedDate.isAfter(startDate)) &&
                (requestedDate.isBefore(endDate) || requestedDate.isEqual(endDate));
    }

    private boolean isWeekend(LocalDate requestedDate) {
        DayOfWeek requestedDateDay = DayOfWeek.of(requestedDate.get(ChronoField.DAY_OF_WEEK));
        return requestedDateDay == DayOfWeek.SATURDAY || requestedDateDay == DayOfWeek.SUNDAY;
    }



    public DeliveryDTO managerAddOrderToDelivery(Long orderId, Long deliveryId) throws Exception {
        //retrieve order
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        //retrieve delivery
        Optional<Delivery> optionalDelivery = deliveryRepository.findById(deliveryId);
        log.info("checking if order {?1} and delivery {1} exist...", orderId, deliveryId);
        if (optionalOrder.isPresent() && optionalDelivery.isPresent()) {
            //check if this order is approved and that it's not added somewhere else
            log.info("checking if order status is appropriate...");
            if ((optionalOrder.get().getOrderStatus() == OrderStatus.APPROVED) ||
                    (optionalOrder.get().getOrderStatus() != OrderStatus.UNDER_DELIVERY)) {
                //edit status and deadline of order
                optionalOrder.get().setOrderStatus(OrderStatus.UNDER_DELIVERY);
                optionalOrder.get().setDeadline(optionalDelivery.get().getScheduledDate().plusDays(1));
                optionalOrder.get().setDelivery(optionalDelivery.get());
                //check if the order can fit in this truck
                double orderRequiredSpace = getOrderTotalSpace(optionalOrder.get().getOrderItems());
                if (orderRequiredSpace <= optionalDelivery.get().getRemainingSpace()) {
                    //if it fits, update the remaining space in the order
                    optionalDelivery.get().setRemainingSpace(
                            optionalDelivery.get().getRemainingSpace() - orderRequiredSpace);
                }
                //if it's bigger, throw exception
                else {
                    throw new Exception("You have surpassed the available space in the truck!");
                }
                //save the order with the new values
                Order updatedOrder = orderRepository.save(optionalOrder.get());
                //update the product quantity in the inventory too
                updateProductQuantitiesAfterOrderConfirmation(updatedOrder);
                //add order to order list in delivery
                optionalDelivery.get().getOrders().add(updatedOrder);
                //return the delivery now with the order in it
                return CustomDeliveryMapper.managerMapDeliveryToDeliveryDTO(
                        //save the delivery with the order in it
                        deliveryRepository.save(optionalDelivery.get()));
            }
            //if order doesn't have these statuses, throw exception
            else {
                throw new Exception("You cannot add this order to delivery");
            }
        }
        //if they don't exist, throw exception
        else {
            throw new Exception("An order or delivery with this ID doesn't exist");
        }
    }



    private Double getOrderTotalSpace(List<OrderItem> orderItems) {
        double orderTotalSpace = 0.0;
        for (OrderItem orderItem : orderItems) {
            orderTotalSpace += (orderItem.getProduct().getVolume() * orderItem.getQuantity());
        }
        return orderTotalSpace;
    }

    private void updateProductQuantitiesAfterOrderConfirmation(Order order) throws Exception {
        for(OrderItem orderItem : order.getOrderItems()) {
            //retrieve product
            Optional<Product> product = productRepository.findById(orderItem.getProduct().getId());
            //check if there's enough quantity for this product
            if(product.get().getQuantity() >= orderItem.getQuantity()) {
                product.get().setQuantity(
                        orderItem.getProduct().getQuantity() - orderItem.getQuantity());
                productRepository.save(product.get());
            }
            //if there's no quantity for this product, throw exception
            else {
                throw new Exception("You don't have enough of this product to deliver!");
            }
        }
    }


    //TODO
    public List<DeliveryDTO> managerRemoveOrderFromDelivery(Long orderId, Long deliveryId) {


        return null;
    }


}
