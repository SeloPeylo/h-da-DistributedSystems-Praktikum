import java.io.*;
import java.net.*;
import java.util.*;

import Converter.MessageRoot;
import org.json.simple.JSONObject;


/**
 * WebServer Class.
 */
class WebServer extends Thread{
    private ServerSocket serverSocket = new ServerSocket(8080);

    private JsonConverter converter;
    private List<Object> luftfeuchtigkeitList;
    private List<Object> temperatursList;
    private List<Object> drehzahlList;
    private List<Object> fensterList;

    private boolean running = true;
    private MqttSubscriber subscriber;

    public WebServer() throws IOException {
        converter = new JsonConverter();
        luftfeuchtigkeitList = new ArrayList<>();
        temperatursList = new ArrayList<>();
        drehzahlList= new ArrayList<>();
        fensterList= new ArrayList<>();
        subscriber = new MqttSubscriber("tcp"
                ,"localhost","1883","weather_data");
        subscriber.run();
    }

    @Override
    public void run() {
        while (running){
            try {
                startWebserver();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void startWebserver() throws Exception {

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
        switch (str){
            case "GET / HTTP/1.1":
                htmlPage(connectionSocket, out, indexPage());
                break;
            case "GET /Luftfeuchtigkeit HTTP/1.1":
                htmlPage(connectionSocket, out, feuchtigkeitPage());
                break;
            case "GET /Fenster HTTP/1.1":
                htmlPage(connectionSocket, out, fensterPage());
                break;
            case "GET /LuefterDrehzahl HTTP/1.1":
                htmlPage(connectionSocket, out, drehzahlPage());
                break;
            case "GET /Temparatur HTTP/1.1":
                htmlPage(connectionSocket, out, temparaturPage());
                break;
            case "GET /Weather HTTP/1.1":
                htmlPage(connectionSocket, out, weatherPage());
                break;
            default:
                htmlPage(connectionSocket, out, pageNotFound("404 Page"));
                break;
        }

    }

    /**
     *
     * @param connectionSocket
     * @param out
     * @param page
     * @throws Exception
     */
    public void htmlPage(Socket connectionSocket, PrintWriter out, String page) throws Exception{

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

    /**
     * Website homepage with four links
     * Luftfeuchtigkeit, Fenster
     * LuefterDrehzahl, Temparatur
     * @return
     */
    public String indexPage(){

        String htmlCode =
                htmlHeader("Index")
                        + "<div>\n"
                        + "<a href=\"Luftfeuchtigkeit\">Luftfeuchtigkeit</a>\n"
                        + "<a href=\"Fenster\">Fenster</a>\n"
                        + "<a href=\"LuefterDrehzahl\">LuefterDrehzahl</a>\n"
                        + "<a href=\"Temparatur\">Temparatur</a>\n"
                        + "<a href=\"Weather\">Weather</a>\n"
                        + "</div>\n"
                        + htmlFooter();
        return htmlCode;
    }

    public String feuchtigkeitPage(){
        this.setLuftfeuchtigkeitList(converter.getLuftfeuchtigkeitList());

        String htmlCode =
                htmlHeader("Luftfeuchtigkeit")
                + "<div>\n";
        for (Object tmp: luftfeuchtigkeitList) {
            JSONObject sensorObject = (JSONObject) tmp;
            htmlCode +=
                    "<ul>\n"
                    + sensorObject.get("Ip") + "\n"
                    + sensorObject.get("Port") + "\n"
                    + sensorObject.get("Data") + "\n"
                    + "</ul>\n";
        }
        htmlCode +=
                    "</div>\n"
                    + htmlFooter();
        return htmlCode;
    }

    public String fensterPage(){
        this.setFensterList(converter.getFensterList());
        String htmlCode =
                htmlHeader("Fenster")
                        + "<div>\n";
        for (Object tmp: fensterList) {
            JSONObject sensorObject = (JSONObject) tmp;
            htmlCode +=
                    "<ul>\n"
                    + sensorObject.get("Ip") + "\n"
                    + sensorObject.get("Port") + "\n"
                    + sensorObject.get("Data") + "\n"
                    + "</ul>\n";
        }
        htmlCode +=
                "</div>\n"
                        + htmlFooter();
        return htmlCode;
    }

    public String drehzahlPage(){
        this.setDrehzahlList(converter.getDrehzahlList());
        String htmlCode =
                htmlHeader("LuefterDrehzahl")
                        + "<div>\n"
                        + "<ol>\n";
        for (Object tmp: drehzahlList) {
            JSONObject sensorObject = (JSONObject) tmp;
            htmlCode +=
                    "<ul>\n"
                    + sensorObject.get("Ip") + "\n"
                    + sensorObject.get("Port") + "\n"
                    + sensorObject.get("Data") + "\n"
                    + "</ul>\n";
        }
        htmlCode +=
                "</div>\n"
                        + htmlFooter();
        return htmlCode;
    }

    public String temparaturPage(){
        this.setTemperatursList(converter.getTemperatursList());
        String htmlCode =
                htmlHeader("Temparatur")
                        + "<div>\n"
                        + "<ol>\n";
        for (Object tmp: temperatursList) {
            JSONObject sensorObject = (JSONObject) tmp;
            htmlCode +=
                    "<ul>\n"
                    + sensorObject.get("Ip") + "\n"
                    + sensorObject.get("Port") + "\n"
                    + sensorObject.get("Data") + "\n"
                    + "</ul>\n";
        }
        htmlCode +=
                "</div>\n"
                        + htmlFooter();
        return htmlCode;
    }

    public String weatherPage(){

        MessageRoot root = subscriber.getData();

        String htmlCode;

        if (root != null) {
            htmlCode =
                    htmlHeader("Weather")
                            + "<div>\n";
            htmlCode +=
                            "<ul>\nCoord: \n"
                                + "<li>lon: " + root.getCoord().getLon() + "</li>\n"
                                + "<li>lat: " + root.getCoord().getLat() + "</li>\n"
                            + "</ul>\n"
                            + "<ul>\nWeather-1: " + "\n"
                                + "<li>id: " + root.getWeather()[0].getID() + "</li>\n"
                                + "<li>main: " + root.getWeather()[0].getMain() + "</li>\n"
                                + "<li>description: " + root.getWeather()[0].getDescription() + "</li>\n"
                                + "<li>icon: " + root.getWeather()[0].getIcon() + "</li>\n"
                            + "</ul>\n"
                            + "<ul>\n Base: " + root.getBase() + "</ul>\n"
                            + "<ul>\nMain: \n"
                                + "<li>temp: " + root.getMain().getTemp() + "</li>\n"
                                + "<li>pressure: " + root.getMain().getPressure() + "</li>\n"
                                + "<li>humidity: " + root.getMain().getHumidity() + "</li>\n"
                                + "<li>temp_min: " + root.getMain().getTempMin() + "</li>\n"
                                + "<li>temp_max: " + root.getMain().getTempMax() + "</li>\n"
                            + "</ul>\n"
                            + "<ul>\n Visibility: " + root.getVisibility() + "</ul>\n"
                            + "<ul>\n Wind: \n"
                                + "<li>speed: " + root.getWind().getSpeed() + "</li>\n"
                                + "<li>deg: " + root.getWind().getDeg() + "</li>\n"
                            + "</ul>\n"
                            + "<ul>\nClouds: " + "\n"
                                + "<li>all: " + root.getClouds().getAll() + "</li>\n"
                            + "</ul>\n"
                            + "<ul>\n dt: " + root.getDt() + "</ul>\n"
                            + "<ul>\n sys:\n"
                                + "<li>type: " + root.getSys().getType() + "</li>\n"
                                + "<li>id: " + root.getSys().getID() + "</li>\n"
                                + "<li>message: " + root.getSys().getMessage() + "</li>\n"
                                + "<li>country: " + root.getSys().getCountry() + "</li>\n"
                                + "<li>sunrise: " + root.getSys().getSunrise() + "</li>\n"
                                + "<li>sunset: " + root.getSys().getSunset() + "</li>\n"
                            + "</ul>\n"
                            + "<ul>\n"
                                + "<li>id:" + root.getID() + "</li>\n"
                                + "<li>name: " + root.getName() + "</li>\n"
                                + "<li>cod: " + root.getCod() + "</li>\n"
                            + "</ul>\n";
            htmlCode +=
                    "</div>\n"
                            + htmlFooter();
        }else {
            htmlCode = "The page is under construction!!!";
        }
        return htmlCode;
    }

    public String pageNotFound(String title){
        String htmlCode =
                htmlHeader("Page not found")
                + "<div>\n"
                + "<h1>404 Page not found</h1>\n"
                + "</div>\n"
                        + htmlFooter();
        return htmlCode;
    }

    public String htmlHeader(String title){
        String htmlCode =
                "<!DOCTYPE html>\n"
                        + "<html>\n"
                        + "<head>\n"
                        + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                        + "<meta charset=\"utf-8\">\n"
                        + "<title>" + title + "</title>\n"
                        + "<link rel=\"shortcut icon\" href=\"https://https://icons8.com/favicon.png\" type=\"image/png\" />\n"
                        + "<link rel=\"icon\" href=\"https://https://icons8.com/favicon.png\" type=\"image/png\" />\n"
                        + "</head>\n"
                        + "<body>\n";
        return htmlCode;
    }

    public String htmlFooter() {
        String htmlCode =
                "</body>\n"
                + "</html>\n";
        return htmlCode;
    }

    public void setLuftfeuchtigkeitList(List<Object> luftfeuchtigkeitList) {
        this.luftfeuchtigkeitList = luftfeuchtigkeitList;
    }

    public void setTemperatursList(List<Object> temperatursList) {
        this.temperatursList = temperatursList;
    }

    public void setDrehzahlList(List<Object> drehzahlList) {
        this.drehzahlList = drehzahlList;
    }

    public void setFensterList(List<Object> fensterList) {
        this.fensterList = fensterList;
    }
}