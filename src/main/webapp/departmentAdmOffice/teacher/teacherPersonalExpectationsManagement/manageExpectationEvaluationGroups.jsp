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
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<html:xhtml/>

<h2><bean:message key="label.manage.groups.complete" bundle="DEPARTMENT_ADM_OFFICE_RESOURCES"/></h2>

<logic:present role="role(DEPARTMENT_ADMINISTRATIVE_OFFICE)">

	<logic:notEmpty name="expectationEvaluationGroupBean">	
		
		<logic:messagesPresent message="true">
			<p>
				<span class="error0"><!-- Error messages go here -->
					<html:messages id="message" message="true" bundle="DEPARTMENT_ADM_OFFICE_RESOURCES">
						<bean:write name="message"/>
					</html:messages>
				</span>
			<p>
		</logic:messagesPresent>
			
		<p><bean:message key="label.common.executionYear" bundle="DEPARTMENT_ADM_OFFICE_RESOURCES"/>: <bean:write name="expectationEvaluationGroupBean" property="executionYear.year"/></p>
		
		<ul class="list5 mvert15">
			<li>
				<html:link page="/defineExpectationEvaluationGroups.do?method=listGroupsInExecutionYear" paramId="executionYearID" paramName="expectationEvaluationGroupBean" paramProperty="executionYear.externalId">		
					<bean:message bundle="DEPARTMENT_ADM_OFFICE_RESOURCES" key="link.return"/>
				</html:link>
			</li>
		</ul>		
			
		<fr:view name="expectationEvaluationGroupBean" property="appraiser" schema="seeTeacherInformationForTeacherPersonalExpectation">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight thright thbgnone"/>
				<fr:property name="columnClasses" value=",bold"/>
			</fr:layout>
		</fr:view>
	
		<logic:notEmpty name="evaluatedTeacherGroups">	
			
			<p class="mbottom05"><b><bean:message key="label.evaluated.groups" bundle="DEPARTMENT_ADM_OFFICE_RESOURCES"/></b></p>
			<table class="tstyle2 thleft thlight mtop05">
				<tr>
					<th><bean:message key="label.teacher.name" bundle="DEPARTMENT_ADM_OFFICE_RESOURCES"/></th>
					<th><bean:message key="label.teacher.number" bundle="DEPARTMENT_ADM_OFFICE_RESOURCES"/></th>	
					<th><bean:message key="label.teacher.category" bundle="DEPARTMENT_ADM_OFFICE_RESOURCES"/></th>
					<th><bean:message key="label.action" bundle="DEPARTMENT_ADM_OFFICE_RESOURCES"/></th>
				</tr>	
				<logic:iterate id="evaluatedTeacherGroup" name="evaluatedTeacherGroups">
					<tr>
						<td>
							<bean:write name="evaluatedTeacherGroup" property="evaluated.person.name"/>
						</td>
						<td class="acenter">
							<bean:write name="evaluatedTeacherGroup" property="evaluated.teacherId"/>
						</td>
						<td class="acenter">
							<logic:notEmpty name="evaluatedTeacherGroup" property="evaluated.category">
								<bean:write name="evaluatedTeacherGroup" property="evaluated.category.name"/>
							</logic:notEmpty>
							<logic:empty name="evaluatedTeacherGroup" property="evaluated.category">
								--
							</logic:empty>
						</td>
						<td>
							<html:link page="/defineExpectationEvaluationGroups.do?method=deleteGroup" paramId="groupID" paramName="evaluatedTeacherGroup" paramProperty="externalId">
								<bean:message key="link.delete" bundle="DEPARTMENT_ADM_OFFICE_RESOURCES"/>
							</html:link>
						</td>			
					</tr>					
				</logic:iterate>
			</table>
			
		</logic:notEmpty>
	
		<p class="mbottom05"><b><bean:message key="label.create.group" bundle="DEPARTMENT_ADM_OFFICE_RESOURCES"/></b></p>
		<fr:form action="/defineExpectationEvaluationGroups.do?method=createGroup">
			<fr:edit id="expectationEvaluationGroupBeanWithEvaluatedTeacher" name="expectationEvaluationGroupBean" schema="createExpectationEvaluationGroup">			
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle5 thlight thmiddle mtop05"/>
				</fr:layout>
			</fr:edit>
			<html:submit><bean:message key="button.submit" bundle="DEPARTMENT_ADM_OFFICE_RESOURCES"/></html:submit>
		</fr:form>			
		
	</logic:notEmpty>

</logic:present>