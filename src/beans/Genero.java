/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import Conexion.Conexion;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author GerardoML
 */
public class Genero implements Serializable{
   private int id_genero;
   private String Nombre;

   private String error;
   
    public int getId_genero() {
        return id_genero;
    }

    public void setId_genero(int id_genero) {
        this.id_genero = id_genero;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
    
    public ArrayList<Genero> obtenerGeneros(){
        ArrayList<Genero> gen = new ArrayList<Genero>();
        Genero g;
        Conexion con = new Conexion();
        ResultSet res;
        res = con.consultar("Select * from genero;");
        try {
            while(res.next()){
                g = new Genero();
                g.setId_genero(Integer.parseInt(res.getString("id_genero")));
                g.setNombre(res.getString("Nombre"));
                gen.add(g);
            }
            con.salir();
        } catch (SQLException ex) {
            error = ex.getMessage();
        }
        return gen;
    }
    
    public int agregarGenero(Genero gen){
        int resultado=0;
        Conexion con = new Conexion();
        resultado = con.insertar("INSERT INTO Genero VALUES('"+gen.getId_genero()+"','"+gen.getNombre()+"');");
       try {
           con.salir();
       } catch (SQLException ex) {
           System.out.println(ex.getMessage());
       }
        return resultado;
    }
    
    public int ObtenerUltimoId(){
        Conexion con = new Conexion();
        ResultSet res=null;
        int id=0;
        res = con.consultar("SELECT id_genero FROM genero ORDER BY id_genero DESC LIMIT 1");
        try {
            if(res.next()){
                id=Integer.parseInt(res.getString("id_genero"));
            }
            con.salir();
        } catch (SQLException ex) {
            error = ex.getMessage();
        }
        return id;
    }
    
    public boolean existeGenero(String gen){
        Conexion con = new Conexion();
        ResultSet res = con.consultar("SELECT * FROM genero WHERE Nombre='"+gen+"'");
        try {
            if(res.next()){
                setId_genero(Integer.parseInt(res.getString("id_genero")));
                setNombre(res.getString("Nombre"));
                con.salir();
                return true;
            } else{ 
                con.salir();
                return false;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return false;
    }
    
    public void relacionGeneroAlbum(int gen, int id_album){
        Conexion con = new Conexion();
        ResultSet res = con.consultar("SELECT * FROM genero WHERE id_genero='"+gen+"'");
        int resultado;
        try {
            if(res.next()){
                resultado=con.insertar("INSERT INTO album_genero VALUES('"+id_album+"','"+gen+"');");
            }
            con.salir();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
