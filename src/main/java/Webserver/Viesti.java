package Webserver;

import java.util.Date;

public class Viesti implements Comparable<Viesti> {

    private int id;
    private int keskustelu_id;
    private String otsikko;
    private String sisalto;
    // LONG INSTEAD OF DATE
    private long luomisaika;
    private String kuvanURL;
    private String luoja;
    private int kayttaja_id;
    private int tykkayksia;
    private Date paiva;

    /**
     *
     * @param otsikko
     * @param sisalto
     */
    public Viesti(String otsikko, String sisalto) {
        this.sisalto = sisalto;
        this.otsikko = otsikko;
        luomisaika = System.currentTimeMillis();

        this.kuvanURL = null;
        this.luoja = "Unknown";
        //this.keskustelu_id = 1;
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

    public int getId() {
        return this.id;
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

    public int getTykkaystenMaara() {
        return this.tykkayksia;
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

    public void setTykkaystenMaara(int t) {
        this.tykkayksia = t;
    }

    public void setPaiva(long paiv) {
        this.paiva = new Date(paiv);
    }

    public Date getPaiva() {
        return this.paiva;
    }

    public void tykkaa() {
        this.tykkayksia++;
    }

    public void epatykkaa() {
        this.tykkayksia--;
    }

    public void setId(int i) {
        this.id = i;
    }

    @Override
    public int compareTo(Viesti v) {

        int compareQuantity = (int)(((Viesti) v).getLuomisaika() / 1000);

        //ascending order
        //return ((int) (this.luomisaika / 1000)) - compareQuantity;

        //descending order
        return compareQuantity - ((int) (this.luomisaika / 1000));
    }

}
