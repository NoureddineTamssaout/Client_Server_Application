package ClientServer;
/**
 *
 * @author noureddine
 */
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class Search extends JFrame implements ActionListener {

    private JTextField keywordField;
    private JButton searchButton;
    private JTable resultTable;
    private DefaultTableModel tableModel;

    public Search() {
        super("Searching for a user");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel keywordPanel = new JPanel();
        JLabel keywordLabel = new JLabel("Keyword:");
        keywordField = new JTextField(20);
        searchButton = new JButton("Search");
        searchButton.addActionListener(this);
        keywordPanel.add(keywordLabel);
        keywordPanel.add(keywordField);
        keywordPanel.add(searchButton);

        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BorderLayout());
        tableModel = new DefaultTableModel(new Object[]{"Cle_Utilisa", "Nom", "Prenom", "Email"}, 0);
        resultTable = new JTable(tableModel);
        resultTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        resultTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        resultTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        resultTable.getColumnModel().getColumn(3).setPreferredWidth(150);
        JScrollPane scrollPane = new JScrollPane(resultTable);
        resultPanel.add(scrollPane, BorderLayout.CENTER);

        getContentPane().add(keywordPanel, BorderLayout.NORTH);
        getContentPane().add(resultPanel, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchButton) {
            String keyword = keywordField.getText();
            if (!keyword.isEmpty()) {
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/clients", "root", "");
                    PreparedStatement stmt = con.prepareStatement("SELECT * FROM utilisateur WHERE Nom LIKE ? OR Prenom LIKE ? OR Email LIKE ?");
                    stmt.setString(1, "%" + keyword + "%");
                    stmt.setString(2, "%" + keyword + "%");
                    stmt.setString(3, "%" + keyword + "%");
                    ResultSet rs = stmt.executeQuery();
                    ArrayList<Object[]> data = new ArrayList<>();
                    while (rs.next()) {
                        Object[] row = new Object[]{
                            rs.getInt("Cle_Utilisa"),
                            rs.getString("Nom"),
                            rs.getString("Prenom"),
                            rs.getString("Email")
                        };
                        data.add(row);
                    }
                    tableModel.setRowCount(0);
                    for (Object[] row : data) {
                        tableModel.addRow(row);
                    }
                    con.close();
                } catch (ClassNotFoundException | SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Erreur while searching" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Give a keyword to search ! ", "Attention", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        Search search = new Search();
    }
}
