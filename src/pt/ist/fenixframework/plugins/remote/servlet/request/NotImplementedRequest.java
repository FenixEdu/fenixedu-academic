package pt.ist.fenixframework.plugins.remote.servlet.request;

import javax.servlet.http.HttpServletResponse;

public class NotImplementedRequest extends InvalidRequest {

    @Override
    protected int getStatus() {
        return HttpServletResponse.SC_NOT_IMPLEMENTED;
    }

    @Override
    protected String getDescription() {
        return "Not Implemented";
    }

}
