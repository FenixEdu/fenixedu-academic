<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<h2><bean:message key="label.candidacy.create" bundle="APPLICATION_RESOURCES"/></h2>

<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES">
	<span class="error0"> <bean:write name="message" /> </span>
	<br />
</html:messages>

<fr:form action="/caseHandlingOver23IndividualCandidacyProcess.do">
 	<html:hidden name="candidacyForm" property="method" value="fillPersonalInformation" />

	<fr:edit id="over23IndividualCandidacyProcessBean" name="over23IndividualCandidacyProcessBean" visible="false" />

	<logic:notEmpty name="over23IndividualCandidacyProcessBean" property="candidacyProcess">
		<logic:empty name="over23IndividualCandidacyProcessBean" property="personBean">
			<h3 class="mtop15 mbottom025"><bean:message key="label.person.title.personal.info" bundle="APPLICATION_RESOURCES"/>:</h3>
			<fr:edit id="Over23IndividualCandidacyProcess.choosePersonBean"
				name="over23IndividualCandidacyProcessBean" property="choosePersonBean"
				schema="Over23IndividualCandidacyProcess.choosePersonBean" nested="true">
				<fr:layout name="tabular-editable">
					<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
			        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
				</fr:layout>
				<fr:destination name="invalid" path="/caseHandlingOver23IndividualCandidacyProcess.do?method=prepareCreateNewProcessInvalid" />
			</fr:edit>
			<html:submit><bean:message key="label.continue" bundle="APPLICATION_RESOURCES" /></html:submit>
		</logic:empty>
	</logic:notEmpty>
	
	<logic:notEmpty name="over23IndividualCandidacyProcessBean" property="candidacyProcess">
		<logic:notEmpty name="over23IndividualCandidacyProcessBean" property="personBean">
			<h3 class="mtop15 mbottom025"><bean:message key="label.person.title.personal.info" bundle="APPLICATION_RESOURCES"/>:</h3>
			<fr:edit id="CandidacyProcess.personalData"
				name="over23IndividualCandidacyProcessBean" property="personBean"
				schema="CandidacyProcess.personalData">
				<fr:layout name="tabular-editable">
					<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
			        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
				</fr:layout>
				<fr:destination name="invalid" path="/caseHandlingOver23IndividualCandidacyProcess.do?method=prepareCreateNewProcessInvalid" />
			</fr:edit>
			<html:submit onclick="this.form.method.value='fillCandidacyInformation';return true;"><bean:message key="label.continue" bundle="APPLICATION_RESOURCES" /></html:submit>
		</logic:notEmpty>
	</logic:notEmpty>
	
	<html:cancel onclick="this.form.method.value='listProcesses';return true;"><bean:message key="label.cancel" bundle="APPLICATION_RESOURCES" /></html:cancel>

</fr:form>
