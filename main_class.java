import FileService.fileclient;
import FileService.fileserver;

import javax.swing.*;

public class run {
    
    // run this once for server, then run the client/s
    public static void main(String[] agrs) throws Exception {

        run frst = new run();
        frst.run_frst();

    }

    public void run_frst() throws Exception{

        String[] options = new String[] {"Server", "Client"};
        int response = JOptionPane.showOptionDialog(null, "Start Client or Server?", "Choose",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, options[0]);

        switch (response){
            case(0):
                myServer();
                break;
            case(1):
                myClient();
                break;
            case(-1):
                System.exit(0);
        }
    }

    private static void myServer() throws Exception{
        server s = new server();
        s.Server();
    }

    private static void myClient() throws Exception{
        client c = new client();
        c.Client();
    }
}
