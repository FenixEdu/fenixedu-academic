<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>
<bean:define id="hoursPattern">HH:mm</bean:define>

<bean:define id="infoTeacher" name="teacherDegreeFinalProjectStudents" property="infoTeacher" scope="request" />

<p class="infoselected">
	<b><bean:message key="label.teacher.name" /></b> <bean:write name="infoTeacher" property="infoPerson.nome"/><br />
	<bean:define id="teacherNumber" name="infoTeacher" property="teacherNumber"/>
	<b><bean:message key="label.teacher.number" /></b> <bean:write name="teacherNumber"/> <br />
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
	<html:hidden property="executionYearId"/>		
	<html:errors />
	<p>
	
		<bean:message key="label.teacher-dfp-student.student-number"/>
		<logic:messagesPresent>
			<html:text property="studentNumber"/>		
		</logic:messagesPresent>
		<logic:messagesNotPresent>
			<html:text property="studentNumber" value=""/>		
		</logic:messagesNotPresent>		

		<html:submit styleClass="inputbutton">
			<bean:message key="button.ok"/>
		</html:submit>
	</p>
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
						<html:link page='<%= "/manageTeacherDFPStudent.do?method=delete&amp;page=0&amp;teacherId=" + teacherId.toString() %>' paramId="idInternal" paramName="infoTeacherDfpStudent" paramProperty="idInternal">
							<bean:message key="link.remove"/>
						</html:link>
					</td>
				</tr>				
			</logic:iterate>

	</table>
</logic:greaterThan>