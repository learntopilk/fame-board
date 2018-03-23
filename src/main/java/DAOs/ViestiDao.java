package DAOs;

import java.sql.SQLException;
import java.util.List;
import java.sql.*;
import static Webserver.Database.getConnection;

import Webserver.Viesti;

public class ViestiDao implements Dao {

    public ViestiDao() throws SQLException {

        try {
            Connection conn = getConnection();

            PreparedStatement tableCreator = conn.prepareStatement("CREATE TABLE IF NOT EXISTS Viesti("
                    + " id SERIAL PRIMARY KEY, "
                    + " otsikko varchar(160),"
                    + " sisalto varchar(3000), "
                    // CHANGE THIS: Long instead of date
                    + " luomisaika date, "
                    + " url_kuva varchar(300), "
                    + " kayttajanNimi varchar(40)"
                    + " keskustelu_id integer, "
                    + " kayttaja_id integer,"
                    + " FOREIGN KEY(keskustelu_id) REFERENCES Keskustelu(id),"
                    + " FOREIGN KEY(kayttaja_id) REFERENCES Kayttaja(id)");

            tableCreator.executeUpdate();
            tableCreator.close();
            System.out.println("ViestiDao initialized.");

        } catch (Exception e) {
            System.out.println("Problem creating or initializing ViestiDao: " + e);
        }

    }

    @Override
    public Viesti findOne(Object key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List findAll() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List findAllByUsername(String username) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List findAllByUserId(int id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Boolean saveOrUpdate(Object viesti) throws SQLException {
        try {
            Viesti v = (Viesti) viesti;
            Connection conn = getConnection();

            PreparedStatement saver = conn.prepareStatement("INSERT INTO Viesti("
                    + "otsikko, "
                    + "sisalto, "
                    + "luomisaika, "
                    + "url_kuva, "
                    + "kayttajanNimi,"
                    + "keskustelu_id,"
                    + "kayttaja_id) "
                    + "VALUES(?,?,?,?,?,?,?)");
            
            saver.setString(0, v.getOtsikko());
            saver.setString(1, v.getSisalto());
            // CONVERT THIS TO USE LONG INSTEAD OF DATE
            //saver.setDate(2, v.getLuomisaika());

        } catch (Exception e) {

        }
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(Object key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
