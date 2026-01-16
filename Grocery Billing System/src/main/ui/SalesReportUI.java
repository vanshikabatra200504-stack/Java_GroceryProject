package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import db.DBConnection;

public class SalesReportUI extends JFrame {

    DefaultTableModel model;

    public SalesReportUI() {

        setTitle("Sales Report");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(UIStyle.BG_COLOR);

        JLabel title = new JLabel("SALES REPORT", JLabel.CENTER);
        title.setFont(UIStyle.TITLE_FONT);
        title.setForeground(Color.WHITE);

        JPanel header = new JPanel();
        header.setBackground(UIStyle.HEADER_COLOR);
        header.add(title);

        model = new DefaultTableModel(
            new String[]{"Bill ID", "Total Amount", "Date"}, 0
        );
        JTable table = new JTable(model);
        loadData();

        JButton backBtn = new JButton("Back");
        backBtn.setFont(UIStyle.BUTTON_FONT);
        backBtn.setBackground(UIStyle.BUTTON_COLOR);
        backBtn.setForeground(Color.WHITE);

        backBtn.addActionListener(e -> {
            dispose();
            new AdminDashboard();
        });

        main.add(header, BorderLayout.NORTH);
        main.add(new JScrollPane(table), BorderLayout.CENTER);
        main.add(backBtn, BorderLayout.SOUTH);
        add(main);

        setVisible(true);
    }

    private void loadData() {
        try (
            Connection con = DBConnection.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(
                "SELECT bill_id, total_amount, bill_date FROM bills")
        ) {
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("bill_id"),
                    rs.getDouble("total_amount"),
                    rs.getDate("bill_date")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
