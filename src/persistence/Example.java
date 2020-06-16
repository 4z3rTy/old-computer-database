package persistence;
import java.sql.*;

public class Example{
	
	
	public static boolean ignoreSQLException(String sqlState) {

	    if (sqlState == null) {
	        System.out.println("The SQL state is not defined!");
	        return false;
	    }

	    // X0Y32: Jar file already exists in schema
	    if (sqlState.equalsIgnoreCase("X0Y32"))
	        return true;

	    // 42Y55: Table already exists in schema
	    if (sqlState.equalsIgnoreCase("42Y55"))
	        return true;

	    return false;
	}	
	public static void printSQLException(SQLException ex) {

	    for (Throwable e : ex) {
	        if (e instanceof SQLException) {
	            if (ignoreSQLException(
	                ((SQLException)e).
	                getSQLState()) == false) {

	                e.printStackTrace(System.err);
	                System.err.println("SQLState: " +
	                    ((SQLException)e).getSQLState());

	                System.err.println("Error Code: " +
	                    ((SQLException)e).getErrorCode());

	                System.err.println("Message: " + e.getMessage());

	                Throwable t = ex.getCause();
	                while(t != null) {
	                    System.out.println("Cause: " + t);
	                    t = t.getCause();
	                }
	            }
	        }
	    }
	}
	
	
	
	
public void connectToAndQueryDatabase(String username , String password){
	Connection con = null;
	try {
		con = DriverManager.getConnection(
		        "jdbc:mysql://localhost:3306/computer-database-db?serverTimezone=UTC",
		        username, password);
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

Statement stmt;
try {
	stmt = con.createStatement();

ResultSet rs = stmt.executeQuery("SELECT * FROM computer WHERE ID=42");
if(rs.next())
{
String computer=rs.getString("name");
System.out.println(computer);
}
/*
while (rs.next()) {
int x = rs.getInt("ID");
String s = rs.getString("NAME");
int f = rs.getInt("COMPANY_ID");
			} */
} catch (SQLException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
}



public static void viewCompany(Connection con, String dbName)
	    throws SQLException {

	    Statement stmt = null;
	    String query =
	        "select id, name from company";

	    try {
	        stmt = con.createStatement();
	        ResultSet rs = stmt.executeQuery(query);
	        while (rs.next()) {
	            String companyName = rs.getString("name");
	            int companyID = rs.getInt("id");
	            System.out.println(companyName + "\t" 
	                           			+ companyID);
	        }
	    } catch (SQLException e ) {
	        printSQLException(e);
	    } finally {
	        if (stmt != null) { stmt.close(); }
	    }
}
public static void viewComputer(Connection con, String dbName)
	    throws SQLException {

	    Statement stmt = null;
	    String query =
	        "select ID, NAME, COMPANY_ID from computer";

	    try {
	        stmt = con.createStatement();
	        ResultSet rs = stmt.executeQuery(query);
	        while (rs.next()) {
	            String computerName = rs.getString("NAME");
	            int ID = rs.getInt("ID");
	            int companyID = rs.getInt("COMPANY_ID");
	            System.out.println(computerName + "\t" + ID +
	                               "\t" + companyID);
	        }
	    } catch (SQLException e ) {
	        printSQLException(e);
	    } finally {
	        if (stmt != null) { stmt.close(); }
	    }
}



public void insertComputer(Connection con,String computerName, int companyID,
        int computerID, Date intro, Date disco)
throws SQLException {
String dbName="computer";

Statement stmt=null;
try {
stmt = con.createStatement(
ResultSet.TYPE_SCROLL_SENSITIVE,
ResultSet.CONCUR_UPDATABLE);

ResultSet uprs = stmt.executeQuery(
"SELECT * FROM " + dbName);

uprs.moveToInsertRow();
uprs.updateString("NAME", computerName);
uprs.updateInt("COMPANY_ID", companyID);
uprs.updateDate("INTRODUCED", intro);
uprs.updateDate("DISCONTINUED", disco);
uprs.updateInt("ID", computerID);

uprs.insertRow();
uprs.beforeFirst();
} catch (SQLException e ) {
printSQLException(e);
} finally {
if (stmt != null) { stmt.close(); 
			}}}



public static void main(String []args)
		{
	Connection con;
	long millis=System.currentTimeMillis();  
    java.sql.Date intro=new java.sql.Date(millis);  
	Date disco=new java.sql.Date(millis);
	try {
		con = DriverManager.getConnection(
		        "jdbc:mysql://localhost:3306/computer-database-db?serverTimezone=UTC",
		        "root", "root");
	
	String dbName="company";
			Example r= new Example();
			r.connectToAndQueryDatabase("root", "root");
			
			viewCompany(con, dbName);
			viewComputer(con,dbName);
			r.insertComputer(con,"XXX", 26, 666,intro, disco);
			viewComputer(con,dbName);
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

}	
}

