package com.smartcalsvendingmachine.ServerProxy;
//EmployeeServer proxy. Invoked whenever the employee needs to communicate with the server.
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class EmployeeClientProxy implements EmployeeClientService {
    private Socket socket;
    private InputStream in;
    private OutputStream out;

    public void connect(String serverIP, int id, String password) throws Exception {
        socket = new Socket(serverIP, Const.EMPLOYEE_PORT);
        in = socket.getInputStream();
        out = socket.getOutputStream();

        authenticate(id, password);
    }

    public String authenticate(int employee, String password) throws Exception {
        String request = Const.AUTHENTICATE + " " + employee + " " + password;
        out.write(request.getBytes());
        out.flush();
        byte[] buffer = new byte[1024];
        int len;
        len = in.read(buffer);
        String reply = new String(buffer, 0, len);

        if (reply.startsWith(Const.OK)) {
            return reply.substring(Const.OK.length());
        } else {
            disconnect();
            throw new Exception(reply.substring(Const.ERROR.length()));
        }
    }

    public String getItemIDs() throws Exception {
        String request = Const.GET_ITEM_IDS;
        out.write(request.getBytes());
        out.flush();
        byte [] buffer=new byte[1024];
        int len;
        len = in.read(buffer);
        String reply = new String(buffer,0,len);

        if (reply.startsWith(Const.OK)) {
            return reply.substring(Const.OK.length());
        } else {
            throw new Exception(reply.substring(Const.ERROR.length()));
        }
    }

    public String getOtherItemIDs(int machine) throws Exception {
        String request = Const.GET_OTHER_ITEM_IDS + " " + machine;
        out.write(request.getBytes());
        out.flush();
        byte [] buffer=new byte[1024];
        int len;
        len = in.read(buffer);
        String reply = new String(buffer,0,len);

        if (reply.startsWith(Const.OK)) {
            return reply.substring(Const.OK.length());
        } else {
            throw new Exception(reply.substring(Const.ERROR.length()));
        }
    }

    public String getItem(int item) throws Exception {
        String request = Const.GET_ITEM + " " + item;
        out.write(request.getBytes());
        out.flush();
        byte [] buffer=new byte[1024];
        int len;
        len = in.read(buffer);
        String reply = new String(buffer,0,len);

        if (reply.startsWith(Const.OK)) {
            return reply.substring(Const.OK.length());
        } else {
            throw new Exception(reply.substring(Const.ERROR.length()));
        }
    }

    public int addMachine(int machineId, String address) throws Exception {
        String request = Const.ADD_MACHINE + " " + machineId + " " + address;
        out.write(request.getBytes());
        out.flush();
        byte [] buffer=new byte[1024];
        int len;
        len = in.read(buffer);
        String reply = new String(buffer,0,len);

        if (reply.startsWith(Const.OK)) {
            return Integer.parseInt(reply.substring(Const.OK.length()));
        } else {
            throw new Exception(reply.substring(Const.ERROR.length()));
        }
    }

    public int checkMachine(int machineId) throws Exception {
        String request = Const.CHECK_MACHINE + " " + machineId;
        out.write(request.getBytes());
        out.flush();
        byte [] buffer=new byte[1024];
        int len;
        len = in.read(buffer);
        String reply = new String(buffer,0,len);

        if (reply.startsWith(Const.OK)) {
            return Integer.parseInt(reply.substring(Const.OK.length()));
        } else {
            throw new Exception(reply.substring(Const.ERROR.length()));
        }
    }

    public void addItemToMachine(int machine, int item, int capacity, int quantity) throws Exception {
        String request = Const.ADD_ITEM_TO_MACHINE + " " + machine + " " + item + " " + capacity + " " + quantity;
        out.write(request.getBytes());
        out.flush();
        byte [] buffer=new byte[1024];
        int len;
        len = in.read(buffer);
        String reply = new String(buffer,0,len);

        if(reply.startsWith(Const.ERROR)){
            throw new Exception(reply.substring(Const.ERROR.length()));
        }
    }

    public void deleteItemFromMachine(int machine, int item) throws Exception {
        String request = Const.DELETE_ITEM_FROM_MACHINE + " " + machine + " " + item;
        out.write(request.getBytes());
        out.flush();
        byte [] buffer=new byte[1024];
        int len;
        len = in.read(buffer);
        String reply = new String(buffer,0,len);

        if(reply.startsWith(Const.ERROR)){
            throw new Exception(reply.substring(Const.ERROR.length()));
        }
    }

    public void updateMachineItemQuantity(int machine, int item, int quantity) throws Exception {
        String request = Const.UPDATE_MACHINE_ITEM_QUANTITY + " " + machine + " " + item + " " + quantity;
        out.write(request.getBytes());
        out.flush();
        byte [] buffer=new byte[1024];
        int len;
        len = in.read(buffer);
        String reply = new String(buffer,0,len);

        if(reply.startsWith(Const.ERROR)){
            throw new Exception(reply.substring(Const.ERROR.length()));
        }
    }

    public String getFile(String path) throws Exception {
        String request = Const.GET_FILE + " " + path;
        out.write(request.getBytes());
        out.flush();
        byte [] buffer=new byte[1024];
        int len;
        len = in.read(buffer);
        String reply = new String(buffer,0,len);

        if (reply.startsWith(Const.OK)) {
            return reply.substring(Const.OK.length());
        } else {
            throw new Exception(reply.substring(Const.ERROR.length()));
        }
    }

    public void disconnect() throws Exception {
        socket.close();
        socket = null;
    }
}
