package org.example.service;
import org.example.service.entity.Rider;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RiderServiceDB {
    //read data from RideRepository table
    public List<Rider> readData(){
        String sqlQuery= "select * from RideRepository;";
        return  this.RiderDataUsingDB(sqlQuery);
    }
    // retrieve total no. of ride,avg fare and total fares and populate it in map
    public Map<String, Object> retrieveRidersDBInvoice() throws Exception {
        Map<String,Object> riderMap= new HashMap<>();
        String sqlQuery="select count(UserID) as Total_Ride, Avg(Fare) as Avg_Fare , sum(Fare) as Total_Fare from RideRepository;";
        try(Connection connection= getConnection()){
            Statement statement= connection.createStatement();
            ResultSet resultSet= statement.executeQuery(sqlQuery);
            while(resultSet.next()) {
                int totalRideNo = resultSet.getInt("Total_Ride");
                Double avgFare = resultSet.getDouble("Avg_Fare");
                Double totalFare = resultSet.getDouble("Total_Fare");
                riderMap.put("TotalRideNo", totalRideNo);
                riderMap.put("AvgFare", avgFare);
                riderMap.put("TotalFare", totalFare);
            }
        }
        return riderMap;
    }
    // retrieve Invoice group by UserID and populate it in list
    public List<Map<String, Object>> retrieveRidersDBInvoiceGroupBYID() throws Exception{
        List<Map<String,Object>> riderMapList=new ArrayList<>();
        String sqlQuery="select UserID, count(UserID) as Total_Ride, Avg(Fare) as Avg_Fare , sum(Fare) as Total_Fare  from RideRepository group by UserID;";
        try(Connection connection= getConnection()){
            Statement statement= connection.createStatement();
            ResultSet resultSet= statement.executeQuery(sqlQuery);
            while(resultSet.next()){
                Map<String,Object> riderEntry= new HashMap<>();
                int UserID= resultSet.getInt("UserID");
                int totalRideNo = resultSet.getInt("Total_Ride");
                Double avgFare = resultSet.getDouble("Avg_Fare");
                Double totalFare = resultSet.getDouble("Total_Fare");
                riderEntry.put("UserID",UserID);
                riderEntry.put("TotalRideNo",totalRideNo);
                riderEntry.put("AvgFare",avgFare);
                riderEntry.put("TotalFare",totalFare);
                riderMapList.add(riderEntry);
            }
        }
        return riderMapList;
    }
    // retrieve the data of table using execution of sqlaQuery
    private List<Rider> RiderDataUsingDB(String sqlQuery) {
        List<Rider> riderList= new ArrayList<>();
        try(Connection connection= getConnection()){
            Statement statement= connection.createStatement();
            ResultSet resultSet= statement.executeQuery(sqlQuery);
            riderList= this.getRiderData(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return riderList;
    }
    // populating list using resultset
    private List<Rider> getRiderData(ResultSet resultSet) {
        List<Rider> riderList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                int UserID= resultSet.getInt("UserID");
                String rideType= resultSet.getString("RideType");
                Double distance= resultSet.getDouble("Distance(in Km)");
                Double time= resultSet.getDouble("Time(in min)");
                Double fare= resultSet.getDouble("Fare");
                riderList.add(new Rider(UserID,rideType,distance,time,fare));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return riderList;
    }

    // establish connection with database jdbc url
    private Connection getConnection(){
        String jdbcURL = "jdbc:mysql://localhost:3306/invoice_generator?useSSL=false&allowPublicKeyRetrieval=true";
        String username = "root";
        String password = "Vivek123";
        Connection connection;
        try {
            connection= DriverManager.getConnection(jdbcURL,username,password);
            System.out.println("Connection established!!!");
        }
        catch (SQLException e) {
            throw new RuntimeException();
        }
        return connection;
    }
}