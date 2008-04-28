<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<h2><bean:message key="label.candidacy.edit" bundle="APPLICATION_RESOURCES"/></h2>

<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES">
	<span class="error0"> <bean:write name="message" /> </span>
	<br />
</html:messages>

<bean:define id="processId" name="process" property="idInternal" />

<fr:form action='<%="/caseHandlingOver23IndividualCandidacyProcess.do?processId=" + processId.toString() %>'>

 	<html:hidden name="candidacyForm" property="method" value="executeIntroduceCandidacyResult" />
	<fr:edit id="over23IndividualCandidacyResultBean" name="over23IndividualCandidacyResultBean" visible="false" />

	<logic:notEmpty name="over23IndividualCandidacyResultBean" property="candidacyProcess">
		
		<h3 class="mtop15 mbottom025"><bean:message key="label.information" bundle="APPLICATION_RESOURCES"/></h3>
		<fr:edit id="over23IndividualCandidacyResultBean.manage"
			name="over23IndividualCandidacyResultBean"
			schema="Over23IndividualCandidacyResultBean.manage">
			<fr:layout name="tabular-editable">
				<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
		        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
			</fr:layout>
			<fr:destination name="invalid" path='<%= "/caseHandlingOver23IndividualCandidacyProcess.do?method=executeIntroduceCandidacyResultInvalid&amp;processId=" + processId.toString() %>' />
		</fr:edit>
		
		<html:submit><bean:message key="label.edit" bundle="APPLICATION_RESOURCES" /></html:submit>
		<html:cancel onclick="this.form.method.value='listProcessAllowedActivities';return true;"><bean:message key="label.back" bundle="APPLICATION_RESOURCES" /></html:cancel>
	</logic:notEmpty>

</fr:form>
