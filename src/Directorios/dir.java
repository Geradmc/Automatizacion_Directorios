package Directorios;

import Conexion.Conexion;
import beans.Album;
import beans.Cancion;
import beans.Cover;
import beans.Genero;
import beans.Interprete;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

public class dir {
    private Conexion con;
    private String root[];
    private int id_dir[];
    private int encontrados;
    
    public static void main(String[] args) {
        try {
            dir ejec = new dir();
            ejec.asignarDir();
            ejec.con.salir();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public dir(){
        con = new Conexion();
        String temp="";
        String ids="";
        String tmp[];
        int i;
        ResultSet res = con.consultar("SELECT * FROM Directorios ORDER BY id_Directorios ASC;");
        try {
            while(res.next()){
                temp+=res.getString("Direccion")+"<>";
                ids+=res.getString("id_Directorios")+"<>";
            }
            root = temp.split("<>");
            tmp = ids.split("<>");
            id_dir = new int[root.length];
            for(i=0; i<id_dir.length; i++){
                id_dir[i] = Integer.parseInt(tmp[i]);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        encontrados=0;
    }
    
    public void asignarDir(){
        int i;
        File Dir;
        Album al = new Album();
        ResultSet res;
        for(i=0; i<root.length; i++){
            Dir = new File(root[i]);
            if(!Dir.exists()){
                res = con.consultar("SELECT id_album FROM album WHERE id_Directorio='"+id_dir[i]+"';");
                try {
                    while(res.next()){
                        al.borrarAlbum(Integer.parseInt(res.getString("id_album")));
                    }
                    con.borrar("DELETE FROM Directorios WHERE id_Directorios='"+id_dir[i]+"';");
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }else if(Dir.isDirectory()){
                borrarAlbums(Dir,id_dir[i]);
                recorrerDirectorio(root[i],0,id_dir[i]);
            }
        }
    }
    
    public void recorrerDirectorio(String directorio, int profundidad, int id_directorio){
        File Dir = new File(directorio);
        File archivos[];
        LeerPropiedadesMP32 prop;
        int i,resultado=0;
        ResultSet res;
        if(Dir.isDirectory()){
            archivos = Dir.listFiles();
            try{
                for(i=0; i<archivos.length; i++){
                    if(archivos[i].isDirectory()){
                        res=con.consultar("SELECT id_album FROM album WHERE Carpeta='"+archivos[i].getPath().replaceAll("\\\\", "/").replaceAll("'", "\\\\'")+"' && id_directorio='"+id_directorio+"';");
                        if(res.next()){
                            resultado=1;
                        }
                        if(resultado==0){
                            profundidad++;
                            recorrerDirectorio(archivos[i].getPath(),profundidad,id_directorio);
                            profundidad--;
                        }
                        resultado=0;
                    } else{
                        int index = archivos[i].getName().lastIndexOf(".");
                        String ext = archivos[i].getName().substring(index+1);
                        if(ext.toUpperCase().equals("MP3")){
                            res=con.consultar("SELECT id_cancion FROM cancion WHERE Carpeta='"+archivos[i].getPath().replaceAll("\\\\", "/").replaceAll("'", "\\\\'")+"';");
                            if(res.next()){
                                resultado=1;
                            }
                            if(resultado==0){
                                prop = new LeerPropiedadesMP32(archivos[i].getPath());
                                ingresarEnBaseDeDatos(prop,archivos[i].getParent(),profundidad,id_directorio,archivos[i].getPath());
                            }
                            resultado=0;
                        } else if(ext.toUpperCase().equals("JPG") || ext.toUpperCase().equals("PNG")||
                            ext.toUpperCase().equals("GIF") || ext.toUpperCase().equals("JPEG")
                            ){
                            res=con.consultar("SELECT id_album FROM covers WHERE Carpeta='"+archivos[i].getPath().replaceAll("\\\\", "/").replaceAll("'", "\\\\'")+"';");
                            if(res.next()){
                                resultado=1;
                            }
                            if(resultado==0){
                                covers(archivos[i].getParent(),profundidad,id_directorio,archivos[i]);
                            }
                            resultado=0; 
                        } else if(ext.toUpperCase().equals("M4A") || ext.toUpperCase().equals("OGG")
                            || ext.toUpperCase().equals("FLAC") || ext.toUpperCase().equals("WAV")){
                            res=con.consultar("SELECT id_cancion FROM cancion WHERE Carpeta='"+archivos[i].getPath().replaceAll("\\\\", "/").replaceAll("'", "\\\\'")+"';");
                            if(res.next()){
                                resultado=1;
                            }
                            if(resultado==0){
                                otrosAudios(archivos[i].getParent(),profundidad,id_directorio,archivos[i]);
                            }
                            resultado=0;
                        }
                    }
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
    
    public void ingresarEnBaseDeDatos(LeerPropiedadesMP32 prop, String dirAlbum, int prof, int id_directorio, String path){
        String tmp[] = dirAlbum.split("\\\\");
        int registros=0,id_album=0;
        String ruta="";
        Album al=null;
        Cancion can=null;
        Genero gen=null;
        Interprete intr=null;
        
        int i;
        if(prof!=0){
            for(i=0; i<=tmp.length-prof; i++){
                if(i==tmp.length-prof)
                    ruta+=tmp[i];
                else
                    ruta+=tmp[i]+"/";
            }
        } else{
            ruta=dirAlbum.replaceAll("\\\\", "/");
        }
        ruta = ruta.replaceAll("'", "\\\\'");
        
        prop.LeerTags();
        if(prof!=0){
            if(prop.getAlbum().equals("")){
                if(prof!=0)
                    prop.setAlbum(tmp[tmp.length-prof]);
                else
                    prop.setAlbum("Sueltos");
            }
        } else prop.setAlbum("sueltos");
        ResultSet res = con.consultar("SELECT * FROM album WHERE Carpeta='"+ruta+"';");
        
        try {
            if(res.next()){
                al = new Album();
                al.setId_album(Integer.parseInt(res.getString("id_album")));
                al = al.crearAlbum(al.getId_album(), prop.getAlbum().replaceAll("'", "\\\\'"), "", prop.getAnio(), "", id_directorio, ruta, "");
                al.actualizarAlbum(al);
                id_album = al.getId_album();
                registros++;
            }
            if(registros==0){
                al= new Album();
                al = al.crearAlbum(al.ObtenerUltimoId()+1, prop.getAlbum().replaceAll("'", "\\\\'"), "", prop.getAnio(), "", id_directorio, ruta, "");
                id_album = al.getId_album();
                al.agregarAlbum(al);
            }
            
            can = new Cancion();
            can = can.crearCancion(can.ObtenerUltimoId()+1, prop.getTitulo().replaceAll("'", "\\\\'"), prop.getPista(), "none", id_album, path.replaceAll("\\\\", "/").replaceAll("'", "\\\\'"));
            can.agregarCancion(can);
            
            intr = new Interprete();
            if(!intr.existeInterprete(prop.getArtista()) && !prop.getArtista().equals("")){
                intr = new Interprete();
                intr = intr.crearInterprete(intr.ObtenerUltimoId()+1, prop.getArtista().replaceAll("'", "\\\\'"), "");
                intr.agregarInterprete(intr);
                intr.relacionInterpreteCancion(intr.getId_interprete(), can.getId_cancion());
            }
            
            gen = new Genero();
            if(gen.existeGenero(prop.getGenero())==false && !prop.getGenero().equals("")){
                gen.setId_genero(gen.ObtenerUltimoId()+1);
                gen.setNombre(prop.getGenero());
                gen.agregarGenero(gen);
                gen.relacionGeneroAlbum(gen.getId_genero(), id_album);
            }
                
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public void covers(String dirAlbum, int prof, int id_directorio, File img){
        String tmp[] = dirAlbum.split("\\\\");
        int registros=0,id_album=0;
        Album al=null;
        Cover cov=null;
        String ruta="";
        int i;
        if(prof!=0){
            for(i=0; i<=tmp.length-prof; i++){
                if(i==tmp.length-prof)
                    ruta+=tmp[i];
                else
                    ruta+=tmp[i]+"/";
            }
        } else{
            ruta=dirAlbum.replaceAll("\\\\", "/");
        }
        ruta=ruta.replaceAll("'", "\\\\'");
        //System.out.println(ruta);
        //System.out.println("Profundidad: "+prof);
        ResultSet res = con.consultar("SELECT * FROM album WHERE Carpeta='"+ruta+"';");
        try {
            if(res.next()){
                al = new Album();
                al.setId_album(Integer.parseInt(res.getString("id_album")));
                al = al.crearAlbum(al.getId_album(), res.getString("Nombre").replaceAll("'", "\\\\'"), res.getString("Mes_salida"), Integer.parseInt(res.getString("Anio_salida")), res.getString("Fecha_pub"), id_directorio, ruta, res.getString("portada").replaceAll("'", "\\\\'"));
                id_album = al.getId_album();
                registros++;
            } 
            
            if(registros==0){
                al= new Album();
                al = al.crearAlbum(al.ObtenerUltimoId()+1, "", "", 0, "", id_directorio, ruta, "");
                id_album = al.getId_album();
                al.agregarAlbum(al);
            }
            cov = new Cover();
            cov.setId_album(id_album);
            cov.setCarpeta(img.getPath().replaceAll("\\\\", "/").replaceAll("'", "\\\\'"));
            tmp = img.getName().split("\\.");
            String tmpor[] = al.getPortada().split("\\\\");
            String tmpor2[] = tmpor[tmpor.length-1].split("\\.");
            if((tmp[0].toUpperCase().equals("COVER") || tmp[0].toUpperCase().equals("FOLDER")) && !tmpor2[0].toUpperCase().equals("COVER")){
                al.setPortada(img.getPath().replaceAll("\\\\", "/").replaceAll("'", "\\\\'"));
            } else if(al.getPortada().equals("")){
                al.setPortada(img.getPath().replaceAll("\\\\", "/").replaceAll("'", "\\\\'"));
            }
            al.actualizarAlbum(al);
            cov.agregarCover(cov);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    
    public void otrosAudios(String dirAlbum, int prof, int id_directorio, File audio){
        String tmp[] = dirAlbum.split("\\\\");
        LeerPropiedadesMP32 letmp= new LeerPropiedadesMP32("");
        int registros=0,id_album=0;
        String tmp2=audio.getName();
        String temp="";
        int pista=-1;
        Album al=null;
        Cancion can=null;
        String ruta="";
        String nomAlbum;
        int i;
        if(prof!=0){
            for(i=0; i<=tmp.length-prof; i++){
                if(i==tmp.length-prof)
                    ruta+=tmp[i];
                else
                    ruta+=tmp[i]+"/";
            }
            nomAlbum=tmp[tmp.length-prof];
        } else{
            ruta=dirAlbum.replaceAll("\\\\", "/");
            nomAlbum="sueltos";
        }
        ruta=ruta.replaceAll("'", "\\\\'");
        //System.out.println(ruta);
        ResultSet res = con.consultar("SELECT * FROM album WHERE Carpeta='"+ruta+"';");
        try {
            if(res.next()){
                al = new Album();
                al.setId_album(Integer.parseInt(res.getString("id_album")));
                al = al.crearAlbum(al.getId_album(), res.getString("Nombre").replaceAll("'", "\\\\'"), res.getString("Mes_salida"), Integer.parseInt(res.getString("Anio_salida")), res.getString("Fecha_pub"), id_directorio, ruta, res.getString("portada").replaceAll("'", "\\\\'"));
                id_album = al.getId_album();
                registros++;
            } 
            
            if(registros==0){
                al= new Album();
                al = al.crearAlbum(al.ObtenerUltimoId()+1, nomAlbum.replaceAll("'", "\\\\'"), "", 0, "", id_directorio, ruta, "");
                id_album = al.getId_album();
                al.agregarAlbum(al);
            }
            can = new Cancion();
            if(tmp2.length()>2){
                for(i=0; i<3; i++){
                    if(letmp.esNumero(tmp2.charAt(i))){
                            temp+=tmp2.charAt(i);
                    }
                }
            }
            if(letmp.esNumero(temp)){
                pista=Integer.parseInt(temp);
            }
            can = can.crearCancion(can.ObtenerUltimoId()+1, tmp2.replaceAll("'", "\\\\'"), pista, "none", id_album, audio.getPath().replaceAll("\\\\", "/").replaceAll("'", "\\\\'"));
            can.agregarCancion(can);
            
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public void borrarAlbums(File raiz, int id_dir){
        int i;
        File archivos[] = raiz.listFiles();
        ResultSet res;
        Album al = new Album();
        Cover cov = new Cover();
        Cancion can = new Cancion();
        try {
            res = con.consultar("SELECT * FROM album WHERE id_Directorio='"+id_dir+"';");
            while(res.next()){
                encontrados=0;
                for(i=0; i<archivos.length; i++){
                    if(archivos[i].isDirectory()){
                        if(res.getString("Carpeta").equals(archivos[i].getPath().replaceAll("\\\\", "/")) || res.getString("Nombre").equals("sueltos")){
                            encontrados++;
                            break;
                        }
                    }
                }
                if(encontrados==0){
                    al.borrarAlbum(Integer.parseInt(res.getString("id_album")));
                    System.out.println("Se borro album: "+res.getString("id_album")+"\nCon nombre: "+res.getString("Nombre"));
                }
            }
            /*res = con.consultar("SELECT covers.* FROM covers,album WHERE covers.id_album=album.id_album && album.id_directorio='"+id_dir+"';");
            while(res.next()){
                encontrados=0;
                buscarArchivo(res,raiz,0);
                if(encontrados==0){
                    cov.borrarCover(Integer.parseInt(res.getString("id_album")),res.getString("Carpeta").replaceAll("'", "\\\\'"));
                    System.out.println("Se borro cover con id_album: "+res.getString("id_album")+"\nCon Direccion: "+res.getString("Carpeta"));
                }
            }
            res = con.consultar("SELECT cancion.* FROM cancion,album WHERE cancion.id_album=album.id_album && album.id_directorio='"+id_dir+"';");
            while(res.next()){
                encontrados=0;
                buscarArchivo(res,raiz,1);
                if(encontrados==0){
                    can.borrarCancion(Integer.parseInt(res.getString("id_cancion")));
                    System.out.println("Se borro cancion: "+res.getString("id_cancion")+"\nCon nombre: "+res.getString("titulo"));
                }
            }*/
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public void buscarArchivo(ResultSet res, File raiz, int tipo){
        File archivos[] = raiz.listFiles();
        int i;
        try {
            for(i=0; i<archivos.length; i++){
                if(archivos[i].isDirectory()){
                    buscarArchivo(res,archivos[i],tipo);
                    if(encontrados==1){
                        return;
                    }
                } else{
                    String temp[];
                    temp = res.getString("Carpeta").split("/");
                    int index = temp[temp.length-1].lastIndexOf(".");
                    String ext = temp[temp.length-1].substring(index+1);
                    if((ext.toUpperCase().equals("M4A") || ext.toUpperCase().equals("OGG") ||
                    ext.toUpperCase().equals("FLAC") || ext.toUpperCase().equals("WAV") || 
                    ext.toUpperCase().equals("MP3")) && tipo==1){
                        if(res.getString("Carpeta").equals(archivos[i].getPath().replaceAll("\\\\", "/")) && archivos[i].getParent().replaceAll("\\\\", "/").equals(raiz.getPath().replaceAll("\\\\", "/"))){
                            encontrados=1;
                            return;
                        }
                    } else if((ext.toUpperCase().equals("JPG") || ext.toUpperCase().equals("PNG")||
                    ext.toUpperCase().equals("GIF") || ext.toUpperCase().equals("JPEG")) && tipo==0
                    ){
                        if(res.getString("Carpeta").equals(archivos[i].getPath().replaceAll("\\\\", "/")) && archivos[i].getParent().replaceAll("\\\\", "/").equals(raiz.getPath().replaceAll("\\\\", "/"))){
                            encontrados=1;
                            return;
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}