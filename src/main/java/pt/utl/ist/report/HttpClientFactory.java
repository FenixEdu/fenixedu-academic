package pt.utl.ist.report;

import net.sourceforge.fenixedu._development.LogLevel;

import org.apache.commons.httpclient.DefaultMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.protocol.DefaultProtocolSocketFactory;
import org.apache.commons.httpclient.protocol.Protocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Luis Cruz
 * @author Shezad Anavarali
 * 
 */
public class HttpClientFactory {
    private static final Logger logger = LoggerFactory.getLogger(HttpClientFactory.class);

	private final static int CONNECTION_TIMEOUT = 30000000;

	protected HttpClientFactory() {
	}

	public static HttpClient getHttpClient(final String host, final String port) {
		int serverPort = Integer.parseInt(port);

		final HttpClient client = new HttpClient();
		final Protocol protocol = getProtocol(host, serverPort);

		client.getHostConfiguration().setHost(host, serverPort, protocol);
		client.getState().setCookiePolicy(CookiePolicy.getDefaultPolicy());
		client.setConnectionTimeout(CONNECTION_TIMEOUT);
		client.setStrictMode(false);

		if (LogLevel.DEBUG) {
		    logger.debug("Created new HttpClient to: http://" + host + ":" + port);
		}

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
