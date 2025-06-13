package model.repository;

import com.github.javafaker.Faker;
import model.entites.Category;
import model.entites.Product;
import utils.DatabaseConfig;

import javax.xml.crypto.Data;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    public void insertMultiProducts(Long numbersOfProducts){
        String sql = """
                INSERT INTO products (uuid, product_name, price, quantity, is_deleted, category)
                VALUES ( ?, ?, ?, ?, FALSE, ?)
                """;
        try(Connection conn = DatabaseConfig.getConnection()){
            PreparedStatement ps = conn.prepareStatement(sql);
            conn.setAutoCommit(false);
            Faker faker = new Faker();
            for(int i = 0; i < numbersOfProducts; i++){
                String productName = faker.commerce().productName();
                String price = faker.commerce().price();
                int qty = faker.random().nextInt(50,100);
                Category category = faker.random().nextBoolean() ? Category.FOOD : Category.DRINK;
                ps.setString(1, UUID.randomUUID().toString());
                ps.setString(2, productName);
                ps.setDouble(3, Double.parseDouble(price));
                ps.setInt(4, qty);
                ps.setString(5, category.toString());
                ps.addBatch();

                if(i % 1000 == 0){
                    ps.executeBatch();
                    conn.commit();

                }

            }
            ps.executeBatch();
            conn.commit();
            System.out.println("Inserted " + numbersOfProducts + " products");
        } catch (SQLException e) {
            System.out.println("[!] Error while inserting a lot products: " + e.getMessage());
        }
    }

    public List<Product> readMultiProducts(){
        String sql = """
                SELECT * FROM products
                WHERE is_deleted = FALSE
                """;
        try(Connection conn = DatabaseConfig.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setFetchSize(1000);
            ResultSet rs = ps.executeQuery();
            List<Product> products = new ArrayList<>();
            while (rs.next()) {
                products.add(getProduct(rs));
            }
            return products;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void clearAll(){
        String sql = """
                TRUNCATE TABLE products
                """;
        try(Connection conn = DatabaseConfig.getConnection()) {
            Statement stmt = conn.createStatement();
            System.out.println(stmt.executeUpdate(sql) + " Rows deleted");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Product> searchProductByName(String name){
        String sql = """
                    SELECT * FROM products 
                    WHERE product_name ILIKE ? AND is_deleted = FALSE""";
        List<Product> products = new ArrayList<>();
        try(Connection con = DatabaseConfig.getConnection()){
            assert con != null;
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1,  name + "%");
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                products.add(getProduct(rs));
            }
            return products;
        }catch (Exception e){
            System.out.println("Error during search product by name: " + e.getMessage());
        }
        return List.of();
    }

    public List<Product> filterByCategory(String category){
        String sql = """
                SELECT * FROM products 
                WHERE is_deleted = FALSE AND category ILIKE ?
                """;
        try(Connection conn = DatabaseConfig.getConnection()){
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "%"+category+"%");
            ResultSet rs = ps.executeQuery();
            List<Product> products = new ArrayList<>();
            while (rs.next()) {
                products.add(getProduct(rs));
            }
            return products;
        } catch (SQLException e) {
            System.out.println("[!] Error while filtering products: " + e.getMessage());
        }
        return null;
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
