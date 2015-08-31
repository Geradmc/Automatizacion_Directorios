/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.io.Serializable;
import Conexion.Conexion;
import java.sql.SQLException;
/**
 *
 * @author Gerardo
 */
public class Cover implements Serializable{
    private int id_album;
    private String Carpeta;

    public int getId_album() {
        return id_album;
    }

    public void setId_album(int id_album) {
        this.id_album = id_album;
    }

    public String getCarpeta() {
        return Carpeta;
    }

    public void setCarpeta(String Carpeta) {
        this.Carpeta = Carpeta;
    }
    
    public int agregarCover(Cover cov){
        int resultado=0;
        Conexion con = new Conexion();
        resultado = con.insertar("INSERT INTO covers VALUES('"+cov.getId_album()+"','"+cov.getCarpeta()+"');");
       try {
           con.salir();
       } catch (SQLException ex) {
           System.out.println(ex.getMessage());
       }
        return resultado;
    }
    
    public int borrarCover(int id_album, String Carpeta){
        int resultado=0;
        Conexion con = new Conexion();
        resultado=con.borrar("DELETE FROM covers WHERE id_album='"+id_album+"' && Carpeta='"+Carpeta+"';");
        try {
            con.salir();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return resultado;
    }
    
    public int borrarCover(int id_album){
        int resultado=0;
        Conexion con = new Conexion();
        resultado=con.borrar("DELETE FROM covers WHERE id_album='"+id_album+"';");
        try {
            con.salir();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return resultado;
    }
}
