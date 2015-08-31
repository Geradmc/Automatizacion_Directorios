/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import Conexion.Conexion;
import beans.Cancion;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author GerardoML
 */
public class Album implements Serializable{
    private int id_album;
    private String Nombre;
    private String mes;
    private int anio;
    private String Fecha_pub;
    private int id_Directorio;
    private String Carpeta;
    private String portada;
    
    private String error;

    public int getId_album() {
        return id_album;
    }

    public void setId_album(int id_album) {
        this.id_album = id_album;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public String getFecha_pub() {
        return Fecha_pub;
    }

    public void setFecha_pub(String Fecha_pub) {
        this.Fecha_pub = Fecha_pub;
    }

    public int getId_Directorio() {
        return id_Directorio;
    }

    public void setId_Directorio(int id_directorio) {
        this.id_Directorio = id_directorio;
    }

    public String getPortada() {
        return portada;
    }

    public void setPortada(String portada) {
        this.portada = portada;
    }

    public String getCarpeta() {
        return Carpeta;
    }

    public void setCarpeta(String Carpeta) {
        this.Carpeta = Carpeta;
    }
    
    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
    
    public Album crearAlbum(int id_album, String Nombre, String mes, int anio, String Fecha_pub, int id_directorio, String Carpeta, String portada){
        Album al = new Album();
        al.setId_album(id_album);
        al.setNombre(Nombre);
        al.setMes(mes);
        al.setAnio(anio);
        al.setFecha_pub(Fecha_pub);
        al.setId_Directorio(id_directorio);
        al.setCarpeta(Carpeta);
        al.setPortada(portada);
        return al;
    }
    
    public ArrayList<Album> buscarAlbumes(String busqueda){
        ArrayList<Album> lst = new ArrayList<Album>();
        Album al = null;
        ResultSet res;
        Conexion con = new Conexion();
        res = con.consultar("SELECT * FROM album WHERE Nombre LIKE '%"+busqueda+"%';");
        try {
            while(res.next()){
                al = new Album();
                al.setId_album(Integer.parseInt(res.getString("id_album")));
                al.setNombre(res.getNString("Nombre"));
                al.setMes(res.getString("Mes_salida"));
                al.setAnio(Integer.parseInt(res.getString("Anio_salida")));
                al.setFecha_pub(res.getString("Fecha_pub"));
                al.setId_Directorio(Integer.parseInt(res.getString("id_Directorio")));
                al.setPortada(res.getString("portada"));
                al.setCarpeta(res.getString("Carpeta"));
                lst.add(al);
            }
            con.salir();
        } catch (SQLException ex) {
            error=ex.getMessage();
        }
        return lst;
    }
    
    public ArrayList<Album> pubPrincipal(String Fecha_inicial){
        ArrayList<Album> lst = new ArrayList<Album>();
        Album al;
        ResultSet res;
        Conexion con = new Conexion();
        res = con.consultar("SELECT * FROM album WHERE Fecha_pub<'"+Fecha_inicial+"' ORDER BY Fecha_pub DESC LIMIT 20;");
        try {
            while(res.next()){
                al = new Album();
                al.setId_album(Integer.parseInt(res.getString("id_album")));
                al.setNombre(res.getNString("Nombre"));
                al.setMes(res.getString("Mes_salida"));
                al.setAnio(Integer.parseInt(res.getString("Anio_salida")));
                al.setFecha_pub(res.getString("Fecha_pub"));
                al.setId_Directorio(Integer.parseInt(res.getString("id_Directorio")));
                al.setPortada(res.getString("portada"));
                al.setCarpeta(res.getString("Carpeta"));
                lst.add(al);
            }
            con.salir();
        } catch (SQLException ex) {
            error = ex.getMessage();
        }
        return lst;
    }
    
    public int agregarAlbum(Album al){
        int resultado=0;
        Conexion con = new Conexion();
        resultado = con.insertar("INSERT INTO album VALUES('"+al.getId_album()+"','"+al.getNombre()+
                    "','"+al.getMes()+"','"+al.getAnio()+"',now(),'"+al.getCarpeta()+"','"+al.getPortada()+
                    "','"+al.getId_Directorio()+"');");
        try {
            con.salir();
        } catch (SQLException ex) {
            error=ex.getMessage();
        }
        return resultado;
    }
    
    public int actualizarAlbum(Album al){
        int resultado=0;
        Conexion con = new Conexion();
        resultado=con.modificar("UPDATE album SET Nombre='"+al.getNombre()+
        "', Mes_salida='"+al.getMes()+"', Anio_salida='"+al.getAnio()+"', portada='"+al.getPortada()+
        "' WHERE id_album='"+al.getId_album()+"';");
        try {
            con.salir();
        } catch (SQLException ex) {
            error=ex.getMessage();
        }
        return resultado;
    }
    
    public void borrarAlbum(int id_album){
        Conexion con = new Conexion();
        Cancion can = new Cancion();
        Cover cov = new Cover();
        can.borrarCancion2(id_album);
        cov.borrarCover(id_album);
        con.borrar("DELETE FROM album_genero WHERE id_album='"+id_album+"';");
        con.borrar("DELETE FROM album WHERE id_album='"+id_album+"';");
        try {
            con.salir();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        
    }
    
    public int ObtenerUltimoId(){
        Conexion con = new Conexion();
        ResultSet res=null;
        int id=0;
        res = con.consultar("SELECT id_album FROM album ORDER BY id_album DESC LIMIT 1");
        try {
            if(res.next()){
                id=Integer.parseInt(res.getString("id_album"));
            }
            con.salir();
        } catch (SQLException ex) {
            error = ex.getMessage();
        }
        return id;
    }
}