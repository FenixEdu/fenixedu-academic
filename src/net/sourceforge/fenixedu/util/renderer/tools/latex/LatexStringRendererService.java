package net.sourceforge.fenixedu.util.renderer.tools.latex;

import java.io.IOException;

import net.sourceforge.fenixedu._development.PropertiesManager;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.lang.CharEncoding;

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
	    HttpClientParams httpClientParams = new HttpClientParams();
	    httpClientParams.setContentCharset(CharEncoding.UTF_8);
	    client = new HttpClient(httpClientParams);

	    client.getHostConfiguration().setHost(this.host, this.port, this.protocol);
	    httpMethod = new PostMethod(this.uri);

	    httpMethod.setRequestHeader(new Header("Accept-Charset", CharEncoding.ISO_8859_1 + "," + CharEncoding.UTF_8 + ";q=0.7,*;q=0.7"));
	    httpMethod.setRequestHeader(new Header("Accept-Language", "en-us,en;q=0.5"));
	    httpMethod.setRequestHeader(new Header("Accept-Encoding", "gzip, deflate"));
	    httpMethod.setRequestHeader(new Header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"));

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
