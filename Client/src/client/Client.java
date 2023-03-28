/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author eltix
 */
     class FunctionIntegral 
     {
        public double f(double x) 
        {
            double F=1/x;
            return F;
        }
    }
     

    class JThread extends Thread
    {
        int a;
        int b;
        float c;
        int num;
        DatagramSocket socket;
        InetAddress address;
        JThread(String name, int _a, int _b, float _c, int _n , DatagramSocket _socket, InetAddress _address)
        {
             super(name);
            a = _a;
            b = _b;
            c = _c;
            num = _n;
            socket = _socket;
            address = _address; 
        }
        
        public void run()
        {
            FunctionIntegral funk = new FunctionIntegral();
                int j;
                double result=0;
                int n = (int)((b-a)/c);
                for(j = 1; j <= n; j++)
                {
                     result += funk.f(a + j *c)* c;
                }

                if(n*c < b - a)
                {
                    float newstep = (b - a) - n*c;
                    result += funk.f(b + newstep);
                }
                
                 String message =  String.valueOf(result) + " " + String.valueOf(num);
                byte[]  buf = message.getBytes();
            DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 26);
            try {
                socket.send(packet);
                System.out.print("dadadadad ");
            } catch (IOException ex) {
                Logger.getLogger(JThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

public class Client {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SocketException, IOException {
          DatagramSocket socket = new DatagramSocket(17);
          DatagramSocket socketSend = new DatagramSocket();
          InetAddress address = InetAddress.getByName("localhost");
        while(true){
            byte[] buffer = new byte[256];
            DatagramPacket request = new DatagramPacket(buffer, buffer.length);
            socket.receive(request);
            if(request.getLength() != 0){
                String Message =  new String(request.getData(), 0, request.getLength());
               String strTop    = "",
                      strLower  = "",
                      strStep   = "",
                      strNum   = "";
               
               int size = Message.length();
               
               int j = 0;
               while (Message.charAt(j) != ' ')
               {
                   strTop += Message.charAt(j);
                   j++;
               }
               j++;
           
               
               while (Message.charAt(j) != ' ')
               {
                   strLower += Message.charAt(j);
                   j++;
               }
               j++;
          
               
                while (Message.charAt(j) != ' ')
               {
                   strStep += Message.charAt(j);
                   j++;
               }
               j++;
    
               
               while (j != size)
               {
                   strNum += Message.charAt(j);
                   j++;
               }
               
                JThread MyThread = new JThread("MyThread", Integer.parseInt(strTop),Integer.parseInt(strLower),Float.parseFloat(strStep), Integer.parseInt(strNum), socketSend, address);
                MyThread.start();
            }
        
        }
    }
    
}
