<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<em><bean:message key="label.candidacies" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="label.candidacy.edit" bundle="APPLICATION_RESOURCES"/></h2>

<br/>
<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES">
	<span class="error0"> <bean:write name="message" /> </span>
	<br />
</html:messages>

<bean:define id="processId" name="process" property="idInternal" />

<fr:form action='<%="/caseHandlingSecondCycleIndividualCandidacyProcess.do?processId=" + processId.toString() %>'>
 	<html:hidden property="method" value="executeEditCandidacyInformation" />

	<fr:edit id="secondCycleIndividualCandidacyProcessBean" name="secondCycleIndividualCandidacyProcessBean" visible="false" />
	
	<fr:edit id="secondCycleIndividualCandidacyProcessBean.candidacyDate" 
		 name="secondCycleIndividualCandidacyProcessBean"
		 schema="SecondCycleIndividualCandidacyProcessBean.candidacyDate">
		<fr:layout name="tabular-editable">
			<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
	        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
		</fr:layout>
		<fr:destination name="invalid" path="<%= "/caseHandlingSecondCycleIndividualCandidacyProcess.do?method=executeEditCandidacyInformationInvalid&amp;processId=" + processId.toString() %>" />
	</fr:edit>

	<h3 class="mtop15 mbottom025"><bean:message key="label.selectDegree" bundle="APPLICATION_RESOURCES"/>:</h3>
	<fr:edit id="secondCycleIndividualCandidacyProcessBean.degree"
		name="secondCycleIndividualCandidacyProcessBean"
		schema="SecondCycleIndividualCandidacyProcessBean.selectDegree.manage">
		<fr:layout name="tabular-editable">
			<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
	        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
		</fr:layout>
		<fr:destination name="invalid" path="<%= "/caseHandlingSecondCycleIndividualCandidacyProcess.do?method=executeEditCandidacyInformationInvalid&amp;processId=" + processId.toString() %>" />
	</fr:edit>
		
	<h3 class="mtop15 mbottom025"><bean:message key="label.candidacy.precedentDegreeInformation" bundle="APPLICATION_RESOURCES"/>:</h3>
	<bean:define id="precedentDegreeTypeName" name="secondCycleIndividualCandidacyProcessBean" property="precedentDegreeType.name" />
	<bean:define id="schema">SecondCycleIndividualCandidacyProcessBean.precedentDegreeInformation.<bean:write name="precedentDegreeTypeName"/></bean:define>
	<fr:edit id="secondCycleIndividualCandidacyProcessBean.precedentDegreeInformation"
		name="secondCycleIndividualCandidacyProcessBean" schema="<%= schema.toString() + ".edit" %>">
		<fr:layout name="tabular-editable">
			<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
  		    	<fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
		</fr:layout>
		<fr:destination name="invalid" path="<%= "/caseHandlingSecondCycleIndividualCandidacyProcess.do?method=executeEditCandidacyInformationInvalid&amp;processId=" + processId.toString() %>" />
	</fr:edit>

	<h3 class="mtop15 mbottom025"><bean:message key="label.candidacy.information" bundle="APPLICATION_RESOURCES"/>:</h3>
	<fr:edit id="secondCycleIndividualCandidacyProcessBean.optionalInformation"
		name="secondCycleIndividualCandidacyProcessBean"
		schema="SecondCycleIndividualCandidacyProcessBean.optionalInformation">
		<fr:layout name="tabular-editable">
			<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
	        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
		</fr:layout>
		<fr:destination name="invalid" path="<%= "/caseHandlingSecondCycleIndividualCandidacyProcess.do?method=executeEditCandidacyInformationInvalid&amp;processId=" + processId.toString() %>" />
	</fr:edit>
		
	<html:submit><bean:message key="label.edit" bundle="APPLICATION_RESOURCES" /></html:submit>
	<html:cancel onclick="this.form.method.value='listProcessAllowedActivities'; return true;"><bean:message key="label.back" bundle="APPLICATION_RESOURCES" /></html:cancel>

</fr:form>
