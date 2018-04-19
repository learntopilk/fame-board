/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import DAOs.KayttajaDao;
import static Webserver.Main.k;
import Webserver.Kayttaja;
import com.lambdaworks.crypto.SCryptUtil;

/**
 *
 * @author Jonse
 */
public class UserController {

    public static boolean authenticate(Kayttaja kirjautuva) {
        if (kirjautuva.getSalasana() == null || kirjautuva.getSalasana().equals("")) {
            return false;
        }

        Kayttaja kayt = null;
        try {
            kayt = k.findOne(kirjautuva.getKayttajanimi());
        } catch (Exception e) {
            System.out.println("Could not get user from database: " + e);
        }

        if (kayt == null) {
            return false;
        }

        if (SCryptUtil.check(kirjautuva.getSalasana(), kayt.getSalasana())) {
            return true;

        } else {
            System.out.println("Sign-in failed");
            return false;
        }
    }
}
