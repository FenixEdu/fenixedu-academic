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

<jsp:include page="teacherCreditsStyles.jsp"/>

<h3><bean:message key="link.teacherCreditsSheet.view" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></h3>

<logic:present name="teacherBean">
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



	<logic:notEmpty name="teacherBean" property="annualTeachingCredits">
		<fr:view name="teacherBean" property="annualTeachingCredits">
			<fr:schema bundle="TEACHER_CREDITS_SHEET_RESOURCES" type="org.fenixedu.academic.domain.credits.util.TeacherCreditsBean">
				<fr:slot name="executionYear" key="label.executionYear" layout="link">
					<fr:property name="subSchema" value="org.fenixedu.academic.domain.ExecutionYear.view"/>
					<fr:property name="subLayout" value="values"/>
					<fr:property name="contextRelative" value="true" />
					<fr:property name="moduleRelative" value="true" />
					<fr:property name="useParent" value="true" />
					<fr:property name="linkFormat" value="/credits.do?method=viewAnnualTeachingCredits&amp;executionYearOid=${executionYear.externalId}&amp;teacherOid=${teacher.externalId}"/>
				</fr:slot>
				<fr:slot name="teachingCredits" key="label.credits.teachingCredits.simpleCode" layout="null-as-label">
					<fr:property name="subLayout" value="decimal-format"/>
					<fr:property name="headerToolTip" value="label.credits.teachingCredits"/>
				</fr:slot>
				<fr:slot name="masterDegreeThesesCredits" key="label.credits.masterDegreeTheses.simpleCode" layout="null-as-label">
					<fr:property name="subLayout" value="decimal-format"/>
					<fr:property name="headerToolTip" value="label.credits.masterDegreeTheses"/>
				</fr:slot>
				<fr:slot name="phdDegreeThesesCredits" key="label.credits.phdDegreeTheses.simpleCode" layout="null-as-label">
					<fr:property name="subLayout" value="decimal-format"/>
					<fr:property name="headerToolTip" value="label.credits.phdDegreeTheses"/>
				</fr:slot>
				<fr:slot name="projectsTutorialsCredits" key="label.credits.projectsAndTutorials.simpleCode" layout="null-as-label">
					<fr:property name="subLayout" value="decimal-format"/>
					<fr:property name="headerToolTip" value="label.credits.projectsAndTutorials"/>
				</fr:slot>
				<fr:slot name="managementFunctionCredits" key="label.credits.managementPositions.simpleCode" layout="null-as-label">
					<fr:property name="subLayout" value="decimal-format"/>
					<fr:property name="headerToolTip" value="label.credits.managementPositions.code.definition"/>
				</fr:slot>
				<fr:slot name="othersCredits" key="label.credits.otherCredits.simpleCode" layout="null-as-label">
					<fr:property name="subLayout" value="decimal-format"/>
					<fr:property name="headerToolTip" value="label.credits.otherTypeCreditLine.code.explanation"/>
				</fr:slot>
				<logic:equal name="teacherBean" property="canSeeCreditsReduction" value="true">
					<fr:slot name="creditsReduction" key="label.credits.creditsReduction.simpleCode" layout="null-as-label">
						<fr:property name="subLayout" value="decimal-format"/>
						<fr:property name="headerToolTip" value="label.credits.creditsReduction.definition"/>
					</fr:slot>
				</logic:equal>
				<logic:notEqual name="teacherBean" property="canSeeCreditsReduction" value="true">
					<fr:slot name="creditsReduction" key="label.credits.creditsReduction.simpleCode" layout="format">
						<fr:property name="format" value="-"/>
						<fr:property name="headerToolTip" value="label.credits.creditsReduction.definition"/>
					</fr:slot>
				</logic:notEqual>
				<fr:slot name="serviceExemptionCredits" layout="null-as-label" key="label.credits.serviceExemptionSituations.simpleCode" >
					<fr:property name="subLayout" value="decimal-format"/>
					<fr:property name="headerToolTip" value="label.credits.serviceExemptionSituations.code.definition"/>
				</fr:slot>
				<fr:slot name="annualTeachingLoad" layout="null-as-label" key="label.credits.normalizedAcademicCredits.simpleCode">
					<fr:property name="subLayout" value="decimal-format"/>
					<fr:property name="headerToolTip" value="label.credits.normalizedAcademicCredits"/>
				</fr:slot>
				<fr:slot name="yearCredits" layout="null-as-label" key="label.credits.yearCredits.simpleCode">
					<fr:property name="subLayout" value="decimal-format"/>
					<fr:property name="headerToolTip" value="label.credits.yearCredits"/>
				</fr:slot>
				<fr:slot name="finalCredits" layout="null-as-label" key="label.credits.finalCredits.simpleCode">
					<fr:property name="subLayout" value="decimal-format"/>
					<fr:property name="headerToolTip" value="label.credits.finalCredits"/>
				</fr:slot>
				<fr:slot name="accumulatedCredits" layout="null-as-label" key="label.credits.accumulatedCredits.simpleCode">
					<fr:property name="subLayout" value="decimal-format"/>
					<fr:property name="headerToolTip" value="label.credits.accumulatedCredits"/>
				</fr:slot>
				<fr:slot name="corrections" key="label.empty"/>
			</fr:schema>
			
			<fr:layout name="tabular-row">
				<fr:property name="classes" value="tstyle1 printborder credits" />
				<fr:property name="columnClasses" value="bgcolor3 acenter,bgcolor3 acenter,bgcolor3 acenter,bgcolor3 acenter,bgcolor3 acenter,bgcolor3 acenter,bgcolor3 acenter,bgcolor3 acenter,bgcolor3 acenter,bgcolor3 acenter,bgcolor3 acenter,bgcolor3 acenter,bgcolor3 acenter,tdclear tderror1,tdclear tderror1" />
				<fr:property name="headerClasses" value="acenter,acenter,acenter,acenter,acenter,acenter,acenter,acenter,acenter,acenter total,acenter total,acenter total,acenter total,thclear,thclear" />
			</fr:layout>
		</fr:view>
		
		<logic:equal name="teacherBean" property="hasAnyYearWithCreditsLimitation" value="true">
			<p><span class="tderror1">(*) </span><bean:message key="message.hasCreditsLimitation" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></p>
		</logic:equal>
		<logic:equal name="teacherBean" property="hasAnyYearWithCorrections" value="true">
			<p><span class="tderror1">(**) </span><bean:message key="message.creditsCorrections" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></p>
		</logic:equal>
		
		<p><strong><bean:message key="label.credits.legenda" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></strong>
		<br/><strong><bean:message key="label.credits.teachingCredits.simpleCode" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></strong>: <bean:message key="label.credits.teachingCredits" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
		<br/><strong><bean:message key="label.credits.masterDegreeTheses.simpleCode" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></strong>: <bean:message key="label.credits.masterDegreeTheses" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
		<br/><strong><bean:message key="label.credits.phdDegreeTheses.simpleCode" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></strong>: <bean:message key="label.credits.phdDegreeTheses" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
		<br/><strong><bean:message key="label.credits.projectsAndTutorials.simpleCode" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></strong>: <bean:message key="label.credits.projectsAndTutorials" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
		<br/><strong><bean:message key="label.credits.managementPositions.simpleCode" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></strong>: <bean:message key="label.credits.managementPositions.code.definition" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
		<br/><strong><bean:message key="label.credits.otherCredits.simpleCode" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></strong>: <bean:message key="label.credits.otherTypeCreditLine.code.explanation" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
		<br/><strong><bean:message key="label.credits.creditsReduction.simpleCode" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></strong>: <bean:message key="label.credits.creditsReduction" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
		<br/><strong><bean:message key="label.credits.serviceExemptionSituations.simpleCode" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></strong>: <bean:message key="label.credits.serviceExemptionSituations.code.definition" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
		<br/><strong><bean:message key="label.credits.normalizedAcademicCredits.simpleCode" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></strong>: <bean:message key="label.credits.normalizedAcademicCredits" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
		<br/><strong><bean:message key="label.credits.yearCredits.simpleCode" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></strong>: <bean:message key="label.credits.yearCredits" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
		<br/><strong><bean:message key="label.credits.finalCredits.simpleCode" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></strong>: <bean:message key="label.credits.finalCredits" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
		<br/><strong><bean:message key="label.credits.accumulatedCredits.simpleCode" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></strong>: <bean:message key="label.credits.accumulatedCredits" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
		</p>
	</logic:notEmpty>
</logic:present>