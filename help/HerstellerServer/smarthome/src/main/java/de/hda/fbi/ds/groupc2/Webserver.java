package de.hda.fbi.ds.groupc2;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class Webserver extends Thread{
    private Socket clientSocket;
    private static ServerSocket serverSocket;
    private Publisher publish;
    private static SmartHome smartHome;
    private PrintWriter out;
    private BufferedReader in;
    private String fileName = "temp.txt";
    private Subscriber sub;
    public Webserver(Socket socket) {
        this.clientSocket = socket;
        this.publish = new Publisher(smartHome);
    }

    public void run() {
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String inputLine;
            Subscriber subscriber1 = new Subscriber("Hersteller 1");

            while ((inputLine = in.readLine()) != null) {

                if ("GET / HTTP/1.1".equals(inputLine)) {
                    out.println("HTTP/1.1 200 OK\r\n"
                            + "Content-Type: text/html\r\n"
                            + "Content-Length: ");
                    out.println("\r\n\r\n");
                    out.println(    "<html>" +
                            "<head>" +
                            "<meta charset=\"utf-8\">" +
                            "<meta http-equiv=\"refresh\" content=\"1\" >" +
                            "<title>Verteilte Systeme</title>" +
                            "</head>" +
                            "<body>" +
                            "<ul>" +
                                    "<li>" + smartHome.getTemp() + "</li>" +
                                    "<li>" + smartHome.getFan() + "</li>" +
                                    "<li>" + smartHome.getHumidity() + "</li>" +
                                    "<li>" + smartHome.getWindow() + "</li>" +
                            "</ul>"+
                            "</body>" +
                            "</html>");
                    System.out.println("\n\n -------------------------- GET / REQUEST ERHALTEN -------------------------- \n\n");
                    System.out.println(inputLine);

                    //Before end
                    publish.send();
                    break;
                } else {
                    System.out.println(inputLine);
                }
            }
            in.close();
            out.close();
            clientSocket.close();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        smartHome = new SmartHome();
        smartHome.start();

        serverSocket = new ServerSocket(8077);
        while (true) {
            Webserver w = new Webserver(serverSocket.accept());
            w.start();
        }

    }
}