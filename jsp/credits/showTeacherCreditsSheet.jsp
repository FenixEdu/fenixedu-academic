<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>
<bean:define id="hoursPattern" value="HH : mm"/>

<bean:define id="infoTeacher" name="teacherCreditsSheet" property="infoTeacher"/>
<bean:define id="infoExecutionPeriod" name="teacherCreditsSheet" property="infoExecutionPeriod"/>
<bean:define id="executionPeriodId" name="infoExecutionPeriod" property="idInternal" />
<logic:present name="teacherCreditsSheet" property="infoCredits">
	<bean:define id="infoCredits" name="teacherCreditsSheet" property="infoCredits"/>
</logic:present>

<p class="infoselected">
	<b><bean:message key="label.teacher.name"  bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></b> <bean:write name="infoTeacher" property="infoPerson.nome"/><br />
	<bean:define id="teacherNumber" name="infoTeacher" property="teacherNumber"/>
	<b><bean:message key="label.teacher.number" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></b> <bean:write name="teacherNumber"/> <br />
	<b><bean:message key="label.execution-period" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></b> <bean:write name="infoExecutionPeriod" property="description"/>
</p>


<tiles:insert definition="creditsLegend"/>


<b><bean:message key="label.credits.resume" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></b>:
<tiles:insert definition="creditsResume">
	<tiles:put name="infoCredits" beanName="infoCredits"/>
</tiles:insert>
<br/>
<br/>
<%-- ========================== PROFESSOR SHIPS ========================================== --%>
<h2>
	<span class="emphasis-box">1</span> <i><bean:message key="label.teacherCreditsSheet.professorships" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></i>
</h2>


<bean:define id="detailedProfessorshipList" name="teacherCreditsSheet" property="detailedProfessorshipList"/>
<logic:notEmpty name="detailedProfessorshipList">
	<logic:iterate id="detailedProfessorship" name="detailedProfessorshipList">
		<bean:define id="infoProfessorship" name="detailedProfessorship" property="infoProfessorship"/>
		<h4>
			<span class="bluetxt">		
				<bean:write name="infoProfessorship" property="infoExecutionCourse.nome"/>
				<bean:define id="infoDegreeList" name="detailedProfessorship" property="infoDegreeList" />
				<bean:size id="degreeSizeList" name="infoDegreeList"/>
				(<logic:iterate id="infoDegree" name="infoDegreeList" indexId="index">
					<bean:write name="infoDegree" property="sigla" /> 
					<logic:notEqual name="degreeSizeList" value="<%= String.valueOf(index.intValue() + 1) %>">
					,
					</logic:notEqual>
				</logic:iterate>)
			</span>
		</h4>
		
<%-- ========================= EXECUTION COURSE SHIFT PROFESSORSHIP ========================== --%>
		<bean:define id="executionCourseId" name="infoProfessorship" property="infoExecutionCourse.idInternal"/>
		<bean:define id="teacherId" name="infoTeacher" property="idInternal"/>

		<bean:define id="executionCourseCode" name="infoProfessorship" property="infoExecutionCourse.sigla"/>
		<bean:define id="infoShiftProfessorshipList" name="teacherCreditsSheet" property='<%= "executionCourseShiftProfessorship(" + executionCourseCode + ")" %>'/>
		<table width="100%" cellspacing="1" cellpadding="1" style="margin-bottom:0;margin-top:0">
			<tr>
				<td colspan="7" class="listClasses-subheader">
					<bean:message key="label.teacherCreditsSheet.shiftProfessorships" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
						<logic:present role="role.department.credits.manager">
							(<html:link page='<%= "/manageTeacherShiftProfessorships.do?page=0&amp;method=showForm&amp;executionCourseId="+ executionCourseId + "&amp;teacherId=" + teacherId + "&amp;executionPeriodId=" + executionPeriodId %>'>
								<bean:message key="link.teacherCreditsTeacher.shiftProfessorship.management" />
							</html:link>)
						</logic:present>
				</td>
			</tr>

			<logic:notEmpty name="infoShiftProfessorshipList">
					<tr>
						<td rowspan="2" class="listClasses-header" width="10%"><bean:message key="label.shift" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></td>
						<td rowspan="2" class="listClasses-header" width="5%"><bean:message key="label.shift.type" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></td>
						<td colspan="4" class="listClasses-header" width="75%"><bean:message key="label.lessons" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></td>
						<td rowspan="2" class="listClasses-header" width="10%"><bean:message key="label.professorship.percentage" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></td>
					</tr>
					<tr>
						<td class="listClasses-header"><bean:message key="label.day.of.week" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></td>
						<td class="listClasses-header"><bean:message key="label.lesson.start" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></td>
						<td class="listClasses-header"><bean:message key="label.lesson.end" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></td>
						<td class="listClasses-header"><bean:message key="label.lesson.room" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></td>			
					</tr> 
					<logic:iterate id="infoShiftProfessorship" name="infoShiftProfessorshipList" >
						<bean:define id="infoLessonList" name="infoShiftProfessorship" property="infoShift.infoLessons"/>
						<bean:size id="numberOfLessons" name="infoLessonList"/>
		
						<logic:equal name="numberOfLessons" value="0">
							<tr>
								<td class="listClasses"><bean:write name="infoShiftProfessorship" property="infoShift.nome"/></td>
								<td class="listClasses"><bean:write name="infoShiftProfessorship" property="infoShift.tipo.siglaTipoAula"/></td>
								<td class="listClasses" colspan="7"> <bean:message key="label.shift.noLessons" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></td>
							</tr>
						</logic:equal>
		
						<logic:notEqual name="numberOfLessons" value="0">
							<logic:iterate id="infoLesson" name="infoLessonList" indexId="indexLessons">
								<logic:equal name="indexLessons" value="0">
									<tr>
										<td class="listClasses" rowspan="<%= numberOfLessons %>">
											<bean:write name="infoShiftProfessorship" property="infoShift.nome"/>
										</td>
										<td class="listClasses" rowspan="<%= numberOfLessons %>">
											<bean:write name="infoShiftProfessorship" property="infoShift.tipo"/>
										</td>
										<td class="listClasses">
											<bean:write name="infoLesson" property="diaSemana"/>
										</td>
										<td class="listClasses">
											<dt:format patternId="hoursPattern">
												<bean:write name="infoLesson" property="inicio.timeInMillis"/>
											</dt:format>
										</td>
										<td class="listClasses">
											<dt:format patternId="hoursPattern">
												<bean:write name="infoLesson" property="fim.timeInMillis"/>
											</dt:format>
										</td>
										<td class="listClasses">
											<bean:write name="infoLesson" property="infoSala.nome"/>
										</td>
										<td class="listClasses"  rowspan="<%= numberOfLessons %>">
											<bean:write name="infoShiftProfessorship" property="percentage"/>
										</td>
									</tr>
								</logic:equal>
								<logic:notEqual name="indexLessons" value="0">
									<tr>
										<td class="listClasses">
											<bean:write name="infoLesson" property="diaSemana"/>
										</td>
										<td class="listClasses">
											<dt:format patternId="hoursPattern">
												<bean:write name="infoLesson" property="inicio.timeInMillis"/>
											</dt:format>
										</td>
										<td class="listClasses">
											<dt:format patternId="hoursPattern">
												<bean:write name="infoLesson" property="fim.timeInMillis"/>
											</dt:format>
										</td>
										<td class="listClasses">
											<bean:write name="infoLesson" property="infoSala.nome"/>
										</td>
									</tr>
								</logic:notEqual>
							</logic:iterate>
						</logic:notEqual>
					</logic:iterate>					
			</logic:notEmpty>
			<logic:empty name="infoShiftProfessorshipList">
				<tr>
					<td colspan="7" class="listClasses"> 
						<i><bean:message key="label.teacherCreditsSheet.noLessons" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></i>						
					</td>
				</tr>
			</logic:empty>
		</table>								
<%-- ================================================================================== --%>
<%-- ========================= EXECUTION COURSE SUPPORT LESSONS ============================ --%>
		<bean:define id="infoSupportLessonList" name="teacherCreditsSheet" property='<%= "executionCourseSupportLessons(" + executionCourseCode + ")" %>' />
		<table width="100%" style="margin-top:0; margin-bottom:20px">
			<tr>
				<td colspan="4" class="listClasses-subheader">
					<bean:message key="label.teacherCreditsSheet.supportLessons" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>				
					<logic:present role="role.department.credits.manager">
						(<html:link page='<%= "/manageTeacherExecutionCourseSupportLessons.do?page=0&amp;method=showForm&amp;executionCourseId="+ executionCourseId + "&amp;teacherId=" + teacherId + "&amp;executionPeriodId=" + executionPeriodId %>'>
							<bean:message key="link.teacherCreditsTeacher.supportLessons.management" />
						</html:link>)
					</logic:present>
				</td>
			</tr>
			<logic:notEmpty name="infoSupportLessonList">
				<tr>
					<td class="listClasses-header"><bean:message key="label.support-lesson.weekday" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></td>			
					<td class="listClasses-header"><bean:message key="label.support-lesson.start-time" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></td>						
					<td class="listClasses-header"><bean:message key="label.support-lesson.end-time" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></td>									
					<td class="listClasses-header"><bean:message key="label.support-lesson.place" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></td>												
				</tr>
				<logic:iterate id="infoSupportLesson" name="infoSupportLessonList">
					<tr>
						<td class="listClasses">
							<bean:write name="infoSupportLesson" property="weekDay"/>
						</td>			
						<td class="listClasses">
							<dt:format patternId="hoursPattern">
								<bean:write name="infoSupportLesson" property="startTime.time"/>
							</dt:format>
						</td>			
						<td class="listClasses">
							<dt:format patternId="hoursPattern">
								<bean:write name="infoSupportLesson" property="endTime.time"/>
							</dt:format>
						</td>			
						<td class="listClasses">
							<bean:write name="infoSupportLesson" property="place"/>
						</td>			
					</tr>
				</logic:iterate>
	
			</logic:notEmpty>
			<logic:empty name="infoSupportLessonList">
				<tr>
					<td colspan="4" class="listClasses">
						<i><bean:message key="label.teacherCreditsSheet.noSupportLessons" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></i>			
					</td>
				</tr>
			</logic:empty>
		</table>			
	</logic:iterate>
</logic:notEmpty>
<logic:empty name="detailedProfessorshipList">
	<i><bean:message key="label.teacherCreditsSheet.noProfessorships" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/><i>
</logic:empty>
<%-- ================================================================================== --%>

<%-- ========================== DEGREE FINAL PROJECT STUDENTS ============================ --%>
<h2>
<span class="emphasis-box">2</span> <i><bean:message key="label.teacherCreditsSheet.degreeFinalProjectStudents" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></i>
</h2>
<bean:define id="teacherDfpStudentsList" name="teacherCreditsSheet" property="infoTeacherDegreeFinalProjectStudentList"/>

<table width="100%">
	<tr>
		<td colspan="3" class="listClasses-subheader">
			<bean:message key="label.teacherCreditsSheet.degreeFinalProjectStudents.items" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
			<logic:present role="role.department.credits.manager">
				(<html:link page='<%= "/manageTeacherDFPStudent.do?method=list&amp;page=0" + "&amp;executionPeriodId=" + executionPeriodId %>' paramId="teacherId" paramName="infoTeacher" paramProperty="idInternal">
					<bean:message key="link.teacherCreditsTeacher.manageDegreeFinalProjectStudents"/>
				</html:link>)
			</logic:present>
		</td>
	</tr>
	<logic:notEmpty name="teacherDfpStudentsList">
			<tr>
				<td class="listClasses-header" width="20%">
					<bean:message key="label.teacher-dfp-student.student-number" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
				</td>
				<td class="listClasses-header" style="text-align:left">
					<bean:message key="label.teacher-dfp-student.student-name" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
				</td>
				<td class="listClasses-header">
					<bean:message key="label.teacher-dfp-student.percentage" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
				</td>
			</tr>			
			<logic:iterate id="infoTeacherDfpStudent" name="teacherDfpStudentsList">
				<tr>
					<td class="listClasses">
						<bean:write name="infoTeacherDfpStudent" property="infoStudent.number"/>
					</td>
					<td class="listClasses" style="text-align:left">
						<bean:write name="infoTeacherDfpStudent" property="infoStudent.infoPerson.nome"/>
					</td>
					<td class="listClasses">
						<bean:write name="infoTeacherDfpStudent" property="percentage"/>
					</td>
				</tr>				
			</logic:iterate>
	</logic:notEmpty>
	<logic:empty name="teacherDfpStudentsList">
		<tr>
			<td colspan="2" class="listClasses">
				<i><bean:message key="label.teacherCreditsSheet.noDegreeFinalProjectStudents" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></i>		
			</td>
		</tr>
	</logic:empty>
</table>
	
<%-- ================================================================================== --%>

<%-- ========================== TEACHER INSTITUTION WORKING TIME ============================ --%>
<h2>
	<span class="emphasis-box">3</span> <i><bean:message key="label.teacherCreditsSheet.institutionWorkingTime" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></i>
</h2>

<bean:define id="teacherInstitutionWorkingTimeList" name="teacherCreditsSheet" property="infoTeacherInstitutionWorkingTimeList" /> 

<table width="100%" border="0" cellspacing="1" cellpadding="1">
	<tr>
		<td colspan="3" class="listClasses-subheader">
			<bean:message key="label.teacherCreditsSheet.institutionWorkingTime.items" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
			<logic:present role="role.department.credits.manager">
				(<html:link page='<%= "/manageTeacherInstitutionWorkingTime.do?method=list&amp;page=0" + "&amp;executionPeriodId=" + executionPeriodId %>' paramId="teacherId" paramName="infoTeacher" paramProperty="idInternal">
					<bean:message key="link.teacherCreditsTeacher.manageInstitutionWorkingTime"/>
				</html:link>)
			</logic:present>
		</td>
	</tr>
	<logic:notEmpty name="teacherInstitutionWorkingTimeList">
			<tr>
				<td class="listClasses-header" width="20%"><bean:message key="label.teacher-institution-working-time.weekday" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></td>			
				<td class="listClasses-header" width="40%"><bean:message key="label.teacher-institution-working-time.start-time" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></td>						
				<td class="listClasses-header" width="40%"><bean:message key="label.teacher-institution-working-time.end-time" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></td>									
			</tr>
			<logic:iterate id="infoTeacherInstitutionWorkingTime" name="teacherInstitutionWorkingTimeList">
				<tr>
					<td class="listClasses">
						<bean:write name="infoTeacherInstitutionWorkingTime" property="weekDay"/>
					</td>			
					<td class="listClasses">
						<dt:format patternId="hoursPattern">
							<bean:write name="infoTeacherInstitutionWorkingTime" property="startTime.time"/>
						</dt:format>
					</td>			
					<td class="listClasses">
						<dt:format patternId="hoursPattern">
							<bean:write name="infoTeacherInstitutionWorkingTime" property="endTime.time"/>
						</dt:format>
					</td>			
				</tr> 
			</logic:iterate>
	</logic:notEmpty>
	<logic:empty name="teacherInstitutionWorkingTimeList">
		<tr>
			<td class="listClasses" colspan="3">
				<i><bean:message key="label.teacherCreditsSheet.noInstitutionWorkingTime" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></i>			
			</td>
		</tr>
	</logic:empty>
</table>	

<%-- ================================================================================== --%>
<%-- ========================== Other Type Credit Lines =============================== --%>
<h2>
<span class="emphasis-box">4</span> <bean:message key="label.teacherCreditsSheet.otherTypeCreditLines" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
</h2>

<bean:define id="otherCreditLines" name="teacherCreditsSheet" property="infoTeacherOtherTypeCreditLineList"/>

<table width="100%" cellspacing="1" cellpadding="3" style="margin-bottom:0;margin-top:0">
	<tr>
		<td colspan="2" class="listClasses-subheader">
				<bean:message key="label.teacherCreditsSheet.otherTypeCreditLines" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
				<logic:present role="role.department.credits.manager">
					<bean:define id="teacherNumber" name="infoTeacher" property="teacherNumber"/>
					(<html:link page='<%= "/teacherSearchForOtherTypeCreditLine.do?page=0&amp;method=doSearch&amp;teacherNumber=" + teacherNumber + "&amp;executionPeriodId=" + executionPeriodId %>'>
						<bean:message key="link.teacherCreditsTeacher.shiftProfessorship.management" />
					</html:link>)
				</logic:present>
		</td>
	</tr>

	<logic:notEmpty name="otherCreditLines">
		<tr>
			<td class="listClasses-header" width="10%"><bean:message key="label.otherTypeCreditLine.credits" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></td>
			<td class="listClasses-header" style="text-align:left"><bean:message key="label.otherTypeCreditLine.reason" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></td>
		</tr>
		<logic:iterate id="creditLine" name="otherCreditLines" >
			<tr>
				<td class="listClasses">
					<bean:write name="creditLine" property="credits"/>
				</td>
				<td class="listClasses" style="text-align:left">
					<pre><bean:write name="creditLine" property="reason"/></pre>
				</td>
			</tr>
		</logic:iterate>
	</logic:notEmpty>
	<logic:empty name="otherCreditLines">
		<tr>
			<td colspan="7" class="listClasses"> 
				<i><bean:message key="message.otherTypeCreditLine.noRegists" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></i>						
			</td>
		</tr>
	</logic:empty>
</table>								

<%-- ========================== Management Position Lines =============================== --%>
<h2>
<span class="emphasis-box">5</span> <bean:message key="label.teacherCreditsSheet.managementPositionLines" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
</h2>

<bean:define id="managementPositions" name="teacherCreditsSheet" property="infoManagementPositions"/>

<table width="100%" cellspacing="1" cellpadding="3" style="margin-bottom:0;margin-top:0">
	<tr>
		<td colspan="2" class="listClasses-subheader">
				<bean:message key="label.teacherCreditsSheet.managementPositionLines" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
<%--				<logic:present role="role.credits.manager">
					<bean:define id="teacherNumber" name="infoTeacher" property="teacherNumber"/>
					(<html:link page='<%= "/teacherSearchForOtherTypeCreditLine.do?page=0&amp;method=doSearch&amp;teacherNumber=" + teacherNumber + "&amp;executionPeriodId=" + executionPeriodId %>'>
						<bean:message key="link.teacherCreditsTeacher.shiftProfessorship.management" />
					</html:link>)
				</logic:present> --%>
		</td>
	</tr>

	<logic:notEmpty name="managementPositions">
		<tr>
			<td class="listClasses-header" style="text-align:left"><bean:message key="label.managementPosition.position" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></td>
			<td class="listClasses-header" width="10%"><bean:message key="label.managementPosition.start" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></td>
			<td class="listClasses-header" width="10%"><bean:message key="label.managementPosition.end" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></td>
		</tr>
		<logic:iterate id="managementPosition" name="managementPositions" >
			<tr>
				<td class="listClasses" style="text-align:left">
					<bean:write name="managementPosition" property="position"/>
				</td>
				<td class="listClasses">
					<dt:format patternId="hoursPattern">
						<bean:write name="managementPosition" property="start.time"/>
					</dt:format>
				</td>
				<td class="listClasses">
					<dt:format patternId="hoursPattern">
						<bean:write name="managementPosition" property="end.time"/>
					</dt:format>
				</td>
			</tr>
		</logic:iterate>
	</logic:notEmpty>
	<logic:empty name="managementPositions">
		<tr>
			<td colspan="3" class="listClasses"> 
				<i><bean:message key="message.managementPositions.noRegists" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></i>						
			</td>
		</tr>
	</logic:empty>
</table>								

<%-- ========================== Service Exemption Lines =============================== --%>
<h2>
<span class="emphasis-box">6</span> <bean:message key="label.teacherCreditsSheet.serviceExemptionLines" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
</h2>

<bean:define id="serviceExemptions" name="teacherCreditsSheet" property="infoServiceExemptions"/>

<table width="100%" cellspacing="1" cellpadding="3" style="margin-bottom:0;margin-top:0">
	<tr>
		<td colspan="2" class="listClasses-subheader">
				<bean:message key="label.teacherCreditsSheet.serviceExemptionLines" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
<%--				<logic:present role="role.credits.manager">
					<bean:define id="teacherNumber" name="infoTeacher" property="teacherNumber"/>
					(<html:link page='<%= "/teacherSearchForOtherTypeCreditLine.do?page=0&amp;method=doSearch&amp;teacherNumber=" + teacherNumber + "&amp;executionPeriodId=" + executionPeriodId %>'>
						<bean:message key="link.teacherCreditsTeacher.shiftProfessorship.management" />
					</html:link>)
				</logic:present> --%>
		</td>
	</tr>

	<logic:notEmpty name="serviceExemptions">
		<tr>
			<td class="listClasses-header" style="text-align:left"><bean:message key="label.serviceExemption.type" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></td>
			<td class="listClasses-header" width="10%"><bean:message key="label.serviceExemption.start" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></td>
			<td class="listClasses-header" width="10%"><bean:message key="label.serviceExemption.end" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></td>
		</tr>
		<logic:iterate id="serviceExemption" name="serviceExemptions" >
			<tr>
				<td class="listClasses" style="text-align:left">
					<bean:message name="serviceExemption" property="type.value" bundle="ENUMERATION_RESOURCES"/>
				</td>
				<td class="listClasses">
					<dt:format patternId="hoursPattern">
						<bean:write name="serviceExemption" property="start.time"/>
					</dt:format>
				</td>
				<td class="listClasses">
					<dt:format patternId="hoursPattern">
						<bean:write name="serviceExemption" property="end.time"/>
					</dt:format>
				</td>
			</tr>
		</logic:iterate>
	</logic:notEmpty>
	<logic:empty name="serviceExemptions">
		<tr>
			<td colspan="3" class="listClasses"> 
				<i><bean:message key="message.serviceExemptions.noRegists" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></i>						
			</td>
		</tr>
	</logic:empty>
</table>								

<%-- ================================================================================== --%>

<%-- ========================== MASTER DEGREE PROFESSORSHIPS =============================== --%>
<%-- <h2>
<span class="emphasis-box">3</span> <bean:message key="label.teacherCreditsSheet.masterDegreeProfessorships"/>
</h2> --%>
<%-- ================================================================================== --%>
