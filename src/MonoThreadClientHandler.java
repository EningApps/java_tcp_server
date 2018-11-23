import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class MonoThreadClientHandler extends KissEventListener implements Runnable {

    private static Socket clientDialog;
    private String clientId;
    private FaceValuesListener faceValuesListener;

    public MonoThreadClientHandler(Socket client, FaceValuesListener faceValuesListener, String clientId) {
        MonoThreadClientHandler.clientDialog = client;
        this.faceValuesListener = faceValuesListener;
        this.clientId = clientId;
    }

    @Override
    public void run() {

        try {
            DataOutputStream out = new DataOutputStream(clientDialog.getOutputStream());
            DataInputStream in = new DataInputStream(clientDialog.getInputStream());

            while (!clientDialog.isClosed()) {
                String entry = in.readUTF();
                out.writeUTF("Server reply - " + entry + " - OK");
                faceValuesListener.onNewValues(2.0f, 3.0f, clientId);

                out.flush();
            }

            System.out.println("Client disconnected");
            System.out.println("Closing connections & channels.");
            in.close();
            out.close();
            clientDialog.close();
            System.out.println("Closing connections & channels - DONE.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onKissEvent() {

    }
}