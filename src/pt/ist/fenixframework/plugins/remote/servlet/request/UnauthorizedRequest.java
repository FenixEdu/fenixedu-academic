package pt.ist.fenixframework.plugins.remote.servlet.request;

import javax.servlet.http.HttpServletResponse;

public class UnauthorizedRequest extends InvalidRequest {

    @Override
    protected int getStatus() {
	return HttpServletResponse.SC_UNAUTHORIZED;
    }

    @Override
    protected String getDescription() {
	return "Not Authorized";
    }

}
