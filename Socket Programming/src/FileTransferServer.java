//Java Libraries Used: Java Input/Output, Java Socket and Java Server Socket

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class FileTransferServer {

    //Defines a constant PORT that specifies the port number the server will listen on
    public static final int PORT = 8080;

    public static void main(String[] args) {
        try {
            //Creates a server socket
            ServerSocket serverSocket = new ServerSocket(PORT);


            while (true) {
                System.out.println("Waiting for a client to connect...");
                //Accepts a Client Connection
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                //Processes the File Transfer
                processFileTransfer(clientSocket);

                //Closes the Client Socket
                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void processFileTransfer(Socket clientSocket) {
        try {
            //Create Input and Output Streams
            DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());

            //Receive File Name and Size
            String fileName = dis.readUTF();
            long fileSize = dis.readLong();
            System.out.println("Receiving file: " + fileName + " (" + fileSize + " bytes)");

            //Create a File Output Stream to save the Received File
            FileOutputStream fos = new FileOutputStream(fileName);

            //Receive File Data
            //The Buffer Size can be adjusted to recieve larger files.
            byte[] buffer = new byte[1048576];
            int bytesRead;
            long remaining = fileSize;
            while ((bytesRead = dis.read(buffer, 0, (int) Math.min(buffer.length, remaining))) > 0) {
                fos.write(buffer, 0, bytesRead);
                remaining -= bytesRead;
            }

            // Close the file output stream
            fos.close();

            System.out.println("File transfer completed: " + fileName);

            //Handles the Input Output Errors/Exceptions
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
