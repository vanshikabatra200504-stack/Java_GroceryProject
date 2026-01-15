package ui;
import javax.swing.*;
import java.awt.*;
//import service.AuthService;

public class LoginUI extends JFrame {

    public LoginUI() {
        setTitle("Grocery Billing System - Login");
        setSize(400,300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(4,2,10,10));
        panel.setBorder(BorderFactory.createEmptyBorder(30,30,30,30));

        JTextField userField = new JTextField();
        JPasswordField passField = new JPasswordField();
        JButton loginBtn = new JButton("Login");

        panel.add(new JLabel("Username"));
        panel.add(userField);
        panel.add(new JLabel("Password"));
        panel.add(passField);
        panel.add(new JLabel(""));
        panel.add(loginBtn);

        add(panel);

        // loginBtn.addActionListener(e -> {
        //     String role = AuthService.login(
        //         userField.getText(),
        //         new String(passField.getPassword())
        //     );

        //     if(role == null) {
        //         JOptionPane.showMessageDialog(this,"Invalid Credentials");
        //     } else {
        //         dispose();
        //         if(role.equals("ADMIN")) new AdminDashboard();
        //         else new CashierDashboard();
        //     }
        // });

        setVisible(true);
    }
}
