package ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import service.BillingService;
import service.ProductService;

public class BillingUI extends JFrame {

    DefaultTableModel model;
    String billId;

    JLabel subtotalLabel = new JLabel("₹0.00");
    JLabel gstLabel = new JLabel("₹0.00");
    JLabel discountShowLabel = new JLabel("₹0.00");
    JLabel finalTotalLabel = new JLabel("₹0.00");

    JTextField discountPercentField = new JTextField();
    JTextField discountAmountField = new JTextField();

    public BillingUI() {

        billId = BillingService.generateId("B");

        setTitle("Billing Counter");
        setSize(1050, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("BILLING COUNTER", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE);

        JPanel header = new JPanel();
        header.setBackground(new Color(30, 41, 59));
        header.add(title);
        add(header, BorderLayout.NORTH);

        if (!BillingService.createBill(billId)) {
            JOptionPane.showMessageDialog(this, "Bill creation failed");
            dispose();
            return;
        }

        JPanel left = new JPanel(new GridLayout(6, 1, 10, 10));
        left.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField productIdField = new JTextField();
        JTextField qtyField = new JTextField();
        JButton addBtn = new JButton("Add Item");
        JButton finishBtn = new JButton("Finalize Bill");

        left.add(new JLabel("Product ID"));
        left.add(productIdField);
        left.add(new JLabel("Quantity"));
        left.add(qtyField);
        left.add(addBtn);
        left.add(finishBtn);
        add(left, BorderLayout.WEST);

        model = new DefaultTableModel(
            new String[]{"Product", "Qty", "Price", "Total"}, 0
        );
        JTable table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel summary = new JPanel(new GridLayout(6, 2, 6, 6));
        summary.setBorder(BorderFactory.createTitledBorder("Bill Summary"));

        summary.add(new JLabel("Subtotal"));
        summary.add(subtotalLabel);

        summary.add(new JLabel("GST (18%)"));
        summary.add(gstLabel);

        summary.add(new JLabel("Discount (%)"));
        summary.add(discountPercentField);

        summary.add(new JLabel("Discount (₹)"));
        summary.add(discountAmountField);

        summary.add(new JLabel("Discount Applied"));
        summary.add(discountShowLabel);

        summary.add(new JLabel("Final Amount"));
        summary.add(finalTotalLabel);

        add(summary, BorderLayout.EAST);

        addBtn.addActionListener(e -> {

            String pid = productIdField.getText().trim();
            int qty;

            try {
                qty = Integer.parseInt(qtyField.getText().trim());
                if (qty <= 0) throw new Exception();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid quantity");
                return;
            }

            String product = ProductService.getProductById(pid);
            if (product == null) {
                JOptionPane.showMessageDialog(this, "Product not found");
                return;
            }

            String[] p = product.split("\\|");
            String name = p[1];
            double price = Double.parseDouble(p[3]);

            boolean added = BillingService.addItemToBill(
                BillingService.generateId("I"), billId, pid, qty
            );

            if (!added) {
                JOptionPane.showMessageDialog(this, "Out of stock");
                return;
            }

            model.addRow(new Object[]{name, qty, price, price * qty});
            updateSummary();

            productIdField.setText("");
            qtyField.setText("");
        });

        finishBtn.addActionListener(e -> {

            double subtotal = BillingService.calculateSubtotal(billId);
            double discount = getDiscount(subtotal);

            BillingService.finalizeBill(billId, discount);

            new BillPrintUI(billId);   // SHOW & PRINT BILL
            dispose();
        });

        setVisible(true);
    }

    private double getDiscount(double subtotal) {
        try {
            if (!discountPercentField.getText().isEmpty()) {
                return subtotal * Double.parseDouble(discountPercentField.getText()) / 100;
            }
            if (!discountAmountField.getText().isEmpty()) {
                return Double.parseDouble(discountAmountField.getText());
            }
        } catch (Exception ignored) {}
        return 0;
    }

    private void updateSummary() {

        double subtotal = BillingService.calculateSubtotal(billId);
        double gst = subtotal * 0.18;
        double discount = getDiscount(subtotal);
        double finalTotal = subtotal + gst - discount;
        if (finalTotal < 0) finalTotal = 0;

        subtotalLabel.setText("₹" + String.format("%.2f", subtotal));
        gstLabel.setText("₹" + String.format("%.2f", gst));
        discountShowLabel.setText("-₹" + String.format("%.2f", discount));
        finalTotalLabel.setText("₹" + String.format("%.2f", finalTotal));
    }
}
