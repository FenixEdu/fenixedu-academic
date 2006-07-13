<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<bean:define id="institutionUrl" type="java.lang.String"><bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/></bean:define>
<bean:define id="fenixUrl" type="java.lang.String"><bean:message key="fenix.url" bundle="GLOBAL_RESOURCES"/></bean:define>

<div id="logoist">
	<a href="<%= institutionUrl %>">
		<img alt="<bean:message key="institution.logo" bundle="IMAGE_RESOURCES" />" width="234" height="51" border="0"
			src="<bean:message key="university.logo.public" bundle="GLOBAL_RESOURCES" arg0="<%= request.getContextPath() %>"/>"/>
	</a>
</div>
<div id="header_links">
	<a href="<%= fenixUrl %>/loginPage.jsp">Login <bean:message key="dot.title" bundle="GLOBAL_RESOURCES"/></a>
	| <a href="<%= institutionUrl %>/html/instituto/index.shtml#cont">Contactos</a>
	| <a href="<%= institutionUrl %>/html/mapadosite/">Mapa do Site</a>
</div>

<div id="search">
		<form method="get" action="http://www.google.com/u/wwwist">
		<input type="hidden" name="ie" value="iso-8859-1" />
		<input type="hidden" name="domains" value="ist.utl.pt" />
		<input type="hidden" name="sitesearch" value="ist.utl.pt" />
		Pesquisar:
		<input type="text" id="textfield" name="q" size="17" />
		<input type="submit" id="submit" name="sa" value="Google" />
		</form>
	</div>
