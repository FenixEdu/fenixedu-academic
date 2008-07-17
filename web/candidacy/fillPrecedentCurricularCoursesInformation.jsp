<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />

<%-- 

This page must be included using <%@ include file="/candidacy/fillPrecedentCurricularCoursesInformation.jsp" %>

-> To use this jsp your action must implement the following methods:

- fillCandidacyInformationInvalid: when validation errors occur
- fillPrecedentInformationPostback: when selecting between 'Internal' and 'External' degree information

-> Schemas to create: 
[schema must extends IndividualCandidacyProcessBean.precedentDegreeInformation.(INSTITUTION/EXTERNAL)_DEGREE.curricularCourses]

- <process_name>.precedentDegreeInformation.INSTITUTION_DEGREE.curricularCourses
- <process_name>.precedentDegreeInformation.EXTERNAL_DEGREE.curricularCourses

--%>

<bean:define id="parentProcessId" name="parentProcess" property="idInternal" />
<bean:define id="processName" name="processName" />

<logic:notEmpty name="individualCandidacyProcessBean" property="precedentDegreeType">
	
	<logic:equal name="individualCandidacyProcessBean" property="validPrecedentDegreeInformation" value="true">

		<bean:define id="precedentDegreeTypeName" name="individualCandidacyProcessBean" property="precedentDegreeType.name" />
		<bean:define id="schema"><bean:write name="processName"/>.precedentDegreeInformation.<bean:write name="precedentDegreeTypeName"/></bean:define>

		<h3 class="mtop15 mbottom025"><bean:message key="label.candidacy.precedentCurricularCoursesInformation" bundle="APPLICATION_RESOURCES"/>:</h3>
		<fr:edit id="individualCandidacyProcessBean.precedentCurricularCoursesInformation"
			name="individualCandidacyProcessBean" schema="<%= schema.toString() + ".curricularCourses" %>" 
			nested="true">
			<fr:layout name="tabular-editable">
				<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
   		    	<fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
			</fr:layout>
			<fr:destination name="invalid" path='<%= "/caseHandling" + processName + ".do?method=fillCandidacyInformationInvalid&amp;parentProcessId=" + parentProcessId.toString() %>'  />
			<fr:destination name="precedentDegreeTypePostback" path='<%= "/caseHandling" + processName + ".do?method=fillPrecedentInformationPostback&amp;parentProcessId=" + parentProcessId.toString() %>' />
		</fr:edit>
	</logic:equal>
</logic:notEmpty>
