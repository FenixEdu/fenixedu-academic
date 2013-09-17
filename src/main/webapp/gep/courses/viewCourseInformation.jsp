<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<h2><bean:message key="title.courseInformation"/></h2>
<logic:present name="infoSiteCourseInformation"> 
	<bean:define id="executionCourse" name="infoSiteCourseInformation" property="infoExecutionCourse"/>
	<bean:define id="executionPeriod" name="executionCourse" property="infoExecutionPeriod"/>
	<bean:define id="executionYear" name="executionPeriod" property="infoExecutionYear"/>
	<br/>
	<logic:present name="infoExecutionDegree">
	<table width="98%" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td align="center" class="infoselected">
				<p>
					<strong><bean:message key="title.gep.teachersInformationSelectedDegree"
										  bundle="GEP_RESOURCES"/>:</strong> 
					<bean:write name="infoExecutionDegree" property="infoDegreeCurricularPlan.infoDegree.nome"/>
					<br />
					<strong><bean:message key="title.gep.executionYear"
										  bundle="GEP_RESOURCES"/>:</strong>
					<bean:write name="infoExecutionDegree" property="infoExecutionYear.year"/>
				</p>			
			</td>
		</tr>
	</table>
	</logic:present>
	<br />
	<table class="infoselected" width="98%">
		<tr>
		<td>
		<b><bean:message key="message.courseInformation.courseName" /></b>&nbsp;
		   <bean:write name="executionCourse" property="nome" />
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;		
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;		
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;		
		<b><bean:message key="message.courseInformation.executionYear" /></b>
		&nbsp;<bean:write name="executionYear" property="year" />
		<logic:iterate id="curricularCourse" name="infoSiteCourseInformation" property="infoCurricularCourses">
			<logic:present name="infoExecutionDegree">
				<bean:define id="degreeCurricularPlanId" name="infoExecutionDegree" property="infoDegreeCurricularPlan.externalId"/>
		  		<logic:equal name="curricularCourse" 
		  						 property="infoDegreeCurricularPlan.externalId" 
		  						 value="<%= degreeCurricularPlanId.toString() %>">
					<blockquote style="margin-top:1px">
						<br />
						<b><bean:write name="curricularCourse" property="infoDegreeCurricularPlan.infoDegree.nome"/></b>
			  			<logic:iterate id="curricularCourseScope" name="curricularCourse" property="infoScopes">
				  				<br />
				  				<b><bean:message key="message.courseInformation.curricularYear" /></b>
								&nbsp;<bean:write name="curricularCourseScope" property="infoCurricularSemester.infoCurricularYear.year" />
								&nbsp;&nbsp;&nbsp;
								<b><bean:message key="message.courseInformation.semester" /></b>
								&nbsp;<bean:write name="curricularCourseScope" property="infoCurricularSemester.semester" />
								<br />
					 	</logic:iterate>	
						<b><bean:message key="message.courseInformation.courseType" /></b>
					  	<logic:equal name="curricularCourse" property="mandatory" value="true">
					  		<bean:message key="message.courseInformation.mandatory" />
					  	</logic:equal>
					  	<logic:equal name="curricularCourse" property="mandatory" value="false">
					  		<bean:message key="message.courseInformation.optional" />
					  	</logic:equal>
					</blockquote>			 	
					  	<%-- VER --%>
					 	<%--<bean:message key="message.courseInformation.courseSemesterOrAnual" />&nbsp;
					 	<bean:write name="curricularCourse" property="curricularCourseExecutionScope.type" />--%>
				</logic:equal>
			</logic:present>
			<logic:notPresent name="infoExecutionDegree">
				<blockquote style="margin-top:1px">
					<br />
					<b><bean:write name="curricularCourse" property="infoDegreeCurricularPlan.infoDegree.nome"/></b>
		  			<logic:iterate id="curricularCourseScope" name="curricularCourse" property="infoScopes">
			  				<br />
			  				<b><bean:message key="message.courseInformation.curricularYear" /></b>
							&nbsp;<bean:write name="curricularCourseScope" property="infoCurricularSemester.infoCurricularYear.year" />
							&nbsp;&nbsp;&nbsp;
							<b><bean:message key="message.courseInformation.semester" /></b>
							&nbsp;<bean:write name="curricularCourseScope" property="infoCurricularSemester.semester" />
							<br />
				 	</logic:iterate>	
					<b><bean:message key="message.courseInformation.courseType" /></b>
				  	<logic:equal name="curricularCourse" property="mandatory" value="true">
				  		<bean:message key="message.courseInformation.mandatory" />
				  	</logic:equal>
				  	<logic:equal name="curricularCourse" property="mandatory" value="false">
				  		<bean:message key="message.courseInformation.optional" />
				  	</logic:equal>
				</blockquote>			 	
				  	<%-- VER --%>
				 	<%--<bean:message key="message.courseInformation.courseSemesterOrAnual" />&nbsp;
				 	<bean:write name="curricularCourse" property="curricularCourseExecutionScope.type" />--%>
			</logic:notPresent>
		</logic:iterate>
		<logic:iterate id="infoTeacher" name="infoSiteCourseInformation" property="infoResponsibleTeachers">
			<b><bean:message key="message.courseInformation.responsibleForTheCourse" /></b>
			<bean:write name="infoTeacher" property="infoPerson.nome" />
			&nbsp;&nbsp;&nbsp;
			<b><bean:message key="message.courseInformation.categoryOfTheResponsibleForCourse" /></b>
			<bean:write name="infoTeacher" property="infoCategory.name" /> <br />
		</logic:iterate>
		</td>
		</tr>
	</table>
	<p class="infoop"><span class="emphasis-box">1</span>
	<bean:message key="message.courseInformation.timeTable" /></p>
	<table width="98%" border="0" cellspacing="1" style="margin-top:10px">
		<tr>
		    <th class="listClasses-header" width="200px"><bean:message key="message.courseInformation.classType"/></th>
		    <th class="listClasses-header"><bean:message key="message.courseInformation.numberOfClasses"/></th>
		    <th class="listClasses-header"><bean:message key="message.courseInformation.classDuration"/></th>
			<th class="listClasses-header"><bean:message key="message.courseInformation.totalDuration"/></th>
		</tr>
		<logic:iterate id="infoLesson" name="infoSiteCourseInformation" property="infoLessons">
			<tr>
				<logic:equal name="infoLesson" property="infoShift.shiftTypesCodePrettyPrint" value="T">
					<td class="listClasses"><bean:message key="message.courseInformation.typeClassTeoricas"/></td>
					<td class="listClasses"><bean:write name="infoSiteCourseInformation" property="numberOfTheoLessons"/></td>
				</logic:equal>
				<logic:equal name="infoLesson" property="infoShift.shiftTypesCodePrettyPrint" value="P">
					<td class="listClasses"><bean:message key="message.courseInformation.typeClassPraticas"/></td>
					<td class="listClasses"><bean:write name="infoSiteCourseInformation" property="numberOfPratLessons"/></td>
				</logic:equal>
				<logic:equal name="infoLesson" property="infoShift.shiftTypesCodePrettyPrint" value="TP">
					<td class="listClasses"><bean:message key="message.courseInformation.typeClassTeoPrat"/></td>
					<td class="listClasses"><bean:write name="infoSiteCourseInformation" property="numberOfTheoPratLessons"/></td>
				</logic:equal>
				<logic:equal name="infoLesson" property="infoShift.shiftTypesCodePrettyPrint" value="L">
					<td class="listClasses"><bean:message key="message.courseInformation.typeClassLab"/></td>
					<td class="listClasses"><bean:write name="infoSiteCourseInformation" property="numberOfLabLessons"/></td>
				</logic:equal>
				
				<td class="listClasses"><bean:write name="infoLesson" property="lessonDuration"/></td>
				
				<logic:equal name="infoLesson" property="infoShift.shiftTypesCodePrettyPrint" value="T">					
					<td class="listClasses"><bean:write name="executionCourse" property="weeklyTheoreticalHours"/></td>
				</logic:equal>				
				<logic:equal name="infoLesson" property="infoShift.shiftTypesCodePrettyPrint" value="P">					
					<td class="listClasses"><bean:write name="executionCourse" property="weeklyPraticalHours"/></td>
				</logic:equal>
				<logic:equal name="infoLesson" property="infoShift.shiftTypesCodePrettyPrint" value="TP">					
					<td class="listClasses"><bean:write name="executionCourse" property="weeklyTheoPratHours"/></td>
				</logic:equal>
				<logic:equal name="infoLesson" property="infoShift.shiftTypesCodePrettyPrint" value="L">					
					<td class="listClasses"><bean:write name="executionCourse" property="weeklyLabHours"/></td>
				</logic:equal>
			</tr>
		</logic:iterate>	
	</table>
	<br />
	<p class="infoop"><span class="emphasis-box">2</span>
	<bean:message key="message.courseInformation.LecturingTeachers" /></p>
	<bean:message key="message.courseInformation.numberOfStudents"/>:
	<bean:write name="infoSiteCourseInformation" property="infoExecutionCourse.numberOfAttendingStudents"/>
	<p><bean:message key="message.courseInformation.specialTeacherWarning"/></p>
	<table width="98%" border="0" cellspacing="1" style="margin-top:10px">
		<tr>
			<th class="listClasses-header"> <bean:message key="message.courseInformation.nameOfTeacher"/></th>
			<th class="listClasses-header"> <bean:message key="message.courseInformation.categoryOfTeacher"/></th>
			<th class="listClasses-header"> <bean:message key="message.courseInformation.typeOfClassOfTeacher"/></th>
		</tr>
		<logic:iterate id="infoTeacher" name="infoSiteCourseInformation" property="infoLecturingTeachers">
			<tr>
				<td class="listClasses"> <bean:write name="infoTeacher" property="infoPerson.nome"/></td>
				<td class="listClasses"> <bean:write name="infoTeacher" property="infoCategory.name"/></td>
				<!--VER O TIPO DE AULA QUE CADA PROF DA-->
				<td class="listClasses">&nbsp;<%--<bean:write name="infoTeacher" property="typeOfClassOfTeacher"/>--%></td>
			</tr>
		</logic:iterate>
	</table>
	<br />
	<p class="infoop"><span class="emphasis-box">3</span>
	<bean:message key="message.courseInformation.CourseResults" /></p>
	<br />
	<bean:message key="message.courseInformation.notYetAvailable" />
	<%--<table border="0" cellspacing="1" style="margin-top:10px">
		<tr>
			<td></td>
			<th class="listClasses-header"><bean:message key="message.courseInformation.numberOfStudents" /></th>
		</tr>
		<tr>
			<th class="listClasses-header"><bean:message key="message.courseInformation.enrolledStudents" /></th>
			<td class="listClasses"><bean:write name="executionCourse" property="numberOfAttendingStudents"/></td>
		</tr>
		<!-- VER-->
		<tr>
			<th class="listClasses-header"><bean:message key="message.courseInformation.evaluatedStudents" /></th>
			<td class="listClasses"><bean:write name="infoSiteCourseInformation" property="evaluatedStudents"/></td>
		</tr>
		<tr>
			<th class="listClasses-header"><bean:message key="message.courseInformation.approvedStudents" /></th>
			<td class="listClasses"><bean:write name="infoSiteCourseInformation" property="approvedStudents"/></td>
		</tr>
		<tr>
			<th class="listClasses-header"><bean:message key="message.courseInformation.evaluatedPerEnrolled" /></th>
			<!-- VER ONDE SE FAZEM AS CONTAS-->
			<td class="listClasses"><bean:write name="infoSiteCourseInformation" property="evaluatedPerEnrolled"/></td>
		</tr>
		<tr>
			<th class="listClasses-header"><bean:message key="message.courseInformation.approvedPerEvaluated" /></th>
			<td class="listClasses"><bean:write name="infoSiteCourseInformation" property="approvedPerEvaluated"/></td>
		</tr>
		<tr>
			<th class="listClasses-header"><bean:message key="message.courseInformation.approvedPerEnrolled" /></th>
			<td class="listClasses"><bean:write name="infoSiteCourseInformation" property="approvedPerEnrolled"/></td>
		</tr>
	</table>--%>
	<br />
	<p class="infoop"><span class="emphasis-box">4</span>
	<bean:message key="message.courseInformation.courseObjectives" /></p>
	<table border="0" cellspacing="1" style="margin-top:10px">
		<logic:iterate id="infoCurriculum" name="infoSiteCourseInformation" property="infoCurriculums">
			<logic:present name="infoExecutionDegree">
				<bean:define id="degreeCurricularPlanId" name="infoExecutionDegree" property="infoDegreeCurricularPlan.externalId"/>
				<logic:equal name="infoCurriculum" 
	  						 property="infoCurricularCourse.infoDegreeCurricularPlan.externalId" 
	  						 value="<%= degreeCurricularPlanId.toString() %>">
					<tr>
						<td>
							<b><bean:write name="infoCurriculum" property="infoCurricularCourse.infoDegreeCurricularPlan.infoDegree.nome"/></b>
							<br />
						</td>
					</tr>
					<tr>
						<td>
							 <u><bean:message key="label.generalObjectives"/></u>
							 <br />
							 <bean:write name="infoCurriculum" property="generalObjectives" filter="false"/>
						</td>
					</tr>
					<tr>
						<td>
							 <u><bean:message key="label.operacionalObjectives"/></u>
							 <br />
							 <bean:write name="infoCurriculum" property="operacionalObjectives" filter="false"/>
							 <br />
		 					 <br />
						 </td>
					</tr>
				</logic:equal>
			</logic:present>
			<logic:notPresent name="infoExecutionDegree">
				<tr>
					<td>
						<b><bean:write name="infoCurriculum" property="infoCurricularCourse.infoDegreeCurricularPlan.infoDegree.nome"/></b>
						<br />
					</td>
				</tr>
				<tr>
					<td>
						 <u><bean:message key="label.generalObjectives"/></u>
						 <br />
						 <bean:write name="infoCurriculum" property="generalObjectives" filter="false"/>
					</td>
				</tr>
				<tr>
					<td>
						 <u><bean:message key="label.operacionalObjectives"/></u>
						 <br />
						 <bean:write name="infoCurriculum" property="operacionalObjectives" filter="false"/>
						 <br />
	 					 <br />
					 </td>
				</tr>
			</logic:notPresent>
		</logic:iterate>
	</table>
	<br />
	<p class="infoop"><span class="emphasis-box">5</span>
	<bean:message key="message.courseInformation.courseProgram" /></p>
		<table border="0" cellspacing="1" style="margin-top:10px">
			<logic:iterate id="infoCurriculum" name="infoSiteCourseInformation" property="infoCurriculums">
				<logic:present name="infoExecutionDegree">
					<bean:define id="degreeCurricularPlanId" name="infoExecutionDegree" property="infoDegreeCurricularPlan.externalId"/>
					<logic:equal name="infoCurriculum" 
	  						 	property="infoCurricularCourse.infoDegreeCurricularPlan.externalId" 
	  						 	value="<%= degreeCurricularPlanId.toString() %>">
						<tr>
							<td>
								<b><bean:write name="infoCurriculum" property="infoCurricularCourse.infoDegreeCurricularPlan.infoDegree.nome"/></b>
								<br />
								<bean:write name="infoCurriculum" property="program" filter="false"/>
								<br />
								<br />
							</td>
						</tr>
					</logic:equal>
				</logic:present>
				<logic:notPresent name="infoExecutionDegree">
					<tr>
						<td>
							<b><bean:write name="infoCurriculum" property="infoCurricularCourse.infoDegreeCurricularPlan.infoDegree.nome"/></b>
							<br />
							<bean:write name="infoCurriculum" property="program" filter="false"/>
							<br />
							<br />
						</td>
					</tr>
				</logic:notPresent>
			</logic:iterate>
		</table>
	<br />
	<p class="infoop"><span class="emphasis-box">6</span>
	<bean:message key="message.courseInformation.courseBibliographicReference" /></p>
	<table border="0" cellspacing="1" style="margin-top:10px" width="98%">
		<tr>
			<th class="listClasses-header" colspan="4"><bean:message key="message.courseInformation.coursePrincipalBibliographicReference" /></th>
		</tr>
		<logic:iterate id="bibliographicReference" name="infoSiteCourseInformation" property="executionCourse.associatedBibliographicReferences">
		<logic:equal name="bibliographicReference" property="optional" value="false">
		<tr>
			<td class="listClasses"> <bean:write name="bibliographicReference" property="title" filter="false"/></td>
			<td class="listClasses"> <bean:write name="bibliographicReference" property="authors"/></td>
			<td class="listClasses"> <bean:write name="bibliographicReference" property="reference"/></td>
			<td class="listClasses"> <bean:write name="bibliographicReference" property="year"/></td>
		</tr>
		</logic:equal>
		</logic:iterate>
	</table>
	<table border="0" cellspacing="1" style="margin-top:10px" width="98%">
		<tr>
			<th class="listClasses-header" colspan="4"><bean:message key="message.courseInformation.courseSecondaryBibliographicReference" /></th>
		</tr>
		<logic:iterate id="bibliographicReference" name="infoSiteCourseInformation" property="executionCourse.associatedBibliographicReferences">
			<logic:equal name="bibliographicReference" property="optional" value="true">
			<tr>
				<td class="listClasses"> <bean:write name="bibliographicReference" property="title" filter="false"/></td>
				<td class="listClasses"> <bean:write name="bibliographicReference" property="authors"/></td>
				<td class="listClasses"> <bean:write name="bibliographicReference" property="reference"/></td>
				<td class="listClasses"> <bean:write name="bibliographicReference" property="year"/></td>
			</tr>
			</logic:equal>
		</logic:iterate>
	</table>
	<br />
	<p class="infoop"><span class="emphasis-box">7</span>
	<bean:message key="message.courseInformation.courseAvaliationMethods" /></p>
	<table border="0" cellspacing="1" style="margin-top:10px">
		<tr>
			<td><bean:write name="infoSiteCourseInformation" property="executionCourse.evaluationMethod.evaluationElements" filter="false"/></td>
		</tr>
	</table>
	<br />
	<p class="infoop"><span class="emphasis-box">8</span>
	<bean:message key="message.courseInformation.courseSupportLessons" /></p>
<%-- 	<table border="0" cellspacing="1" style="margin-top:10px">
		<tr>
			<td> <bean:write name="infoSiteCourseInformation" property="courseSupportLessons"/></td>
		</tr>
	</table>--%>
	<br/>
	<p class="infoop"><span class="emphasis-box">9</span>
	<bean:message key="message.courseInformation.courseReport" /></p>
	<br />
	<table width="98%" cellpadding="0" cellspacing="0" style="margin-top:10px">
		<tr>
			<td>
				<logic:present name="infoSiteCourseInformation" property="infoCourseReport">
					<logic:notEmpty name="infoSiteCourseInformation" property="infoCourseReport.report">
						<bean:write name="infoSiteCourseInformation" property="infoCourseReport.report"/>
					</logic:notEmpty>
				</logic:present>
				<logic:empty name="infoSiteCourseInformation" property="infoCourseReport">
						<bean:message key="message.courseInformation.notYetAvailable"/>
				</logic:empty>
			</td> 
		</tr>
	</table>
</logic:present>
