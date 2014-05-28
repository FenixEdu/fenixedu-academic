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
<%@page import="org.fenixedu.bennu.portal.domain.PortalConfiguration"%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.util.FenixConfigurationManager"%>

<bean:define id="institutionUrl" type="java.lang.String"><%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %></bean:define>
<bean:define id="fenixUrl" type="java.lang.String"><bean:message key="fenix.url" bundle="GLOBAL_RESOURCES"/></bean:define>
<bean:define id="searchTitle" type="java.lang.String"><bean:message bundle="GLOBAL_RESOURCES" key="search.title"/></bean:define>

<div id="logoist">
	<a href="<%= institutionUrl %>">
		<img alt="<%=org.fenixedu.bennu.portal.domain.PortalConfiguration.getInstance().getApplicationTitle().getContent() %>" src="<bean:message key="university.logo.public" bundle="GLOBAL_RESOURCES" arg0="<%= request.getContextPath() %>"/>"/>
	</a>
</div>

<% if (!FenixConfigurationManager.isBarraAsAuthenticationBroker()) { %>
<div id="header_links">
	<a href="<%= fenixUrl %>/loginPage.jsp">Login <%= PortalConfiguration.getInstance().getApplicationTitle().getContent() %><!--bean:message key="dot.title" bundle="GLOBAL_RESOURCES"/--></a>
	| <a href="<%= institutionUrl %>/pt/sobre-IST/contacto">Contactos</a>
	| <a href="<%= institutionUrl %>/html/mapadosite/">Mapa do Site</a>
</div>
<% } %>

<div id="search">
	<%--
	<form method="get" action="http://www.google.com/u/wwwist">
		<input alt="input.ie" type="hidden" name="ie" value="<%= java.nio.charset.Charset.defaultCharset().name() %>" />
		<input alt="input.domains" type="hidden" name="domains" value="ist.utl.pt" />
		<input alt="input.sitesearch" type="hidden" name="sitesearch" value="ist.utl.pt" />
		<label for="textfield">
			<bean:message bundle="GLOBAL_RESOURCES" key="search.title"/>:
			<input alt="input.q" type="text" id="textfield" name="q" size="17"/>
		</label>
		<input alt="input.sa" type="submit" id="submit" name="sa" value="Google" />
	</form>
	 --%>
	<form action="<%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %>search/">
		<input type="hidden" name="cx" value="007266409324096302065:xkagxvojzme" />
		<input type="hidden" name="cof" value="FORID:10" />
		<input type="hidden" name="ie" value="UTF-8" />
		<label for="textfield">
			<input alt="input.q" placeholder="<%= searchTitle %>" type="text" id="textfield" name="q" size="17"/>
		</label>
		<label for="submit_button">
			<input alt="input.sa" type="submit" id="submit" name="sa" value="Google" />
		</label>
	</form>
</div>
