<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.ChecksumRewriter"%>
<%@page import="net.sourceforge.fenixedu.util.stork.SPUtil" %>

<bean:define id="mappingPath" name="mappingPath"/>
<bean:define id="fullPath"><%= request.getContextPath() + "/publico" + mappingPath + ".do" %></bean:define>

<div class="breadcumbs">
	<a href="http://www.ist.utl.pt">IST</a> &gt;
	<%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="http://gri.ist.utl.pt"><bean:message key="title.candidate" bundle="CANDIDATE_RESOURCES"/></a> &gt;
	<bean:message key="title.application.name.erasmus" bundle="CANDIDATE_RESOURCES"/>
</div>

<h1><bean:message key="title.application.name.erasmus" bundle="CANDIDATE_RESOURCES"/></h1>

<%
	String applicationUrl = SPUtil.getInstance().getSpInvokeUrl();
 %>
<iframe src="<%= applicationUrl %>" width="100%" height="300px" style="border:0px;"></iframe>

</body>	
</html>
