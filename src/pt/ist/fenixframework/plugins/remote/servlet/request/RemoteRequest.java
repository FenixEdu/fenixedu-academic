package pt.ist.fenixframework.plugins.remote.servlet.request;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.servlet.http.HttpServletResponse;

public abstract class RemoteRequest {
	public void sendResponse(final HttpServletResponse response) throws IOException {
		response.setStatus(getStatus());
		response.setContentType(getContentType());
//	response.setCharacterEncoding(getCharacterEncoding());
		Writer writer = new BufferedWriter(new OutputStreamWriter(response.getOutputStream(), getCharacterEncoding()));
		writer.write("<remote>");
		writeResponseBody(writer);
		writer.write("</remote>");
		writer.close();
	}

	protected String getCharacterEncoding() {
		return "UTF-8";
	}

	protected String getContentType() {
		return "text/xml";
	}

	protected abstract int getStatus();

	protected abstract void writeResponseBody(final Writer writer) throws IOException;

	protected void writeTag(final Writer writer, final String tag, String content) throws IOException {
		if (content != null) {
			writer.write("<" + tag + ">");
			writer.write(content);
			writer.write("</" + tag + ">");
		}
	}

}
