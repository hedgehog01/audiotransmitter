/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package audiotransmitter;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;


public class ClientTest implements Runnable
{
    public byte[] buffer;
    private static int port = 50005;
    static AudioInputStream ais;
    private volatile boolean running = true;
    private InetAddress addr = null;
    

        @Override
    public void run()
    {
        System.out.println("Starting client");
        TargetDataLine line;
        DatagramPacket dgp; 

        AudioFormat.Encoding encoding = AudioFormat.Encoding.PCM_SIGNED;
        float rate = 44100.0f;
        int channels = 2;
        int sampleSize = 16;
        boolean bigEndian = true;


        //AudioFormat format = new AudioFormat(encoding, rate, sampleSize, channels, (sampleSize / 8) * channels, rate, bigEndian);
        AudioFormat format = AudioSettings.getAudioFormat();
        
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        if (!AudioSystem.isLineSupported(info)) {
            System.out.println("Line matching " + info + " not supported.");
            return;
        }

        try
        {
            line = (TargetDataLine) AudioSystem.getLine(info);

            int buffsize = line.getBufferSize()/5;
            buffsize += 512; 

            line.open(format);

            line.start();   

            int numBytesRead;
            byte[] data = new byte[buffsize];

            if (addr == null){
                System.out.println ("Did not get ip address for server, setting to default 127.0.0.1");
                addr = InetAddress.getByName("127.0.0.1");
            }
            
            //addr = InetAddress.getByName("127.0.0.1");
            DatagramSocket socket = new DatagramSocket();
            while (running) {
                   // Read the next chunk of data from the TargetDataLine.
                   numBytesRead =  line.read(data, 0, data.length);
                   // Save this chunk of data.
                   dgp = new DatagramPacket (data,data.length,addr,port);

                   socket.send(dgp);
                }
            System.out.println ("Client: line stopping...");
            line.stop();
            System.out.println ("Client: line closing...");
            line.close();

        }catch (LineUnavailableException e) {
            e.printStackTrace();
        }catch (UnknownHostException e) {
            // TODO: handle exception
        } catch (SocketException e) {
            // TODO: handle exception
        } catch (IOException e2) {
            // TODO: handle exception
        }
    }
    
    public void terminate() {
        running = false;
    }
    
    public void initialize(){
        running = true;
    }

    
   public void setServerAddress(String address){
        try
        {
            addr = InetAddress.getByName(address);
        } catch (UnknownHostException ex)
        {
            Logger.getLogger(ClientTest.class.getName()).log(Level.SEVERE, null, ex);
        }
   }

}
