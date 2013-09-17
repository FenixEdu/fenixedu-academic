<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml/>

<em><bean:message key="label.candidacies" bundle="APPLICATION_RESOURCES"/></em>
<h2><bean:message key="label.candidacy.cancel" bundle="APPLICATION_RESOURCES"/></h2>

<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES">
	<span class="error0"> <bean:write name="message" /> </span>
	<br />
</html:messages>

<bean:define id="processId" name="process" property="externalId" />
<bean:define id="processName" name="processName"/>

<fr:form action='<%= "/caseHandling" + processName.toString() + ".do?processId=" + processId.toString() %>'>
 	<html:hidden property="method" value="executeCancelCandidacy" />

	<strong><bean:message key="label.candidacy.cancel.confirm.message" bundle="APPLICATION_RESOURCES" />&nbsp;<bean:write name="process" property="displayName" />?</strong>
	<br/>
	<br/>
	<html:submit><bean:message key="label.yes.capitalized" bundle="APPLICATION_RESOURCES" /></html:submit>
	<html:cancel onclick="this.form.method.value='listProcessAllowedActivities'; return true;"><bean:message key="label.no.capitalized" bundle="APPLICATION_RESOURCES" /></html:cancel>
</fr:form>
