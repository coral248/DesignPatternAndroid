package com.smartcalsvendingmachine.SocketProxy;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class CustomerServerProxy implements CustomerServer {
    private Socket socket;
    private InputStream in;
    private OutputStream out;

    public void connect(String serverIP) throws Exception {
        socket = new Socket(serverIP, Const.SERVER_PORT);
        in = socket.getInputStream();
        out = socket.getOutputStream();
    }

    public int buyCard(double balance) throws Exception {
        String request = Const.BUY_CARD + " " + balance;
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

    public double checkBalance(int card) throws Exception {
        String request = Const.CHECK_BALANCE + " " + card;
        out.write(request.getBytes());
        out.flush();
        byte [] buffer=new byte[1024];
        int len;
        len = in.read(buffer);
        String reply = new String(buffer,0,len);

        if (reply.startsWith(Const.OK)) {
            return Double.parseDouble(reply.substring(Const.OK.length()));
        } else {
            throw new Exception(reply.substring(Const.ERROR.length()));
        }
    }

    public double updateBalance(int card, double deduction) throws Exception {
        String request = Const.UPDATE_BALANCE + " " + card + " " + deduction;
        out.write(request.getBytes());
        out.flush();
        byte [] buffer=new byte[1024];
        int len;
        len = in.read(buffer);
        String reply = new String(buffer,0,len);

        if (reply.startsWith(Const.OK)) {
            return Double.parseDouble(reply.substring(Const.OK.length()));
        } else {
            throw new Exception(reply.substring(Const.ERROR.length()));
        }
    }

    public void disconnect() throws Exception {
        socket.close();
        socket = null;
    }
}
