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
public class Interprete implements Serializable{
    private int id_interprete;
    private String Nombre;
    private String Informacion;

    private String error;
    
    public int getId_interprete() {
        return id_interprete;
    }

    public void setId_interprete(int id_interprete) {
        this.id_interprete = id_interprete;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getInformacion() {
        return Informacion;
    }

    public void setInformacion(String Informacion) {
        this.Informacion = Informacion;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
    
    public Interprete crearInterprete(int id_intr, String Nombre, String info){
        Interprete intr = new Interprete();
        intr.setId_interprete(id_intr);
        intr.setInformacion(info);
        intr.setNombre(Nombre);
        return intr;
    }
    
    public ArrayList<Interprete> obtenerInterpretes(){
        ArrayList<Interprete> art = new ArrayList<Interprete>();
        Interprete a;
        Conexion con = new Conexion();
        ResultSet res;
        res = con.consultar("Select * from interprete;");
        try {
            while(res.next()){
                a = new Interprete();
                a.setId_interprete(Integer.parseInt(res.getString("id_interprete")));
                a.setNombre(res.getString("Nombre"));
                a.setInformacion(res.getString("Informacion"));
                art.add(a);
            }
            con.salir();
        } catch (SQLException ex) {
            error = ex.getMessage();
        }
        return art;
    }
    
    public int ObtenerUltimoId(){
        Conexion con = new Conexion();
        ResultSet res=null;
        int id=0;
        res = con.consultar("SELECT id_interprete FROM interprete ORDER BY id_interprete DESC LIMIT 1");
        try {
            if(res.next()){
                id=Integer.parseInt(res.getString("id_interprete"));
            }
            con.salir();
        } catch (SQLException ex) {
            error = ex.getMessage();
        }
        return id;
    }
    
    public int agregarInterprete(Interprete intr){
        int resultado=0;
        Conexion con = new Conexion();
        resultado = con.insertar("INSERT INTO interprete VALUES('"+intr.getId_interprete()+"','"+intr.getNombre()+
                    "','"+intr.getInformacion()+"');");
        try {
            con.salir();
        } catch (SQLException ex) {
            error=ex.getMessage();
        }
        return resultado;
    }
    
    public boolean existeInterprete(String intr){
        Conexion con = new Conexion();
        ResultSet res = con.consultar("SELECT * FROM interprete WHERE Nombre='"+intr.replaceAll("'", "\\\\'")+"';");
        try {
            if(res.next()){
                setId_interprete(Integer.parseInt(res.getString("id_interprete")));
                setNombre(res.getString("Nombre"));
                setInformacion(res.getString("Informacion"));
                con.salir();
                return true;
            }
            con.salir();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return false;
    }
    
    public void relacionInterpreteCancion(int intr, int id_cancion){
        Conexion con = new Conexion();
        ResultSet res = con.consultar("SELECT * FROM interprete WHERE id_interprete='"+intr+"';");
        int resultado;
        try {
            if(res.next()){
                resultado=con.insertar("INSERT INTO cancion_interprete VALUES('"+id_cancion+"','"+intr+"');");
            }
            con.salir();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
}
