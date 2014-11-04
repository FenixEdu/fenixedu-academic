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
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<h3><bean:message key="label.credits.projectsAndTutorials" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></h3>

<jsp:include page="../teacherCreditsStyles.jsp"/>
<logic:present name="professorship">
	<bean:define id="url" type="java.lang.String">/user/photo/<bean:write name="professorship" property="teacher.person.username"/></bean:define>
	<table class="headerTable"><tr>
	<td><img src="<%= request.getContextPath() + url %>"/></td>
	<td >
		<fr:view name="professorship">
			<fr:schema bundle="TEACHER_CREDITS_SHEET_RESOURCES" type="org.fenixedu.academic.domain.Professorship">
				<fr:slot name="teacher.person.presentationName" key="label.name"/>
				<fr:slot name="this" key="label.course" layout="format">
					<fr:property name="format" value="${executionCourse.nome}  (${degreeSiglas})" />
				</fr:slot>
				<fr:slot name="executionCourse.executionPeriod" key="label.period" layout="format">
					<fr:property name="format" value="${name}  ${executionYear.year}" />
				</fr:slot>
				<fr:slot name="executionCourse.executionPeriod.executionYear.nextExecutionYear" key="label.credits" layout="format">
					<fr:property name="format" value="${year}" />
				</fr:slot>
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="creditsStyle"/>
			</fr:layout>
		</fr:view>
		</td>
	</tr></table>
	<html:messages id="message" message="true" bundle="TEACHER_CREDITS_SHEET_RESOURCES"><span class="error"><bean:write name="message" filter="false" /></span></html:messages>
	<fr:hasMessages><p><span class="error0"><fr:messages><fr:message/></fr:messages></span></p></fr:hasMessages>
	
	<logic:present name="projectTutorialServiceBeans">
		<bean:define id="professorshipID" name="professorship" property="externalId"/>
		<bean:define id="executionYearOid" name="professorship" property="executionCourse.executionPeriod.executionYear.nextExecutionYear.externalId"/>
		<bean:define id="teacherOid" name="professorship" property="teacher.externalId"/>
			
		<h3 class="separator2 mtop2"><bean:message key="label.availableOrientations" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></h3>
		<fr:edit id="projectTutorialService" name="projectTutorialServiceBeans" action="<%= "/degreeProjectTutorialService.do?method=updateProjectTutorialService&professorshipID="+professorshipID%>">
			<fr:schema type="org.fenixedu.academic.domain.credits.util.ProjectTutorialServiceBean" bundle="TEACHER_CREDITS_SHEET_RESOURCES">
				<fr:slot name="attend.registration.number" key="label.teacher-thesis-student.student-number" readOnly="true"/>
				<fr:slot name="attend.registration.student.person.name" key="label.teacher-thesis-student.student-name" readOnly="true"/>
				<fr:slot name="percentage" key="label.teacher-thesis-student.percentage" validator="pt.ist.fenixWebFramework.renderers.validators.NumberValidator"/>
				<fr:slot name="othersDegreeProjectTutorialServiceString" key="label.teacher-thesis-student.percentage" readOnly="true" layout="html"/>
			</fr:schema>
			<fr:layout name="tabular-editable">
				<fr:property name="classes" value="tstyle2 thlight thleft mtop05 mbottom05"/>
				<fr:property name="columnClasses" value=",,,,tdclear tderror1"/>
				<fr:property name="subSchema" value="edit.degreeProjectTutorialService"></fr:property>
			</fr:layout>
			<fr:destination name="cancel" path="<%="/credits.do?method=viewAnnualTeachingCredits&executionYearOid="+executionYearOid+"&teacherOid="+teacherOid %>"/>
		</fr:edit>
	</logic:present>
</logic:present>