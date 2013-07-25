package net.sourceforge.fenixedu.presentationTier.Action.externalServices;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.amber.oauth2.as.issuer.MD5Generator;
import org.apache.amber.oauth2.as.issuer.OAuthIssuer;
import org.apache.amber.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.amber.oauth2.as.request.OAuthTokenRequest;
import org.apache.amber.oauth2.as.response.OAuthASResponse;
import org.apache.amber.oauth2.common.exception.OAuthSystemException;
import org.apache.amber.oauth2.common.message.OAuthResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.CASAuthenticationAction;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.utils.RequestUtils;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.functionalities.FilterFunctionalityContext;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;


@Mapping(module = "external", path = "/oauth", scope = "request", parameter = "method")
@Forwards({
	@Forward(name = "showAuthorizationPage", path = "showAuthorizationPage"),
	@Forward(name = "oauthErrorPage", path = "/auth/oauthErrorPage.jsp")	
})

public class OAuthAction extends FenixDispatchAction {

	//http://localhost:8080/ciapl/external/oauth.do?method=getUserPermission&client_id=123123&redirect_uri=http://www.google.com
	public ActionForward getUserPermission(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		Person person = getLoggedPerson(request);
		if (person == null) {
			RequestUtils.sendLoginRedirect(request,response);
			return null;
		} else {
			return mapping.findForward("showAuthorizationPage");
		}

	}

	//http://localhost:8080/ciapl/external/oauth.do?method=userConfirmation&...
	public ActionForward userConfirmation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());

		Person person = getLoggedPerson(request);
		if (person == null) {
			return null;
		}

		//TODO check if redirect_uri and client_id are correct
		String client_id = request.getParameter("client_id");
		String redirect_uri = request.getParameter("redirect_uri");

		try {
			String code = oauthIssuerImpl.authorizationCode();
			OAuthResponse resp = OAuthASResponse
					.authorizationResponse(request, HttpServletResponse.SC_FOUND)
					.location(redirect_uri)
					.setCode(code)
					.buildQueryMessage();			
			
			//TODO save code/state(?) > domain <-> time
			//Build object
			response.sendRedirect(resp.getLocationUri());

		} catch (OAuthSystemException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
		return null;
	}

	//http://localhost:8080/ciapl/external/oauth.do?method=getTokens&...
	public ActionForward getTokens(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		OAuthTokenRequest oauthRequest = new OAuthTokenRequest(request);
		OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());

		String client_id = oauthRequest.getClientId();
		String client_secret = oauthRequest.getClientSecret();
		String redirect_uri = oauthRequest.getRedirectURI();
		String authzCode = oauthRequest.getCode();
		String expires = "3600";
			
		//TODO check if all the information is correct

		//build tokens base64(oid_app ; istid ; rand ; device_id(?))
		String accessToken = oauthIssuerImpl.accessToken();
		String refreshToken = oauthIssuerImpl.refreshToken();

		//TODO save access, refresh token and timestamp
		//clean Code

		OAuthResponse r = OAuthASResponse
				.tokenResponse(HttpServletResponse.SC_OK)
				.location(redirect_uri)
				.setAccessToken(accessToken)
				.setExpiresIn(expires)
				.setRefreshToken(refreshToken)
				.buildJSONMessage();

		response.setStatus(r.getResponseStatus());
		PrintWriter pw = response.getWriter();
		pw.print(r.getBody());
		pw.flush();
		pw.close();
		return null;
	}	
}
