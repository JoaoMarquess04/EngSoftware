import  java.sql.Connection;
import  java.sql.DriverManager;

public class ConnFactory {

    private static final String URL = "jdbc:mysql://localhost:3306/escola";
    private static final String USER = "root";
    private static final String SENHA = "Joanajoao1973";

    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String ERROBD = "Erro na conexão com o Banco de Dados: ";
    

    // Conecta com o banco de dados e retorna a conexao ****************
    public static Connection conectar(){
        try{
        return DriverManager.getConnection(URL + "?user=" + USER + "&password=" + SENHA);
        }
        catch (Exception e){
        return null;
        }
    }
    
    // desconecta com o banco de dados ********************************
    public static void desconectar(Connection conn) {
        try{
            conn.close();
        }
        catch(Exception e){
            ;
        }
    }   
}