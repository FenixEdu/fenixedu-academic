package net.sourceforge.fenixedu.presentationTier.servlets.filters;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionUtils;
import net.sourceforge.fenixedu.renderers.plugin.upload.CommonsFile;
import net.sourceforge.fenixedu.renderers.plugin.upload.UploadedFile;

import org.apache.commons.fileupload.DefaultFileItemFactory;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.log4j.Level;

/**
 * 17/Fev/2003
 * 
 * @author jpvl
 */
public class RequestWrapperFilter implements Filter {


    public static class FenixSessionWrapper implements HttpSession {

	public static Logger logger = Logger.getLogger(FenixSessionWrapper.class.getName());
	private HttpSession session;

	public FenixSessionWrapper(HttpSession session) {
	    this.session = session;
	}

	public Object getAttribute(String arg0) {
	    return session.getAttribute(arg0);
	}

	public Enumeration getAttributeNames() {
	    return session.getAttributeNames();
	}

	public long getCreationTime() {
	    return session.getCreationTime();
	}

	public String getId() {
	    return session.getId();
	}

	public long getLastAccessedTime() {
	    return session.getLastAccessedTime();
	}

	public int getMaxInactiveInterval() {
	    return session.getMaxInactiveInterval();
	}

	public ServletContext getServletContext() {
	    return session.getServletContext();
	}

	public HttpSessionContext getSessionContext() {
	    return session.getSessionContext();
	}

	public Object getValue(String arg0) {
	    return session.getValue(arg0);
	}

	public String[] getValueNames() {
	    return session.getValueNames();
	}

	public void invalidate() {
	    session.invalidate();

	}

	public boolean isNew() {
	    return session.isNew();
	}

	public void putValue(String arg0, Object arg1) {
	    session.putValue(arg0, arg1);
	}

	public void removeAttribute(String arg0) {
	    session.removeAttribute(arg0);
	}

	public void removeValue(String arg0) {
	    session.removeValue(arg0);
	}

	public void setAttribute(String arg0, Object arg1) {
	    if (!(arg1 instanceof Serializable)) {
		try {
		    throw new Error("Trying to find out where's the serialization problem");
		} catch (Error e) {		    
		    logger.warning(arg1.getClass().getName() + "\n");
		    e.printStackTrace();
		}
	    }
	    session.setAttribute(arg0, arg1);
	}

	public void setMaxInactiveInterval(int arg0) {
	    session.setMaxInactiveInterval(arg0);

	}

    }

    public void init(FilterConfig config) {
    }

    public void destroy() {
    }

    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
	    throws IOException, ServletException {
	final HttpServletRequest httpServletRequest = (HttpServletRequest) request;
	chain.doFilter(new FenixHttpServletRequestWrapper(httpServletRequest), response);
	setSessionTimeout(httpServletRequest);
    }

    private void setSessionTimeout(final HttpServletRequest request) {
	final HttpSession session = request.getSession(false);
	if (session != null) {
	    final IUserView userView = SessionUtils.getUserView(request);
	    final int m = userView == null ? 600 : 7200;
	    session.setMaxInactiveInterval(m);
	}
    }

    public static class FenixHttpServletRequestWrapper extends HttpServletRequestWrapper {

	public static final String ITEM_MAP_ATTRIBUTE = "FenixHttpServletRequestWrapper_itemsMap";

	private static final String PAGE_DEFAULT = "0";

	private static final String[] PAGE_DEFAULT_ARRAY = { PAGE_DEFAULT };

	final Hashtable<String, UploadedFile> itemsMap = new Hashtable<String, UploadedFile>();

	final Map<String, List<String>> parameters = new HashMap<String, List<String>>();

	public FenixHttpServletRequestWrapper(HttpServletRequest request) {
	    super(request);
	    if (FileUpload.isMultipartContent(request)) {
		try {
		    parseRequest(request);
		} catch (FileUploadException e) {
		    throw new Error(e);
		}
	    }
	    request.setAttribute(ITEM_MAP_ATTRIBUTE, itemsMap);
	}

	private void parseRequest(final HttpServletRequest request) throws FileUploadException {
	    final List fileItems = new FileUpload(new DefaultFileItemFactory()).parseRequest(request);

	    for (final Object object : fileItems) {
		final FileItem item = (FileItem) object;

		if (item.isFormField()) {
		    addParameter(item.getFieldName(), item.getString());
		} else {
		    UploadedFile uploadedFile = new CommonsFile(item);

		    if (uploadedFile.getName() != null && uploadedFile.getName().length() > 0) {
			itemsMap.put(item.getFieldName(), uploadedFile);
		    }

		    addParameter(item.getFieldName(), uploadedFile.getName());
		}
	    }
	}

	@Override
	public HttpSession getSession() {
	    return new FenixSessionWrapper(super.getSession());
	}

	private void addParameter(final String fieldName, final String value) {
	    List<String> strings = parameters.get(fieldName);
	    if (strings == null) {
		strings = new ArrayList<String>();
		final String[] values = super.getParameterValues(fieldName);
		if (values != null) {
		    for (final String v : values) {
			strings.add(v);
		    }
		}
		parameters.put(fieldName, strings);
	    }
	    strings.add(value);
	}

	@Override
	public String getRemoteUser() {
	    final IUserView userView = SessionUtils.getUserView(this);
	    return userView != null ? userView.getUtilizador() : super.getRemoteUser();
	}

	@Override
	public boolean isUserInRole(final String role) {
	    final IUserView userView = SessionUtils.getUserView(this);
	    final RoleType roleType = RoleType.valueOf(role);
	    return userView != null && userView.hasRoleType(roleType);
	}

	@Override
	public Enumeration getParameterNames() {
	    final Vector params = new Vector();

	    final Enumeration paramEnum = super.getParameterNames();
	    boolean gotPageParameter = false;
	    while (paramEnum.hasMoreElements()) {
		final String parameterName = (String) paramEnum.nextElement();
		if (paramEnum.equals("page")) {
		    gotPageParameter = true;
		}
		params.add(parameterName);
	    }
	    if (!gotPageParameter) {
		params.add("page");
	    }
	    for (final String name : parameters.keySet()) {
		params.add(name);
	    }

	    return params.elements();
	}

	@Override
	public String[] getParameterValues(final String parameter) {
	    if (parameters.containsKey(parameter)) {
		final List<String> parameterList = parameters.get(parameter);
		return parameterList.toArray(new String[0]);
	    }
	    final String[] parameterValues = super.getParameterValues(parameter);
	    return parameterValues == null && parameter.equals("page") ? PAGE_DEFAULT_ARRAY : parameterValues;
	}

	@Override
	public String getParameter(final String parameter) {
	    if (parameters.containsKey(parameter)) {
		final List<String> parameterList = parameters.get(parameter);
		return parameterList.size() > 0 ? parameterList.get(0) : null;
	    }
	    final String parameterValue = super.getParameter(parameter);
	    return parameterValue == null && parameter.equals("page") ? PAGE_DEFAULT : parameterValue;
	}

	@Override
	public Map<String, String[]> getParameterMap() {
	    Map<String, String[]> resultMap = new Hashtable<String, String[]>(super.getParameterMap());

	    for (String parameter : this.parameters.keySet()) {
		resultMap.put(parameter, parameters.get(parameter).toArray(new String[0]));
	    }

	    return resultMap;
	}

    }
}