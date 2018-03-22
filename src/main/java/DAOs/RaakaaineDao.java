package DAOs;

import Webserver.AnnosRaakaaine;
import static Webserver.Database.getConnection;
import Webserver.Raaka_aine;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*
DAO raaka-aineille, constructorissa create table
 */
public class RaakaaineDao implements Dao {

    public RaakaaineDao() throws SQLException {
        Connection con = getConnection();
        try {
            PreparedStatement createTable = con.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS Raakaaine ("
                    + "id SERIAL PRIMARY KEY,"
                    + "nimi varchar(50),"
                    + "mittayksikko varchar(50),"
                    + "kuvaus varchar(500)"
                    + ");");
            createTable.executeUpdate();
            createTable.close();
            System.out.println("Luotiin taulu Raakaaine, jos sitä ei ollut");

        } catch (Exception e) {
            System.out.println("ongelma luodessa raakaaine-taulua: " + e.getMessage());
        }
        try {
            // Luodaan indeksi, jolla voidaan hakea aineita lower(nimi)
            PreparedStatement createIndexAineId = con.prepareStatement(
                    "CREATE INDEX IF NOT EXISTS RaakaaineIdx "
                    + "ON Raakaaine (id);");
            createIndexAineId.executeUpdate();
            createIndexAineId.close();
            System.out.println("Luotiin indeksi taululle Raakaaine, jos sitä ei ollut");
        } catch (Exception e) {
            System.out.println("ongelma luodessa raakaaine-taulun indeksiä: " + e.getMessage());
        }
        try {
            // Luodaan indeksi, jolla voidaan hakea aineita lower(nimi)
            PreparedStatement createIndexAineNimi = con.prepareStatement(
                    "CREATE INDEX IF NOT EXISTS RaakaaineNimiLowerIdx "
                    + "ON Raakaaine ((lower(nimi)));");
            createIndexAineNimi.executeUpdate();
            createIndexAineNimi.close();
            System.out.println("Luotiin indeksi taululle Raakaaine, jos sitä ei ollut");
        } catch (Exception e) {
            System.out.println("ongelma luodessa raakaaine-taulun indeksiä: " + e.getMessage());
        }
        con.close();

    }

    @Override
    public Raaka_aine findOne(Object key) throws SQLException {
        try {
            Raaka_aine etsittava = (Raaka_aine) key;
            Raaka_aine osuma = null;
            Connection con = getConnection();
            PreparedStatement haku = con.prepareStatement("SELECT "
                    + "ra.id, ra.nimi, ra.mittayksikko, ra.kuvaus, count(distinct ara.annos_id) AS kaytossa "
                    + "FROM Raakaaine ra LEFT OUTER JOIN AnnosRaakaaine ara "
                    + "ON ra.id = ara.raakaaine_id "
                    + "WHERE ra.nimi = ? "
                    + "GROUP BY ra.id, ra.nimi, ra.mittayksikko, ra.kuvaus");
            haku.setString(1, etsittava.getNimi());
            ResultSet rs = haku.executeQuery();
            if (rs.next()) {
                osuma = populoi(rs);
            }

            rs.close();
            haku.close();
            con.close();
            return osuma;

        } catch (SQLException e) {
            System.out.println("ongelma etsiessä yhtä raaka-ainetta" + e.getMessage());
        }
        return null;
    }
    
        //@Override
    public Raaka_aine findOne(int id) throws SQLException {
        try {
           // Raaka_aine etsittava = (Raaka_aine) key;
            Raaka_aine osuma = null;
            Connection con = getConnection();
            PreparedStatement haku = con.prepareStatement("SELECT "
                    + "ra.id, ra.nimi, ra.mittayksikko, ra.kuvaus, count(distinct ara.annos_id) AS kaytossa "
                    + "FROM Raakaaine ra LEFT OUTER JOIN AnnosRaakaaine ara "
                    + "ON ra.id = ara.raakaaine_id "
                    + "WHERE ra.id = ? "
                    + "GROUP BY ra.id, ra.nimi, ra.mittayksikko, ra.kuvaus");
            haku.setInt(1, id);
            ResultSet rs = haku.executeQuery();
            if (rs.next()) {
                osuma = populoi(rs);
            }

            rs.close();
            haku.close();
            con.close();
            return osuma;

        } catch (SQLException e) {
            System.out.println("ongelma etsiessä yhtä raaka-ainetta" + e.getMessage());
        }
        return null;
    }
    
    @Override
    public List findAll() throws SQLException {

        List<Raaka_aine> ret = new ArrayList<>();

        try {

            Connection con = getConnection();
            PreparedStatement prep = con.prepareStatement("SELECT "
                    + "ra.id, ra.nimi, ra.mittayksikko, ra.kuvaus, count(distinct ara.annos_id) AS kaytossa "
                    + "FROM Raakaaine ra LEFT OUTER JOIN AnnosRaakaaine ara "
                    + "ON ra.id = ara.raakaaine_id "
                    + "GROUP BY ra.id, ra.nimi, ra.mittayksikko, ra.kuvaus "
                    + "ORDER BY ra.nimi");

            ResultSet r = prep.executeQuery();

            while (r.next()) {
                ret.add(populoi(r));
            }

            r.close();
            prep.close();
            con.close();

            return ret;
        } catch (Exception e) {
            System.out.println("Ongelma findAll-metodissa: " + e);
            return null;
        }
    }

    public List findByNameCaseIns(String hakutermi) throws SQLException {

        // Jos halutaan hakea nimellä joka on jotain sinne päin
        List<Raaka_aine> ret = new ArrayList<>();

        try {

            Connection con = getConnection();
            PreparedStatement prep = con.prepareStatement("SELECT "
                    + "ra.id, ra.nimi, ra.mittayksikko, ra.kuvaus, count(distinct ara.annos_id) AS kaytossa "
                    + "FROM Raakaaine ra LEFT OUTER JOIN AnnosRaakaaine ara "
                    + "ON ra.id = ara.raakaaine_id "
                    + "WHERE ra.nimi like '%?%' "
                    + "GROUP BY ra.id, ra.nimi, ra.mittayksikko, ra.kuvaus "
                    + "ORDER BY ra.nimi");

            prep.setString(1, hakutermi);
            ResultSet r = prep.executeQuery();

            while (r.next()) {
                ret.add(populoi(r));
            }

            r.close();
            prep.close();
            con.close();

            return ret;
        } catch (Exception e) {
            System.out.println("Ongelma findAll-metodissa: " + e);
            return null;
        }
    }

    private Raaka_aine populoi(ResultSet r) throws SQLException {
        Raaka_aine raak = new Raaka_aine();
        raak.setId(r.getInt("id"));
        raak.setNimi(r.getString("nimi"));
        raak.setMittayksikko(r.getString("mittayksikko"));
        raak.setKuvaus(r.getString("kuvaus"));
        raak.setKaytossa(r.getInt("kaytossa"));
        return raak;
    }

    @Override
    public Object saveOrUpdate(Object raakaaine) throws SQLException {
        try {
            Raaka_aine uusi = (Raaka_aine) raakaaine;
            if (findOne(uusi) != null) {
                Raaka_aine vanha = (Raaka_aine) findOne(uusi);
                Connection con = getConnection();
                PreparedStatement paivita = con.prepareStatement(""
                        + "UPDATE Raakaaine SET mittayksikko = ?, kuvaus = ? WHERE nimi = ?");
                paivita.setString(1, uusi.getMittayksikko());
                paivita.setString(2, uusi.getKuvaus());
                paivita.setString(3, uusi.getNimi());
                paivita.executeUpdate();
                paivita.close();
                con.close();

            } else {

                Connection con = getConnection();
                PreparedStatement lisaa = con.prepareStatement("INSERT INTO Raakaaine (nimi, mittayksikko, kuvaus) "
                        + "VALUES (?, ?, ?)");
                lisaa.setString(1, uusi.getNimi());
                lisaa.setString(2, uusi.getMittayksikko());
                lisaa.setString(3, uusi.getKuvaus());

                lisaa.executeUpdate();
                lisaa.close();
                con.close();
            }

        } catch (SQLException e) {
            System.out.println("ongelma lisätessä uutta raaka-ainetta" + e.getMessage());
        }
        return true;

    }

    @Override
    public void delete(Object key) throws SQLException {
        // TODO: poistaminen

        try {
            Raaka_aine pois = (Raaka_aine) key;

            Connection con = getConnection();
            PreparedStatement poista = con.prepareStatement(""
                    + "DELETE FROM Raakaaine "
                    + "WHERE id = ? AND NOT EXISTS "
                    + "(SELECT 1 FROM AnnosRaakaaine WHERE raakaaine_id = ?)");
            poista.setInt(1, pois.getId());
            poista.setInt(2, pois.getId());
            poista.executeUpdate();
            poista.close();
            con.close();

        } catch (SQLException e) {
            System.out.println("Hirveä ongelma poistettaessa raaka-ainetta" + e.getMessage());
        }
    }

}
