package com.smartcalsvendingmachine.ServerProxy;
//MachineService interface, defining an interface for machine-server communication
public interface MachineService {

    public int checkMachine(int machine) throws Exception;

    public String getUpdatedIDs(int machineid) throws Exception;

    public String getItem(int item) throws Exception;

    public String getFile(String path) throws Exception;

    public void updateMachineItemQuantity(int machineid, int itemid,
                                          int quantity) throws Exception;

    public void addSale(int machineid, int itemid, double profit, String date) throws Exception;

    public void updateSyncDate(int machineid) throws Exception;

}