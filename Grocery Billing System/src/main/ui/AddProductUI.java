package ui;

import javax.swing.*;
import java.awt.*;
import service.ProductService;

public class AddProductUI extends JFrame {

    JTextField id = new JTextField();
    JTextField name = new JTextField();
    JTextField category = new JTextField();
    JTextField price = new JTextField();
    JTextField qty = new JTextField();

    public AddProductUI() {

        setTitle("Add Product");
        setSize(420, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(UIStyle.BG_COLOR);

        JLabel title = new JLabel("ADD PRODUCT", JLabel.CENTER);
        title.setFont(UIStyle.TITLE_FONT);
        title.setForeground(Color.WHITE);

        JPanel header = new JPanel();
        header.setBackground(UIStyle.HEADER_COLOR);
        header.add(title);

        JPanel form = new JPanel(new GridLayout(5,2,12,12));
        form.setBorder(BorderFactory.createEmptyBorder(30,40,30,40));
        form.setBackground(Color.WHITE);

        form.add(new JLabel("Product ID"));
        form.add(id);
        form.add(new JLabel("Name"));
        form.add(name);
        form.add(new JLabel("Category"));
        form.add(category);
        form.add(new JLabel("Price"));
        form.add(price);
        form.add(new JLabel("Quantity"));
        form.add(qty);

        JButton addBtn = new JButton("Add Product");
        JButton backBtn = new JButton("Back");

        styleBtn(addBtn);
        styleBtn(backBtn);

        JPanel btns = new JPanel(new GridLayout(1,2,10,10));
        btns.setBackground(Color.WHITE);
        btns.add(addBtn);
        btns.add(backBtn);

        addBtn.addActionListener(e -> {
            try {
                boolean ok = ProductService.addProduct(
                    id.getText(), name.getText(), category.getText(),
                    Double.parseDouble(price.getText()),
                    Integer.parseInt(qty.getText())
                );
                JOptionPane.showMessageDialog(this,
                    ok ? "Product Added Successfully" : "Failed to Add Product");
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
