package controller;

import model.dto.OrderCreateDto;
import model.dto.OrderResponseDto;
import model.service.OrderServiceImpl;

import java.util.List;

public class OrderController {
    private final OrderServiceImpl orderService = new OrderServiceImpl();
    public void saveOrder(OrderCreateDto orderCreateDto) {
        orderService.addOrder(orderCreateDto);
    }

    public List<OrderResponseDto> getAllOrders() {
        return orderService.getAllOrders();
    }

    public List<OrderResponseDto> getOrdersByCustomer(String uuid) {
        return orderService.gerOrdersByCustomer(uuid);
    }
}
