package DAOs;

import java.sql.*;
import static Webserver.Database.getConnection;


public class resetDao {

    public void dropTables() throws SQLException {
        Connection con = getConnection();
        PreparedStatement dropOld = con.prepareStatement("DROP TABLE AnnosRaakaaine CASCADE");
        dropOld.executeUpdate();
        dropOld = con.prepareStatement("DROP TABLE Annos CASCADE");
        dropOld.executeUpdate();
        dropOld = con.prepareStatement("DROP TABLE Raakaaine CASCADE");
        dropOld.executeUpdate();
        dropOld.close();
        con.close();
    }

}
