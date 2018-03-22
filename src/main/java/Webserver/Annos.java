package Webserver;

import java.util.ArrayList;
import java.util.List;

public class Annos {

    private int id;
    private String nimi;
    private String valmistusohje;
//    private ArrayList<Raaka_aine> AnnosRaakaAineLista;  kommentoin ulos, näitä ei kait tarvita täällä? -tt
    private ArrayList<Raaka_aine> AnnosRaakaAineLista;
    // Nää tarvitaan just siksi, että objektissa olisi kaikki annoksen tiedot
    // Niin ei tarvitse erikseen mitään HashMappeja

    public Annos() {
        this.AnnosRaakaAineLista = new ArrayList<>();
    }

    public Annos(int id, String nimi, String valmistusohje) {
        this.id = id;
        this.nimi = nimi;
        this.valmistusohje = valmistusohje;
        this.AnnosRaakaAineLista = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getNimi() {
        return nimi;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    public void setValmistusohje(String valmistusohje) {
        this.valmistusohje = valmistusohje;
    }

    public String getValmistusohje() {
        return valmistusohje;
    }
    
    public void setRaakaaineet(List raakaaineet) {
        this.AnnosRaakaAineLista.addAll(raakaaineet);
    }
    
    public ArrayList getRaakaaineet() {
        return this.AnnosRaakaAineLista;
    }
    
    public int getRaakaaineLkm() {
        return (this.AnnosRaakaAineLista == null ? 0 : this.AnnosRaakaAineLista.size());
    }
}