package connection059;

import java.sql.*;


public class DBConnect {
    public Connection conn;
    public Statement stat;
    public ResultSet result;
    public PreparedStatement pstat;

    public DBConnect(){

    try {
        String user = "sa";
        String pass = "polman";
        String url = "jdbc:sqlserver://DESKTOP-BCLQG20:1433;databaseName = Library059";
        conn = DriverManager.getConnection(url,user,pass);
        stat = conn.createStatement();
    }
    catch (Exception e){
        System.out.println("Error : "+e);
    }

    }

    public static void main(String[] args) {
        DBConnect connection = new DBConnect();
        System.out.println("Berhasil");
    }
}
