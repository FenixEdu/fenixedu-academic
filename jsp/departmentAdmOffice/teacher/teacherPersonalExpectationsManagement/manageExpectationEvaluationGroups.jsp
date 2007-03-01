<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<em><bean:message key="label.departmentAdmOffice" bundle="DEPARTMENT_ADM_OFFICE_RESOURCES"/></em>
<h2><bean:message key="label.manage.groups.complete" bundle="DEPARTMENT_ADM_OFFICE_RESOURCES"/></h2>

<logic:present role="DEPARTMENT_ADMINISTRATIVE_OFFICE">

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
				<html:link page="/defineExpectationEvaluationGroups.do?method=listGroupsInExecutionYear" paramId="executionYearID" paramName="expectationEvaluationGroupBean" paramProperty="executionYear.idInternal">		
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
			
			<p><b><bean:message key="label.evaluated.groups" bundle="DEPARTMENT_ADM_OFFICE_RESOURCES"/></b></p>
			<table class="tstyle2 thleft thlight">
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
							<bean:write name="evaluatedTeacherGroup" property="evaluated.teacherNumber"/>
						</td>
						<td class="acenter">
							<logic:notEmpty name="evaluatedTeacherGroup" property="evaluated.category">
								<bean:write name="evaluatedTeacherGroup" property="evaluated.category.code"/>
							</logic:notEmpty>
							<logic:empty name="evaluatedTeacherGroup" property="evaluated.category">
								--
							</logic:empty>
						</td>
						<td>
							<html:link page="/defineExpectationEvaluationGroups.do?method=deleteGroup" paramId="groupID" paramName="evaluatedTeacherGroup" paramProperty="idInternal">
								<bean:message key="link.delete" bundle="DEPARTMENT_ADM_OFFICE_RESOURCES"/>
							</html:link>
						</td>			
					</tr>					
				</logic:iterate>
			</table>
			
		</logic:notEmpty>
	
		<p><b><bean:message key="label.create.group" bundle="DEPARTMENT_ADM_OFFICE_RESOURCES"/></b></p>
		<fr:form action="/defineExpectationEvaluationGroups.do?method=createGroup">
			<fr:edit id="expectationEvaluationGroupBeanWithEvaluatedTeacher" name="expectationEvaluationGroupBean" schema="createExpectationEvaluationGroup">			
				<fr:layout name="tabular" />
			</fr:edit>
			<html:submit><bean:message key="button.submit" bundle="DEPARTMENT_ADM_OFFICE_RESOURCES"/></html:submit>
		</fr:form>			
		
	</logic:notEmpty>

</logic:present>