<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<%-- 

This page must be included using <%@ include file="/candidacy/fillPrecedentDegreeInformation.jsp" %>

To use this jsp your action must implement the following methods:

- fillCandidacyInformationInvalid: when validation errors occur
- fillCandidacyInformationPostback: when selecting between 'Internal' and 'External' degree information
- fillCandidacyInformationStudentCurricularPlanPostback: having more than one precedent SCP and when choosing one

--%>

<bean:define id="parentProcessId" name="parentProcess" property="idInternal" />
<bean:define id="processName" name="processName" />

<h3 class="mtop15 mbottom025"><bean:message key="label.candidacy.precedentDegreeInformation" bundle="APPLICATION_RESOURCES"/>:</h3>
<logic:notEmpty name="individualCandidacyProcessBean" property="precedentDegreeType">
	
	<logic:equal name="individualCandidacyProcessBean" property="validPrecedentDegreeInformation" value="true">
		<bean:define id="precedentDegreeTypeName" name="individualCandidacyProcessBean" property="precedentDegreeType.name" />
		<bean:define id="schema">IndividualCandidacyProcessBean.precedentDegreeInformation.<bean:write name="precedentDegreeTypeName"/></bean:define>
		<fr:edit id="individualCandidacyProcessBean.precedentDegreeInformation"
			name="individualCandidacyProcessBean" schema="<%= schema.toString() + ".create" %>" 
			nested="true">
			<fr:layout name="tabular-editable">
				<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
   		    	<fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
			</fr:layout>
			<fr:destination name="invalid" path='<%= "/caseHandling" + processName + ".do?method=fillCandidacyInformationInvalid&amp;parentProcessId=" + parentProcessId.toString() %>'  />
			<fr:destination name="precedentDegreeTypePostback" path='<%= "/caseHandling" + processName + ".do?method=fillCandidacyInformationPostback&amp;parentProcessId=" + parentProcessId.toString() %>' />
		</fr:edit>
		<logic:equal name="individualCandidacyProcessBean" property="externalPrecedentDegreeType" value="true">
			<em><bean:message key="label.candidacy.precedentDegree.externalPrecedentDegreeType" bundle="APPLICATION_RESOURCES"/></em> (<html:link action="/externalUnits.do?method=prepareSearch" target="_blank"><bean:message key="label.externalUnits" bundle="ACADEMIC_OFFICE_RESOURCES" /></html:link>)
		</logic:equal>
	</logic:equal>

	<logic:equal name="individualCandidacyProcessBean" property="validPrecedentDegreeInformation" value="false">
		<bean:size id="numberOfSCPs" name="individualCandidacyProcessBean" property="precedentStudentCurricularPlans" />
	
		<logic:equal name="numberOfSCPs" value="0">
			<fr:edit id="individualCandidacyProcessBean.precedentDegreeInformation"
				name="individualCandidacyProcessBean" schema="IndividualCandidacyProcessBean.precedentDegreeInformation.chooseType" 
				nested="true">
				<fr:layout name="tabular-editable">
					<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
   			    	<fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
				</fr:layout>
				<fr:destination name="invalid" path='<%= "/caseHandling" + processName + ".do?method=fillCandidacyInformationInvalid&amp;parentProcessId=" + parentProcessId.toString() %>'  />
				<fr:destination name="precedentDegreeTypePostback" path='<%= "/caseHandling" + processName + ".do?method=fillCandidacyInformationPostback&amp;parentProcessId=" + parentProcessId.toString() %>' />
			</fr:edit>
			<strong><em><bean:message key="label.candidacy.invalid.precedentDegree" bundle="APPLICATION_RESOURCES"/></em></strong>
		</logic:equal>
			
		<logic:greaterThan name="numberOfSCPs" value="1">
			<fr:edit id="individualCandidacyProcessBean.precedentDegreeInformation"
					name="individualCandidacyProcessBean" 
					schema="IndividualCandidacyProcessBean.precedentDegreeInformation.choosePrecedentSCP"
					nested="true">
				<fr:layout name="tabular-editable">
					<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
   			    	<fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
				</fr:layout>
				<fr:destination name="invalid" path='<%= "/caseHandling" + processName + ".do?method=fillCandidacyInformationInvalid&amp;parentProcessId=" + parentProcessId.toString() %>'  />
				<fr:destination name="precedentDegreeTypePostback" path='<%= "/caseHandling" + processName + ".do?method=fillCandidacyInformationPostback&amp;parentProcessId=" + parentProcessId.toString() %>' />
				<fr:destination name="precedentStudentCurricularPlanPostback" path='<%= "/caseHandling" + processName + ".do?method=fillCandidacyInformationStudentCurricularPlanPostback&amp;parentProcessId=" + parentProcessId.toString() %>' />
			</fr:edit>
		</logic:greaterThan>
		
	</logic:equal>
	
</logic:notEmpty>

<logic:empty name="individualCandidacyProcessBean" property="precedentDegreeType">
	<fr:edit id="individualCandidacyProcessBean.precedentDegreeInformation"
		name="individualCandidacyProcessBean" schema="IndividualCandidacyProcessBean.precedentDegreeInformation.chooseType"
		nested="true">
		<fr:layout name="tabular-editable">
			<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
  		    	<fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
		</fr:layout>
		<fr:destination name="invalid" path='<%= "/caseHandling" + processName +".do?method=fillCandidacyInformationInvalid&amp;parentProcessId=" + parentProcessId.toString() %>'  />
		<fr:destination name="precedentDegreeTypePostback" path='<%= "/caseHandling" + processName + ".do?method=fillCandidacyInformationPostback&amp;parentProcessId=" + parentProcessId.toString() %>' />
	</fr:edit>
</logic:empty>
