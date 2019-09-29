package ticket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Ticket {

    public String ticketId;
    public List<Integer> valuesList = new ArrayList<>();

    public int userIntersectionCount;

    public Ticket(String ticketId, List<Integer> valuesList) {
        this.ticketId = ticketId;
        this.valuesList = valuesList;
    }

    public static List<Ticket> loadTickets() throws IOException {
        List<Ticket> tickets = new ArrayList<>();

        BufferedReader reader = new BufferedReader(new InputStreamReader(Ticket.class.getResourceAsStream("tickets")));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parsedLine = line.split(" ");
            List<Integer> values = new ArrayList<>();
            for (int i = 1; i < parsedLine.length; i++) {
                values.add(Integer.parseInt(parsedLine[i]));
            }
            Ticket ticket = new Ticket(parsedLine[0], values);
            tickets.add(ticket);
        }
        return tickets;
    }

}
