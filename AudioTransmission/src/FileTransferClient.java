//Muhammad Faraz (ERP ID: 25142)

//Language Used: Java. Kindly find the comments explaining each part or section of the Code
//Bonus Part of the Assignment which can Transfer the Audio Broadcast between two devices in real time

//Java Libraries Used: Java Input/Output, Java Socket and Java Sound

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class FileTransferClient {
    public static final String SERVER_IP = "192.168.1.114";
    public static final int SERVER_PORT = 8080;

    public static void main(String[] args) {

        try {
            //Creates a Socket to Connect to the Server
            Socket socket = new Socket(SERVER_IP, SERVER_PORT);
            System.out.println("Connected to the server: " + socket.getInetAddress());

            //Calling the function to Send the Audio
            sendAudio(socket);

            //Closes the Socket
            socket.close();

            //Error Handling for the Input Output Errors.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendAudio(Socket socket) {
        try {
            //Sets up the Audio Input
            AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false);
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();

            //Sets up the Output Stream
            OutputStream out = socket.getOutputStream();

            //Sends the Audio Data
            byte[] buffer = new byte[16384];
            while (true) {
                int count = line.read(buffer, 0, buffer.length);
                out.write(buffer, 0, count);
            }

            //Error Handling for the Input Output Errors.
        } catch (IOException e) {
            e.printStackTrace();

        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}
