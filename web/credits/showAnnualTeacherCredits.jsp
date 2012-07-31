<%@page contentType="text/html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<html:xhtml/>

<em><bean:message key="label.teacherService.credits"/></em>


<logic:present name="annualTeachingCreditsBean">
	<h3><bean:message key="label.teacherService.credits"/>&nbsp;<bean:write name="annualTeachingCreditsBean" property="executionYear.name"/></h3>

	<fr:view name="annualTeachingCreditsBean">
		<fr:schema bundle="TEACHER_CREDITS_SHEET_RESOURCES" type="net.sourceforge.fenixedu.domain.credits.util.AnnualTeachingCreditsBean">
			<fr:slot name="teacher.person.name" key="label.name"/>
			<fr:slot name="teacher.teacherId" key="label.teacher.id"/>
			<fr:slot name="professionalCategoryName" key="label.category" layout="null-as-label"/>
			<fr:slot name="departmentName" key="label.department" layout="null-as-label"/>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight thright mtop05 mbottom05"/>
    		<fr:property name="columnClasses" value="width12em,,"/>
		</fr:layout>
	</fr:view>
</logic:present>

<bean:define id="teacherId" name="annualTeachingCreditsBean" property="teacher.externalId"/>

<style>
	h2.separator2 {
		cursor:pointer;
		padding:10px;
	}
	h2.separator2 img {
		margin-right:2px;
	}
	h2.separator2.highlight {
		background:#eee9dd;
	}	
	.panel .mtop2 {
		margin:8px 0px 0px !important;
	}
	.inner-panel {
		display:none;
		margin:10px 0 50px;
	}
	#wrap .panel.first {
		margin-top:30px;
	}
</style>

<script language="Javascript" type="text/javascript">
<!--
$(document).ready(function() {
	$('h3.separator2').click(function () {
		$(this).toggleClass('highlight');
		$(this).parent().find('.inner-panel').toggle();
		if ($(this).hasClass('highlight')) {
			$(this).find('#status-icon').attr('src','<%= request.getContextPath() +"/images/down30.png" %>');
		} else {
			$(this).find('#status-icon').attr('src','<%= request.getContextPath() +"/images/right30.png" %>');
		}
	});
});
//-->
</script>

<bean:define id="executionYearOid" name="annualTeachingCreditsBean" property="executionYear.externalId"/>

<%--
<p><html:link page='<%= "/credits.do?method=recalculateCredits&amp;executionYearOid=" + executionYearOid + "&amp;teacherOid=" + teacherId %>'>
		recalcular
</html:link></p>
--%>		
<div class="panel first"><h3 class="separator2 mtop2"><img id="status-icon" width="15px" alt="" src="<%= request.getContextPath() +"/images/right30.png" %>">
<bean:message key="label.teacherCreditsSheet.professorships" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></h3>
	<div class="inner-panel" style="display: none;">
		<logic:iterate id="annualTeachingCreditsByPeriodBean" name="annualTeachingCreditsBean" property="annualTeachingCreditsByPeriodBeans">
		<h3 class="infoop"><bean:write name="annualTeachingCreditsByPeriodBean" property="executionPeriod.qualifiedName"/></h3>
			<bean:size id="totalNumberOfProfessorship" name="annualTeachingCreditsByPeriodBean" property="professorships"/>
			<logic:equal name="totalNumberOfProfessorship" value="0">
				<bean:message key="label.teacherCreditsSheet.noDataFound" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
			</logic:equal>
			<logic:notEqual name="totalNumberOfProfessorship" value="0">
				<table class="tstyle4 mbottom05 mbottom05">
				<tr>
						<th rowspan="2"><bean:message key="label.course" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
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
				
				<logic:iterate id="professorship" name="annualTeachingCreditsByPeriodBean" property="professorships">
					<bean:define id="professorshipID" name="professorship" property="idInternal"/>
					<bean:define id="executionPeriodId" name="professorship" property="executionCourse.executionPeriod.idInternal"/>
					<bean:define id="totalNumberOfLessons" name="professorship" property="allLessonsNumber"/>
					<bean:size id="numberOfShifts" name="professorship" property="degreeTeachingServicesOrderedByShift"/>
						<logic:iterate id="degreeTeachingService" name="professorship" property="degreeTeachingServicesOrderedByShift" indexId="indexShifts">
							<bean:size id="numberOfLessons" name="degreeTeachingService" property="shift.lessonsOrderedByWeekDayAndStartTime"/>
							<logic:iterate id="lesson" name="degreeTeachingService" property="shift.lessonsOrderedByWeekDayAndStartTime" indexId="indexLessons">
								<tr>
								<logic:equal name="indexLessons" value="0">
									<logic:equal name="indexShifts" value="0">
										<td rowspan="<%= totalNumberOfLessons%>">
										<bean:write name="professorship" property="executionCourse.name"/> (<bean:write name="professorship" property="degreeSiglas"/>)
										 <%--
										<html:link page='<%= "/degreeTeachingServiceManagement.do?page=0&amp;method=showTeachingServiceDetails&amp;professorshipID="+professorshipID + "&amp;executionPeriodId="+ executionPeriodId %>'>
											<bean:write name="professorship" property="executionCourse.name"/> (<bean:write name="professorship" property="degreeSiglas"/>)
										</html:link>
										 --%>
										</td>
									</logic:equal>							
									<td rowspan="<%= numberOfLessons %>"><bean:write name="degreeTeachingService" property="shift.nome"/></td>
									<td rowspan="<%= numberOfLessons %>"><bean:write name="degreeTeachingService" property="shift.shiftTypesPrettyPrint"/></td>
								</logic:equal>
								<td><bean:write name="lesson" property="diaSemana"/></td>
								<td><fr:view name="lesson" property="beginHourMinuteSecond"/></td>
								<td><fr:view name="lesson" property="endHourMinuteSecond"/></td>
								<td><logic:notEmpty name="lesson" property="sala">
									<bean:write name="lesson" property="sala.nome"/>
									</logic:notEmpty>
									<logic:empty name="lesson" property="sala">
										-
									</logic:empty>
								</td>
								<logic:equal name="indexLessons" value="0">
									<td style="text-align: right;" rowspan="<%= numberOfLessons %>">
										<bean:define id="teachingServicePercentage" name="degreeTeachingService" property="percentage"/>
										<%= ((Math.round(((Double)teachingServicePercentage).doubleValue() * 100.0)) / 100.0) %>
									</td>
								</logic:equal>
								</tr>
							</logic:iterate>
						</logic:iterate>
						<bean:size id="numberOfSupportLessons" name="professorship" property="supportLessonsOrderedByStartTimeAndWeekDay"/>
						<logic:iterate id="supportLesson" name="professorship" property="supportLessonsOrderedByStartTimeAndWeekDay" indexId="indexSupportLessons">
							<tr>
							<logic:equal name="indexSupportLessons" value="0">
								<td rowspan="<%= numberOfSupportLessons %>">-</td>
								<td rowspan="<%= numberOfSupportLessons %>"><bean:message key="label.supportLessons" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></td>
							</logic:equal>
							<td><bean:write name="supportLesson" property="weekDay"/></td>
							<td><fr:view name="supportLesson" property="startTimeHourMinuteSecond"/></td>
							<td><fr:view name="supportLesson" property="endTimeHourMinuteSecond"/></td>
							<td><bean:write name="supportLesson" property="place"/></td>
							<logic:equal name="indexSupportLessons" value="0">
								<td style="text-align: right;" rowspan="<%= numberOfSupportLessons %>">-</td>
							</logic:equal>
							</tr>
						</logic:iterate>
				</logic:iterate>
				</table>
			</logic:notEqual>
		</logic:iterate>
	</div>
</div>
<div class="panel"><h3 class="separator2 mtop2"><img id="status-icon" width="15px" alt="" src="<%= request.getContextPath() +"/images/right30.png" %>"> <bean:message key="label.teacherCreditsSheet.institutionWorkingTime" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></h3>
	<div class="inner-panel" style="display: none;">
		<logic:iterate id="annualTeachingCreditsByPeriodBean" name="annualTeachingCreditsBean" property="annualTeachingCreditsByPeriodBeans">
			<h3 class="infoop"><bean:write name="annualTeachingCreditsByPeriodBean" property="executionPeriod.qualifiedName"/></h3>
			<bean:define id="executionPeriodId" name="annualTeachingCreditsByPeriodBean" property="executionPeriod.idInternal"/>
			<fr:view name="annualTeachingCreditsByPeriodBean" property="institutionWorkTime">
				<fr:schema bundle="TEACHER_CREDITS_SHEET_RESOURCES" type="net.sourceforge.fenixedu.domain.teacher.InstitutionWorkTime">
					<fr:slot name="weekDay" key="label.teacher-institution-working-time.weekday"/>
					<fr:slot name="startTime" key="label.teacher-institution-working-time.start-time">
						<fr:property name="format" value="HH:mm"/>
					</fr:slot>
					<fr:slot name="endTime" key="label.teacher-institution-working-time.end-time">
						<fr:property name="format" value="HH:mm"/>
					</fr:slot>
				</fr:schema>
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle2 thlight thleft mtop05 mbottom05"/>
		    		<fr:property name="columnClasses" value="width12em"/>
				</fr:layout>
			</fr:view>
			<%-- 
			<p><html:link page='<%= "/institutionWorkingTimeManagement.do?method=showTeacherWorkingTimePeriods&amp;page=0" + "&amp;executionPeriodId=" + executionPeriodId + "&amp;teacherId=" + teacherId %>'>
				<bean:message key="link.change"/>&nbsp;<bean:write name="annualTeachingCreditsByPeriodBean" property="executionPeriod.qualifiedName"/>
			</html:link></p>
			--%>
		</logic:iterate>
	</div>
</div>

<div class="panel"><h3 class="separator2 mtop2"><img id="status-icon" width="15px" alt="" src="<%= request.getContextPath() +"/images/right30.png" %>"> <bean:message key="label.credits.masterDegreeTheses" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></h3>
	<div class="inner-panel" style="display: none;">
	<logic:empty name="annualTeachingCreditsBean" property="masterDegreeThesis">
		<bean:message key="label.teacherCreditsSheet.noDataFound" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
	</logic:empty>
	<logic:notEmpty name="annualTeachingCreditsBean" property="masterDegreeThesis">
		<fr:view name="annualTeachingCreditsBean" property="masterDegreeThesis">
			<fr:schema bundle="TEACHER_CREDITS_SHEET_RESOURCES" type="net.sourceforge.fenixedu.domain.thesis.ThesisEvaluationParticipant">
				<fr:slot name="thesis.student.number" key="label.teacher-thesis-student.student-number"/>
				<fr:slot name="thesis.student.person.name" key="label.teacher-thesis-student.student-name"/>
				<fr:slot name="thesis.title" key="label.teacher-thesis-student.title"/>
				<fr:slot name="thesis.evaluation" key="label.date"/>
				<fr:slot name="type" key="label.teacher-thesis-student.function"/>
				<fr:slot name="creditsDistribution" key="label.teacher-thesis-student.percentage"/>
				<%-- <fr:slot name="participationCredits" key="label.credits"/> --%>
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight thleft mtop05 mbottom05"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
	</div>
</div>
<div class="panel"><h3 class="separator2 mtop2"><img id="status-icon" width="15px" alt="" src="<%= request.getContextPath() +"/images/right30.png" %>"> <bean:message key="label.credits.phdDegreeTheses" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></h3>
	<div class="inner-panel" style="display: none;">
		<bean:message key="label.teacherCreditsSheet.noDataFound" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
	</div>
</div>

<div class="panel"><h3 class="separator2 mtop2"><img id="status-icon" width="15px" alt="" src="<%= request.getContextPath() +"/images/right30.png" %>"> <bean:message key="label.credits.projectsAndTutorials" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></h3>
	<div class="inner-panel" style="display: none;">
	<bean:define id="projectAndTutorialProfessorships" name="annualTeachingCreditsBean" property="projectAndTutorialProfessorships"/>
	<logic:empty name="projectAndTutorialProfessorships">
		<bean:message key="label.teacherCreditsSheet.noDataFound" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
	</logic:empty>
	<logic:notEmpty name="projectAndTutorialProfessorships">
		<logic:iterate id="professorship" name="projectAndTutorialProfessorships">
			<bean:define id="professorshipID" name="professorship" property="externalId"/>
			<bean:define id="executionPeriodId" name="professorship" property="executionCourse.executionPeriod.idInternal"/>
			
			<h3 class="mtop15 mbottom05">
				<fr:view name="professorship" layout="values">
					<fr:schema bundle="TEACHER_CREDITS_SHEET_RESOURCES" type="net.sourceforge.fenixedu.domain.Professorship">
						<fr:slot name="executionCourse.nome"/>
						<fr:slot name="executionCourse.sigla"/>
						<fr:slot name="degreeSiglas"/>
					</fr:schema>
				</fr:view>
			</h3>
			
			<logic:present name="professorship" property="degreeProjectTutorialService">
				<fr:view name="professorship" property="degreeProjectTutorialService.attends">
					<fr:schema bundle="TEACHER_CREDITS_SHEET_RESOURCES" type="net.sourceforge.fenixedu.domain.Attends">
						<fr:slot name="registration.number" key="label.teacher-thesis-student.student-number"/>
						<fr:slot name="registration.student.person.name" key="label.teacher-thesis-student.student-name"/>
						<fr:slot name="enrolment.approvementDate" key="label.date"/>
					</fr:schema>
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle2 thlight thleft mtop05 mbottom05"/>
					</fr:layout>
				</fr:view>
			</logic:present>
			<%-- 
			<html:link page='<%= "/degreeProjectTutorialService.do?page=0&amp;method=showProjectTutorialServiceDetails&amp;professorshipID="+professorshipID + "&amp;executionPeriodId="+ executionPeriodId %>'>
				<bean:message key="link.change" />
			</html:link>
			--%>
		</logic:iterate>
	</logic:notEmpty>
	</div>
</div>

<div class="panel"><h3 class="separator2 mtop2"><img id="status-icon" width="15px" alt="" src="<%= request.getContextPath() +"/images/right30.png" %>"> <bean:message key="label.credits.creditsReduction" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></h3>
	<div class="inner-panel" style="display: none;">
		<bean:define id="creditsReductionService" name="annualTeachingCreditsBean" property="creditsReductionService"/>
		<logic:empty name="creditsReductionService">
			<bean:message key="label.teacherCreditsSheet.noDataFound" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
		</logic:empty>
		<logic:notEmpty name="creditsReductionService">
			<fr:view name="creditsReductionService">
				<fr:schema bundle="TEACHER_CREDITS_SHEET_RESOURCES" type="net.sourceforge.fenixedu.domain.teacher.ReductionService">
					<fr:slot name="teacherService.executionPeriod.name" key="label.period"/>
					<fr:slot name="creditsReduction" key="label.managementPosition.credits" layout="null-as-label"/>
				</fr:schema>
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle2 thlight thleft mtop05 mbottom05"/>
				</fr:layout>
			</fr:view>
		</logic:notEmpty>
	</div>
</div>

<div class="panel"><h3 class="separator2 mtop2"><img id="status-icon" width="15px" alt="" src="<%= request.getContextPath() +"/images/right30.png" %>"> <bean:message key="label.teacherCreditsSheet.managementPositionLines" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></h3>
	<div class="inner-panel" style="display: none;">
		<logic:iterate id="annualTeachingCreditsByPeriodBean" name="annualTeachingCreditsBean" property="annualTeachingCreditsByPeriodBeans">
			<h3 class="infoop"><bean:write name="annualTeachingCreditsByPeriodBean" property="executionPeriod.qualifiedName"/></h3>
			<bean:define id="personFunctions" name="annualTeachingCreditsByPeriodBean" property="personFunctions"/>
			<logic:empty name="personFunctions">
				<bean:message key="label.teacherCreditsSheet.noDataFound" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
			</logic:empty>
			<logic:notEmpty name="personFunctions">
				<fr:view name="annualTeachingCreditsByPeriodBean" property="personFunctions">
					<fr:schema bundle="TEACHER_CREDITS_SHEET_RESOURCES" type="net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction">
						<fr:slot name="function.name" key="label.managementPosition.position"/>
						<fr:slot name="function.unit.name" key="label.managementPosition.unit"/>
						<fr:slot name="credits" key="label.managementPosition.credits"/>
						<fr:slot name="beginDateInDateType" key="label.managementPosition.start"/>
						<fr:slot name="endDateInDateType" key="label.managementPosition.end"/>
					</fr:schema>
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle2 thlight thleft mtop05 mbottom05"/>
					</fr:layout>
				</fr:view>
			</logic:notEmpty>
		</logic:iterate>
	</div>
</div>

<div class="panel"><h3 class="separator2 mtop2"><img id="status-icon" width="15px" alt="" src="<%= request.getContextPath() +"/images/right30.png" %>"> <bean:message key="label.teacherCreditsSheet.otherTypeCreditLines" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></h3>
	<div class="inner-panel" style="display: none;">
		<logic:iterate id="annualTeachingCreditsByPeriodBean" name="annualTeachingCreditsBean" property="annualTeachingCreditsByPeriodBeans">
			<h3 class="infoop"><bean:write name="annualTeachingCreditsByPeriodBean" property="executionPeriod.qualifiedName"/></h3>
			<bean:define id="executionPeriodId" name="annualTeachingCreditsByPeriodBean" property="executionPeriod.idInternal"/>
			<%-- 
			<p><html:link page='<%= "/otherServiceManagement.do?page=0&amp;method=showOtherServices&amp;teacherId="+ teacherId + "&amp;executionPeriodId="+ executionPeriodId %>'>
				<bean:message key="link.change"/>&nbsp;<bean:write name="annualTeachingCreditsByPeriodBean" property="executionPeriod.qualifiedName"/>
			</html:link></p>
			--%>
			<bean:define id="otherServices" name="annualTeachingCreditsByPeriodBean" property="otherServices"/>
			<logic:empty name="otherServices">
				<bean:message key="label.teacherCreditsSheet.noDataFound" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
			</logic:empty>
			<logic:notEmpty name="otherServices">
				<fr:view name="annualTeachingCreditsByPeriodBean" property="otherServices">
					<fr:schema bundle="TEACHER_CREDITS_SHEET_RESOURCES" type="net.sourceforge.fenixedu.domain.teacher.OtherService">
						<fr:slot name="credits" key="label.otherTypeCreditLine.credits"/>
						<fr:slot name="reason" key="label.otherTypeCreditLine.reason"/>
					</fr:schema>
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle2 thlight thleft mtop05 mbottom05"/>
					</fr:layout>
				</fr:view>
			</logic:notEmpty>
		</logic:iterate>
	</div>
</div>	

<div class="panel"><h3 class="separator2 mtop2"><img id="status-icon" width="15px" alt="" src="<%= request.getContextPath() +"/images/right30.png" %>"> <bean:message key="label.teacherCreditsSheet.serviceExemptionLines" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></h3>
	<div class="inner-panel" style="display: none;">
		<logic:iterate id="annualTeachingCreditsByPeriodBean" name="annualTeachingCreditsBean" property="annualTeachingCreditsByPeriodBeans">
			<h3 class="infoop"><bean:write name="annualTeachingCreditsByPeriodBean" property="executionPeriod.qualifiedName"/></h3>
			<bean:define id="serviceExemptions" name="annualTeachingCreditsByPeriodBean" property="serviceExemptions"/>
			<logic:empty name="serviceExemptions">
				<bean:message key="label.teacherCreditsSheet.noDataFound" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
			</logic:empty>
			<logic:notEmpty name="serviceExemptions">
				<fr:view name="annualTeachingCreditsByPeriodBean" property="serviceExemptions">
					<fr:schema bundle="TEACHER_CREDITS_SHEET_RESOURCES" type="net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonContractSituation">
						<fr:slot name="contractSituation.name.content" key="label.serviceExemption.type"/>
						<fr:slot name="beginDate" key="label.serviceExemption.start"/>
						<fr:slot name="serviceExemptionEndDate" key="label.serviceExemption.end" layout="null-as-label"/>
					</fr:schema>
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle2 thlight thleft mtop05 mbottom05"/>
					</fr:layout>
				</fr:view>
			</logic:notEmpty>
		</logic:iterate>
	</div>
</div>