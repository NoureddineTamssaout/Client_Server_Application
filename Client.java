
package ClientServer;
import java.io.*;
import java.net.*;
import java.sql.*;
import javax.swing.*;

public class Client {

    public static void main(String[] args) {
        
        String nom = JOptionPane.showInputDialog("Type your last name ");
        String prenom = JOptionPane.showInputDialog("Type your first name : ");
        String email = JOptionPane.showInputDialog("Type your email address : ");
        String password = JOptionPane.showInputDialog("Type your password: ");

        try {
            // Connecting to the database
            Class.forName("com.mysql.cj.jdbc.Driver");
            // inserting data
            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/clients", "root", "")) {
                
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Utilisateur (Nom, Prenom, Email, Password) VALUES (?, ?, ?, ?)");
                preparedStatement.setString(1, nom);
                preparedStatement.setString(2, prenom);
                preparedStatement.setString(3, email);
                preparedStatement.setString(4, password);
                int result = preparedStatement.executeUpdate();
                
                // closing the connection to the db
                preparedStatement.close();
            }
            
            JOptionPane.showMessageDialog(null, "Compte créé avec succès !");
        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Erreur : " + e.getMessage());
        }
    }

    void setVisible(boolean b) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    boolean isVisible() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    void setDefaultCloseOperation(int EXIT_ON_CLOSE) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
