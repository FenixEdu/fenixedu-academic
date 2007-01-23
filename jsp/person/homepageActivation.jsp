<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>

<em><bean:message key="label.person.main.title" /></em>
<h2><bean:message key="title.manage.homepage" bundle="HOMEPAGE_RESOURCES"/></h2>

<div class="infoop2">
<p class="mvert0"><bean:message key="message.homepage.activation" bundle="HOMEPAGE_RESOURCES"/></p>
</div>

<p><span class="error"><!-- Error messages go here --><html:errors /></span></p>

<logic:notPresent name="UserView" property="person.user.userUId">
	<span class="error">
		<bean:message key="message.resource.not.available.for.external.users" bundle="HOMEPAGE_RESOURCES"/>
	</span>
</logic:notPresent>

<logic:present name="UserView" property="person.user.userUId">
<html:form action="/manageHomepage">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="activateHomepage"/>

	<p>
		<bean:message key="label.homepage.activated" bundle="HOMEPAGE_RESOURCES"/>
		<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.activated" property="activated" value="true" onchange="this.form.submit()"/><bean:message key="label.homepage.activated.yes" bundle="HOMEPAGE_RESOURCES"/>
		<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.activated" property="activated" value="false" onchange="this.form.submit()"/><bean:message key="label.homepage.activated.no" bundle="HOMEPAGE_RESOURCES"/>
		<html:submit styleId="javascriptButtonID" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
			<bean:message key="button.submit"/>
		</html:submit>
	</p>
	

	<% final String appContext = net.sourceforge.fenixedu._development.PropertiesManager.getProperty("app.context"); %>
	<% final String context = (appContext != null && appContext.length() > 0) ? "/" + appContext : ""; %>

	<bean:define id="homepageURL" type="java.lang.String"><%= request.getScheme() %>://<%= request.getServerName() %>:<%= request.getServerPort() %><%= context %>/homepage/<bean:write name="UserView" property="person.user.userUId"/></bean:define>
	<p>
	<bean:message key="person.homepage.adress" bundle="HOMEPAGE_RESOURCES"/>:
	<logic:notPresent name="UserView" property="person.homepage">
		<bean:write name="homepageURL"/>
	</logic:notPresent>
	<logic:present name="UserView" property="person.homepage">
		<logic:notPresent name="UserView" property="person.homepage.activated">
				<bean:write name="homepageURL"/>
		</logic:notPresent>
		<logic:present name="UserView" property="person.homepage.activated">
			<logic:equal name="UserView" property="person.homepage.activated" value="true">
				<html:link href="<%= homepageURL %>"><bean:write name="homepageURL"/></html:link>
			</logic:equal>
			<logic:equal name="UserView" property="person.homepage.activated" value="false">
				<bean:write name="homepageURL"/>
			</logic:equal>
		</logic:present>
	</logic:present>
	</p>

</html:form>
</logic:present>