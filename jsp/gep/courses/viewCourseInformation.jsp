<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<h2><bean:message key="title.courseInformation"/></h2>
<logic:present name="infoSiteCourseInformation"> 
	<bean:define id="executionCourse" name="infoSiteCourseInformation" property="infoExecutionCourse"/>
	<bean:define id="executionPeriod" name="executionCourse" property="infoExecutionPeriod"/>
	<bean:define id="executionYear" name="executionPeriod" property="infoExecutionYear"/>
	<br/>
	<logic:present name="infoExecutionDegree">
	<table width="100%" border="0" cellpadding="0" cellspacing="0">
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
	<table class="infoselected" width="100%">
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
				<bean:define id="degreeCurricularPlanId" name="infoExecutionDegree" property="infoDegreeCurricularPlan.idInternal"/>
		  		<logic:equal name="curricularCourse" 
		  						 property="infoDegreeCurricularPlan.idInternal" 
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
			<bean:write name="infoTeacher" property="infoCategory.longName" /> <br />
		</logic:iterate>
		</td>
		</tr>
	</table>
	<p class="infoop"><span class="emphasis-box">1</span>
	<bean:message key="message.courseInformation.timeTable" /></p>
	<table width="100%" border="0" cellspacing="1" style="margin-top:10px">
		<tr>
		    <td class="listClasses-header" width="200px"><bean:message key="message.courseInformation.classType"/></td>
		    <td class="listClasses-header"><bean:message key="message.courseInformation.numberOfClasses"/></td>
		    <td class="listClasses-header"><bean:message key="message.courseInformation.classDuration"/></td>
			<td class="listClasses-header"><bean:message key="message.courseInformation.totalDuration"/></td>
		</tr>
		<logic:iterate id="infoLesson" name="infoSiteCourseInformation" property="infoLessons">
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
	<bean:write name="infoSiteCourseInformation" property="infoExecutionCourse.numberOfAttendingStudents"/>
	<table width="100%" border="0" cellspacing="1" style="margin-top:10px">
		<tr>
			<td class="listClasses-header"> <bean:message key="message.courseInformation.nameOfTeacher"/></td>
			<td class="listClasses-header"> <bean:message key="message.courseInformation.categoryOfTeacher"/></td>
			<td class="listClasses-header"> <bean:message key="message.courseInformation.typeOfClassOfTeacher"/></td>
		</tr>
		<logic:iterate id="infoTeacher" name="infoSiteCourseInformation" property="infoLecturingTeachers">
			<tr>
				<td class="listClasses"> <bean:write name="infoTeacher" property="infoPerson.nome"/></td>
				<td class="listClasses"> <bean:write name="infoTeacher" property="infoCategory.longName"/></td>
				<!--VER O TIPO DE AULA QUE CADA PROF DA-->
				<td class="listClasses">&nbsp;<%--<bean:write name="infoTeacher" property="typeOfClassOfTeacher"/>--%></td>
			</tr>
		</logic:iterate>
	</table>
	<br />
	<p class="infoop"><span class="emphasis-box">3</span>
	<bean:message key="message.courseInformation.CourseResults" /></p>
	<br />
	Informação ainda não disponível
	<%--<table border="0" cellspacing="1" style="margin-top:10px">
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
			<td class="listClasses"><bean:write name="infoSiteCourseInformation" property="evaluatedStudents"/></td>
		</tr>
		<tr>
			<td class="listClasses-header"><bean:message key="message.courseInformation.approvedStudents" /></td>
			<td class="listClasses"><bean:write name="infoSiteCourseInformation" property="approvedStudents"/></td>
		</tr>
		<tr>
			<td class="listClasses-header"><bean:message key="message.courseInformation.evaluatedPerEnrolled" /></td>
			<!-- VER ONDE SE FAZEM AS CONTAS-->
			<td class="listClasses"><bean:write name="infoSiteCourseInformation" property="evaluatedPerEnrolled"/></td>
		</tr>
		<tr>
			<td class="listClasses-header"><bean:message key="message.courseInformation.approvedPerEvaluated" /></td>
			<td class="listClasses"><bean:write name="infoSiteCourseInformation" property="approvedPerEvaluated"/></td>
		</tr>
		<tr>
			<td class="listClasses-header"><bean:message key="message.courseInformation.approvedPerEnrolled" /></td>
			<td class="listClasses"><bean:write name="infoSiteCourseInformation" property="approvedPerEnrolled"/></td>
		</tr>
	</table>--%>
	<br />
	<p class="infoop"><span class="emphasis-box">4</span>
	<bean:message key="message.courseInformation.courseObjectives" /></p>
	<table border="0" cellspacing="1" style="margin-top:10px">
		<logic:iterate id="infoCurriculum" name="infoSiteCourseInformation" property="infoCurriculums">
			<logic:present name="infoExecutionDegree">
				<bean:define id="degreeCurricularPlanId" name="infoExecutionDegree" property="infoDegreeCurricularPlan.idInternal"/>
				<logic:equal name="infoCurriculum" 
	  						 property="infoCurricularCourse.infoDegreeCurricularPlan.idInternal" 
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
					<bean:define id="degreeCurricularPlanId" name="infoExecutionDegree" property="infoDegreeCurricularPlan.idInternal"/>
					<logic:equal name="infoCurriculum" 
	  						 	property="infoCurricularCourse.infoDegreeCurricularPlan.idInternal" 
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
	<table border="0" cellspacing="1" style="margin-top:10px" width="100%">
		<tr>
			<td class="listClasses-header" colspan="4"><bean:message key="message.courseInformation.coursePrincipalBibliographicReference" /></td>
		</tr>
		<logic:iterate id="infoBibliographicReference" name="infoSiteCourseInformation" property="infoBibliographicReferences">
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
	<table border="0" cellspacing="1" style="margin-top:10px" width="100%">
		<tr>
			<td class="listClasses-header" colspan="4"><bean:message key="message.courseInformation.courseSecondaryBibliographicReference" /></td>
		</tr>
		<logic:iterate id="infoBibliographicReference" name="infoSiteCourseInformation" property="infoBibliographicReferences">
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
			<td><bean:write name="infoSiteCourseInformation" property="infoEvaluationMethod.evaluationElements" filter="false"/></td>
		</tr>
	</table>
	<br />
	<p class="infoop"><span class="emphasis-box">8</span>
	<bean:message key="message.courseInformation.courseSupportLessons" /></p>
	<table border="0" cellspacing="1" style="margin-top:10px">
		<tr>
			<%--<td> <bean:write name="infoSiteCourseInformation" property="courseSupportLessons"/></td>--%>
		</tr>
	</table>
	<br/>
	<p class="infoop"><span class="emphasis-box">9</span>
	<bean:message key="message.courseInformation.courseReport" /></p>
	<br />
	Informação ainda não disponível
<%--	<table width="100%" cellpadding="0" cellspacing="0" style="margin-top:10px">
		<tr>
			<td><bean:write name="infoSiteCourseInformation" property="infoCourseReport.report"/></td> 
		</tr>
	</table>--%>
</logic:present>
