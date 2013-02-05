package pt.ist.fenixframework.plugins.remote.servlet.request;

import javax.servlet.http.HttpServletRequest;

public class RemoteClassRequest extends ValidRequest {

    public RemoteClassRequest(final HttpServletRequest request, final String[] pathParts) throws ClassNotFoundException,
            SecurityException, NoSuchMethodException {
        super(Class.forName(pathParts[1]), pathParts);
    }

}
