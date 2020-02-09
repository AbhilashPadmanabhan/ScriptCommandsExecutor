import org.sikuli.script.App;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Key;
import org.sikuli.script.Location;
import org.sikuli.script.Region;
import org.sikuli.script.Screen;

public class ScriptExecutor {

	public static void main(String[] args) throws Exception {
		Screen s = new Screen();
        App.open("/Applications/Google Chrome.app"); //To Open Chrome Browser
        try {
            s.type(Key.CMD, "L"); //To enter URL in the Address Bar 
            s.type("facebook.com"); //Enter Facebook as URL
            s.type(Key.ENTER); //Hit Enter Button in Address Bar after ebtering URL
            Thread.sleep(10000); //Sleep for sometime to load the entire page
            int x = 960; //Facebook Email Field X,Y value (Note this is my machine value)
            int y = 132;
            s.click(new Location(x, y)); //Click inside the Email Field
            s.click(new Region(1, 1, 1, 1));
            Thread.sleep(5000); //Sleep to let the mouse move and click the Email Field
            s.type("mohit.rathi"); //Type Username in Email field
            Thread.sleep(5000);
            int a = 1123;
            int b = 133;
            s.click(new Location(a, b));
            Thread.sleep(5000);
            s.type("passw0rd");
            Thread.sleep(5000);
            int c = 1288;
            int d = 133;
            s.click(new Location(c, d)); //Click on Log-In button
            Thread.sleep(5000);
            s.type(Key.CMD, "Q");
            //App.close("/Applications/Google Chrome.app");
        } catch (FindFailed e) {
            e.printStackTrace();
        }
	}
}
