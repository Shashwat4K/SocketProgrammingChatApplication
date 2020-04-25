import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.ServerSocket;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;
import java.util.logging.FileHandler;


public class Server {

    private static final int PORT = 1234;
    private static ArrayList<ClientHandler> clients = new ArrayList<>();
    private static ExecutorService pool = Executors.newFixedThreadPool(12);
    private static final String LOG_DIR = ".\\messaging_history";
    // private static Logger logger = Logger.getLogger(Server.class.getName());
    public static void main(String[] args) throws IOException {
        ServerSocket listener = new ServerSocket(PORT);
        BufferedReader in = null;
        FileHandler fileHandler = null;
        Logger logger = null;
        try{
            // Make messaging history directory here (if it does not exists)
            File dir = new File(LOG_DIR);
            boolean exists = dir.exists();
            if (!exists) {
                boolean b = dir.mkdir();
                System.out.println("Directory created. status:" + b);
            } else {
                System.out.println("Directory already exists");
            }
            logger = Logger.getLogger(Server.class.getName());
            String serverLogFilePath = LOG_DIR + "\\history_" + System.currentTimeMillis() + ".log";
            fileHandler = new FileHandler(serverLogFilePath, true);
            logger.addHandler(fileHandler);
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
                    ClientHandler clientThread = new ClientHandler(client, clients, uname, id, logger);
                    clients.add(clientThread);
    
                    pool.execute(clientThread);
                }
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        } finally {
            // Do 
            System.out.println("Inside 'finally' block. Closing open resources!");
            if(fileHandler != null) {
                fileHandler.close();
            }
            if(in != null) {
                in.close();
            }
            listener.close();
            pool.shutdown();
        }
    }
}