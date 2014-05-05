<%@page import="org.fenixedu.bennu.core.util.CoreConfiguration"%>
<%@page import="net.sourceforge.fenixedu.domain.organizationalStructure.Unit"%>
<%@page import="net.sourceforge.fenixedu.domain.Installation"%>
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt"%>
<html:xhtml />

<bean:define id="institutionUrl" type="java.lang.String">
	<%=Installation.getInstance().getInstituitionURL()%>
</bean:define>
<bean:define id="institutionName" type="java.lang.String">
	<%=Unit.getInstitutionName()%>
</bean:define>

<head>
	<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/CSS/fenixEduBar.css" media="screen" />
	<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/CSS/general.css" media="screen" />
</head>

<div id="fenixEduBar">
	<h1 id="institutionLogo">
		<a href="<%=institutionUrl%>" target="_blank"><%=institutionName %></a>
	</h1>
	<div id="authentication">
		<logic:notPresent name="LOGGED_USER_ATTRIBUTE">
			<h1 id="logout">
				<!-- NO_CHECKSUM --><html:link styleClass="button" href="loginPage.jsp">Login</html:link>
			</h1>
		</logic:notPresent>
		
		<logic:present name="LOGGED_USER_ATTRIBUTE">
			<p id="user">
				<bean:write name="LOGGED_USER_ATTRIBUTE" property="person.firstAndLastName"/>	
			</p>
		
			<% if (CoreConfiguration.getConfiguration().developmentMode()) { %>
				<h1 id="debug-mode">
					<a class="button" href="#"><bean:message key="debug.mode" bundle="GLOBAL_RESOURCES" /></a>
				</h1>
			<% } %>
			
			<h1 id="logout">
				<a class="button" href="<%=request.getContextPath()%>/logoff.do"><bean:message key="link.logout" bundle="GLOBAL_RESOURCES" /></a>
			</h1>
		</logic:present>
	</div>
</div>