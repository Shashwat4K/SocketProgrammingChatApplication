import java.lang.Runnable;
import java.net.Socket;
import java.net.SocketException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.IOException;

public class ServerResponder implements Runnable {
    private Socket server;
    private BufferedReader in;
    // private PrintWriter out;

    public ServerResponder(Socket s) throws IOException {
        server = s;
        in = new BufferedReader(new InputStreamReader(server.getInputStream()));
        // out = new PrintWriter(server.getOutputStream(), true);
    }

    @Override
    public void run() {
        try {
            if(!server.isClosed()) {
                while (true) {
                    String serverResponse = in.readLine();
                    if(serverResponse == null) {
                        break;
                    }
                    System.out.println(serverResponse);
                    System.out.print("> ");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } /*catch(SocketException e) {
            System.out.println("Socket Exception somewhere");
        }*/ 
        finally {
            try{
                in.close();
                if(!server.isClosed()) {
                    server.close();
                } 
            }catch(IOException e) {
                e.printStackTrace();
            }
        }
    }
}