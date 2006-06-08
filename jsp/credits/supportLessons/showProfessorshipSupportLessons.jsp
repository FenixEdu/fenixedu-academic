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
</p>
<span class="error"><html:errors/></span>
<html:messages id="message" message="true">
	<span class="error">
		<bean:write name="message" filter="false"/>
	</span>
</html:messages>
<h3><bean:message key="label.support-lessons.management"/></h3>

<bean:define id="link" type="java.lang.String">
	/supportLessonsManagement.do?method=prepareEdit&amp;page=0&amp;professorshipID=<bean:write name="professorship" property="idInternal"/>
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
	<br />
</logic:empty>

<logic:notEmpty name="supportLessonList">
	<table class="tstyle4">
		<tr>
			<th><bean:message key="label.support-lesson.weekday"/></th>			
			<th><bean:message key="label.support-lesson.start-time"/></th>						
			<th><bean:message key="label.support-lesson.end-time"/></th>									
			<th><bean:message key="label.support-lesson.place"/></th>												
			<th><bean:message key="label.support-lesson.edit"/></th>												
			<th><bean:message key="label.support-lesson.delete"/></th>																		
		</tr>

		<bean:define id="linkDelete" type="java.lang.String">
			/supportLessonsManagement.do?method=deleteSupportLesson&amp;page=0&amp;professorshipID=<bean:write name="professorship" property="idInternal"/>
		</bean:define>
		<logic:iterate id="supportLesson" name="supportLessonList">
			<tr>
				<td>
					<bean:write name="supportLesson" property="weekDay"/>
				</td>			
				<td>
					<dt:format patternId="hoursPattern">
						<bean:write name="supportLesson" property="startTime.time"/>
					</dt:format>
				</td>			
				<td>
					<dt:format patternId="hoursPattern">
						<bean:write name="supportLesson" property="endTime.time"/>
					</dt:format>
				</td>			
				<td>
					<bean:write name="supportLesson" property="place"/>
				</td>			
				<td >
					<html:link page="<%= link %>" paramId="supportLessonID" paramName="supportLesson" paramProperty="idInternal" >
						<bean:message key="link.edit"/>
					</html:link>
				</td>
				<td >
					<html:link page="<%= linkDelete %>" paramId="supportLessonID" paramName="supportLesson" paramProperty="idInternal" >
						<bean:message key="link.delete"/>
					</html:link>
				</td>
			</tr>
		</logic:iterate>
	</table>	
</logic:notEmpty>
<br/>
<bean:define id="link">
	/showFullTeacherCreditsSheet.do?method=showTeacherCredits&amp;executionPeriodId=<bean:write name="executionPeriod" property="idInternal"/>&amp;teacherId=<bean:write name="teacher" property="idInternal"/>	
</bean:define>
<html:link page='<%= link %>'>
	<bean:message key="link.return"/>
</html:link>
