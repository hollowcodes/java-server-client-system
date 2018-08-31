import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class server {
    public void Server() throws Exception{
        ExecutorService executor = Executors.newFixedThreadPool(30);

        System.out.println("[*] server started\n");

        int port = 6666;
        ServerSocket server_s = new ServerSocket(port);

        while(true){
            Socket client = server_s.accept();

            executor.execute(new Handler(client));

        }
    }
}
