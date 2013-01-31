package pt.ist.fenixframework.plugins.remote.servlet.request;

import javax.servlet.http.HttpServletRequest;

import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class RemoteObjectRequest extends ValidRequest {

	public RemoteObjectRequest(final HttpServletRequest request, final String[] pathParts) throws SecurityException,
			NoSuchMethodException {
		super(AbstractDomainObject.fromExternalId(pathParts[1]), pathParts);
	}

}
