package org.example.service;
import org.example.service.entity.Rider;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RiderService {
    public RiderService(){};
    public enum IOService{CONSOLE_IO,FILE_IO,DB_IO,REST_IO};
    private List<Rider> riderList;
    RiderServiceDB riderServiceDB = new RiderServiceDB();
    // read data of the rideRepository table
    public List<Rider> readRidersList(IOService ioService) {
        if(ioService.equals(IOService.DB_IO))
            this.riderList= riderServiceDB.readData();
        return riderList;
    }
    // read Invoice generated of all the rides
    public Map<String, Object> retriveRidersInvoice() throws Exception {
        Map<String,Object> riderMap= new HashMap<>();
        riderMap= riderServiceDB.retrieveRidersDBInvoice();
        return riderMap;
    }
    // read Invoice generated group by userID
    public List<Map<String, Object>> retriveRidersInvoiceGroupByID() throws Exception {
        List<Map<String, Object>> riderMapList= new ArrayList<>();
        riderMapList= riderServiceDB.retrieveRidersDBInvoiceGroupBYID();
        return riderMapList;
    }
}