package net.sourceforge.fenixedu.webServices.jersey.beans.publico;

import net.sourceforge.fenixedu._development.PropertiesManager;

import org.codehaus.jackson.annotate.JsonIgnore;

public class FenixAbout {

    String newsRss;
    String eventsRss;

    @JsonIgnore
    private static FenixAbout instance;

    private FenixAbout() {
        newsRss = PropertiesManager.getProperty("fenix.api.news.rss.url");
        eventsRss = PropertiesManager.getProperty("fenix.api.events.rss.url");
    }

    public static FenixAbout getInstance() {
        if (instance == null) {
            instance = new FenixAbout();
        }
        return instance;
    }

    public String getNewsRss() {
        return newsRss;
    }

    public void setNewsRss(String newsRss) {
        this.newsRss = newsRss;
    }

    public String getEventsRss() {
        return eventsRss;
    }

    public void setEventsRss(String eventsRss) {
        this.eventsRss = eventsRss;
    }

}
