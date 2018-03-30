package Webserver;

import java.util.Date;

public class Viesti {

    private int id;
    private int keskustelu_id;
    private String otsikko;
    private String sisalto;
    // LONG INSTEAD OF DATE
    private long luomisaika;
    private String kuvanURL;
    private String luoja;
    private int kayttaja_id;

    public Viesti(String otsikko, String sisalto) {
        this.sisalto = sisalto;
        this.otsikko = otsikko;
        luomisaika = System.currentTimeMillis();

        this.luoja = "Unknown";
        this.keskustelu_id = 1;
        this.kayttaja_id = 1;
    }

    public Viesti(String otsikko, String sisalto, int id) {
        this.sisalto = sisalto;
        this.otsikko = otsikko;
        luomisaika = System.currentTimeMillis();
        this.id = id;
    }

    public Viesti(String otsikko, String sisalto, String kuvanURL) {
        this.otsikko = otsikko;
        this.sisalto = sisalto;
        this.kuvanURL = kuvanURL;
    }

    public Viesti(String otsikko, String sisalto, String kuvanURL, int id) {
        this.otsikko = otsikko;
        this.sisalto = sisalto;
        this.kuvanURL = kuvanURL;
        this.id = id;
    }

    public Viesti(String otsikko, String sisalto, int id, long time) {
        this.otsikko = otsikko;
        this.sisalto = sisalto;
        //this.kuvanURL = kuvanURL;
        this.id = id;
        this.luomisaika = time;
    }

    public String getSisalto() {
        return this.sisalto;
    }

    public String getOtsikko() {
        return this.otsikko;
    }

    public String getKuvanURL() {
        return this.kuvanURL;
    }

    public long getLuomisaika() {
        return this.luomisaika;
    }

    public String getLuoja() {
        return this.luoja;
    }

    public int getKeskustelu_id() {
        return this.keskustelu_id;
    }

    public int getKayttajaId() {
        return this.kayttaja_id;
    }

    public void setSisalto(String sis) {
        this.sisalto = sis;
    }

    public void setOtsikko(String ots) {
        this.otsikko = ots;
    }

    public void setLuomisaika(Long dat) {
        this.luomisaika = dat;
    }

    public void setKuvanURL(String URL) {
        this.kuvanURL = URL;
    }

    public void setLuoja(String luoja) {
        this.luoja = luoja;
    }

    public void setKeskustelu_id(int id) {
        this.keskustelu_id = id;
    }

    public void setKayttajaId(int id) {
        this.kayttaja_id = id;
    }

}
