<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="net.sourceforge.fenixedu.domain.ExecutionYear"%>
<%@page import="net.sourceforge.fenixedu.domain.student.Registration"%>
<%@page import="net.sourceforge.fenixedu.domain.student.curriculum.AverageType"%>
<%@page import="net.sourceforge.fenixedu.domain.student.curriculum.ICurriculum"%>
<%@page import="net.sourceforge.fenixedu.dataTransferObject.student.RegistrationCurriculumBean"%>
<html:xhtml />

<h2><bean:message key="registration.curriculum" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<bean:define id="registrationCurriculumBean" name="registrationCurriculumBean" type="net.sourceforge.fenixedu.dataTransferObject.student.RegistrationCurriculumBean"/>
<%
	final Registration registration = ((RegistrationCurriculumBean) registrationCurriculumBean).getRegistration();
	request.setAttribute("registration", registration);

	// average
	ICurriculum curriculum = registrationCurriculumBean.getCurriculum();
	request.setAttribute("curriculum", curriculum);

	final BigDecimal sumPiCi = curriculum.getSumPiCi();
	request.setAttribute("sumPiCi", sumPiCi);

	final BigDecimal sumPi = curriculum.getSumPi();
	request.setAttribute("sumPi", sumPi);

	final BigDecimal average = curriculum.getAverage();
	request.setAttribute("weightedAverage", average);
	
	// curricular year
	final ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
	request.setAttribute("currentExecutionYear", currentExecutionYear);
	curriculum = registrationCurriculumBean.getCurriculum(currentExecutionYear);
	
	final BigDecimal sumEctsCredits = curriculum.getSumEctsCredits();
	request.setAttribute("sumEctsCredits", sumEctsCredits);
		
	final Integer curricularYear = curriculum.getCurricularYear();
	request.setAttribute("curricularYear", curricularYear);
%>

<%-- Person and Student short info --%>
<p class="mvert2">
	<span class="showpersonid">
	<bean:message key="label.student" bundle="ACADEMIC_OFFICE_RESOURCES"/>: 
		<fr:view name="registration" property="student" schema="student.show.personAndStudentInformation.short">
			<fr:layout name="flow">
				<fr:property name="labelExcluded" value="true"/>
			</fr:layout>
		</fr:view>
	</span>
</p>

<logic:equal name="curriculum" property="empty" value="true">
	<p class="mvert15">
		<em>
			<bean:message key="no.approvements" bundle="ACADEMIC_OFFICE_RESOURCES"/>
		</em>
	</p>	
</logic:equal>

<logic:equal name="curriculum" property="empty" value="false">

	<logic:equal name="registrationCurriculumBean" property="conclusionProcessed" value="true">
		<div class="infoop2 mvert2">
			<p class="mvert05"><strong><bean:message key="final.degree.average.info" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>

			<p class="mtop1 mbottom05"><strong><bean:message key="degree.average" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
			<p class="pleft1 mvert05"><bean:message key="degree.average.abbreviation" bundle="ACADEMIC_OFFICE_RESOURCES"/> = <b class="highlight1"><bean:write name="registrationCurriculumBean" property="finalAverage"/></b></p>
			<p class="mtop1 mbottom05"><strong><bean:message key="conclusion.date" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
			<p class="pleft1 mvert05"><b class="highlight1"><bean:write name="registrationCurriculumBean" property="conclusionDate"/></b></p>
		</div>	
	</logic:equal>
		
	<logic:equal name="registrationCurriculumBean" property="conclusionProcessed" value="false">

		<logic:equal name="registrationCurriculumBean" property="concluded" value="true">
			<div class="infoop2 mvert2">
				<p class="mvert05"><span class="error0"><strong><bean:message key="missing.final.average.info" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></span></p>
			</div>
		</logic:equal>

		<div class="infoop2 mvert2">
			<p class="mvert05"><strong><bean:message key="legal.value.info" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
			<p class="mvert05"><strong><bean:message key="rules.info" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
	
			<p class="mtop1 mbottom05"><strong><bean:message key="degree.average.is.current.info" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
			<p class="pleft1 mvert05"><bean:message key="degree.average" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <b class="highlight1"><bean:write name="weightedAverage"/></b></p>
			<p class="pleft1 mvert05"><bean:message key="rule" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <bean:message key="average.rule" bundle="ACADEMIC_OFFICE_RESOURCES"/></p>
			<p class="pleft1 mvert05"><bean:message key="result" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <bean:message key="degree.average.abbreviation" bundle="ACADEMIC_OFFICE_RESOURCES"/> = <bean:write name="sumPiCi"/> / <bean:write name="sumPi"/> = <b class="highlight1"><bean:write name="weightedAverage"/></b></p>
	
			<p class="mtop1 mbottom05"><strong><bean:message key="curricular.year.in.begin.of.execution.year.info" bundle="ACADEMIC_OFFICE_RESOURCES"/> <bean:write name="currentExecutionYear" property="year"/></strong>.</p>
			<p class="pleft1 mvert05"><bean:message key="curricular.year" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <b class="highlight1"><bean:write name="curricularYear"/></b></p>
			<p class="pleft1 mvert05"><bean:message key="rule" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <bean:message key="curricular.year.rule" bundle="ACADEMIC_OFFICE_RESOURCES"/></p>
			<p class="pleft1 mvert05"><bean:message key="result" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <bean:message key="curricular.year.abbreviation" bundle="ACADEMIC_OFFICE_RESOURCES"/> = <bean:message key="minimum" bundle="ACADEMIC_OFFICE_RESOURCES"/> (<bean:message key="int" bundle="ACADEMIC_OFFICE_RESOURCES"/> ( (<bean:write name="sumEctsCredits"/> + 24) / 60 + 1) ; <bean:write name="registrationCurriculumBean" property="curriculum.totalCurricularYears"/>) = <b class="highlight1"><bean:write name="curricularYear"/></b>;</p>
		</div>

	</logic:equal>

	<table class="tstyle4 thlight tdcenter mtop15">
		<tr>
			<th><bean:message key="label.numberAprovedCurricularCourses" bundle="ACADEMIC_OFFICE_RESOURCES"/></th>
			<th><bean:message key="label.total.ects.credits" bundle="ACADEMIC_OFFICE_RESOURCES"/></th>
			<th><bean:message key="average" bundle="STUDENT_RESOURCES"/> <bean:message key="AverageType.WEIGHTED" bundle="ENUMERATION_RESOURCES"/></th>
			<th><bean:message key="average" bundle="STUDENT_RESOURCES"/> <bean:message key="AverageType.SIMPLE" bundle="ENUMERATION_RESOURCES"/></th>
			<th><bean:message key="label.curricular.year" bundle="STUDENT_RESOURCES"/></th>
		</tr>
		<tr>
			<bean:size id="curricularEntriesCount" name="curriculum" property="curriculumEntries"/>
			<td><bean:write name="curricularEntriesCount"/></td>
			<td><bean:write name="sumEctsCredits"/></td>
			<logic:equal name="registrationCurriculumBean" property="conclusionProcessed" value="false">
				<td><bean:write name="weightedAverage"/></td>
				<logic:equal name="curriculum" property="studentCurricularPlan.averageType.name" value="WEIGHTED">
					<td>-</td>
				</logic:equal>
				<logic:notEqual name="curriculum" property="studentCurricularPlan.averageType.name" value="WEIGHTED">
					<%
						curriculum.setAverageType(AverageType.SIMPLE);
						request.setAttribute("simpleAverage", curriculum.getAverage());
					%>
					<td><bean:write name="simpleAverage"/></td>
				</logic:notEqual>
				<td><bean:write name="curricularYear"/></td>
			</logic:equal>
			<logic:equal name="registrationCurriculumBean" property="conclusionProcessed" value="true">
				<logic:equal name="curriculum" property="studentCurricularPlan.averageType.name" value="WEIGHTED">
					<td>
							<bean:write name="registrationCurriculumBean" property="finalAverage"/>
					</td>
					<td>-</td>
				</logic:equal>
				<logic:notEqual name="curriculum" property="studentCurricularPlan.averageType.name" value="WEIGHTED">
					<td>-</td>
					<td>
						<bean:write name="registrationCurriculumBean" property="finalAverage"/>
					</td>
				</logic:notEqual>
				<td>-</td>
			</logic:equal>			
		</tr>
	</table>

	<logic:equal name="curriculum" property="studentCurricularPlan.boxStructure" value="true">
		<p>
			<fr:view name="curriculum"/>
		</p>
	</logic:equal>
	
	<logic:equal name="curriculum" property="studentCurricularPlan.boxStructure" value="false">
		<bean:define id="curriculumEntries" name="curriculum" property="curriculumEntries"/>
		<table class="scplan">
			<tr class="scplangroup">
				<td class=" scplancolcurricularcourse" rowspan="2" colspan="2">
					<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.curricular.course.from.curriculum"/>
				</td>
				<td class=" scplancolgrade"colspan="3">
					<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="degree.average"/>
				</td>
				<td class=" scplancolgrade">
					<bean:message key="curricular.year" bundle="ACADEMIC_OFFICE_RESOURCES"/>
				</td>
			</tr>
			<tr class="scplangroup">
				<td class=" scplancolgrade">
					<bean:message bundle="APPLICATION_RESOURCES" key="label.grade"/>
				</td>
				<td class=" scplancolgrade">
					<bean:message bundle="APPLICATION_RESOURCES" key="label.weight"/>
				</td>
				<td class=" scplancolgrade" style="width: 100px;">
					(<bean:message bundle="APPLICATION_RESOURCES" key="label.weight"/> x <bean:message bundle="APPLICATION_RESOURCES" key="label.grade"/>)
				</td>
				<td  class=" scplancolgrade" style="width: 100px;">
					<bean:message bundle="APPLICATION_RESOURCES" key="label.credits"/>
				</td>
			</tr>
			<logic:iterate id="curriculumEntry" name="curriculumEntries">
				<logic:equal name="curriculumEntry" property="class.name" value="net.sourceforge.fenixedu.domain.student.curriculum.NotInDegreeCurriculumCurriculumEntry">
					<tr class="scplanenrollment">
						<td><bean:write name="curriculumEntry" property="enrolment.curricularCourse.code"/></td>
						<td class=" scplancolcurricularcourse"><bean:write name="curriculumEntry" property="enrolment.curricularCourse.name"/></td>
						<td class=" scplancolgrade"><bean:write name="curriculumEntry" property="enrolment.latestEnrolmentEvaluation.gradeValue"/></td>						
						<td class=" scplancolweight"><bean:write name="curriculumEntry" property="weigthForCurriculum"/></td>
						<td class=" scplancolweight">
							<logic:empty name="curriculumEntry" property="weigthTimesGrade">
								-
							</logic:empty>
							<logic:notEmpty name="curriculumEntry" property="weigthTimesGrade">
								<bean:write name="curriculumEntry" property="weigthTimesGrade"/>
							</logic:notEmpty>
						</td>
						<td class=" scplancolects">
							<logic:empty name="curriculumEntry" property="ectsCreditsForCurriculum">
								-
							</logic:empty>
							<logic:notEmpty name="curriculumEntry" property="ectsCreditsForCurriculum">
								<bean:write name="curriculumEntry" property="ectsCreditsForCurriculum"/>
							</logic:notEmpty>
						</td>
					</tr>
				</logic:equal>
			</logic:iterate>				
			<logic:iterate id="curriculumEntry" name="curriculumEntries">
				<logic:equal name="curriculumEntry" property="class.name" value="net.sourceforge.fenixedu.domain.student.curriculum.GivenCreditsEntry">
					<tr class="scplanenrollment">
						<td class="acenter">-</td>
						<td class=" scplancolcurricularcourse"><bean:message bundle="APPLICATION_RESOURCES" key="label.givenCredits"/></td>
						<td class=" scplancolgrade">-</td>						
						<td class=" scplancolweight">-</td>
						<td class=" scplancolweight">-</td>
						<td class=" scplancolects">
							<logic:empty name="curriculumEntry" property="ectsCreditsForCurriculum">
								-
							</logic:empty>
							<logic:notEmpty name="curriculumEntry" property="ectsCreditsForCurriculum">
								<bean:write name="curriculumEntry" property="ectsCreditsForCurriculum"/>
							</logic:notEmpty>
						</td>
					</tr>
				</logic:equal>
			</logic:iterate>				
			<tr class="scplanenrollment">
				<td colspan="3" style="text-align: right;">
					Somatórios
				</td>
				<td class=" scplancolweight">
					<bean:write name="sumPi"/>
				</td>
				<td class=" scplancolweight">
					<bean:write name="sumPiCi"/>
				</td>
				<td class=" scplancolects">
					<bean:write name="sumEctsCredits"/>
				</td>
			</tr>
		</table>
	</logic:equal>

</logic:equal>
