package ui;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class BillingUI extends JFrame {

    DefaultTableModel model;
    double grandTotal = 0;

    public BillingUI() {
        setTitle("Billing Counter");
        setSize(700,500);
        setLocationRelativeTo(null);

        model = new DefaultTableModel(
            new String[]{"Product","Qty","Price","Total"},0
        );
        JTable table = new JTable(model);

        JButton addBtn = new JButton("Add Item");
        JButton generateBtn = new JButton("Generate Bill");

        JLabel totalLabel = new JLabel("Total: 0");

        JPanel bottom = new JPanel();
        bottom.add(addBtn);
        bottom.add(generateBtn);
        bottom.add(totalLabel);

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        setVisible(true);
    }
}
