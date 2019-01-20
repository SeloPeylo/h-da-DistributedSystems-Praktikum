
import java.io.IOException;
import java.util.Scanner;

public class SmartHomeCentral {

    public static void main(String[] args) {

        startApplication();

    }

    public static void startApplication(){
        Runnable udpServer = null;
        Thread udpServerThread = null;
        Thread webServer;
        while (true) {

            try {
                Scanner reader = new Scanner(System.in);  // Reading from System.in
                System.out.println("Enter a number 1-7: ");
                System.out.println("1- To start the UDPserver: ");
                System.out.println("2- To stop the UDPserver: ");
                System.out.println("3- To print list of clients: ");
                System.out.println("4- To start the TCPServer: ");
                System.out.println("5- To start the Mqtt publisher: ");
                System.out.println("6- To stop the application: ");
                System.out.print("Type: ");
                int input = reader.nextInt(); // Scans the next token of the input as an int.

                switch (input) {
                    case 1:
                        if(udpServer == null) {
                            udpServer = new UDPServer(5001);
                            udpServerThread = new Thread(udpServer);
                            udpServerThread.start();
                            System.out.println("\nRUNNING SERVER AT PORT: " + 5001);
                        }else if(Thread.interrupted()){
                            ((UDPServer) udpServer).startServer();
                            udpServerThread = new Thread(udpServer);
                            udpServerThread.start();
                            System.out.println("THE SERVER HAS RUNNING STARTED AGAIN!!!\n");
                        }else{
                            System.out.println("THE SERVER IS ALREADY RUNNING!!!\n");
                        }
                        break;
                    case 2:
                        if (((UDPServer) udpServer).isRunning() && udpServerThread.isAlive()) {
                            ((UDPServer) udpServer).stopServer();
                            Thread.currentThread().interrupt();
                            System.out.println("THE SERVER HAS STOPPED RUNNING!!!\n");
                        } else {
                            System.out.println("THE SERVER IS NOT RUNNING AT THE MOMENT!!!\n");
                        }
                        break;
                    case 3:
                        System.out.println("LIST OF CLIENTS WHO HAS REQUESTED\n");
                        for (SenderInfo each : ((UDPServer) udpServer).getAnfragen()) {
                            System.out.println(each.toString());
                        }
                        break;
                    case 4:
                        webServer = new WebServer();
                        webServer.start();
                        System.out.println("THE WEBSERVER HAS STARTED SUCCESSFULLY AT PORT 8080\n");
                        break;
                    case 5:
                        MqttPublisher publisher = new MqttPublisher("tcp", "localhost",
                                "1883", "sensor_data");
                        publisher.run();
                        System.out.println("MqttPublisher has published.");
                        break;
                    case 6:
                        System.out.println("YOU HAVE SUCCESSFULLY STOPPED THE APPLICATION!!!");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("There is a problem with the server");
                        System.out.println("Please type a valid number!!!");
                }

            }catch (IOException e){
                e.printStackTrace();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
