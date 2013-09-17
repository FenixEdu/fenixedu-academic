<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml/>

<em><bean:message key="label.candidacies" bundle="APPLICATION_RESOURCES"/></em>
<h2><bean:write name="process" property="displayName" /></h2>

<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES">
	<span class="error0"> <bean:write name="message" /> </span>
	<br />
</html:messages>
<bean:define id="degreeCurricularPlanID" name="degreeCurricularPlanID"/>
<bean:define id="processId" name="process" property="externalId" />

<fr:form action='<%="/caseHandlingSecondCycleIndividualCandidacyProcess.do?processId=" + processId.toString() + "&amp;degreeCurricularPlanID=" + degreeCurricularPlanID.toString() %>'>
 	<html:hidden property="method" value="executeIntroduceCandidacyResult" />

	<fr:edit id="secondCycleIndividualCandidacyResultBean" name="secondCycleIndividualCandidacyResultBean" visible="false" />

	<logic:notEmpty name="secondCycleIndividualCandidacyResultBean" property="candidacyProcess">
		<h3 class="mtop15 mbottom025">Resultados de Seriação</h3>
		<fr:edit id="secondCycleIndividualCandidacyResultBean.manage"
			name="secondCycleIndividualCandidacyResultBean"
			schema="SecondCycleIndividualCandidacyResultBean.introduce.result.coordinator">
			<fr:layout name="tabular-editable">
				<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
		        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
			</fr:layout>
			<fr:destination name="invalid" path='<%= "/caseHandlingSecondCycleIndividualCandidacyProcess.do?method=executeIntroduceCandidacyResultInvalid&amp;processId=" + processId.toString() + "&amp;degreeCurricularPlanID=" + degreeCurricularPlanID.toString() %>' />
		</fr:edit>
		
		<html:submit><bean:message key="label.submit" bundle="APPLICATION_RESOURCES" /></html:submit>
	</logic:notEmpty>
	<html:cancel onclick="this.form.method.value='listProcessAllowedActivities';return true;"><bean:message key="label.back" bundle="APPLICATION_RESOURCES" /></html:cancel>
	
</fr:form>
