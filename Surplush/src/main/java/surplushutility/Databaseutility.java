package surplushutility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.mysql.cj.jdbc.Driver;

public class Databaseutility {
    Connection con;

    public Connection getDBConnection(String url, String un, String pwd) throws SQLException {
        Driver d=new Driver();
        DriverManager.registerDriver(d);
        return DriverManager.getConnection(url, un, pwd);
    }

    public boolean validateDataEntry(String url, String un, String pwd, String tName, String cName, String data) throws SQLException {
        con=getDBConnection(url,un,pwd);
        Statement s=con.createStatement();
        String query="select * from"+tName+"where"+cName+"='"+data+"';";
        return s.execute(query);
    }

    public ResultSet readDataFromDB(String url, String un, String pwd, String query) throws Exception {
        Connection con=getDBConnection(url, un, pwd);
        Statement s=con.createStatement();
        return s.executeQuery(query);
    }

    public void closeConnection(Connection con) throws Exception {
        con.close();
    }

	public void connectToDB() {
		// TODO Auto-generated method stub
		
	}

	public void closeDB() {
		// TODO Auto-generated method stub
		
	}

	
}


