package pt.utl.ist.report;

import org.apache.commons.httpclient.DefaultMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.protocol.DefaultProtocolSocketFactory;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.log4j.Logger;

/**
 * @author Luis Cruz
 * @author Shezad Anavarali
 * 
 */
public class HttpClientFactory {

	private final static Logger logger = Logger
			.getLogger(HttpClientFactory.class);

	private final static int CONNECTION_TIMEOUT = 30000000;

	protected HttpClientFactory() {
	}

	public static HttpClient getHttpClient(final String host, final String port) {
		int serverPort = Integer.parseInt(port);

		final HttpClient client = new HttpClient();
		final Protocol protocol = getProtocol(host, serverPort);

		client.getHostConfiguration().setHost(host, serverPort, protocol);
		client.getState().setCookiePolicy(CookiePolicy.COMPATIBILITY);
		client.setConnectionTimeout(CONNECTION_TIMEOUT);
		client.setStrictMode(true);

		logger.debug("Created new HttpClient to: http://" + host + ":" + port);

		return client;
	}

	protected static Protocol getProtocol(final String serverHostname,
			final int serverPort) {
		return new Protocol(serverHostname, new DefaultProtocolSocketFactory(), serverPort);
	}

	public static GetMethod getGetMethod(final String path) {
		final GetMethod method = new GetMethod(path);

		DefaultMethodRetryHandler retryhandler = new DefaultMethodRetryHandler();
		retryhandler.setRequestSentRetryEnabled(false);
		retryhandler.setRetryCount(3);
		method.setMethodRetryHandler(retryhandler);

		return method;
	}

}
