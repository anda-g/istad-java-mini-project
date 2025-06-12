package model.service;

import model.dto.OrderCreateDto;
import model.dto.OrderResponseDto;
import model.entites.Order;

import java.util.List;

public interface OrderService {
    void addOrder(OrderCreateDto orderCreateDto);
    List<OrderResponseDto> getAllOrders();
    List<OrderResponseDto> gerOrdersByCustomer(String uuid);
}
