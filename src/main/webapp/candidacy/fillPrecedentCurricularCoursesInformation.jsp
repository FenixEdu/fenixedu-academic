<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml />

<%-- 

This page must be included using <%@ include file="/candidacy/fillPrecedentCurricularCoursesInformation.jsp" %>

-> To use this jsp your action must implement the following methods:

- fillCandidacyInformationInvalid: when validation errors occur

-> Schemas to create: 
[schema must extends IndividualCandidacyProcessBean.precedentDegreeInformation.(INSTITUTION/EXTERNAL)_DEGREE.curricularCourses]

- <process_name>.precedentDegreeInformation.INSTITUTION_DEGREE.curricularCourses
- <process_name>.precedentDegreeInformation.EXTERNAL_DEGREE.curricularCourses

--%>

<bean:define id="parentProcessId" name="parentProcess" property="externalId" />
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
		</fr:edit>
	</logic:equal>
</logic:notEmpty>
