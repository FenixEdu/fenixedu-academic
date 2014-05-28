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
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<html:xhtml/>

<h2><bean:message key="title.courseInformation"/></h2>

<logic:present name="siteView"> 
	<bean:define id="siteCourseInformation" name="siteView" property="component"/>
	<bean:define id="executionCourse" name="siteCourseInformation" property="infoExecutionCourse" type="net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse"/>
	<bean:define id="executionPeriod" name="executionCourse" property="infoExecutionPeriod"/>
	<bean:define id="executionYear" name="executionPeriod" property="infoExecutionYear"/>

	<div class="infoop2">
		<p>
			<%--<bean:message key="message.courseInformation.courseName"/>--%>
			<%--<bean:message key="message.courseInformation.executionYear"/>--%>
			<h3 class="mtop05 mbottom15"><bean:write name="executionCourse" property="nome"/> <bean:write name="executionYear" property="year"/></h3>
		</p>
			<logic:iterate id="curricularCourse" name="siteCourseInformation" property="infoCurricularCourses">
				<div class="mbottom15">
					<b><bean:write name="curricularCourse" property="infoDegreeCurricularPlan.infoDegree.nome"/></b>

		  			<logic:iterate id="curricularCourseScope" name="curricularCourse" property="infoScopes">
		  				<p class="mvert05">
		  				<bean:message key="message.courseInformation.curricularYear" />
						<bean:write name="curricularCourseScope" property="infoCurricularSemester.infoCurricularYear.year" />
						</p>
						<p class="mvert05">
						<bean:message key="message.courseInformation.semester" />
						<bean:write name="curricularCourseScope" property="infoCurricularSemester.semester" />
						</p>
				 	</logic:iterate>	
					
					<p class="mvert05">
						<bean:message key="message.courseInformation.courseType" />
					  	<logic:equal name="curricularCourse" property="mandatory" value="true">
					  		<bean:message key="message.courseInformation.mandatory" />
					  	</logic:equal>
					  	<logic:equal name="curricularCourse" property="mandatory" value="false">
					  		<bean:message key="message.courseInformation.optional" />
					  	</logic:equal>
				  	</p>
				</div>
	 	
				  	<%-- VER --%>
				 	<%--<bean:message key="message.courseInformation.courseSemesterOrAnual" />&nbsp;
				 	<bean:write name="curricularCourse" property="curricularCourseExecutionScope.type" />--%>
			</logic:iterate>

		<div class="mtop15">
			<p class="mvert05">
				<strong><bean:message key="message.courseInformation.responsiblesForTheCourse"/></strong>
			</p>
		<logic:iterate id="infoTeacher" name="siteCourseInformation" property="infoResponsibleTeachers">
			<p class="mvert05">
				<bean:write name="infoTeacher" property="infoPerson.nome" /> - 
				<bean:message key="message.courseInformation.categoryOfTheResponsibleForCourse" />
				<logic:present name="infoTeacher" property="infoCategory.name">
					<bean:write name="infoTeacher" property="infoCategory.name" />
				</logic:present>
			</p>
		</logic:iterate>
		</div>
	</div>
	
	<p class="infoop mtop2"><span class="emphasis-box">1</span>
	<bean:message key="message.courseInformation.timeTable" /></p>
	<table class="tstyle4" width="98%">
		<tr>
		    <th style="width: 200px;"><bean:message key="message.courseInformation.classType"/></th>
		    <th><bean:message key="message.courseInformation.numberOfClasses"/></th>
		    <th><bean:message key="message.courseInformation.classDuration"/></th>
			<th><bean:message key="message.courseInformation.totalDuration"/></th>
		</tr>
		<logic:iterate id="infoLesson" name="siteCourseInformation" property="infoLessons">
			<tr>
					<logic:equal name="infoLesson" property="infoShift.shiftTypesCodePrettyPrint" value="T">
						<td><bean:message key="message.courseInformation.typeClassTeoricas"/></td>
						<td><bean:write name="siteCourseInformation" property="numberOfTheoLessons"/></td>
					</logic:equal>
					<logic:equal name="infoLesson" property="infoShift.shiftTypesCodePrettyPrint" value="P">
						<td><bean:message key="message.courseInformation.typeClassPraticas"/></td>
						<td><bean:write name="siteCourseInformation" property="numberOfPratLessons"/></td>
					</logic:equal>
					<logic:equal name="infoLesson" property="infoShift.shiftTypesCodePrettyPrint" value="TP">
						<td><bean:message key="message.courseInformation.typeClassTeoPrat"/></td>
						<td><bean:write name="siteCourseInformation" property="numberOfTheoPratLessons"/></td>
					</logic:equal>
					<logic:equal name="infoLesson" property="infoShift.shiftTypesCodePrettyPrint" value="L">
						<td><bean:message key="message.courseInformation.typeClassLab"/></td>
						<td><bean:write name="siteCourseInformation" property="numberOfLabLessons"/></td>
					</logic:equal>
					
					<td><bean:write name="infoLesson" property="lessonDuration"/></td>
					
					<logic:equal name="infoLesson" property="infoShift.shiftTypesCodePrettyPrint" value="T">				
						<td><bean:write name="executionCourse" property="weeklyTheoreticalHours"/></td>
					</logic:equal>				
					<logic:equal name="infoLesson" property="infoShift.shiftTypesCodePrettyPrint" value="P">				
						<td><bean:write name="executionCourse" property="weeklyPraticalHours"/></td>
					</logic:equal>				
					<logic:equal name="infoLesson" property="infoShift.shiftTypesCodePrettyPrint" value="TP">				
						<td><bean:write name="executionCourse" property="weeklyTheoPratHours"/></td>
					</logic:equal>				
					<logic:equal name="infoLesson" property="infoShift.shiftTypesCodePrettyPrint" value="L">				
						<td><bean:write name="executionCourse" property="weeklyLabHours"/></td>
					</logic:equal>						
			</tr>
		</logic:iterate>
	</table>


	<p class="infoop mtop2"><span class="emphasis-box">2</span>
	<bean:message key="message.courseInformation.LecturingTeachers" /></p>
	
	<p class="mvert05">
		<bean:message key="message.courseInformation.numberOfStudents"/>:
		<strong><bean:write name="siteCourseInformation" property="infoExecutionCourse.numberOfAttendingStudents"/></strong>
	</p>
	<p class="mvert05"><bean:message key="message.courseInformation.specialTeacherWarning" arg0="<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>"/></p>
	<table class="tstyle4 mtop05" width="98%">
		<tr>
			<th> <bean:message key="message.courseInformation.nameOfTeacher"/></th>
			<th> <bean:message key="message.courseInformation.categoryOfTeacher"/></th>
			<th> <bean:message key="message.courseInformation.typeOfClassOfTeacher"/></th>
		</tr>
		<logic:iterate id="infoTeacher" name="siteCourseInformation" property="infoLecturingTeachers">
			<tr>
				<td><bean:write name="infoTeacher" property="infoPerson.nome"/></td>
				<td>
					<logic:present name="infoTeacher" property="infoCategory.name">
						<bean:write name="infoTeacher" property="infoCategory.name"/>
					</logic:present>
				</td>
				<!--VER O TIPO DE AULA QUE CADA PROF DA-->
				<td>&nbsp;<%--<bean:write name="infoTeacher" property="typeOfClassOfTeacher"/>--%></td>
			</tr>
		</logic:iterate>
	</table>

	<p class="infoop mtop2"><span class="emphasis-box">3</span>
	<bean:message key="message.courseInformation.CourseResults" /></p>
	<p>
		<em><bean:message key="message.courseInformation.notYetAvailable" /></em>
	</p>
	<%--<table>
		<tr>
			<td></td>
			<th><bean:message key="message.courseInformation.numberOfStudents" /></th>
		</tr>
		<tr>
			<th><bean:message key="message.courseInformation.enrolledStudents" /></th>
			<td><bean:write name="executionCourse" property="numberOfAttendingStudents"/></td>
		</tr>
		<!-- VER-->
		<tr>
			<th><bean:message key="message.courseInformation.evaluatedStudents" /></th>
			<td><bean:write name="siteCourseInformation" property="evaluatedStudents"/></td>
		</tr>
		<tr>
			<th><bean:message key="message.courseInformation.approvedStudents" /></th>
			<td><bean:write name="siteCourseInformation" property="approvedStudents"/></td>
		</tr>
		<tr>
			<th><bean:message key="message.courseInformation.evaluatedPerEnrolled" /></th>
			<!-- VER ONDE SE FAZEM AS CONTAS-->
			<td><bean:write name="siteCourseInformation" property="evaluatedPerEnrolled"/></td>
		</tr>
		<tr>
			<th><bean:message key="message.courseInformation.approvedPerEvaluated" /></th>
			<td><bean:write name="siteCourseInformation" property="approvedPerEvaluated"/></td>
		</tr>
		<tr>
			<th><bean:message key="message.courseInformation.approvedPerEnrolled" /></th>
			<td><bean:write name="siteCourseInformation" property="approvedPerEnrolled"/></td>
		</tr>
	</table>--%>

	<p class="infoop mtop2"><span class="emphasis-box">4</span>
	<bean:message key="message.courseInformation.courseObjectives" /></p>

	<logic:iterate id="infoCurriculum" name="siteCourseInformation" property="infoCurriculums">
		<p>
			<b><bean:write name="infoCurriculum" property="infoCurricularCourse.infoDegreeCurricularPlan.infoDegree.nome"/></b>
		</p>
		<p>		
			<b><bean:message key="label.generalObjectives"/></b>
			<bean:write name="infoCurriculum" property="generalObjectives" filter="false"/>
		</p>
		<p>
			<b><bean:message key="label.operacionalObjectives"/></b>
			<bean:write name="infoCurriculum" property="operacionalObjectives" filter="false"/>
		</p>
	</logic:iterate>


	<p class="infoop mtop2"><span class="emphasis-box">5</span>
	<bean:message key="message.courseInformation.courseProgram" /></p>
		<table>
			<logic:iterate id="infoCurriculum" name="siteCourseInformation" property="infoCurriculums">
				<tr>
					<td>
						<b><bean:write name="infoCurriculum" property="infoCurricularCourse.infoDegreeCurricularPlan.infoDegree.nome"/></b>
						<br />
						<bean:write name="infoCurriculum" property="program" filter="false"/>
					</td>
				</tr>
			</logic:iterate>
		</table>

	<p class="infoop mtop2"><span class="emphasis-box">6</span>
	<bean:message key="message.courseInformation.courseBibliographicReference" /></p>

	<p class="mtop15 mbottom05"><strong><bean:message key="message.courseInformation.coursePrincipalBibliographicReference" /></strong></p>
	<table class="tstyle4 tdcenter mtop05" width="98%">
		<logic:iterate id="bibliographicReference" name="siteCourseInformation" property="executionCourse.associatedBibliographicReferences">
		<logic:equal name="bibliographicReference" property="optional" value="false">
		<tr>
			<td><bean:write name="bibliographicReference" property="title" filter="false"/></td>
			<td><bean:write name="bibliographicReference" property="authors"/></td>
			<td><bean:write name="bibliographicReference" property="reference"/></td>
			<td><bean:write name="bibliographicReference" property="year"/></td>
		</tr>
		</logic:equal>
		</logic:iterate>
	</table>
	
	<p class="mtop15 mbottom05"><strong><bean:message key="message.courseInformation.courseSecondaryBibliographicReference" /></strong></p>
	<table class="tstyle4 tdcenter mvert05" width="98%">
		<logic:iterate id="bibliographicReference" name="siteCourseInformation" property="executionCourse.associatedBibliographicReferences">
		<logic:equal name="bibliographicReference" property="optional" value="true">
		<tr>
			<td><bean:write name="bibliographicReference" property="title" filter="false"/></td>
			<td><bean:write name="bibliographicReference" property="authors"/></td>
			<td><bean:write name="bibliographicReference" property="reference"/></td>
			<td><bean:write name="bibliographicReference" property="year"/></td>
		</tr>
		</logic:equal>
		</logic:iterate>
	</table>

	<p class="infoop mtop2"><span class="emphasis-box">7</span>
	<bean:message key="message.courseInformation.courseAvaliationMethods" /></p>

	<logic:present name="siteCourseInformation" property="executionCourse.evaluationMethod">
		<logic:present name="siteCourseInformation" property="executionCourse.evaluationMethod.evaluationElements">
			<p><bean:write name="siteCourseInformation" property="executionCourse.evaluationMethod.evaluationElements" filter="false"/></p>
		</logic:present>
	</logic:present>


	<p class="infoop mtop2"><span class="emphasis-box">8</span>
	<bean:message key="message.courseInformation.courseSupportLessons" /></p>
<%--  
	<table>
		<tr>
			<td><bean:write name="siteCourseInformation" property="courseSupportLessons"/></td>
		</tr>
	</table>
--%>

	<p class="infoop mtop2"><span class="emphasis-box">9</span>
	<bean:message key="message.courseInformation.courseReport" /></p>

	<logic:present name="siteCourseInformation" property="infoCourseReport">
		<logic:notEmpty name="siteCourseInformation" property="infoCourseReport.report">
			<p>
				<bean:write name="siteCourseInformation" property="infoCourseReport.report"/>
			</p>
		</logic:notEmpty>
	</logic:present>
	<logic:empty name="siteCourseInformation" property="infoCourseReport">
		<p>
			<em><bean:message key="message.courseInformation.notYetAvailable"/></em>
		</p>
	</logic:empty>
	
</logic:present>
