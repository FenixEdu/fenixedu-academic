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
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml/>

<logic:notEmpty name="period">
	<h2><bean:message key="label.editTeacherExpectationAutoEvaluationPeriodManagement.title"/></h2>
</logic:notEmpty>
<logic:empty name="period">	
	<h2><bean:message key="label.createTeacherExpectationAutoEvaluationPeriodManagement.title"/></h2>
</logic:empty>

<logic:present role="role(DEPARTMENT_ADMINISTRATIVE_OFFICE)">

	<logic:messagesPresent message="true">
		<p>
			<span class="error0"><!-- Error messages go here -->
				<html:messages id="message" message="true" bundle="DEPARTMENT_ADM_OFFICE_RESOURCES">
					<bean:write name="message"/>
				</html:messages>
			</span>
		<p>
	</logic:messagesPresent>

	<logic:notEmpty name="period">		
		<p class="mtop15 mbottom05"><bean:message key="label.common.executionYear"/>: <bean:write name="period" property="executionYear.year"/></p>							
		<bean:define id="executionYearId" name="period" property="executionYear.externalId"/>
		<fr:edit id="editInterval" name="period" schema="editTeacherPersonalExpectationsAutoAvaliationPeriod" layout="tabular">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thright mtop05"/>
				<fr:property name="columnClasses" value=",,tdclear tderror1"/>
			</fr:layout>
			<fr:destination name="success" path="<%= "/autoEvaluationTeacherExpectationManagementAction.do?method=showPeriodInExecutionYear&executionYearId=" + executionYearId %>"/>
			<fr:destination name="cancel" path="/autoEvaluationTeacherExpectationManagementAction.do?method=showPeriod"/>
		</fr:edit>		
	</logic:notEmpty>	
		
	<logic:empty name="period">		
		<p class="mtop15 mbottom05"><bean:message key="label.common.executionYear"/>: <bean:write name="executionYear" property="year"/></p>		
		<bean:define id="executionYearId" name="executionYear" property="externalId"/>
		<fr:create id="createInterval" type="org.fenixedu.academic.domain.TeacherAutoEvaluationDefinitionPeriod" schema="createTeacherPersonalExpectationsAutoAvaliationPeriod">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thright mtop05"/>
				<fr:property name="columnClasses" value=",,tdclear tderror1"/>
			</fr:layout>
		   	<fr:hidden slot="executionYear" name="executionYear"/>
		   	<fr:hidden slot="department" name="department"/>
			<fr:destination name="success" path="<%= "/autoEvaluationTeacherExpectationManagementAction.do?method=showPeriodInExecutionYear&executionYearId=" + executionYearId %>"/>
			<fr:destination name="cancel" path="/autoEvaluationTeacherExpectationManagementAction.do?method=showPeriod"/>
		</fr:create>		
	</logic:empty>
	
</logic:present>
