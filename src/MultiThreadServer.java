import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadServer {

    private static ExecutorService executeIt = Executors.newFixedThreadPool(2);

    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(3345);

             BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("Server socket created");

            int i = 0;

            while (!server.isClosed()) {

                Socket clientSocket = server.accept();
                String clientId = "Client socket " + i++;
                ClientHandlerRunnable clientHandler = new ClientHandlerRunnable(clientSocket, clientId);
                executeIt.execute(clientHandler);

                System.out.print("Connection accepted: " + clientId);
            }
            executeIt.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}