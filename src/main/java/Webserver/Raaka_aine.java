package Webserver;

public class Raaka_aine {

    private String nimi;
    private String mittayksikko;
    private String kuvaus;
    private Integer id;
    private Integer kaytossa;

    public Raaka_aine() {
        
    }
    
    public Raaka_aine(Integer id, String nimi, String mittayksikko, String kuvaus) {
        this.nimi = nimi;
        this.id = id;
        this.kuvaus = kuvaus;
        this.mittayksikko = mittayksikko;
    }

    public String getMittayksikko() {
        return mittayksikko;
    }

    public String getKuvaus() {
        return kuvaus;
    }

    public Integer getId() {
        return id;
    }

    public String getNimi() {

        return this.nimi;
    }

    public Integer getKaytossa() {
        return (this.kaytossa == null ? 0 : this.kaytossa);
    }
    public void setMittayksikko(String mittayksikko) {
        this.mittayksikko = mittayksikko;
    }

    public void setKuvaus(String kuvaus) {
        this.kuvaus = kuvaus;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    public void setKaytossa(Integer kaytossa) {
        this.kaytossa = kaytossa;
    }
}
