/*
 * Created on 2003/12/25
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.security.MessageDigest;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.Authenticate;
import net.sourceforge.fenixedu.applicationTier.logging.SystemInfo;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.util.Base64;
import org.restlet.Client;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Method;
import org.restlet.data.Parameter;
import org.restlet.data.Protocol;
import org.restlet.data.Reference;
import org.restlet.engine.security.SslContextFactory;
import org.restlet.util.Series;

import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter;

/**
 * @author Luis Cruz
 */
public class MonitorSystemDA extends FenixDispatchAction {

    public ActionForward monitor(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        IUserView userView = UserView.getUser();

        SystemInfo systemInfoApplicationServer = ServiceManagerServiceFactory.getSystemInfo(userView);
        request.setAttribute("systemInfoApplicationServer", systemInfoApplicationServer);

        SystemInfo systemInfoWebContainer = ServiceManagerServiceFactory.getSystemInfo(userView);
        request.setAttribute("systemInfoWebContainer", systemInfoWebContainer);

        String useBarraAsAuth = PropertiesManager.getProperty("barra.as.authentication.broker");
        request.setAttribute("useBarraAsAuth", useBarraAsAuth);

        request.setAttribute("startMillis", ""
                + ExecutionSemester.readActualExecutionSemester().getAcademicInterval().getStartMillis());
        request.setAttribute("endMillis", ""
                + ExecutionSemester.readActualExecutionSemester().getAcademicInterval().getEndMillis());
        request.setAttribute("chronology", ""
                + ExecutionSemester.readActualExecutionSemester().getAcademicInterval().getChronology().toString());

        return mapping.findForward("Show");
    }

    public ActionForward mock(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        final Person person = getDomainObject(request, "personID");

        final String requestURL = request.getRequestURL().toString();

        IUserView currentUser = UserView.getUser();

        if (currentUser == null || !currentUser.hasRoleType(RoleType.MANAGER)) {
            throw new Error("not.authorized");
        }

        final IUserView userView = new Authenticate().mock(person, requestURL);

        UserView.setUser(userView);

        request.getSession().setAttribute(SetUserViewFilter.USER_SESSION_ATTRIBUTE, userView);

        final ActionForward forward = new ActionForward("/home.do", true);
        forward.setModule("/");
        return forward;
    }

    public ActionForward testRestlet(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        byte[] hashedSecret =
                messageDigest.digest(PropertiesManager.getProperty("external.application.workflow.equivalences.uri.secret")
                        .getBytes());

        final Reference reference =
                new Reference(PropertiesManager.getProperty("external.application.workflow.equivalences.uri") + "aaaa")
                        .addQueryParameter("creator", "xxxx").addQueryParameter("requestor", "yyyyy")
                        .addQueryParameter("base64Secret", new String(Base64.encode(hashedSecret)));

        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
            }

            @Override
            public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
            }
        } };

        // Install the all-trusting trust manager
        final SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());

        Client client = null;
        try {
            client = new Client(Protocol.HTTPS);
            client.setContext(new org.restlet.Context());
            client.getContext().getAttributes().put("sslContextFactory", new SslContextFactory() {
                @Override
                public SSLContext createSslContext() throws Exception {
                    return sc;
                }

                @Override
                public void init(Series<Parameter> parameters) {
                }
            });

            final Response responseFromClient = client.handle(new Request(Method.POST, reference, null));

            if (responseFromClient.getStatus().getCode() != 200) {
                throw new DomainException(responseFromClient.getStatus().getThrowable() != null ? responseFromClient.getStatus()
                        .getThrowable().getMessage() : "error.equivalence.externalEntity");
            }
        } finally {
            Context.setCurrent(null);
            Response.setCurrent(null);
            if (client != null) {
                client.stop();
            }
        }

        return mapping.findForward("Show");
    }

    public ActionForward switchBarraAsAuthenticationBroker(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final String useBarraAsAuth = request.getParameter("useBarraAsAuth");
        PropertiesManager.setProperty("barra.as.authentication.broker", useBarraAsAuth);

        return monitor(mapping, form, request, response);
    }

    /*
     * public ActionForward monitor(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws Exception {

    IUserView userView = UserView.getUser();

    SystemInfo systemInfoApplicationServer = ServiceManagerServiceFactory.getSystemInfo(userView);
    request.setAttribute("systemInfoApplicationServer", systemInfoApplicationServer);

    SystemInfo systemInfoWebContainer = ServiceManagerServiceFactory.getSystemInfo(userView);
    request.setAttribute("systemInfoWebContainer", systemInfoWebContainer);
    
    String useBarraAsAuth = PropertiesManager.getProperty("barra.as.authentication.broker");
    request.setAttribute("useBarraAsAuth", new UseBarraAsAuthenciationBrokerBean(useBarraAsAuth));

    request.setAttribute("startMillis", ""
    	+ ExecutionSemester.readActualExecutionSemester().getAcademicInterval().getStartMillis());
    request.setAttribute("endMillis", ""
    	+ ExecutionSemester.readActualExecutionSemester().getAcademicInterval().getEndMillis());
    request.setAttribute("chronology", ""
    	+ ExecutionSemester.readActualExecutionSemester().getAcademicInterval().getChronology().toString());

    return mapping.findForward("Show");
    }

    public ActionForward mock(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws Exception {

    final Person person = getDomainObject(request, "personID");

    final String requestURL = request.getRequestURL().toString();

    IUserView currentUser = UserView.getUser();

    if (currentUser == null || !currentUser.hasRoleType(RoleType.MANAGER)) {
        throw new Error("not.authorized");
    }

    final IUserView userView = new Authenticate().mock(person, requestURL);

    UserView.setUser(userView);

    request.getSession().setAttribute(SetUserViewFilter.USER_SESSION_ATTRIBUTE, userView);

    final ActionForward forward = new ActionForward("/home.do", true);
    forward.setModule("/");
    return forward;
    }

    public ActionForward testRestlet(ActionMapping mapping, ActionForm form, HttpServletRequest request,
        HttpServletResponse response) throws Exception {

    MessageDigest messageDigest = MessageDigest.getInstance("MD5");
    byte[] hashedSecret = messageDigest.digest(PropertiesManager.getProperty(
    	"external.application.workflow.equivalences.uri.secret").getBytes());

    final Reference reference = new Reference(PropertiesManager.getProperty("external.application.workflow.equivalences.uri")
    	+ "aaaa").addQueryParameter("creator", "xxxx").addQueryParameter("requestor", "yyyyy")
    	.addQueryParameter("base64Secret", new String(Base64.encode(hashedSecret)));

    TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {

        @Override
        public X509Certificate[] getAcceptedIssuers() {
    	return null;
        }

        @Override
        public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
        }

        @Override
        public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
        }
    } };

    // Install the all-trusting trust manager
    final SSLContext sc = SSLContext.getInstance("SSL");
    sc.init(null, trustAllCerts, new java.security.SecureRandom());

    Client client = null;
    try {
        client = new Client(Protocol.HTTPS);
        client.setContext(new org.restlet.Context());
        client.getContext().getAttributes().put("sslContextFactory", new SslContextFactory() {
    	@Override
    	public SSLContext createSslContext() throws Exception {
    	    return sc;
    	}

    	@Override
    	public void init(Series<Parameter> parameters) {
    	}
        });

        final Response responseFromClient = client.handle(new Request(Method.POST, reference, null));

        if (responseFromClient.getStatus().getCode() != 200) {
    	throw new DomainException(responseFromClient.getStatus().getThrowable() != null ? responseFromClient.getStatus()
    		.getThrowable().getMessage() : "error.equivalence.externalEntity");
        }
    } finally {
        Context.setCurrent(null);
        Response.setCurrent(null);
        if (client != null)
    	client.stop();
    }

    return mapping.findForward("Show");
    }
    
    public ActionForward switchBarraAsAuthenticationBroker(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws Exception {

    final UseBarraAsAuthenciationBrokerBean bean = (UseBarraAsAuthenciationBrokerBean) request.getAttribute("useBarraAsAuth");
    PropertiesManager.setProperty("barra.as.authentication.broker", bean.getUseBarraAsAuth());
    RenderUtils.invalidateViewState();

    return monitor(mapping, form, request, response);
    }
    
    public class UseBarraAsAuthenciationBrokerBean implements Serializable {
    private String useBarraAsAuth;
    
    public UseBarraAsAuthenciationBrokerBean() {
        super();
    }
    
    public UseBarraAsAuthenciationBrokerBean(String useBarraAsAuth) {
        this();
        setUseBarraAsAuth(useBarraAsAuth);
    }
    
    public String getUseBarraAsAuth() {
        return useBarraAsAuth;
    }
    
    public void setUseBarraAsAuth(String useBarraAsAuth) {
        this.useBarraAsAuth = useBarraAsAuth;
    }
    }
     */
}