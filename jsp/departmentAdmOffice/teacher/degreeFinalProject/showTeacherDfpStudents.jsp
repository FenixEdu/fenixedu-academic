<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>
<bean:define id="hoursPattern">HH:mm</bean:define>

<bean:define id="infoTeacher" name="teacherDegreeFinalProjectStudents" property="infoTeacher" scope="request" />
<bean:define id="infoExecutionPeriod" name="teacherDegreeFinalProjectStudents" property="infoExecutionPeriod" />
<bean:define id="executionPeriodId" name="infoExecutionPeriod" property="idInternal"/>
<p class="infoselected">
	<b><bean:message key="label.teacher.name" /></b> <bean:write name="infoTeacher" property="infoPerson.nome"/><br />
	<bean:define id="teacherNumber" name="infoTeacher" property="teacherNumber"/>
	<b><bean:message key="label.teacher.number" /></b> <bean:write name="teacherNumber"/> <br />
	<b><bean:message key="label.execution-period" /></b> <bean:write name="infoExecutionPeriod" property="description"/> <br />
	
	(<i><html:link page='<%= "/teacherSearchForTeacherCreditsSheet.do?method=doSearch&page=1&amp;executionPeriodId=" + executionPeriodId %>' paramId="teacherNumber" paramName="infoTeacher" paramProperty="teacherNumber">
		<bean:message key="label.departmentTeachersList.teacherCreditsSheet"/>
	</html:link></i>)
</p>

<h3>
		<bean:message key="label.teacher-dfp-student.add-student"/>			
</h3>
<html:form action="/manageTeacherDFPStudent">
	<html:hidden property="method" value="edit"/>
	<html:hidden property="page" value="1"/>	
	<html:hidden property="idInternal"/>
	<html:hidden property="studentId"/>	
	<html:hidden property="teacherId"/>	
	<html:hidden property="executionPeriodId"/>		
	<html:errors />
	<table>
		<tr>
			<td>
				<bean:message key="label.teacher-dfp-student.student-number"/>				
			</td>
			<td>
				<logic:messagesPresent>
					<html:text property="studentNumber" size="6"/>		
				</logic:messagesPresent>
				<logic:messagesNotPresent>
					<html:text property="studentNumber" value="" size="6"/>		
				</logic:messagesNotPresent>		
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.teacher-dfp-student.percentage"/>
			</td>
			
			<td>
				<html:text property="percentage" size="4"/>
				<html:submit styleClass="inputbutton">
					<bean:message key="button.ok"/>
				</html:submit>
			</td>
		</tr>
	</table>	
</html:form>

<bean:define id="teacherDfpStudentsList" name="teacherDegreeFinalProjectStudents" property="infoTeacherDegreeFinalProjectStudentList" scope="request"/>

<bean:size id="listSize" name="teacherDfpStudentsList"/>

<logic:equal name="listSize" value="0">
	<span class="error"><bean:message key="label.teacher-dfp-student.no-students"/></span>
</logic:equal>

<logic:greaterThan name="listSize" value="0">
	<h3>
		<bean:message key="label.teacher-dfp-student.associated-students"/>
	</h3>
	<table>
		<tr>
			<td class="listClasses-header">
				<bean:message key="label.teacher-dfp-student.student-number"/>
			</td>
			<td class="listClasses-header">
				<bean:message key="label.teacher-dfp-student.student-name"/>
			</td>
			<td class="listClasses-header">
				<bean:message key="label.teacher-dfp-student.percentage"/>
			</td>
			<td class="listClasses-header">
				<bean:message key="label.teacher-dfp-student.remove-student"/>
			</td>
			
		</tr>			
			<bean:define id="teacherId" name="infoTeacher" property="idInternal"/>
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
						<html:link page='<%= "/manageTeacherDFPStudent.do?method=delete&amp;page=0&amp;teacherId=" + teacherId.toString()+"&amp;executionPeriodId=" + executionPeriodId %>' paramId="idInternal" paramName="infoTeacherDfpStudent" paramProperty="idInternal">
							<bean:message key="link.remove"/>
						</html:link>
					</td>
				</tr>				
			</logic:iterate>

	</table>
</logic:greaterThan>