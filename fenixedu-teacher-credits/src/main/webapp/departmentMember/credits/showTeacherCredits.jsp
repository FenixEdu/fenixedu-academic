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
<%@page import="org.fenixedu.academic.domain.degreeStructure.RegimeType"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<bean:define id="hoursPattern" value="HH : mm"/>
<bean:define id="datePattern" value="dd-MM-yyyy"/>

<em><bean:message key="label.teacherServiceDistribution.departmentName"/></em>
<h2><bean:message key="label.teacherService.credits"/></h2>

<bean:define id="executionPeriodId" name="executionPeriod" property="externalId" />
<bean:define id="teacherId" name="teacher" property="externalId" />

<bean:define id="link">
	/showAllTeacherCreditsResume.do?method=showTeacherCreditsResume&amp;teacherId=<bean:write name="teacherId"/>
</bean:define>

<bean:define id="linkToPrintSchedules">
	/schedulesPrint.do?method=showSchedulesPrint&amp;teacherId=<bean:write name="teacherId"/>&amp;executionPeriodId=<bean:write name="executionPeriodId"/>
</bean:define>

<html:messages id="message" message="true">
	<p>
		<span class="error"><!-- Error messages go here -->
			<bean:write name="message"/>
		</span>
	</p>
</html:messages>

<div class="infoop2">
	<p class="mvert05"><b><bean:message key="label.teacher.name.short"  bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>:</b> <bean:write name="teacher" property="person.name"/></p>
	<p class="mvert05"><b><bean:message key="label.teacher.id.short" arg0="<%=org.fenixedu.academic.domain.organizationalStructure.Unit.getInstitutionAcronym()%>" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>:</b> <bean:write name="teacher" property="teacherId"/></p>
	<logic:notEmpty name="teacherCategory">
		<p class="mvert05"><b><bean:message key="label.category" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>:</b> <bean:write name="teacherCategory"/></p>
	</logic:notEmpty>	
	<p class="mvert05"><b><bean:message key="label.execution-period" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>:</b> <bean:write name="executionPeriod" property="name"/> - <bean:write name="executionPeriod" property="executionYear.year"/></p>
</div>

<ul class="mvert1">
	<li>
		<html:link page='<%= link %>'>
			<bean:message key="link.back"/>
		</html:link>
	</li>
	<li>
		<html:link page='<%= linkToPrintSchedules %>' target="_blank">
			<bean:message key="link.schedules.print"/>
		</html:link>
	</li>
</ul>

<%--
<tiles:insert definition="creditsResumeWithDescription">
	<tiles:put name="creditLineDTO" beanName="creditLineDTO"/>
</tiles:insert>
 --%>
 
<%-- ========================== PROFESSOR SHIPS ========================================== --%>

<h3 class="mtop2 separator2"><span>1) <bean:message key="label.teacherCreditsSheet.professorships.graduation" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></span></h3>

<logic:notEmpty name="professorships">
	<logic:iterate id="professorship" name="professorships">
		<p class="mtop1">
			<h4 style="display:inline">
				<span class="bluetxt">		
					<bean:write name="professorship" property="executionCourse.nome"/>				
					(<bean:write name="professorship" property="degreeSiglas"/>)
				</span>
			</h4>
		</p>
<%-- ========================= DEGREE TEACHING SERVICES ========================== --%>
		<bean:define id="professorshipID" name="professorship" property="externalId"/>

		<bean:define id="degreeTeachingServices" name="professorship" property="degreeTeachingServicesOrderedByShift"/>
		
		<p class="mbottom0"><b><bean:message key="label.teacherCreditsSheet.shiftProfessorships" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>:</b></p>
			
		<logic:notEmpty name="degreeTeachingServices">
			<table class="tstyle4 mbottom05">
				<tr>
					<th rowspan="2"><bean:message key="label.shift" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
					<th rowspan="2"><bean:message key="label.shift.type" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
					<th colspan="4"><bean:message key="label.lessons" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
					<th rowspan="2"><bean:message key="label.professorship.percentage" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
				</tr>
				<tr>
					<th><bean:message key="label.day.of.week" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
					<th><bean:message key="label.lesson.start" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
					<th><bean:message key="label.lesson.end" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
					<th><bean:message key="label.lesson.room" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>			
				</tr> 
				<logic:iterate id="degreeTeachingService" name="degreeTeachingServices" >
					<bean:define id="lessonList" name="degreeTeachingService" property="shift.lessonsOrderedByWeekDayAndStartTime"/>
					<bean:size id="numberOfLessons" name="lessonList"/>
	
					<logic:equal name="numberOfLessons" value="0">
						<tr>
							<td><bean:write name="degreeTeachingService" property="shift.nome"/></td>
							<td><bean:write name="degreeTeachingService" property="shift.shiftTypesCodePrettyPrint"/></td>
							<td colspan="7"> <bean:message key="label.shift.noLessons" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></td>
						</tr>
					</logic:equal>
	
					<logic:notEqual name="numberOfLessons" value="0">
						<logic:iterate id="lesson" name="lessonList" indexId="indexLessons">
							<logic:equal name="indexLessons" value="0">
								<tr>
									<td rowspan="<%= numberOfLessons %>">
										<bean:write name="degreeTeachingService" property="shift.nome"/>
									</td>
									<td rowspan="<%= numberOfLessons %>">
										<bean:write name="degreeTeachingService" property="shift.shiftTypesPrettyPrint"/>
									</td>
									<td>
										<bean:write name="lesson" property="diaSemana"/>
									</td>
									<td>
										<dt:format patternId="hoursPattern">
											<bean:write name="lesson" property="inicio.timeInMillis"/>
										</dt:format>
									</td>
									<td>
										<dt:format patternId="hoursPattern">
											<bean:write name="lesson" property="fim.timeInMillis"/>
										</dt:format>
									</td>
									<td>
										<logic:notEmpty name="lesson" property="sala">
											<bean:write name="lesson" property="sala.name"/>
										</logic:notEmpty>
										<logic:empty name="lesson" property="sala">
											-
										</logic:empty>
									</td>
									<td style="text-align: right;" rowspan="<%= numberOfLessons %>">
										<bean:define id="teachingServicePercentage" name="degreeTeachingService" property="percentage"/>
										<%= ((Math.round(((Double)teachingServicePercentage).doubleValue() * 100.0)) / 100.0) %>
									</td>
								</tr>
							</logic:equal>
							<logic:notEqual name="indexLessons" value="0">
								<tr>
									<td>
										<bean:write name="lesson" property="diaSemana"/>
									</td>
									<td>
										<dt:format patternId="hoursPattern">
											<bean:write name="lesson" property="inicio.timeInMillis"/>
										</dt:format>
									</td>
									<td>
										<dt:format patternId="hoursPattern">
											<bean:write name="lesson" property="fim.timeInMillis"/>
										</dt:format>
									</td>
									<td>
										<logic:notEmpty name="lesson" property="sala">
											<bean:write name="lesson" property="sala.name"/>
										</logic:notEmpty>
										<logic:empty name="lesson" property="sala">
											-
										</logic:empty>
									</td>
								</tr>
							</logic:notEqual>
						</logic:iterate>
					</logic:notEqual>
				</logic:iterate>
			</table>					
		</logic:notEmpty>
		<logic:empty name="degreeTeachingServices">
			<table class="tstyle4 mbottom05">
				<tr>
					<td colspan="7"> 
						<i><bean:message key="label.teacherCreditsSheet.noLessons" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></i>						
					</td>
				</tr>
			</table>
	    </logic:empty>	
	    <logic:equal name="showLinks" value="true">
			<p class="mtop0 pleft1">
				<html:link page='<%= "/degreeTeachingServiceManagement.do?page=0&amp;method=showTeachingServiceDetails&amp;professorshipID="+ professorshipID + "&amp;executionPeriodId="+ executionPeriodId %>'>
					<bean:message key="link.change" />
				</html:link>															
			</p>						
		</logic:equal>
<%-- ================================================================================== --%>
<%-- ================================= SUPPORT LESSONS ================================ --%>
		<bean:define id="supportLessonList" name="professorship" property="supportLessonsOrderedByStartTimeAndWeekDay"/>
		<p class="mbottom0"><b><bean:message key="label.teacherCreditsSheet.supportLessons" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>:</b></p>				
		<logic:notEmpty name="supportLessonList">
			<table class="tstyle4 mbottom05">
				<tr>
					<th><bean:message key="label.support-lesson.weekday" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>			
					<th><bean:message key="label.support-lesson.start-time" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>						
					<th><bean:message key="label.support-lesson.end-time" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>									
					<th><bean:message key="label.support-lesson.place" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>												
				</tr>
				<logic:iterate id="supportLesson" name="supportLessonList">
					<tr>
						<td>
							<bean:write name="supportLesson" property="weekDay"/>
						</td>			
						<td>
							<dt:format patternId="hoursPattern">
								<bean:write name="supportLesson" property="startTime.time"/>
							</dt:format>
						</td>			
						<td>
							<dt:format patternId="hoursPattern">
								<bean:write name="supportLesson" property="endTime.time"/>
							</dt:format>
						</td>			
						<td>
							<bean:write name="supportLesson" property="place"/>
						</td>			
					</tr>
				</logic:iterate>
			</table>		
			</logic:notEmpty>
			<logic:empty name="supportLessonList">
				<table class="tstyle4 mbottom05">
					<tr>
						<td colspan="4">
							<i><bean:message key="label.teacherCreditsSheet.noSupportLessons" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></i>			
						</td>
					</tr>
				</table>
			</logic:empty>			
			<logic:equal name="showLinks" value="true">
				<p class="mtop0 pleft1">
					<html:link page='<%= "/supportLessonsManagement.do?page=0&amp;method=showSupportLessons&amp;professorshipID="+ professorshipID + "&amp;executionPeriodId="+ executionPeriodId %>'>
							<bean:message key="link.change" />
					</html:link>
				</p>	
			</logic:equal>
	</logic:iterate>
</logic:notEmpty>
<logic:empty name="professorshipDTOs">
	<table class="tstyle4 mbottom05">
		<tr>
			<td colspan="2">
				<i><bean:message key="label.teacherCreditsSheet.noProfessorships" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></i>
			</td>
		</tr>	
	</table>
</logic:empty>
<%-- ================================================================================== --%>

<%-- ========================== MASTER DEGREE PROFESSORSHIPS =============================== --%>
<h3 class="mtop2 separator2"><span>2) <bean:message key="label.teacherCreditsSheet.professorships.postGraduation" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></span></h3>

<logic:notEmpty name="masterDegreeServices">
	<table class="tstyle4 mbottom05">
		<tr>
			<th>
				<bean:message key="label.masterDegree.curricularPlans" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
			</th>
			<th>
				<bean:message key="label.masterDegree.curricularCourse" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
			</th>
			<th>
				<bean:message key="label.hours" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
			</th>
			<th>
				<bean:message key="label.masterDegree.credits" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
			</th>				
		</tr>			
		<logic:iterate id="masterDegreeService" name="masterDegreeServices">
			<tr>
				<td>										  
					<logic:iterate id="curricularCourse" name="masterDegreeService" property="professorship.executionCourse.associatedCurricularCourses">
						<bean:write name="curricularCourse" property="degreeCurricularPlan.name"/> 
					</logic:iterate>
				</td>
				<td style="text-align:left">
					<bean:write name="masterDegreeService" property="professorship.executionCourse.nome"/>
				</td>
				<td>
					<logic:empty name="masterDegreeService" property="hours">
						0
					</logic:empty>
					<logic:notEmpty name="masterDegreeService" property="hours">
						<bean:write name="masterDegreeService" property="hours"/>
					</logic:notEmpty>
				</td>
				<td>
					<logic:empty name="masterDegreeService" property="credits">
						0
					</logic:empty>
					<logic:notEmpty name="masterDegreeService" property="credits">
						<bean:write name="masterDegreeService" property="credits"/>
					</logic:notEmpty>
				</td>
			</tr>				
		</logic:iterate>
	</table>
</logic:notEmpty>

<logic:empty name="masterDegreeServices">
	<table class="tstyle4 mbottom05">
		<tr>
			<td colspan="2">
				<i><bean:message key="label.teacherCreditsSheet.noMasterDegreeProfessorships" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></i>		
			</td>
		</tr>
	</table>
</logic:empty>

<logic:notEmpty name="teacherServiceNotes">		
	<logic:notEmpty name="teacherServiceNotes" property="masterDegreeTeachingNotes">		
		<table class="tstyle4 mbottom05">
				<tr>
					<th><bean:message key="label.notes" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
				</tr>
				<tr>
					<td>
						<bean:define id="masterDegreeTeachingNotesAux" name="teacherServiceNotes" property="masterDegreeTeachingNotes" />
						<%= masterDegreeTeachingNotesAux.toString().replaceAll("(\r\n)|(\n)", "<br />") %>
					</td>
				</tr>
		</table>
	</logic:notEmpty>
</logic:notEmpty>	
<logic:equal name="showLinks" value="true">
	<p class="mtop0 pleft1">
		<html:link page='<%= "/manageCreditsNotes.do?method=viewNote&amp;noteType=masterDegreeTeachingNote&amp;page=0" + "&amp;executionPeriodId=" + executionPeriodId %>' paramId="teacherId" paramName="teacherId">
				<bean:message key="link.notes" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
		</html:link>
	</p>
</logic:equal>
<%-- ================================================================================== --%>

<%-- ============================== ADVISES TFC ======================================= --%>
<h3 class="mtop2 separator2"><span>3) <bean:message key="label.teacherCreditsSheet.degreeFinalProjectStudents" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
<bean:message key="label.teacherCreditsSheet.degreeFinalProjectStudents.expirationWarning" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></span></h3>
<p class="mbottom0"><strong><bean:message key="label.teacherCreditsSheet.degreeFinalProjectStudents.items" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>:</strong></p>

<logic:notEmpty name="adviseServices">
	<table class="tstyle4 mbottom05">
			<tr>
				<th>
					<bean:message key="label.teacher-dfp-student.student-number" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
				</th>
				<th>
					<bean:message key="label.teacher-dfp-student.student-name" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
				</th>
				<th>
					<bean:message key="label.teacher-dfp-student.percentage" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
				</th>
			</tr>			
			<logic:iterate id="adviseService" name="adviseServices">
				<tr>
					<td>
						<bean:write name="adviseService" property="advise.student.number"/>
					</td>
					<td style="text-align:left">
						<bean:write name="adviseService" property="advise.student.person.name"/>
					</td>
					<td>
						<bean:write name="adviseService" property="percentage"/>
					</td>
				</tr>				
			</logic:iterate>
		</table>		
</logic:notEmpty>
<logic:empty name="adviseServices">
	<table class="tstyle4 mbottom05">
		<tr>
			<td colspan="2">
				<i><bean:message key="label.teacherCreditsSheet.noDegreeFinalProjectStudents" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></i>		
			</td>
		</tr>
	</table>
</logic:empty>
<logic:equal name="showLinks" value="true">
	<p class="mtop0 pleft1">
		<html:link page='<%= "/teacherAdviseServiceManagement.do?method=showTeacherAdvises&amp;page=0" + "&amp;executionPeriodId=" + executionPeriodId %>' paramId="teacherId" paramName="teacherId">
				<bean:message key="link.change"/>
		</html:link>
	</p>
</logic:equal>

<%-- ======================================================================================== --%>
<%-- ========================================= THESIS ======================================= --%>

<h3 class="mtop2 separator2"><span>4) <bean:message key="label.teacherCreditsSheet.thesis" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></span></h3>

<logic:present name="teacherThesisEvaluationParticipants">
	<table class="tstyle4 mbottom05">
			<tr>
				<th>
					<bean:message key="label.teacher-thesis-student.student-number" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
				</th>
				<th>
					<bean:message key="label.teacher-thesis-student.student-name" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
				</th>
				<th>
					<bean:message key="label.teacher-thesis-student.title" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
				</th>
				<th>
					<bean:message key="label.teacher-thesis-student.percentage" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
				</th>
				<th>
					<bean:message key="label.teacher-thesis-student.function" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
				</th>
				<th>
					<bean:message key="label.type" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
				</th>
			</tr>			
			<logic:iterate id="participant" name="teacherThesisEvaluationParticipants"  type="org.fenixedu.academic.domain.thesis.ThesisEvaluationParticipant">
				<tr>
					<td>
						<bean:write name="participant" property="thesis.student.number"/>
					</td>
					<td>
						<bean:write name="participant" property="thesis.student.person.name"/>
					</td>
					<td>
						<bean:write name="participant" property="thesis.title"/>
					</td>
					<td>
						<fmt:formatNumber maxFractionDigits="1" minFractionDigits="1" pattern="###.#">
							<bean:write name="participant" property="creditsDistribution"/>
						</fmt:formatNumber>
					</td>
					<td>
						<bean:message name="participant" property="type.name" bundle="ENUMERATION_RESOURCES"/>
					</td>
					<td>
						<%
							if (participant.getThesis().isAnual()) {
						%>
							<%= RegimeType.ANUAL.getLocalizedName() %>
						<%
							} else {
						%>
							<%= RegimeType.SEMESTRIAL.getLocalizedName() %>
						<%
							}
						%>
					</td>
				</tr>				
			</logic:iterate>
		</table>
</logic:present>

<logic:notPresent name="teacherThesisEvaluationParticipants">
	<table class="tstyle4 mbottom05">
		<tr>
			<td colspan="3">
				<i><bean:message key="label.teacherCreditsSheet.noThesis" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></i>			
			</td>
		</tr>
	</table>
</logic:notPresent>

<logic:notEmpty name="teacherServiceNotes">
	<logic:notEmpty name="teacherServiceNotes" property="thesisNote">
		<table class="tstyle4 mbottom05">
				<tr>
					<th><bean:message key="label.notes" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
				</tr>
				<tr>
					<td>
						<bean:define id="thesisNoteAux" name="teacherServiceNotes" property="thesisNote" />
						<%= thesisNoteAux.toString().replaceAll("(\r\n)|(\n)", "<br />") %>
					</td>
				</tr>
		</table>
	</logic:notEmpty>
</logic:notEmpty>

<logic:equal name="showLinks" value="true">
	<p class="mtop0 pleft1">
		<html:link page='<%= "/manageCreditsNotes.do?method=viewNote&amp;noteType=thesisNote&amp;page=0" + "&amp;executionPeriodId=" + executionPeriodId %>' paramId="teacherId" paramName="teacherId">
			<bean:message key="link.notes" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
		</html:link>
	</p>
</logic:equal>

<%-- ========================== TEACHER INSTITUTION WORKING TIME ============================ --%>
<h3 class="mtop2 separator2"><span>5) <bean:message key="label.teacherCreditsSheet.institutionWorkingTime" arg0="<%=org.fenixedu.academic.domain.organizationalStructure.Unit.getInstitutionAcronym()%>" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></span></h3>

<p class="mbottom0"><strong><bean:message key="label.teacherCreditsSheet.institutionWorkingTime.items" arg0="<%=org.fenixedu.academic.domain.organizationalStructure.Unit.getInstitutionAcronym()%>" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>:</strong></p>

<logic:present name="institutionWorkTimeList">
	<table class="tstyle4 mbottom05">
		<tr>
			<th><bean:message key="label.teacher-institution-working-time.weekday" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>			
			<th><bean:message key="label.teacher-institution-working-time.start-time" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>						
			<th><bean:message key="label.teacher-institution-working-time.end-time" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>									
		</tr>
		<logic:iterate id="institutionWorkTime" name="institutionWorkTimeList">
			<tr>
				<td>
					<bean:message name="institutionWorkTime" property="weekDay.name" bundle="ENUMERATION_RESOURCES"/>
				</td>			
				<td>
					<dt:format patternId="hoursPattern">
						<bean:write name="institutionWorkTime" property="startTime.time"/>
					</dt:format>
				</td>			
				<td>
					<dt:format patternId="hoursPattern">
						<bean:write name="institutionWorkTime" property="endTime.time"/>
					</dt:format>
				</td>			
			</tr> 
		</logic:iterate>
	</table>	
</logic:present>
<logic:notPresent name="institutionWorkTimeList">
	<table class="tstyle4 mbottom05">
		<tr>
			<td colspan="3">
				<i><bean:message key="label.teacherCreditsSheet.noInstitutionWorkingTime" arg0="<%=org.fenixedu.academic.domain.organizationalStructure.Unit.getInstitutionAcronym()%>" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></i>			
			</td>
		</tr>
	</table>
</logic:notPresent>
<logic:equal name="showLinks" value="true">
	<p class="mtop0 pleft1">
		<html:link page='<%= "/institutionWorkingTimeManagement.do?method=showTeacherWorkingTimePeriods&amp;page=0" + "&amp;executionPeriodId=" + executionPeriodId %>' paramId="teacherId" paramName="teacherId">
					<bean:message key="link.change"/>
		</html:link>
	</p>
</logic:equal>
<%-- ================================================================================== --%>
<%-- ========================== FUNCTIONS_ACCUMULATING ================================ --%>
<h3 class="mtop2 separator2"><span>6) <bean:message key="label.teacherCreditsSheet.functionsAccumulation" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></span></h3>

<p class="mbottom0"><strong><bean:message key="label.teacherCreditsSheet.functionsAccumulation" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>:</strong></p>
	
<logic:notEmpty name="teacherServiceNotes">
	<logic:notEmpty name="teacherServiceNotes" property="functionsAccumulation">
		<table class="tstyle4 mbottom05">
				<tr>
					<th><bean:message key="label.notes" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
				</tr>
				<tr>
					<td>
						<bean:define id="functionsAccumulationAux" name="teacherServiceNotes" property="functionsAccumulation" />
						<%= functionsAccumulationAux.toString().replaceAll("(\r\n)|(\n)", "<br />") %>
					</td>
				</tr>
		</table>
	</logic:notEmpty>
	<logic:empty name="teacherServiceNotes" property="functionsAccumulation">
		<table class="tstyle4 mbottom05">
			<tr>
				<td colspan="2"> 
					<i><bean:message key="message.functionsAccumulation.noRegists" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></i>						
				</td>
			</tr>
		</table>
	</logic:empty>
</logic:notEmpty>
<logic:empty name="teacherServiceNotes">	
	<table class="tstyle4 mbottom05">
		<tr>
			<td colspan="2"> 
				<i><bean:message key="message.functionsAccumulation.noRegists" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></i>						
			</td>
		</tr>
	</table>
</logic:empty>
<logic:equal name="showLinks" value="true">
	<p class="mtop0 pleft1">
		<html:link page='<%= "/manageCreditsNotes.do?method=viewNote&amp;noteType=functionsAccumulationNote&amp;page=0" + "&amp;executionPeriodId=" + executionPeriodId %>' paramId="teacherId" paramName="teacherId">
				<bean:message key="link.notes" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
		</html:link>
	</p>
</logic:equal>

<%-- ================================================================================== --%>
<%-- ========================== OTHER SERVICES CREDTIS ================================ --%>
<h3 class="mtop2 separator2"><span>7) <bean:message key="label.teacherCreditsSheet.otherTypeCreditLines" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></span></h3>

<p class="mbottom0"><strong><bean:message key="label.teacherCreditsSheet.otherTypeCreditLines" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>:</strong></p>
	

<logic:notEmpty name="otherServices">
	<table class="tstyle4 mbottom05">
		<tr>
			<th><bean:message key="label.otherTypeCreditLine.credits" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
			<th><bean:message key="label.otherTypeCreditLine.reason" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
		</tr>
		<logic:iterate id="otherService" name="otherServices" >
			<tr>
				<td>
					<bean:write name="otherService" property="credits"/>
				</td>
				<td style="text-align:left">
					<pre style="font-family: Verdana; font-size: 1em; margin: 0; padding: 0;"><bean:write name="otherService" property="reason"/></pre>
				</td>
			</tr>
		</logic:iterate>		
	</table>	
</logic:notEmpty>
<logic:empty name="otherServices">
	<table class="tstyle4 mbottom05">
		<tr>
			<td colspan="2"> 
				<i><bean:message key="message.otherTypeCreditLine.noRegists" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></i>						
			</td>
		</tr>
	</table>
</logic:empty>	

<logic:notEmpty name="teacherServiceNotes">
	<logic:notEmpty name="teacherServiceNotes" property="othersNotes">
		<table class="tstyle4 mbottom05">
				<tr>
					<th><bean:message key="label.notes" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
				</tr>
				<tr>
					<td>
						<bean:define id="othersNotesAux" name="teacherServiceNotes" property="othersNotes" />
						<%= othersNotesAux.toString().replaceAll("(\r\n)|(\n)", "<br />") %>
					</td>
				</tr>
		</table>
	</logic:notEmpty>
</logic:notEmpty>	
<logic:equal name="showLinks" value="true">
	<p class="mtop0 pleft1">
		<html:link page='<%= "/manageCreditsNotes.do?method=viewNote&amp;noteType=otherNote&amp;page=0" + "&amp;executionPeriodId=" + executionPeriodId %>' paramId="teacherId" paramName="teacherId">
				<bean:message key="link.notes" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
		</html:link>
	</p>
</logic:equal>
<%-- ========================== Management Position Lines =============================== --%>
<h3 class="mtop2 separator2"><span>8) <bean:message key="label.teacherCreditsSheet.managementPositionLines" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></span></h3>

<p class="mbottom0"><strong><bean:message key="label.teacherCreditsSheet.managementPositionLines" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>:</strong></p>
	
<logic:notEmpty name="personFunctions">
	<table class="tstyle4 mbottom05">
		<tr>
			<th><bean:message key="label.managementPosition.position" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
			<th><bean:message key="label.managementPosition.unit" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>										
			<th><bean:message key="label.managementPosition.credits" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>			
			<th><bean:message key="label.managementPosition.start" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
			<th><bean:message key="label.managementPosition.end" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
		</tr>
		<logic:iterate id="personFunction" name="personFunctions" >
			<tr>
				<td style="text-align:left">
					<bean:write name="personFunction" property="function.name"/>
				</td>
				<td style="text-align:left">
					<bean:define id="unit" name="personFunction" property="function.unit"/>
					<bean:write name="unit" property="presentationNameWithParents"/>										 
				</td>		
				<td>
					<bean:write name="personFunction" property="credits"/>
				</td>
				<td>
					<dt:format patternId="datePattern">
						<bean:write name="personFunction" property="beginDateInDateType.time"/>
					</dt:format>
				</td>
				<logic:notEmpty name="personFunction" property="endDate">
					<td>
						<dt:format patternId="datePattern">
							<bean:write name="personFunction" property="endDateInDateType.time"/>
						</dt:format>
					</td>
				</logic:notEmpty>
				<logic:empty name="personFunction" property="endDate">
				-			
				</logic:empty>
			</tr>			
		</logic:iterate>
	</table>	
</logic:notEmpty>
<logic:empty name="personFunctions">
	<table class="tstyle4 mbottom05">
		<tr>
			<td colspan="4"> 
				<i><bean:message key="message.managementPositions.noRegists" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></i>						
			</td>
		</tr>
	</table>
</logic:empty>

<logic:notEmpty name="teacherServiceNotes">
	<logic:notEmpty name="teacherServiceNotes" property="managementFunctionNotes">
		<table class="tstyle4 mbottom05">
				<tr>
					<th><bean:message key="label.notes" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
				</tr>
				<tr>
					<td>
						<bean:define id="managementFunctionNotesAux" name="teacherServiceNotes" property="managementFunctionNotes"/>
						<%= managementFunctionNotesAux.toString().replaceAll("(\r\n)|(\n)", "<br />") %>
					</td>
				</tr>
		</table>
	</logic:notEmpty>
</logic:notEmpty>	
<logic:equal name="showLinks" value="true">
	<p class="mtop0 pleft1">
		<html:link page='<%= "/manageCreditsNotes.do?method=viewNote&amp;noteType=managementFunctionNote&amp;page=0" + "&amp;executionPeriodId=" + executionPeriodId %>' paramId="teacherId" paramName="teacherId">
				<bean:message key="link.notes" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
		</html:link>
	</p>	
</logic:equal>

<%-- ============================ SERVICE EXEMPTIONS ================================= --%>
<h3 class="mtop2 separator2"><span>9) <bean:message key="label.teacherCreditsSheet.serviceExemptionLines" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></span></h3>

<p class="mbottom0"><strong><bean:message key="label.teacherCreditsSheet.serviceExemptionLines" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>:</strong></p>

<logic:notEmpty name="serviceExemptions">
	<table class="tstyle4 mbottom05">
		<tr>
			<th><bean:message key="label.serviceExemption.type" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
			<th><bean:message key="label.serviceExemption.start" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
			<th><bean:message key="label.serviceExemption.end" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
		</tr>
		<logic:iterate id="serviceExemption" name="serviceExemptions" >
			<tr>
				<td style="text-align:left">
					<bean:write name="serviceExemption" property="contractSituation.name.content"/>
				</td>
				<td><bean:write name="serviceExemption" property="beginDate"/></td>
				<td>
					<logic:notEmpty name="serviceExemption" property="serviceExemptionEndDate">
						<bean:write name="serviceExemption" property="serviceExemptionEndDate"/>
					</logic:notEmpty>
					<logic:empty name="serviceExemption" property="serviceExemptionEndDate">
					-
					</logic:empty>
				</td>
			</tr>
		</logic:iterate>
	</table>	
</logic:notEmpty>
<logic:empty name="serviceExemptions">
	<table class="tstyle4 mbottom05">
			<tr>
				<td colspan="3"> 
					<i><bean:message key="message.serviceExemptions.noRegists" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></i>						
				</td>
			</tr>
	</table>					
</logic:empty>

<logic:notEmpty name="teacherServiceNotes">
	<logic:notEmpty name="teacherServiceNotes" property="serviceExemptionNotes">
		<table class="tstyle4 mbottom05">
				<tr>
					<th><bean:message key="label.notes" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
				</tr>
				<tr>
					<td>
						<bean:define id="serviceExemptionNotesAux" name="teacherServiceNotes" property="serviceExemptionNotes"/>
						<%= serviceExemptionNotesAux.toString().replaceAll("(\r\n)|(\n)", "<br />") %>
					</td>
				</tr>
		</table>
	</logic:notEmpty>
</logic:notEmpty>	
<logic:equal name="showLinks" value="true">
	<p class="mtop0 pleft1">
		<html:link page='<%= "/manageCreditsNotes.do?method=viewNote&amp;noteType=serviceExemptionNote&amp;page=0" + "&amp;executionPeriodId=" + executionPeriodId %>' paramId="teacherId" paramName="teacherId">
				<bean:message key="link.notes" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
		</html:link>
	</p>
</logic:equal>
<%--
<br/>
<html:link page='<%= link %>'>
<ul>
	<li><bean:message key="link.back"/></li>
</ul>
</html:link>
--%>

<%-- ================================================================================== --%>
