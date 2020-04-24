import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.Runnable;
import java.net.Socket;
import java.util.ArrayList;
import java.sql.Timestamp;

public class ClientHandler implements Runnable
{
    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private String Username;
    private int ID;
    private ArrayList<ClientHandler> clients;
    public ClientHandler(Socket s, ArrayList<ClientHandler> clients, String uname, int id) throws IOException{
        // Initialize the attributes
        this.client = s;
        this.clients = clients;
        this.Username = uname;
        this.ID = id;
        in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        out = new PrintWriter(s.getOutputStream(), true);
    }

    public void setUsername(String username) {
        Username = username;
    }

    public void setID(int iD) {
        ID = iD;
    }
    @Override
    public void run()
    {
        try{
            while(true)
            {
                String request = in.readLine();
                broadcastMessage(request);
            }
        } catch (IOException e) {
            System.err.println("IOException in Client Handler!");
            e.printStackTrace();
        } finally {
            out.close();
            try{
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    private void broadcastMessage(String msg) {
        // Broadcasts a message to all client threads
        for (ClientHandler c : clients) {
            if(c.Username.equals(this.Username) != true) {
                c.out.println("<" + Username + " (" + new Timestamp(System.currentTimeMillis()) + ")> " + msg); // We can access 'private' PrintWriter out, because the class definition is not over yet, and we are accessing the private member inside the same class, which is allowed. Otherwise we would have used getters and setters. Old school!
            }
        }
    }
}