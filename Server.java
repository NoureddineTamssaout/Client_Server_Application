
package ClientServer;
import java.io.*;
import java.net.*;
import java.sql.*;

public class Server {

    public static void main(String[] args) {
        try {
            // openning the server with the port 12345
            //
            ServerSocket serverSocket = new ServerSocket(12345);
            System.out.println("Serveur démarré sur le port 12345");

            while (true) {
                // waiting for a connection
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connecté : " + clientSocket.getInetAddress().getHostName());

                // reading the data
                ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
                String email = (String) in.readObject();
                String password = (String) in.readObject();
                System.out.println("Email : " + email);
                System.out.println("Password : " + password);

                // checking the data
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/clients", "root", "");
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Utilisateur WHERE Email = ? AND Password = ?");
                preparedStatement.setString(1, email);
                preparedStatement.setString(2, password);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    
                    PreparedStatement updateStatement = connection.prepareStatement("UPDATE Utilisateur SET connecte_ou_non = ?, @IP = ? WHERE Email = ?");
                    updateStatement.setBoolean(1, true);
                    updateStatement.setString(2, clientSocket.getInetAddress().getHostName());
                    updateStatement.setString(3, email);
                    int updateResult = updateStatement.executeUpdate();
                    System.out.println("Connexion réussie");

                    // sending the confirmation of the connection to the client
                    ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                    out.writeObject("Connexion réussie");
                } else {
                    System.out.println("Email ou mot de passe incorrect");

                    //error message
                    ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                    out.writeObject("Email ou mot de passe incorrect");
                }

                // closing the connection
                preparedStatement.close();
                connection.close();
                clientSocket.close();
            }
        } catch (IOException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
