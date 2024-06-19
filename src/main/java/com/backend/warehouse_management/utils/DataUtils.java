package com.backend.warehouse_management.utils;

import com.backend.warehouse_management.entity.Order;
import com.backend.warehouse_management.enums.OrderStatus;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoField;

public final class DataUtils {
    private DataUtils() {
    }

    public static boolean isWithinDateRange(LocalDate requestedDate, LocalDate startDate, LocalDate endDate) {
        return (requestedDate.isEqual(startDate) || requestedDate.isAfter(startDate)) &&
                (requestedDate.isBefore(endDate) || requestedDate.isEqual(endDate));
    }

    public static boolean isOrderCancellable(Order order) {
        return !(order.getOrderStatus().equals(OrderStatus.FULFILLED) ||
                order.getOrderStatus().equals(OrderStatus.UNDER_DELIVERY) ||
                order.getOrderStatus().equals(OrderStatus.CANCELLED));
    }

    public static boolean isOrderEditable(Order order) {
        return (order.getOrderStatus().equals(OrderStatus.CREATED) ||
                order.getOrderStatus().equals(OrderStatus.DECLINED));
    }

    public static boolean isWeekend(LocalDate requestedDate) {
        DayOfWeek requestedDateDay = DayOfWeek.of(requestedDate.get(ChronoField.DAY_OF_WEEK));
        return requestedDateDay == DayOfWeek.SATURDAY || requestedDateDay == DayOfWeek.SUNDAY;
    }
}
