package net.sourceforge.fenixedu.presentationTier.servlets.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.Authenticate;
import net.sourceforge.fenixedu.domain.AppUserSession;

import org.joda.time.DateTime;

import org.apache.commons.codec.binary.Base64;


import pt.ist.fenixWebFramework.security.User;
import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.pstm.AbstractDomainObject;
import pt.ist.fenixframework.pstm.Transaction;

public class JerseyOAuth2 implements Filter {

	final static String ACCESS_TOKEN = "access_token";
	public static final String USER_SESSION_ATTRIBUTE = "username";


	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		final HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
		final HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
		doFilter(httpServletRequest, httpServletResponse, filterChain);
	}

	public void doFilter(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain)
			throws IOException, ServletException {
		if (checkAccessControl(request)) {
			//Transaction.begin(true);
			//Transaction.currentFenixTransaction().setReadOnly();
			System.out.println("oiii");
			System.out.println("UserInicio: "+ UserView.getUser().getUsername());
			//Transaction.forceFinish();
			
			
			filterChain.doFilter(request, response);
			
	
			//Transaction.begin(true);
			//Transaction.currentFenixTransaction().setReadOnly();
			System.out.println("oiii");
			System.out.println("UserFinal: "+ UserView.getUser().getUsername());

			
			//Transaction.forceFinish();
		} else {
			throw new ServletException("Not Authorized");
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}

	private Boolean checkAccessControl(final HttpServletRequest request) {

		final String accessToken = request.getHeader(ACCESS_TOKEN);

		String[] extractedAccessToken = extractAccessToken(request, accessToken);

		if (extractedAccessToken == null) {
			return Boolean.FALSE;
		}

		Boolean found = Boolean.TRUE;		
		return found;
	}

	private String[] extractAccessToken(final HttpServletRequest request, final String accessToken) {


		String accessTokenDecoded = new String(Base64.decodeBase64(accessToken));
		String[] accessTokenBuilder = accessTokenDecoded.split(":");
		System.out.println("AccessToken: "+accessTokenDecoded);
		System.out.println("[0]: "+accessTokenBuilder[0]);
		System.out.println("[1]: "+accessTokenBuilder[1]);

		//Transaction.begin(true);
		//Transaction.currentFenixTransaction().setReadOnly();

		if (accessTokenBuilder.length != 2) {
			System.out.println("problem length");
			return null;
		}

		AppUserSession appUserSession = getAppUserSession(accessTokenBuilder[0]);
		if (appUserSession == null) {
			System.out.println("no AppUserSession");
			return null;
		}

		System.out.println("Username: "+appUserSession.getUsername());
		System.out.println("Author: "+appUserSession.getApplication().getAuthor().getPerson().getName());
		System.out.println("AccessToken: "+appUserSession.getAccessToken());
		System.out.println("AccessToken: "+appUserSession.getApplication().getName());

		if (!appUserSession.matchesAccessToken(accessToken)) {				
			System.out.println("problem in accesstoken");
			//return null;
		}

		net.sourceforge.fenixedu.domain.User foundUser = net.sourceforge.fenixedu.domain.User.readUserByUserUId("ist158444");
	
		Authenticate as = new Authenticate();
		
		UserView.setUser(as.mock(foundUser.getPerson(),request.toString()));

		
		
		System.out.println("intermedia");
		System.out.println("User: "+ UserView.getUser().getUsername());

		
		//Transaction.forceFinish();
		return accessTokenBuilder;
	}

	private AppUserSession getAppUserSession(String appUserSession) {
		//check if it is a number
		DomainObject domainObject = AbstractDomainObject.fromExternalId(appUserSession);
		if (domainObject == null || !(domainObject instanceof AppUserSession)) {
			return null;
		}
		return (AppUserSession) domainObject;
	}

}
