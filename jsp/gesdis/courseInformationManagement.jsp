<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<h2><bean:message key="title.courseInformation"/></h2>
<logic:present name="siteView"> 
<html:form action="/courseInformation">
<bean:define id="siteCourseInformation" name="siteView" property="component"/>
<bean:define id="executionCourse" name="siteCourseInformation" property="infoExecutionCourse"/>
<bean:define id="executionPeriod" name="executionCourse" property="infoExecutionPeriod"/>
<bean:define id="executionYear" name="executionPeriod" property="infoExecutionYear"/>
<br/>
<table>
<tr>
	<td><bean:message key="message.courseInformation.courseName" /> 
		&nbsp;<bean:write name="executionCourse" property="nome" /></td>
	<td></td>
	<td></td>
	<td><bean:message key="message.courseInformation.executionYear" />
		&nbsp;<bean:write name="executionYear" property="year" /></td>
</tr>
<tr>
	<td>
	<logic:iterate id="curricularCourse" name="executionCourse" property="associatedInfoCurricularCourses">
	<logic:iterate id="curricularCourseScope" name="curricularCourse" property="infoCurricularCourseScope">
		<bean:message key="message.courseInformation.curricularYear" />
		&nbsp;<bean:write name="curricularCourseScope" property="infoCurricularSemester.infoCurricularYear.year" />,
		<bean:message key="message.courseInformation.semester" />
		&nbsp;<bean:write name="curricularCourseScope" property="infoCurricularSemester.semester" />
	</logic:iterate>
	</logic:iterate>
	</td>
	<td></td>
	<!-- VER!!!!!!!!!!!!-->
	<td><bean:message key="message.courseInformation.courseType" />
		&nbsp;<bean:write name="courseInformation" property="" /></td>
	<td><bean:message key="message.courseInformation.courseSemesterOrAnual" />
		&nbsp;<bean:write name="courseInformation" property="" /></td>
</tr>
<tr>
	<td>
		<logic:iterate id="infoTeacher" name="siteCourseInformation" property="infoResponsibleTeachers">
		<bean:message key="message.courseInformation.responsibleForTheCourse" />
		&nbsp;<bean:write name="infoTeacher" property="infoPerson.nome" />,
		<bean:message key="message.courseInformation.categoryOfTheResponsibleForCourse" />
		&nbsp; <bean:write name="infoTeacher" property="infoCategory.name" />
		</logic:iterate>
	</td>
	<td></td>
	<td></td>
	<td></td>
</tr>
</table>
<br />
<table width="100%" cellpadding="0" cellspacing="0">
	<tr>
		<td class="infoop" width="50px"><span class="emphasis-box">1</span></td>
		<td class="infoop"><bean:message key="message.courseInformation.timeTable" />
		</td>
	</tr>
</table>
<br />
<table width="100%" border=1>
	  <td width="200px"><bean:message key="message.courseInformation.classType"/>
	</td>
	    <td><bean:message key="message.courseInformation.numberOfClasses"/>
	</td>
	    <td><bean:message key="message.courseInformation.classDuration"/>
	</td>
	<td><bean:message key="message.courseInformation.totalDuration"/></td>
</tr>
<logic:iterate id="infoLesson" name="siteCourseInformation" property="infoLessons">
<tr>
	<td>
	  <bean:message key="message.courseInformation.typeClassTeoricas"/>
	</td>
	<td>
	  <bean:write name="executionCourse" property="theoreticalHours"/>
	</td>
	<td>
	  <bean:write name="infoLesson" property="tipo.inicio"/>
    </td>
	<td>
	  <bean:write name="typeTeoricas" property="totalDurationTeoricas"/>
    </td>
</tr>
<tr>
	<td>
	  <bean:message key="message.courseInformation.typeClassPraticas"/>
	</td>
	<td>
	  <bean:write name="executionCourse" property="praticalHours"/>
	</td>
	<td>
	  <bean:write name="infoLesson" property="classDurationTeoricas"/>
    </td>
	<td>
	  <bean:write name="typePraticas" property="totalDurationPraticas"/>
    </td>
</tr>
<tr>
	<td>
	  <bean:message key="message.courseInformation.typeClassTeoPrat"/>
	</td>
	<td>
	  <bean:write name="executionCourse" property="theoPratHours"/>
	</td>
	<td>
	  <bean:write name="infoLesson" property="classDurationTeoPrat"/>
    </td>
	<td>
	  <bean:write name="typeTeoPrat" property="totalDurationTeoPrat"/>
    </td>
</tr>
<tr>
	<td>
	  <bean:message key="message.courseInformation.typeClassLab"/>
	</td>
	<td>
	  <bean:write name="executionCourse" property="labHours"/>
	</td>
	<td>
	  <bean:write name="infoLesson" property="classDurationLab"/>
    </td>
	<td>
	  <bean:write name="typeLab" property="totalDurationLab"/>
    </td>
</tr>
</logic:iterate>
</table>
<br />
<table width="100%" cellpadding="0" cellspacing="0">
<tr>
	<td class="infoop" width="50px"><span class="emphasis-box">2</span></td>
	<td class="infoop">
	  <bean:message key="message.courseInformation.LecturingTeachers" />
	</td>
</tr>
</table>
<table>
<tr>
	<td> <bean:message key="message.courseInformation.numberOfStudents"/></td>
	<td> <bean:write name="courseInformation" property="numberOfStudents"/></td>
</tr>
</table>
<table border=1>
<tr>
	<td> <bean:message key="message.courseInformation.nameOfTeacher"/></td>
	<td> <bean:message key="message.courseInformation.categoryOfTeacher"/></td>
	<td> <bean:message key="message.courseInformation.typeOfClassOfTeacher"/></td>
</tr>
<tr>
	<logic:iterate id="infoTeacher" name="siteCourseInformation" property="infoLecturingTeachers">
		<td> <bean:write name="infoTeacher" property="infoPerson.nome"/></td>
		<td> <bean:write name="infoTeacher" property="infoCategory.name"/></td>
		<!--VER O TIPO DE AULA QUE CADA PROF DA-->
		<td> <bean:write name="infoTeacher" property="typeOfClassOfTeacher"/></td>
	</logic:iterate>
</tr>
</table>
<br />
<table width="100%" cellpadding="0" cellspacing="0">
<tr>
	<td class="infoop" width="50px"><span class="emphasis-box">3</span></td>
	<td class="infoop">
	  <bean:message key="message.courseInformation.CourseResults" />
	</td>
</tr>
</table>
<table border=1>
<tr>
	<td></td>
	<td><bean:message key="message.courseInformation.numberOfStudents" /></td>
</tr>
<tr>
	<td><bean:message key="message.courseInformation.enrolledStudents" /></td>
	<td><bean:write name="executionCourse" property="numberOfAttendingStudents"/></td>
</tr>
<!-- VER-->
<tr>
	<td><bean:message key="message.courseInformation.evaluatedStudents" /></td>
	<td><bean:write name="courseInformation" property="evaluatedStudents"/></td>
</tr>
<tr>
	<td><bean:message key="message.courseInformation.approvedStudents" /></td>
	<td><bean:write name="courseInformation" property="approvedStudents"/></td>
</tr>
<tr>
	<td><bean:message key="message.courseInformation.evaluatedPerEnrolled" /></td>
	<!-- VER ONDE SE FAZEM AS CONTAS-->
	<td><bean:write name="courseInformation" property="evaluatedPerEnrolled"/></td>
</tr>
<tr>
	<td><bean:message key="message.courseInformation.approvedPerEvaluated" /></td>
	<td><bean:write name="courseInformation" property="approvedPerEvaluated"/></td>
</tr>
<tr>
	<td><bean:message key="message.courseInformation.approvedPerEnrolled" /></td>
	<td><bean:write name="courseInformation" property="approvedPerEnrolled"/></td>
</tr>
</table>
<br />
<table width="100%" cellpadding="0" cellspacing="0">
<tr>
	<td class="infoop" width="50px"><span class="emphasis-box">4</span></td>
	<td class="infoop">
	  <bean:message key="message.courseInformation.courseObjectives" />
	</td>
</tr>
</table>
<table>
<logic:iterate id="infoCurriculum" name="siteCourseInformation" property="infoCurriculums">
<tr>
	<td> <bean:write name="infoCurriculum" property="generalObjectives"/></td>
</tr>
<tr>
	<td> <bean:write name="infoCurriculum" property="operacionalObjectives"/></td>
</tr>
<logic:iterate>
</table>
<br />
<table width="100%" cellpadding="0" cellspacing="0">
<tr>
	<td class="infoop" width="50px"><span class="emphasis-box">5</span></td>
	<td class="infoop">
	  <bean:message key="message.courseInformation.courseProgram" />
	</td>
</tr>
</table>
<table>
<logic:iterate id="infoCurriculum" name="siteCourseInformation" property="infoCurriculums">
<tr>
	<td> <bean:write name="infoCurriculum" property="program"/></td>
</tr>
</logic:iterate>
</table>
<br />
<table width="100%" cellpadding="0" cellspacing="0">
<tr>
	<td class="infoop" width="50px"><span class="emphasis-box">6</span></td>
	<td class="infoop">
	  <bean:message key="message.courseInformation.courseBibliographicReference" />
	</td>
</tr>
</table>
<table>
<tr>
	<td><bean:message key="message.courseInformation.coursePrincipalBibliographicReference" /></td>
	<td></td>
	<td></td>
	<td></td>
</tr>
	<logic:iterate id="infoBibliographicReference" name="siteCourseInformation" property="infoBibliographicReferences">
	<tr>
		<td> <bean:write name="infoBibliographicReference" property="title"/></td>
		<td> <bean:write name="infoBibliographicReference" property="authors"/></td>
		<td> <bean:write name="infoBibliographicReference" property="reference"/></td>
		<td> <bean:write name="infoBibliographicReference" property="year"/></td>
	</tr>
	</logic:iterate>
</table>
<table>
<tr>
	<td><bean:message key="message.courseInformation.courseSecondaryBibliographicReference" /></td>
</tr>
	<!-- VER -->
	<logic:iterate id="courseInformation" name="component" property="infoCourseInformation" type="DataBeans.InfoCourseInformation">
	<tr>
		<td> <bean:write name="courseInformation" property="secondaryBibliographicReference"/></td>
	</tr>
	</logic:iterate>
</table>
<br />
<table width="100%" cellpadding="0" cellspacing="0">
<tr>
	<td class="infoop" width="50px"><span class="emphasis-box">7</span></td>
	<td class="infoop">
	  <bean:message key="message.courseInformation.courseAvaliationMethods" />
	</td>
</tr>
</table>
<table>
<logic:iterate id="evaluationMethod" name="siteCourseInformation" property="infoEvaluationMethod">
<tr>
	<td> <bean:write name="evaluationMethod" property="evaluationElements"/></td>
</tr>
</table>
<br />
<table width="100%" cellpadding="0" cellspacing="0">
<tr>
	<td class="infoop" width="50px"><span class="emphasis-box">8</span></td>
	<td class="infoop">
	  <bean:message key="message.courseInformation.courseSupportLessons" />
	</td>
</tr>
</table>
<table>
<tr>
	<td> <bean:write name="courseInformation" property="courseSupportLessons"/></td>
</tr>
</table>
<br />
<table width="100%" cellpadding="0" cellspacing="0">
<tr>
	<td class="infoop" width="50px"><span class="emphasis-box">9</span></td>
	<td class="infoop">
	  <bean:message key="message.courseInformation.courseReport" />
	</td>
</tr>
</table>
<table width="100%" cellpadding="0" cellspacing="0"  border=1>
<tr>
	<td><html:textarea name="siteCourseInformation" property="infoCourseReport"/></td> 
</tr>
</table>
<h3>
<table>
<html:hidden property="courseReportId" value="siteCourseInformation.infoCourseReport.idInternal"/> 
<html:hidden property="executionCourseId" value="executionCourse.idInternal"/> 
<html:hidden property="executionPeriodId" value="executionPeriod.idInternal"/> 
<html:hidden property="executionYearId" value="executionYear.idInternal"/> 
<tr align="center">	
	<td>
	<html:submit styleClass="inputbutton" property="confirm">
		<bean:message key="button.save"/>
	</html:submit>
	</td>
	<td>
		<html:reset styleClass="inputbutton">
		<bean:message key="label.clear"/>
	</html:reset>
	</td>
</tr>
</table>
</h3>
</html:form>
</logic:present>
