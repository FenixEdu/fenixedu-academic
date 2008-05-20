<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<em><bean:message key="label.candidacies" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="label.candidacy.secondCycle" bundle="APPLICATION_RESOURCES"/></h2>


<strong><bean:write name="process" property="displayName" /></strong>
<br/>

<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES">
	<span class="error0"> <bean:write name="message" /> </span>
	<br />
</html:messages>

<bean:define id="processId" name="process" property="idInternal" />

<fr:form action='<%="/caseHandlingSecondCycleCandidacyProcess.do?processId=" + processId.toString() %>'>
 	<html:hidden property="method" value="executeIntroduceCandidacyResults" />

	<h3 class="mtop15 mbottom025"><bean:message key="label.candidacy.introduce.result" bundle="APPLICATION_RESOURCES"/></h3>
	<fr:edit id="secondCycleIndividualCandidacyResultBeans"
		name="secondCycleIndividualCandidacyResultBeans"
		schema="SecondCycleIndividualCandidacyResultBean.introduce.results">
		<fr:layout name="tabular-editable">
			<fr:property name="classes" value="tstyle4 mtop025"/>
		</fr:layout>
		<fr:destination name="invalid" path='<%= "/caseHandlingSecondCycleCandidacyProcess.do?method=executeIntroduceCandidacyResultsInvalid&amp;processId=" + processId.toString() %>' />
	</fr:edit>
		
	<html:submit><bean:message key="label.submit" bundle="APPLICATION_RESOURCES" /></html:submit>
	<html:cancel onclick="this.form.method.value='listProcessAllowedActivities';return true;"><bean:message key="label.back" bundle="APPLICATION_RESOURCES" /></html:cancel>

</fr:form>
