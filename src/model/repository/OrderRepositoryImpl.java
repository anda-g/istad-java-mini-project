package model.repository;

import model.dto.OrderResponseDto;
import model.entites.Order;
import utils.DatabaseConfig;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderRepositoryImpl implements OrderRepository {
    @Override
    public void save(Order order) {
        for (Integer productId : order.getProductId()) {
            makeOrder(order.getUserId(), productId, order.getOrderDate());
        }
    }

    public void makeOrder(Integer userId, Integer productId, LocalDate orderDate) {
        String sql = """
                INSERT INTO orders (user_id, product_id, order_date)
                VALUES (?, ?, ?)
                """;
        try(Connection conn = DatabaseConfig.getConnection()){
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            ps.setDate(3, Date.valueOf(orderDate));
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("[!] SQLException: " + e.getMessage());
        }
    }
    @Override
    public List<OrderResponseDto> getAllOrders() {
        String sql = """
                SELECT U.username, P.product_name, O.order_date
                FROM orders O
                INNER JOIN users U ON O.user_id = U.id
                INNER JOIN products P ON O.product_id = P.id
                """;
        try(Connection conn = DatabaseConfig.getConnection()){
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            return getOrderResponseDtoList(rs);
        } catch (SQLException e) {
            System.out.println("[!] SQLException: " + e.getMessage());
        }
        return List.of();
    }

    @Override
    public List<OrderResponseDto> getOrdersByCustomer(String uuid) {
        String sql = """
                SELECT U.username, P.product_name, O.order_date
                FROM orders O
                INNER JOIN users U ON O.user_id = U.id
                INNER JOIN products P ON O.product_id = P.id
                WHERE U.uuid = ?
                """;
        try(Connection conn = DatabaseConfig.getConnection()){
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, uuid);
            ResultSet rs = stmt.executeQuery();
            return getOrderResponseDtoList(rs);
        } catch (SQLException e) {
            System.out.println("[!] SQLException: " + e.getMessage());
        }
        return List.of();
    }

    private List<OrderResponseDto> getOrderResponseDtoList(ResultSet rs) throws SQLException {
        List<OrderResponseDto> orders = new ArrayList<>();
        while (rs.next()) {
            String username = rs.getString("username");
            String productName = rs.getString("product_name");
            LocalDate orderDate = rs.getDate("order_date").toLocalDate();
            orders.add(new OrderResponseDto(username, productName, orderDate));
        }
        return orders;
    }
}
