import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.Runnable;
import java.net.Socket;
import java.util.ArrayList;
import java.sql.Timestamp;
import java.util.logging.Logger;


public class ClientHandler implements Runnable
{
    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private String Username;
    private int ID;
    private ArrayList<ClientHandler> clients;
    private Logger logger;
    private boolean done;

    public ClientHandler(Socket s, ArrayList<ClientHandler> clients, String uname, int id, Logger logger) throws IOException{
        // Initialize the attributes
        this.client = s;
        this.clients = clients;
        this.Username = uname;
        this.ID = id;
        this.logger = logger;
        in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        out = new PrintWriter(s.getOutputStream(), true);
        done = false;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public void setID(int iD) {
        ID = iD;
    }

    private void iAmDone() {
        this.done = true;
    }
    @Override
    public void run()
    {
        try{
            while(!this.done)
            {
                String request = in.readLine();
                if(request.equals("exit")) {
                    out.println("Bye!!");
                    iAmDone(); 
                } else {
                    logger.info(this.Username+",Message:'"+ request +"',Timestamp:"+new Timestamp(System.currentTimeMillis()));
                    if(request.startsWith("$USERS")) {
                        getUsers();
                    } else {
                        broadcastMessage(request);
                    }
                }    
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

    private void getUsers() {
        String output = "Users currently connected to the group are: \n";
        for(ClientHandler c: clients) {
            output = output + c.Username + "\n";
        }
        out.println(output);
    }
}