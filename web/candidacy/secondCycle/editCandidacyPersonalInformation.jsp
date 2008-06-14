<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<em><bean:message key="label.candidacies" bundle="APPLICATION_RESOURCES"/></em>
<h2><bean:message key="label.candidacy.edit" bundle="APPLICATION_RESOURCES"/></h2>
<strong><bean:write name="process" property="displayName" /></strong>
<br/>

<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES">
	<span class="error0"> <bean:write name="message" /> </span>
	<br />
</html:messages>
<fr:hasMessages for="CandidacyProcess.personalDataBean" type="conversion">
	<ul class="nobullet list6">
		<fr:messages>
			<li><span class="error0"><fr:message/></span></li>
		</fr:messages>
	</ul>
</fr:hasMessages>

<bean:define id="processId" name="process" property="idInternal" />

<fr:form action='<%="/caseHandlingSecondCycleIndividualCandidacyProcess.do?processId=" + processId.toString() %>'>

 	<html:hidden property="method" value="executeEditCandidacyPersonalInformation" />
	<fr:edit id="secondCycleIndividualCandidacyProcessBean" name="secondCycleIndividualCandidacyProcessBean" visible="false" />

	<logic:notEmpty name="secondCycleIndividualCandidacyProcessBean" property="personBean">
		
		<h3 class="mtop15 mbottom025"><bean:message key="label.person.title.personal.info" bundle="APPLICATION_RESOURCES"/>:</h3>
		<fr:edit id="candidacyProcess.personalDataBean"
			name="secondCycleIndividualCandidacyProcessBean" property="personBean"
			schema="CandidacyProcess.personalDataBean">
			<fr:layout name="tabular-editable">
				<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
		        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
			</fr:layout>
			<fr:destination name="invalid" path='<%= "/caseHandlingSecondCycleIndividualCandidacyProcess.do?method=executeEditCandidacyPersonalInformationInvalid&amp;processId=" + processId.toString() %>' />
		</fr:edit>
		
		<html:submit><bean:message key="label.edit" bundle="APPLICATION_RESOURCES" /></html:submit>
		<html:cancel onclick="this.form.method.value='listProcessAllowedActivities';return true;"><bean:message key="label.back" bundle="APPLICATION_RESOURCES" /></html:cancel>
	</logic:notEmpty>

</fr:form>
