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
		
		try {
			String code = oauthIssuerImpl.authorizationCode();
			//TODO change the location. Get location from domain or request.getParameter("redirect_uri")
			OAuthResponse resp = OAuthASResponse
					.authorizationResponse(request, HttpServletResponse.SC_FOUND)
					.location("fenix://mobile/")
					.setCode(code)
					.buildQueryMessage();			
			
			//TODO save code/state(?) > domain
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

		//Get client_id
		//Get client_secret
		//get redirect_uri
		String authzCode = oauthRequest.getCode();
		//TODO check if all the information is correct

		//build tokens base64(oid_app ; istid ; rand ; device_id(?))
		String accessToken = oauthIssuerImpl.accessToken();
		String refreshToken = oauthIssuerImpl.refreshToken();

		//TODO save access, refresh token and timestamp

		OAuthResponse r = OAuthASResponse
				.tokenResponse(HttpServletResponse.SC_OK)
				.location("fenix://mobile")
				.setAccessToken(accessToken)
				.setExpiresIn("3600")
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
