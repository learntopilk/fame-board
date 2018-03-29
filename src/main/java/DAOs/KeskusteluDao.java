package DAOs;

import java.sql.SQLException;
import java.util.List;
import static Webserver.Database.getConnection;

import Webserver.Keskustelu;
import java.sql.Connection;
import java.sql.PreparedStatement;


public class KeskusteluDao implements Dao {
    
    public KeskusteluDao(){
        try {
            Connection conn = getConnection();

            PreparedStatement KeskusteluCreator = conn.prepareStatement("CREATE TABLE IF NOT EXISTS Keskustelu("
                    + " id SERIAL PRIMARY KEY, "
                    + " otsikko varchar(160),"
                    + "luomisaika bigint,"
                    + " kayttaja_id integer,"
                    + " FOREIGN KEY(kayttaja_id) REFERENCES Kayttaja(id))");

            KeskusteluCreator.executeUpdate();
            KeskusteluCreator.close();
            System.out.println("KeskusteluDao initialized.");

        } catch (Exception e) {
            System.out.println("Problem creating or initializing ViestiDao: " + e);
        }
    }

    @Override
    public Keskustelu findOne(Object key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List findAll() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Boolean saveOrUpdate(Object object) throws SQLException {
        
        try {
            Keskustelu k = (Keskustelu) (object);
            Connection conn = getConnection();
            PreparedStatement prep = conn.prepareStatement("");
            
            prep.executeUpdate();
            
            prep.close();
            conn.close();
            
            return true;
            
        }catch (Exception e) {
            System.out.println("Exception while saving Keskustelu:" + e);
            return false;
        }
    }

    @Override
    public void delete(Object key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
