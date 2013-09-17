<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@page import="net.sourceforge.fenixedu._development.PropertiesManager"%>

<bean:define id="institutionUrl" type="java.lang.String"><bean:message bundle="GLOBAL_RESOURCES" key="institution.url"/></bean:define>
<bean:define id="loginUrl" type="java.lang.String"><%= PropertiesManager.getProperty("login.page") %></bean:define>
<bean:define id="siteMapUrl" type="java.lang.String"><bean:message bundle="GLOBAL_RESOURCES" key="siteMap.link"/></bean:define>
<bean:define id="searchUrl" type="java.lang.String"><bean:message bundle="GLOBAL_RESOURCES" key="search.url"/></bean:define>
<bean:define id="searchDomain" type="java.lang.String"><bean:message bundle="GLOBAL_RESOURCES" key="search.domain"/></bean:define>
<bean:define id="searchSite" type="java.lang.String"><bean:message bundle="GLOBAL_RESOURCES" key="search.site"/></bean:define>
<bean:define id="searchTitle" type="java.lang.String"><bean:message bundle="GLOBAL_RESOURCES" key="search.title"/></bean:define>

<div id="logoist">
	<a href="<%= institutionUrl %>">
		<img alt="<bean:message key="institution.logo" bundle="IMAGE_RESOURCES" />" src="<bean:message bundle="GLOBAL_RESOURCES" key="university.logo.public" arg0="<%= request.getContextPath() %>"/>"/>
	</a>
</div>

<% if (!PropertiesManager.useBarraAsAuthenticationBroker()) { %>
<div id="header_links">
	<a href="<%= loginUrl %>">
		<bean:message bundle="GLOBAL_RESOURCES" key="dot.login"/>
		<bean:message bundle="GLOBAL_RESOURCES" key="dot.title"/>
	</a>
	|
	<a href="<%= siteMapUrl %>">
		<bean:message bundle="GLOBAL_RESOURCES" key="siteMap.title"/>
	</a>
</div>
<% } %>

<div id="search">
	<form method="get" action="<%= searchUrl %>">
		<input alt="input.ie" type="hidden" name="ie" value="<%= net.sourceforge.fenixedu._development.PropertiesManager.DEFAULT_CHARSET %>" />
		<input alt="input.domains" type="hidden" name="domains" value="<%= searchDomain %>" />
		<input alt="input.sitesearch" type="hidden" name="sitesearch" value="<%= searchSite %>" />
		<label for="textfield">
			<input alt="input.q" placeholder="<%= searchTitle %>" type="text" id="textfield" name="q" size="17"/>
		</label>
		<input alt="input.sa" type="submit" id="submit" name="sa" value="Google" />
	</form>
</div>
