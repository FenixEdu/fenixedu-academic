package pt.utl.ist.renderers;

public class ParserConfiguration {

    private String destination;
    private String configuration;
    private String dtd;
    private String type;
    
    public String getDestination() {
        return this.destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getConfiguration() {
        return this.configuration;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }

    public String getDtd() {
        return this.dtd;
    }

    public void setDtd(String dtd) {
        this.dtd = dtd;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
}
