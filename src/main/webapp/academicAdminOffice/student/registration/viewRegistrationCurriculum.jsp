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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/academic" prefix="academic" %>
<%@ page language="java" %>
<%@page import="net.sourceforge.fenixedu.domain.ExecutionYear"%>
<%@page import="net.sourceforge.fenixedu.domain.student.Registration"%>
<%@page import="net.sourceforge.fenixedu.domain.student.curriculum.AverageType"%>
<%@page import="net.sourceforge.fenixedu.domain.student.curriculum.ICurriculum"%>
<html:xhtml />

<h2><bean:message key="registration.curriculum" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<bean:define id="registrationCurriculumBean" name="registrationCurriculumBean" type="net.sourceforge.fenixedu.dataTransferObject.student.RegistrationCurriculumBean"/>
<%
	final Registration registration = registrationCurriculumBean.getRegistration();
	request.setAttribute("registration", registration);
	final ExecutionYear executionYear = registrationCurriculumBean.getExecutionYear();
	request.setAttribute("executionYear", executionYear);
%>

<academic:allowed operation="VIEW_FULL_STUDENT_CURRICULUM" program="<%= registration.getDegree() %>">
<p>
	<html:link page="/student.do?method=visualizeRegistration" paramId="registrationID" paramName="registration" paramProperty="externalId">
		<bean:message key="link.student.back" bundle="ACADEMIC_OFFICE_RESOURCES"/>
	</html:link>
</p>
</academic:allowed>


<div style="float: right;">
	<bean:define id="personID" name="registration" property="student.person.externalId"/>
	<html:img align="middle" src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveByID&amp;personCode="+personID.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES" styleClass="showphoto"/>
</div>

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

<logic:present name="registration" property="ingression">

<h3 class="mbottom05"><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>

<fr:view name="registration" schema="student.registrationDetail" >
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thright thlight mtop05"/>
		<fr:property name="rowClasses" value=",,tdhl1,,,,,,"/>
	</fr:layout>
</fr:view>
</logic:present>

<logic:notPresent name="registration" property="ingression">
<h3 class="mbottom05"><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
<fr:view name="registration" schema="student.registrationsWithStartData" >
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thright thlight mtop05"/>
	</fr:layout>
</fr:view>
</logic:notPresent>

<p class="mtop15 mbottom025"><strong>Visualizar Currículo:</strong></p>
<fr:form action="/registration.do?method=viewRegistrationCurriculum">
	<fr:edit id="registrationCurriculumBean" 
		name="registrationCurriculumBean"
		visible="false"/>
	<fr:edit id="registrationCurriculumBean-executionYear" name="registrationCurriculumBean" schema="registration-select-execution-year" >
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thright thlight mtop025 thmiddle"/>
			<fr:property name="columnClasses" value=",,tdclear"/>
		</fr:layout>
	</fr:edit>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit">
		<bean:message bundle="APPLICATION_RESOURCES" key="label.submit"/>
	</html:submit>
</fr:form>

<%
	final ICurriculum curriculum = registrationCurriculumBean.getCurriculum(executionYear);
	request.setAttribute("curriculum", curriculum);	

	request.setAttribute("sumPiCi", curriculum.getSumPiCi());
	request.setAttribute("sumPi", curriculum.getSumPi());
	request.setAttribute("weightedAverage", curriculum.getAverage());
	request.setAttribute("sumEctsCredits", curriculum.getSumEctsCredits());
	request.setAttribute("curricularYear", curriculum.getCurricularYear());
	request.setAttribute("totalCurricularYears", curriculum.getTotalCurricularYears());
%>

<logic:equal name="curriculum" property="empty" value="true">
	<p class="mvert15">
		<em>
			<bean:message key="no.approvements" bundle="ACADEMIC_OFFICE_RESOURCES"/>
		</em>
	</p>	
</logic:equal>

<logic:equal name="curriculum" property="empty" value="false">

	<div class="infoop2 mtop2">
		<logic:notEmpty name="executionYear">
			<p class="mvert05"><strong>Atenção:</strong><br/> <bean:message key="following.info.refers.to" bundle="ACADEMIC_OFFICE_RESOURCES"/><bean:message key="begin.of.execution.year" bundle="ACADEMIC_OFFICE_RESOURCES"/> <bean:write name="executionYear" property="year"/>.</p>
			<br/>
			<p class="mvert05"><strong><bean:message key="legal.value.info" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
			<p class="mvert05"><strong><bean:message key="rules.info" arg0="<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionName().getContent()%>" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>

			<p class="mtop1 mbottom05"><strong>Cálculo da <bean:message key="degree.average" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
			<p class="pleft1 mvert05"><bean:message key="degree.average" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <b class="highlight1"><bean:write name="weightedAverage"/></b></p>
			<p class="pleft1 mvert05"><bean:message key="rule" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <bean:message key="average.rule" bundle="ACADEMIC_OFFICE_RESOURCES"/></p>
			<p class="pleft1 mvert05"><bean:message key="result" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <bean:message key="degree.average.abbreviation" bundle="ACADEMIC_OFFICE_RESOURCES"/> = <bean:write name="sumPiCi"/> / <bean:write name="sumPi"/> = <b class="highlight1"><bean:write name="weightedAverage"/></b></p>

			<p class="mtop1 mbottom05"><strong>Cálculo do <bean:message key="curricular.year" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
			<p class="pleft1 mvert05"><bean:message key="curricular.year" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <b class="highlight1"><bean:write name="curricularYear"/></b></p>
			<p class="pleft1 mvert05"><bean:message key="rule" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <bean:message key="curricular.year.rule" bundle="ACADEMIC_OFFICE_RESOURCES"/></p>
			<p class="pleft1 mvert05"><bean:message key="result" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <bean:message key="curricular.year.abbreviation" bundle="ACADEMIC_OFFICE_RESOURCES"/> = <bean:message key="minimum" bundle="ACADEMIC_OFFICE_RESOURCES"/> (<bean:message key="int" bundle="ACADEMIC_OFFICE_RESOURCES"/> ( (<bean:write name="sumEctsCredits"/> + 24) / 60 + 1) ; <bean:write name="totalCurricularYears"/>) = <b class="highlight1"><bean:write name="curricularYear"/></b>;</p>
		</logic:notEmpty>

		<logic:empty name="executionYear">
			<p class="warning0"><strong>Atenção:</strong><br/> <bean:message key="following.info.refers.to" bundle="ACADEMIC_OFFICE_RESOURCES"/> <bean:message key="all.curriculum" bundle="ACADEMIC_OFFICE_RESOURCES"/></p>
			<br/>

			<logic:equal name="registrationCurriculumBean" property="conclusionProcessed" value="true">
				<p class="mvert05"><strong><bean:message key="final.degree.average.info" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
	
				<p class="mtop1 mbottom05"><strong><bean:message key="degree.average" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
				<p class="pleft1 mvert05"><bean:message key="degree.average.abbreviation" bundle="ACADEMIC_OFFICE_RESOURCES"/> = <b class="highlight1"><bean:write name="registrationCurriculumBean" property="finalAverage"/></b></p>
				<p class="mtop1 mbottom05"><strong><bean:message key="conclusion.date" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
				<p class="pleft1 mvert05"><b class="highlight1"><bean:write name="registrationCurriculumBean" property="conclusionDate"/></b></p>
			</logic:equal>
			<logic:equal name="registrationCurriculumBean" property="conclusionProcessed" value="false">
				<logic:equal name="registrationCurriculumBean" property="concluded" value="true">
					<p class="mvert05"><span class="error0"><strong><bean:message key="missing.final.average.info" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></span></p>
				</logic:equal>

				<p class="mvert05"><strong><bean:message key="legal.value.info" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
				<p class="mvert05"><strong><bean:message key="rules.info" arg0="<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionName().getContent()%>" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
	
				<p class="mtop1 mbottom05"><strong>Cálculo da <bean:message key="degree.average" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
				<p class="pleft1 mvert05"><bean:message key="degree.average" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <b class="highlight1"><bean:write name="weightedAverage"/></b></p>
				<p class="pleft1 mvert05"><bean:message key="rule" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <bean:message key="average.rule" bundle="ACADEMIC_OFFICE_RESOURCES"/></p>
				<p class="pleft1 mvert05"><bean:message key="result" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <bean:message key="degree.average.abbreviation" bundle="ACADEMIC_OFFICE_RESOURCES"/> = <bean:write name="sumPiCi"/> / <bean:write name="sumPi"/> = <b class="highlight1"><bean:write name="weightedAverage"/></b></p>
	
				<p class="mtop1 mbottom05"><strong>Cálculo do <bean:message key="curricular.year" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
				<p class="pleft1 mvert05"><bean:message key="curricular.year" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <b class="highlight1"><bean:write name="curricularYear"/></b></p>
				<p class="pleft1 mvert05"><bean:message key="rule" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <bean:message key="curricular.year.rule" bundle="ACADEMIC_OFFICE_RESOURCES"/></p>
				<p class="pleft1 mvert05"><bean:message key="result" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <bean:message key="curricular.year.abbreviation" bundle="ACADEMIC_OFFICE_RESOURCES"/> = <bean:message key="minimum" bundle="ACADEMIC_OFFICE_RESOURCES"/> (<bean:message key="int" bundle="ACADEMIC_OFFICE_RESOURCES"/> ( (<bean:write name="sumEctsCredits"/> + 24) / 60 + 1) ; <bean:write name="totalCurricularYears"/>) = <b class="highlight1"><bean:write name="curricularYear"/></b>;</p>
			</logic:equal>
		</logic:empty>
	</div>

	<table class="tstyle4 thlight tdcenter mtop15">
		<tr>
			<th><bean:message key="label.numberAprovedCurricularCourses" bundle="ACADEMIC_OFFICE_RESOURCES"/></th>
			<th><bean:message key="label.total.ects.credits" bundle="ACADEMIC_OFFICE_RESOURCES"/></th>
			<th><bean:message key="average" bundle="STUDENT_RESOURCES"/> Ponderada</th>
			<th><bean:message key="average" bundle="STUDENT_RESOURCES"/> Simples</th>
			<th><bean:message key="label.curricular.year" bundle="STUDENT_RESOURCES"/></th>
		</tr>
		<tr>
			<bean:size id="curricularEntriesCount" name="curriculum" property="curriculumEntries"/>
			<td><bean:write name="curricularEntriesCount"/></td>
			<td><bean:write name="sumEctsCredits"/></td>
			<logic:notEmpty name="executionYear">
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
			</logic:notEmpty>
			<logic:empty name="executionYear">
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
			</logic:empty>
		</tr>
	</table>

	<logic:equal name="curriculum" property="studentCurricularPlan.boxStructure" value="true">
<%--
		<p>
		<fr:view name="registration">
			<fr:layout name="registration-curriculum">
				<fr:property name="executionYear" value="<%=executionYear == null ? "" : executionYear.getYear()%>" />
			</fr:layout>
		</fr:view>
		</p>
--%>
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
				<td class=" scplancolgrade" style="width: 100px;">
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
