package Webserver;

import java.util.Date;

public class Viesti {

    private int id;
    private int keskustelu_id;
    private String otsikko;
    private String sisalto;
    // LONG INSTEAD OF DATE
    private Date luomisaika;
    private String kuvanURL;
    private String luoja;

    public Viesti(String otsikko, String sisalto) {
        this.sisalto = sisalto;
        this.otsikko = otsikko;
        luomisaika = new Date();
    }

    public Viesti(String otsikko, String sisalto, int id) {
        this.sisalto = sisalto;
        this.otsikko = otsikko;
        luomisaika = new Date();
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

    public String getSisalto() {
        return this.sisalto;
    }

    public String getOtsikko() {
        return this.otsikko;
    }

    public String getKuvanURL() {
        return this.kuvanURL;
    }

    public Date getLuomisaika() {
        return this.luomisaika;
    }
    
    public String getLuoja() {
        return this.luoja;
    }
    public int getKeskustelu_id() {
        return this.keskustelu_id;
    }

    public void setSisalto(String sis) {
        this.sisalto = sis;
    }

    public void setOtsikko(String ots) {
        this.otsikko = ots;
    }

    public void setLuomisaika(Date dat) {
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

}
