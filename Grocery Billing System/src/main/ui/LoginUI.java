package ui;

import java.awt.*;
import javax.swing.*;
import service.AuthService;

public class LoginUI extends JFrame {

    public LoginUI() {
        setTitle("Grocery Billing System");
        setSize(420,320);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(UIStyle.BG_COLOR);

        JLabel title = new JLabel("LOGIN", JLabel.CENTER);
        title.setFont(UIStyle.TITLE_FONT);
        title.setForeground(Color.WHITE);

        JPanel header = new JPanel();
        header.setBackground(UIStyle.HEADER_COLOR);
        header.add(title);

        JPanel form = new JPanel(new GridLayout(4,2,12,12));
        form.setBorder(BorderFactory.createEmptyBorder(30,40,30,40));
        form.setBackground(UIStyle.BG_COLOR);

        JTextField username = new JTextField();
        JPasswordField password = new JPasswordField();

        JButton loginBtn = new JButton("Login");
        loginBtn.setFont(UIStyle.BUTTON_FONT);
        loginBtn.setBackground(UIStyle.BUTTON_COLOR);
        loginBtn.setForeground(Color.WHITE);

        form.add(new JLabel("Username"));
        form.add(username);
        form.add(new JLabel("Password"));
        form.add(password);
        form.add(new JLabel(""));
        form.add(loginBtn);

        loginBtn.addActionListener(e -> {

            String user = username.getText();
            String pass = new String(password.getPassword());

            String role = AuthService.login(user, pass);

            if (role == null) {
                JOptionPane.showMessageDialog(
                    this,
                    "Invalid Username or Password",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            dispose();

            if (role.equals("ADMIN")) {
                new AdminDashboard();
            } else {
                new CashierDashboard();
            }
        });

        main.add(header, BorderLayout.NORTH);
        main.add(form, BorderLayout.CENTER);
        add(main);

        setVisible(true);
    }
}
