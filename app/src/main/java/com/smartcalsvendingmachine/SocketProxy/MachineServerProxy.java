package com.smartcalsvendingmachine.SocketProxy;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class MachineServerProxy implements MachineServer {
	private Socket socket;
	private InputStream in;
	private OutputStream out;

	public void connect(String serverIP) throws Exception {
		socket = new Socket(serverIP, Const.SERVER_PORT);
		in = socket.getInputStream();
		out = socket.getOutputStream();
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
	
	public String getUpdatedIDs(int machine) throws Exception {
		String request = Const.GET_UPDATED_IDS + " " + machine;
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
	
	public void addSale(int machine, int item, double profit, String date) throws Exception {
		String request = Const.ADD_SALE + " " + machine + " " + item + " " + profit + " " + date;
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

    public void updateSyncDate(int machine) throws Exception {
        String request = Const.UPDATE_SYNC_DATE + " " + machine;
        out.write(request.getBytes());
        out.flush();
        byte [] buffer=new byte[1024];
        int len;
        len = in.read(buffer);
        String reply = new String(buffer,0,len);

        if (reply.startsWith(Const.ERROR)) {
            throw new Exception(reply.substring(Const.ERROR.length()));
        }
    }
	
	public void disconnect() throws Exception {
        socket.close();
        socket = null;
	}
}
