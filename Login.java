package ClientServer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Login extends JFrame implements ActionListener {
    private JTextField txtUser;
    private JPasswordField txtPass;
    private JButton btnLogin, btnCancel;
    private JLabel lblUser, lblPass;
    
    public Login() {
        super("Login");
        
        // Set look and feel to the system look and feel
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | javax.swing.UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
        
        // Define fields
        lblUser = new JLabel("email:");
        lblPass = new JLabel("Password:");
        txtUser = new JTextField(20);
        txtPass = new JPasswordField(20);
        btnLogin = new JButton("Login");
        btnCancel = new JButton("Cancel");
        
        // Define frame
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null);
        
        // Add components to frame
        lblUser.setBounds(10, 10, 80, 25);
        add(lblUser);
        txtUser.setBounds(100, 10, 160, 25);
        add(txtUser);
        lblPass.setBounds(10, 40, 80, 25);
        add(lblPass);
        txtPass.setBounds(100, 40, 160, 25);
        add(txtPass);
        btnLogin.setBounds(10, 80, 80, 25);
        add(btnLogin);
        btnCancel.setBounds(180, 80, 80, 25);
        add(btnCancel);
        
        // Add action listeners to buttons
        btnLogin.addActionListener(this);
        btnCancel.addActionListener(this);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnLogin) {
            String email = txtUser.getText();
            String password = new String(txtPass.getPassword());
            
            // Connect to database
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/clients", "root", "");
                
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM utilisateur WHERE email = '" + email + "' AND password = '" + password + "'");
                
                if (rs.next()) {
                    JOptionPane.showMessageDialog(null, "Login successful!");
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid email or password!");
                }
                
                con.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (e.getSource() == btnCancel) {
            dispose();
        }
    }
    
    public static void main(String[] args) {
        Login login = new Login();
        login.setVisible(true);
    }
}
