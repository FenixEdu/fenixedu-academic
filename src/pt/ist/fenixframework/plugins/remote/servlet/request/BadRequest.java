package pt.ist.fenixframework.plugins.remote.servlet.request;

import javax.servlet.http.HttpServletResponse;

public class BadRequest extends InvalidRequest {

    final String details;

    public BadRequest(final String details) {
	this.details = details;
    }

    public BadRequest() {
	this(null);
    }

    @Override
    protected int getStatus() {
	return HttpServletResponse.SC_BAD_REQUEST;
    }

    @Override
    protected String getDescription() {
	return "Bad Request";
    }

    @Override
    protected String getDetails() {
	return details;
    }

}
