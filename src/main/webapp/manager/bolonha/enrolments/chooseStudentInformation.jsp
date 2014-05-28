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
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>

<%@page import="net.sourceforge.fenixedu.domain.StudentCurricularPlan"%><html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>


<logic:present role="role(MANAGER)">
	<h2><bean:message key="title.student.curriculum" bundle="APPLICATION_RESOURCES" /></h2>

	<fr:hasMessages for="student-number-bean" type="conversion">
		<ul class="nobullet list6">
			<fr:messages>
				<li><span class="error0"><fr:message /></span></li>
			</fr:messages>
		</ul>
	</fr:hasMessages>
	
	<logic:messagesPresent message="true" property="success">
		<div class="success5 mbottom05" style="width: 700px;">
			<html:messages id="messages" message="true" bundle="APPLICATION_RESOURCES" property="success">
				<p class="mvert025"><bean:write name="messages" /></p>
			</html:messages>
		</div>
	</logic:messagesPresent>

	<fr:form action="/bolonhaStudentEnrolment.do?method=prepareSearchStudent">

		<fr:edit id="student-number-bean" name="studentNumberBean" schema="StudentNumberBean.edit">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thlight thright mtop05" />
			</fr:layout>
		</fr:edit>

		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message bundle="APPLICATION_RESOURCES" key="label.submit" /></html:submit>
	</fr:form>
	
	<logic:present name="registrations">
		<logic:notEmpty name="registrations">
			<br/>
			<logic:iterate id="registration" name="registrations">
				<br/>

				<logic:empty name="registration" property="lastStudentCurricularPlan" >
					<strong><bean:write name="registration" property="degreeName" /></strong> (<bean:message name="registration" property="activeStateType.qualifiedName" bundle="ENUMERATION_RESOURCES" />)
				</logic:empty>

				<%-- show operations   --%>
				<logic:notEmpty name="registration" property="lastStudentCurricularPlan" >
				
					<strong><bean:write name="registration" property="lastStudentCurricularPlan.degreeCurricularPlan.degree.sigla"/> - <bean:write name="registration" property="lastStudentCurricularPlan.degreeCurricularPlan.degree.name"/></strong> (<bean:message name="registration" property="activeStateType.qualifiedName" bundle="ENUMERATION_RESOURCES" />)

					<logic:notEmpty name="registration" property="lastStudentCurricularPlan.externalCurriculumGroups">
						<html:link action="/bolonhaStudentEnrolment.do?method=separateCycles" paramId="scpOid" paramName="registration" paramProperty="lastStudentCurricularPlan.externalId" >
							<bean:message  key="student.separateCycle" bundle="ACADEMIC_OFFICE_RESOURCES"/>
						</html:link>
						,
					</logic:notEmpty>

					<html:link action="/bolonhaStudentEnrolment.do?method=showRegistrationStatesLog" paramId="registrationId" paramName="registration" paramProperty="externalId" ><bean:message  key="student.registration.states.log" bundle="APPLICATION_RESOURCES"/></html:link>
				
				</logic:notEmpty>
				
				<%-- show student curricular plans information --%>
				<logic:notEmpty name="registration" property="studentCurricularPlans">
					
					<fr:view name="registration" property="studentCurricularPlans">
						
						<fr:schema bundle="ACADEMIC_OFFICE_RESOURCES" type="<%= StudentCurricularPlan.class.getName() %>">
							<fr:slot name="startDateYearMonthDay" key="label.startDate" />
							<fr:slot name="degreeCurricularPlan.degree.tipoCurso.localizedName" key="label.degreeType" />
							<fr:slot name="degreeCurricularPlan.name" key="label.curricularPlan" />
						</fr:schema>
						
						<fr:layout name="tabular">
							<fr:property name="classes" value="tstyle4" />
					
							<fr:link name="view" label="link.student.viewCurriculum,ACADEMIC_OFFICE_RESOURCES" order="1"
							 	link="/bolonhaStudentEnrolment.do?method=viewStudentCurriculum&scpId=${externalId}" />
							 	
							<fr:link name="move" label="label.course.moveEnrolments,ACADEMIC_OFFICE_RESOURCES" order="3"
							 	link="/curriculumLinesLocationManagement.do?method=prepareWithoutRules&scpID=${externalId}"
							 	condition="!transition" />

							<fr:link name="transit" label="link.student.transitToBolonha,ACADEMIC_OFFICE_RESOURCES" order="5"
							 	link="/bolonhaStudentEnrolment.do?method=prepareTransit&scpId=${externalId}"
							 	condition="transition" />
							 	
						</fr:layout>
					</fr:view>

				</logic:notEmpty>
			</logic:iterate>
		</logic:notEmpty>
	</logic:present>

</logic:present>