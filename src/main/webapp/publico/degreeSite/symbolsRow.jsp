<%@page import="pt.ist.bennu.core.util.ConfigurationManager"%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<bean:define id="institutionUrl" type="java.lang.String"><bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/></bean:define>
<bean:define id="fenixUrl" type="java.lang.String"><bean:message key="fenix.url" bundle="GLOBAL_RESOURCES"/></bean:define>
<bean:define id="searchTitle" type="java.lang.String"><bean:message bundle="GLOBAL_RESOURCES" key="search.title"/></bean:define>

<div id="logoist">
	<a href="<%= institutionUrl %>">
		<img alt="<bean:message key="institution.logo" bundle="IMAGE_RESOURCES" />" src="<bean:message key="university.logo.public" bundle="GLOBAL_RESOURCES" arg0="<%= request.getContextPath() %>"/>"/>
	</a>
</div>

<% if (!ConfigurationManager.getBooleanProperty("barra.as.authentication.broker", false)) { %>
<div id="header_links">
	<a href="<%= fenixUrl %>/loginPage.jsp">Login .IST<!--bean:message key="dot.title" bundle="GLOBAL_RESOURCES"/--></a>
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
	<form action="http://www.ist.utl.pt/search/">
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
