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
<html:xhtml/>

<bean:define id="processName" name="processName" />
<bean:define id="parentProcessId" name="parentProcess" property="externalId" />
<bean:define id="individualCandidacyProcess" name="process"/>

<em><bean:message key="label.candidacies" bundle="APPLICATION_RESOURCES"/></em>

<logic:notEmpty name="process">
	<bean:define id="mobilityProgram" name="process" property="candidacy.mobilityStudentData.selectedOpening.mobilityAgreement.mobilityProgram.registrationAgreement.description"/>
	<h2><bean:write name="process" property="displayName" />: <strong><bean:write name="mobilityProgram"/></strong></h2>
</logic:notEmpty>

<p>
	<html:link action='<%= "/caseHandling" + processName.toString() + ".do?method=listProcesses&amp;parentProcessId=" + parentProcessId.toString() %>'>
		« <bean:message key="label.back" bundle="APPLICATION_RESOURCES"/>	
	</html:link>
</p>


<logic:notEmpty name="process">
	<bean:define id="processId" name="process" property="externalId" />
	<bean:define id="candidacy" name="process" property="candidacy" />
	<logic:notEmpty name="activities">
		<%-- list process activities --%>
		<ul class="mtop15 mbottom2">
		<logic:iterate id="activity" name="activities">
			<bean:define id="activityName" name="activity" property="class.simpleName" />
			<li>
				<html:link action='<%= "/caseHandling" + processName.toString() + ".do?method=prepareExecute" + activityName.toString() + "&amp;processId=" + processId.toString()%>'>
					<bean:message name="activity" property="class.name" bundle="CASE_HANDLING_RESOURCES" />
				</html:link>
			</li>
		</logic:iterate>
	</ul>
	</logic:notEmpty>
	<logic:present role="role(INTERNATIONAL_RELATION_OFFICE)">
		<logic:equal name="process" property="candidacy.mostRecentApprovedLearningAgreementNotViewed" value="true">
			<div class="infoop-blue">
				<bean:message key="message.erasmus.most.approved.learning.agreement.is.not.viewed" bundle="ACADEMIC_OFFICE_RESOURCES" />
			</div>
		</logic:equal>
	</logic:present>
	
	<logic:present role="role(INTERNATIONAL_RELATION_OFFICE)">
		<logic:equal name="process" property="processWithMostRecentAlertMessageNotViewed" value="true">
			<div class="infoop-blue">
				<bean:message key="message.erasmus.most.recent.alert.not.viewed" bundle="ACADEMIC_OFFICE_RESOURCES" />
			</div>
		</logic:equal>
	</logic:present>
	
	<%-- student information --%>
	<logic:notEmpty name="process" property="personalDetails.student">
		<p class="mbottom05"><strong><bean:message key="label.studentDetails" bundle="APPLICATION_RESOURCES"/></strong></p>
		<fr:view name="process" property="personalDetails.student" schema="student.show.number.information">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 thlight thright mtop025"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
	
	<%-- show candidacy information --%>
	<p class="mbottom05"><strong><bean:message key="label.candidacy.data" bundle="APPLICATION_RESOURCES"/></strong></p>
	<fr:view name="process" schema='<%= processName.toString() +  ".view" %>'>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thlight thright mtop025"/>
	        <fr:property name="columnClasses" value="width14em,,tdclear tderror1"/>
		</fr:layout>
	</fr:view>
	
	<%-- show person information --%>
	<p class="mbottom05"><strong><bean:message key="label.candidacy.personalData" bundle="APPLICATION_RESOURCES" /></strong></p>
	<fr:view name="process" property="personalDetails" schema="ErasmusCandidacyProcess.personalData">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thlight thright mtop025"/>
	        <fr:property name="columnClasses" value="width14em,,tdclear tderror1"/>
		</fr:layout>
	</fr:view>
	
	
	<logic:present role="role(MANAGER)">
	<%-- show public candidacy access information --%>
	<h3 style="margin-top: 1em;"><bean:message key="title.public.candidacy.information.access" bundle="CANDIDATE_RESOURCES" />:</h3>
	
	<fr:view name="process" property="candidacyHashCode" schema="PublicCandidacyHashCode.view" >
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
	        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
		</fr:layout>	
	</fr:view>
	</logic:present>

	<%-- show home institution data --%>
	<p class="mbottom05"><strong><bean:message key="label.erasmus.home.institution" bundle="ACADEMIC_OFFICE_RESOURCES" /></strong></p>
	<fr:view name="process" property="candidacy.mobilityStudentData" schema="ErasmusIndividualCandidacyProcess.home.institution.view">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thlight thright mtop025"/>
	        <fr:property name="columnClasses" value="width14em,,tdclear tderror1"/>
		</fr:layout>
	</fr:view>


	<%-- show current study --%>
	<p class="mbottom05">
		<strong><bean:message key="label.erasmus.current.study" bundle="ACADEMIC_OFFICE_RESOURCES" /></strong>
		(<bean:message key="label.erasmus.current.study.detailed" bundle="ACADEMIC_OFFICE_RESOURCES" />)	
	</p>
	
	<fr:view name="process" schema="MobilityIndividualApplicationProcess.current.study.view" property="candidacy.mobilityStudentData">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thlight thright mtop025"/>
	        <fr:property name="columnClasses" value="width30em,,tdclear tderror1"/>
		</fr:layout>
	</fr:view>
	
	<%-- show period of study --%>
	<p class="mbottom05"><strong><bean:message key="label.erasmus.period.of.study" bundle="ACADEMIC_OFFICE_RESOURCES" /></strong></p>
	<fr:view name="process" schema="MobilityIndividualAppicationProcess.period.of.study.view" property="candidacy.mobilityStudentData">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thlight thright mtop025"/>
	        <fr:property name="columnClasses" value="width30em,,tdclear tderror1"/>
		</fr:layout>
	</fr:view>
	
	
	<%-- Choosen courses --%>
	<p class="mbottom05"><strong><bean:message key="label.erasmus.courses" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
	
	<table class="tstyle2 thlight thcenter mtop05">
		<tr>
			<th><bean:message key="label.erasmus.course" bundle="ACADEMIC_OFFICE_RESOURCES"/></th>
			<th><bean:message key="label.erasmus.degree" bundle="ACADEMIC_OFFICE_RESOURCES"/></th>
		</tr>
		<logic:iterate id="course" name="process" property="sortedSelectedCurricularCourses" indexId="index">
				<bean:define id="curricularCourseId" name="course" property="externalId" />
			<tr>
				<td>
					<fr:view 	name="course"
								property="nameI18N">
					</fr:view>			
				</td>
				<td>
					<fr:view	name="course"
								property="degree.nameI18N" /> - 
					<fr:view	name="course"
								property="degree.sigla" />
				</td>
			</tr>
		</logic:iterate>
	</table>	
	
	<logic:present role="role(INTERNATIONAL_RELATION_OFFICE)">
		<p class="mbottom05"><html:link page="<%= "/caseHandling" + processName.toString() + ".do?method=retrieveLearningAgreement&processId=" + processId %>">Download learning agreement</html:link></p>
	</logic:present>
	
	<%-- show documents--%>

	<p><strong><bean:message key="label.documentation" bundle="CANDIDATE_RESOURCES"/></strong></p> 
	
	<logic:empty name="individualCandidacyProcess" property="candidacy.documents">
		<p><em><bean:message key="message.documents.empty" bundle="CANDIDATE_RESOURCES"/>.</em></p>
	</logic:empty>
	
	<logic:notEmpty name="individualCandidacyProcess" property="candidacy.documents">
		<table class="tstyle1 thlight thcenter">
			<tr>
				<th><bean:message key="label.candidacy.document.kind" bundle="CANDIDATE_RESOURCES"/></th>
				<th><bean:message key="label.dateTime.submission" bundle="CANDIDATE_RESOURCES"/></th>
				<th><bean:message key="label.document.file.name" bundle="CANDIDATE_RESOURCES"/></th>
				<th><bean:message key="label.document.file.active" bundle="CANDIDATE_RESOURCES"/></th>
				<th></th>
			</tr>
			<logic:iterate id="documentFile" name="individualCandidacyProcess" property="candidacy.documents">
				<tr>
					<td><fr:view name="documentFile" property="candidacyFileType"/></td>
					<td><fr:view name="documentFile" property="uploadTime"/></td>
					<td><fr:view name="documentFile" property="filename"/></td>
					<td><fr:view name="documentFile" property="candidacyFileActive"/></td>
					<td><fr:view name="documentFile" layout="link"/></td>
				</tr>	
			</logic:iterate>
		</table>
	</logic:notEmpty>

	<%-- show approved learning agreements--%>

	<p><strong><bean:message key="label.erasmus.approved.learning.agreements" bundle="CANDIDATE_RESOURCES"/></strong></p> 
	
	<logic:empty name="individualCandidacyProcess" property="candidacy.approvedLearningAgreements" >
		<p class="mbottom05"><em><bean:message key="label.erasmus.approved.learning.agreements.empty" bundle="ACADEMIC_OFFICE_RESOURCES" /></em></p>
	</logic:empty>
	
	<logic:notEmpty name="individualCandidacyProcess" property="candidacy.approvedLearningAgreements" >
		<fr:view name="individualCandidacyProcess" property="candidacy.approvedLearningAgreements" schema="IndividualCandidacyDocumentFile.view">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 thlight thright mtop025"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
	
	<p><strong><bean:message key="label.erasmus.candidacy.registration" bundle="CANDIDATE_RESOURCES"/></strong></p>
	
	<logic:empty name="individualCandidacyProcess" property="candidacy.registration">
		<bean:message key="message.erasmus.candidacy.candidate.is.not.registered" bundle="ACADEMIC_OFFICE_RESOURCES"/>
	</logic:empty>
	
	<logic:notEmpty name="individualCandidacyProcess" property="candidacy.registration">
		<fr:view name="individualCandidacyProcess" property="candidacy.registration" schema="student.registrationsWithStartData">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 thlight thright mtop025"/>
			</fr:layout>
		</fr:view>
		
		<bean:define id="registrationOID" name="individualCandidacyProcess" property="candidacy.registration.externalId" />
		
		<logic:present role="role(TEACHER)">
			<html:link action="<%= "/viewCurriculum.do?method=prepare&registrationOID=" + registrationOID %>" target="_blank">
				<bean:message key="title.student.curriculum" bundle="APPLICATION_RESOURCES" />
			</html:link>
		</logic:present>
		
	</logic:notEmpty>
	
</logic:notEmpty>
