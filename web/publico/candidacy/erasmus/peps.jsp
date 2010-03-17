<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.ChecksumRewriter"%>
<%@page import="net.sourceforge.fenixedu.util.stork.SPUtil" %>

<%!

	static String f(String value, Object ... args) {
    	return String.format(value, args);
	}
%>


<bean:define id="mappingPath" name="mappingPath"/>
<bean:define id="fullPath"><%= request.getContextPath() + "/publico" + mappingPath + ".do" %></bean:define>

<div class="breadcumbs">
	<a href="http://www.ist.utl.pt">IST</a> &gt;
	<%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="http://gri.ist.utl.pt"><bean:message key="title.candidate" bundle="CANDIDATE_RESOURCES"/></a> &gt;
	<bean:message key="title.application.name.erasmus" bundle="CANDIDATE_RESOURCES"/>
</div>

<h1><bean:message key="title.application.name.erasmus" bundle="CANDIDATE_RESOURCES"/></h1>

<h2>Choose Country</h2>

<p class="mvert05">
You will be presented with a list of countries in the context of STORK project.
</p>

<div class="h_box">
	<p  class="mvert05">
		If the country of your nationality is not in the list then proceed to the email registration following this link
		<b><%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="<%= f("%s/candidacies/erasmus/preregistration", request.getContextPath()) %>">Email registration process »</a></b>
	</p>
</div>

<%
	String applicationUrl = SPUtil.getInstance().getSpInvokeUrl();
%>

<p class="mvert05">
Please choose the country of your nationality and click to proceed:
</p>

<iframe src="<%= applicationUrl %>" width="100%" height="200px" style="border:0px;"></iframe>

</body>	
</html>
