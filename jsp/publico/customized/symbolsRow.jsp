<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu._development.PropertiesManager"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<bean:define id="institutionUrl" type="java.lang.String"><bean:message bundle="GLOBAL_RESOURCES" key="institution.url"/></bean:define>
<bean:define id="loginUrl" type="java.lang.String"><%= PropertiesManager.getProperty("login.page") %></bean:define>
<bean:define id="siteMapUrl" type="java.lang.String"><bean:message bundle="GLOBAL_RESOURCES" key="siteMap.link"/></bean:define>
<bean:define id="searchUrl" type="java.lang.String"><bean:message bundle="GLOBAL_RESOURCES" key="search.url"/></bean:define>
<bean:define id="searchDomain" type="java.lang.String"><bean:message bundle="GLOBAL_RESOURCES" key="search.domain"/></bean:define>
<bean:define id="searchSite" type="java.lang.String"><bean:message bundle="GLOBAL_RESOURCES" key="search.site"/></bean:define>

<div id="logoist">

	<a href="<%= institutionUrl %>">
		<img alt="<bean:message key="institution.logo" bundle="IMAGE_RESOURCES" />" src="<%= request.getContextPath() + "/images/ist_logo.gif"%>"/>
	</a>

	<logic:equal name="site" property="defaultLogoUsed" value="false">
	<logic:present name="site" property="logo">
		<bean:define id="logoFile" type="net.sourceforge.fenixedu.domain.UnitSiteFile" name="site" property="logo"/>
			<img alt="Logo" src="<%= logoFile.getDownloadUrl() %>"/>
	</logic:present>
	</logic:equal>

</div>


	<logic:notPresent name="site" property="logo">
		<div style="position: absolute; left: 60px; top: 45px;">
			<h1 class="unitname">
				<span><fr:view name="site" property="unit.acronym"/></span>
				<br/><span class=""><fr:view name="site" property="unit.name"/></span>
			</h1>
		</div>
	</logic:notPresent>
	

<div id="header_links">
	<a href="<%= loginUrl %>">
		<bean:message bundle="GLOBAL_RESOURCES" key="dot.login"/>
		<bean:message bundle="GLOBAL_RESOURCES" key="dot.title"/>
	</a>

<logic:notEmpty name="site" property="sortedTopLinks">
   |
   <fr:view name="site" property="sortedTopLinks">
		<fr:layout name="flowLayout">
			<fr:property name="eachLayout" value="values"/>
			<fr:property name="eachSchema" value="showFooterLink"/>
			<fr:property name="htmlSeparator" value="|"/>
		</fr:layout>
	</fr:view> 
</logic:notEmpty>

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