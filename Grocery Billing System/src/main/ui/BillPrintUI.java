package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.print.*;
import db.DBConnection;
import java.sql.*;

public class BillPrintUI extends JFrame implements Printable {

    JTextArea billArea;
    String billId;

    public BillPrintUI(String billId) {

        this.billId = billId;

        setTitle("Bill Preview");
        setSize(420, 620);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        billArea = new JTextArea();
        billArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        billArea.setEditable(false);

        generateBillText();

        JScrollPane scroll = new JScrollPane(billArea);

        // 🔘 Buttons panel
        JButton printBtn = new JButton("Print Bill");
        JButton newBillBtn = new JButton("New Bill");

        printBtn.addActionListener(e -> printBill());

        newBillBtn.addActionListener(e -> {
            dispose();              // Close bill preview
            new BillingUI();        // Start new bill
        });

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 8));
        btnPanel.add(printBtn);
        btnPanel.add(newBillBtn);

        add(scroll, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void generateBillText() {

        StringBuilder sb = new StringBuilder();

        sb.append("SDM GROCERY MART\n");
        sb.append("Sector 15, Delhi\n");
        sb.append("Phone: 9876543210\n");
        sb.append("--------------------------------\n");

        try (Connection con = DBConnection.getConnection()) {

            PreparedStatement psBill = con.prepareStatement(
                "SELECT subtotal, gst, discount, total_amount, bill_date " +
                "FROM bills WHERE bill_id=?"
            );
            psBill.setString(1, billId);
            ResultSet rb = psBill.executeQuery();
            if (!rb.next()) return;

            sb.append("Bill ID: ").append(billId).append("\n");
            sb.append("Date: ").append(rb.getTimestamp("bill_date")).append("\n");
            sb.append("--------------------------------\n");

            sb.append(String.format("%-10s %5s %7s %7s\n",
                    "Item", "Qty", "Price", "Total"));

            PreparedStatement psItems = con.prepareStatement(
                "SELECT p.name, bi.quantity, bi.price, bi.item_total " +
                "FROM bill_items bi JOIN products p " +
                "ON bi.product_id=p.product_id WHERE bi.bill_id=?"
            );
            psItems.setString(1, billId);
            ResultSet ri = psItems.executeQuery();

            while (ri.next()) {
                sb.append(String.format("%-10s %5d %7.2f %7.2f\n",
                        ri.getString("name"),
                        ri.getInt("quantity"),
                        ri.getDouble("price"),
                        ri.getDouble("item_total")));
            }

            sb.append("--------------------------------\n");
            sb.append(String.format("Subtotal:      %.2f\n", rb.getDouble("subtotal")));
            sb.append(String.format("GST (18%%):     %.2f\n", rb.getDouble("gst")));
            sb.append(String.format("Discount:     -%.2f\n", rb.getDouble("discount")));
            sb.append("--------------------------------\n");
            sb.append(String.format("FINAL TOTAL:   %.2f\n", rb.getDouble("total_amount")));
            sb.append("--------------------------------\n");
            sb.append("Thank you for shopping!\n");

            billArea.setText(sb.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printBill() {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(this);

        if (job.printDialog()) {
            try {
                job.print();
            } catch (PrinterException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int print(Graphics g, PageFormat pf, int pageIndex) {
        if (pageIndex > 0) return NO_SUCH_PAGE;
        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(pf.getImageableX(), pf.getImageableY());
        billArea.printAll(g);
        return PAGE_EXISTS;
    }
}
