<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %> 
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<html:xhtml/>

<h2><bean:message key="label.information.export.manage.passwords"/></h2>

<bean:define id="student" name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" property="person.student"/>

<br/>

<h3><bean:message key="label.information.export.password"/>:</h3>

<p>
	<logic:present name="student" property="exportInformationPassword">
		<font color="red" style="bold" size="5"><bean:write name="student" property="exportInformationPassword"/></font>
		<html:link page="/managePasswords.do?method=deletePassword" titleKey="label.information.export.manage.passwords">
			<bean:message key="label.information.export.manage.passwords.delete"/>
		</html:link>
	</logic:present>
	<logic:notPresent name="student" property="exportInformationPassword">
		<span class="info"><bean:message key="label.information.export.manage.passwords.none.present"/></span>
	</logic:notPresent>
</p>

<html:link page="/managePasswords.do?method=generatePassword" titleKey="label.information.export.manage.passwords">
	<bean:message key="label.information.export.manage.passwords.generate"/>
</html:link>
