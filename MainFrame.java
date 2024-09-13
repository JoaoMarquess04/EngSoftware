import java.awt.BorderLayout;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;



public class MainFrame extends JFrame {
    private int userId;
    String URL = "jdbc:mysql://127.0.0.1:3306/escola";
    String USER = "root";
    String PASSWORD = "bemvindos123";

    public MainFrame(int userId) {
        this.userId = userId;
        setTitle("Main Application");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JTable table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement()) {

            String query = "SELECT m.nome, nf.nota, nf.falta " +
                           "FROM notas_faltas nf " +
                           "JOIN materias m ON nf.materia_id = m.id " +
                           "WHERE nf.usuario_id = " + userId;
            ResultSet resultSet = statement.executeQuery(query);

            table.setModel(new ResultSetTableModel(resultSet));

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(MainFrame.this, "Database error.");
        }

        add(panel);
    }
}

