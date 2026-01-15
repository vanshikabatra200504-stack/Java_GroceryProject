package ui;
import javax.swing.*;

public class AdminDashboard extends JFrame {

    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setSize(500,400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JButton addProduct = new JButton("Add Product");
        JButton updateStock = new JButton("Update Stock");
        JButton viewSales = new JButton("View Sales");

        JPanel panel = new JPanel();
        panel.add(addProduct);
        panel.add(updateStock);
        panel.add(viewSales);

        add(panel);
        setVisible(true);
    }
}
