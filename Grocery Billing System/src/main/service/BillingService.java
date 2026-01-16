package service;

import db.DBConnection;
import java.sql.*;
import java.util.UUID;

public class BillingService {

    public static String generateId(String prefix) {
        return prefix + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }

    // Create bill
    public static boolean createBill(String billId) {

        String sql =
            "INSERT INTO bills (bill_id, subtotal, gst, discount, total_amount) " +
            "VALUES (?, 0, 0, 0, 0)";

        try (
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, billId);
            return ps.executeUpdate() == 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Add item
    public static boolean addItemToBill(
            String itemId, String billId, String productId, int qty) {

        try (Connection con = DBConnection.getConnection()) {

            PreparedStatement ps1 = con.prepareStatement(
                "SELECT price, quantity FROM products WHERE product_id=?"
            );
            ps1.setString(1, productId);
            ResultSet rs = ps1.executeQuery();

            if (!rs.next()) return false;

            double price = rs.getDouble("price");
            int stock = rs.getInt("quantity");
            if (qty > stock) return false;

            double itemTotal = price * qty;

            PreparedStatement ps2 = con.prepareStatement(
                "INSERT INTO bill_items " +
                "(item_id, bill_id, product_id, quantity, price, item_total) " +
                "VALUES (?, ?, ?, ?, ?, ?)"
            );
            ps2.setString(1, itemId);
            ps2.setString(2, billId);
            ps2.setString(3, productId);
            ps2.setInt(4, qty);
            ps2.setDouble(5, price);
            ps2.setDouble(6, itemTotal);
            ps2.executeUpdate();

            PreparedStatement ps3 = con.prepareStatement(
                "UPDATE products SET quantity=? WHERE product_id=?"
            );
            ps3.setInt(1, stock - qty);
            ps3.setString(2, productId);
            ps3.executeUpdate();

            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Subtotal
    public static double calculateSubtotal(String billId) {

        try (
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                "SELECT SUM(item_total) FROM bill_items WHERE bill_id=?"
            )
        ) {
            ps.setString(1, billId);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getDouble(1) : 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Finalize bill (GST 18%)
    public static boolean finalizeBill(String billId, double discount) {

        double subtotal = calculateSubtotal(billId);
        double gst = subtotal * 0.18;
        double total = subtotal + gst - discount;
        if (total < 0) total = 0;

        String sql =
            "UPDATE bills SET subtotal=?, gst=?, discount=?, total_amount=? " +
            "WHERE bill_id=?";

        try (
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setDouble(1, subtotal);
            ps.setDouble(2, gst);
            ps.setDouble(3, discount);
            ps.setDouble(4, total);
            ps.setString(5, billId);
            return ps.executeUpdate() == 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
