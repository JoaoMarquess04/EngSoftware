import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class SistemaAcademico {

    private static final String URL = "jdbc:mysql://localhost/escola";
    private static final String USER = "root";
    private static final String PASSWORD = "sua_senha";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }

    private static class LoginFrame extends JFrame {
        private JTextField usernameField;
        private JPasswordField passwordField;

        public LoginFrame() {
            setTitle("Login");
            setSize(300, 150);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);

            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(3, 2));

            panel.add(new JLabel("Username:"));
            usernameField = new JTextField();
            panel.add(usernameField);

            panel.add(new JLabel("Password:"));
            passwordField = new JPasswordField();
            panel.add(passwordField);

            JButton loginButton = new JButton("Login");
            loginButton.addActionListener(new LoginButtonListener());
            panel.add(loginButton);

            add(panel);
        }

        private class LoginButtonListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                     Statement statement = connection.createStatement()) {

                    String query = "SELECT * FROM usuarios WHERE username = '" + username + "' AND senha = '" + password + "'";
                    ResultSet resultSet = statement.executeQuery(query);

                    if (resultSet.next()) {
                        // Login successful, show the main application
                        new MainFrame(resultSet.getInt("id")).setVisible(true);
                        dispose();  // Close the login window
                    } else {
                        JOptionPane.showMessageDialog(LoginFrame.this, "Invalid username or password.");
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(LoginFrame.this, "Database error.");
                }
            }
        }
    }

    private static class MainFrame extends JFrame {
        private int userId;

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
}
