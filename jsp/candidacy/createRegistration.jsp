<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<em><bean:message key="label.candidacies" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:write name="process" property="displayName" /></h2>
<h3><bean:message key="label.candidacy.createRegistration" bundle="APPLICATION_RESOURCES"/></h3>

<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES">
	<span class="error0"> <bean:write name="message" /> </span>
	<br />
</html:messages>

<bean:define id="processId" name="process" property="idInternal" />
<bean:define id="processName" name="processName"/>

<fr:form action='<%= "/caseHandling" + processName.toString() + ".do?processId=" + processId.toString() %>'>
 	<html:hidden property="method" value="executeCreateRegistration" />
	
	<bean:define id="degreeName" name="degree" property="name" />
	
	<strong><bean:message key="label.candidacy.createRegistration.confirm.message" arg0="<%= degreeName.toString() %>" bundle="APPLICATION_RESOURCES" />?</strong>
	<br/>
	<br/>
	<html:submit><bean:message key="label.yes.capitalized" bundle="APPLICATION_RESOURCES" /></html:submit>
	<html:cancel onclick="this.form.method.value='listProcessAllowedActivities'; return true;"><bean:message key="label.no.capitalized" bundle="APPLICATION_RESOURCES" /></html:cancel>
</fr:form>
