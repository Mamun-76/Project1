import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.time.LocalDateTime;

public class BankManagementSystem extends JFrame {

    JLabel userLabel, passLabel, balanceLabel, centerImage, depositLabel, withdrawLabel;
    JTextField userTF, depositTF, withdrawTF;
    JPasswordField passPF;
    JButton loginBtn, exitBtn, recoverBtn, depositBtn, withdrawBtn, historyBtn, SummaryBtn;
    JRadioButton rememberMe;
    JCheckBox termsCheck, offersCheck;
    JComboBox<String> combo;
    JPanel panel;

    double balance = 1000.0;
    final double WITHDRAW_LIMIT = 500.0;

    private FileWriter writer;

    public BankManagementSystem() {
        super("Banking System");
        setSize(1080, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(211, 211, 211));

        combo = new JComboBox<>(new String[]{"Customer", "Admin", "Clerk", "New Account"});
        combo.setBounds(50, 150, 290, 30);
        combo.setBackground(Color.WHITE);
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panel.add(combo);

        userLabel = new JLabel("Username:");
        userLabel.setBounds(50, 200, 100, 30);
        userLabel.setForeground(new Color(255, 255, 255));
        panel.add(userLabel);

        userTF = new JTextField();
        userTF.setBounds(160, 200, 180, 30);
        panel.add(userTF);

        passLabel = new JLabel("Password:");
        passLabel.setBounds(50, 250, 100, 30);
        passLabel.setForeground(new Color(255, 255, 255));
        panel.add(passLabel);

        passPF = new JPasswordField();
        passPF.setBounds(160, 250, 180, 30);
        panel.add(passPF);

        rememberMe = new JRadioButton("Remember Me");
        rememberMe.setBounds(50, 300, 290, 30);
        rememberMe.setBackground(new Color(245, 248, 255));
        panel.add(rememberMe);
		
		historyBtn = createButton("History", 750, 150, new Color(32, 201, 151));
		historyBtn.addActionListener(e -> showMessage("Transaction history feature coming soon!"));
        panel.add(historyBtn);

        SummaryBtn = createButton("Summary", 870, 150, new Color(0, 123, 255));
		SummaryBtn.addActionListener(e -> showMessage("Account summary feature coming soon!"));
        panel.add(SummaryBtn);

        loginBtn = createButton("Login", 50, 380, new Color(0, 123, 255));
        loginBtn.addActionListener(e -> showMessage("Login successful! Welcome back."));
        panel.add(loginBtn);

        recoverBtn = createButton("Recover", 160, 380, new Color(32, 201, 151));
        recoverBtn.addActionListener(e -> showMessage("Password recovery instructions sent to your email."));
        panel.add(recoverBtn);

        exitBtn = createButton("Exit", 270, 380, new Color(220, 53, 69));
        exitBtn.addActionListener(e -> {
            showMessage("Goodbye! Application will now close.");
            try {
                if (writer != null) writer.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            System.exit(0);
        });
        panel.add(exitBtn);

        balanceLabel = new JLabel("Current Balance: $" + balance);
        balanceLabel.setBounds(750, 200, 300, 30);
        balanceLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        balanceLabel.setForeground(new Color(255, 255, 255));
        panel.add(balanceLabel);

        depositLabel = new JLabel("Deposit Amount:");
        depositLabel.setBounds(650, 250, 120, 30);
        depositLabel.setForeground(new Color(255, 255, 255));
        panel.add(depositLabel);

        depositTF = new JTextField();
        depositTF.setBounds(755, 250, 160, 30);
        panel.add(depositTF);

        depositBtn = createButton("Deposit", 915, 250, new Color(255, 193, 7));
        depositBtn.addActionListener(e -> {
            try {
                double amount = Double.parseDouble(depositTF.getText());
                if (amount > 0) {
                    balance += amount;
                    updateBalance();
                    showMessage("Successfully deposited $" + amount);
                    logTransaction("Deposited $" + amount);
                    depositTF.setText("");
                } else {
                    showMessage("Please enter a valid amount.");
                }
            } catch (Exception ex) {
                showMessage("Invalid input. Please enter a number.");
            }
        });
        panel.add(depositBtn);

        withdrawLabel = new JLabel("Withdraw Amount:");
        withdrawLabel.setBounds(650, 300, 120, 30);
        withdrawLabel.setForeground(new Color(255, 255, 255));
        panel.add(withdrawLabel);

        withdrawTF = new JTextField();
        withdrawTF.setBounds(755, 300, 160, 30);
        panel.add(withdrawTF);

        withdrawBtn = createButton("Withdraw", 915, 300, new Color(23, 162, 184));
        withdrawBtn.addActionListener(e -> {
            try {
                double amount = Double.parseDouble(withdrawTF.getText());
                if (amount <= 0) showMessage("Invalid amount.");
                else if (amount > balance) showMessage("Insufficient balance.");
                else if (amount > WITHDRAW_LIMIT)
                    showMessage("Withdrawal exceeds limit of $" + WITHDRAW_LIMIT);
                else {
                    balance -= amount;
                    updateBalance();
                    showMessage("Successfully withdrew $" + amount);
                    logTransaction("Withdrew $" + amount);
                    withdrawTF.setText("");
                }
            } catch (Exception ex) {
                showMessage("Invalid input. Please enter a number.");
            }
        });
        panel.add(withdrawBtn);

        termsCheck = new JCheckBox("Accept Terms");
        termsCheck.setBounds(65, 440, 150, 30);
        termsCheck.setBackground(new Color(245, 248, 255));
        panel.add(termsCheck);

        offersCheck = new JCheckBox("Enable Offers");
        offersCheck.setBounds(225, 440, 150, 30);
        offersCheck.setBackground(new Color(245, 248, 255));
        panel.add(offersCheck);

        String[] navItems = {"Accounts", "Credit Cards", "Loans", "Investments", "Rewards", "Advice"};
        Color[] navColors = {
            new Color(0, 123, 255),
            new Color(40, 167, 69),
            new Color(255, 193, 7),
            new Color(23, 162, 184),
            new Color(111, 66, 193),
            new Color(220, 53, 69)
        };
        int x = 130;
        for (int i = 0; i < navItems.length; i++) {
            JButton btn = createButton(navItems[i], x, 50, navColors[i]);
            int finalI = i;
            btn.addActionListener(e -> showMessage(navItems[finalI] + " section opened."));
            panel.add(btn);
            x += 140;
        }
		
		centerImage = new JLabel(new ImageIcon("Images/banking.jpg"));
        centerImage.setBounds(0, 0, 1080, 640);
        panel.add(centerImage);

        add(panel);
        setVisible(true);

        setupFileWriter(); 
    }

    private void setupFileWriter() {
        try {
            File file = new File("History.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            writer = new FileWriter(file, true); 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void logTransaction(String message) {
        try {
            writer.write(LocalDateTime.now() + " - " + message + "\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JButton createButton(String text, int x, int y, Color baseColor) {
        JButton btn = new JButton(text);
        btn.setBounds(x, y, 120, 30);
        btn.setBackground(baseColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));

        Color hoverColor = baseColor.darker();
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(hoverColor);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(baseColor);
            }
        });

        return btn;
    }

    private void updateBalance() {
        balanceLabel.setText("Current Balance: $" + String.format("%.2f", balance));
    }

    private void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }
}