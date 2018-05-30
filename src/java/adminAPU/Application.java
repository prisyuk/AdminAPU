package adminAPU;           

import adminAPU.Pages.WelcomePage;
import org.apache.wicket.protocol.http.WebApplication;

public class Application extends WebApplication {

    public Application() {
    }

    public Class getHomePage() {
        return WelcomePage.class;
    }

}