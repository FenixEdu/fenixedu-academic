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

		try {
			publishReport(wikiHost, wikiPort, wikiPage, filename);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		System.exit(0);
	}

	private static void publishReport(final String wikiHost,
			final String wikiPort, final String wikiPage, final String filename)
			throws IOException {

        final FileReader fileReader = new FileReader(filename);
        char[] buffer = new char[4096];
        final StringBuffer fileContents = new StringBuffer();
        int n = 0;
        while ((n = fileReader.read(buffer)) != -1) {
            fileContents.append(buffer, 0, n);
        }
        fileReader.close();

		postResults(wikiHost, wikiPort, wikiPage, fileContents.toString());
	}

	private static void postResults(final String wikiHost,
			final String wikiPort, final String wikiPage,
			final String fileContents) {
		try {
			final HttpClient httpClient = HttpClientFactory.getHttpClient(
					wikiHost, wikiPort);

			final GetMethod editMethod = HttpClientFactory
					.getGetMethod("/" + wikiPage + "?action=edit");
			executeMethod(httpClient, editMethod);

			final GetMethod submitEditFormMethod = HttpClientFactory
					.getGetMethod(wikiPage + "#preview");

			final NameValuePair[] nameValuePairs = new NameValuePair[2];
			nameValuePairs[0] = new NameValuePair("action", "savepage");
			nameValuePairs[1] = new NameValuePair("savetext", fileContents);
			submitEditFormMethod.setQueryString(nameValuePairs);
			executeMethod(httpClient, submitEditFormMethod);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected static void executeMethod(final HttpClient httpClient,
			final GetMethod method) throws IOException, HttpException {
		method.setFollowRedirects(true);
		httpClient.executeMethod(method);
		method.releaseConnection();
		method.recycle();
	}

}