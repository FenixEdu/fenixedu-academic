package net.sourceforge.fenixedu.util.renderer.tools;

import java.io.IOException;

import net.sourceforge.fenixedu._development.PropertiesManager;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

public class LatexStringRendererService {
    
    private String host;
    private int port;
    private String uri;

    public LatexStringRendererService() {
	this.host = PropertiesManager.getProperty("latex.service.host");
	this.port = Integer.valueOf(PropertiesManager.getProperty("latex.service.port"));
	this.uri = PropertiesManager.getProperty("latex.service.url");
    }

    public byte[] render(final String texData) {
	HttpClient client = null;
	PostMethod httpMethod = null;

	try {
	    client = new HttpClient();
	    client.getHostConfiguration().setHost(this.host, this.port, "http");
	    httpMethod = new PostMethod(this.uri + "?");

	    httpMethod.setRequestBody(new NameValuePair[] { new NameValuePair("texData", texData) });

	    try {
		client.executeMethod(httpMethod);
		return httpMethod.getResponseBody();
	    } catch (HttpException e) {
		throw new RuntimeException(e);
	    } catch (IOException e) {
		throw new RuntimeException(e);
	    }

	} finally {
	    if (httpMethod != null) {
		httpMethod.releaseConnection();
	    }
	}
    }

}
