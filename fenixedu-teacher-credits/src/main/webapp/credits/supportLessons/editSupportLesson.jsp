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
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<jsp:include page="../teacherCreditsStyles.jsp"/>

<bean:define id="teacher" name="professorship" property="teacher" scope="request" />
<bean:define id="executionPeriodId" name="professorship" property="executionCourse.executionPeriod.externalId"/>

<h3><bean:message key="label.teacherCreditsSheet.supportLessons" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></h3>

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


<logic:messagesPresent>
	<span class="error"><!-- Error messages go here --><html:errors /></span>
</logic:messagesPresent>

<bean:define id="teacherId" name="teacher" property="externalId"/>
<bean:define id="executionYearOid" name="professorship" property="executionCourse.executionPeriod.executionYear.externalId" />

<p><html:link page="<%="/credits.do?method=viewAnnualTeachingCredits&amp;executionYearOid="+executionYearOid+"&teacherOid="+teacherId%>"><bean:message key="link.return"/></html:link></p>

<p>
	<strong>
	<logic:present name="supportLesson">
		<bean:message key="label.support-lesson.edit"/>
	</logic:present>
	<logic:notPresent name="supportLesson">
		<bean:message key="label.support-lesson.create"/>			
	</logic:notPresent>
	</strong>
</p>

<html:form action="/supportLessonsManagement">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="editSupportLesson"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.externalId" property="externalId"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.supportLessonID" property="supportLessonID"/>	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.professorshipID" property="professorshipID"/>	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionPeriodId" property="executionPeriodId"/>		

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.teacherId" property="teacherId"/>	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseId" property="executionCourseId"/>		

	<table class="mbottom2">
		<tr>
			<td>
				<bean:message key="label.support-lesson.weekday"/>:
			</td>
			<td>
				<e:labelValues id="values" enumeration="org.fenixedu.academic.util.WeekDay" bundle="ENUMERATION_RESOURCES" excludedFields="SUNDAY"/>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.weekDay" property="weekDay">
						<html:options collection="values" property="value" labelProperty="label"/>
				</html:select>
			</td>
		</tr>

		<tr>
			<td>
				<bean:message key="label.support-lesson.start-time"/>:
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.startTimeHour" property="startTimeHour" maxlength="2" size="2" /> : <html:text bundle="HTMLALT_RESOURCES" altKey="text.startTimeMinutes" property="startTimeMinutes" maxlength="2" size="1"/>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.support-lesson.end-time"/>:
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.endTimeHour" property="endTimeHour" maxlength="2" size="2"/> : <html:text bundle="HTMLALT_RESOURCES" altKey="text.endTimeMinutes" property="endTimeMinutes" maxlength="2" size="1"/>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.support-lesson.place"/>:
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.place" property="place"/>
			</td>
		</tr>
	</table>
	<p>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
			<bean:message key="button.submit"/>
		</html:submit>
	</p>
</html:form>