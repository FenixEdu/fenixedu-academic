package net.sourceforge.fenixedu.presentationTier.servlets.filters;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionUtils;

import org.joda.time.DateTime;

public class InitAccessControlFilter implements Filter {

    private static class PublicRequester implements IUserView {
	private class InformationNotAvailable extends RuntimeException {
	    private static final long serialVersionUID = -7527899059986512251L;

	    public InformationNotAvailable(String msg) {
		super(msg);
	    }
	}

	private InformationNotAvailable makeException() {
	    throw new InformationNotAvailable("property person not available on a public requester user view");
	}

	public Person getPerson() {
	    return null;
	}

	public String getUtilizador() {
	    throw makeException();
	}

	public String getFullName() {
	    throw makeException();
	}

	public Collection<RoleType> getRoleTypes() {
	    throw makeException();
	}

	public boolean hasRoleType(RoleType roleType) {
	    throw makeException();
	}

	public boolean isPublicRequester() {
	    return true;
	}

	public DateTime getExpirationDate() {
	    throw makeException();
	}

	@Override
	public boolean equals(Object obj) {
	    return obj instanceof PublicRequester;
	}

	@Override
	public int hashCode() {
	    return 0;
	}

	public String getPrivateConstantForDigestCalculation() {
	    return null;
	}

    }

    private static final PublicRequester PUBLIC_REQUESTER = new PublicRequester();

    public void init(FilterConfig config) {
    }

    public void destroy() {
    }

    protected IUserView getUserView(final ServletRequest request) {
	IUserView userView = null;
	if (request instanceof HttpServletRequest) {
	    final HttpServletRequest httpRequest = (HttpServletRequest) request;
	    userView = SessionUtils.getUserView(httpRequest);
	}
	return userView == null ? PUBLIC_REQUESTER : userView;
    }

    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
    		throws IOException, ServletException {
	AccessControl.setUserView(getUserView(request));
	chain.doFilter(request, response);
    }

}

