<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<em><bean:message key="label.candidacies" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="label.create" bundle="APPLICATION_RESOURCES"/> <bean:message name="processName" bundle="CASE_HANDLING_RESOURCES"/></h2>

<p class="breadcumbs">
	<span class="actual"><strong><bean:message key="label.step" bundle="APPLICATION_RESOURCES" /> 1</strong>: <bean:message key="label.candidacy.selectPerson" bundle="APPLICATION_RESOURCES" /> </span> &gt;
	<span><strong><bean:message key="label.step" bundle="APPLICATION_RESOURCES" /> 2</strong>: <bean:message key="label.candidacy.personalData" bundle="APPLICATION_RESOURCES" /> </span> &gt;
	<span><strong><bean:message key="label.step" bundle="APPLICATION_RESOURCES" /> 3</strong>: <bean:message key="label.candidacy.candidacyInformation" bundle="APPLICATION_RESOURCES" /> </span>
</p>

<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES">
	<span class="error0"> <bean:write name="message" /> </span>
	<br />
</html:messages>
<fr:hasMessages for="Over23IndividualCandidacyProcess.choosePersonBean.edit" type="conversion">
	<ul class="nobullet list6">
		<fr:messages>
			<li><span class="error0"><fr:message/></span></li>
		</fr:messages>
	</ul>
</fr:hasMessages>

<bean:define id="parentProcessId" name="parentProcess" property="idInternal" />

<fr:form action='<%= "/caseHandlingOver23IndividualCandidacyProcess.do?parentProcessId=" + parentProcessId.toString() %>'>
 	<html:hidden property="method" value="searchPersonForCandidacy" />

	<fr:edit id="over23IndividualCandidacyProcessBean" name="over23IndividualCandidacyProcessBean" visible="false" />

	<logic:notEmpty name="over23IndividualCandidacyProcessBean" property="candidacyProcess">
		
		<logic:equal name="over23IndividualCandidacyProcessBean" property="choosePersonBean.firstTimeSearch" value="true">
			<fr:edit id="Over23IndividualCandidacyProcess.choosePersonBean.edit"
				name="over23IndividualCandidacyProcessBean" property="choosePersonBean"
				schema="Over23IndividualCandidacyProcess.choosePersonBean.edit" nested="true">
				<fr:layout name="tabular-editable">
					<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
			        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
			        <fr:property name="requiredMarkShown" value="true" />
				</fr:layout>
				<fr:destination name="invalid" path='<%= "/caseHandlingOver23IndividualCandidacyProcess.do?method=prepareCreateNewProcessInvalid&amp;parentProcessId=" + parentProcessId.toString() %>' />
			</fr:edit>
			<html:submit><bean:message key="label.continue" bundle="APPLICATION_RESOURCES" /></html:submit>
		</logic:equal>
		
		<logic:equal name="over23IndividualCandidacyProcessBean" property="choosePersonBean.firstTimeSearch" value="false">
			<fr:view name="over23IndividualCandidacyProcessBean" property="choosePersonBean" schema="CandidacyProcess.choosePersonBean.view">
				<fr:layout name="tabular-editable">
					<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
			        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
				</fr:layout>
				<fr:destination name="invalid" path='<%= "/caseHandlingOver23IndividualCandidacyProcess.do?method=prepareCreateNewProcessInvalid&amp;parentProcessId=" + parentProcessId.toString() %>' />
			</fr:view>
			<html:submit onclick="this.form.method.value='searchAgainPersonForCandidacy';return true;"><bean:message key="label.change" bundle="APPLICATION_RESOURCES" /></html:submit>
			
			<br/>
			<br/>
			<bean:message key="label.candidacy.similar.persons" bundle="APPLICATION_RESOURCES" />:
			<fr:edit id="Over23IndividualCandidacyProcess.choosePersonBean.selectPerson"
				name="over23IndividualCandidacyProcessBean" property="choosePersonBean"
				schema="Over23IndividualCandidacyProcess.choosePersonBean.selectPerson" nested="true">
				<fr:layout name="tabular-editable">
					<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
			        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
				</fr:layout>
				<fr:destination name="invalid" path='<%= "/caseHandlingOver23IndividualCandidacyProcess.do?method=prepareCreateNewProcessInvalid&amp;parentProcessId=" + parentProcessId.toString() %>' />
			</fr:edit>
			<bean:message key="label.candidacy.use.similar.person" bundle="APPLICATION_RESOURCES" />:
			<br/>
			<html:submit onclick="this.form.method.value='selectPersonForCandidacy';return true;"><bean:message key="label.choose" bundle="APPLICATION_RESOURCES" /></html:submit>

			<br/>
			<br/>
			<bean:message key="label.candidacy.no.similar.person.to.use" bundle="APPLICATION_RESOURCES" />:
			<br/>
			<html:submit onclick="this.form.method.value='fillPersonalInformation';return true;"><bean:message key="label.create" bundle="APPLICATION_RESOURCES" /></html:submit>
		</logic:equal>
		
	</logic:notEmpty>

	<html:cancel onclick="this.form.method.value='listProcesses';return true;"><bean:message key="label.cancel" bundle="APPLICATION_RESOURCES" /></html:cancel>
	
</fr:form>

<br/>
<em><bean:message key="renderers.validator.required.mark.explanation" bundle="RENDERER_RESOURCES" /></em>

