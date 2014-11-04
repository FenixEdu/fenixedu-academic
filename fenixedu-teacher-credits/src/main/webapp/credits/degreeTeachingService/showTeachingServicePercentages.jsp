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
<%@ page isELIgnored="true"%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<jsp:include page="../teacherCreditsStyles.jsp"/>

<bean:define id="hoursPattern">HH : mm</bean:define>
<bean:define id="teacher" name="professorship" property="teacher" scope="request" />
<bean:define id="teacherId" name="teacher" property="externalId" />
<bean:define id="executionCourse" name="professorship" property="executionCourse" scope="request" />
<bean:define id="executionPeriodId" name="executionCourse" property="executionPeriod.externalId" />

<h3><bean:message key="label.teacherCreditsSheet.professorships" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></h3>

<bean:define id="url" type="java.lang.String">/user/photo/<bean:write name="professorship" property="teacher.person.username"/></bean:define>
<table class="headerTable"><tr>
<td><img src="<%= request.getContextPath() + url %>"/></td>
<td >
	<fr:view name="professorship">
		<fr:schema bundle="TEACHER_CREDITS_SHEET_RESOURCES" type="org.fenixedu.academic.domain.Professorship">
			<fr:slot name="teacher.person.presentationName" key="label.name"/>
			<fr:slot name="executionCourse.nome" key="label.course"/>
			<fr:slot name="executionCourse.executionPeriod" key="label.execution-period" layout="format">
				<fr:property name="format" value="${name}  ${executionYear.year}" />
			</fr:slot>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="creditsStyle"/>
		</fr:layout>
	</fr:view>
	</td>
</tr></table>


<bean:define id="teacherId" name="teacher" property="externalId"/>
<bean:define id="executionYearOid" name="executionCourse" property="executionPeriod.executionYear.externalId" />

<p><html:link page="<%="/credits.do?method=viewAnnualTeachingCredits&amp;executionYearOid="+executionYearOid+"&teacherOid="+teacherId%>"><bean:message key="link.return"/></html:link></p>

<html:messages id="message" message="true">
	<span class="error"><!-- Error messages go here -->
		<bean:write name="message" filter="false"/>
	</span>
</html:messages>

<h3 class="separator2 mtop2"><bean:message key="label.teacherCreditsSheet.shiftProfessorships" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></h3>
<p class="infoop2">
<bean:message key="label.teaching.service.help.top" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
</p>
<html:form action="/degreeTeachingServiceManagement">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.teacherId" property="teacherId" value="<%= teacherId.toString() %>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionPeriodId" property="executionPeriodId" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseId" property="executionCourseId"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.professorshipID" property="professorshipID"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="updateTeachingServices"/>

	<table class="tstyle4 mtop1">
		<%-- ********************************* HEADER *********************************************** --%>
		<tr>
			<th rowspan="2" width="10%"><bean:message key="label.shift"/></th>
			<th rowspan="2" width="5%"><bean:message key="label.shift.type"/></th>
			<th colspan="5" width="40%"><bean:message key="label.lessons"/></th>
			<th rowspan="2"><bean:message key="label.weeklyAverage" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
			<th rowspan="2"><bean:message key="label.semesterTotal" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
			<th rowspan="2"><bean:message key="label.professorship.percentage"/></th>
			<th><bean:message key="label.teacher.applied"/></th>			
		</tr>
		<tr>
			<th><bean:message key="label.week" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
			<th><bean:message key="label.day.of.week"/></th>
			<th><bean:message key="label.lesson.start"/></th>
			<th><bean:message key="label.lesson.end"/></th>
			<th><bean:message key="label.lesson.room"/></th>	
			<th><bean:message key="label.teacher"/> - <bean:message key="label.professorship.percentage"/></th>
		</tr> 
		<%-- ********************************* SHIFTS *********************************************** --%>	
		<logic:iterate id="teachingServicePercentage" name="teachingServicePercentages">
			<bean:define id="shift" name="teachingServicePercentage" property="shift"/>
			<bean:define id="availablePercentage" name="teachingServicePercentage" property="availablePercentage"/>
			
			<bean:size id="lessonsSize" name="shift" property="associatedLessons" />	

				<logic:equal name="lessonsSize" value="0">
					<tr>
						<td><bean:write name="shift" property="nome"/></td>
						<td><bean:write name="shift" property="shiftTypesCodePrettyPrint"/></td>
						<td colspan="5"> Não tem aulas </td>
						<td> - </td>
						<td> - </td>
						<td>
							<logic:greaterThan name="availablePercentage" value="0">
									<bean:define id="propertyName">
										teacherPercentageMap(<bean:write name="shift" property="externalId"/>)
									</bean:define>
									<html:text alt='<%= propertyName %>' property='<%= propertyName %>' size="4" /> %
							</logic:greaterThan>
							<logic:equal name="availablePercentage" value="0">
								&nbsp;
							</logic:equal>
						</td>
						<td rowspan="<%= 1 %>">
							<bean:size id="teachingServiceSize" name="shift" property="degreeTeachingServices"/>
							<logic:equal name="teachingServiceSize" value="0">&nbsp;</logic:equal>
							<logic:notEqual name="teachingServiceSize" value="0">
								<logic:iterate id="teachingService"	name="shift" property="degreeTeachingServices" indexId="indexPercentage">						
						    		<bean:write name="teachingService" property="professorship.person.name" />
			 						<bean:define id="teachingServicePercentage" name="teachingService" property="percentage"/>
			 						&nbsp;-&nbsp;<%= ((Math.round(((Double)teachingServicePercentage).doubleValue() * 100.0)) / 100.0) %>
			 						<br />
								</logic:iterate>			
							</logic:notEqual>
							<logic:iterate id="nonRegularTeachingService" name="shift" property="nonRegularTeachingServices">
								<bean:write name="nonRegularTeachingService" property="professorship.person.name" />
								<bean:define id="nonRegularTeachingServicePerscentage" name="nonRegularTeachingService" property="percentage"/>
		 						&nbsp;-&nbsp;<%= ((Math.round(((Double)nonRegularTeachingServicePerscentage).doubleValue() * 100.0)) / 100.0) %>
		 						<br />
							</logic:iterate>
						</td>
					</tr>
				</logic:equal>

				<logic:notEqual name="lessonsSize" value="0">
					<logic:iterate id="lesson" name="shift" property="lessonsOrderedByWeekDayAndStartTime" indexId="indexLessons" >
			            <logic:equal name="indexLessons" value="0">

							<tr>
							<td rowspan="<%= lessonsSize %>"><bean:write name="shift" property="nome"/></td>
							<td rowspan="<%= lessonsSize %>"><bean:write name="shift" property="shiftTypesCodePrettyPrint"/></td>
							<td><fr:view name="lesson" property="occurrenceWeeksAsString"/></td>
							<td>
								<bean:write name="lesson" property="weekDay.labelShort"/>
							</td>
							<td>
								<dt:format patternId="hoursPattern">
									<bean:write name="lesson" property="inicio.time.time"/>
								</dt:format>
							</td>
							<td>
								<dt:format patternId="hoursPattern">
									<bean:write name="lesson" property="fim.time.time"/>
								</dt:format>
							</td>
							<td>
								<logic:notEmpty name="lesson" property="sala">
									<bean:write name="lesson" property="sala.name"/>
								</logic:notEmpty>					
								<logic:empty name="lesson" property="sala">
									-
								</logic:empty>
							</td>
							<td rowspan="<%= lessonsSize %>"><fr:view name="shift" property="courseLoadWeeklyAverage"/></td>
							<td rowspan="<%= lessonsSize %>"><bean:write name="shift" property="courseLoadTotalHours"/></td>
							<td rowspan="<%= lessonsSize %>">
								<logic:greaterThan name="availablePercentage" value="0">
									<bean:define id="propertyName">
										teacherPercentageMap(<bean:write name="shift" property="externalId"/>)
									</bean:define>
									<html:text alt='<%= propertyName %>' property='<%= propertyName %>' size="4" /> %
								</logic:greaterThan>
								<logic:equal name="availablePercentage" value="0">
									&nbsp;
								</logic:equal>
							</td>
							<td rowspan="<%= lessonsSize %>">
								<bean:size id="teachingServiceSize" name="shift" property="degreeTeachingServices"/>
								<logic:equal name="teachingServiceSize" value="0">&nbsp;</logic:equal>
								<logic:notEqual name="teachingServiceSize" value="0">
									<logic:iterate id="teachingService"	name="shift" property="degreeTeachingServices" indexId="indexPercentage">						
							    		<bean:write name="teachingService" property="professorship.person.name" />
				 						<bean:define id="teachingServicePercentage" name="teachingService" property="percentage"/>
				 						&nbsp;-&nbsp;<%= ((Math.round(((Double)teachingServicePercentage).doubleValue() * 100.0)) / 100.0) %>
				 						<br />
									</logic:iterate>			
								</logic:notEqual>
								<logic:iterate id="nonRegularTeachingService" name="shift" property="nonRegularTeachingServices">
									<bean:write name="nonRegularTeachingService" property="professorship.person.name" />
									<bean:define id="nonRegularTeachingServicePerscentage" name="nonRegularTeachingService" property="percentage"/>
			 						&nbsp;-&nbsp;<%= ((Math.round(((Double)nonRegularTeachingServicePerscentage).doubleValue() * 100.0)) / 100.0) %>
			 						<br />
								</logic:iterate>
							</td>						
							</tr>
						</logic:equal>
						
						<logic:greaterThan name="indexLessons" value="0">
							<tr>
								<td><fr:view name="lesson" property="occurrenceWeeksAsString"/></td>
								<td>
									<bean:write name="lesson" property="weekDay.labelShort"/>
								</td>
								<td>
									<dt:format patternId="hoursPattern">
										<bean:write name="lesson" property="inicio.time.time"/>
									</dt:format>
								</td>
								<td>
									<dt:format patternId="hoursPattern">
										<bean:write name="lesson" property="fim.time.time"/>
									</dt:format>
								</td>
								<td>
									<logic:notEmpty name="lesson" property="sala">
										<bean:write name="lesson" property="sala.name"/>
									</logic:notEmpty>	
									<logic:empty name="lesson" property="sala">
										-
									</logic:empty>				
								</td>						
							</tr>
						</logic:greaterThan>
					</logic:iterate>
				</logic:notEqual>	
		</logic:iterate>
	</table>
	
	<p class="mtop05"><bean:message key="label.teaching.service.help.bottom" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></p>
	

	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message key="button.save"/>
	</html:submit>
</html:form>

<h3 class="separator2 mtop2"><bean:message key="label.teacherCreditsSheet.supportLessons" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></h3>

<bean:define id="link" type="java.lang.String">/supportLessonsManagement.do?method=prepareEdit&amp;page=0&amp;professorshipID=<bean:write name="professorship" property="externalId"/></bean:define>
<html:link page="<%= link %>"><bean:message key="link.support-lesson.create"/></html:link>

<bean:define id="supportLessonList" name="professorship" property="supportLessonsOrderedByStartTimeAndWeekDay"/>
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

		<bean:define id="linkDelete" type="java.lang.String">/supportLessonsManagement.do?method=deleteSupportLesson&amp;page=0&amp;professorshipID=<bean:write name="professorship" property="externalId"/></bean:define>
		<logic:iterate id="supportLesson" name="supportLessonList">
			<tr>
				<td>
					<bean:write name="supportLesson" property="weekDayObject.labelShort"/>
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
					<html:link page="<%= link %>" paramId="supportLessonID" paramName="supportLesson" paramProperty="externalId" >
						<bean:message key="link.edit"/>
					</html:link>
				</td>
				<td >
					<html:link page="<%= linkDelete %>" paramId="supportLessonID" paramName="supportLesson" paramProperty="externalId" >
						<bean:message key="link.delete"/>
					</html:link>
				</td>
			</tr>
		</logic:iterate>
	</table>	
</logic:notEmpty>
