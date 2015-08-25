package com.smartcalsvendingmachine.ServerProxy;
//ClientServer interface, defining common client side service features
public interface ClientService {

    public void connect(String serverIP, int id, String password) throws Exception;

    public void disconnect() throws Exception;

}
