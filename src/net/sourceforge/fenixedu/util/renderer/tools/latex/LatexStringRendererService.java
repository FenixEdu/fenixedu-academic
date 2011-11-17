package net.sourceforge.fenixedu.util.renderer.tools.latex;

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
    private String protocol;

    public LatexStringRendererService() {
	this.host = PropertiesManager.getProperty("latex.service.host");
	this.port = Integer.valueOf(PropertiesManager.getProperty("latex.service.port"));
	this.uri = PropertiesManager.getProperty("latex.service.uri");
	this.protocol = PropertiesManager.getProperty("latex.service.protocol");
    }

    public byte[] render(final String texData, LatexFontSize size) {
	HttpClient client = null;
	PostMethod httpMethod = null;

	try {
	    client = new HttpClient();
	    client.getHostConfiguration().setHost(this.host, this.port, this.protocol);
	    httpMethod = new PostMethod(this.uri);

	    httpMethod.setRequestBody(new NameValuePair[] { new NameValuePair("title", texData),
		    new NameValuePair("fontSize", size.size) });

	    try {
		client.executeMethod(httpMethod);
		byte[] responseBody = httpMethod.getResponseBody();

		if (httpMethod.getStatusCode() != 200) {
		    throw new LatexStringRendererException();
		}

		return responseBody;
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
