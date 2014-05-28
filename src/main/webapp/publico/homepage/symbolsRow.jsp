<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@page import="net.sourceforge.fenixedu.util.FenixConfigurationManager"%>

<bean:define id="institutionUrl" type="java.lang.String"><%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %></bean:define>
<bean:define id="loginUrl" type="java.lang.String"><%= FenixConfigurationManager.getConfiguration().getLoginPage() %></bean:define>
<bean:define id="siteMapUrl" type="java.lang.String"><bean:message bundle="GLOBAL_RESOURCES" key="siteMap.link"/></bean:define>
<bean:define id="searchUrl" type="java.lang.String"><bean:message bundle="GLOBAL_RESOURCES" key="search.url"/></bean:define>
<bean:define id="searchDomain" type="java.lang.String"><bean:message bundle="GLOBAL_RESOURCES" key="search.domain"/></bean:define>
<bean:define id="searchSite" type="java.lang.String"><bean:message bundle="GLOBAL_RESOURCES" key="search.site"/></bean:define>
<bean:define id="searchTitle" type="java.lang.String"><bean:message bundle="GLOBAL_RESOURCES" key="search.title"/></bean:define>

<div id="logoist">
	<a href="<%= institutionUrl %>">
		<img alt="<%=org.fenixedu.bennu.portal.domain.PortalConfiguration.getInstance().getApplicationTitle().getContent() %>" src="<bean:message bundle="GLOBAL_RESOURCES" key="university.logo.public" arg0="<%= request.getContextPath() %>"/>"/>
	</a>
</div>

<% if (!FenixConfigurationManager.isBarraAsAuthenticationBroker()) { %>
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
		<input alt="input.ie" type="hidden" name="ie" value="<%= java.nio.charset.Charset.defaultCharset().name() %>" />
		<input alt="input.domains" type="hidden" name="domains" value="<%= searchDomain %>" />
		<input alt="input.sitesearch" type="hidden" name="sitesearch" value="<%= searchSite %>" />
		<label for="textfield">
			<input alt="input.q" placeholder="<%= searchTitle %>" type="text" id="textfield" name="q" size="17"/>
		</label>
		<input alt="input.sa" type="submit" id="submit" name="sa" value="Google" />
	</form>
</div>
