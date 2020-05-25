package DBOperations;

import java.sql.*;

public class TestOps {
    public void testConnection() {
        try {
            Connection con;

            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            //Activate/use JDBC
            con = DriverManager.getConnection("jdbc:sqlserver://" +
                    "localhost:1433;databaseName=SQLProject;" +
                    "user=Java;password=passwd;");
            System.out.println("Po³¹czono z baz¹ danych");
            con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

            String querySELECT = "SELECT * FROM urzadzenie";
            String queryINSERT = "INSERT INTO urzadzenie VALUES\n" +
                    "(5, 10)";
            String queryDELETE = "DELETE FROM urzadzenie WHERE id = 5;\n";

//          Creating basic statement
            Statement st = con.createStatement();

            // execute the query, and get a java resultset
            ResultSet rs = st.executeQuery(querySELECT);


            // iterate through the java resultset
            while (rs.next()) {
                String id = rs.getString("id");
                String ip_stacji = rs.getString("ip_stacji");

                // print the results
                System.out.format("%s, %s\n", id, ip_stacji);
            }

            st.close();
            con.close();
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        try {
            Connection con;

            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            //Activate/use JDBC
            con = DriverManager.getConnection("jdbc:sqlserver://" +
                    "localhost:1433;databaseName=SQLProject;" +
                    "user=Java;password=passwd;");
            System.out.println("Po³¹czono z baz¹ danych");
            con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            return con;
        } catch (Exception e) {
            System.err.println("ERROR CONNECTING TO DATABASE");
            e.printStackTrace();
            return null;
        }
    }

    public int getColumnSize(Connection con, String tableName, String columnName) throws SQLException {
        Statement st = con.createStatement();
        int rows = 0;

        String querySELECT = "SELECT " + columnName + " FROM " + tableName;
        ResultSet resultSet = st.executeQuery(querySELECT);

        //Gets the number of rows in resultset
        while (resultSet.next()) {
            rows++;
        }
//        int rows = resultSet.getRow();

        resultSet.close();

        return rows;
    }

    public String[] getColumnData(Connection con, String tableName, String columnName) throws SQLException {
        String[] output;
        int i = 0;
        Statement st = con.createStatement();

        String querySELECT = "SELECT " + columnName + " FROM " + tableName;
        ResultSet resultSet = st.executeQuery(querySELECT);

        //Gets the number of rows in resultset
        int rows = 0;
        while (resultSet.next()) {
            rows++;
        }


        //Now output table is the same size as data provided
        output = new String[rows];

        //Returns to beginning so .next() method works
        ResultSet resultSet2 = st.executeQuery(querySELECT);

        //Fills output table with data from resultSet
        while (resultSet2.next()) {
            String currRow = resultSet2.getString(columnName);
            output[i] = currRow;
            i++;
        }

        return output;
    }
}
