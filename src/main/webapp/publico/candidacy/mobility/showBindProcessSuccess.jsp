<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<%@page import="net.sourceforge.fenixedu.util.stork.SPUtil" %>

<%!

	static String f(String value, Object ... args) {
    	return String.format(value, args);
	}
%>

<bean:define id="mappingPath" name="mappingPath"/>
<bean:define id="fullPath"><%= request.getContextPath() + "/publico" + mappingPath + ".do" %></bean:define>

<div class="breadcumbs">
	<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="http://gri.ist.utl.pt/en">NMCI</a> &gt;
	<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="http://gri.ist.utl.pt/en/ist/">Study at IST</a> &gt;
	<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href='<%= f("%s/candidacies/erasmus", request.getContextPath()) %>'><bean:message key="title.application.name.erasmus" bundle="CANDIDATE_RESOURCES"/></a> &gt;
	<bean:message key="erasmus.title.application.submission" bundle="CANDIDATE_RESOURCES" />
</div>

<h1><bean:message key="title.application.name.erasmus" bundle="CANDIDATE_RESOURCES"/></h1>

<bean:define id="process" name="process" />
<bean:define id="processId" name="process" property="externalId" />

<%
	String spToInvoke = SPUtil.getInstance().getSpInvokeUrl();
	String applicationUrl = SPUtil.getInstance().getSPReturnFromPepsToAccessApplicationUrl() + "?processId=" + processId;
	applicationUrl = java.net.URLEncoder.encode(applicationUrl);
	applicationUrl = f("%s?applicationUrl=%s", spToInvoke, applicationUrl);	
%>

<p>Now you can access your application with your national citizen card.</p>

<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="<%= applicationUrl %>">Click here in order to authenticate with your national citizen card</a>

</body>	
</html>
