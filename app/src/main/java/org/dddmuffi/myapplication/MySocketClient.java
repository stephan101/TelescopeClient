package org.dddmuffi.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.util.Date;

/**
 * Created by SuStevieDani on 15.11.2015.
 */


 public class MySocketClient extends AsyncTask<Void, Void, Void> {


    String dstAddress;
    int dstPort;
    String response;
    String exeptionmessage;

    ByteBuffer dstmessage;
    Context context = App.getContext();
    MySocketClient(String addr, int port, ByteBuffer bb) {
        dstAddress = addr;
        dstPort = port;
        dstmessage = bb;
    }
    MySocketClient( ByteBuffer bb) {
        dstmessage = bb;
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        dstAddress=(sharedPrefs.getString("IPAdresse", "localhost"));
        dstPort=Integer.parseInt((sharedPrefs.getString("Port", "10000")));
    }
    @Override
    protected Void doInBackground(Void... arg0) {
       Socket socket = null;
        DataOutputStream dataOutputStream = null;
        DataInputStream dataInputStream = null;
        response="";
        exeptionmessage="";

        int c;
        try {
            socket = new Socket(dstAddress, dstPort);
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(
                    socket.getOutputStream());
            if(dstmessage != null) {
                WritableByteChannel channel = Channels.newChannel(dataOutputStream);
                channel.write(dstmessage);
            }
            try {
;
                while ((c=dataInputStream.read()) != -1) {
                    if (c !=  0){
                        response=response+(char) c;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                exeptionmessage = "IOExceptionReadInputStream: " + e.toString();
             };
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            exeptionmessage = "UnknownException: " + e.toString();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            exeptionmessage = "IOException: " + e.toString();
        } finally {IOException
            dstmessage=null;
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (dataOutputStream != null) {
                try {
                    dataOutputStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (dataInputStream != null) {
                try {
                    dataInputStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        return null;
    }
    protected void onPreExecute() {
        super.onPreExecute();

    }
    @Override
    protected void onPostExecute(Void result) {
        Date d = new Date();
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString("SocketResponse",response);
        editor.putString("SocketError",d+": "+exeptionmessage);
        editor.commit();
    }
};