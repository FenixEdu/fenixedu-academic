<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<h2><bean:message key="title.courseInformation"/></h2>
<logic:present name="siteView"> 
	<html:form action="/courseInformation">
	<span class="error">
		<html:errors/>
	</span>
	<br />
	<bean:define id="siteCourseInformation" name="siteView" property="component"/>
	<bean:define id="executionCourse" name="siteCourseInformation" property="infoExecutionCourse"/>
	<bean:define id="executionPeriod" name="executionCourse" property="infoExecutionPeriod"/>
	<bean:define id="executionYear" name="executionPeriod" property="infoExecutionYear"/>
	<br/>
		<table>
		<tr>
			<td>
				<bean:message key="message.courseInformation.courseName" /> &nbsp;
				<bean:write name="executionCourse" property="nome" />
			</td>
			<td></td>
			<td></td>
			<td>
				<bean:message key="message.courseInformation.executionYear" />&nbsp;
				<bean:write name="executionYear" property="year" />
			</td>
		</tr>
		<tr>
			<td>
			<logic:iterate id="curricularCourse" name="siteCourseInformation" property="infoCurricularCourses">
			  	<logic:iterate id="curricularCourseScope" name="curricularCourse" property="infoScopes">
					<bean:message key="message.courseInformation.curricularYear" />
					&nbsp;<bean:write name="curricularCourseScope" property="infoCurricularSemester.infoCurricularYear.year" />,
					<bean:message key="message.courseInformation.semester" />
					&nbsp;<bean:write name="curricularCourseScope" property="infoCurricularSemester.semester" />
			 	</logic:iterate>
			  	<bean:message key="message.courseInformation.courseType" />,
			  	<logic:equal name="curricularCourse" property="mandatory" value="true">
			  		<bean:message key="message.courseInformation.mandatory" />
			  	</logic:equal>
			  	<logic:equal name="curricularCourse" property="mandatory" value="false">
			  		<bean:message key="message.courseInformation.optional" />
			  	</logic:equal>
			  	<!-- VER -->
			 	<%--<bean:message key="message.courseInformation.courseSemesterOrAnual" />&nbsp;
					 	<bean:write name="curricularCourse" property="curricularCourseExecutionScope.type" />--%>
			</logic:iterate>
			</td>
			<td></td>
		</tr>
		<tr>
			<td>
				<logic:iterate id="infoTeacher" name="siteCourseInformation" property="infoResponsibleTeachers">
					<bean:message key="message.courseInformation.responsibleForTheCourse" />
					&nbsp;<bean:write name="infoTeacher" property="infoPerson.nome" />,
					<bean:message key="message.courseInformation.categoryOfTheResponsibleForCourse" />
					&nbsp; <bean:write name="infoTeacher" property="infoCategory.longName" />
				</logic:iterate>
			</td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		</table>
		<br />
		<p class="infoop"><span class="emphasis-box">1</span>
		<bean:message key="message.courseInformation.timeTable" /></p>
		<table width="100%" border="0" cellspacing="1" style="margin-top:10px">
			<tr>
			    <td class="listClasses-header" width="200px"><bean:message key="message.courseInformation.classType"/></td>
			    <td class="listClasses-header"><bean:message key="message.courseInformation.numberOfClasses"/></td>
			    <td class="listClasses-header"><bean:message key="message.courseInformation.classDuration"/></td>
				<td class="listClasses-header"><bean:message key="message.courseInformation.totalDuration"/></td>
			</tr>
			<logic:iterate id="infoLesson" name="siteCourseInformation" property="infoLessons">
				<tr>
					<logic:equal name="infoLesson" property="tipo.siglaTipoAula" value="T">
						<td class="listClasses"><bean:message key="message.courseInformation.typeClassTeoricas"/></td>
						<td class="listClasses"><bean:write name="executionCourse" property="theoreticalHours"/></td>
					</logic:equal>
					<logic:equal name="infoLesson" property="tipo.siglaTipoAula" value="P">
						<td class="listClasses"><bean:message key="message.courseInformation.typeClassPraticas"/></td>
						<td class="listClasses"><bean:write name="executionCourse" property="praticalHours"/></td>
					</logic:equal>
					<logic:equal name="infoLesson" property="tipo.siglaTipoAula" value="TP">
						<td class="listClasses"><bean:message key="message.courseInformation.typeClassTeoPrat"/></td>
						<td class="listClasses"><bean:write name="executionCourse" property="theoPratHours"/></td>
					</logic:equal>
					<logic:equal name="infoLesson" property="tipo.siglaTipoAula" value="L">
						<td class="listClasses"><bean:message key="message.courseInformation.typeClassLab"/></td>
						<td class="listClasses"><bean:write name="executionCourse" property="labHours"/></td>
					</logic:equal>
					<td class="listClasses"><bean:write name="infoLesson" property="lessonDuration"/></td>
					<td class="listClasses"><bean:write name="infoLesson" property="totalDuration"/></td>
				</tr>
			</logic:iterate>
		</table>
		<br />
		<p class="infoop"><span class="emphasis-box">2</span>
		<bean:message key="message.courseInformation.LecturingTeachers" /></p>
		<style="margin-top:10px">
		<bean:message key="message.courseInformation.numberOfStudents"/>
		<bean:write name="siteCourseInformation" property="infoExecutionCourse.numberOfAttendingStudents"/>
		<table width="100%" border="0" cellspacing="1" style="margin-top:10px">
			<tr>
				<td class="listClasses-header"> <bean:message key="message.courseInformation.nameOfTeacher"/></td>
				<td class="listClasses-header"> <bean:message key="message.courseInformation.categoryOfTeacher"/></td>
				<td class="listClasses-header"> <bean:message key="message.courseInformation.typeOfClassOfTeacher"/></td>
			</tr>
			<tr>
				<logic:iterate id="infoTeacher" name="siteCourseInformation" property="infoLecturingTeachers">
					<td class="listClasses"> <bean:write name="infoTeacher" property="infoPerson.nome"/></td>
					<td class="listClasses"> <bean:write name="infoTeacher" property="infoCategory.longName"/></td>
					<!--VER O TIPO DE AULA QUE CADA PROF DA-->
					<%--<td class="listClasses"> <bean:write name="infoTeacher" property="typeOfClassOfTeacher"/></td>--%>
				</logic:iterate>
			</tr>
		</table>
		<br />
		<p class="infoop"><span class="emphasis-box">3</span>
		<bean:message key="message.courseInformation.CourseResults" /></p>
		<br />
		Informação ainda não disponível
		<%--
		<table border="0" cellspacing="1" style="margin-top:10px">
			<tr>
				<td></td>
				<td class="listClasses-header"><bean:message key="message.courseInformation.numberOfStudents" /></td>
			</tr>
			<tr>
				<td class="listClasses-header"><bean:message key="message.courseInformation.enrolledStudents" /></td>
				<td class="listClasses"><bean:write name="executionCourse" property="numberOfAttendingStudents"/></td>
			</tr>
			<!-- VER-->
			<tr>
				<td class="listClasses-header"><bean:message key="message.courseInformation.evaluatedStudents" /></td>
				<td class="listClasses"><bean:write name="siteCourseInformation" property="evaluatedStudents"/></td>
			</tr>
			<tr>
				<td class="listClasses-header"><bean:message key="message.courseInformation.approvedStudents" /></td>
				<td class="listClasses"><bean:write name="siteCourseInformation" property="approvedStudents"/></td>
			</tr>
			<tr>
				<td class="listClasses-header"><bean:message key="message.courseInformation.evaluatedPerEnrolled" /></td>
				<!-- VER ONDE SE FAZEM AS CONTAS-->
				<td class="listClasses"><bean:write name="siteCourseInformation" property="evaluatedPerEnrolled"/></td>
			</tr>
			<tr>
				<td class="listClasses-header"><bean:message key="message.courseInformation.approvedPerEvaluated" /></td>
				<td class="listClasses"><bean:write name="siteCourseInformation" property="approvedPerEvaluated"/></td>
			</tr>
			<tr>
				<td class="listClasses-header"><bean:message key="message.courseInformation.approvedPerEnrolled" /></td>
				<td class="listClasses"><bean:write name="siteCourseInformation" property="approvedPerEnrolled"/></td>
			</tr>
		</table>--%>
		<br />
		<p class="infoop"><span class="emphasis-box">4</span>
		<bean:message key="message.courseInformation.courseObjectives" /></p>
		<table border="0" cellspacing="1" style="margin-top:10px">
			<logic:iterate id="infoCurriculum" name="siteCourseInformation" property="infoCurriculums">
				<tr>
					<td><bean:write name="infoCurriculum" property="generalObjectives"/></td>
				</tr>
				<tr>
					<td><bean:write name="infoCurriculum" property="operacionalObjectives"/></td>
				</tr>
			</logic:iterate>
		</table>
		<br />
		<p class="infoop"><span class="emphasis-box">5</span>
		<bean:message key="message.courseInformation.courseProgram" /></p>
		<table border="0" cellspacing="1" style="margin-top:10px">
			<logic:iterate id="infoCurriculum" name="siteCourseInformation" property="infoCurriculums">
				<tr>
					<td><bean:write name="infoCurriculum" property="program"/></td>
				</tr>
			</logic:iterate>
		</table>
		<br />
		<p class="infoop"><span class="emphasis-box">6</span>
		<bean:message key="message.courseInformation.courseBibliographicReference" /></p>
		<table>
			<tr>
				<td class="listClasses-header"><bean:message key="message.courseInformation.coursePrincipalBibliographicReference" /></td>
				<td></td>
				<td></td>
				<td></td>
			</tr>
			<logic:iterate id="infoBibliographicReference" name="siteCourseInformation" property="infoBibliographicReferences">
				<logic:equal name="infoBibliographicReference" property="optional" value="false">
				<tr>
					<td class="listClasses"> <bean:write name="infoBibliographicReference" property="title"/></td>
					<td class="listClasses"> <bean:write name="infoBibliographicReference" property="authors"/></td>
					<td class="listClasses"> <bean:write name="infoBibliographicReference" property="reference"/></td>
					<td class="listClasses"> <bean:write name="infoBibliographicReference" property="year"/></td>
				</tr>
				</logic:equal>
			</logic:iterate>
		</table>
		<table border="0" cellspacing="1" style="margin-top:10px">
			<tr>
				<td class="listClasses-header"><bean:message key="message.courseInformation.courseSecondaryBibliographicReference" /></td>
			</tr>
				<logic:iterate id="infoBibliographicReference" name="siteCourseInformation" property="infoBibliographicReferences">
					<logic:equal name="infoBibliographicReference" property="optional" value="true">
					<tr>
						<td class="listClasses"> <bean:write name="infoBibliographicReference" property="title"/></td>
						<td class="listClasses"> <bean:write name="infoBibliographicReference" property="authors"/></td>
						<td class="listClasses"> <bean:write name="infoBibliographicReference" property="reference"/></td>
						<td class="listClasses"> <bean:write name="infoBibliographicReference" property="year"/></td>
					</tr>
					</logic:equal>
				</logic:iterate>
		</table>
		<br />
		<p class="infoop"><span class="emphasis-box">7</span>
		<bean:message key="message.courseInformation.courseAvaliationMethods" /></p>
		<table border="0" cellspacing="1" style="margin-top:10px">
		<tr>
			<td><bean:write name="siteCourseInformation" property="infoEvaluationMethod.evaluationElements"/></td>
		</tr>
		</table>
		<br />
		<p class="infoop"><span class="emphasis-box">8</span>
		<bean:message key="message.courseInformation.courseSupportLessons" /></p>
		<table border="0" cellspacing="1" style="margin-top:10px">
			<tr>
				<%--<td> <bean:write name="siteCourseInformation" property="courseSupportLessons"/></td>--%>
			</tr>
		</table>
		<br/>
		<p class="infoop"><span class="emphasis-box">9</span>
		<bean:message key="message.courseInformation.courseReport" /></p>
		<br />
		Informação ainda não disponível
		<%--<table width="100%" cellpadding="0" cellspacing="0" style="margin-top:10px">
			<tr>
				<td><html:textarea property="report" rows="7" cols="90%"/></td> 
			</tr>
		</table>--%>
		<h3>
		<table>
			<html:hidden property="courseReportId" /> 
			<html:hidden property="executionCourseId" /> 
			<html:hidden property="executionPeriodId" /> 
			<html:hidden property="executionYearId" />
			<html:hidden property="method" value="edit"/>
			<html:hidden property="page" value="1"/>
			<%--<tr align="center">	
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
			</tr>--%>
		</table>
		</h3>
	</html:form>
</logic:present>