package model.repository;

import model.entites.Category;
import model.entites.Product;
import utils.DatabaseConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductRepository implements Repository<Product, Integer> {
    @Override
    public List<Product> findAll() {
        String sql = """
                SELECT * FROM products
                WHERE is_deleted = FALSE
                """;
        try(Connection conn = DatabaseConfig.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            List<Product> products = new ArrayList<>();
            while (rs.next()) {
                products.add(getProduct(rs));
            }
            return products;
        } catch (SQLException e) {
            System.err.println("[!] SQLException: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Product findById(Integer id) {
        String sql = """
                SELECT * FROM products
                WHERE id = ? AND is_deleted = FALSE
                """;
        try(Connection conn = DatabaseConfig.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return getProduct(rs);
            }
            return null;
        } catch (SQLException e) {
            System.err.println("[!] SQLException: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Product findByUuid(String uuid) {
        String sql = """
                SELECT * FROM products
                WHERE uuid = ? AND is_deleted = FALSE
                """;
        try(Connection conn = DatabaseConfig.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return getProduct(rs);
            }
            return null;
        } catch (SQLException e) {
            System.err.println("[!] SQLException: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Boolean save(Product product) {
        String sql = """
                INSERT INTO products (uuid, product_name, price, quantity, is_deleted, category)
                VALUES ( ?, ?, ?, ?, FALSE, ?)
                """;
        try(Connection conn = DatabaseConfig.getConnection()){
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, product.getUuid());
            stmt.setString(2, product.getName());
            stmt.setDouble(3, product.getPrice());
            stmt.setInt(4, product.getQuantity());
            stmt.setString(5, product.getCategory().toString());
            if(stmt.executeUpdate() > 0){
                return true;
            }
        } catch (SQLException e) {
            System.err.println("[!] SQLException: " + e.getMessage());
        }
        return false;
    }

    @Override
    public Boolean deleteById(Integer id) {
        String sql = """
                UPDATE products
                SET is_deleted = TRUE
                WHERE id = ?
                """;
        try(Connection conn = DatabaseConfig.getConnection()){
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            if(ps.executeUpdate() > 0){
                return true;
            }
        } catch (SQLException e) {
            System.err.println("[!] SQLException: " + e.getMessage());
        }
        return false;
    }

    @Override
    public Boolean deleteByUuid(String uuid) {
        Product product = findByUuid(uuid);
        if(product != null){
            deleteById(product.getId());
            return true;
        }
        return false;
    }

    public Product getProduct(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setId(rs.getInt("id"));
        product.setUuid(rs.getString("uuid"));
        product.setName(rs.getString("product_name"));
        product.setPrice(rs.getDouble("price"));
        product.setQuantity(rs.getInt("quantity"));
        product.setCategory(Category.valueOf(rs.getString("category")));
        return product;
    }
}
