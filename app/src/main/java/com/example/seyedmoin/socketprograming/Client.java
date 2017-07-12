package com.example.seyedmoin.socketprograming;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

public class Client extends AsyncTask<String, String, String> {

    String dstAddress;
    int dstPort;
    String response = "";
    Socket socket;

    String Massage;
    OutputStream out;

    PrintWriter output;

    public AsyncResult asyncResult = null;

    Client(String addr, int port, AsyncResult asyncResult) {
        dstAddress = addr;
        dstPort = port;
        this.asyncResult = asyncResult;
    }

    @Override
    protected String doInBackground(String... params) {

        socket = null;
        Massage = params[0];

        try {
            SocketAddress socketAddress = new InetSocketAddress(dstAddress, dstPort);
            socket = new Socket();
            //socket.setTcpNoDelay(false);
            //socket.setSoTimeout(5000);
            socket.connect(socketAddress);

            if (socket.isConnected())
                Log.e("Client", "C: Connected");

            out = socket.getOutputStream();
            output = new PrintWriter(out);

            output.println(Massage);
            output.flush();
            out.flush();

            char[] buffer = new char[2048];
            int charsRead = 0;
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while ((charsRead = in.read(buffer)) != -1) {
                response = new String(buffer).substring(0, charsRead);
                Log.e("In While", "msg :" + response);
                out.close();
            }


        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            if (socket != null) {
                try {
                    socket.close();
                    Log.e("Client", "close");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response;
    }

    @Override
    protected void onPostExecute(String result) {
        //Log.e("POST:", result);
        asyncResult.onResponse(result);
        super.onPostExecute(result);
    }

}