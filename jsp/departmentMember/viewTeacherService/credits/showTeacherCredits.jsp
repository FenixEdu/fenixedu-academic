<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<bean:define id="hoursPattern" value="HH : mm"/>
<bean:define id="datePattern" value="dd-MM-yyyy"/>


<bean:define id="executionPeriodId" name="executionPeriod" property="idInternal" />

<p class="infoselected">
	<b><bean:message key="label.teacher.name"  bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></b> <bean:write name="teacher" property="person.nome"/><br />
	<b><bean:message key="label.teacher.number" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></b> <bean:write name="teacher" property="teacherNumber"/> <br />
	<b><bean:message key="label.execution-period" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></b> <bean:write name="executionPeriod" property="name"/> - <bean:write name="executionPeriod" property="executionYear.year"/><br/>
</p>

<br/>
<b><bean:message key="label.credits.resume" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></b>:

<tiles:insert definition="creditsResumeLine">
	<tiles:put name="creditLineDTO" beanName="creditLineDTO"/>
</tiles:insert>

<br/>
<br/>
<u><strong><bean:message key="label.credits.legenda" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></strong></u>:
<p>
<bean:message key="label.credits.lessons.code" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/> - <bean:message key="label.credits.lessons.code.definition" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>,&nbsp;
<bean:message key="label.credits.supportLessons.code" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/> - <bean:message key="label.credits.supportLessons.code.explanation" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>,&nbsp;
<bean:message key="label.credits.masterDegreeLessons.code" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/> - <bean:message key="label.credits.masterDegreeLessons.code.explanation" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>,&nbsp;
<bean:message key="label.credits.degreeFinalProjectStudents.code" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/> - <bean:message key="label.credits.degreeFinalProjectStudents.code.explanation" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>,&nbsp;
<bean:message key="label.credits.institutionWorkTime.code" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/> - <bean:message key="label.credits.institutionWorkTime.code.explanation" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>, &nbsp;
<bean:message key="label.credits.otherTypeCreditLine.code" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/> - <bean:message key="label.credits.otherTypeCreditLine.code.explanation" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>, &nbsp;
<bean:message key="label.credits.managementPositions.code" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/> - <bean:message key="label.credits.managementPositions.code.definition" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>, &nbsp;
<bean:message key="label.credits.serviceExemptionSituations.code" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/> - <bean:message key="label.credits.serviceExemptionSituations.code.definition" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>.
</p>

<br/>
<%-- ========================== PROFESSOR SHIPS ========================================== --%>
<h2>
	<span class="emphasis-box">1</span> <i><bean:message key="label.teacherCreditsSheet.professorships" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></i>
</h2>

<logic:notEmpty name="professorshipDTOs">
	<logic:iterate id="professorshipDTO" name="professorshipDTOs">
		<bean:define id="professorship" name="professorshipDTO" property="professorship"/>
		<h4 style="display:inline">
			<span class="bluetxt">		
				<bean:write name="professorship" property="executionCourse.nome"/>				
				<bean:size id="degreeSiglasSizeList" name="professorshipDTO" property="degreeSiglas"/>
				(<logic:iterate id="sigla" name="professorshipDTO" property="degreeSiglas" indexId="index">
					<bean:write name="sigla" /> 
					<logic:notEqual name="degreeSiglasSizeList" value="<%= String.valueOf(index.intValue() + 1) %>">
					,
					</logic:notEqual>
				</logic:iterate>)
			</span>
		</h4>
<%-- ========================= DEGREE TEACHING SERVICES ========================== --%>
		<bean:define id="professorshipID" name="professorship" property="idInternal"/>

		<bean:define id="degreeTeachingServices" name="professorship" property="degreeTeachingServices"/>
		<table width="100%" cellspacing="1" cellpadding="1" style="margin-bottom:0;margin-top:0">
			<tr>
				<td colspan="7" class="listClasses-subheader">
					<bean:message key="label.teacherCreditsSheet.shiftProfessorships" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>						
				</td>
			</tr>
			<logic:notEmpty name="degreeTeachingServices">
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
					<logic:iterate id="degreeTeachingService" name="degreeTeachingServices" >
						<bean:define id="lessonList" name="degreeTeachingService" property="shift.associatedLessons"/>
						<bean:size id="numberOfLessons" name="lessonList"/>
		
						<logic:equal name="numberOfLessons" value="0">
							<tr>
								<td class="listClasses"><bean:write name="degreeTeachingService" property="shift.nome"/></td>
								<td class="listClasses"><bean:write name="degreeTeachingService" property="shift.tipo.siglaTipoAula"/></td>
								<td class="listClasses" colspan="7"> <bean:message key="label.shift.noLessons" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></td>
							</tr>
						</logic:equal>
		
						<logic:notEqual name="numberOfLessons" value="0">
							<logic:iterate id="lesson" name="lessonList" indexId="indexLessons">
								<logic:equal name="indexLessons" value="0">
									<tr>
										<td class="listClasses" rowspan="<%= numberOfLessons %>">
											<bean:write name="degreeTeachingService" property="shift.nome"/>
										</td>
										<td class="listClasses" rowspan="<%= numberOfLessons %>">
											<bean:write name="degreeTeachingService" property="shift.tipo"/>
										</td>
										<td class="listClasses">
											<bean:write name="lesson" property="diaSemana"/>
										</td>
										<td class="listClasses">
											<dt:format patternId="hoursPattern">
												<bean:write name="lesson" property="inicio.timeInMillis"/>
											</dt:format>
										</td>
										<td class="listClasses">
											<dt:format patternId="hoursPattern">
												<bean:write name="lesson" property="fim.timeInMillis"/>
											</dt:format>
										</td>
										<td class="listClasses">
											<bean:write name="lesson" property="sala.nome"/>
										</td>
										<td class="listClasses"  rowspan="<%= numberOfLessons %>">
											<bean:write name="degreeTeachingService" property="percentage"/>
										</td>
									</tr>
								</logic:equal>
								<logic:notEqual name="indexLessons" value="0">
									<tr>
										<td class="listClasses">
											<bean:write name="lesson" property="diaSemana"/>
										</td>
										<td class="listClasses">
											<dt:format patternId="hoursPattern">
												<bean:write name="lesson" property="inicio.timeInMillis"/>
											</dt:format>
										</td>
										<td class="listClasses">
											<dt:format patternId="hoursPattern">
												<bean:write name="lesson" property="fim.timeInMillis"/>
											</dt:format>
										</td>
										<td class="listClasses">
											<bean:write name="lesson" property="sala.nome"/>
										</td>
									</tr>
								</logic:notEqual>
							</logic:iterate>
						</logic:notEqual>
					</logic:iterate>					
			</logic:notEmpty>
			<logic:empty name="degreeTeachingServices">
				<tr>
					<td colspan="7" class="listClasses"> 
						<i><bean:message key="label.teacherCreditsSheet.noLessons" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></i>						
					</td>
				</tr>
			</logic:empty>
		</table>	
						
<%-- ================================================================================== --%>
<%-- ================================= SUPPORT LESSONS ================================ --%>
		<bean:define id="supportLessonList" name="professorship" property="supportLessons"/>
		<table width="100%" style="margin-top:0; margin-bottom:20px">
			<tr>
				<td colspan="4" class="listClasses-subheader">
					<bean:message key="label.teacherCreditsSheet.supportLessons" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>									
				</td>
			</tr>
			<logic:notEmpty name="supportLessonList">
				<tr>
					<td class="listClasses-header"><bean:message key="label.support-lesson.weekday" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></td>			
					<td class="listClasses-header"><bean:message key="label.support-lesson.start-time" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></td>						
					<td class="listClasses-header"><bean:message key="label.support-lesson.end-time" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></td>									
					<td class="listClasses-header"><bean:message key="label.support-lesson.place" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></td>												
				</tr>
				<logic:iterate id="supportLesson" name="supportLessonList">
					<tr>
						<td class="listClasses">
							<bean:write name="supportLesson" property="weekDay"/>
						</td>			
						<td class="listClasses">
							<dt:format patternId="hoursPattern">
								<bean:write name="supportLesson" property="startTime.time"/>
							</dt:format>
						</td>			
						<td class="listClasses">
							<dt:format patternId="hoursPattern">
								<bean:write name="supportLesson" property="endTime.time"/>
							</dt:format>
						</td>			
						<td class="listClasses">
							<bean:write name="supportLesson" property="place"/>
						</td>			
					</tr>
				</logic:iterate>
	
			</logic:notEmpty>
			<logic:empty name="supportLessonList">
				<tr>
					<td colspan="4" class="listClasses">
						<i><bean:message key="label.teacherCreditsSheet.noSupportLessons" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></i>			
					</td>
				</tr>
			</logic:empty>
		</table>			
	</logic:iterate>
</logic:notEmpty>
<logic:empty name="professorshipDTOs">
	<i><bean:message key="label.teacherCreditsSheet.noProfessorships" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/><i>
</logic:empty>
<%-- ================================================================================== --%>

<%-- ========================== MASTER DEGREE PROFESSORSHIPS =============================== --%>
<h2>
<span class="emphasis-box">2</span> <i><bean:message key="label.teacherCreditsSheet.masterDegreeProfessorships" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></i>
</h2>

<table width="100%">
	<tr>
		<td colspan="4" class="listClasses-subheader">
			<bean:message key="label.teacherCreditsSheet.masterDegreeProfessorships" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>		
		</td>
	</tr>
	<logic:notEmpty name="masterDegreeServices">
			<tr>
				<td class="listClasses-header" width="20%">
					<bean:message key="label.masterDegree.curricularPlans" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
				</td>
				<td class="listClasses-header" style="text-align:left">
					<bean:message key="label.masterDegree.curricularCourse" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
				</td>
				<td class="listClasses-header">
					<bean:message key="label.hours" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
				</td>
				<td class="listClasses-header">
					<bean:message key="label.masterDegree.credits" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
				</td>				
			</tr>			
			<logic:iterate id="masterDegreeService" name="masterDegreeServices">
				<tr>
					<td class="listClasses">										  
						<logic:iterate id="curricularCourse" name="masterDegreeService" property="professorship.executionCourse.associatedCurricularCourses">
							<bean:write name="curricularCourse" property="degreeCurricularPlan.name"/> 
						</logic:iterate>
					</td>
					<td class="listClasses" style="text-align:left">
						<bean:write name="masterDegreeService" property="professorship.executionCourse.nome"/>
					</td>
					<td class="listClasses">
						<logic:empty name="masterDegreeService" property="hours">
							0
						</logic:empty>
						<logic:notEmpty name="masterDegreeService" property="hours">
							<bean:write name="masterDegreeService" property="hours"/>
						</logic:notEmpty>
					</td>
					<td class="listClasses">
						<logic:empty name="masterDegreeService" property="credits">
							0
						</logic:empty>
						<logic:notEmpty name="masterDegreeService" property="credits">
							<bean:write name="masterDegreeService" property="credits"/>
						</logic:notEmpty>
					</td>
				</tr>				
			</logic:iterate>
	</logic:notEmpty>
	<logic:empty name="masterDegreeServices">
		<tr>
			<td colspan="2" class="listClasses">
				<i><bean:message key="label.teacherCreditsSheet.noMasterDegreeProfessorships" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></i>		
			</td>
		</tr>
	</logic:empty>
</table>

<%-- ================================================================================== --%>

<%-- ============================== ADVISES TFC ======================================= --%>
<h2>
<span class="emphasis-box">3</span> <i><bean:message key="label.teacherCreditsSheet.degreeFinalProjectStudents" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></i>
</h2>

<table width="100%">
	<tr>
		<td colspan="3" class="listClasses-subheader">
			<bean:message key="label.teacherCreditsSheet.degreeFinalProjectStudents.items" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>		
		</td>
	</tr>
	<logic:notEmpty name="adviseServices">
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
			<logic:iterate id="adviseService" name="adviseServices">
				<tr>
					<td class="listClasses">
						<bean:write name="adviseService" property="advise.student.number"/>
					</td>
					<td class="listClasses" style="text-align:left">
						<bean:write name="adviseService" property="advise.student.person.nome"/>
					</td>
					<td class="listClasses">
						<bean:write name="adviseService" property="percentage"/>
					</td>
				</tr>				
			</logic:iterate>
	</logic:notEmpty>
	<logic:empty name="adviseServices">
		<tr>
			<td colspan="2" class="listClasses">
				<i><bean:message key="label.teacherCreditsSheet.noDegreeFinalProjectStudents" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></i>		
			</td>
		</tr>
	</logic:empty>
</table>
	
<%-- ======================================================================================== --%>

<%-- ========================== TEACHER INSTITUTION WORKING TIME ============================ --%>
<h2>
	<span class="emphasis-box">4</span> <i><bean:message key="label.teacherCreditsSheet.institutionWorkingTime" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></i>
</h2>

<table width="100%" border="0" cellspacing="1" cellpadding="1">
	<tr>
		<td colspan="3" class="listClasses-subheader">
			<bean:message key="label.teacherCreditsSheet.institutionWorkingTime.items" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>			
		</td>
	</tr>
	<logic:present name="institutionWorkTimeList">
		<tr>
			<td class="listClasses-header" width="20%"><bean:message key="label.teacher-institution-working-time.weekday" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></td>			
			<td class="listClasses-header" width="40%"><bean:message key="label.teacher-institution-working-time.start-time" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></td>						
			<td class="listClasses-header" width="40%"><bean:message key="label.teacher-institution-working-time.end-time" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></td>									
		</tr>
		<logic:iterate id="institutionWorkTime" name="institutionWorkTimeList">
			<tr>
				<td class="listClasses">
					<bean:message name="institutionWorkTime" property="weekDay.name" bundle="ENUMERATION_RESOURCES"/>
				</td>			
				<td class="listClasses">
					<dt:format patternId="hoursPattern">
						<bean:write name="institutionWorkTime" property="startTime.time"/>
					</dt:format>
				</td>			
				<td class="listClasses">
					<dt:format patternId="hoursPattern">
						<bean:write name="institutionWorkTime" property="endTime.time"/>
					</dt:format>
				</td>			
			</tr> 
		</logic:iterate>
	</logic:present>
	<logic:notPresent name="institutionWorkTimeList">
		<tr>
			<td class="listClasses" colspan="3">
				<i><bean:message key="label.teacherCreditsSheet.noInstitutionWorkingTime" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></i>			
			</td>
		</tr>
	</logic:notPresent>
</table>	

<%-- ================================================================================== --%>
<%-- ========================== OTHER SERVICES CREDTIS ================================ --%>
<h2>
<span class="emphasis-box">5</span> <i><bean:message key="label.teacherCreditsSheet.otherTypeCreditLines" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></i>
</h2>

<table width="100%" cellspacing="1" cellpadding="3" style="margin-bottom:0;margin-top:0">
	<tr>
		<td colspan="2" class="listClasses-subheader">
				<bean:message key="label.teacherCreditsSheet.otherTypeCreditLines" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>				
		</td>
	</tr>

	<logic:notEmpty name="otherServices">
		<tr>
			<td class="listClasses-header" width="10%"><bean:message key="label.otherTypeCreditLine.credits" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></td>
			<td class="listClasses-header" style="text-align:left"><bean:message key="label.otherTypeCreditLine.reason" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></td>
		</tr>
		<logic:iterate id="otherService" name="otherServices" >
			<tr>
				<td class="listClasses">
					<bean:write name="otherService" property="credits"/>
				</td>
				<td class="listClasses" style="text-align:left">
					<pre><bean:write name="otherService" property="reason"/></pre>
				</td>
			</tr>
		</logic:iterate>
	</logic:notEmpty>
	<logic:empty name="otherServices">
		<tr>
			<td colspan="2" class="listClasses"> 
				<i><bean:message key="message.otherTypeCreditLine.noRegists" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></i>						
			</td>
		</tr>
	</logic:empty>
</table>								

<%-- ========================== Management Position Lines =============================== --%>
<h2>
<span class="emphasis-box">6</span> <i><bean:message key="label.teacherCreditsSheet.managementPositionLines" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></i>
</h2>

<table width="100%" cellspacing="1" cellpadding="3" style="margin-bottom:0;margin-top:0">
	<tr>
		<td colspan="5" class="listClasses-subheader">
			<bean:message key="label.teacherCreditsSheet.managementPositionLines" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
		</td>
	</tr>

	<logic:notEmpty name="personFunctions">
		<tr>
			<td class="listClasses-header" style="text-align:left"><bean:message key="label.managementPosition.position" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></td>
			<td class="listClasses-header" style="text-align:left"><bean:message key="label.managementPosition.unit" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></td>										
			<td class="listClasses-header" width="10%"><bean:message key="label.managementPosition.credits" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></td>			
			<td class="listClasses-header" width="10%"><bean:message key="label.managementPosition.start" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></td>
			<td class="listClasses-header" width="10%"><bean:message key="label.managementPosition.end" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></td>
		</tr>
		<logic:iterate id="personFunction" name="personFunctions" >
			<tr>
				<td class="listClasses" style="text-align:left">
					<bean:write name="personFunction" property="function.name"/>
				</td>
				<td class="listClasses" style="text-align:left">
					<bean:define id="unit" name="personFunction" property="function.unit"/>
					<bean:write name="unit" property="name"/>
					<logic:notEmpty name="unit" property="topUnits">
						-
						<logic:iterate id="topUnit" name="unit" property="topUnits">
							<bean:write name="topUnit" property="name"/>,							
						</logic:iterate>								
					</logic:notEmpty>
				</td>		
				<td class="listClasses">
					<bean:write name="personFunction" property="credits"/>
				</td>
				<td class="listClasses">
					<dt:format patternId="datePattern">
						<bean:write name="personFunction" property="beginDateInDateType.time"/>
					</dt:format>
				</td>
				<logic:notEmpty name="personFunction" property="endDate">
					<td class="listClasses">
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
	</logic:notEmpty>
	<logic:empty name="personFunctions">
		<tr>
			<td colspan="4" class="listClasses"> 
				<i><bean:message key="message.managementPositions.noRegists" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></i>						
			</td>
		</tr>
	</logic:empty>
</table>								

<%-- ============================ SERVICE EXEMPTIONS ================================= --%>
<h2>
<span class="emphasis-box">7</span> <i><bean:message key="label.teacherCreditsSheet.serviceExemptionLines" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></i>
</h2>

<table width="100%" cellspacing="1" cellpadding="3" style="margin-bottom:0;margin-top:0">
	<tr>
		<td colspan="4" class="listClasses-subheader">
				<bean:message key="label.teacherCreditsSheet.serviceExemptionLines" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>			
		</td>
	</tr>

	<logic:notEmpty name="serviceExemptions">
		<tr>
			<td class="listClasses-header" style="text-align:left"><bean:message key="label.serviceExemption.type" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></td>
			<td class="listClasses-header" style="text-align:left"><bean:message key="label.serviceExemption.organization" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></td>			
			<td class="listClasses-header" width="10%"><bean:message key="label.serviceExemption.start" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></td>
			<td class="listClasses-header" width="10%"><bean:message key="label.serviceExemption.end" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></td>
		</tr>
		<logic:iterate id="serviceExemption" name="serviceExemptions" >
			<tr>
				<td class="listClasses" style="text-align:left">
					<bean:message name="serviceExemption" property="type.name" bundle="ENUMERATION_RESOURCES"/>
				</td>
				<td class="listClasses" style="text-align:left">
					<logic:notEmpty name="serviceExemption" property="institution">
						<bean:write name="serviceExemption" property="institution"/>
					</logic:notEmpty>
					<logic:empty name="serviceExemption" property="institution">
						-
					</logic:empty>					
				</td>				
				<td class="listClasses">
					<dt:format patternId="datePattern">
						<bean:write name="serviceExemption" property="start.time"/>
					</dt:format>
				</td>
				<td class="listClasses">
					<logic:notEmpty name="serviceExemption" property="end">
						<dt:format patternId="datePattern">
							<bean:write name="serviceExemption" property="end.time"/>
						</dt:format>
					</logic:notEmpty>
					<logic:empty name="serviceExemption" property="end">
					-
					</logic:empty>
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
