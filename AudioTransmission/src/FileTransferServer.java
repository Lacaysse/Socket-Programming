//Java Libraries Used: Java Input/Output, Java Socket and Java Sound

import javax.sound.sampled.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class FileTransferServer {
    public static final int PORT = 8080;
    public static final String SERVER_IP = "192.168.1.114"; //You can Replace with the IP address of the server

    public static void main(String[] args) {
        try {
            //Creates a Server Socket
            ServerSocket serverSocket = new ServerSocket(PORT);

            while (true) {
                System.out.println("Waiting for a client to connect...");
                //Accepts a Client Connection
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                //Process the Audio Transfer
                processAudioTransfer(clientSocket);

                //Closes the client socket
                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void processAudioTransfer(Socket clientSocket) {
        try {
            //Creates Input and Output Streams
            DataInputStream dis = new DataInputStream(clientSocket.getInputStream());

            //Receives Audio Data
            byte[] buffer = new byte[4096];
            int bytesRead;
            SourceDataLine line = null;
            AudioFormat format = getAudioFormat();
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
            if (!AudioSystem.isLineSupported(info)) {
                System.err.println("Line not supported");
                return;
            }
            line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();
            while ((bytesRead = dis.read(buffer)) > 0) {
                // Write audio data to output stream
                line.write(buffer, 0, bytesRead);
            }
            line.drain();
            line.close();

            System.out.println("Audio transfer completed");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private static AudioFormat getAudioFormat() {
        float sampleRate = 44100;
        int sampleSizeInBits = 16;
        int channels = 2;
        boolean signed = true;
        boolean bigEndian = false;
        return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
    }
}
