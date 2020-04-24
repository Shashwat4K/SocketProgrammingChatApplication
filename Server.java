import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private static final int PORT = 1234;
    private static ArrayList<ClientHandler> clients = new ArrayList<>();
    private static ExecutorService pool = Executors.newFixedThreadPool(12);
    public static void main(String[] args) throws IOException {
        ServerSocket listener = new ServerSocket(PORT);
        BufferedReader in = null;
        try{
            while(true) {
                System.out.println("{SERVER} Waiting for clients to connect...");
                Socket client = listener.accept(); // Accepts Client connection and makes a Socket for it
                System.out.println("{SERVER} Connected to a client.");
                in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                String firstMessage = in.readLine();
                if(firstMessage.contains("!@#")) {
                    String[] words = firstMessage.split(",");
                    String uname = words[0];
                    int id = Integer.parseInt(words[1]);
                    ClientHandler clientThread = new ClientHandler(client, clients, uname, id);
                    clients.add(clientThread);
    
                    pool.execute(clientThread);
                }
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        } finally {
            // Do 
            if(in != null) {
                in.close();
            }
            listener.close();
        }
    }
}