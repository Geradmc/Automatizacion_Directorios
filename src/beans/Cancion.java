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
public class Cancion implements Serializable{
    private int id_cancion;
    private String titulo;
    private int num_pista;
    private String duracion;
    private int id_album;
    private String Carpeta;

    private String error;
    
    public int getId_cancion() {
        return id_cancion;
    }

    public void setId_cancion(int id_cancion) {
        this.id_cancion = id_cancion;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo=titulo;
    }

    public int getNum_pista() {
        return num_pista;
    }

    public void setNum_pista(int num_pista) {
        this.num_pista = num_pista;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public String getCarpeta() {
        return Carpeta;
    }

    public void setCarpeta(String Carpeta) {
        this.Carpeta = Carpeta;
    }
    
    public int getId_album() {
        return id_album;
    }

    public void setId_album(int id_album) {
        this.id_album = id_album;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
    
    public Cancion crearCancion(int id_cancion, String titulo, int num_pista, String duracion, int id_album, String Carpeta){
        Cancion can = new Cancion();
        can.setId_cancion(id_cancion);
        can.setTitulo(titulo);
        can.setNum_pista(num_pista);
        can.setDuracion(duracion);
        can.setId_album(id_album);
        can.setCarpeta(Carpeta);
        return can;
    }
    
    public int agregarCancion(Cancion can){
        int resultado=0;
        Conexion con = new Conexion();
        if(can.getNum_pista()==-1){
            resultado=con.insertar("INSERT INTO cancion VALUES('"+can.getId_cancion()+"','"+can.getTitulo()+
            "','0','"+can.getDuracion()+"','"+can.getCarpeta()+"','"+can.getId_album()+"');");
        } else{
            resultado=con.insertar("INSERT INTO cancion VALUES('"+can.getId_cancion()+"','"+can.getTitulo()+
            "','"+can.getNum_pista()+"','"+can.getDuracion()+"','"+can.getCarpeta()+"','"+can.getId_album()+"');");
        }
        try {
            con.salir();
        } catch (SQLException ex) {
            error = ex.getMessage();
        }
        return resultado;
    }
    
    public ArrayList<Album> buscarAlbumesPorCanciones(String busqueda){
        ArrayList<Album> lst = new ArrayList<Album>();
        Album al = null;
        ResultSet res,resAl;
        Conexion con = new Conexion();
        res = con.consultar("SELECT id_album FROM cancion WHERE Titulo LIKE '%"+busqueda+"%' ORDER BY id_album ASC;");
        try {
            while(res.next()){
                resAl = con.consultar("SELECT * FROM album WHERE id_album='"+res.getString("id_album")+"';");
                if(resAl.next()){
                    al = new Album();
                    al.setId_album(Integer.parseInt(resAl.getString("id_album")));
                    al.setNombre(resAl.getNString("Nombre"));
                    al.setMes(resAl.getString("Mes_salida"));
                    al.setAnio(Integer.parseInt(resAl.getString("Anio_salida")));
                    al.setFecha_pub(resAl.getString("Fecha_pub"));
                    al.setId_Directorio(Integer.parseInt(resAl.getString("id_Directorio")));
                    al.setPortada(resAl.getString("portada"));
                    al.setCarpeta(resAl.getString("carpeta"));
                    lst.add(al);
                }
            }
            con.salir();
        } catch (SQLException ex) {
            error=ex.getMessage();
        }
        return lst;
    }
    
    public int borrarCancion(int id_cancion){
        int resultado=0;
        Conexion con = new Conexion();
        resultado=con.borrar("DELETE FROM cancion_interprete WHERE id_cancion='"+id_cancion+"';");
        resultado+=con.borrar("DELETE FROM cancion WHERE id_cancion='"+id_cancion+"';");
        try {
            con.salir();
        } catch (SQLException ex) {
            error=ex.getMessage();
        }
        return resultado;
    }
    
    public void borrarCancion2(int id_album){
        Conexion con = new Conexion();
        int id_can=0;
        ResultSet res=null;
        res = con.consultar("SELECT id_cancion FROM cancion WHERE id_album='"+id_album+"';");
        try {
            while(res.next()){
                id_can=Integer.parseInt(res.getString("id_cancion"));
                con.borrar("DELETE FROM cancion_interprete WHERE id_cancion='"+id_can+"';");
            }
            con.borrar("DELETE FROM cancion WHERE id_album='"+id_album+"';");
            con.salir();
        } catch (SQLException ex) {
            error=ex.getMessage();
        }
    }
    
    public int actualizarCancion(Cancion can){
        int resultado=0;
        Conexion con = new Conexion();
        resultado=con.modificar("UPDATE cancion SET Titulo='"+can.getTitulo()+
        "', num_pista='"+can.getNum_pista()+"', duracion='"+can.getDuracion()+
        "' WHERE id_cancion='"+can.getId_cancion()+"';");
        try {
            con.salir();
        } catch (SQLException ex) {
            error=ex.getMessage();
        }
        return resultado;
    }
    
    public int ObtenerUltimoId(){
        Conexion con = new Conexion();
        ResultSet res=null;
        int id=0;
        res = con.consultar("SELECT id_cancion FROM cancion ORDER BY id_cancion DESC LIMIT 1");
        try {
            if(res.next()){
                id=Integer.parseInt(res.getString("id_cancion"));
            }
            con.salir();
        } catch (SQLException ex) {
            error = ex.getMessage();
        }
        return id;
    }
    
}
