//Muhammad Faraz (ERP ID: 25142)

//Language Used: Java. Kindly find the comments explaining each part or section of the Code.
//I have Transferred CiscoPacketTracer_821_Windows_64bit.exe as a Testing File which was 227MB in size.

//Please give the Path of the File to be transferred in the Command Line while Testing.

//Example Output:
//Connected to the server: /192.168.1.114
//File transfer completed: CiscoPacketTracer_821_Windows_64bit.exe

//Java Libraries Used: Java Input/Output and Java Socket

import java.io.*;
import java.net.Socket;


public class FileTransferClient {

    //Constants which define the IP and PORT number of the Server to which the client will connect to
    public static final String SERVER_IP = "192.168.1.114";
    public static final int SERVER_PORT = 8080;

    public static void main(String[] args) {

        //The Path of File to be transferred is given as Command Line Input. This part of the code checks whether or not the Command Line Path is Provided or not.
        //If Command Line Input is empty, the program exits.
        if (args.length != 1) {
            System.err.println("Usage: java FileTransferClient CiscoPacketTracer_821_Windows_64bit.exe");
            return;
        }

        //If the program was called with the correct number of arguments, it reads the file path from the argument and creates a File object representing the file.
        String filePath = args[0];
        File file = new File(filePath);

        //If the file does not exist, it displays an error message and exits the program.
        if (!file.exists()) {
            System.err.println("File not found: " + "CiscoPacketTracer_821_Windows_64bit.exe");
            return;
        }

        try {
            //Creates a Socket Object to connect to the server using the Constants of IP and PORT number as the argument.
            Socket socket = new Socket(SERVER_IP, SERVER_PORT);
            System.out.println("Connected to the server: " + socket.getInetAddress());

            //Sends the file
            sendFile(socket, file);


            //Closes the socket
            socket.close();

            //Error Handling for the Input Output Errors.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendFile(Socket socket, File file) {
        try {
            //Create input and output streams
            DataInputStream dis = new DataInputStream(new FileInputStream(file));
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            //Send file name and size
            dos.writeUTF(file.getName());
            dos.writeLong(file.length());

            //Send file data
            //We can adjust the Buffer Size to send larger files
            byte[] buffer = new byte[1048576];
            int bytesRead;
            while ((bytesRead = dis.read(buffer)) > 0) {
                dos.write(buffer, 0, bytesRead);
            }

            // Closes the input stream
            dis.close();

            System.out.println("File transfer completed: " + file.getName());

            //Handles the Input Output Errors/Exceptions
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

