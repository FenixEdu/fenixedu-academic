<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<% final String appContext = net.sourceforge.fenixedu._development.PropertiesManager.getProperty("app.context"); %>
<% final String context = (appContext != null && appContext.length() > 0) ? "/" + appContext : ""; %>
<bean:define id="hostURL" type="java.lang.String"><%= request.getScheme() %>://<%= request.getServerName() %>:<%= request.getServerPort() %><%= context %>/</bean:define>

<p>
	<span class="error">
		<html:errors/>
	</span>
</p>

<logic:messagesPresent property="error.exception.notAuthorized">
	<span class="error">
		<bean:message key="label.notAuthorized.courseInformation" />
	</span>	
</logic:messagesPresent>

<logic:messagesNotPresent property="error.exception.notAuthorized">
	<img alt="Administração de disciplina" src="<%= request.getContextPath() %>/images/title_adminDisc.gif" />
	<p><bean:message key="label.instructions" /></p>
</logic:messagesNotPresent>