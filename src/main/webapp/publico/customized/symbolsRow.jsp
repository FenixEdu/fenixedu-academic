<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<%@page import="net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext"%>
<%@page import="net.sourceforge.fenixedu.util.FenixConfigurationManager"%>
<bean:define id="institutionUrl" type="java.lang.String"><%= net.sourceforge.fenixedu.domain.Instalation.getInstance().getInstituitionURL() %></bean:define>
<bean:define id="loginUrl" type="java.lang.String"><%= FenixConfigurationManager.getConfiguration().getLoginPage() %></bean:define>
<bean:define id="siteMapUrl" type="java.lang.String"><bean:message bundle="GLOBAL_RESOURCES" key="siteMap.link"/></bean:define>
<bean:define id="searchUrl" type="java.lang.String"><bean:message bundle="GLOBAL_RESOURCES" key="search.url"/></bean:define>
<bean:define id="searchDomain" type="java.lang.String"><bean:message bundle="GLOBAL_RESOURCES" key="search.domain"/></bean:define>
<bean:define id="searchSite" type="java.lang.String"><bean:message bundle="GLOBAL_RESOURCES" key="search.site"/></bean:define>
<bean:define id="searchTitle" type="java.lang.String"><bean:message bundle="GLOBAL_RESOURCES" key="search.title"/></bean:define>

<logic:notPresent name="site">
	<bean:define id="site" name="<%= FunctionalityContext.CONTEXT_KEY %>" property="selectedContainer" toScope="request"/>
</logic:notPresent>

<div id="logoist">

	<logic:equal name="site" property="showInstitutionLogo" value="true">
		<a href="<%= institutionUrl %>">
			<img alt="<%=net.sourceforge.fenixedu.domain.Instalation.getInstance().getInstalationName() %>" src="<%= request.getContextPath() + "/images/newImage2012/logo-ist.png"%>"/>
		</a>
	</logic:equal>

	<logic:equal name="site" property="defaultLogoUsed" value="false">
		<logic:present name="site" property="logo">
			<bean:define id="logoFile" type="net.sourceforge.fenixedu.domain.UnitSiteFile" name="site" property="logo"/>
				<a href="<%= institutionUrl %>">
					<img alt="Logo" src="<%= logoFile.getDownloadUrl() %>" class="usitelogo"/>
				</a>
		</logic:present>
	</logic:equal>

	<logic:equal name="site" property="defaultLogoUsed" value="true">
		<logic:present name="siteDefaultLogo">
			<bean:define id="logoUrl" name="siteDefaultLogo" type="java.lang.String"/>
				<a href="<%= institutionUrl %>">
					<img alt="Logo" src="<%= logoUrl %>" class="usitelogo"/>
				</a>
		</logic:present>
	</logic:equal>
</div>

<logic:equal name="site" property="defaultLogoUsed" value="true">
	<logic:notPresent name="siteDefaultLogo">
		<div class="unitname">
			<logic:notEmpty name="site" property="unit.acronym">
				<h1>
					<fr:view name="site" property="unit.acronym"/>
				</h1>
				<p>
					<fr:view name="site" property="unit.nameI18n"/>
				</p>
			</logic:notEmpty>
			<logic:empty name="site" property="unit.acronym">
				<h1>
					<fr:view name="site" property="unit.nameI18n"/>
				</h1>
			</logic:empty>
		</div>
	</logic:notPresent>
</logic:equal>

<% if (!FenixConfigurationManager.isBarraAsAuthenticationBroker()) { %>
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
<% } %>

<div id="search">
	<form method="get" action="<%= searchUrl %>">
		<input alt="input.ie" type="hidden" name="ie" value="UTF-8" />
		<input alt="input.domains" type="hidden" name="domains" value="<%= searchDomain %>" />
		<input alt="input.sitesearch" type="hidden" name="sitesearch" value="<%= searchSite %>" />
		<label for="textfield">
			<input alt="input.q" placeholder="<%= searchTitle %>" type="text" id="textfield" name="q" size="17"/>
		</label>
		<input alt="input.sa" type="submit" id="submit" name="sa" value="Google" />
	</form>
</div>