<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>
<bean:define id="hoursPattern">HH : mm</bean:define>

<bean:define id="infoTeacher" name="professorshipSupportLessons" property="infoProfessorship.infoTeacher" scope="request" />
<bean:define id="infoExecutionCourse" name="professorshipSupportLessons" property="infoProfessorship.infoExecutionCourse" scope="request" />
<bean:define id="infoSupportLessonList" name="professorshipSupportLessons" property="infoSupportLessonList" scope="request" />
<bean:define id="infoProfessorship" name="professorshipSupportLessons" property="infoProfessorship" scope="request" />

<p class="infoselected">
	<b><bean:message key="label.teacher.name" /></b> <bean:write name="infoTeacher" property="infoPerson.nome"/><br />
	<bean:define id="teacherNumber" name="infoTeacher" property="teacherNumber"/>
	<b><bean:message key="label.teacher.number" /></b> <bean:write name="teacherNumber"/> <br />

	<b> <bean:message key="label.execution-course.name" /></b> <bean:write name="infoExecutionCourse" property="nome"/>
</p>
<h3><bean:message key="label.support-lessons.management"/></h3>




<bean:define id="link" type="java.lang.String">
/manageSupportLesson.do?method=prepareEdit&amp;page=0&amp;infoProfessorshipId=<bean:write name="infoProfessorship" property="idInternal"/>
&amp;teacherId=<bean:write name="infoTeacher" property="idInternal"/>&amp;executionCourseId=<bean:write name="infoExecutionCourse" property="idInternal"/>
</bean:define>
<html:link page="<%= link %>">
	<bean:message key="link.support-lesson.create"/>
</html:link>
<br />
<span class="error"><html:errors/></span>

<bean:size id="listSize" name="infoSupportLessonList"/>

<logic:equal name="listSize" value="0">
	<br />
	<span class="error"><bean:message key="message.support-lessons-list.empty"/></span>
</logic:equal>

<logic:greaterThan name="listSize" value="0">
	<table>
		<tr>
			<td class="listClasses-header"><bean:message key="label.support-lesson.weekday"/></td>			
			<td class="listClasses-header"><bean:message key="label.support-lesson.start-time"/></td>						
			<td class="listClasses-header"><bean:message key="label.support-lesson.end-time"/></td>									
			<td class="listClasses-header"><bean:message key="label.support-lesson.place"/></td>												
			<td class="listClasses-header"><bean:message key="label.support-lesson.edit"/></td>												
			<td class="listClasses-header"><bean:message key="label.support-lesson.delete"/></td>																		
		</tr>

		<bean:define id="linkDelete" type="java.lang.String">
			/manageSupportLesson.do?method=delete&amp;page=0&amp;infoProfessorshipId=<bean:write name="infoProfessorship" property="idInternal"/>
			&amp;teacherId=<bean:write name="infoTeacher" property="idInternal"/>&amp;executionCourseId=<bean:write name="infoExecutionCourse" property="idInternal"/>
		</bean:define>
		<logic:iterate id="infoSupportLesson" name="infoSupportLessonList">
			<tr>
				<td class="listClasses">
					<bean:write name="infoSupportLesson" property="weekDay"/>
				</td>			
				<td class="listClasses">
					<dt:format patternId="hoursPattern">
						<bean:write name="infoSupportLesson" property="startTime.time"/>
					</dt:format>
				</td>			
				<td class="listClasses">
					<dt:format patternId="hoursPattern">
						<bean:write name="infoSupportLesson" property="endTime.time"/>
					</dt:format>
				</td>			
				<td class="listClasses">
					<bean:write name="infoSupportLesson" property="place"/>
				</td>			
				<td  class="listClasses">
					<html:link page="<%= link %>" paramId="idInternal" paramName="infoSupportLesson" paramProperty="idInternal" >
						<bean:message key="link.edit"/>
					</html:link>
				</td>
				<td  class="listClasses">
					<html:link page="<%= linkDelete %>" paramId="idInternal" paramName="infoSupportLesson" paramProperty="idInternal" >
						<bean:message key="link.delete"/>
					</html:link>
				</td>
			</tr>
		</logic:iterate>
	</table>	
</logic:greaterThan>
