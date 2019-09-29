package client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client {

    public static void main(String[] args) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        try {
            Socket socket = new Socket("localhost", 3345);
            System.out.println("Client connected to socket");

            try (DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                 DataInputStream inputStream = new DataInputStream(socket.getInputStream())) {

                String message = inputStream.readUTF();
                System.out.println(message);

                int i = 0;
                while (i < 10) {

                    String str = reader.readLine();

                    outputStream.writeUTF(str);
                    outputStream.flush();

                    String in = inputStream.readUTF();
                    System.out.println(in);
                    i++;
                }

                String resultMessage = inputStream.readUTF();
                System.out.println(resultMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}