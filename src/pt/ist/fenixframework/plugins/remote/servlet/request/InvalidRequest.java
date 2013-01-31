package pt.ist.fenixframework.plugins.remote.servlet.request;

import java.io.IOException;
import java.io.Writer;

public abstract class InvalidRequest extends RemoteRequest {

	@Override
	protected void writeResponseBody(final Writer writer) throws IOException {
		writer.write("<invalidRequest>");
		writeTag(writer, "description", getDescription());
		writeTag(writer, "details", getDetails());
		writer.write("</invalidRequest>");
	}

	protected abstract String getDescription();

	protected String getDetails() {
		return null;
	}

}
