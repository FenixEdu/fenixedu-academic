<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@page import="org.fenixedu.academic.domain.ExecutionYear"%>
<%@page import="org.fenixedu.academic.domain.student.Registration"%>
<%@page import="org.fenixedu.academic.domain.student.curriculum.AverageType"%>
<%@page import="org.fenixedu.academic.domain.student.curriculum.ICurriculum"%>

<html:xhtml/>


<h2><bean:message key="registration.curriculum" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<bean:define id="registrationCurriculumBean" name="registrationCurriculumBean" type="org.fenixedu.academic.dto.student.RegistrationCurriculumBean"/>
<%
	final Registration registration = registrationCurriculumBean.getRegistration();
	request.setAttribute("registration", registration);
	final ExecutionYear executionYear = registrationCurriculumBean.getExecutionYear();
	request.setAttribute("executionYear", executionYear);
%>


<p>
	<html:link page="/student.do?method=visualizeRegistration" paramId="registrationID" paramName="registration" paramProperty="externalId">
		<bean:message key="link.student.back" bundle="ACADEMIC_OFFICE_RESOURCES"/>
	</html:link>
</p>


<div style="float: right;">
	<bean:define id="personID" name="registration" property="student.person.username"/>
	<html:img align="middle" src="<%= request.getContextPath() +"/user/photo/" + personID.toString() %>" altKey="personPhoto" bundle="IMAGE_RESOURCES" styleClass="showphoto"/>
</div>

<p class="mvert2">
	<span style="background-color: #ebf5ea; border-bottom: 1px solid #cce5c7; padding: 0.4em 0.6em;">
	<bean:message key="label.student" bundle="ACADEMIC_OFFICE_RESOURCES"/>: 
		<fr:view name="registration" property="student" schema="student.show.personAndStudentInformation.short">
			<fr:layout name="flow">
				<fr:property name="labelExcluded" value="true"/>
			</fr:layout>
		</fr:view>
	</span>
</p>

<logic:present name="registration" property="ingressionType">
<h3 class="mbottom025"><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
<fr:view name="registration" schema="student.registrationDetail" >
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thright thlight mtop025"/>
		<fr:property name="rowClasses" value=",,tdhl1,,,,,,"/>
	</fr:layout>
</fr:view>
</logic:present>

<logic:notPresent name="registration" property="ingressionType">
<h3 class="mbottom025"><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
<fr:view name="registration" schema="student.registrationsWithStartData" >
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thright thlight mtop025"/>
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
			<p class="mvert05"><strong><bean:message key="rules.info" arg0="<%=org.fenixedu.academic.domain.organizationalStructure.Unit.getInstitutionName().getContent()%>" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>

			<p class="mtop1 mbottom05"><strong>Cálculo da <bean:message key="degree.average" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
			<p class="pleft1 mvert05"><bean:message key="degree.average" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <b class="highlight1"><bean:write name="weightedAverage"/></b></p>
			<p class="pleft1 mvert05"><bean:message key="rule" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <bean:message key="average.rule" bundle="ACADEMIC_OFFICE_RESOURCES"/></p>

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
				<logic:equal name="registration" property="concluded" value="true">
					<p class="mvert05"><span class="error0"><strong><bean:message key="missing.final.average.info" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></span></p>
				</logic:equal>

				<p class="mvert05"><strong><bean:message key="legal.value.info" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
				<p class="mvert05"><strong><bean:message key="rules.info" arg0="<%=org.fenixedu.academic.domain.organizationalStructure.Unit.getInstitutionName().getContent()%>" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
	
				<p class="mtop1 mbottom05"><strong>Cálculo da <bean:message key="degree.average" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
				<p class="pleft1 mvert05"><bean:message key="degree.average" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <b class="highlight1"><bean:write name="weightedAverage"/></b></p>
				<p class="pleft1 mvert05"><bean:message key="rule" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <bean:message key="average.rule" bundle="ACADEMIC_OFFICE_RESOURCES"/></p>
	
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
					<td>
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
					</td>
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

		<p>
			<fr:view name="curriculum"/>
		</p>
</logic:equal>

