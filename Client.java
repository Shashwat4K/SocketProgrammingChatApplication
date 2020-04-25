import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.lang.Thread;
import java.util.Scanner;
import java.sql.Timestamp;

public class Client {
    private static String SERVER_IP;  // Localhost
    private static int SERVER_PORT; // Server port number
    private static int threadCounter = 0;
    private static int clientID;
    private static String clientUsername;
    public static void main(String[] args) throws IOException {
        Scanner sc = null;
        BufferedReader client_keyboard = null;
        PrintWriter client_writer = null;
        try{
            // Collecting User data
            sc = new Scanner(System.in);
            System.out.print("Enter IP address: ");
            SERVER_IP = sc.next();
            System.out.print("Enter port number (1234 default): ");
            SERVER_PORT = sc.nextInt();
            System.out.print("Enter your Username: ");
            String uname = sc.next();
            setClientUsername(uname);
            
            Socket socket = new Socket(SERVER_IP, SERVER_PORT);
            setClientID();

            ServerResponder server_responder = new ServerResponder(socket);

            client_keyboard = new BufferedReader(new InputStreamReader(System.in)); // Take inputs from keyboard
            client_writer = new PrintWriter(socket.getOutputStream(), true);

            client_writer.println(clientUsername+","+clientID+",!@#");

            System.out.println("\nWelcome to the Group " + clientUsername + "!");
            System.out.println("<Connected to the group on " + new Timestamp(System.currentTimeMillis()) + ">\n\n");

            new Thread(server_responder).start();

            // Main processing block. Do all your client side stuff here!
            while(true) {
                System.out.print("> ");
                String message = client_keyboard.readLine();
                if(message.equals("exit")) {
                    client_writer.println("exit");
                    break;
                }
                client_writer.println(message);
            }

        }catch(IOException e) {
            // System.err.println(e.getStackTrace());
        } finally {
            if(sc != null) {
                sc.close();
            }
            if(client_writer != null) {
                client_writer.close();
            }
            if(client_keyboard != null) {
                client_keyboard.close();
            }
            // socket.close();
        }      
    }

    private static void setClientID(){
        clientID = threadCounter++;
    }

    private static void setClientUsername(String uname) {
        clientUsername = uname;
    }
}