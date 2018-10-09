package bel.l4d.task3;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class SqlWorker {

    private Connection sqlConnection = null;
    private String dataSource = "";
    private String dbName = "";
    private String sqlUser = "";
    private String sqlPass = "";

    public SqlWorker(String sqlURL, String dbName, String sqlUser, String sqlPassword) throws Exception {
        dataSource = sqlURL;
        this.dbName = dbName;
        this.sqlUser = sqlUser;
        sqlPass = sqlPassword;

        establishConnection();
    }

    public void closeConnection() throws Exception {
        try {
            sqlConnection.close();
        } catch (SQLException e) {
            throw new Exception(e.getMessage() + "\r\n" + "Error: Cannot close SQL connection.");
        }
    }

    private void establishConnection() throws Exception {
        try {
            sqlConnection = DriverManager.getConnection(dataSource, sqlUser, sqlPass) ;
        } catch (SQLException e) {
            throw new Exception(e.getMessage() + "\r\n" + "SQL connection error: Cannot connect to database " + dataSource + " with User: \"" + sqlUser + "\" and Pass: \"" + sqlPass + "\".");
        }
    }

    public ArrayList<Book> getBooksByAuthor(String Author) throws Exception {

        ArrayList<Book> books = new ArrayList<>();

        String strQuery =
            "SELECT "+
                "id_book, title, year "+
            "FROM "+
                dbName+".books,"+
                dbName+".authors,"+
                dbName+".books_authors "+
            "WHERE "+
                "id_book = book_id "+
                    "AND author_id = id_author " +
                    "AND name = \"" + Author + "\"";   //Using double quotes to avoid ' in name of author

        try (
                Statement statement = sqlConnection.createStatement();
                ResultSet result = statement.executeQuery(strQuery);
        ) {
            while(result.next()) {
                books.add(new Book(result.getString("title"),result.getInt("year")));
            }
            return books;
        } catch (SQLException e) {
            throw new Exception(e.getMessage() + "\r\n" + "Error: Cannot create statement or execute SQL query.");
        }

    }

}
