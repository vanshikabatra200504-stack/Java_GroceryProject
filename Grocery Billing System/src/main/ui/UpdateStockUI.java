package ui;

import javax.swing.*;
import java.awt.*;
import service.ProductService;

public class UpdateStockUI extends JFrame {

    JTextField productId = new JTextField();
    JTextField quantity = new JTextField();

    public UpdateStockUI() {

        setTitle("Update Stock");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(UIStyle.BG_COLOR);

        JLabel title = new JLabel("UPDATE STOCK", JLabel.CENTER);
        title.setFont(UIStyle.TITLE_FONT);
        title.setForeground(Color.WHITE);

        JPanel header = new JPanel();
        header.setBackground(UIStyle.HEADER_COLOR);
        header.add(title);

        JPanel form = new JPanel(new GridLayout(2,2,12,12));
        form.setBorder(BorderFactory.createEmptyBorder(40,40,40,40));
        form.setBackground(Color.WHITE);

        form.add(new JLabel("Product ID"));
        form.add(productId);
        form.add(new JLabel("New Quantity"));
        form.add(quantity);

        JButton updateBtn = new JButton("Update");
        JButton backBtn = new JButton("Back");

        styleBtn(updateBtn);
        styleBtn(backBtn);

        JPanel btns = new JPanel(new GridLayout(1,2,10,10));
        btns.setBackground(Color.WHITE);
        btns.add(updateBtn);
        btns.add(backBtn);

        updateBtn.addActionListener(e -> {
            try {
                boolean ok = ProductService.updateStock(
                    productId.getText(),
                    Integer.parseInt(quantity.getText())
                );
                JOptionPane.showMessageDialog(this,
                    ok ? "Stock Updated" : "Update Failed");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid Input");
            }
        });

        backBtn.addActionListener(e -> {
            dispose();
            new AdminDashboard();
        });

        main.add(header, BorderLayout.NORTH);
        main.add(form, BorderLayout.CENTER);
        main.add(btns, BorderLayout.SOUTH);
        add(main);

        setVisible(true);
    }

    private void styleBtn(JButton btn) {
        btn.setFont(UIStyle.BUTTON_FONT);
        btn.setBackground(UIStyle.BUTTON_COLOR);
        btn.setForeground(Color.WHITE);
    }
}
