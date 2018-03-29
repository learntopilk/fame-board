/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Webserver;

/**
 *
 * @author Jonse
 */
public class Kayttaja {
    String kayttajanimi;
    String salasana;
    public Kayttaja(String kayt, String sal) {
        this.kayttajanimi = kayt;
        this.salasana = sal;
    }
    
    public String getKayttajanimi(){
        return this.kayttajanimi;
    }
    
    public String getSalasana(){
        return this.salasana;
    }
    
    public void setSalasana(String s){
        this.salasana = s;
    }
}
