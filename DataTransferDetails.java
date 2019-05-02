package com.data;

import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DataTransferDetails {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(DataTransferDetails.class);
    PreparedStatement ps = null;
    ResultSet rs = null;

    public List<TableColumnPojo> getSybasedetails(PropertiesPojo pj, Connection connection){
        List<TableColumnPojo> SybaseTabledetails = new ArrayList<>();
        TableColumnPojo tcp;
        try {
            StringBuilder SybaseQueryString = new StringBuilder(pj.getSybasequery());
            ps = connection.prepareStatement(SybaseQueryString.toString());
            rs = ps.executeQuery();
            if(rs != null){
                while (rs.next()){
                    tcp = new TableColumnPojo();
                    tcp.setAddress(rs.getString("1"));
                    tcp.setAge(rs.getInt("2"));
                    tcp.setName(rs.getString("3"));
                    tcp.setId(rs.getString("4"));
                    SybaseTabledetails.add(tcp);
                }

            }else{
                logger.info("There is no data in the Sybase table.");
            }
            connection.close();
        }catch (Exception ex){
            logger.error("An Error retrieving sybase data. \n" + ex.getMessage());
        }finally {
            if(rs != null){
                try{
                    rs.close();
                }catch (Exception ex) {
                    logger.error("An Error closing sybase connection. \n" + ex.getMessage());
                }
            }
            if(ps != null){
                try{
                    ps.close();
                }catch (Exception ex) {
                    logger.error("An Error closing sybase prepared connection. \n" + ex.getMessage());
                }
            }
            if(connection != null){
                try{
                    connection.close();
                }catch (Exception ex) {
                    logger.error("An Error closing sybase connection. \n" + ex.getMessage());
                }
            }
        }
        return SybaseTabledetails;
    }


    public void insertDetailsIntoOracle(List<TableColumnPojo> list, PropertiesPojo pj, Connection connection) {

        final int batchSize = 1000;
        int count = 0;
        try {
            if(list.size() > 0){
                String OracleDetails = pj.getOraclequery();
                ps = connection.prepareStatement(OracleDetails);
                for(int data = 0; data < list.size(); data ++){
                    ps.setString(1, list.get(data).getAddress());
                    ps.setInt(2, list.get(data).getAge());
                    ps.setString(3, list.get(data).getName());
                    ps.setString(1, list.get(data).getId());
                    ps.addBatch();

                    if(++count % batchSize == 0) {
                        ps.executeBatch();
                    }
                }
                ps.executeBatch(); //Execute the rest of the batch if it is not as much as 1000
                logger.error("Done inserting data into table.");
            }else{
                logger.error("No data to insert into table.");
            }
            connection.close();
        } catch (Exception ex) {
            logger.error("An Error while inserting data into Oracle. \n" + ex.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception ex) {
                    logger.error("An Error closing oracle prepared connection. \n" + ex.getMessage());
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception ex) {
                    logger.error("An Error closing oracle connection. \n" + ex.getMessage());
                }
            }

        }
    }


 /*   import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.microsoft.sqlserver.jdbc.SQLServerBulkCopy;

    public class BulkCopySingle {
        public static void main(String[] args) {
            String connectionUrl = "jdbc:sqlserver://<server>:<port>;databaseName=AdventureWorks;user=<user>;password=<password>";
            String destinationTable = "dbo.BulkCopyDemoMatchingColumns";
            int countBefore, countAfter;
            ResultSet rsSourceData;

            try (Connection sourceConnection = DriverManager.getConnection(connectionUrl);
                 Connection destinationConnection = DriverManager.getConnection(connectionUrl);
                 Statement stmt = sourceConnection.createStatement();
                 SQLServerBulkCopy bulkCopy = new SQLServerBulkCopy(destinationConnection)) {

                // Empty the destination table.
                stmt.executeUpdate("DELETE FROM " + destinationTable);

                // Perform an initial count on the destination table.
                countBefore = getRowCount(stmt, destinationTable);

                // Get data from the source table as a ResultSet.
                rsSourceData = stmt.executeQuery("SELECT ProductID, Name, ProductNumber FROM Production.Product");

                // In real world applications you would
                // not use SQLServerBulkCopy to move data from one table to the other
                // in the same database. This is for demonstration purposes only.

                // Set up the bulk copy object.
                // Note that the column positions in the source
                // table match the column positions in
                // the destination table so there is no need to
                // map columns.
                bulkCopy.setDestinationTableName(destinationTable);

                // Write from the source to the destination.
                bulkCopy.writeToServer(rsSourceData);

                // Perform a final count on the destination
                // table to see how many rows were added.
                countAfter = getRowCount(stmt, destinationTable);
                System.out.println((countAfter - countBefore) + " rows were added.");
            }
            // Handle any errors that may have occurred.
            catch (SQLException e) {
                e.printStackTrace();
            }
        }

        private static int getRowCount(Statement stmt,
                                       String tableName) throws SQLException {
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM " + tableName);
            rs.next();
            int count = rs.getInt(1);
            rs.close();
            return count;
        }
    }*/
}
