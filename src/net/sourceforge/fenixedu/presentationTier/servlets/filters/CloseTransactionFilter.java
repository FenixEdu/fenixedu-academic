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
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.stm.Transaction;

public class CloseTransactionFilter implements Filter {

	private class PublicRequester implements IUserView {
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
			throw makeException();
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
	}

	public void init(FilterConfig config) {

	}

	public void destroy() {

	}

	protected IUserView getUserView(ServletRequest request) {
		IUserView userView = null;
		if (request instanceof HttpServletRequest) {
			HttpServletRequest httpRequest = (HttpServletRequest) request;
				userView = SessionUtils.getUserView(httpRequest);
		}

		if (userView == null)
			userView = new PublicRequester();

		return userView;
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
        try {
            Transaction.begin(true);
            Transaction.currentFenixTransaction().setReadOnly();
            setTransactionOwner(request);
			chain.doFilter(request, response);
		} finally {
			Transaction.forceFinish();
		}
	}

	/**
	 * 
	 */
	private void setTransactionOwner(ServletRequest request) {
		AccessControl.setUserView(this.getUserView(request));
	}
}
