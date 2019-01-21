import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class HttpServer implements Runnable {

    private ServerSocket serverSocket;
    SensorData sensorData;
    private boolean running;

    public HttpServer(SensorData sensorData) {
        try {
            this.serverSocket = new ServerSocket(8080);
            this.sensorData = sensorData;
            this.running = true;
            System.out.println("HTML-Server IP = " + serverSocket.getLocalSocketAddress());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (running) {
            try {
                startWebserver();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void startWebserver() throws Exception {
        //accept is a blocking method and blocks until a client connects to the server.
        //As soon as a client connect it returns the Socket object which can be used to read client request and send response to client.
        Socket connectionSocket = serverSocket.accept();

        BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));

        // read the data sent. We basically ignore it,
        // stop reading once a blank line is hit. This
        // blank line signals the end of the client HTTP
        // headers.
        String str = "";
        str = inFromClient.readLine();

        //DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
        //System.out.println(str);

        PrintWriter out = new PrintWriter(connectionSocket.getOutputStream());
        switch (str) {
            case "GET / HTTP/1.1":
                htmlPage(connectionSocket, out, indexPage());
                break;
            case "GET /Bath HTTP/1.1":
                htmlPage(connectionSocket, out, sensorPage("BathSensor"));
                break;
            case "GET /Humidity HTTP/1.1":
                htmlPage(connectionSocket, out, sensorPage("HumiditySensor"));
                break;
            case "GET /Temp HTTP/1.1":
                htmlPage(connectionSocket, out, sensorPage("TempSensor"));
                break;
            case "GET /Window HTTP/1.1":
                htmlPage(connectionSocket, out, sensorPage("WindowSensor"));
                break;
            case "GET /Weather HTTP/1.1":
                htmlPage(connectionSocket, out, weatherPage());
                break;
            default:
                htmlPage(connectionSocket, out, pageNotFound());
                break;
        }
    }

    public void htmlPage(Socket connectionSocket, PrintWriter out, String page) throws Exception {

        // Send the response
        // Send the headers
        out.println("HTTP/1.0 200 OK");
        out.println("Content-Type: text/html");
        out.println("Content-Length: ");
        out.println("Server: Bot");
        // this blank line signals the end of the headers
        out.println("");

        // Send the HTML page
        out.println(page);
        out.flush();
        connectionSocket.close();
    }

    public String indexPage() {

        String htmlCode = ""
                + "<h3>Willkommen zur Smart Home Zentrale</h3>"
                + "<p><em>von M. S. Sinan und R. Akbari</em></p>"
                + "<p>Sensortypen:</p>"
                + "<p>"
                + "<a href=\"Bath\">Bath-Sensor</a><br />"
                + "<a href=\"Humidity\">Humidity-Sensor</a><br />"
                + "<a href=\"Temp\">Temperature-Sensor</a><br />"
                + "<a href=\"Window\">Window-Sensor</a><br />"
                + "<a href=\"Weather\">Weather-Data</a><br />"
                + "</p>";
        return htmlCode;
    }

    public String sensorPage(String sensorType) {
        String tableContent = "";
        Vector<JSONObject> dataList = sensorData.getFilteredList(sensorType);

        tableContent += ""
                + "<tr>"
                + "<td style=\"width: 100px;\">&nbsp;" + "Address" + "</td>"
                + "<td style=\"width: 200px;\">&nbsp;" + "Port" + "</td>"
                + "<td style=\"width: 300px;\">&nbsp;" + "Time" + "</td>"
                + "<td style=\"width: 450px;\">&nbsp;" + "Message" + "</td>"
                + "</tr>";
        for (JSONObject k : dataList) {
            try {
                tableContent += ""
                        + "<tr>"
                        + "<td style=\"width: 100px;\">&nbsp;" + k.get("Address") + "</td>"
                        + "<td style=\"width: 200px;\">&nbsp;" + k.get("Port") + "</td>"
                        + "<td style=\"width: 300px;\">&nbsp;" + k.get("Time") + "</td>"
                        + "<td style=\"width: 450px;\">&nbsp;" + k.get("Message") + "</td>"
                        + "</tr>";
            } catch (JSONException jex) {
                jex.printStackTrace();
            }
        }

        String htmlCode = ""
                + "<h3>" + sensorType + "-Sensors</h3>"
                + "<table style=\"height: 124px; width: 500px;\" border=\"1\">"
                + "<tbody>"
                + tableContent
                + "</tbody>"
                + "</table>";

        return htmlCode;
    }

    public String weatherPage(){
        String tableContent = "";

        Vector<JSONObject> weatherList = sensorData.getWeatherList();

        tableContent += ""
                + "<tr>"
                + "<td style=\"width: 100px;\">&nbsp;" + "Ort" + "</td>"
                + "<td style=\"width: 200px;\">&nbsp;" + "Koordinaten" + "</td>"
                + "<td style=\"width: 300px;\">&nbsp;" + "Message" + "</td>"
                + "</tr>";
        for (JSONObject k : weatherList) {
            try {
                tableContent += ""
                        + "<tr>"
                        + "<td style=\"width: 100px;\">&nbsp;" + k.get("name") + "</td>"
                        + "<td style=\"width: 200px;\">&nbsp;" + k.get("coord") + "</td>"
                        + "<td style=\"width: 300px;\">&nbsp;" + k.get("main") + "</td>"
                        + "</tr>";
            } catch (JSONException jex) {
                jex.printStackTrace();
            }
        }

        String htmlCode = ""
                + "<h3>Weather-Data</h3>"
                + "<table style=\"height: 124px; width: 500px;\" border=\"1\">"
                + "<tbody>"
                + tableContent
                + "</tbody>"
                + "</table>";

        return htmlCode;
    }

    public String pageNotFound() {
        String htmlCode = "<h1>404 Page not found</h1>\n";
        return htmlCode;
    }

}
