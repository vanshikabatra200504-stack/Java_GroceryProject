package service;

import db.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductService {

    // Add product
    public static boolean addProduct(
            String id, String name, String category,
            double price, int quantity) {

        String sql =
            "INSERT INTO products (product_id, name, category, price, quantity) " +
            "VALUES (?, ?, ?, ?, ?)";

        try (
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, id);
            ps.setString(2, name);
            ps.setString(3, category);
            ps.setDouble(4, price);
            ps.setInt(5, quantity);

            return ps.executeUpdate() == 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Update stock
    public static boolean updateStock(String productId, int quantity) {

        String sql = "UPDATE products SET quantity=? WHERE product_id=?";

        try (
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setInt(1, quantity);
            ps.setString(2, productId);

            return ps.executeUpdate() == 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Get product by ID
    public static String getProductById(String productId) {

        String sql = "SELECT * FROM products WHERE product_id=?";

        try (
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, productId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString("product_id") + "|" +
                       rs.getString("name") + "|" +
                       rs.getString("category") + "|" +
                       rs.getDouble("price") + "|" +
                       rs.getInt("quantity");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Get all products (for table)
    public static List<String> getAllProducts() {

        List<String> list = new ArrayList<>();
        String sql = "SELECT * FROM products";

        try (
            Connection con = DBConnection.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql)
        ) {
            while (rs.next()) {
                list.add(
                    rs.getString("product_id") + "|" +
                    rs.getString("name") + "|" +
                    rs.getString("category") + "|" +
                    rs.getDouble("price") + "|" +
                    rs.getInt("quantity")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
