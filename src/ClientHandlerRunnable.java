import ticket.Ticket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ClientHandlerRunnable implements Runnable {

    private Socket clientConnection;
    private String clientId;

    public ClientHandlerRunnable(Socket clientConnection, String clientId) {
        this.clientConnection = clientConnection;
        this.clientId = clientId;
    }

    @Override
    public void run() {

        try {
            DataOutputStream outputStream = new DataOutputStream(clientConnection.getOutputStream());
            DataInputStream inputStream = new DataInputStream(clientConnection.getInputStream());

            String welcomeMessage = "\nДобро пожаловать на сервер, " + clientId + "\nВведите 10 значений :\n";
            outputStream.writeUTF(welcomeMessage);
            outputStream.flush();

            List<Integer> clientValues = new ArrayList<>();

            while (!clientConnection.isClosed()) {

                String entry = inputStream.readUTF();
                clientValues.add(Integer.parseInt(entry));

                outputStream.writeUTF("Внесено значение " + entry);
                outputStream.flush();

                if(clientValues.size()==10){
                    break;
                }
            }

            Ticket maxInteractionsTicket = getMaxInteractionsTicket(clientValues);
            String resultMessage;
            if (maxInteractionsTicket != null) {
                resultMessage = "\nВаш лучший результат:" + maxInteractionsTicket.ticketId + " со значениями: " + maxInteractionsTicket.valuesList;
            } else {
                resultMessage = "Произошла ошибка";
            }
            outputStream.writeUTF(resultMessage);
            outputStream.flush();

            System.out.println("Client disconnected");
            inputStream.close();
            outputStream.close();
            clientConnection.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Ticket getMaxInteractionsTicket(List<Integer> clientValues) {
        try {
            List<Ticket> tickets = Ticket.loadTickets();
            for (Ticket ticket : tickets) {
                for (Integer value : clientValues) {
                    if (ticket.valuesList.contains(value)) {
                        ticket.userIntersectionCount++;
                    }
                }
            }
            return tickets.stream().max(new Comparator<Ticket>() {
                @Override
                public int compare(Ticket o1, Ticket o2) {
                    return o1.userIntersectionCount - o2.userIntersectionCount;
                }
            }).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}