package Directorios;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Gerardo
 */
public class GeneroList {
    private String Generos[] = {
    "Blues","Classic Rock","Country","Dance","Disco","Funk","Grunge","Hip-Hop","Jazz","Metal","New Age",
    "Oldies","Other","Pop","R&B","Rap","Reggae","Rock","Techno","Industrial","Alternative","Ska","Death Metal",
    "Pranks","Soundtrack","Euro-Techno","Ambient","Trip-Hop","Vocal","Jazz+Funk","Fusion","Trance","Classical",
    "Instrumental","Acid","House","Game","Sound Clip","Gospel","Noise","Alternative Rock","Bass","Soul","Punk",
    "Space","Meditative","Instrumental Pop","Instrumental Rock","Ethnic","Gothic","Darkwave","Techno-Industrial",
    "Electronic","Pop-Folk","Eurodance","Dream","Southern Rock","Comedy","Cult","Gangsta","Top 40","Christian Rap",
    "Pop/Funk","Jungle","Native US","Cabaret","New Wave","Psychadelic","Rave","Showtunes","Trailer","Lo-Fi","Tribal",
    "Acid Punk","Acid Jazz","Polka","Retro","Musical","Rock & Roll","Hard Rock","Folk","Folk-Rock","National Folk",
    "Swing","Fast Fusion","Bebob","Latin","Revival","Celtic","Bluegrass","Avantgarde","Gothic Rock","Progressive Rock",
    "Psychedelic Rock","Symphonic Rock","Slow Rock","Big Band","Chorus","Easy Listening","Acoustic","Humour","Speech",
    "Chanson","Opera","Chamber Music","Sonata","Symphony","Booty Bass","Primus","Porn Groove","Satire","Slow Jam",
    "Club","Tango","Samba","Folklore","Ballad","Power Ballad","Rhytmic Soul","Freestyle","Duet","Punk Rock","Drum Solo",
    "Acapella","Euro-House","Dance Hall","Goa","Drum & Bass","Club-House","Hardcore","Terror","Indie","BritPop",
    "Negerpunk","Polsk Punk","Beat","Christian Gangsta","Heavy Metal","Black Metal","Crossover","Contemporary C",
    "Christian Rock","Merengue","Salsa","Thrash Metal","Anime","JPop","SynthPop"};
    
    public String obtenerGenero(int codigo){
        if(codigo-1>Generos.length){
            return "Others";
        } else return Generos[codigo];
    }
    
    public boolean generoRegistrado(String gen){
        int i;
        for(i=0; i<Generos.length; i++){
            if(Generos[i].toUpperCase().equals(gen.toUpperCase())){
                return true;
            }
        }
        return false;
    }
}
