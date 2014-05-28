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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<%@ page import="net.sourceforge.fenixedu.domain.candidacyProcess.InstitutionPrecedentDegreeInformation" %>
<%@ page import="net.sourceforge.fenixedu.domain.candidacyProcess.ExternalPrecedentDegreeInformation" %>
<%@ page import="net.sourceforge.fenixedu.domain.student.PrecedentDegreeInformation" %>
<%@ page import="net.sourceforge.fenixedu.domain.phd.candidacy.PHDProgramCandidacy" %>


<html:xhtml/>

<logic:present role="role(MANAGER)">

<h2><bean:message key="title.personal.ingression.data.viewer.student.raides.data.view" bundle="GEP_RESOURCES" /></h2>

<strong><bean:message key="label.personal.ingression.data.viewer.student.data" bundle="GEP_RESOURCES" /></strong>

<fr:view name="student">
	<fr:schema type="net.sourceforge.fenixedu.domain.student.Student" bundle="GEP_RESOURCES">
		<fr:slot name="number" key="label.personal.ingression.data.viewer.student.number" />
		<fr:slot name="person.name" key="label.personal.ingression.data.viewer.student.name" />
	</fr:schema>
	
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1" />
	</fr:layout>
</fr:view>

<p><strong><bean:message key="label.personal.ingression.data.viewer.registrations" bundle="GEP_RESOURCES" /></strong></p>

<bean:define id="registrationList" name="student" property="registrations" />

<logic:empty name="registrationList">
	</p><em><bean:message key="message.personal.ingression.data.viewer.student.lack.registrations" bundle="GEP_RESOURCES" /></em></p>
</logic:empty>

<logic:notEmpty name="registrationList">
	
	<logic:iterate id="registration" name="registrationList">
	
		<fr:view name="registration">
			<fr:schema type="net.sourceforge.fenixedu.domain.student.Registration" bundle="GEP_RESOURCES">
				<fr:slot name="number" key="label.personal.ingression.data.viewer.student.number" />
				<fr:slot name="degree.presentationName" key="label.personal.ingression.data.viewer.registration.degree" />
				<fr:slot name="startExecutionYear.name" key="label.personal.ingression.data.viewer.registration.startExecutionYear" />
			</fr:schema>
	
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1" />
			</fr:layout>
	
		</fr:view>
	
		<div style="margin-left: 100px">
			<p><strong><bean:message key="label.personal.ingression.data.viewer.precedent.degree.info" bundle="GEP_RESOURCES" /> (1-*)</strong></p>
			
			<bean:define id="precedentDegreeInformationList" name="registration" property="precedentDegreesInformations" />
			
			<logic:empty name="precedentDegreeInformationList">
				<p><em><bean:message key="message.personal.ingression.data.viewer.student.lack.precedent.degree.information" bundle="GEP_RESOURCES" /></em></p>
			</logic:empty>
			
			<logic:notEmpty name="precedentDegreeInformationList">

				<logic:iterate id="precedentDegreeInformation" name="precedentDegreeInformationList" type="PrecedentDegreeInformation">
				
					<bean:define id="precedentDegreeInformationId" name="precedentDegreeInformation" property="externalId" type="String" />
									
					<jsp:include page="/gep/student/candidacy/personal/ingression/data/viewPrecedentDegreeInformation.jsp">
						<jsp:param name="precedentDegreeInformationId" value="<%= precedentDegreeInformationId %>" />
					</jsp:include>
					
					
				</logic:iterate>
				
			</logic:notEmpty>
		</div>

		<div style="margin-left: 100px">
			<p><strong><bean:message key="label.personal.ingression.data.viewer.precedent.degree.info" bundle="GEP_RESOURCES" /> (1-1 *DEPRECATED*)</strong></p>
			
			<logic:empty name="registration" property="precedentDegreeInformation">
				<p><em><bean:message key="message.personal.ingression.data.viewer.student.lack.precedent.degree.information" bundle="GEP_RESOURCES" /></em></p>
			</logic:empty>
			
			<logic:notEmpty name="registration" property="precedentDegreeInformation">
			
				<bean:define id="precedentDegreeInformation" name="registration" property="precedentDegreeInformation" />
				<bean:define id="precedentDegreeInformationId" name="precedentDegreeInformation" property="externalId" type="String" />
								
				<jsp:include page="/gep/student/candidacy/personal/ingression/data/viewPrecedentDegreeInformation.jsp">
					<jsp:param name="precedentDegreeInformationId" value="<%= precedentDegreeInformationId %>" />
				</jsp:include>
				
			</logic:notEmpty>
		</div>
	
	</logic:iterate>
	
	
</logic:notEmpty>


<hr />


<p><strong><bean:message key="label.personal.ingression.data.viewer.phdIndividualProgramProcesses" bundle="GEP_RESOURCES" /></strong></p>

<bean:define id="person" name="student" property="person" />

<bean:define id="phdIndividualProgramProcessList" name="person" property="phdIndividualProgramProcesses" />

<logic:empty name="phdIndividualProgramProcessList">
	</p><em><bean:message key="message.personal.ingression.data.viewer.student.lack.phdIndividualProgramProcesses" bundle="GEP_RESOURCES" /></em></p>
</logic:empty>

<logic:notEmpty name="phdIndividualProgramProcessList">
	
	<logic:iterate id="phdIndividualProgramProcess" name="phdIndividualProgramProcessList">
		
		<fr:view name="phdIndividualProgramProcess">
			<fr:schema type="net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess" bundle="GEP_RESOURCES">
				<fr:slot name="phdProgram.name" layout="null-as-label" key="label.personal.ingression.data.viewer.phd.program.name"/>
				<fr:slot name="executionYear.name" layout="null-as-label" key="label.personal.ingression.data.viewer.phd.start.execution.year" />
			</fr:schema>
			
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1" />
			</fr:layout>			
		</fr:view>
		
		<div style="margin-left: 100px">
			<p><strong><bean:message key="label.personal.ingression.data.viewer.precedent.degree.info" bundle="GEP_RESOURCES" /> (1-*)</strong></p>
			
			<bean:define id="precedentDegreeInformationList" name="phdIndividualProgramProcess" property="precedentDegreeInformations" />
			
			<logic:empty name="precedentDegreeInformationList">
				<p><em><bean:message key="message.personal.ingression.data.viewer.student.lack.precedent.degree.information" bundle="GEP_RESOURCES" /></em></p>
			</logic:empty>
			
			<logic:notEmpty name="precedentDegreeInformationList">

				<logic:iterate id="precedentDegreeInformation" name="precedentDegreeInformationList" type="PrecedentDegreeInformation">
				
					<bean:define id="precedentDegreeInformationId" name="precedentDegreeInformation" property="externalId" type="String" />
									
					<jsp:include page="/gep/student/candidacy/personal/ingression/data/viewPrecedentDegreeInformation.jsp">
						<jsp:param name="precedentDegreeInformationId" value="<%= precedentDegreeInformationId %>" />
					</jsp:include>
					
					
				</logic:iterate>
				
			</logic:notEmpty>
		</div>
		
	</logic:iterate>
</logic:notEmpty>


<hr />


<p><strong><bean:message key="label.personal.ingression.data.viewer.raides.data.personal.ingression" bundle="GEP_RESOURCES" /></strong></p>

<bean:define id="personalIngressionDataList" name="student" property="personalIngressionsData" />

<logic:empty name="personalIngressionDataList">
	<p><em><bean:message key="message.personal.ingression.data.viewer.student.lack.personal.ingression.data" bundle="GEP_RESOURCES" /></em></p>
</logic:empty>

<logic:notEmpty name="personalIngressionDataList">

	<logic:iterate id="pid" name="personalIngressionDataList">
	
		<fr:view name="pid">
			<fr:schema type="net.sourceforge.fenixedu.domain.student.PersonalIngressionData" bundle="GEP_RESOURCES">
	
				<logic:notEmpty name="pid" property="dislocatedFromPermanentResidence">
					<fr:slot name="dislocatedFromPermanentResidence" layout="null-as-label" key="label.personal.ingression.data.viewer.dislocatedFromPermanentResidence" />
				</logic:notEmpty>


				<logic:notEmpty name="pid" property="highSchoolType">
					<fr:slot name="highSchoolType" layout="null-as-label" key="label.personal.ingression.data.viewer.highSchoolType" />
				</logic:notEmpty>


				<logic:notEmpty name="pid" property="grantOwnerType">
					<fr:slot name="grantOwnerType" layout="null-as-label" key="label.personal.ingression.data.viewer.grantOwnerType" />
				</logic:notEmpty>


				<logic:notEmpty name="pid" property="grantOwnerProvider">
					<fr:slot name="grantOwnerProvider.name" layout="null-as-label" key="label.personal.ingression.data.viewer.grantOwnerProvider" />
				</logic:notEmpty>


				<logic:notEmpty name="pid" property="maritalStatus">
					<fr:slot name="maritalStatus" layout="null-as-label" key="label.personal.ingression.data.viewer.maritalStatus" />
				</logic:notEmpty>


				<logic:notEmpty name="pid" property="professionType">
					<fr:slot name="professionType.localizedName" layout="null-as-label" key="label.personal.ingression.data.viewer.professionType" />
				</logic:notEmpty>


				<logic:notEmpty name="pid" property="professionalCondition">
					<fr:slot name="professionalCondition.localizedName" layout="null-as-label" key="label.personal.ingression.data.viewer.professionalCondition" />
				</logic:notEmpty>


				<logic:notEmpty name="pid" property="motherSchoolLevel">
					<fr:slot name="motherSchoolLevel.localizedName" layout="null-as-label" key="label.personal.ingression.data.viewer.motherSchoolLevel" />
				</logic:notEmpty>


				<logic:notEmpty name="pid" property="motherProfessionType">
					<fr:slot name="motherProfessionType.localizedName" layout="null-as-label" key="label.personal.ingression.data.viewer.motherProfessionType" />
				</logic:notEmpty>


				<logic:notEmpty name="pid" property="motherProfessionalCondition">
					<fr:slot name="motherProfessionalCondition.localizedName" layout="null-as-label" key="label.personal.ingression.data.viewer.motherProfessionalCondition" />
				</logic:notEmpty>


				<logic:notEmpty name="pid" property="fatherSchoolLevel">
					<fr:slot name="fatherSchoolLevel.localizedName" layout="null-as-label" key="label.personal.ingression.data.viewer.fatherSchoolLevel"/>
				</logic:notEmpty>


				<logic:notEmpty name="pid" property="fatherProfessionType">
					<fr:slot name="fatherProfessionType.localizedName" layout="null-as-label" key="label.personal.ingression.data.viewer.fatherProfessionType" />
				</logic:notEmpty>


				<logic:notEmpty name="pid" property="fatherProfessionalCondition">
					<fr:slot name="fatherProfessionalCondition.localizedName" layout="null-as-label" key="label.personal.ingression.data.viewer.fatherProfessionalCondition" />
				</logic:notEmpty>


				<logic:notEmpty name="pid" property="spouseSchoolLevel">
					<fr:slot name="spouseSchoolLevel.localizedName" layout="null-as-label" key="label.personal.ingression.data.viewer.spouseSchoolLevel" />
				</logic:notEmpty>

				<logic:notEmpty name="pid" property="spouseProfessionType">
				<fr:slot name="spouseProfessionType.localizedName" layout="null-as-label" key="label.personal.ingression.data.viewer.spouseProfessionType" />
				</logic:notEmpty>


				<logic:notEmpty name="pid" property="spouseProfessionalCondition">
					<fr:slot name="spouseProfessionalCondition.localizedName" layout="null-as-label" key="label.personal.ingression.data.viewer.spouseProfessionalCondition" />
				</logic:notEmpty>

				<logic:notEmpty name="pid" property="countryOfResidence">
					<fr:slot name="countryOfResidence.name" layout="null-as-label" key="label.personal.ingression.data.viewer.countryOfResidence" />
				</logic:notEmpty>

				<logic:notEmpty name="pid" property="districtSubdivisionOfResidence">
					<fr:slot name="districtSubdivisionOfResidence.name" layout="null-as-label" key="label.personal.ingression.data.viewer.districtSubdivisionOfResidence" />
				</logic:notEmpty>


				<logic:notEmpty name="pid" property="schoolTimeDistrictSubDivisionOfResidence">
					<fr:slot name="schoolTimeDistrictSubDivisionOfResidence.name" layout="null-as-label" key="label.personal.ingression.data.viewer.schoolTimeDistrictSubDivisionOfResidence" />
				</logic:notEmpty>


				<logic:notEmpty name="pid" property="executionYear">
					<fr:slot name="executionYear.name" layout="null-as-label" key="label.personal.ingression.data.viewer.personal.ingression.data.executionYear" />
				</logic:notEmpty>


				<logic:notEmpty name="pid" property="lastModifiedDate">
					<fr:slot name="lastModifiedDate" layout="null-as-label" key="label.personal.ingression.data.viewer.lastModifiedDate" />
				</logic:notEmpty>
				
			</fr:schema>
	
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1" />
				
				<fr:link name="view" 
					link="/personalIngressionDataViewer.do?method=viewPersonalIngressionData&amp;personalIngressionDataId=${externalId}" 
					label="label.view,APPLICATION_RESOURCES" />
					
			</fr:layout>
		</fr:view>

		<div style="margin-left: 100px">
			<p><strong><bean:message key="label.personal.ingression.data.viewer.precedent.degree.info" bundle="GEP_RESOURCES" /></strong></p>
			
			<bean:define id="precedentDegreeInformationList" name="pid" property="precedentDegreesInformations" />
			
			
			<logic:empty name="precedentDegreeInformationList">
				<p><em><bean:message key="message.personal.ingression.data.viewer.student.lack.precedent.degree.information" bundle="GEP_RESOURCES" /></em></p>
			</logic:empty>
			
			<logic:notEmpty name="precedentDegreeInformationList">

				<logic:iterate id="precedentDegreeInformation" name="precedentDegreeInformationList" type="PrecedentDegreeInformation">
				
					<bean:define id="precedentDegreeInformationId" name="precedentDegreeInformation" property="externalId" type="String" />
									
					<jsp:include page="/gep/student/candidacy/personal/ingression/data/viewPrecedentDegreeInformation.jsp">
						<jsp:param name="precedentDegreeInformationId" value="<%= precedentDegreeInformationId %>" />
					</jsp:include>
					
					
				</logic:iterate>
				
			</logic:notEmpty>
		</div>
	
	</logic:iterate>
	
</logic:notEmpty>


<hr />
<p><strong><bean:message key="label.personal.ingression.data.viewer.student.candidacies" bundle="GEP_RESOURCES" /></strong></p>

<bean:define id="studentCandidacies" name="student" property="person.candidacies" />

<logic:empty name="studentCandidacies">
	<bean:message key="message.personal.ingression.data.viewer.student.lack.student.candidacies" bundle="GEP_RESOURCES" />
</logic:empty>

<logic:notEmpty name="studentCandidacies">
	<logic:iterate id="candidacy" name="studentCandidacies">
	
		<fr:view name="candidacy">
			<fr:schema type="net.sourceforge.fenixedu.domain.candidacy.StudentCandidacy" bundle="GEP_RESOURCES">
				
				
				<logic:notEmpty name="candidacy" property="contigent">
					<fr:slot name="contigent" layout="null-as-label" key="label.personal.ingression.data.viewer.contigent" />
				</logic:notEmpty>
				
				<logic:notEmpty name="candidacy" property="entryGrade">
					<fr:slot name="entryGrade" layout="null-as-label" key="label.personal.ingression.data.viewer.entryGrade" />
				</logic:notEmpty>
				
				<logic:notEmpty name="candidacy" property="entryPhase">
					<fr:slot name="entryPhase.localizedName" layout="null-as-label" key="label.personal.ingression.data.viewer.entryPhase" />
				</logic:notEmpty>
				
				<logic:notEmpty name="candidacy" property="ingression">
					<fr:slot name="ingression.localizedName" layout="null-as-label" key="label.personal.ingression.data.viewer.ingression" />
				</logic:notEmpty>
				
				<logic:notEmpty name="candidacy" property="applyForResidence">
					<fr:slot name="applyForResidence" layout="null-as-label" key="label.personal.ingression.data.viewer.applyForResidence" />
				</logic:notEmpty>
				
				<logic:notEmpty name="candidacy" property="notesAboutResidenceAppliance">
					<fr:slot name="notesAboutResidenceAppliance" layout="null-as-label" key="label.personal.ingression.data.viewer.notesAboutResidenceAppliance" />
				</logic:notEmpty>
				
				<logic:notEmpty name="candidacy" property="studentPersonalDataAuthorizationChoice">
					<fr:slot name="studentPersonalDataAuthorizationChoice.description" layout="null-as-label" key="label.personal.ingression.data.viewer.studentPersonalDataAuthorizationChoice" />
				</logic:notEmpty>
				
				<logic:notEmpty name="candidacy" property="dislocatedFromPermanentResidence">
					<fr:slot name="dislocatedFromPermanentResidence" layout="null-as-label" key="label.personal.ingression.data.viewer.dislocatedFromPermanentResidence" />
				</logic:notEmpty>
				
				<logic:notEmpty name="candidacy" property="placingOption">
					<fr:slot name="placingOption" layout="null-as-label" key="label.personal.ingression.data.viewer.placingOption" />
				</logic:notEmpty>
				
				<logic:notEmpty name="candidacy" property="grantOwnerType">
					<fr:slot name="grantOwnerType" layout="null-as-label" key="label.personal.ingression.data.viewer.grantOwnerType" />
				</logic:notEmpty>
				
				<logic:notEmpty name="candidacy" property="numberOfCandidaciesToHigherSchool">
					<fr:slot name="numberOfCandidaciesToHigherSchool" layout="null-as-label" key="label.personal.ingression.data.viewer.numberOfCandidaciesToHigherSchool" />
				</logic:notEmpty>
				
				<logic:notEmpty name="candidacy" property="numberOfFlunksOnHighSchool">
					<fr:slot name="numberOfFlunksOnHighSchool" layout="null-as-label" key="label.personal.ingression.data.viewer.numberOfFlunksOnHighSchool" />
				</logic:notEmpty>
				
				<logic:notEmpty name="candidacy" property="highSchoolType">
					<fr:slot name="highSchoolType" layout="null-as-label" key="label.personal.ingression.data.viewer.highSchoolType" />
				</logic:notEmpty>
				
				<logic:notEmpty name="candidacy" property="maritalStatus">
					<fr:slot name="maritalStatus" layout="null-as-label" key="label.personal.ingression.data.viewer.maritalStatus" />
				</logic:notEmpty>
				
				<logic:notEmpty name="candidacy" property="professionType">
					<fr:slot name="professionType.localizedName" layout="null-as-label" key="label.personal.ingression.data.viewer.professionType" />
				</logic:notEmpty>
				
				<logic:notEmpty name="candidacy" property="professionalCondition">
					<fr:slot name="professionalCondition.localizedName" layout="null-as-label" key="label.personal.ingression.data.viewer.professionalCondition" />
				</logic:notEmpty>
				
				<logic:notEmpty name="candidacy" property="motherSchoolLevel">
					<fr:slot name="motherSchoolLevel.localizedName" layout="null-as-label" key="label.personal.ingression.data.viewer.motherSchoolLevel" />
				</logic:notEmpty>
				
				<logic:notEmpty name="candidacy" property="motherProfessionType">
					<fr:slot name="motherProfessionType.localizedName" layout="null-as-label" key="label.personal.ingression.data.viewer.motherProfessionType" />
				</logic:notEmpty>
				
				<logic:notEmpty name="candidacy" property="motherProfessionalCondition">
					<fr:slot name="motherProfessionalCondition.localizedName" layout="null-as-label" key="label.personal.ingression.data.viewer.motherProfessionalCondition" />
				</logic:notEmpty>
				
				<logic:notEmpty name="candidacy" property="fatherSchoolLevel">
					<fr:slot name="fatherSchoolLevel.localizedName" layout="null-as-label" key="label.personal.ingression.data.viewer.fatherSchoolLevel" />
				</logic:notEmpty>
				
				<logic:notEmpty name="candidacy" property="fatherProfessionType">
					<fr:slot name="fatherProfessionType.localizedName" layout="null-as-label" key="label.personal.ingression.data.viewer.fatherProfessionType" />
				</logic:notEmpty>
				
				<logic:notEmpty name="candidacy" property="fatherProfessionalCondition">
					<fr:slot name="fatherProfessionalCondition.localizedName" layout="null-as-label" key="label.personal.ingression.data.viewer.fatherProfessionalCondition" />
				</logic:notEmpty>
				
				<logic:notEmpty name="candidacy" property="spouseSchoolLevel">
					<fr:slot name="spouseSchoolLevel.localizedName" layout="null-as-label" key="label.personal.ingression.data.viewer.spouseSchoolLevel" />
				</logic:notEmpty>
				
				<logic:notEmpty name="candidacy" property="spouseProfessionType">
					<fr:slot name="spouseProfessionType.localizedName" layout="null-as-label" key="label.personal.ingression.data.viewer.spouseProfessionType" />
				</logic:notEmpty>
				
				<logic:notEmpty name="candidacy" property="spouseProfessionalCondition">
					<fr:slot name="spouseProfessionalCondition.localizedName" layout="null-as-label" key="label.personal.ingression.data.viewer.spouseProfessionalCondition" />
				</logic:notEmpty>
				
				<logic:notEmpty name="candidacy" property="firstTimeCandidacy">
					<fr:slot name="firstTimeCandidacy" layout="null-as-label" key="label.personal.ingression.data.viewer.firstTimeCandidacy" />
				</logic:notEmpty>
				
				<logic:notEmpty name="candidacy" property="executionDegree">
					<fr:slot name="executionDegree.degreeName" layout="null-as-label" key="label.personal.ingression.data.viewer.studentCandidacy.executionDegree" />
				</logic:notEmpty>
				
				<logic:notEmpty name="candidacy" property="executionDegree">
					<fr:slot name="executionDegree.executionYear.name" layout="null-as-label" key="label.personal.ingression.data.viewer.studentCandidacy.executionDegree.executionYear" />
				</logic:notEmpty>
				
				<logic:notEmpty name="candidacy" property="countryOfResidence">
					<fr:slot name="countryOfResidence.name" layout="null-as-label" key="label.personal.ingression.data.viewer.countryOfResidence" />
				</logic:notEmpty>
				
				<logic:notEmpty name="candidacy" property="grantOwnerProvider">
					<fr:slot name="grantOwnerProvider.name" layout="null-as-label" key="label.personal.ingression.data.viewer.grantOwnerProvider" />
				</logic:notEmpty>
				
				<logic:notEmpty name="candidacy" property="districtSubdivisionOfResidence">
					<fr:slot name="districtSubdivisionOfResidence.name" layout="null-as-label" key="label.personal.ingression.data.viewer.districtSubdivisionOfResidence" />
				</logic:notEmpty>
				
				<logic:notEmpty name="candidacy" property="schoolTimeDistrictSubDivisionOfResidence">
					<fr:slot name="schoolTimeDistrictSubDivisionOfResidence.name" layout="null-as-label" key="label.personal.ingression.data.viewer.schoolTimeDistrictSubDivisionOfResidence" />
				</logic:notEmpty>
				
				<logic:notEmpty name="candidacy" property="registration">
					<fr:slot name="registration.degree.presentationName" layout="null-as-label" key="label.personal.ingression.data.viewer.registration.degree" />
					<fr:slot name="registration.startExecutionYear.name" layout="null-as-label" key="label.personal.ingression.data.viewer.registration.startExecutionYear" />
				</logic:notEmpty>
				
				<% if(candidacy instanceof PHDProgramCandidacy) { %>
				
				<logic:notEmpty name="candidacy" property="candidacyProcess">
					<fr:slot name="candidacyProcess.individualProgramProcess.phdProgram.name" key="label.personal.ingression.data.viewer.phd.program.name" />
					<fr:slot name="candidacyProcess.individualProgramProcess.executionYear.name" key="label.personal.ingression.data.viewer.phd.start.execution.year" />
				</logic:notEmpty>
				
				<% } %>
				
			</fr:schema>
			
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1" />
			</fr:layout>
			
		</fr:view>
		
		<div style="margin-left: 100px">
			<p><strong><bean:message key="label.personal.ingression.data.viewer.precedent.degree.info" bundle="GEP_RESOURCES" /></strong></p>
			
			<bean:define id="precedentDegreeInformation" name="candidacy" property="precedentDegreeInformation" />
			
			
			<logic:empty name="precedentDegreeInformation">
				<p><em><bean:message key="message.personal.ingression.data.viewer.student.lack.precedent.degree.information" bundle="GEP_RESOURCES" /></em></p>
			</logic:empty>
			
			<logic:notEmpty name="precedentDegreeInformation">
			
				<bean:define id="precedentDegreeInformationId" name="precedentDegreeInformation" property="externalId" type="String" />
								
				<jsp:include page="/gep/student/candidacy/personal/ingression/data/viewPrecedentDegreeInformation.jsp">
					<jsp:param name="precedentDegreeInformationId" value="<%= precedentDegreeInformationId %>" />
				</jsp:include>
			
			</logic:notEmpty>
		</div>
		
	</logic:iterate> 
</logic:notEmpty>

<hr />

<p><strong><bean:message key="label.personal.ingression.data.viewer.individual.candidacies" bundle="GEP_RESOURCES" /></strong></p>

<bean:define id="individualCandidaciesPersonalDetailsList" name="student" property="person.individualCandidacies" />

<logic:empty name="individualCandidaciesPersonalDetailsList">
	<p><bean:message key="message.personal.ingression.data.viewer.student.lack.individual.candidacies" bundle="GEP_RESOURCES" /></p>
</logic:empty>

<logic:notEmpty name="individualCandidaciesPersonalDetailsList">
	<logic:iterate id="individualCandidaciesPersonalDetails" name="individualCandidaciesPersonalDetailsList">
	
		<bean:define id="individualCandidacy" name="individualCandidaciesPersonalDetails" property="candidacy" />
	
		<fr:view name="individualCandidacy">
			<fr:schema type="net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacy" bundle="GEP_RESOURCES">
				
				<fr:slot name="externalId"/>
				<fr:slot name="class.simpleName"/>
				<logic:notEmpty name="individualCandidacy" property="dislocatedFromPermanentResidence">
					<fr:slot name="dislocatedFromPermanentResidence" layout="null-as-label" key="label.personal.ingression.data.viewer.dislocatedFromPermanentResidence" />
				</logic:notEmpty>
				
				<logic:notEmpty name="individualCandidacy" property="grantOwnerType">
					<fr:slot name="grantOwnerType" layout="null-as-label" key="label.personal.ingression.data.viewer.grantOwnerType" />
				</logic:notEmpty>
				
				<logic:notEmpty name="individualCandidacy" property="numberOfCandidaciesToHigherSchool">
					<fr:slot name="numberOfCandidaciesToHigherSchool" layout="null-as-label" key="label.personal.ingression.data.viewer.numberOfCandidaciesToHigherSchool" />
				</logic:notEmpty>
				
				<logic:notEmpty name="individualCandidacy" property="numberOfFlunksOnHighSchool">
					<fr:slot name="numberOfFlunksOnHighSchool" layout="null-as-label" key="label.personal.ingression.data.viewer.numberOfFlunksOnHighSchool" />
				</logic:notEmpty>
				
				<logic:notEmpty name="individualCandidacy" property="highSchoolType">
					<fr:slot name="highSchoolType" layout="null-as-label" key="label.personal.ingression.data.viewer.highSchoolType" />
				</logic:notEmpty>
				
				<logic:notEmpty name="individualCandidacy" property="maritalStatus">
					<fr:slot name="maritalStatus" layout="null-as-label" key="label.personal.ingression.data.viewer.maritalStatus" />
				</logic:notEmpty>
				
				<logic:notEmpty name="individualCandidacy" property="professionType">
					<fr:slot name="professionType.localizedName" layout="null-as-label" key="label.personal.ingression.data.viewer.professionType" />
				</logic:notEmpty>
				
				<logic:notEmpty name="individualCandidacy" property="professionalCondition">
					<fr:slot name="professionalCondition.localizedName" layout="null-as-label" key="label.personal.ingression.data.viewer.professionalCondition" />
				</logic:notEmpty>
				
				<logic:notEmpty name="individualCandidacy" property="motherSchoolLevel">
					<fr:slot name="motherSchoolLevel.localizedName" layout="null-as-label" key="label.personal.ingression.data.viewer.motherSchoolLevel" />
				</logic:notEmpty>
				
				<logic:notEmpty name="individualCandidacy" property="motherProfessionType">
					<fr:slot name="motherProfessionType.localizedName" layout="null-as-label" key="label.personal.ingression.data.viewer.motherProfessionType" />
				</logic:notEmpty>
				
				<logic:notEmpty name="individualCandidacy" property="motherProfessionalCondition">
					<fr:slot name="motherProfessionalCondition.localizedName" layout="null-as-label" key="label.personal.ingression.data.viewer.motherProfessionalCondition" />
				</logic:notEmpty>
				
				<logic:notEmpty name="individualCandidacy" property="fatherSchoolLevel">
					<fr:slot name="fatherSchoolLevel.localizedName" layout="null-as-label" key="label.personal.ingression.data.viewer.fatherSchoolLevel" />
				</logic:notEmpty>
				
				<logic:notEmpty name="individualCandidacy" property="fatherProfessionType">
					<fr:slot name="fatherProfessionType.localizedName" layout="null-as-label" key="label.personal.ingression.data.viewer.fatherProfessionType" />
				</logic:notEmpty>
				
				<logic:notEmpty name="individualCandidacy" property="fatherProfessionalCondition">
					<fr:slot name="fatherProfessionalCondition.localizedName" layout="null-as-label" key="label.personal.ingression.data.viewer.fatherProfessionalCondition" />
				</logic:notEmpty>
				
				<logic:notEmpty name="individualCandidacy" property="spouseSchoolLevel">
					<fr:slot name="spouseSchoolLevel.localizedName" layout="null-as-label" key="label.personal.ingression.data.viewer.spouseSchoolLevel" />
				</logic:notEmpty>
				
				<logic:notEmpty name="individualCandidacy" property="spouseProfessionType">
					<fr:slot name="spouseProfessionType.localizedName" layout="null-as-label" key="label.personal.ingression.data.viewer.spouseProfessionType" />
				</logic:notEmpty>
				
				<logic:notEmpty name="individualCandidacy" property="spouseProfessionalCondition">
					<fr:slot name="spouseProfessionalCondition.localizedName" layout="null-as-label" key="label.personal.ingression.data.viewer.spouseProfessionalCondition" />
				</logic:notEmpty>
				
				<logic:notEmpty name="individualCandidacy" property="countryOfResidence">
					<fr:slot name="countryOfResidence.name" layout="null-as-label" key="label.personal.ingression.data.viewer.countryOfResidence" />
				</logic:notEmpty>
				
				<logic:notEmpty name="individualCandidacy" property="grantOwnerProvider">
					<fr:slot name="grantOwnerProvider.name" layout="null-as-label" key="label.personal.ingression.data.viewer.grantOwnerProvider" />
				</logic:notEmpty>
				
				<logic:notEmpty name="individualCandidacy" property="districtSubdivisionOfResidence">
					<fr:slot name="districtSubdivisionOfResidence.name" layout="null-as-label" key="label.personal.ingression.data.viewer.districtSubdivisionOfResidence" />
				</logic:notEmpty>
				
				<logic:notEmpty name="individualCandidacy" property="schoolTimeDistrictSubDivisionOfResidence">
					<fr:slot name="schoolTimeDistrictSubDivisionOfResidence.name" layout="null-as-label" key="label.personal.ingression.data.viewer.schoolTimeDistrictSubDivisionOfResidence" />
				</logic:notEmpty>
				
				<logic:notEmpty name="individualCandidacy" property="registration">
					<fr:slot name="registration.degree.presentationName" layout="null-as-label" key="label.personal.ingression.data.viewer.registration.degree"/>
					<fr:slot name="registration.startExecutionYear.name" layout="null-as-label" key="label.personal.ingression.data.viewer.registration.startExecutionYear" />
				</logic:notEmpty>
				
			</fr:schema>
			
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1" />
			</fr:layout>
			
		</fr:view>
		
		<div style="margin-left: 100px">
			<p><strong><bean:message key="label.personal.ingression.data.viewer.candidacy.precedent.degree.info" bundle="GEP_RESOURCES" /></strong></p>
			
			
			<logic:empty name="individualCandidacy" property="precedentDegreeInformation">
				<p><em><bean:message key="message.personal.ingression.data.viewer.student.lack.precedent.degree.information" bundle="GEP_RESOURCES" /></em></p>
			</logic:empty>
			
			<logic:notEmpty name="individualCandidacy" property="precedentDegreeInformation">
				
				<bean:define id="precedentDegreeInformation" name="individualCandidacy" property="precedentDegreeInformation" />
				
				<fr:view name="precedentDegreeInformation">
					<fr:schema type="net.sourceforge.fenixedu.domain.student.PrecedentDegreeInformation" bundle="GEP_RESOURCES" >
					
						<logic:notEmpty name="precedentDegreeInformation" property="schoolLevel">
							<fr:slot name="schoolLevel.localizedName" layout="null-as-label" key="label.personal.ingression.data.viewer.schoolLevel" />
						</logic:notEmpty>


						<logic:notEmpty name="precedentDegreeInformation" property="otherSchoolLevel">
							<fr:slot name="otherSchoolLevel" layout="null-as-label" key="label.personal.ingression.data.viewer.otherSchoolLevel" />
						</logic:notEmpty>

						<logic:notEmpty name="precedentDegreeInformation" property="country">
							<fr:slot name="country.name" layout="null-as-label" key="label.personal.ingression.data.viewer.country" />
						</logic:notEmpty>
					
						<% if(precedentDegreeInformation instanceof InstitutionPrecedentDegreeInformation) { %>

						<logic:notEmpty name="precedentDegreeInformation" property="cycleType">
							<fr:slot name="cycleType" layout="null-as-label" key="label.personal.ingression.data.viewer.cycleType" />
						</logic:notEmpty>
						
						<% } %>

						<% if(precedentDegreeInformation instanceof ExternalPrecedentDegreeInformation ) { %>

						<logic:notEmpty name="precedentDegreeInformation" property="degreeDesignation">
							<fr:slot name="degreeDesignation" layout="null-as-label" key="label.personal.ingression.data.viewer.degreeDesignation" />
						</logic:notEmpty>

						<logic:notEmpty name="precedentDegreeInformation" property="institution">
							<fr:slot name="institution.name" layout="null-as-label" key="label.personal.ingression.data.viewer.institution" />
						</logic:notEmpty>


						<logic:notEmpty name="precedentDegreeInformation" property="sourceInstitution">
							<fr:slot name="sourceInstitution.name" layout="null-as-label" key="label.personal.ingression.data.viewer.sourceInstitution" />
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


						<logic:notEmpty name="precedentDegreeInformation" property="gradeSum">
							<fr:slot name="gradeSum" layout="null-as-label" key="label.personal.ingression.data.viewer.gradeSum" />
						</logic:notEmpty>

						<% } %>


						<logic:notEmpty name="precedentDegreeInformation" property="candidacy.registration">
							<fr:slot name="candidacy.registration.degree.presentationName" layout="null-as-label" key="label.personal.ingression.data.viewer.registration.degree" />
							<fr:slot name="candidacy.registration.startExecutionYear.name" layout="null-as-label" key="label.personal.ingression.data.viewer.registration.startExecutionYear" />
						</logic:notEmpty>


					</fr:schema>
					
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle1" />
					</fr:layout>
					
				</fr:view>
			</logic:notEmpty>
		</div>
		
		<div style="margin-left: 100px">
			<p><strong><bean:message key="label.personal.ingression.data.viewer.precedent.degree.info" bundle="GEP_RESOURCES" /></strong></p>
			
			
			<logic:empty name="individualCandidacy" property="refactoredPrecedentDegreeInformation" >
				<p><em><bean:message key="message.personal.ingression.data.viewer.student.lack.precedent.degree.information" bundle="GEP_RESOURCES" /></em></p>
			</logic:empty>
			
			<logic:notEmpty name="individualCandidacy" property="refactoredPrecedentDegreeInformation" >
			
				<bean:define id="precedentDegreeInformation" name="individualCandidacy" property="refactoredPrecedentDegreeInformation" />

				<bean:define id="precedentDegreeInformationId" name="precedentDegreeInformation" property="externalId" type="String" />
								
				<jsp:include page="/gep/student/candidacy/personal/ingression/data/viewPrecedentDegreeInformation.jsp">
					<jsp:param name="precedentDegreeInformationId" value="<%= precedentDegreeInformationId %>" />
				</jsp:include>
			
			</logic:notEmpty>

		</div>
		
	
	</logic:iterate> 
</logic:notEmpty>


</logic:present>