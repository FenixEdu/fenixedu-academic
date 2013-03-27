package pt.utl.ist.report;

import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;

/**
 * @author Luis Cruz
 * 
 */
public class WikiPublisher {

	public static void main(String[] args) {
		final String wikiHost = args[0];
		final String wikiPort = args[1];
		final String wikiPage = args[2];
		final String filename = args[3];
        final String wikiUsername = args[4];
        final String wikiPassword = args[5];

		try {
			publishReport(wikiHost, wikiPort, wikiPage, filename, wikiUsername, wikiPassword);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		System.exit(0);
	}

	private static void publishReport(final String wikiHost,
			final String wikiPort, final String wikiPage, final String filename, String wikiUsername, String wikiPassword)
			throws IOException {

        final FileReader fileReader = new FileReader(filename);
        char[] buffer = new char[4096];
        final StringBuilder fileContents = new StringBuilder();
        int n = 0;
        while ((n = fileReader.read(buffer)) != -1) {
            fileContents.append(buffer, 0, n);
        }
        fileReader.close();

        final String fileContentsString = fileContents.toString();
        if (fileContentsString.trim().length() > 0) {
        	postResults(wikiHost, wikiPort, wikiPage, fileContentsString, wikiUsername, wikiPassword);
        }
	}

	private static void postResults(final String wikiHost,
			final String wikiPort, final String wikiPage,
			final String fileContents, final String wikiUsername, final String wikiPassword) {
		try {
			final HttpClient httpClient = HttpClientFactory.getHttpClient(
					wikiHost, wikiPort);

            final GetMethod method = HttpClientFactory.getGetMethod("/UserPreferences");
            executeMethod(httpClient, method);

            method.setPath("/UserPreferences" + "#preview");

            final NameValuePair[] loginNameValuePairs = new NameValuePair[5];
            loginNameValuePairs[0] = new NameValuePair("action", "userform");
            loginNameValuePairs[1] = new NameValuePair("username", wikiUsername);
            loginNameValuePairs[2] = new NameValuePair("password", wikiPassword);
            loginNameValuePairs[3] = new NameValuePair("login", "Login");
            loginNameValuePairs[4] = new NameValuePair("method", "post");
            method.setQueryString(loginNameValuePairs);
            executeMethod(httpClient, method);

            method.setPath("/" + wikiPage);
            executeMethod(httpClient, method);

			method.setPath("/" + wikiPage + "?action=edit");
			executeMethod(httpClient, method);

			method.setPath("/" + wikiPage);

			final NameValuePair[] nameValuePairs = new NameValuePair[4];
			nameValuePairs[0] = new NameValuePair("action", "savepage");
			nameValuePairs[1] = new NameValuePair("button_save", "Save Changes");
			nameValuePairs[2] = new NameValuePair("savetext", fileContents);
			nameValuePairs[3] = new NameValuePair("method", "post");
			method.setQueryString(nameValuePairs);
			executeMethod(httpClient, method);
        } catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected static void executeMethod(final HttpClient httpClient,
			final GetMethod method) throws IOException, HttpException {
		method.setFollowRedirects(true);
		httpClient.executeMethod(method);

        //method.releaseConnection();
		method.recycle();
	}

}