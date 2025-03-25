package hex.util;
 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
 
public class DBUtil {
private static final String URL="jdbc:mysql://localhost:3306/carrental";
private static final String USER="root";
private static final String PASSWORD="Hexaware@12345";
public static Connection getConnection()throws SQLException{
	 return DriverManager.getConnection(URL,USER,PASSWORD);
}
}