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
<%@ page isELIgnored="true"%>
<%@page import="org.fenixedu.academic.domain.ExecutionSemester"%>
<%@page contentType="text/html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app" %>
<html:xhtml/>

<jsp:include page="teacherCreditsStyles.jsp"/>

<logic:present name="annualTeachingCreditsBean">
	<h3><bean:message key="label.teacherService.credits"/>&nbsp;<bean:write name="annualTeachingCreditsBean" property="executionYear.name"/></h3>
	<bean:define id="url" type="java.lang.String">/user/photo/<bean:write name="annualTeachingCreditsBean" property="teacher.person.username"/></bean:define>
	<table class="headerTable"><tr>	
		<td><img src="<%= request.getContextPath() + url %>" /></td>
		<td >
		
		<fr:view name="annualTeachingCreditsBean">
			<fr:schema bundle="TEACHER_CREDITS_SHEET_RESOURCES" type="org.fenixedu.academic.domain.credits.util.AnnualTeachingCreditsBean">
				<fr:slot name="teacher.person.presentationName" key="label.name"/>
				<fr:slot name="professionalCategoryName" key="label.category" layout="null-as-label"/>
				<fr:slot name="departmentName" key="label.department" layout="null-as-label"/>
			</fr:schema>
			<fr:layout name="tabular">
	    		<fr:property name="classes" value="creditsStyle"/>
			</fr:layout>
		</fr:view>
		
		</td></tr></table>
</logic:present>


<script language="Javascript" type="text/javascript">
<!--
$(document).ready(function() {
	$('h3.infoop').hover(function () {
		$(this).addClass('hovered');
	},
	function () {
		$(this).removeClass('hovered');
	});
	$('h3.infoop').click(function () {
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

<bean:define id="teacherId" name="annualTeachingCreditsBean" property="teacher.externalId"/>
<bean:define id="executionYearOid" name="annualTeachingCreditsBean" property="executionYear.externalId"/>
<bean:define id="roleType" name="annualTeachingCreditsBean" property="roleType"/>

<div class="infoop2">
	<bean:message key="label.credits.usefull.information" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
</div>

<logic:notEqual name="roleType" value="DEPARTMENT_MEMBER">
	<p><html:link page='<%= "/annualTeachingCreditsDocument.do?method=getAnnualTeachingCreditsPdf&teacherOid="+teacherId+"&executionYearOid=" + executionYearOid %>'>
		<bean:message key="label.exportToPDF"  bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
	</html:link></p>
</logic:notEqual>

<bean:define id="areCreditsCalculated" name="annualTeachingCreditsBean" property="areCreditsCalculated"/>
<logic:equal name="areCreditsCalculated" value="true">
	<logic:equal name="roleType" value="SCIENTIFIC_COUNCIL">
		<p><html:link page='<%= "/credits.do?method=recalculateCredits&amp;executionYearOid=" + executionYearOid + "&amp;teacherOid=" + teacherId %>'>
		Recalcular
		</html:link></p>
	</logic:equal>
	<div class="panel first">
		<h3 class="credits-header mtop2"><b><bean:message key="label.credits.normalizedAcademicCredits"  bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></b><span><fr:view name="annualTeachingCreditsBean" property="annualTeachingLoad"><fr:layout name="decimal-format"><fr:property name="format" value="######0.00 'Créditos'"/></fr:layout></fr:view></span></h3>
		<h3 class="credits-header mtop2"><b><bean:message key="label.credits.yearCredits"  bundle="TEACHER_CREDITS_SHEET_RESOURCES"/><logic:equal name="annualTeachingCreditsBean" property="hasAnyLimitation" value="true"><span class="tderror1"> *</span></logic:equal></b><span><fr:view name="annualTeachingCreditsBean" property="yearCredits"><fr:layout name="decimal-format"><fr:property name="format" value="######0.00 'Créditos'"/></fr:layout></fr:view></span></h3>
		<h3 class="credits-header mtop2"><b><bean:message key="label.credits.finalCredits"  bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></b><span><fr:view name="annualTeachingCreditsBean" property="finalCredits"><fr:layout name="decimal-format"><fr:property name="format" value="######0.00 'Créditos'"/></fr:layout></fr:view></span></h3>
	</div>
</logic:equal>
<logic:notEqual name="areCreditsCalculated" value="true">
	<div class="infoop2">
		<logic:iterate id="annualTeachingCreditsByPeriodBean" name="annualTeachingCreditsBean" property="annualTeachingCreditsByPeriodBeans">
			<%
				final ExecutionSemester executionSemester = ((org.fenixedu.academic.domain.credits.util.AnnualTeachingCreditsByPeriodBean) annualTeachingCreditsByPeriodBean).getExecutionPeriod();
				if (executionSemester.isBeforeOrEquals(ExecutionSemester.readActualExecutionSemester())) {
			%>
			<bean:define id="executionPeriodQualifiedName" name="annualTeachingCreditsByPeriodBean" property="executionPeriod.qualifiedName"></bean:define>
			<bean:define id="executionPeriodOid" name="annualTeachingCreditsByPeriodBean" property="executionPeriod.externalId"/>
			<p>
				<logic:equal name="annualTeachingCreditsByPeriodBean" property="showTeacherCreditsLockedMessage" value="true">
					<bean:message key="message.teacherCreditsLocked" bundle="TEACHER_CREDITS_SHEET_RESOURCES" arg0="<%=executionPeriodQualifiedName.toString()%>"/>
				</logic:equal>
				<logic:equal name="annualTeachingCreditsByPeriodBean" property="showTeacherCreditsUnlockedMessage" value="true">
					<bean:message key="message.teacherCreditsUnlocked" bundle="TEACHER_CREDITS_SHEET_RESOURCES" arg0="<%=executionPeriodQualifiedName.toString()%>"/>
				</logic:equal>
				<logic:equal name="annualTeachingCreditsByPeriodBean" property="canLockTeacherCredits" value="true">
					<bean:define id="confirmationMessage"><bean:message key="label.teacher.lockTeacherCredits.confirmationMessage"  bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></bean:define>
					<html:link page='<%= "/credits.do?method=lockTeacherCredits&amp;executionPeriodOid=" + executionPeriodOid + "&amp;teacherOid=" + teacherId %>' onclick="<%="return confirm('"+confirmationMessage+"')" %>">
						<b><bean:message key="label.teacher.lock"  bundle="TEACHER_CREDITS_SHEET_RESOURCES" arg0="<%=executionPeriodQualifiedName.toString()%>"/></b>
					</html:link>
				</logic:equal>
				<logic:equal name="annualTeachingCreditsByPeriodBean" property="canUnlockTeacherCredits" value="true">
					<bean:define id="confirmationMessage"><bean:message key="label.teacher.unlockTeacherCredits.confirmationMessage"  bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></bean:define>
					<html:link page='<%= "/credits.do?method=unlockTeacherCredits&amp;executionPeriodOid=" + executionPeriodOid + "&amp;teacherOid=" + teacherId %>' onclick="<%="return confirm('"+confirmationMessage+"')" %>">
						<bean:message key="label.teacher.unlock" bundle="TEACHER_CREDITS_SHEET_RESOURCES" arg0="<%=executionPeriodQualifiedName.toString()%>"/>
					</html:link>
				</logic:equal>
			</p>
			<%	} %>
		</logic:iterate>
	</div>
</logic:notEqual>

<div class="panel first"><h3 class="infoop mtop2"><img id="status-icon" width="15px" alt="" src="<%= request.getContextPath() +"/images/right30.png" %>">
<b><bean:message key="label.teacherCreditsSheet.professorships" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></b>
<logic:equal name="areCreditsCalculated" value="true">
	<span><fr:view name="annualTeachingCreditsBean" property="teachingCredits"><fr:layout name="decimal-format"><fr:property name="format" value="######0.00 'Créditos'"/></fr:layout></fr:view></span>
</logic:equal></h3>
	<div class="inner-panel" style="display: none;">
		<logic:iterate id="annualTeachingCreditsByPeriodBean" name="annualTeachingCreditsBean" property="annualTeachingCreditsByPeriodBeans">
		<h3><bean:write name="annualTeachingCreditsByPeriodBean" property="executionPeriod.qualifiedName"/></h3>
			<bean:size id="totalNumberOfProfessorship" name="annualTeachingCreditsByPeriodBean" property="professorships"/>
			<logic:equal name="totalNumberOfProfessorship" value="0">
				<bean:message key="label.teacherCreditsSheet.noDataFound" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
			</logic:equal>
			<logic:notEqual name="totalNumberOfProfessorship" value="0">
				<table class="tstyle4 mbottom05 mbottom05">
				<tr>
						<th rowspan="2"><bean:message key="label.course" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
						<th rowspan="2"><bean:message key="label.effortRate" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
						<th rowspan="2"><bean:message key="label.unitCredit" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
						<th rowspan="2"><bean:message key="label.shift" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
						<th rowspan="2"><bean:message key="label.shift.type" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
						<th colspan="4"><bean:message key="label.lessons" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
						<th rowspan="2"><bean:message key="label.weeklyAverage" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
						<th rowspan="2"><bean:message key="label.semesterTotal" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
						<th rowspan="2"><bean:message key="label.professorship.percentage" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
						<th rowspan="2"><bean:message key="label.expectedEfectiveLoad" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
					</tr>
					<tr>
						<th><bean:message key="label.day.of.week" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
						<th><bean:message key="label.lesson.start" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
						<th><bean:message key="label.lesson.end" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
						<th><bean:message key="label.lesson.room" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>			
					</tr> 
				
				<bean:define id="canEditCreditsInfo" name="annualTeachingCreditsByPeriodBean" property="canEditTeacherCredits"/>
				<logic:iterate id="professorship" name="annualTeachingCreditsByPeriodBean" property="professorships">
					<bean:define id="professorshipID" name="professorship" property="externalId"/>
					<bean:define id="executionPeriodId" name="professorship" property="executionCourse.executionPeriod.externalId"/>
					<bean:define id="totalNumberOfLessons" name="professorship" property="degreeTeachingServiceLessonRows"/>
					<bean:size id="numberOfShifts" name="professorship" property="degreeTeachingServicesOrderedByShift"/>
					<tr>
						<td rowspan="<%= java.lang.Math.max(((Integer)totalNumberOfLessons).intValue(),1)%>">
							<logic:equal name="canEditCreditsInfo" value="true">
								<html:link page='<%= "/degreeTeachingServiceManagement.do?page=0&amp;method=showTeachingServiceDetails&amp;professorshipID="+professorshipID + "&amp;executionPeriodId="+ executionPeriodId %>'>
									<bean:write name="professorship" property="executionCourse.name"/> (<bean:write name="professorship" property="degreeSiglas"/>)
								</html:link>
							</logic:equal>
							<logic:notEqual name="canEditCreditsInfo" value="true">
								<bean:write name="professorship" property="executionCourse.name"/> (<bean:write name="professorship" property="degreeSiglas"/>)
							</logic:notEqual>									
						</td>
						<td rowspan="<%= java.lang.Math.max(((Integer)totalNumberOfLessons).intValue(),1)%>"><bean:write name="professorship" property="executionCourse.effortRate"/></td>
						<td rowspan="<%= java.lang.Math.max(((Integer)totalNumberOfLessons).intValue(),1)%>"><bean:write name="professorship" property="executionCourse.unitCreditValue"/></td>
						<logic:equal name="numberOfShifts" value="0">
							<td colspan="10"/>
						</logic:equal>
						
						<logic:iterate id="degreeTeachingService" name="professorship" property="degreeTeachingServicesOrderedByShift" indexId="indexShifts">
							<bean:size id="numberOfLessons" name="degreeTeachingService" property="shift.lessonsOrderedByWeekDayAndStartTime"/>
							<logic:equal name="numberOfLessons" value="0">
								<td><bean:write name="degreeTeachingService" property="shift.nome"/></td>
								<td><bean:write name="degreeTeachingService" property="shift.shiftTypesPrettyPrint"/></td>
								<td colspan="6"/>
								<td style="text-align: right;">
									<bean:define id="teachingServicePercentage" name="degreeTeachingService" property="percentage"/>
									<%= ((Math.round(((Double)teachingServicePercentage).doubleValue() * 100.0)) / 100.0) %>
								</td>
								<td style="text-align: right;">-</td>
								</tr>
							</logic:equal>
							<logic:iterate id="lesson" name="degreeTeachingService" property="shift.lessonsOrderedByWeekDayAndStartTime" indexId="indexLessons">
								<logic:notEqual name="indexLessons" value="0">
									</tr>
								</logic:notEqual>
								<logic:notEqual name="indexShifts" value="0">
									<tr>
								</logic:notEqual>
								<logic:equal name="indexLessons" value="0">
									<td rowspan="<%= numberOfLessons %>"><bean:write name="degreeTeachingService" property="shift.nome"/></td>
									<td rowspan="<%= numberOfLessons %>"><bean:write name="degreeTeachingService" property="shift.shiftTypesPrettyPrint"/></td>
								</logic:equal>
								<td><bean:write name="lesson" property="weekDay.labelShort"/></td>
								<td><fr:view name="lesson" property="beginHourMinuteSecond"/></td>
								<td><fr:view name="lesson" property="endHourMinuteSecond"/></td>
								<td>
									<logic:notEmpty name="lesson" property="sala">
										<bean:write name="lesson" property="sala.name"/>
									</logic:notEmpty>
									<logic:empty name="lesson" property="sala">
										-
									</logic:empty>
								</td>
								<logic:equal name="indexLessons" value="0">
									<td style="text-align: right;" rowspan="<%= numberOfLessons %>"><bean:write name="degreeTeachingService" property="shift.courseLoadWeeklyAverage"/></td>
									<td style="text-align: right;" rowspan="<%= numberOfLessons %>"><fr:view name="degreeTeachingService" property="shift.courseLoadTotalHours" /></td>
									<td style="text-align: right;" rowspan="<%= numberOfLessons %>">
										<bean:define id="teachingServicePercentage" name="degreeTeachingService" property="percentage"/>
										<%= ((Math.round(((Double)teachingServicePercentage).doubleValue() * 100.0)) / 100.0) %>
									</td>
									<logic:equal name="degreeTeachingService" property="shift.executionCourse.projectTutorialCourse" value="true">
										<td style="text-align: right;" rowspan="<%= numberOfLessons %>">-</td>
									</logic:equal>
									<logic:notEqual name="degreeTeachingService" property="shift.executionCourse.projectTutorialCourse" value="true">
										<td style="text-align: right;" rowspan="<%= numberOfLessons %>"><bean:write name="degreeTeachingService" property="efectiveLoad"/></td>
									</logic:notEqual>
								</logic:equal>
							</logic:iterate>
						</logic:iterate>
						</tr>
						<bean:size id="numberOfSupportLessons" name="professorship" property="supportLessonsOrderedByStartTimeAndWeekDay"/>
						<logic:iterate id="supportLesson" name="professorship" property="supportLessonsOrderedByStartTimeAndWeekDay" indexId="indexSupportLessons">
							<tr>
							<logic:equal name="indexSupportLessons" value="0">
								<td rowspan="<%= numberOfSupportLessons %>">-</td>
								<td rowspan="<%= numberOfSupportLessons %>"><bean:message key="label.supportLessons" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></td>
							</logic:equal>
							<td><bean:write name="supportLesson" property="weekDayObject.labelShort"/></td>
							<td><fr:view name="supportLesson" property="startTimeHourMinuteSecond"/></td>
							<td><fr:view name="supportLesson" property="endTimeHourMinuteSecond"/></td>
							<td><bean:write name="supportLesson" property="place"/></td>
							<logic:equal name="indexSupportLessons" value="0">
								<td style="text-align: right;" rowspan="<%= numberOfSupportLessons %>">-</td>
								<td style="text-align: right;" rowspan="<%= numberOfSupportLessons %>">-</td>
								<td style="text-align: right;" rowspan="<%= numberOfSupportLessons %>">-</td>
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
<div class="panel"><h3 class="infoop mtop2"><img id="status-icon" width="15px" alt="" src="<%= request.getContextPath() +"/images/right30.png" %>">
<b><bean:message key="label.teacherCreditsSheet.institutionWorkingTime.optional" arg0="<%=org.fenixedu.academic.domain.organizationalStructure.Unit.getInstitutionAcronym()%>" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></b>
<logic:equal name="areCreditsCalculated" value="true">
	<span>-</span>
</logic:equal></h3>
	<div class="inner-panel" style="display: none;">
		<logic:iterate id="annualTeachingCreditsByPeriodBean" name="annualTeachingCreditsBean" property="annualTeachingCreditsByPeriodBeans">
			<h3><bean:write name="annualTeachingCreditsByPeriodBean" property="executionPeriod.qualifiedName"/></h3>
			<bean:define id="executionPeriodId" name="annualTeachingCreditsByPeriodBean" property="executionPeriod.externalId"/>
			<bean:define id="canEditCreditsInfo" name="annualTeachingCreditsByPeriodBean" property="canEditTeacherCredits"/>
			<logic:equal name="canEditCreditsInfo" value="true">
				<p><html:link page='<%= "/institutionWorkingTimeManagement.do?method=create&amp;page=0" + "&amp;executionPeriodId=" + executionPeriodId + "&amp;teacherId=" + teacherId %>'>
					<bean:message key="link.teacher-institution-working-time.create"/>
				</html:link></p>
			</logic:equal>
			<fr:view name="annualTeachingCreditsByPeriodBean" property="institutionWorkTime">
				<fr:schema bundle="TEACHER_CREDITS_SHEET_RESOURCES" type="org.fenixedu.academic.domain.teacher.InstitutionWorkTime">
					<fr:slot name="weekDay.label" key="label.teacher-institution-working-time.weekday"/>
					<fr:slot name="startTime" key="label.teacher-institution-working-time.start-time">
						<fr:property name="format" value="HH:mm"/>
					</fr:slot>
					<fr:slot name="endTime" key="label.teacher-institution-working-time.end-time">
						<fr:property name="format" value="HH:mm"/>
					</fr:slot>
				</fr:schema>
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle2 thlight thleft mtop05 mbottom05"/>
		    		<fr:property name="columnClasses" value="width12em,,,"/>
					<logic:equal name="canEditCreditsInfo" value="true">
						<fr:property name="link(edit)" value="/institutionWorkingTimeManagement.do?method=prepareEdit" />
						<fr:property name="key(edit)" value="label.edit" />
						<fr:property name="param(edit)" value="externalId/institutionWorkTimeOid" />
						<fr:property name="bundle(edit)" value="TEACHER_CREDITS_SHEET_RESOURCES" />
						<fr:property name="link(delete)" value="/institutionWorkingTimeManagement.do?method=delete" />
						<fr:property name="key(delete)" value="label.delete" />
						<fr:property name="param(delete)" value="externalId/institutionWorkTimeOid" />
						<fr:property name="bundle(delete)" value="TEACHER_CREDITS_SHEET_RESOURCES" />
					</logic:equal>
				</fr:layout>
			</fr:view>
		</logic:iterate>
	</div>
</div>

<div class="panel"><h3 class="infoop mtop2"><img id="status-icon" width="15px" alt="" src="<%= request.getContextPath() +"/images/right30.png" %>">
<b><bean:message key="label.credits.masterDegreeTheses" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></b>
<logic:equal name="areCreditsCalculated" value="true">
	<span><fr:view name="annualTeachingCreditsBean" property="masterDegreeThesesCredits"><fr:layout name="decimal-format"><fr:property name="format" value="######0.00 'Créditos'"/></fr:layout></fr:view></span>
</logic:equal></h3>
	<div class="inner-panel" style="display: none;">
	<logic:empty name="annualTeachingCreditsBean" property="masterDegreeThesis">
		<bean:message key="label.teacherCreditsSheet.noDataFound" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
	</logic:empty>
	<logic:notEmpty name="annualTeachingCreditsBean" property="masterDegreeThesis">
		<fr:view name="annualTeachingCreditsBean" property="masterDegreeThesis">
			<fr:schema bundle="TEACHER_CREDITS_SHEET_RESOURCES" type="org.fenixedu.academic.domain.thesis.ThesisEvaluationParticipant">
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
<div class="panel"><h3 class="infoop mtop2"><img id="status-icon" width="15px" alt="" src="<%= request.getContextPath() +"/images/right30.png" %>">
<b><bean:message key="label.credits.phdDegreeTheses" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></b>
<logic:equal name="areCreditsCalculated" value="true">
	<span><fr:view name="annualTeachingCreditsBean" property="phdDegreeThesesCredits"><fr:layout name="decimal-format"><fr:property name="format" value="######0.00 'Créditos'"/></fr:layout></fr:view></span>
</logic:equal></h3>
	<div class="inner-panel" style="display: none;">
		<bean:define id="phdDegreeTheses" name="annualTeachingCreditsBean" property="phdDegreeTheses"/>
		<logic:empty name="phdDegreeTheses">
			<bean:message key="label.teacherCreditsSheet.noDataFound" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
		</logic:empty>
		<logic:notEmpty name="phdDegreeTheses">
			<fr:view name="phdDegreeTheses">
				<fr:schema bundle="TEACHER_CREDITS_SHEET_RESOURCES" type="org.fenixedu.academic.domain.phd.InternalPhdParticipant">
					<fr:slot name="individualProcess.student.number" key="label.teacher-thesis-student.student-number"/>
					<fr:slot name="individualProcess.student.person.name" key="label.teacher-thesis-student.student-name"/>
					<fr:slot name="individualProcess.thesisTitle" key="label.teacher-thesis-student.title"/>
					<fr:slot name="individualProcess.conclusionDate" key="label.date"/>
					<fr:slot name="roleOnProcess" key="label.teacher-thesis-student.function" layout="null-as-label"/>
					<fr:slot name="individualProcess.assistantGuidingsCount" key="label.teacher-assistant-guiding-number"/>
				</fr:schema>
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle2 thlight thleft mtop05 mbottom05"/>
				</fr:layout>
			</fr:view>
		</logic:notEmpty>
	</div>
</div>

<div class="panel"><h3 class="infoop mtop2"><img id="status-icon" width="15px" alt="" src="<%= request.getContextPath() +"/images/right30.png" %>">
<b><bean:message key="label.credits.projectsAndTutorials" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></b>
<logic:equal name="areCreditsCalculated" value="true">
	<span><fr:view name="annualTeachingCreditsBean" property="projectsTutorialsCredits"><fr:layout name="decimal-format"><fr:property name="format" value="######0.00 'Créditos'"/></fr:layout></fr:view></span>
</logic:equal></h3>
	<div class="inner-panel" style="display: none;">
	<bean:define id="projectAndTutorialProfessorships" name="annualTeachingCreditsBean" property="projectAndTutorialProfessorships"/>
	<logic:empty name="projectAndTutorialProfessorships">
		<bean:message key="label.teacherCreditsSheet.noDataFound" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
	</logic:empty>
	<logic:notEmpty name="projectAndTutorialProfessorships">
		<logic:iterate id="professorship" name="projectAndTutorialProfessorships">
			<bean:define id="professorshipID" name="professorship" property="externalId"/>
			<bean:define id="executionPeriodId" name="professorship" property="executionCourse.executionPeriod.externalId"/>
			
			<h3 class="mtop15 mbottom05">
				<fr:view name="professorship" layout="values">
					<fr:schema bundle="TEACHER_CREDITS_SHEET_RESOURCES" type="org.fenixedu.academic.domain.Professorship">
						<fr:slot name="executionCourse.nome"/>
						<fr:slot name="executionCourse.sigla"/>
						<fr:slot name="degreeSiglas"/>
					</fr:schema>
				</fr:view>
			</h3>
			
			<logic:notEmpty name="professorship" property="degreeProjectTutorialServices">
				<fr:view name="professorship" property="degreeProjectTutorialServices" schema="show.degreeProjectTutorialService">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle2 thlight thleft mtop05 mbottom05"/>
					</fr:layout>
				</fr:view>
			</logic:notEmpty> 
		<%--	<bean:define id="canEditCreditsInfo" name="annualTeachingCreditsBean" property="canEditTeacherCredits"/> --%>
			<bean:define id="canEditCreditsInfo" name="annualTeachingCreditsBean" property="canEditTeacherCreditsInAnyPeriod"/>
			<logic:equal name="canEditCreditsInfo" value="true">
				<html:link page='<%= "/degreeProjectTutorialService.do?page=0&amp;method=showProjectTutorialServiceDetails&amp;professorshipID="+professorshipID + "&amp;executionPeriodId="+ executionPeriodId %>'>
					<bean:message key="link.change" />
				</html:link>
			</logic:equal>
		</logic:iterate>
	</logic:notEmpty>
	</div>
</div>

<div class="panel"><h3 class="infoop mtop2"><img id="status-icon" width="15px" alt="" src="<%= request.getContextPath() +"/images/right30.png" %>">
<b><bean:message key="label.teacherCreditsSheet.managementPositionLines" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></b>
<logic:equal name="areCreditsCalculated" value="true">
	<span><fr:view name="annualTeachingCreditsBean" property="managementFunctionCredits"><fr:layout name="decimal-format"><fr:property name="format" value="######0.00 'Créditos'"/></fr:layout></fr:view></span>
</logic:equal></h3>
	<div class="inner-panel" style="display: none;">
		<logic:iterate id="annualTeachingCreditsByPeriodBean" name="annualTeachingCreditsBean" property="annualTeachingCreditsByPeriodBeans">
			<h3><bean:write name="annualTeachingCreditsByPeriodBean" property="executionPeriod.qualifiedName"/></h3>
			<bean:define id="executionPeriodOid" name="annualTeachingCreditsByPeriodBean" property="executionPeriod.externalId"/>
			<p><logic:equal name="roleType" value="SCIENTIFIC_COUNCIL">
				<html:link page='<%= "/managePersonFunctionsShared.do?method=prepareToAddPersonFunction&amp;page=0" + "&amp;executionPeriodOid=" + executionPeriodOid + "&amp;teacherOid=" + teacherId %>'>
					<bean:message key="link.managementPosition.create" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
				</html:link>
			</logic:equal>
			<logic:equal name="roleType" value="DEPARTMENT_ADMINISTRATIVE_OFFICE">
				<logic:equal name="annualTeachingCreditsByPeriodBean" property="canEditTeacherManagementFunctions" value="true">
					<html:link page='<%= "/managePersonFunctionsShared.do?method=prepareToAddPersonFunctionShared&amp;page=0" + "&amp;executionPeriodOid=" + executionPeriodOid + "&amp;teacherOid=" + teacherId %>'>
						<bean:message key="link.managementPosition.create" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
					</html:link>
				</logic:equal>
			</logic:equal></p>
			<bean:define id="personFunctions" name="annualTeachingCreditsByPeriodBean" property="personFunctions"/>
			<logic:empty name="personFunctions">
				<bean:message key="label.teacherCreditsSheet.noDataFound" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
			</logic:empty>
			<logic:notEmpty name="personFunctions">
				<fr:view name="annualTeachingCreditsByPeriodBean" property="personFunctions">
					<fr:schema bundle="TEACHER_CREDITS_SHEET_RESOURCES" type="org.fenixedu.academic.domain.organizationalStructure.PersonFunction">
						<fr:slot name="function.name" key="label.managementPosition.position"/>
						<fr:slot name="function.unit.name" key="label.managementPosition.unit"/>
						<logic:equal name="areCreditsCalculated" value="true">	
							<fr:slot name="credits" key="label.managementPosition.credits"/>
						</logic:equal>
					</fr:schema>
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle2 thlight thleft mtop05 mbottom05"/>
						<logic:equal name="roleType" value="DEPARTMENT_ADMINISTRATIVE_OFFICE">
							<fr:property name="link(edit)" value="/managePersonFunctionsShared.do?method=prepareToEditPersonFunctionShared" />
							<fr:property name="key(edit)" value="label.edit" />
							<fr:property name="param(edit)" value="externalId/personFunctionOid" />
							<fr:property name="bundle(edit)" value="TEACHER_CREDITS_SHEET_RESOURCES" />
							<fr:property name="visibleIf(edit)" value="canBeEditedByDepartmentAdministrativeOffice" />
							<fr:property name="link(delete)" value="<%="/managePersonFunctionsShared.do?method=deletePersonFunctionShared&executionYearOid="+executionYearOid%>" />
							<fr:property name="key(delete)" value="label.delete" />
							<fr:property name="param(delete)" value="externalId/personFunctionOid" />
							<fr:property name="bundle(delete)" value="TEACHER_CREDITS_SHEET_RESOURCES" />
							<fr:property name="visibleIf(delete)" value="canBeEditedByDepartmentAdministrativeOffice" />
						</logic:equal>
						<logic:equal name="roleType" value="SCIENTIFIC_COUNCIL">
							<fr:property name="link(edit2)" value="<%="/managePersonFunctionsShared.do?method=prepareToEditPersonFunction&executionPeriodOid="+executionPeriodOid%>" />
							<fr:property name="key(edit2)" value="label.edit" />
							<fr:property name="param(edit2)" value="externalId/personFunctionOid" />
							<fr:property name="bundle(edit2)" value="TEACHER_CREDITS_SHEET_RESOURCES" />
							<fr:property name="link(delete2)" value="<%="/managePersonFunctionsShared.do?method=deletePersonFunctionShared&executionYearOid="+executionYearOid%>" />
							<fr:property name="key(delete2)" value="label.delete" />
							<fr:property name="param(delete2)" value="externalId/personFunctionOid" />
							<fr:property name="bundle(delete2)" value="TEACHER_CREDITS_SHEET_RESOURCES" />
						</logic:equal>
					</fr:layout>
				</fr:view>
			</logic:notEmpty>
		</logic:iterate>
	</div>
</div>

<div class="panel"><h3 class="infoop mtop2"><img id="status-icon" width="15px" alt="" src="<%= request.getContextPath() +"/images/right30.png" %>">
<b><bean:message key="label.teacherCreditsSheet.otherTypeCreditLines" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></b>
<logic:equal name="areCreditsCalculated" value="true">
	<span><fr:view name="annualTeachingCreditsBean" property="othersCredits"><fr:layout name="decimal-format"><fr:property name="format" value="######0.00 'Créditos'"/></fr:layout></fr:view></span>
</logic:equal></h3>
	<div class="inner-panel" style="display: none;">
		<logic:iterate id="annualTeachingCreditsByPeriodBean" name="annualTeachingCreditsBean" property="annualTeachingCreditsByPeriodBeans">
			<h3><bean:write name="annualTeachingCreditsByPeriodBean" property="executionPeriod.qualifiedName"/></h3>
			<bean:define id="executionPeriodOid" name="annualTeachingCreditsByPeriodBean" property="executionPeriod.externalId"/>
			<logic:equal name="roleType" value="SCIENTIFIC_COUNCIL">
				<p><html:link page='<%= "/otherServiceManagement.do?page=0&amp;method=prepareEditOtherService&amp;teacherId="+ teacherId + "&amp;executionPeriodOid="+ executionPeriodOid %>'>
					<bean:message key="label.insertNew" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
				</html:link></p>
			</logic:equal>
			<bean:define id="otherServices" name="annualTeachingCreditsByPeriodBean" property="otherServices"/>
			<logic:empty name="otherServices">
				<bean:message key="label.teacherCreditsSheet.noDataFound" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
			</logic:empty>
			<logic:notEmpty name="otherServices">
				<fr:view name="annualTeachingCreditsByPeriodBean" property="otherServices">
					<fr:schema bundle="TEACHER_CREDITS_SHEET_RESOURCES" type="org.fenixedu.academic.domain.teacher.OtherService">
						<fr:slot name="credits" key="label.credits"/>
						<fr:slot name="reason" key="label.otherTypeCreditLine.reason"/>
						<fr:slot name="correctedExecutionSemester.executionYear.name" key="label.executionYear"/>
					</fr:schema>
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle2 thlight thleft mtop05 mbottom05"/>
						<logic:equal name="roleType" value="SCIENTIFIC_COUNCIL">
							<fr:property name="link(edit)" value="/otherServiceManagement.do?method=prepareEditOtherService" />
							<fr:property name="key(edit)" value="label.edit" />
							<fr:property name="param(edit)" value="externalId/otherServiceOid" />
							<fr:property name="bundle(edit)" value="TEACHER_CREDITS_SHEET_RESOURCES" />
							<fr:property name="link(delete)" value="/otherServiceManagement.do?method=deleteOtherService" />
							<fr:property name="key(delete)" value="label.delete" />
							<fr:property name="param(delete)" value="externalId/otherServiceOid" />
							<fr:property name="bundle(delete)" value="TEACHER_CREDITS_SHEET_RESOURCES" />
						</logic:equal>
					</fr:layout>
				</fr:view>
			</logic:notEmpty>
		</logic:iterate>
	</div>
</div>	

<div class="panel"><h3 class="infoop mtop2"><img id="status-icon" width="15px" alt="" src="<%= request.getContextPath() +"/images/right30.png" %>">
<b><bean:message key="label.credits.creditsReduction.definition" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></b>
<logic:equal name="areCreditsCalculated" value="true">
	<logic:equal name="annualTeachingCreditsBean" property="canSeeCreditsReduction" value="true">
		<span><fr:view name="annualTeachingCreditsBean" property="creditsReduction"><fr:layout name="decimal-format"><fr:property name="format" value="######0.00 'Créditos'"/></fr:layout></fr:view></span>
	</logic:equal>
</logic:equal></h3>
	<div class="inner-panel" style="display: none;">
		<logic:equal name="annualTeachingCreditsBean" property="canSeeCreditsReduction" value="true">
			<fr:view name="annualTeachingCreditsBean" property="annualTeachingCreditsByPeriodBeans">
				<fr:schema bundle="TEACHER_CREDITS_SHEET_RESOURCES" type="org.fenixedu.academic.domain.credits.util.AnnualTeachingCreditsByPeriodBean">
					<fr:slot name="executionPeriod.name" key="label.period"/>
					<fr:slot name="requestCreditsReduction" key="label.requestedReductionCredits" layout="radio"/>
					<fr:slot name="creditsReductionServiceAttribute" key="label.attributedReductionCredits" layout="null-as-label">
						<fr:property name="label" value="label.pendingValidation"/>
						<fr:property name="key" value="true"/>
						<fr:property name="bundle" value="TEACHER_CREDITS_SHEET_RESOURCES"/>
					</fr:slot>
				</fr:schema>
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle2 thlight thleft mtop05 mbottom05"/>
				    <fr:property name="link(edit)" value="/membersCreditsReductions.do?method=editCreditsReduction" />
					<fr:property name="key(edit)" value="label.edit" />
					<fr:property name="param(edit)" value="executionPeriod.externalId/executionPeriodOID,teacher.externalId/teacherOID" />
					<fr:property name="bundle(edit)" value="TEACHER_CREDITS_SHEET_RESOURCES" />
					<fr:property name="visibleIf(edit)" value="canEditTeacherCreditsReductions" />
				  	<logic:equal name="roleType" value="SCIENTIFIC_COUNCIL">
					  	<fr:property name="link(aprove)" value="/creditsReductions.do?method=aproveReductionService" />
						<fr:property name="key(aprove)" value="label.edit" />
						<fr:property name="param(aprove)" value="executionPeriod.externalId/executionPeriodOID,teacher.externalId/teacherOID" />
						<fr:property name="bundle(aprove)" value="TEACHER_CREDITS_SHEET_RESOURCES" />
					</logic:equal>
				</fr:layout>
			</fr:view>
		</logic:equal>
		<logic:notEqual name="annualTeachingCreditsBean" property="canSeeCreditsReduction" value="true">
			<bean:message key="label.confidentialInformation" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
		</logic:notEqual>
	</div>
</div>

<div class="panel"><h3 class="infoop mtop2"><img id="status-icon" width="15px" alt="" src="<%= request.getContextPath() +"/images/right30.png" %>">
<b><bean:message key="label.teacherCreditsSheet.serviceExemptionLines" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></b>
<logic:equal name="areCreditsCalculated" value="true">
	<span><fr:view name="annualTeachingCreditsBean" property="serviceExemptionCredits"><fr:layout name="decimal-format"><fr:property name="format" value="######0.00 'Créditos'"/></fr:layout></fr:view></span>
</logic:equal></h3>
	<div class="inner-panel" style="display: none;">
		<logic:iterate id="annualTeachingCreditsByPeriodBean" name="annualTeachingCreditsBean" property="annualTeachingCreditsByPeriodBeans">
			<h3><bean:write name="annualTeachingCreditsByPeriodBean" property="executionPeriod.qualifiedName"/></h3>
			<bean:define id="serviceExemptions" name="annualTeachingCreditsByPeriodBean" property="serviceExemptions"/>
			<logic:empty name="serviceExemptions">
				<bean:message key="label.teacherCreditsSheet.noDataFound" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
			</logic:empty>
			<logic:notEmpty name="serviceExemptions">
				<fr:view name="annualTeachingCreditsByPeriodBean" property="serviceExemptions">
					<fr:schema bundle="TEACHER_CREDITS_SHEET_RESOURCES" type="org.fenixedu.academic.domain.personnelSection.contracts.PersonContractSituation">
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


<div class="panel"><h3 class="infoop mtop2"><img id="status-icon" width="15px" alt="" src="<%= request.getContextPath() +"/images/right30.png" %>">
<b><bean:message key="label.notes" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></b>
<logic:equal name="areCreditsCalculated" value="true">
	<span>-</span>
</logic:equal></h3>
	<div class="inner-panel" style="display: none;">
		<bean:define id="canEditCreditsInfo" name="annualTeachingCreditsBean" property="canEditTeacherCreditsInAnyPeriod"/>
		<logic:equal name="canEditCreditsInfo" value="true">
			<p><html:link page='<%= "/teacherServiceComments.do?page=0&amp;method=editTeacherServiceComment&amp;teacherOid="+teacherId + "&amp;executionYearOid="+ executionYearOid %>'>
				<bean:message key="label.newComment"  bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
			</html:link></p>
		</logic:equal>
	
		<bean:define id="teacherServiceComments" name="annualTeachingCreditsBean" property="teacherServiceComments"/>
		<logic:empty name="teacherServiceComments">
			<bean:message key="label.teacherCreditsSheet.noDataFound" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
		</logic:empty>
		<logic:notEmpty name="teacherServiceComments">
			<fr:view name="teacherServiceComments">
				<fr:schema bundle="TEACHER_CREDITS_SHEET_RESOURCES" type="org.fenixedu.academic.domain.teacher.TeacherServiceComment">
					<fr:slot name="content" key="label.comment"/>
					<fr:slot name="createdBy" key="label.user">
						<fr:property name="format" value="${name} (${username})"/>
					</fr:slot>
					<fr:slot name="creationDate" key="label.date"/>
					<fr:slot name="lastModifiedDate" key="label.lastModifiedDate"/>
				</fr:schema>
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle2 thlight thleft mtop05 mbottom05"/>
					<logic:equal name="canEditCreditsInfo" value="true">
						<fr:property name="link(edit)" value="/teacherServiceComments.do?method=editTeacherServiceComment" />
						<fr:property name="key(edit)" value="label.edit" />
						<fr:property name="param(edit)" value="externalId/teacherServiceCommentOid" />
						<fr:property name="bundle(edit)" value="TEACHER_CREDITS_SHEET_RESOURCES" />
						<fr:property name="visibleIf(edit)" value="canEdit" />
						<fr:property name="link(delete)" value="/teacherServiceComments.do?method=deleteTeacherServiceComment" />
						<fr:property name="key(delete)" value="label.delete" />
						<fr:property name="param(delete)" value="externalId/teacherServiceCommentOid" />
						<fr:property name="bundle(delete)" value="TEACHER_CREDITS_SHEET_RESOURCES" />
						<fr:property name="visibleIf(delete)" value="canEdit" />
					</logic:equal>
				</fr:layout>
			</fr:view>
		</logic:notEmpty>
	</div>
</div>


<logic:equal name="annualTeachingCreditsBean" property="canUserSeeTeacherServiceLogs" value="true">
	<div class="panel"><h3 class="infoop mtop2"><img id="status-icon" width="15px" alt="" src="<%= request.getContextPath() +"/images/right30.png" %>">
		<b><bean:message key="label.teacher.service.logs" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></b>
		<logic:equal name="areCreditsCalculated" value="true">
			<span>-</span>
		</logic:equal></h3>
		<div class="inner-panel" style="display: none;">
			<jsp:include page="showTeachingServiceLogs.jsp"/>
		</div>
	</div>
</logic:equal>

<logic:equal name="areCreditsCalculated" value="true">
	<logic:equal name="annualTeachingCreditsBean" property="hasAnyLimitation" value="true">
		<p><span class="tderror1">* </span><bean:message key="message.hasCreditsLimitation" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></p>
	</logic:equal>
</logic:equal>
