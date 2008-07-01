<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<em><bean:message key="label.candidacies" bundle="APPLICATION_RESOURCES"/></em>
<h2><bean:write name="process" property="displayName" /></h2>
<h3><bean:message key="label.candidacy.createRegistrations" bundle="APPLICATION_RESOURCES"/></h3>

<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES">
	<span class="error0"> <bean:write name="message" /> </span>
	<br />
</html:messages>

<bean:define id="processId" name="process" property="idInternal" />
<bean:define id="processName" name="processName"/>

<fr:form action='<%= "/caseHandling" + processName.toString() + ".do?processId=" + processId.toString() %>'>
 	<html:hidden property="method" value="executeCreateRegistrations" />
 	
 	<logic:notEmpty name="candidacyDegreeBeans">
 		<fr:view name="candidacyDegreeBeans" schema='<%= processName.toString() + ".candidacyDegreeBean" %>'>
 			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 mtop025"/>
			</fr:layout>
 		</fr:view>
	</logic:notEmpty>
	
	<logic:empty name="candidacyDegreeBeans">
		<strong><em><bean:message key="label.candidacy.no.accepted.candidacies" bundle="APPLICATION_RESOURCES" /></em></strong>
	</logic:empty>
	
 	<br/>
 	
	<logic:notEmpty name="candidacyDegreeBeans">
		<strong><bean:message key="label.candidacy.create.registrations.question" bundle="APPLICATION_RESOURCES" /></strong>
		<br/>
		<br/>
		<html:submit><bean:message key="label.yes.capitalized" bundle="APPLICATION_RESOURCES" /></html:submit>
		<html:cancel onclick="this.form.method.value='listProcessAllowedActivities'; return true;"><bean:message key="label.no.capitalized" bundle="APPLICATION_RESOURCES" /></html:cancel>
	</logic:notEmpty>
	<logic:empty name="candidacyDegreeBeans">
		<br/>
		<html:cancel onclick="this.form.method.value='listProcessAllowedActivities'; return true;"><bean:message key="label.back" bundle="APPLICATION_RESOURCES" /></html:cancel>
	</logic:empty>
</fr:form>
