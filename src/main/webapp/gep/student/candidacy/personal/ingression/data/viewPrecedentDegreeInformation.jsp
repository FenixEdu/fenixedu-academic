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
<%@page import="pt.ist.fenixframework.FenixFramework"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<%@ page import="net.sourceforge.fenixedu.domain.student.PrecedentDegreeInformation" %>

<% 

request.setAttribute("precedentDegreeInformationTemp", 
	FenixFramework.getDomainObject(request.getParameter("precedentDegreeInformationId")));

%>

<bean:define id="precedentDegreeInformation" name="precedentDegreeInformationTemp" />

<logic:notEmpty name="precedentDegreeInformation">
					
					<fr:view name="precedentDegreeInformation">
						<fr:schema type="net.sourceforge.fenixedu.domain.student.PrecedentDegreeInformation" bundle="GEP_RESOURCES" >
						
							<logic:notEmpty name="precedentDegreeInformation" property="degreeDesignation">
								<fr:slot name="degreeDesignation" layout="null-as-label" key="label.personal.ingression.data.viewer.degreeDesignation" />
							</logic:notEmpty>
	
							<logic:notEmpty name="precedentDegreeInformation" property="institution">
								<fr:slot name="institution.name" layout="null-as-label" key="label.personal.ingression.data.viewer.institution" />
							</logic:notEmpty>
	
	
							<logic:notEmpty name="precedentDegreeInformation" property="country">
								<fr:slot name="country.name" layout="null-as-label" key="label.personal.ingression.data.viewer.country" />
							</logic:notEmpty>
	
	
							<logic:notEmpty name="precedentDegreeInformation" property="schoolLevel">
								<fr:slot name="schoolLevel.localizedName" layout="null-as-label" key="label.personal.ingression.data.viewer.schoolLevel" />
							</logic:notEmpty>
	
	
							<logic:notEmpty name="precedentDegreeInformation" property="otherSchoolLevel">
								<fr:slot name="otherSchoolLevel" layout="null-as-label" key="label.personal.ingression.data.viewer.otherSchoolLevel" />
							</logic:notEmpty>
						
	
	
							<logic:notEmpty name="precedentDegreeInformation" property="precedentDegreeDesignation">
								<fr:slot name="precedentDegreeDesignation" layout="null-as-label" key="label.personal.ingression.data.viewer.precedentDegreeDesignation" />
							</logic:notEmpty>
	
	
							<logic:notEmpty name="precedentDegreeInformation" property="precedentInstitution">
								<fr:slot name="precedentInstitution.name" layout="null-as-label" key="label.personal.ingression.data.viewer.precedentInstitution" />
							</logic:notEmpty>
	
	
							<logic:notEmpty name="precedentDegreeInformation" property="precedentCountry">
								<fr:slot name="precedentCountry.name" layout="null-as-label" key="label.personal.ingression.data.viewer.precedentCountry" />
							</logic:notEmpty>
	
	
							<logic:notEmpty name="precedentDegreeInformation" property="precedentSchoolLevel">
								<fr:slot name="precedentSchoolLevel.localizedName" layout="null-as-label" key="label.personal.ingression.data.viewer.precedentSchoolLevel" />
							</logic:notEmpty>
	
	
							<logic:notEmpty name="precedentDegreeInformation" property="sourceInstitution">
								<fr:slot name="sourceInstitution.name" layout="null-as-label" key="label.personal.ingression.data.viewer.sourceInstitution" />
							</logic:notEmpty>
	
	
							<logic:notEmpty name="precedentDegreeInformation" property="numberOfEnrolmentsInPreviousDegrees">
								<fr:slot name="numberOfEnrolmentsInPreviousDegrees" layout="null-as-label" key="label.personal.ingression.data.viewer.numberOfEnrolmentsInPreviousDegrees" />
							</logic:notEmpty>
	
	
							<logic:notEmpty name="precedentDegreeInformation" property="numberOfEnroledCurricularCourses">
								<fr:slot name="numberOfEnroledCurricularCourses" layout="null-as-label" key="label.personal.ingression.data.viewer.numberOfEnroledCurricularCourses" />
							</logic:notEmpty>
	
	
							<logic:notEmpty name="precedentDegreeInformation" property="numberOfApprovedCurricularCourses">
								<fr:slot name="numberOfApprovedCurricularCourses" layout="null-as-label" key="label.personal.ingression.data.viewer.numberOfApprovedCurricularCourses" />
							</logic:notEmpty>
	
	
							<logic:notEmpty name="precedentDegreeInformation" property="approvedEcts">
								<fr:slot name="approvedEcts" layout="null-as-label" key="label.personal.ingression.data.viewer.approvedEcts" />
							</logic:notEmpty>
	
	
							<logic:notEmpty name="precedentDegreeInformation" property="enroledEcts">
								<fr:slot name="enroledEcts" layout="null-as-label" key="label.personal.ingression.data.viewer.enroledEcts" />
							</logic:notEmpty>
							
				
							<logic:notEmpty name="precedentDegreeInformation" property="conclusionGrade">
								<fr:slot name="conclusionGrade" layout="null-as-label" key="label.personal.ingression.data.viewer.conclusionGrade" />
							</logic:notEmpty>
	
	
							<logic:notEmpty name="precedentDegreeInformation" property="conclusionYear">
								<fr:slot name="conclusionYear" layout="null-as-label" key="label.personal.ingression.data.viewer.conclusionYear" />
							</logic:notEmpty>
	
	
							<logic:notEmpty name="precedentDegreeInformation" property="conclusionDate">
								<fr:slot name="conclusionDate" layout="null-as-label" key="label.personal.ingression.data.viewer.conclusionDate" />
							</logic:notEmpty>
	
	
							<logic:notEmpty name="precedentDegreeInformation" property="mobilityProgramDuration">
								<fr:slot name="mobilityProgramDuration" layout="null-as-label" key="label.personal.ingression.data.viewer.mobilityProgramDuration" />
							</logic:notEmpty>
	
	
							<logic:notEmpty name="precedentDegreeInformation" property="lastModifiedDate">
								<fr:slot name="lastModifiedDate" layout="null-as-label" key="label.personal.ingression.data.viewer.lastModifiedDate" />
							</logic:notEmpty>
	
	
							<logic:notEmpty name="precedentDegreeInformation" property="cycleType">
								<fr:slot name="cycleType.localizedName" layout="null-as-label" key="label.personal.ingression.data.viewer.cycleType" />
							</logic:notEmpty>
	
	
							<logic:notEmpty name="precedentDegreeInformation" property="gradeSum">
								<fr:slot name="gradeSum" layout="null-as-label" key="label.personal.ingression.data.viewer.gradeSum" />
							</logic:notEmpty>
	
	
							<logic:notEmpty name="precedentDegreeInformation" property="student">
								<fr:slot name="student.degree.presentationName" layout="null-as-label" key="label.personal.ingression.data.viewer.registration.degree" />
								<fr:slot name="student.startExecutionYear.name" layout="null-as-label" key="label.personal.ingression.data.viewer.registration.startExecutionYear" />
							</logic:notEmpty>
	
							<logic:notEmpty name="precedentDegreeInformation" property="registration">
								<fr:slot name="registration.degree.presentationName" layout="null-as-label" key="label.personal.ingression.data.viewer.registration.degree" />
								<fr:slot name="registration.startExecutionYear.name" layout="null-as-label" key="label.personal.ingression.data.viewer.registration.startExecutionYear" />
							</logic:notEmpty>
	
							<logic:notEmpty name="precedentDegreeInformation" property="personalIngressionData">
								<fr:slot name="personalIngressionData.executionYear.name" layout="null-as-label" key="label.personal.ingression.data.viewer.personalIngressionData.executionYear" />
							</logic:notEmpty>
							
							<logic:notEmpty name="precedentDegreeInformation" property="studentCandidacy">
								<fr:slot name="studentCandidacy.executionDegree.degree.presentationName" layout="null-as-label" key="label.personal.ingression.data.viewer.studentCandidacy.executionDegree" />
								<fr:slot name="studentCandidacy.executionDegree.executionYear.name" layout="null-as-label" key="label.personal.ingression.data.viewer.studentCandidacy.executionDegree.executionYear" />
							</logic:notEmpty>
	
							<logic:notEmpty name="precedentDegreeInformation" property="individualCandidacy">
								<fr:slot name="individualCandidacy.candidacyProcess.candidacyExecutionInterval.name" layout="null-as-label" key="label.personal.ingression.data.viewer.individualCandidacy.executionYear" />
							</logic:notEmpty>
							
							<logic:notEmpty name="precedentDegreeInformation" property="studentCurricularPlan">
								<fr:slot name="studentCurricularPlan.degree.name" layout="null-as-label" key="label.personal.ingression.data.viewer.studentCurricularPlan" />
							</logic:notEmpty>
							
						</fr:schema>
						
						<fr:layout name="tabular">
							<fr:property name="classes" value="tstyle1" />
						</fr:layout>
						
					</fr:view>
</logic:notEmpty>