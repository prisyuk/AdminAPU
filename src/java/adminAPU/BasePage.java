package adminAPU;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.Component;

public class BasePage extends WebPage {

    private Component headerPanel;
    private Component footerPanel;

    public BasePage() {
        add(headerPanel = new HeaderPanel("headerPanel"));
        add(footerPanel = new FooterPanel("footerPanel"));
    }

}