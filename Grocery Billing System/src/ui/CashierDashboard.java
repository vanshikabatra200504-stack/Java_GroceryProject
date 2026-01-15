package ui;
import javax.swing.*;

public class CashierDashboard extends JFrame {

    public CashierDashboard() {
        setTitle("Cashier Dashboard");
        setSize(400,300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JButton billingBtn = new JButton("Start Billing");
        billingBtn.addActionListener(e -> new BillingUI());

        add(billingBtn);
        setVisible(true);
    }
}
