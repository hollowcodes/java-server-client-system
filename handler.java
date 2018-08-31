import com.sun.rmi.rmid.ExecPermission;
import com.sun.security.ntlm.Client;
import com.sun.security.ntlm.Server;
import java.io.*;
import java.lang.reflect.Array;
import java.net.*;
import java.rmi.server.ExportException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;

class Handler implements Runnable{

    private filehandeling fh = new filehandeling();

    private Socket client;

    public Handler(Socket client){
        this.client = client;
    }

    public String listToString(String[] lst){

        String str = "";
        for(int i=0;i<=lst.length-1;i++){
            if(str.toCharArray().length == 0){
                str += lst[i];
            }
            else{
                str += ", " + lst[i];
            }
        }
        return str;
    }

    public void saveClient(String c) throws Exception{

        FileReader fileReader = new FileReader("clients.txt");
        StringBuilder stringBuffer = new StringBuilder();
        int numCharsRead;
        char[] charArray = new char[1024];
        while ((numCharsRead = fileReader.read(charArray)) > 0) {
            stringBuffer.append(charArray, 0, numCharsRead);
        }
        String allclients =  stringBuffer.toString();

        String[] cs = allclients.split(" ");

        if(!Arrays.asList(cs).contains(c)){
            FileWriter fileWriter = new FileWriter(new File("clients.txt"), true);
            fileWriter.write(" " + c);
            fileWriter.flush();
            fileWriter.close();
        }

    }

    public String readClient() throws Exception{
        FileReader fileReader = new FileReader("clients.txt");
        StringBuilder stringBuffer = new StringBuilder();
        int numCharsRead;
        char[] charArray = new char[1024];
        while ((numCharsRead = fileReader.read(charArray)) > 0) {
            stringBuffer.append(charArray, 0, numCharsRead);
        }
        String allclients =  stringBuffer.toString();

        String[] cs = allclients.split(" ");

        return listToString(cs);
    }

    @Override
    public void run() {
        try{
            DataOutputStream dout = new DataOutputStream(client.getOutputStream());
            DataInputStream dis = new DataInputStream(client.getInputStream());

            String msg = (String) dis.readUTF();

            String[] msg_splitted = msg.split(" ");

            // save client
            saveClient(msg_splitted[0]);
            String clients = readClient();

            // socket information
            String[] sock = client.toString().split("Socket");
            String sock_info = sock[1];

            // check quitting
            if(msg_splitted[msg_splitted.length-1].equals("q")) {
                System.out.println(sock_info + ", client : " + msg_splitted[0] + " has disconnected");


                fh.write("onlinecheck.txt", " disconn", true);

                String[] cs = fh.read("clients.txt").split(" ");
                int cs_len = cs.length;

                String[] o = fh.read("onlinecheck.txt").split(" ");
                int o_len = o.length;

                if(cs_len == o_len){
                    System.out.println("\n[-] server stopped");

                    fh.write("clients.txt", "", false);
                    fh.write("onlinecheck.txt", "", false);

                    System.exit(0);
                }
            }

            else if(msg_splitted[2].equals("load")){
                System.out.println(sock_info + ", client information sent to client : " + msg_splitted[0]);
                dout.writeUTF("Connected clients: " + clients);
            }
            else{
                System.out.println(sock_info + ", from " + msg);
                dout.writeUTF("Echo from server: " + sock_info + ", from " + msg);

            dout.flush();
            dout.close();
            dis.close();}

        } catch (Exception e){}
    }
}
