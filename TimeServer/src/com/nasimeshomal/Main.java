package com.nasimeshomal;

import org.joda.time.DateTime;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket=new ServerSocket(10109);

        while (true)
        {
            Socket socket=serverSocket.accept();
            OutputStream outputStream=socket.getOutputStream();

            ObjectOutputStream objectOutputStream=new ObjectOutputStream(outputStream);

            LocalTime localTime=new LocalTime();
            localTime.time=new DateTime();

            objectOutputStream.writeObject(localTime);
            outputStream.flush();

            outputStream.close();
            socket.close();
        }
    }
}
