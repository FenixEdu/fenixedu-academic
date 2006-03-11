<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h2><bean:message key="title.manage.homepage"/></h2>

<logic:notPresent name="UserView" property="person.homepage">
	<fr:create type="net.sourceforge.fenixedu.domain.Homepage" schema="person.homepage.manage">
		<fr:layout name="tabular">
			<fr:hidden slot="person" name="UserView" property="person"/>
			<fr:property name="classes" value="style1" />
			<fr:property name="columnClasses" value="listClasses" />
		</fr:layout>
	</fr:create>
</logic:notPresent>

<logic:present name="UserView" property="person.homepage">

	<% final String appContext = net.sourceforge.fenixedu._development.PropertiesManager.getProperty("app.context"); %>
	<% final String context = (appContext != null && appContext.length() > 0) ? "/" + appContext : ""; %>

	<bean:define id="homepageURL" type="java.lang.String"><%= request.getScheme() %>://<%= request.getServerName() %>:<%= request.getServerPort() %><%= context %>/homepage/<bean:write name="UserView" property="person.homepage.myUrl"/></bean:define>
	<bean:message key="person.homepage.url"/>
	<html:link href="<%= homepageURL %>"><bean:write name="homepageURL"/></html:link>
	
	<br/>
	<br/>

	<fr:edit name="UserView" property="person.homepage"
			type="net.sourceforge.fenixedu.domain.Homepage"
			 schema="person.homepage.manage">
		<fr:layout name="tabular">
			<fr:property name="classes" value="style1" />
			<fr:property name="columnClasses" value="listClasses" />
		</fr:layout>
	</fr:edit>
</logic:present>