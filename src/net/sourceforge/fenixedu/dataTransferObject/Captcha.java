package net.sourceforge.fenixedu.dataTransferObject;

import java.io.Serializable;

public class Captcha implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String response;
    
    public Captcha() {
    }

    public Captcha(final String response) {
	this.response = response;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
    
        

}
