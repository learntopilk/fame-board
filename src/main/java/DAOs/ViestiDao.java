package DAOs;

import java.sql.SQLException;
import java.util.List;
import java.sql.*;
import static Webserver.Database.getConnection;

import Webserver.Viesti;
import java.util.ArrayList;

public class ViestiDao implements Dao {

    public ViestiDao() throws SQLException {

        try {
            Connection conn = getConnection();

            PreparedStatement tableCreator = conn.prepareStatement("CREATE TABLE IF NOT EXISTS Viesti("
                    + " id SERIAL PRIMARY KEY, "
                    + " otsikko varchar(160),"
                    + " sisalto varchar(3000), "
                    // CHANGE THIS: Long instead of date
                    + " luomisaika bigint, "
                    + " url_kuva varchar(300), "
                    + " kayttajanNimi varchar(40)"
                    + " keskustelu_id integer, "
                    + " kayttaja_id integer,"
                    + " FOREIGN KEY(keskustelu_id) REFERENCES Keskustelu(id),"
                    + " FOREIGN KEY(kayttaja_id) REFERENCES Kayttaja(id))");

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
        List<Viesti> a = new ArrayList<>();
        
        try {
            Connection conn = getConnection();
            
            PreparedStatement getter = conn.prepareStatement("SELECT * FROM Viesti;");
            
            ResultSet rs = getter.executeQuery();
            
            
            rs.close();
            getter.close();
            conn.close();
            
            
        } catch (SQLException e) {
            System.out.println("Problem saving message: " + e);
        }
        
        return a;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
            saver.setLong(2, v.getLuomisaika());
            saver.setString(3, v.getKuvanURL());
            saver.setString(4, v.getLuoja());
            saver.setInt(5, v.getKeskustelu_id());
            saver.setInt(6, v.getKayttajaId());
            
            saver.executeUpdate();
            
            saver.close();
            conn.close();


            return true;
        } catch (Exception e) {

        }
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(Object key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
