package DBOperations;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

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
}
