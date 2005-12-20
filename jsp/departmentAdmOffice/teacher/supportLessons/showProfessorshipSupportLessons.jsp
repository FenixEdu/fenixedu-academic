<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>
<bean:define id="hoursPattern">HH : mm</bean:define>

<bean:define id="teacher" name="professorship" property="teacher" scope="request" />
<bean:define id="executionCourse" name="professorship" property="executionCourse" scope="request" />
<bean:define id="executionPeriod" name="professorship" property="executionCourse.executionPeriod" scope="request" />
<bean:define id="professorship" name="professorship" scope="request" />

<bean:define id="executionPeriodId" name="executionCourse" property="executionPeriod.idInternal" />

<p class="infoselected">
	<b><bean:message key="label.teacher.name" /></b> <bean:write name="teacher" property="person.nome"/><br />
	<bean:define id="teacherNumber" name="teacher" property="teacherNumber"/>
	<b><bean:message key="label.teacher.number" /></b> <bean:write name="teacherNumber"/> <br />

	<b> <bean:message key="label.execution-course.name" /></b> <bean:write name="executionCourse" property="nome"/> <br />
	<b> <bean:message key="label.execution-period" /></b> <bean:write name="executionPeriod" property="name"/> - <bean:write name="executionPeriod" property="executionYear.year"/><br />
	(<i><html:link page='<%= "/showTeacherCredits.do?method=showTeacherCredits&page=1&amp;executionPeriodId=" + executionPeriodId %>' paramId="teacherId" paramName="teacher" paramProperty="idInternal">
		<bean:message key="label.departmentTeachersList.teacherCreditsSheet"/>
	</html:link></i>)
</p>
<h3><bean:message key="label.support-lessons.management"/></h3>




<bean:define id="link" type="java.lang.String">
/supportLessonsManagement.do?method=prepareEdit&amp;page=0&amp;professorshipID=<bean:write name="professorship" property="idInternal"/>
&amp;teacherId=<bean:write name="teacher" property="idInternal"/>&amp;executionCourseId=<bean:write name="executionCourse" property="idInternal"/>&amp;executionPeriodId=<bean:write name="executionCourse" property="executionPeriod.idInternal"/>
</bean:define>
<html:link page="<%= link %>">
	<bean:message key="link.support-lesson.create"/>
</html:link>
<br />
<br />
<span class="error"><html:errors/></span>

<logic:empty name="supportLessonList">
	<br />
	<span class="error"><bean:message key="message.support-lessons-list.empty"/></span>
</logic:empty>

<logic:notEmpty name="supportLessonList">
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
			/supportLessonsManagement.do?method=deleteSupportLesson&amp;page=0&amp;professorshipID=<bean:write name="professorship" property="idInternal"/>
			&amp;executionPeriodId=<bean:write name="executionCourse" property="executionPeriod.idInternal"/>
		</bean:define>
		<logic:iterate id="supportLesson" name="supportLessonList">
			<tr>
				<td class="listClasses">
					<bean:write name="supportLesson" property="weekDay"/>
				</td>			
				<td class="listClasses">
					<dt:format patternId="hoursPattern">
						<bean:write name="supportLesson" property="startTime.time"/>
					</dt:format>
				</td>			
				<td class="listClasses">
					<dt:format patternId="hoursPattern">
						<bean:write name="supportLesson" property="endTime.time"/>
					</dt:format>
				</td>			
				<td class="listClasses">
					<bean:write name="supportLesson" property="place"/>
				</td>			
				<td  class="listClasses">
					<html:link page="<%= link %>" paramId="supportLessonID" paramName="supportLesson" paramProperty="idInternal" >
						<bean:message key="link.edit"/>
					</html:link>
				</td>
				<td  class="listClasses">
					<html:link page="<%= linkDelete %>" paramId="supportLessonID" paramName="supportLesson" paramProperty="idInternal" >
						<bean:message key="link.delete"/>
					</html:link>
				</td>
			</tr>
		</logic:iterate>
	</table>	
</logic:notEmpty>
