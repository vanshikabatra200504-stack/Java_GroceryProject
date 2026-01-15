package ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class BillingUI extends JFrame {

    DefaultTableModel model;
    JLabel totalLabel;
    double grandTotal = 0;

    public BillingUI() {
        setTitle("Billing Counter");
        setSize(900,520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(UIStyle.BG_COLOR);

        // Header
        JLabel title = new JLabel("BILLING COUNTER", JLabel.CENTER);
        title.setFont(UIStyle.TITLE_FONT);
        title.setForeground(Color.WHITE);

        JPanel header = new JPanel();
        header.setBackground(UIStyle.HEADER_COLOR);
        header.add(title);

        // Top form
        JPanel top = new JPanel(new GridLayout(2,4,10,10));
        top.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        top.setBackground(UIStyle.BG_COLOR);

        JTextField productField = new JTextField();
        JTextField qtyField = new JTextField();
        JButton addBtn = new JButton("Add Item");

        addBtn.setFont(UIStyle.BUTTON_FONT);
        addBtn.setBackground(UIStyle.BUTTON_COLOR);
        addBtn.setForeground(Color.WHITE);

        top.add(new JLabel("Product Name"));
        top.add(productField);
        top.add(new JLabel("Quantity"));
        top.add(qtyField);
        top.add(new JLabel(""));
        top.add(addBtn);

        // Table
        model = new DefaultTableModel(
            new String[]{"Product","Qty","Price","Total"}, 0
        );
        JTable table = new JTable(model);
        table.setRowHeight(24);

        JScrollPane scroll = new JScrollPane(table);

        // Bottom
        totalLabel = new JLabel("Total: ₹0.00");
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        totalLabel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(totalLabel, BorderLayout.EAST);

        // Add everything
        main.add(header, BorderLayout.NORTH);
        main.add(top, BorderLayout.CENTER);
        main.add(scroll, BorderLayout.SOUTH);

        add(main, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        setVisible(true);
    }
}
