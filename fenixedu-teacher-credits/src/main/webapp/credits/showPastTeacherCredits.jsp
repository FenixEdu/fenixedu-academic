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
<%@page contentType="text/html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app" %>
<html:xhtml/>

<h2><bean:message key="label.teacherService.credits.resume"/></h2>

<logic:present name="teacherBean">
	<jsp:include page="teacherCreditsStyles.jsp"/>
	
	<bean:define id="url" type="java.lang.String">/user/photo/<bean:write name="teacherBean" property="teacher.person.username"/></bean:define>
	<table class="headerTable"><tr>
	<td><img src="<%= request.getContextPath() + url %>"/></td>
	<td ><fr:view name="teacherBean" property="teacher">
		<fr:schema bundle="TEACHER_CREDITS_SHEET_RESOURCES" type="org.fenixedu.academic.domain.Teacher">
			<fr:slot name="person.presentationName" key="label.name"/>
			<fr:slot name="department.name" key="label.department" layout="null-as-label"/>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="creditsStyle"/>
		</fr:layout>
	</fr:view></td>
	</tr></table>

	<logic:notEmpty name="teacherBean" property="pastTeachingCredits">
		<fr:view name="teacherBean" property="pastTeachingCredits">
			<fr:schema bundle="TEACHER_CREDITS_SHEET_RESOURCES" type="org.fenixedu.academic.domain.credits.util.TeacherCreditsBean">
				<fr:slot name="executionPeriod.qualifiedName" key="label.execution-period" layout="link">
					<fr:property name="useParent" value="true"/>
					<fr:property name="hasChecksum" value="false"/>
					<fr:property name="hasContext" value="true"/>
  	    			<fr:property name="linkFormat" value="${teacherCreditsDocument.downloadUrl}"/>
				</fr:slot>
				<fr:slot name="teachingDegreeCredits" key="label.credits.lessons.simpleCode" layout="null-as-label"/>
				<fr:slot name="supportLessonHours" key="label.credits.supportLessons.simpleCode" layout="null-as-label"/>
				<fr:slot name="masterDegreeCredits" key="label.credits.masterDegreeLessons.simpleCode" layout="null-as-label"/>
				<fr:slot name="tfcAdviseCredits" key="label.credits.thesis.code" layout="null-as-label"/>
				<fr:slot name="thesesCredits" key="label.credits.thesis.simpleCode" layout="null-as-label"/>
				<fr:slot name="institutionWorkingHours" key="label.credits.institutionWorkTime.simpleCode" layout="null-as-label"/>
				<fr:slot name="otherCredits" key="label.credits.otherTypeCreditLine.simpleCode" layout="null-as-label"/>
				<fr:slot name="managementCredits" key="label.credits.managementPositions.simpleCode" layout="null-as-label"/>
				<fr:slot name="serviceExemptionCredits" key="label.credits.serviceExemptionSituations.simpleCode" layout="null-as-label"/>
				<fr:slot name="totalCredits" key="label.credits.yearCredits.simpleCode" layout="null-as-label"/>
				<fr:slot name="mandatoryLessonHours" key="label.credits.normalizedAcademicCredits.simpleCode" layout="null-as-label"/>
				<fr:slot name="finalLineCredits" key="label.credits.finalCredits.simpleCode" layout="null-as-label"/>
				<fr:slot name="totalLineCredits" key="label.credits.totalCredits" layout="null-as-label"/>
				<fr:slot name="corrections" key="label.empty"/>
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 printborder" />
				<fr:property name="columnClasses" value="bgcolor3 acenter,bgcolor3 acenter,bgcolor3 acenter,bgcolor3 acenter,bgcolor3 acenter,bgcolor3 acenter,bgcolor3 acenter,bgcolor3 acenter,bgcolor3 acenter,bgcolor3 acenter,bgcolor3 acenter,bgcolor3 acenter,bgcolor3 acenter,bgcolor3 acenter,tdclear tderror1" />
				<fr:property name="headerClasses" value=",,,,,,,,,,total,total,total,total,thclear" />
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
	
	<logic:equal name="teacherBean" property="hasAnyYearWithCorrections" value="true">
		<p><span class="tderror1">(**) </span>Correcções efectuadas no ano lectivo indicado</p>
	</logic:equal>
	
	<p><strong><bean:message key="label.credits.legenda" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>:</strong><br/>
	<bean:message key="label.credits.lessons.code" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/> - <bean:message key="label.credits.lessons.code.definition" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>,&nbsp;
	<bean:message key="label.credits.supportLessons.code" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/> - <bean:message key="label.credits.supportLessons.code.explanation" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>,&nbsp;
	<bean:message key="label.credits.masterDegreeLessons.code" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/> - <bean:message key="label.credits.masterDegreeLessons.code.explanation" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>,&nbsp;	
	<bean:message key="label.credits.degreeFinalProjectStudents.code" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/> - <bean:message key="label.credits.degreeFinalProjectStudents.code.definition" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>,&nbsp;
	<bean:message key="label.credits.thesis.code" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/> - <bean:message key="label.credits.thesis.code.definition" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>,&nbsp;
	<bean:message key="label.credits.institutionWorkTime.code" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/> - <bean:message key="label.credits.institutionWorkTime.code.explanation" arg0="<%=org.fenixedu.academic.domain.organizationalStructure.Unit.getInstitutionAcronym()%>" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>, &nbsp;
	<bean:message key="label.credits.otherTypeCreditLine.code" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/> - <bean:message key="label.credits.otherTypeCreditLine.code.explanation" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>, &nbsp;
	<bean:message key="label.credits.managementPositions.code" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/> - <bean:message key="label.credits.managementPositions.code.definition" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>, &nbsp;
	<bean:message key="label.credits.serviceExemptionSituations.code" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/> - <bean:message key="label.credits.serviceExemptionSituations.code.definition" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>.
	</p>
</logic:present>