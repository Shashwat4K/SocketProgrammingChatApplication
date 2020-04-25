import java.lang.Runnable;
import java.net.Socket;
import java.net.SocketException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.IOException;
import javax.sound.sampled.*;

public class ServerResponder implements Runnable {
    private Socket server;
    private BufferedReader in;
    // private PrintWriter out;

    public static float SAMPLE_RATE = 8000f;

    public static void tone(int hz, int msecs) throws LineUnavailableException {
        tone(hz, msecs, 1.0);
    }

    public static void tone(int hz, int msecs, double vol) throws LineUnavailableException {
        byte[] buf = new byte[1];
        AudioFormat af = new AudioFormat(SAMPLE_RATE, // sampleRate
                8, // sampleSizeInBits
                1, // channels
                true, // signed
                false); // bigEndian
        SourceDataLine sdl = AudioSystem.getSourceDataLine(af);
        sdl.open(af);
        sdl.start();
        for (int i = 0; i < msecs * 8; i++) {
            double angle = i / (SAMPLE_RATE / hz) * 2.0 * Math.PI;
            buf[0] = (byte) (Math.sin(angle) * 127.0 * vol);
            sdl.write(buf, 0, 1);
        }
        sdl.drain();
        sdl.stop();
        sdl.close();
    }
    
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
                    if(!serverResponse.contains("java.net.SocketException")) {
                        ServerResponder.tone(5000, 100);
                        System.out.println(serverResponse);
                    }
                    System.out.print("> ");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch(LineUnavailableException e) {
            System.out.println("OOPS!");
        } 
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