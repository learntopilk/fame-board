package Webserver;

//Luokka tietokannan getConnection-metodia varten (ja yleisesti tietokannan
//alkusointuja varten
import java.sql.*;
import java.sql.DriverManager;

public class Database {

    // Yrittää yhteyttä PostGres-tietokantaan. Jos sitä ei ole, ottaa yhteyden paikalliseen tietokantaan.
    //Huom, muutin Exceptionin SQLExceptioniksi, muuten tuli erroria metodia kutsuttaessa.
    public static Connection getConnection() throws SQLException {
        String dbUrl = System.getenv("JDBC_DATABASE_URL");
        if (dbUrl != null && dbUrl.length() > 0) {
            return DriverManager.getConnection(dbUrl);
        }

        return DriverManager.getConnection("jdbc:sqlite:viestikanta.db");
    }

}
