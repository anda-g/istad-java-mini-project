package model.repository;

import model.dto.OrderResponseDto;
import model.entites.Order;

import java.util.List;

public interface OrderRepository {
    void save(Order order);
    List<OrderResponseDto> getAllOrders();
    List<OrderResponseDto> getOrdersByCustomer(String uuid);
}
