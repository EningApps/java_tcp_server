import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class MonoThreadClientHandler extends KissEventListener implements Runnable {

    private static Socket clientDialog;
    private FaceValuesListener faceValuesListener;

    private DataOutputStream outputStream;
    private DataInputStream inputStream;

    public MonoThreadClientHandler(Socket client, FaceValuesListener faceValuesListener, String clientId) {
        MonoThreadClientHandler.clientDialog = client;
        this.faceValuesListener = faceValuesListener;
        super.clientId = clientId;
    }

    @Override
    public void run() {

        try {
            outputStream = new DataOutputStream(clientDialog.getOutputStream());
            inputStream = new DataInputStream(clientDialog.getInputStream());

            while (!clientDialog.isClosed()) {
                //12f
                String entry = inputStream.readUTF();
                float value = Float.valueOf(entry);
                if (value == 1000f) {
                    faceValuesListener.onKissValue(clientId);
                } else {
                    faceValuesListener.onNewValues(value, clientId);
                }

                outputStream.flush();
            }

            System.out.println("Client disconnected");
            System.out.println("Closing connections & channels.");
            inputStream.close();
            outputStream.close();
            clientDialog.close();
            System.out.println("Closing connections & channels - DONE.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSmileEvent() {
        try {
            outputStream.writeUTF("SMILE");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onKissEvent() {
        try {
            outputStream.writeUTF("KISS");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}