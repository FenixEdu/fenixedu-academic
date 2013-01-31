/*
 * $Id: MultipartRequestWrapper.java 164684 2005-04-25 23:19:11Z niallp $ 
 *
 * Copyright 1999-2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pt.ist.fenixframework.plugins.remote.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.Principal;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class functions as a wrapper around HttpServletRequest to
 * provide working getParameter methods for multipart requests. Once
 * Struts requires Servlet 2.3, this class will definately be changed to
 * extend javax.servlet.http.HttpServletRequestWrapper instead of
 * implementing HttpServletRequest. Servlet 2.3 methods are implemented
 * to return <code>null</code> or do nothing if called on. Use {@link #getRequest() getRequest} to retrieve the underlying
 * HttpServletRequest
 * object and call on the 2.3 method there, the empty methods are here only
 * so that this will compile with the Servlet 2.3 jar. This class exists temporarily
 * in the process() method of ActionServlet, just before the ActionForward is processed
 * and just after the Action is performed, the request is set back to the original
 * HttpServletRequest object.
 */
public class MultipartRequestWrapper implements HttpServletRequest {

	/** Logging instance */
	private static final Log log = LogFactory.getLog(MultipartRequestWrapper.class);

	/**
	 * The parameters for this multipart request
	 */
	protected Map parameters;

	/**
	 * The underlying HttpServletRequest
	 */
	protected HttpServletRequest request;

	public MultipartRequestWrapper(HttpServletRequest request) {
		this.request = request;
		this.parameters = new HashMap();
	}

	/**
	 * Sets a parameter for this request. The parameter is actually
	 * separate from the request parameters, but calling on the
	 * getParameter() methods of this class will work as if they weren't.
	 */
	public void setParameter(String name, String value) {
		String[] mValue = (String[]) parameters.get(name);
		if (mValue == null) {
			mValue = new String[0];
		}
		String[] newValue = new String[mValue.length + 1];
		System.arraycopy(mValue, 0, newValue, 0, mValue.length);
		newValue[mValue.length] = value;

		parameters.put(name, newValue);
	}

	/**
	 * Attempts to get a parameter for this request. It first looks in the
	 * underlying HttpServletRequest object for the parameter, and if that
	 * doesn't exist it looks for the parameters retrieved from the multipart
	 * request
	 */
	@Override
	public String getParameter(String name) {
		String value = request.getParameter(name);
		if (value == null) {
			String[] mValue = (String[]) parameters.get(name);
			if ((mValue != null) && (mValue.length > 0)) {
				value = mValue[0];
			}
		}
		return value;
	}

	/**
	 * Returns the names of the parameters for this request.
	 * The enumeration consists of the normal request parameter
	 * names plus the parameters read from the multipart request
	 */
	@Override
	public Enumeration getParameterNames() {
		Enumeration baseParams = request.getParameterNames();
		Vector list = new Vector();
		while (baseParams.hasMoreElements()) {
			list.add(baseParams.nextElement());
		}
		Collection multipartParams = parameters.keySet();
		Iterator iterator = multipartParams.iterator();
		while (iterator.hasNext()) {
			list.add(iterator.next());
		}
		return Collections.enumeration(list);
	}

	@Override
	public String[] getParameterValues(String name) {
		String[] value = request.getParameterValues(name);
		if (value == null) {
			value = (String[]) parameters.get(name);
		}
		return value;
	}

	/**
	 * Returns the underlying HttpServletRequest for this wrapper
	 */
	public HttpServletRequest getRequest() {
		return request;
	}

	//WRAPPER IMPLEMENTATIONS OF SERVLET REQUEST METHODS
	@Override
	public Object getAttribute(String name) {
		return request.getAttribute(name);
	}

	@Override
	public Enumeration getAttributeNames() {
		return request.getAttributeNames();
	}

	@Override
	public String getCharacterEncoding() {
		return request.getCharacterEncoding();
	}

	@Override
	public int getContentLength() {
		return request.getContentLength();
	}

	@Override
	public String getContentType() {
		return request.getContentType();
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		return request.getInputStream();
	}

	@Override
	public String getProtocol() {
		return request.getProtocol();
	}

	@Override
	public String getScheme() {
		return request.getScheme();
	}

	@Override
	public String getServerName() {
		return request.getServerName();
	}

	@Override
	public int getServerPort() {
		return request.getServerPort();
	}

	@Override
	public BufferedReader getReader() throws IOException {
		return request.getReader();
	}

	@Override
	public String getRemoteAddr() {
		return request.getRemoteAddr();
	}

	@Override
	public String getRemoteHost() {
		return request.getRemoteHost();
	}

	@Override
	public void setAttribute(String name, Object o) {
		request.setAttribute(name, o);
	}

	@Override
	public void removeAttribute(String name) {
		request.removeAttribute(name);
	}

	@Override
	public Locale getLocale() {
		return request.getLocale();
	}

	@Override
	public Enumeration getLocales() {
		return request.getLocales();
	}

	@Override
	public boolean isSecure() {
		return request.isSecure();
	}

	@Override
	public RequestDispatcher getRequestDispatcher(String path) {
		return request.getRequestDispatcher(path);
	}

	@Override
	public String getRealPath(String path) {
		return request.getRealPath(path);
	}

	//WRAPPER IMPLEMENTATIONS OF HTTPSERVLETREQUEST METHODS
	@Override
	public String getAuthType() {
		return request.getAuthType();
	}

	@Override
	public Cookie[] getCookies() {
		return request.getCookies();
	}

	@Override
	public long getDateHeader(String name) {
		return request.getDateHeader(name);
	}

	@Override
	public String getHeader(String name) {
		return request.getHeader(name);
	}

	@Override
	public Enumeration getHeaders(String name) {
		return request.getHeaders(name);
	}

	@Override
	public Enumeration getHeaderNames() {
		return request.getHeaderNames();
	}

	@Override
	public int getIntHeader(String name) {
		return request.getIntHeader(name);
	}

	@Override
	public String getMethod() {
		return request.getMethod();
	}

	@Override
	public String getPathInfo() {
		return request.getPathInfo();
	}

	@Override
	public String getPathTranslated() {
		return request.getPathTranslated();
	}

	@Override
	public String getContextPath() {
		return request.getContextPath();
	}

	@Override
	public String getQueryString() {
		return request.getQueryString();
	}

	@Override
	public String getRemoteUser() {
		return request.getRemoteUser();
	}

	@Override
	public boolean isUserInRole(String user) {
		return request.isUserInRole(user);
	}

	@Override
	public Principal getUserPrincipal() {
		return request.getUserPrincipal();
	}

	@Override
	public String getRequestedSessionId() {
		return request.getRequestedSessionId();
	}

	@Override
	public String getRequestURI() {
		return request.getRequestURI();
	}

	@Override
	public String getServletPath() {
		return request.getServletPath();
	}

	@Override
	public HttpSession getSession(boolean create) {
		return request.getSession(create);
	}

	@Override
	public HttpSession getSession() {
		return request.getSession();
	}

	@Override
	public boolean isRequestedSessionIdValid() {
		return request.isRequestedSessionIdValid();
	}

	@Override
	public boolean isRequestedSessionIdFromURL() {
		return request.isRequestedSessionIdFromURL();
	}

	@Override
	public boolean isRequestedSessionIdFromUrl() {
		return request.isRequestedSessionIdFromUrl();
	}

	//SERVLET 2.3 METHODS

	/**
	 * Implements the Servlet 2.3 <i>getParameterMap</i> method.
	 */
	@Override
	public Map getParameterMap() {
		Map map = new HashMap(parameters);
		Enumeration names = request.getParameterNames();
		while (names.hasMoreElements()) {
			String name = (String) names.nextElement();
			map.put(name, request.getParameterValues(name));
		}
		return map;
	}

	/**
	 * Use Reflection to invoke Servlet 2.3 <i>setCharacterEncoding</i>
	 * method on the wrapped Request.
	 */
	@Override
	public void setCharacterEncoding(String encoding) {
		invokeRequestMethod("setCharacterEncoding", new Object[] { encoding });
	}

	/**
	 * Use Reflection to invoke Servlet 2.3 <i>getRequestURL</i>
	 * method on the wrapped Request.
	 */
	@Override
	public StringBuffer getRequestURL() {
		return (StringBuffer) invokeRequestMethod("getRequestURL", null);
	}

	/**
	 * Use Reflection to invoke Servlet 2.3 <i>isRequestedSessionIdFromCookie</i>
	 * method on the wrapped Request.
	 */
	@Override
	public boolean isRequestedSessionIdFromCookie() {
		Object result = invokeRequestMethod("isRequestedSessionIdFromCookie", null);
		return (result == null) ? false : ((Boolean) result).booleanValue();
	}

	//SERVLET 2.4 METHODS

	/**
	 * Use Reflection to invoke Servlet 2.4 <i>getLocalAddr</i>
	 * method on the wrapped Request.
	 */
	@Override
	public String getLocalAddr() {
		return (String) invokeRequestMethod("getLocalAddr", null);
	}

	/**
	 * Use Reflection to invoke Servlet 2.4 <i>getLocalName</i>
	 * method on the wrapped Request.
	 */
	@Override
	public String getLocalName() {
		return (String) invokeRequestMethod("getLocalName", null);
	}

	/**
	 * Use Reflection to invoke Servlet 2.4 <i>getLocalPort</i>
	 * method on the wrapped Request.
	 */
	@Override
	public int getLocalPort() {
		Object result = invokeRequestMethod("getLocalPort", null);
		return (result == null) ? 0 : ((Integer) result).intValue();
	}

	/**
	 * Use Reflection to invoke Servlet 2.4 <i>getRemotePort</i>
	 * method on the wrapped Request.
	 */
	@Override
	public int getRemotePort() {
		Object result = invokeRequestMethod("getRemotePort", null);
		return (result == null) ? 0 : ((Integer) result).intValue();
	}

	/**
	 * Convenience method which uses reflection to invoke a method
	 * on the Request.
	 */
	private Object invokeRequestMethod(String name, Object[] args) {
		try {
			return MethodUtils.invokeExactMethod(request, name, args);
		} catch (NoSuchMethodException e) {
			if (log.isDebugEnabled()) {
				log.debug("Method '" + name + "' not defined for javax.servlet.http.HttpServletRequest");
			}
		} catch (InvocationTargetException e) {
			log.error("Error invoking method '" + name + "' ", e.getTargetException());
		} catch (IllegalAccessException e) {
			log.error("Error invoking method '" + name + "' ", e);
		}
		return null;
	}

}
