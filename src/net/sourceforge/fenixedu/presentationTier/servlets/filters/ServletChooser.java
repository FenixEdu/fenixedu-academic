package net.sourceforge.fenixedu.presentationTier.servlets.filters;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.presentationTier.servlets.ActionServletWrapper;
import net.sourceforge.fenixedu.presentationTier.servlets.jsf.FenixFacesServlet;

public class ServletChooser implements Filter {

    private ActionServletWrapper actionServletWrapper = new ActionServletWrapper();

    private FenixFacesServlet fenixFacesServlet = new FenixFacesServlet();

    private boolean servletsAreInitialized = false;

    public void initServlets() {
	try {
	    actionServletWrapper.init(ActionServletWrapper.servletConfig);
	    fenixFacesServlet.init(FenixFacesServlet.servletConfig);
	} catch (ServletException e) {
	    throw new Error(e);
	}
	servletsAreInitialized = true;
    }

    public void init(final FilterConfig filterConfig) throws ServletException {
    }

    public void destroy() {
	actionServletWrapper = null;
	fenixFacesServlet = null;
    }

    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain)
	    throws IOException, ServletException {
	final Servlet servlet = getHttpServlet(servletRequest);
	if (servlet != null) {
//	    servlet.service(servletRequest, servletResponse);
	    
	    try {
		final Method method = filterChain.getClass().getDeclaredMethod("setServlet", new Class[] { Servlet.class });
		method.setAccessible(true);
		method.invoke(filterChain, new Object[] { servlet });
		System.out.println("Saved servlet in filter chain.");
		servlet.service(servletRequest, servletResponse);
	    } catch (NoSuchMethodException e) {
		throw new Error(e);
	    } catch (IllegalArgumentException e) {
		throw new Error(e);
	    } catch (IllegalAccessException e) {
		throw new Error(e);
	    } catch (InvocationTargetException e) {
		throw new Error(e);
	    }
	    System.out.println("Done invoking service.");
	} else {
	    filterChain.doFilter(servletRequest, servletResponse);
	}
    }

    protected Servlet getHttpServlet(final ServletRequest servletRequest) {
	System.out.println("1");
	if (servletRequest instanceof HttpServletRequest) {
	    System.out.println("2");
	    final HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
	    if (httpServletRequest.getAttribute(ContentInjectionRewriter.CONTEXT_ATTRIBUTE_NAME) != null) {
		System.out.println("3");
		final String uri = httpServletRequest.getRequestURI();
		System.out.println("It is a HttpServletRequest: " + uri);
		final int indexOfFaces = uri.indexOf(".faces");
		if (indexOfFaces >= 0) {
		    System.out.println("Returning servlet fenixFacesServlet.");
		    if (!servletsAreInitialized) {
			initServlets();
		    }
		    return fenixFacesServlet;
		}
		final int indexOfDo = uri.indexOf(".do");
		if (indexOfDo >= 0) {
		    System.out.println("Returning servlet ActionServletWrapper.");
		    if (!servletsAreInitialized) {
			initServlets();
		    }
		    return actionServletWrapper;
		}
	    }
	    System.out.println("Did not match with any servlet mapping." + servletRequest.getClass().getName());
	} else {
	    System.out.println("It's not a HttpServletRequest: " + servletRequest.getClass().getName());
	}
	return null;
    }
    // java.lang.Error
    // at
    // net.sourceforge.fenixedu.applicationTier.Servico.Authenticate.run(Authenticate.java:197)
    // at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
    // at
    // sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:39)
    // at
    // sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:25)
    // at java.lang.reflect.Method.invoke(Method.java:585)
    // at
    // pt.utl.ist.berserk.logic.serviceManager.ServiceInvoker.doInvocation(ServiceInvoker.java:164)
    // at
    // pt.utl.ist.berserk.logic.serviceManager.TransactionalServiceInvoker.invoke(TransactionalServiceInvoker.java:71)
    // at
    // pt.utl.ist.berserk.logic.serviceManager.ServiceManager.execute(ServiceManager.java:238)
    // at
    // pt.utl.ist.berserk.logic.serviceManager.ServiceManager.execute(ServiceManager.java:222)
    // at
    // net.sourceforge.fenixedu.applicationTier.ServiceManagerDefaultImpl.execute(ServiceManagerDefaultImpl.java:115)
    // at
    // net.sourceforge.fenixedu.applicationTier.ServiceManagerDefaultImpl.execute(ServiceManagerDefaultImpl.java:84)
    // at
    // net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory.executeService(ServiceManagerServiceFactory.java:24)
    // at
    // net.sourceforge.fenixedu.presentationTier.Action.LocalAuthenticationAction.doAuthentication(LocalAuthenticationAction.java:35)
    // at
    // net.sourceforge.fenixedu.presentationTier.Action.BaseAuthenticationAction.execute(BaseAuthenticationAction.java:50)
    // at
    // org.apache.struts.action.RequestProcessor.processActionPerform(RequestProcessor.java:419)
    // at
    // net.sourceforge.fenixedu.renderers.plugin.RenderersRequestProcessor.processActionPerform(RenderersRequestProcessor.java:241)
    // at
    // org.apache.struts.action.RequestProcessor.process(RequestProcessor.java:224)
    // at
    // net.sourceforge.fenixedu.renderers.plugin.RenderersRequestProcessor.process(RenderersRequestProcessor.java:92)
    // at
    // org.apache.struts.action.ActionServlet.process(ActionServlet.java:1194)
    // at org.apache.struts.action.ActionServlet.doPost(ActionServlet.java:432)
    // at javax.servlet.http.HttpServlet.service(HttpServlet.java:710)
    // at javax.servlet.http.HttpServlet.service(HttpServlet.java:803)
    // at
    // org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:269)
    // at
    // org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:188)
    // at
    // net.sourceforge.fenixedu.presentationTier.servlets.filters.ServletChooser.doFilter(ServletChooser.java:33)
    // at
    // org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:215)
    // at
    // org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:188)
    // at
    // net.sourceforge.fenixedu.presentationTier.servlets.filters.functionalities.CheckAvailabilityFilter.doFilter(CheckAvailabilityFilter.java:101)
    // at
    // org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:215)
    // at
    // org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:188)
    // at
    // net.sourceforge.fenixedu.presentationTier.servlets.filters.RequestRewriteFilter.doFilter(RequestRewriteFilter.java:107)
    // at
    // org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:215)
    // at
    // org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:188)
    // at
    // net.sourceforge.fenixedu.presentationTier.servlets.filters.RequestChecksumFilter.applyFilter(RequestChecksumFilter.java:61)
    // at
    // net.sourceforge.fenixedu.presentationTier.servlets.filters.RequestChecksumFilter.doFilter(RequestChecksumFilter.java:40)
    // at
    // org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:215)
    // at
    // org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:188)
    // at
    // net.sourceforge.fenixedu.presentationTier.servlets.filters.authentication.cas.CASFilter.doFilter(CASFilter.java:52)
    // at
    // org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:215)
    // at
    // org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:188)
    // at
    // net.sourceforge.fenixedu.presentationTier.servlets.filters.CheckUserViewFilter.doFilter(CheckUserViewFilter.java:37)
    // at
    // org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:215)
    // at
    // org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:188)
    // at
    // com.atlassian.util.profiling.filters.ProfilingFilter.doFilter(ProfilingFilter.java:153)
    // at
    // org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:215)
    // at
    // org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:188)
    // at
    // net.sourceforge.fenixedu.presentationTier.servlets.filters.RequestWrapperFilter.doFilter(RequestWrapperFilter.java:38)
    // at
    // org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:215)
    // at
    // org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:188)
    // at
    // net.sourceforge.fenixedu.presentationTier.servlets.filters.I18NFilter.doFilter(I18NFilter.java:73)
    // at
    // org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:215)
    // at
    // org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:188)
    // at
    // net.sourceforge.fenixedu.presentationTier.servlets.filters.CloseTransactionFilter.doFilter(CloseTransactionFilter.java:109)
    // at
    // org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:215)
    // at
    // org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:188)
    // at
    // net.sourceforge.fenixedu.presentationTier.servlets.filters.PathAccessControlFilter.doFilter(PathAccessControlFilter.java:119)
    // at
    // org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:215)
    // at
    // org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:188)
    // at
    // org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:213)
    // at
    // org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:174)
    // at
    // org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:433)
    // at
    // org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:127)
    // at
    // org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:117)
    // at
    // org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:108)
    // at
    // org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:151)
    // at
    // org.apache.coyote.http11.Http11Processor.process(Http11Processor.java:874)
    // at
    // org.apache.coyote.http11.Http11BaseProtocol$Http11ConnectionHandler.processConnection(Http11BaseProtocol.java:665)
    // at
    // org.apache.tomcat.util.net.PoolTcpEndpoint.processSocket(PoolTcpEndpoint.java:528)
    // at
    // org.apache.tomcat.util.net.LeaderFollowerWorkerThread.runIt(LeaderFollowerWorkerThread.java:81)
    // at
    // org.apache.tomcat.util.threads.ThreadPool$ControlRunnable.run(ThreadPool.java:689)

}
