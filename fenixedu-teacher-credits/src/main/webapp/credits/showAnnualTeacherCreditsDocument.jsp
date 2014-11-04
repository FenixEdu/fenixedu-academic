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
<%@page contentType="text/html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app" %>
<html:xhtml/>

<style media="all">

body {
font-family: Verdana, Arial, Helvetica, sans-serif;
font-size: 84%;
margin: 0;
padding: 0;
}
</style>

<em><bean:message key="label.teacherService.credits"/></em>
<jsp:include page="teacherCreditsStyles.jsp"/>

<logic:present name="annualTeachingCreditsBean">
	<h3><bean:message key="label.teacherService.credits"/>&nbsp;<bean:write name="annualTeachingCreditsBean" property="executionYear.name"/></h3>
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
</logic:present>


<bean:define id="teacherId" name="annualTeachingCreditsBean" property="teacher.externalId"/>
<bean:define id="executionYearOid" name="annualTeachingCreditsBean" property="executionYear.externalId"/>

<h3 class="separator2 mtop2" style="background: #f4f4e4; border-bottom: 2px solid #eed; padding: 0.25em;"><img id="status-icon" width="15px" alt="" src="<%= request.getContextPath() +"/images/right30.png" %>">
<bean:message key="label.teacherCreditsSheet.professorships" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></h3>
	
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
					<bean:define id="professorshipID" name="professorship" property="externalId"/>
					<bean:define id="executionPeriodId" name="professorship" property="executionCourse.executionPeriod.externalId"/>
					<bean:define id="totalNumberOfLessons" name="professorship" property="degreeTeachingServiceLessonRows"/>
					<bean:size id="numberOfShifts" name="professorship" property="degreeTeachingServicesOrderedByShift"/>
					<tr>
						<td rowspan="<%= java.lang.Math.max(((Integer)totalNumberOfLessons).intValue(),1)%>">
								<bean:write name="professorship" property="executionCourse.name"/> (<bean:write name="professorship" property="degreeSiglas"/>)
						</td>
						<logic:equal name="numberOfShifts" value="0">
							<td colspan="7"/>
						</logic:equal>
						
						<logic:iterate id="degreeTeachingService" name="professorship" property="degreeTeachingServicesOrderedByShift" indexId="indexShifts">
							<bean:size id="numberOfLessons" name="degreeTeachingService" property="shift.lessonsOrderedByWeekDayAndStartTime"/>
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
								<td><logic:notEmpty name="lesson" property="sala">
									<bean:write name="lesson" property="sala.name"/>
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
							</logic:equal>
							</tr>
						</logic:iterate>
				</logic:iterate>
				</table>
			</logic:notEqual>
		</logic:iterate>
	

<h3 class="separator2 mtop2"><img id="status-icon" width="15px" alt="" src="<%= request.getContextPath() +"/images/right30.png" %>"> <bean:message key="label.teacherCreditsSheet.institutionWorkingTime" arg0="<%=org.fenixedu.academic.domain.organizationalStructure.Unit.getInstitutionAcronym()%>" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></h3>
	
		<logic:iterate id="annualTeachingCreditsByPeriodBean" name="annualTeachingCreditsBean" property="annualTeachingCreditsByPeriodBeans">
			<h3 class="infoop"><bean:write name="annualTeachingCreditsByPeriodBean" property="executionPeriod.qualifiedName"/></h3>
			<bean:define id="executionPeriodId" name="annualTeachingCreditsByPeriodBean" property="executionPeriod.externalId"/>
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
				</fr:layout>
			</fr:view>
		</logic:iterate>
	


<h3 class="separator2 mtop2"><img id="status-icon" width="15px" alt="" src="<%= request.getContextPath() +"/images/right30.png" %>"> <bean:message key="label.credits.masterDegreeTheses" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></h3>
	
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
	

<h3 class="separator2 mtop2"><img id="status-icon" width="15px" alt="" src="<%= request.getContextPath() +"/images/right30.png" %>"> <bean:message key="label.credits.phdDegreeTheses" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></h3>
	
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
	


<h3 class="separator2 mtop2"><img id="status-icon" width="15px" alt="" src="<%= request.getContextPath() +"/images/right30.png" %>"> <bean:message key="label.credits.projectsAndTutorials" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></h3>
	
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
		</logic:iterate>
	</logic:notEmpty>


<h3 class="separator2 mtop2"><img id="status-icon" width="15px" alt="" src="<%= request.getContextPath() +"/images/right30.png" %>"> <bean:message key="label.teacherCreditsSheet.managementPositionLines" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></h3>
	
		<logic:iterate id="annualTeachingCreditsByPeriodBean" name="annualTeachingCreditsBean" property="annualTeachingCreditsByPeriodBeans">
			<h3 class="infoop"><bean:write name="annualTeachingCreditsByPeriodBean" property="executionPeriod.qualifiedName"/></h3>
			<bean:define id="personFunctions" name="annualTeachingCreditsByPeriodBean" property="personFunctions"/>
			<logic:empty name="personFunctions">
				<bean:message key="label.teacherCreditsSheet.noDataFound" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
			</logic:empty>
			<logic:notEmpty name="personFunctions">
				<fr:view name="annualTeachingCreditsByPeriodBean" property="personFunctions">
					<fr:schema bundle="TEACHER_CREDITS_SHEET_RESOURCES" type="org.fenixedu.academic.domain.organizationalStructure.PersonFunction">
						<fr:slot name="function.name" key="label.managementPosition.position"/>
						<fr:slot name="function.unit.name" key="label.managementPosition.unit"/>
					</fr:schema>
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle2 thlight thleft mtop05 mbottom05"/>
					</fr:layout>
				</fr:view>
			</logic:notEmpty>
		</logic:iterate>

<h3 class="separator2 mtop2"><img id="status-icon" width="15px" alt="" src="<%= request.getContextPath() +"/images/right30.png" %>"> <bean:message key="label.teacherCreditsSheet.otherTypeCreditLines" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></h3>
	
		<logic:iterate id="annualTeachingCreditsByPeriodBean" name="annualTeachingCreditsBean" property="annualTeachingCreditsByPeriodBeans">
			<h3 class="infoop"><bean:write name="annualTeachingCreditsByPeriodBean" property="executionPeriod.qualifiedName"/></h3>
			<bean:define id="otherServices" name="annualTeachingCreditsByPeriodBean" property="otherServices"/>
			<logic:empty name="otherServices">
				<bean:message key="label.teacherCreditsSheet.noDataFound" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
			</logic:empty>
			<logic:notEmpty name="otherServices">
				<fr:view name="annualTeachingCreditsByPeriodBean" property="otherServices">
					<fr:schema bundle="TEACHER_CREDITS_SHEET_RESOURCES" type="org.fenixedu.academic.domain.teacher.OtherService">
						<fr:slot name="credits" key="label.credits"/>
						<fr:slot name="reason" key="label.otherTypeCreditLine.reason"/>
					</fr:schema>
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle2 thlight thleft mtop05 mbottom05"/>
					</fr:layout>
				</fr:view>
			</logic:notEmpty>
		</logic:iterate>


<h3 class="separator2 mtop2"><img id="status-icon" width="15px" alt="" src="<%= request.getContextPath() +"/images/right30.png" %>"> <bean:message key="label.credits.creditsReduction.definition" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></h3>
		<logic:equal name="annualTeachingCreditsBean" property="canSeeCreditsReduction" value="true">
			<fr:view name="annualTeachingCreditsBean" property="annualTeachingCreditsByPeriodBeans">
				<fr:schema bundle="TEACHER_CREDITS_SHEET_RESOURCES" type="org.fenixedu.academic.domain.credits.util.AnnualTeachingCreditsByPeriodBean">
					<fr:slot name="executionPeriod.name" key="label.period"/>
					<fr:slot name="requestCreditsReduction" key="label.requestedReductionCredits" layout="radio"/>
					<fr:slot name="creditsReductionServiceAttribute" key="label.attributedReductionCredits" layout="null-as-label"/>
				</fr:schema>
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle2 thlight thleft mtop05 mbottom05"/>
				</fr:layout>
			</fr:view>
		</logic:equal>
		<logic:notEqual name="annualTeachingCreditsBean" property="canSeeCreditsReduction" value="true">
			<bean:message key="label.confidentialInformation" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
		</logic:notEqual>


		
<h3 class="separator2 mtop2"><img id="status-icon" width="15px" alt="" src="<%= request.getContextPath() +"/images/right30.png" %>"> <bean:message key="label.teacherCreditsSheet.serviceExemptionLines" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></h3>
	
		<logic:iterate id="annualTeachingCreditsByPeriodBean" name="annualTeachingCreditsBean" property="annualTeachingCreditsByPeriodBeans">
			<h3 class="infoop"><bean:write name="annualTeachingCreditsByPeriodBean" property="executionPeriod.qualifiedName"/></h3>
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
	



	

<h3 class="separator2 mtop2"><img id="status-icon" width="15px" alt="" src="<%= request.getContextPath() +"/images/right30.png" %>"> <bean:message key="label.notes" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></h3>
	
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
				</fr:layout>
			</fr:view>
		</logic:notEmpty>
	
