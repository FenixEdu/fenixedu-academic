<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<em><bean:message key="label.candidacies" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="label.candidacy.create" bundle="APPLICATION_RESOURCES"/></h2>

<p class="breadcumbs">
	<span><strong><bean:message key="label.step" bundle="APPLICATION_RESOURCES" /> 1</strong>: <bean:message key="label.candidacy.selectPerson" bundle="APPLICATION_RESOURCES" /> </span> &gt;
	<span><strong><bean:message key="label.step" bundle="APPLICATION_RESOURCES" /> 2</strong>: <bean:message key="label.candidacy.personalData" bundle="APPLICATION_RESOURCES" /> </span> &gt;
	<span class="actual"><strong><bean:message key="label.step" bundle="APPLICATION_RESOURCES" /> 3</strong>: <bean:message key="label.candidacy.candidacyInformation" bundle="APPLICATION_RESOURCES" /> </span>
</p>

<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES">
	<span class="error0"> <bean:write name="message" /> </span>
	<br />
</html:messages>

<bean:define id="parentProcessId" name="parentProcess" property="idInternal" />

<fr:form action='<%= "/caseHandlingOver23IndividualCandidacyProcess.do?parentProcessId=" + parentProcessId.toString() %>'>
 	<html:hidden property="method" value="createNewProcess" />

	<fr:edit id="over23IndividualCandidacyProcessBean" name="over23IndividualCandidacyProcessBean" visible="false" />

	<logic:notEmpty name="over23IndividualCandidacyProcessBean" property="candidacyProcess">
	
		<fr:edit id="over23IndividualCandidacyProcessBean.candidacyDate" 
			 name="over23IndividualCandidacyProcessBean"
			 schema="Over23IndividualCandidacyProcessBean.candidacyDate">
			<fr:layout name="tabular-editable">
				<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
		        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
			</fr:layout>
			<fr:destination name="invalid" path='<%= "/caseHandlingOver23IndividualCandidacyProcess.do?method=fillCandidacyInformationInvalid&amp;parentProcessId=" + parentProcessId.toString() %>' />
		</fr:edit>
	
		<fr:edit id="Over23IndividualCandidacyProcessBean.degrees"
			name="over23IndividualCandidacyProcessBean"
			schema="Over23IndividualCandidacyProcessBean.degrees">
			<fr:layout name="tabular-editable">
				<fr:property name="classes" value="tstyle4 thlight thright"/>
		        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
			</fr:layout>
			<fr:destination name="invalid" path='<%= "/caseHandlingOver23IndividualCandidacyProcess.do?method=fillCandidacyInformationInvalid&amp;parentProcessId=" + parentProcessId.toString() %>' />
		</fr:edit>
		<html:submit onclick="this.form.method.value='addDegreeToCandidacy';return true;"><bean:message key="label.add" bundle="APPLICATION_RESOURCES" /></html:submit>
		
		<logic:empty name="over23IndividualCandidacyProcessBean" property="selectedDegrees">
			<br/>
			<br/>
			<em><bean:message key="label.candidacy.over23.no.associated.degrees" bundle="APPLICATION_RESOURCES"/>.<span class="highlight1"><bean:message key="label.candidacy.over23.must.select.at.least.one.degree" bundle="APPLICATION_RESOURCES"/></span>.</em>
			<br/>
		</logic:empty>
		<logic:notEmpty name="over23IndividualCandidacyProcessBean" property="selectedDegrees">
			<br/>
			<br/>
			<bean:message key="label.candidacy.choosen.degrees" bundle="APPLICATION_RESOURCES"/>:
			<table class="tstyle1 mtop025">
			<logic:iterate id="degree" name="over23IndividualCandidacyProcessBean" property="selectedDegrees">
				<tr>
					<td><bean:write name="degree" property="name" /> - <bean:write name="degree" property="sigla" /></td>
					<td>
						<bean:define id="degreeId" name="degree" property="idInternal" />
						<html:hidden property="degreeToDelete" value="<%= degreeId.toString() %>" />
						<html:submit onclick="this.form.method.value='removeDegreeFromCandidacy';return true;"><bean:message key="label.remove" bundle="APPLICATION_RESOURCES" /></html:submit>
					</td>
				</tr>
			</logic:iterate>
			</table>
			<br/>
		</logic:notEmpty>
		
		<fr:edit id="Over23IndividualCandidacyProcessBean.optionalInformation"
			name="over23IndividualCandidacyProcessBean"
			schema="Over23IndividualCandidacyProcessBean.optionalInformation">
			<fr:layout name="tabular-editable">
				<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
		        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
			</fr:layout>
			<fr:destination name="invalid" path='<%= "/caseHandlingOver23IndividualCandidacyProcess.do?method=fillCandidacyInformationInvalid&amp;parentProcessId=" + parentProcessId.toString() %>' />
		</fr:edit>
		<br/>
	</logic:notEmpty>
	
	<html:submit><bean:message key="label.create" bundle="APPLICATION_RESOURCES" /></html:submit>
	<html:cancel onclick="this.form.method.value='listProcesses'; return true;"><bean:message key="label.cancel" bundle="APPLICATION_RESOURCES" /></html:cancel>

</fr:form>
