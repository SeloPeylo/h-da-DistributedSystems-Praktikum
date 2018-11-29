package SmartHome;
public class SHMain {

    public static void main(String[] args) {
	// write your code here
        SmartHome smartHome = new SmartHome();
        Thread t = new Thread(smartHome);

        t.start();
    }
}
