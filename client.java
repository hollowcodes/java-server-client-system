import com.sun.org.apache.bcel.internal.generic.GotoInstruction;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class client {

    public void Client() throws Exception{

        String host = "127.0.0.1";
        int port = 6666;

        System.out.println("[*] connecting to host: " + host + "\n");

        Scanner scan = new Scanner(System.in);
        System.out.print("Enter ID: ");
        String id = scan.nextLine();

        outter : while(true){
            Socket s = new Socket(host, port);

            DataOutputStream dout = new DataOutputStream(s.getOutputStream());

            DataInputStream dis = new DataInputStream(s.getInputStream());


            inner : while(true) {
                System.out.print(">> ");
                String msg = scan.nextLine();

                if (!msg.equals("")) {
                    dout.writeUTF(id + " >> " + msg);
                    dout.flush();

                    if (msg.equals("q")) {
                        s.close();
                        System.out.println("\n[-] disconnected from server");
                        break outter;
                    }
                    break inner;
                }
            }

            String echo = dis.readUTF();

            System.out.println(echo);

            dout.close();
            dis.close();

        }
    }
}
