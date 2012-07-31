<%@page contentType="text/html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<html:xhtml/>

<em><bean:message key="label.teacherService.credits"/></em>
<h2><bean:message key="label.teacherService.credits.resume"/></h2>

<div class="infoop2">
	<bean:message key="label.teacherService.credits.explanation"/><br/>
	<em><bean:message key="label.teacherService.credits.diferentCategories.explanation"/></em>
</div>
<style>
	.tstyle1 th.total {
		background:#e5e5e5;
	}
</style>

<logic:present name="teacherBean">
	<fr:view name="teacherBean" property="teacher">
		<fr:schema bundle="TEACHER_CREDITS_SHEET_RESOURCES" type="net.sourceforge.fenixedu.domain.Teacher">
			<fr:slot name="person.name" key="label.name"/>
			<fr:slot name="teacherId" key="label.teacher.id"/>
			<fr:slot name="currentWorkingDepartment.name" key="label.department" layout="null-as-label"/>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="columnClasses" value="aleft" />
		</fr:layout>
	</fr:view>

	<logic:notEmpty name="teacherBean" property="pastTeachingCredits">
		<fr:view name="teacherBean" property="pastTeachingCredits">
			<fr:schema bundle="TEACHER_CREDITS_SHEET_RESOURCES" type="net.sourceforge.fenixedu.domain.credits.util.TeacherCreditsBean">
				<fr:slot name="executionPeriod.qualifiedName" key="label.execution-period" >
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
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 printborder" />
				<fr:property name="columnClasses" value="bgcolor3 acenter" />
				<fr:property name="headerClasses" value=",,,,,,,,,,total,total,total,total" />
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
	
	<p><strong><bean:message key="label.credits.legenda" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>:</strong><br/>
	<bean:message key="label.credits.lessons.code" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/> - <bean:message key="label.credits.lessons.code.definition" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>,&nbsp;
	<bean:message key="label.credits.supportLessons.code" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/> - <bean:message key="label.credits.supportLessons.code.explanation" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>,&nbsp;
	<bean:message key="label.credits.masterDegreeLessons.code" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/> - <bean:message key="label.credits.masterDegreeLessons.code.explanation" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>,&nbsp;	
	<bean:message key="label.credits.degreeFinalProjectStudents.code" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/> - <bean:message key="label.credits.degreeFinalProjectStudents.code.definition" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>,&nbsp;
	<bean:message key="label.credits.thesis.code" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/> - <bean:message key="label.credits.thesis.code.definition" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>,&nbsp;
	<bean:message key="label.credits.institutionWorkTime.code" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/> - <bean:message key="label.credits.institutionWorkTime.code.explanation" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>, &nbsp;
	<bean:message key="label.credits.otherTypeCreditLine.code" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/> - <bean:message key="label.credits.otherTypeCreditLine.code.explanation" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>, &nbsp;
	<bean:message key="label.credits.managementPositions.code" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/> - <bean:message key="label.credits.managementPositions.code.definition" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>, &nbsp;
	<bean:message key="label.credits.serviceExemptionSituations.code" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/> - <bean:message key="label.credits.serviceExemptionSituations.code.definition" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>.
	</p>
</logic:present>