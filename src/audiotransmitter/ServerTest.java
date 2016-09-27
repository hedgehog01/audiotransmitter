/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package audiotransmitter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class ServerTest implements Runnable
{

    AudioInputStream audioInputStream;
    static AudioInputStream ais;
    static AudioFormat format;
    static boolean running = true;
    static int port = 50005;
    static int sampleRate = 44100;

    static DataLine.Info dataLineInfo;
    static SourceDataLine sourceDataLine;

    @Override
    public void run()
    {
        try
        {
            System.out.println("Server started at port:" + port);

            DatagramSocket serverSocket = new DatagramSocket(port);

            byte[] receiveData = new byte[4096];

            //set timeout for how long no data received
            serverSocket.setSoTimeout(10000);
            while (running == true)
            {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                byte[] packetSize = receivePacket.getData();

                ByteArrayInputStream baiss = new ByteArrayInputStream(packetSize);
                try
                {
                    serverSocket.receive(receivePacket);
                } catch (SocketTimeoutException  e)
                {
                    System.out.println("Server timedout due to no data received");
                    
                }

                int packetLength = receivePacket.getLength();
 
                getAudioResources();
                ais = new AudioInputStream(baiss, format, packetLength);

                toSpeaker(packetSize);
               
            }
            
        } catch (SocketException ex)
        {
            Logger.getLogger(ServerTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex)
        {
            Logger.getLogger(ServerTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (LineUnavailableException ex)
        {
            Logger.getLogger(ServerTest.class.getName()).log(Level.SEVERE, null, ex);
        } 

    }

    private static void getAudioResources() throws LineUnavailableException
    {
        //format = new AudioFormat(sampleRate, 16, 1, true, false);
        format = AudioSettings.getAudioFormat();
        dataLineInfo = new DataLine.Info(SourceDataLine.class, format);
        sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
        sourceDataLine.open(format);
        sourceDataLine.start();
    }

    public static void toSpeaker(byte soundbytes[])
    {
        try
        {
            
            System.out.println("At the speaker");
            sourceDataLine.write(soundbytes, 0, soundbytes.length);

        } catch (Exception e)
        {
            System.out.println("Not working in speakers...");
            e.printStackTrace();
        }
    }
    
        public void terminate() {
        running = false;
    }
    
    public void initialize(){
        running = true;
    }

}
