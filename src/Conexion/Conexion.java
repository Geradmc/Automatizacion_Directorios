package Conexion;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class Conexion {
    private Connection conn;
    private Statement stm;
    
    public Conexion(){
        try {
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/admin_music?user=Geradmc&password=datos53");
        } catch (SQLException ex) {
            System.out.println("Error: "+ex.getMessage());
        }
    }
    
    public ResultSet consultar(String sql){
        ResultSet rst=null;
        try{
            stm = conn.createStatement();
            rst = stm.executeQuery(sql);
        } catch (SQLException ex) {
            System.out.println("Error: "+ex.getMessage());
        }
        return rst;
    }

    public int modificar(String sql){
        int result=0;
        try{
            stm = conn.createStatement();
            result = stm.executeUpdate(sql);
        } catch (SQLException ex) {
            System.out.println("Error: "+ex.getMessage());
        }
        return result;
    }
    
    public int insertar(String sql){
        int result=0;
        try{
            stm = conn.createStatement();
            result = stm.executeUpdate(sql);
        } catch (SQLException ex) {
            System.out.println("Error: "+ex.getMessage());
        }
        return result;
    }
    
    public int borrar(String sql){
        int result=0;
        try{
            stm = conn.createStatement();
            result = stm.executeUpdate(sql);
        } catch (SQLException ex) {
            System.out.println("Error: "+ex.getMessage());
        }
        return result;
    }

    public void salir() throws SQLException{
        conn.close();
    }
}
