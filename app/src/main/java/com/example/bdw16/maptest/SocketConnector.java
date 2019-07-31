package com.example.bdw16.maptest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class SocketConnector {

    private Socket socket = null;

    private String host;
    private int port;

    public SocketConnector(String host, int port) {

        this.host = host;
        this.port = port;

        runReceivePoll();

    }

    private boolean connect() {
        // establish socket connection to server
        try {
            socket = new Socket(host, port);
        } catch (UnknownHostException e) {
            System.out.println("Couldn't establish socket connection");
            return false;
        } catch (IOException e) {
            System.out.println("IO exception on attempting to connect socket");
            return false;
        }
        return true;
    }



    public void send(final String data) {

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                if (socket == null || socket.isClosed()) if (!connect()) return;

                try {
                    OutputStream os;
                    os = socket.getOutputStream();
                    os.write(data.getBytes());
                    os.close();
                } catch (IOException e) {
                    System.out.println("IO exception trying to write to socket");
                    return;
                }
            }

        });
        t.start();

    }


    public void runReceivePoll() {

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                receive();
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();

    }

    private void receive() {

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                if (socket == null || socket.isClosed()) if (!connect()) return;

                try {
                    InputStream is;
                    is = socket.getInputStream();

                    Scanner s = new Scanner(is);

                    Networker.receiveData(s);

                    is.close();
                } catch (IOException e) {
                    System.out.println("IO exception trying to write to socket");
                    return;
                }

            }
        });
        t.start();


    }


}