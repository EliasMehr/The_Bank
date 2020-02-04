package Repository;

import java.sql.*;

public class DBConnection {

    public static Connection connection;

    static {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/the_bank", "root", "root");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        return connection;
    }

    public static void setConnection(Connection connection) {
        DBConnection.connection = connection;
    }


    public static boolean columnExists(ResultSet resultSet, String columnName) throws SQLException {
        ResultSetMetaData meta = resultSet.getMetaData();
        int nrOfColumns = meta.getColumnCount();

        for (int i = 1; i <= nrOfColumns; i++) {
            if (meta.getColumnName(i).equals(columnName))
                return true; //Column name found in result set
        }

        //Column name was not found in result set
        return false;
    }
}
