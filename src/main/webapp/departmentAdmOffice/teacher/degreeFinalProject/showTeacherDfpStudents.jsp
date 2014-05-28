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
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt"%>
<bean:define id="hoursPattern">HH:mm</bean:define>

<bean:define id="infoTeacher" name="teacherDegreeFinalProjectStudents" property="infoTeacher" scope="request" />
<bean:define id="infoExecutionPeriod" name="teacherDegreeFinalProjectStudents" property="infoExecutionPeriod" />
<bean:define id="executionPeriodId" name="infoExecutionPeriod" property="externalId"/>
<p class="infoselected">
	<b><bean:message key="label.teacher.name" /></b> <bean:write name="infoTeacher" property="infoPerson.nome"/><br />
	<bean:define id="teacherId" name="infoTeacher" property="teacherId"/>
	<b><bean:message key="label.teacher.number" /></b> <bean:write name="teacherId"/> <br />
	<b><bean:message key="label.execution-period" /></b> <bean:write name="infoExecutionPeriod" property="description"/> <br />
	
	(<i><html:link page='<%= "/teacherSearchForTeacherCreditsSheet.do?method=doSearch&page=1&amp;executionPeriodId=" + executionPeriodId %>' paramId="teacherId" paramName="infoTeacher" paramProperty="teacherId">
		<bean:message key="label.departmentTeachersList.teacherCreditsSheet"/>
	</html:link></i>)
</p>

<h3>
		<bean:message key="label.teacher-dfp-student.add-student"/>			
</h3>
<html:form action="/manageTeacherDFPStudent">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="edit"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.externalId" property="externalId"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentId" property="studentId"/>	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.teacherId" property="teacherId"/>	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionPeriodId" property="executionPeriodId"/>
	
	<span class="error"><!-- Error messages go here --><html:errors /></span>
	
	<table>
		<tr>
			<td>
				<bean:message key="label.teacher-dfp-student.student-number"/>				
			</td>
			<td>
				<logic:messagesPresent>
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.studentNumber" property="studentNumber" size="6"/>		
				</logic:messagesPresent>
				<logic:messagesNotPresent>
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.studentNumber" property="studentNumber" value="" size="6"/>		
				</logic:messagesNotPresent>		
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.teacher-dfp-student.percentage"/>
			</td>
			
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.percentage" property="percentage" size="4"/>
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
					<bean:message key="button.ok"/>
				</html:submit>
			</td>
		</tr>
	</table>	
</html:form>

<bean:define id="teacherDfpStudentsList" name="teacherDegreeFinalProjectStudents" property="infoTeacherDegreeFinalProjectStudentList" scope="request"/>

<bean:size id="listSize" name="teacherDfpStudentsList"/>

<logic:equal name="listSize" value="0">
	<span class="error"><!-- Error messages go here --><bean:message key="label.teacher-dfp-student.no-students"/></span>
</logic:equal>

<logic:greaterThan name="listSize" value="0">
	<h3>
		<bean:message key="label.teacher-dfp-student.associated-students"/>
	</h3>
	<table>
		<tr>
			<th class="listClasses-header">
				<bean:message key="label.teacher-dfp-student.student-number"/>
			</th>
			<th class="listClasses-header">
				<bean:message key="label.teacher-dfp-student.student-name"/>
			</th>
			<th class="listClasses-header">
				<bean:message key="label.teacher-dfp-student.percentage"/>
			</th>
			<th class="listClasses-header">
				<bean:message key="label.teacher-dfp-student.remove-student"/>
			</th>
			
		</tr>			
			<bean:define id="teacherId" name="infoTeacher" property="externalId"/>
			<logic:iterate id="infoTeacherDfpStudent" name="teacherDfpStudentsList">
				<tr>
					<td class="listClasses">
						<bean:write name="infoTeacherDfpStudent" property="infoStudent.number"/>
					</td>
					<td class="listClasses">
						<bean:write name="infoTeacherDfpStudent" property="infoStudent.infoPerson.nome"/>
					</td>
					<td class="listClasses">
						<bean:write name="infoTeacherDfpStudent" property="percentage"/> %
					</td>
					
					<td class="listClasses">
						<html:link page='<%= "/manageTeacherDFPStudent.do?method=delete&amp;page=0&amp;teacherId=" + teacherId.toString()+"&amp;executionPeriodId=" + executionPeriodId %>' paramId="externalId" paramName="infoTeacherDfpStudent" paramProperty="externalId">
							<bean:message key="link.remove"/>
						</html:link>
					</td>
				</tr>				
			</logic:iterate>
	</table>
</logic:greaterThan>