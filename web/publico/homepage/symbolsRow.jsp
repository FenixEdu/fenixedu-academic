<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@page import="net.sourceforge.fenixedu._development.PropertiesManager"%>

<bean:define id="institutionUrl" type="java.lang.String"><bean:message bundle="GLOBAL_RESOURCES" key="institution.url"/></bean:define>
<bean:define id="loginUrl" type="java.lang.String"><%= PropertiesManager.getProperty("login.page") %></bean:define>
<bean:define id="siteMapUrl" type="java.lang.String"><bean:message bundle="GLOBAL_RESOURCES" key="siteMap.link"/></bean:define>
<bean:define id="searchUrl" type="java.lang.String"><bean:message bundle="GLOBAL_RESOURCES" key="search.url"/></bean:define>
<bean:define id="searchDomain" type="java.lang.String"><bean:message bundle="GLOBAL_RESOURCES" key="search.domain"/></bean:define>
<bean:define id="searchSite" type="java.lang.String"><bean:message bundle="GLOBAL_RESOURCES" key="search.site"/></bean:define>

<div id="logoist">
	<a href="<%= institutionUrl %>">
		<img alt="<bean:message key="institution.logo" bundle="IMAGE_RESOURCES" />" width="234" height="51" src="<bean:message bundle="GLOBAL_RESOURCES" key="university.logo.public" arg0="<%= request.getContextPath() %>"/>"/>
	</a>
</div>

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

<div id="search">
	<form method="get" action="<%= searchUrl %>">
		<input alt="input.ie" type="hidden" name="ie" value="iso-8859-1" />
		<input alt="input.domains" type="hidden" name="domains" value="<%= searchDomain %>" />
		<input alt="input.sitesearch" type="hidden" name="sitesearch" value="<%= searchSite %>" />
		<label for="textfield">
			<bean:message bundle="GLOBAL_RESOURCES" key="search.title"/>:
			<input alt="input.q" type="text" id="textfield" name="q" size="17"/>
		</label>
		<input alt="input.sa" type="submit" id="submit" name="sa" value="Google" />
	</form>
</div>
