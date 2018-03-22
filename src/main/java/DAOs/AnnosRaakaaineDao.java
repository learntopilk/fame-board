/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAOs;

import Webserver.AnnosRaakaaine;
import static Webserver.Database.getConnection;
import Webserver.Raaka_aine;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author jaakkovilenius
 */
public class AnnosRaakaaineDao implements Dao {

    public AnnosRaakaaineDao() throws SQLException {

        Connection con = getConnection();
        try {
            PreparedStatement createTable = con.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS AnnosRaakaaine ("
                    + "annos_id integer,"
                    + "raakaaine_id integer,"
                    + "jarjestys integer,"
                    + "maara integer,"
                    + "ohje varchar(500),"
                    + "FOREIGN KEY (annos_id) REFERENCES Annos (id),"
                    + "FOREIGN KEY (raakaaine_id) REFERENCES Raakaaine (id)"
                    + ");");
            createTable.executeUpdate();
            createTable.close();
            System.out.println("Luotiin AnnosRaakaaine taulu");
        } catch (Exception e) {
            System.out.println("ongelma luodessa annosraakaaine-taulua: " + e.getMessage());
        }
        try {
            PreparedStatement createIndexAnnos = con.prepareStatement(
                    "CREATE UNIQUE INDEX IF NOT EXISTS AnnosRaakaaineAnnosIdx "
                    + "ON AnnosRaakaaine (annos_id, jarjestys);");
            createIndexAnnos.executeUpdate();
            createIndexAnnos.close();
            System.out.println("Luotiin indeksi (annos_id, jarjestys) taululle AnnosRaakaaine");
        } catch (Exception e) {
            System.out.println("ongelma luodessa annosraakaaine-taulun indeksiä: " + e.getMessage());
        }
        try {
            PreparedStatement createIndexAnnos = con.prepareStatement(
                    "CREATE INDEX IF NOT EXISTS AnnosRaakaaineRaakaaineIdx "
                    + "ON AnnosRaakaaine (raakaaine_id);");
            createIndexAnnos.executeUpdate();
            createIndexAnnos.close();
            System.out.println("Luotiin indeksi (raakaaine_id) taululle AnnosRaakaaine");
        } catch (Exception e) {
            System.out.println("ongelma luodessa annosraakaaine-taulun indeksiä: " + e.getMessage());
        }

        con.close();

    }

    @Override
    public AnnosRaakaaine findOne(Object key) throws SQLException {
        try {
            AnnosRaakaaine etsittava = (AnnosRaakaaine) key;
            AnnosRaakaaine osuma = null;
            Connection con = getConnection();
            PreparedStatement haku = con.prepareStatement(""
                    + "SELECT "
                    + "ara.annos_id, "
                    + "ara.raakaaine_id, "
                    + "ara.jarjestys, "
                    + "ara.maara, "
                    + "ara.ohje, "
                    + "ra.nimi, "
                    + "ra.mittayksikko, "
                    + "ra.kuvaus "
                    + "FROM AnnosRaakaaine ara, Raakaaine ra "
                    + "WHERE ara.annos_id = ? "
                    + "AND ara.raakaaine_id = ? "
                    + "AND ara.jarjestys = ?"
                    + "AND ara.raakaaine_id = ra.id "
                    + "ORDER BY jarjestys");
            haku.setInt(1, etsittava.getAnnosId());
            haku.setInt(2, etsittava.getRaakaaineId());
            haku.setInt(3, etsittava.getJarjestys());
            ResultSet rs = haku.executeQuery();
            if (rs.next()) {
                osuma = populoi(rs);
            }

            rs.close();
            haku.close();
            con.close();
            return osuma;

        } catch (SQLException e) {
            System.out.println("ongelma etsiessä yhtä AnnosRaaka-ainetta" + e.getMessage());
        }
        return null;
    }

    @Override
    public List findAll() throws SQLException {

        List<AnnosRaakaaine> ret = new ArrayList<>();

        try {

            Connection con = getConnection();
            PreparedStatement prep = con.prepareStatement(""
                    + "SELECT "
                    + "ara.annos_id, "
                    + "ara.raakaaine_id, "
                    + "ara.jarjestys, "
                    + "ara.maara, "
                    + "ara.ohje, "
                    + "ra.nimi, "
                    + "ra.mittayksikko, "
                    + "ra.kuvaus "
                    + "FROM AnnosRaakaaine ara, Raakaaine ra "
                    + "WHERE ara.raakaaine_id = ra.id "
                    + "ORDER BY jarjestys");

            ResultSet r = prep.executeQuery();

            while (r.next()) {
                ret.add(populoi(r));
            }

            r.close();
            prep.close();
            con.close();

            return ret;
        } catch (Exception e) {
            System.out.println("Ongelma AnnosRaakaaine findAll-metodissa: " + e);
            return null;
        }
    }

    public List findAll(int annosId) throws SQLException {

        List<AnnosRaakaaine> ret = new ArrayList<>();

        try {

            Connection con = getConnection();
            PreparedStatement prep = con.prepareStatement(""
                    + "SELECT "
                    + "ara.annos_id, "
                    + "ara.raakaaine_id, "
                    + "ara.jarjestys, "
                    + "ara.maara, "
                    + "ara.ohje, "
                    + "ra.nimi, "
                    + "ra.mittayksikko, "
                    + "ra.kuvaus "
                    + "FROM AnnosRaakaaine ara, Raakaaine ra "
                    + "WHERE ara.annos_id = ? "
                    + "AND ara.raakaaine_id = ra.id "
                    + "ORDER BY jarjestys");

            prep.setInt(1, annosId);
            ResultSet r = prep.executeQuery();

            while (r.next()) {
                ret.add(populoi(r));
            }

            r.close();
            prep.close();
            con.close();

            return ret;
        } catch (Exception e) {
            System.out.println("Ongelma AnnosRaakaaine findAll-metodissa: " + e);
            return null;
        }
    }

    private AnnosRaakaaine populoi(ResultSet r) throws SQLException {
        AnnosRaakaaine annRaak = new AnnosRaakaaine();
        annRaak.setAnnosId(r.getInt("annos_id"));
        annRaak.setRaakaaineId(r.getInt("raakaaine_id"));
        annRaak.setJarjestys(r.getInt("jarjestys"));
        annRaak.setMaara(r.getInt("maara"));
        annRaak.setOhje(r.getString("ohje"));
        annRaak.setRaakaaineenNimi(r.getString("nimi"));
        annRaak.setRaakaaineenMittayksikko(r.getString("mittayksikko"));
        annRaak.setRaakaaineenKuvaus(r.getString("kuvaus"));
        return annRaak;
    }

    @Override
    public Object saveOrUpdate(Object annosaine) throws SQLException {

        try {
            AnnosRaakaaine uusi = (AnnosRaakaaine) annosaine;
            if (findOne(uusi) != null) {
                Connection con = getConnection();
                PreparedStatement paivita = con.prepareStatement(""
                        + "UPDATE AnnosRaakaaine SET "
                        + "maara = ?, "
                        + "ohje = ? "
                        + "WHERE annos_id = ? "
                        + "AND raakaaine_id = ? "
                        + "AND jarjestys = ?");
                paivita.setInt(1, uusi.getMaara());
                paivita.setString(2, uusi.getOhje());
                paivita.setInt(3, uusi.getAnnosId());
                paivita.setInt(4, uusi.getRaakaaineId());
                paivita.setInt(5, uusi.getJarjestys());
                paivita.executeUpdate();
                paivita.close();
                con.close();

            } else {

                // Lisää uuden. jarjestys = saman annoksen max(jarjestys, 0)+1
                // COALESCE palauttaa sulkujen sisällä olevan listan ensimmäisen ei null arvon
                Connection con = getConnection();
                PreparedStatement lisaa = con.prepareStatement(""
                        + "INSERT INTO AnnosRaakaaine ("
                        + "annos_id, raakaaine_id, jarjestys, maara, ohje) "
                        + "SELECT ?, ?, COALESCE(MAX(jarjestys), 0)+1, ? , ? "
                        + "FROM AnnosRaakaaine "
                        + "WHERE annos_id = ? ");
                lisaa.setInt(1, uusi.getAnnosId());
                lisaa.setInt(2, uusi.getRaakaaineId());
                lisaa.setInt(3, uusi.getMaara());
                lisaa.setString(4, uusi.getOhje());
                lisaa.setInt(5, uusi.getAnnosId());

                lisaa.executeUpdate();
                lisaa.close();
                con.close();
            }

        } catch (SQLException e) {
            System.out.println("ongelma lisätessä uutta raaka-ainetta saveOrUpdate AnnosRaakaaineDao" + e.getMessage());
        }
        return true;

    }

    @Override
    public void delete(Object key) throws SQLException {

        try {
            AnnosRaakaaine pois = (AnnosRaakaaine) key;

            Connection con = getConnection();
            PreparedStatement poista = con.prepareStatement(""
                    + "DELETE FROM AnnosRaakaaine "
                    + "WHERE annos_id = ? "
                    + "AND raakaaine_id = ? "
                    + "AND jarjestys = ?");
            poista.setInt(3, pois.getAnnosId());
            poista.setInt(4, pois.getRaakaaineId());
            poista.setInt(5, pois.getJarjestys());
            poista.executeUpdate();
            poista.close();
            con.close();

        } catch (SQLException e) {
            System.out.println("Kauhea ongelma poistettaessa raaka-ainetta annoksesta" + e.getMessage());
        }

    }

    public void deleteAll(int annosId) throws SQLException {

        try {

            Connection con = getConnection();
            PreparedStatement poista = con.prepareStatement(""
                    + "DELETE FROM AnnosRaakaaine "
                    + "WHERE annos_id = ?");
            poista.setInt(1, annosId);
            poista.executeUpdate();
            poista.close();
            con.close();

        } catch (SQLException e) {
            System.out.println("Katastrofaalinen ongelma poistettaessa annoksen raaka-aineita" + e.getMessage());
        }
    }

}
