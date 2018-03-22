package DAOs;

import Webserver.Annos;
import Webserver.Database;
import static Webserver.Database.getConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AnnosDao implements Dao {

    public AnnosDao() throws SQLException {
        try {
            Connection con = getConnection();
            PreparedStatement createTable = con.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS Annos ("
                    + "id SERIAL PRIMARY KEY,"
                    + "nimi varchar(50),"
                    + "valmistusohje varchar(500)"
                    + ");");
            createTable.executeUpdate();
            createTable.close();
            con.close();

        } catch (Exception e) {
            System.out.println("ongelma luodessa Annos-taulua: " + e.getMessage());
        }
    }

    public Annos findByName(Object key) throws SQLException {
        try {
            String etsittava = (String) key;
            Connection connection = getConnection();
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Annos WHERE nimi = ?");
            stmt.setString(1, etsittava);

            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                return null;
            }

            Annos annos = new Annos(rs.getInt("id"), rs.getString("nimi"), rs.getString("valmistusohje"));
            return annos;

        } catch (SQLException e) {
            System.out.println("ongelma etsiessä yhtä annosta" + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Annos> findAll() throws SQLException {
        List<Annos> annokset = new ArrayList<>();

        Connection con = getConnection();
        PreparedStatement haeKaikki = con.prepareStatement("SELECT * FROM Annos");
        ResultSet rs = haeKaikki.executeQuery();

        while (rs.next()) {
            annokset.add(new Annos(rs.getInt("id"), rs.getString("nimi"), rs.getString("valmistusohje")));
        }

        rs.close();
        haeKaikki.close();
        con.close();

        return annokset;

    }

    @Override
    public Annos saveOrUpdate(Object key) throws SQLException {
        try {
            Annos talletettava = (Annos) key;
            Connection connection = getConnection();

            if (findOne(talletettava) == null) {

                PreparedStatement stmt = connection.prepareStatement(""
                        + "INSERT INTO Annos (nimi, valmistusohje) VALUES (?, ?)");
                stmt.setString(1, talletettava.getNimi());
                stmt.setString(2, talletettava.getValmistusohje());

                stmt.executeUpdate();
                stmt.close();
                connection.close();

            } else {
                PreparedStatement paivita = connection.prepareStatement("UPDATE Annos SET "
                        + "valmistusohje = ? WHERE nimi = ?");
                paivita.setString(1, talletettava.getValmistusohje());
                paivita.setString(2, talletettava.getNimi());
                paivita.executeUpdate();
                paivita.close();
                connection.close();
            }

        } catch (SQLException e) {
            System.out.println("ongelma lisättäessä/päivitettäessä yhtä annosta" + e.getMessage());
        }
        return null;
    }

    @Override
    public void delete(Object key) throws SQLException {

        Annos poistettava = (Annos) this.findOne(key);

        AnnosRaakaaineDao ARADao = new AnnosRaakaaineDao();
        ARADao.deleteAll(poistettava.getId());

        Connection con = getConnection();
        PreparedStatement poista = con.prepareStatement("DELETE FROM Annos WHERE id = ?");
        poista.setInt(1, poistettava.getId());
        poista.executeUpdate();
        poista.close();
        con.close();
    }

    public void delete(int annosId, AnnosRaakaaineDao ARADao) throws SQLException {

        ARADao.deleteAll(annosId);

        Connection con = getConnection();
        PreparedStatement poista = con.prepareStatement("DELETE FROM Annos WHERE id = ?");
        poista.setInt(1, annosId);
        poista.executeUpdate();
        poista.close();
        con.close();
    }
    @Override
    public Annos findOne(Object key) throws SQLException {
        Annos etsittava = (Annos) key;
        Annos osuma = null;
        Connection con = getConnection();
        PreparedStatement etsiYksi = con.prepareStatement("SELECT * FROM Annos WHERE nimi = (?)");
        etsiYksi.setString(1, etsittava.getNimi());
        ResultSet rs = etsiYksi.executeQuery();

        if (rs.next()) {
            osuma = new Annos(rs.getInt("id"), rs.getString("nimi"), rs.getString("valmistusohje"));
        }

        rs.close();
        etsiYksi.close();
        con.close();

        return osuma;

    }
    
    public Object findById(int annosId) throws SQLException {
        Annos osuma = null;
        Connection con = getConnection();
        PreparedStatement etsiYksi = con.prepareStatement("SELECT * FROM Annos WHERE id = ?");
        etsiYksi.setInt(1, annosId);
        ResultSet rs = etsiYksi.executeQuery();

        if (rs.next()) {
            osuma = new Annos(rs.getInt("id"), rs.getString("nimi"), rs.getString("valmistusohje"));
        }

        rs.close();
        etsiYksi.close();
        con.close();

        return osuma;

    }

}
