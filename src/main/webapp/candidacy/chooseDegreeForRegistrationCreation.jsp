<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<em><bean:message key="label.candidacies" bundle="APPLICATION_RESOURCES"/></em>
<h2><bean:write name="process" property="displayName" /></h2>
<h3><bean:message key="label.candidacy.choose.registation.for.creation" bundle="APPLICATION_RESOURCES"/></h3>

<bean:define id="processId" name="process" property="externalId" />
<bean:define id="processName" name="processName"/>

<bean:define id="individualCandidacyProcessBean" name="individualCandidacyProcessBean" />
 
<fr:form action='<%= "/caseHandling" + processName.toString() + ".do?processId=" + processId.toString() %>'>
 	<html:hidden property="method" value="continueExecuteCreateRegistration" />

 	<fr:edit id="individualCandidacyProcessBean" 
 			name="individualCandidacyProcessBean"
 			type="net.sourceforge.fenixedu.domain.candidacyProcess.secondCycle.SecondCycleIndividualCandidacyProcessBean" 
 			visible="false" />
 			
 	<fr:edit id="individualCandidacyProcessBean-select-degree" name="individualCandidacyProcessBean">
 		<fr:schema 	bundle="ACADEMIC_OFFICE_RESOURCES" 
 					type="net.sourceforge.fenixedu.domain.candidacyProcess.secondCycle.SecondCycleIndividualCandidacyProcessBean" >
 			<fr:slot name="selectedDegree" layout="menu-select" required="true" key="degree">
 				<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.Action.candidacy.secondCycle.SecondCycleIndividualCandidacyProcessDA$SelectedDegreesForRegistrationCreationProvider" />
 				<fr:property name="format" value="${nameI18N}" />
 			</fr:slot>
 		</fr:schema>

		<fr:layout name="tabular-editable">
			<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
	        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
		</fr:layout>

		<fr:destination name="invalid" path="<%= "/caseHandling" + processName.toString() + ".do?method=prepareExecuteCreateRegistrationInvalid&processId=" + processId.toString() %>"/>
		<fr:destination name="cancel" path="<%= "/caseHandling" + processName.toString() + ".do?method=listProcessAllowedActivities&processId=" + processId.toString() %>" />
 	</fr:edit>

	<html:submit><bean:message key="button.continue" bundle="APPLICATION_RESOURCES" /></html:submit>
	<html:cancel><bean:message key="button.cancel" bundle="APPLICATION_RESOURCES" /></html:cancel>	
</fr:form>
