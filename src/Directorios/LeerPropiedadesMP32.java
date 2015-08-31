package Directorios;

import java.io.File;
import org.blinkenlights.jid3.ID3Exception;
import org.blinkenlights.jid3.MP3File;
import org.blinkenlights.jid3.MediaFile;
import org.blinkenlights.jid3.v1.ID3V1_0Tag;
import org.blinkenlights.jid3.v2.ID3V2_3_0Tag;


public class LeerPropiedadesMP32 {
    private GeneroList gen;
    private String titulo;
    private String album;
    private String genero;
    private String artista;
    private int anio;
    private int pista;
    File mp3;
    
    public LeerPropiedadesMP32(String nombre){
        gen = new GeneroList();
        genero="";
        titulo="";
        album="";
        artista="Desconocido";
        anio=0;
        pista=-1;
        mp3 = new File(nombre);
    }
    
    public void LeerTags() {
    MediaFile mediaFile = new MP3File(mp3);
        try {
            for (Object obj : mediaFile.getTags()) {
                if (obj instanceof ID3V1_0Tag)
                    LeerID3V1Tags(obj);
                else if (obj instanceof ID3V2_3_0Tag)
                    LeerID3V2_3Tags(obj);
            }
        } catch (ID3Exception e1) {
            e1.printStackTrace();
        }
    }
    
    private void LeerID3V1Tags(Object obj) {
        ID3V1_0Tag v1 = (ID3V1_0Tag) obj;
        if (v1.getTitle() != null)
            setTitulo(v1.getTitle());
        else setTitulo(mp3.getName());
        if (v1.getAlbum() != null)
            setAlbum(v1.getAlbum());
        if (v1.getArtist() != null)
            setArtista(v1.getArtist());
        if(v1.getGenre()!=null)
            setGenero(gen.obtenerGenero(v1.getGenre().getByteValue()));
        if(v1.getYear()!=null && esNumero(v1.getYear()))
            setAnio(Integer.parseInt(v1.getYear()));
    }
    
    private void LeerID3V2_3Tags(Object obj) {
        String temp="";
        String tmp="";
        int i;
        ID3V2_3_0Tag v2_3 = (ID3V2_3_0Tag) obj;
        if (v2_3.getTitle() != null)
            setTitulo(v2_3.getTitle());
        else setTitulo(mp3.getName());
        if (v2_3.getAlbum() != null)
            setAlbum(v2_3.getAlbum());
        if (v2_3.getArtist() != null)
            setArtista(v2_3.getArtist());
        if(v2_3.getGenre()!=null){
            if(gen.generoRegistrado(v2_3.getGenre())==true){
                setGenero(v2_3.getGenre());
            } else{
                temp = dejarNumero(v2_3.getGenre());
                if(esNumero(temp)){
                    setGenero(gen.obtenerGenero(Integer.parseInt(temp)));
                }
            }
        }
        
        try{
            if(esNumero(v2_3.getTrackNumber()+""))
                setPista(v2_3.getTrackNumber());
        } catch (ID3Exception ex) {
            //System.out.println(ex.getMessage());
            temp="";
            tmp=mp3.getName();
            if(tmp.length()>2){
                for(i=0; i<3; i++){
                    if(esNumero(tmp.charAt(i))){
                            temp+=tmp.charAt(i);
                    }
                }
            }
            if(esNumero(temp)){
                setPista(Integer.parseInt(temp));
            }
        }
        try {
            setAnio(v2_3.getYear());
        } catch (ID3Exception ex) {
            //System.out.println(ex.getMessage());
        }
    }
    
    public boolean esNumero(String num){
        char c;
        int i;
        for(i=0; i<num.length(); i++){
            c=num.charAt(i);
            if(c=='0' || c=='1' || c=='2' || c=='3' || c=='4' ||
               c=='5' || c=='6' || c=='7' || c=='8' || c=='9')
            { 
            } else return false;
        }
        if(num.equals("")) return false;
        return true;
    }
    
    public boolean esNumero(char num){
        char c=num;
        if(c=='0' || c=='1' || c=='2' || c=='3' || c=='4' ||
           c=='5' || c=='6' || c=='7' || c=='8' || c=='9')
        {
            return true;
        } else return false;
    }
    
    public String dejarNumero(String num){
        int i;
        String temp="";
        for(i=0; i<num.length(); i++){
            if(esNumero(num.charAt(i))){
                temp+=num.charAt(i);
            }
        }
        return temp;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getArtista() {
        return artista;
    }

    public void setArtista(String artista) {
        this.artista = artista;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public int getPista() {
        return pista;
    }

    public void setPista(int pista) {
        this.pista = pista;
    }
    
}