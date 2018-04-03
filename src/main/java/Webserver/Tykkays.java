
package Webserver;


public class Tykkays {
    
    public int viesti_id;
    public int kayttaja_id;
    
    public Tykkays(){
        
    }
    
    public Tykkays(int viestiId, int kayttajaId) {
        this.viesti_id = viestiId;
        this.kayttaja_id = kayttaja_id;
        
    }
    
    public int getViestiId() {
        return this.viesti_id;
    }
    
    public int getKayttajaId() {
        return this.kayttaja_id;
    }
    
}
