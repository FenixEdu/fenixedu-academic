package net.sourceforge.fenixedu.domain;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter;

public class PendingRequest extends PendingRequest_Base {

    public static String buildVersion;

    public PendingRequest(HttpServletRequest request) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setGenerationDate(new DateTime());
        setPost(!request.getMethod().equalsIgnoreCase("GET"));
        setUrl(request.getContextPath() + request.getServletPath());
        setBuildVersion(buildVersion);
        final String queryString = request.getQueryString();
        for (Object object : request.getParameterMap().keySet()) {
            String key = (String) object;
            if (key != null) {
                final boolean isParam;
                if (queryString == null) {
                    isParam = false;
                } else {
                    final int paramIndex = queryString.indexOf(key);
                    final int nextChar = paramIndex + key.length();
                    isParam =
                            paramIndex >= 0 && queryString.length() > nextChar && queryString.charAt(nextChar) == '='
                                    && (paramIndex == 0 || queryString.charAt(paramIndex - 1) == '&');
                }
                addPendingRequestParameter(new PendingRequestParameter(key, request.getParameter(key), !isParam));
            }
        }

        for (Enumeration<String> e = request.getAttributeNames(); e.hasMoreElements();) {
            String key = e.nextElement();
            Object object = request.getAttribute(key);
            if (object.getClass().isArray()) {
                for (Object value : java.util.Arrays.asList(object)) {
                    PendingRequestParameter pendingRequestParameter = new PendingRequestParameter(key, (String) value, true);
                    addPendingRequestParameter(pendingRequestParameter);
                }
            } else if (object instanceof String) {
                addPendingRequestParameter(new PendingRequestParameter(key, (String) object, true));
            } else {
                // Not sure how to procede here...
            }
        }
    }

    public void delete() {
        for (PendingRequestParameter pendingRequestParameter : getPendingRequestParameter()) {
            pendingRequestParameter.delete();
        }
        setRootDomainObject(null);
        deleteDomainObject();
    }

    public String getRequestChecksumParameter() {
        for (final PendingRequestParameter pendingRequestParameter : getPendingRequestParameter()) {
            if (pendingRequestParameter.getParameterKey().equals(GenericChecksumRewriter.CHECKSUM_ATTRIBUTE_NAME)) {
                return pendingRequestParameter.getParameterValue();
            }
        }
        return null;
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.PendingRequestParameter> getPendingRequestParameter() {
        return getPendingRequestParameterSet();
    }

    @Deprecated
    public boolean hasAnyPendingRequestParameter() {
        return !getPendingRequestParameterSet().isEmpty();
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasUrl() {
        return getUrl() != null;
    }

    @Deprecated
    public boolean hasPost() {
        return getPost() != null;
    }

    @Deprecated
    public boolean hasGenerationDate() {
        return getGenerationDate() != null;
    }

    @Deprecated
    public boolean hasBuildVersion() {
        return getBuildVersion() != null;
    }

}
