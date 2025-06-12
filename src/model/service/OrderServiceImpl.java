package model.service;

import model.dto.OrderCreateDto;
import model.dto.OrderResponseDto;
import model.entites.Order;
import model.entites.Product;
import model.entites.User;
import model.repository.OrderRepositoryImpl;
import model.repository.ProductRepository;
import model.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class OrderServiceImpl implements OrderService {
    private final UserRepository userRepository = new UserRepository();
    private final OrderRepositoryImpl orderRepository = new OrderRepositoryImpl();
    private final ProductRepository productRepository = new ProductRepository();
    @Override
    public void addOrder(OrderCreateDto orderCreateDto) {
        Order order = new Order();
        User user = userRepository.findByUuid(orderCreateDto.userUuid());
        List<Integer> productId = new ArrayList<>();
        for (String uuid : orderCreateDto.productUuid()) {
            Product product = productRepository.findByUuid(uuid);
            productId.add(product.getId());
        }
        order.setUserId(user.getId());
        order.setProductId(productId);
        order.setOrderDate(orderCreateDto.orderDate());
        orderRepository.save(order);
    }

    @Override
    public List<OrderResponseDto> getAllOrders() {
        return orderRepository.getAllOrders();
    }

    @Override
    public List<OrderResponseDto> gerOrdersByCustomer(String uuid) {
        return orderRepository.getOrdersByCustomer(uuid);
    }
}
