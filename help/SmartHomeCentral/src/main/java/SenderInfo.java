
import java.net.InetAddress;

/**
 * A class which saves information about each client request
 */
public class SenderInfo {
    private InetAddress ip;
    private int port;
    private String data;

    public SenderInfo() {}

    /**
     *
     * @param ip
     * @param port
     * @param data
     */
    public SenderInfo(InetAddress ip, int port, String data){
        this.setIp(ip);
        this.setPort(port);
        this.setData(data);
    }


    public InetAddress getIp() {
        return ip;
    }

    public void setIp(InetAddress ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return
                "IP=" + ip +
                ", PORT=" + port +
                ", DATA=" + data ;
    }
}
