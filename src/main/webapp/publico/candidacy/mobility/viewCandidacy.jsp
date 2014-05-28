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
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ page import="org.fenixedu.commons.i18n.I18N"%>
<%@ page import="java.util.Locale"%>

<%@ page import="net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyDocumentFile" %>
<%@ page import="net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityIndividualApplicationProcess" %>
<%@ page import="net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.NationalIdCardAvoidanceQuestion" %>

<%!
	static String f(String value, Object ... args) {
    	return String.format(value, args);
	}
%>

<html:xhtml/>

<style type="text/css">

.somewidth2em {
	padding: 2em;
}

.somewidth3em {
	padding: 3em;
}

.nopadding ul {
	padding: 0em;
}

</style>

<bean:define id="mappingPath" name="mappingPath"/>
<bean:define id="fullPath"><%= request.getContextPath() + "/publico" + mappingPath + ".do" %></bean:define>
<bean:define id="applicationInformationLinkDefault" name="application.information.link.default"/>
<bean:define id="applicationInformationLinkEnglish" name="application.information.link.english"/>

<bean:define id="individualCandidacyProcessBean" name="individualCandidacyProcessBean" type="net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityIndividualApplicationProcessBean"/>
<bean:define id="individualCandidacyProcess" name="individualCandidacyProcessBean" property="individualCandidacyProcess" type="MobilityIndividualApplicationProcess"/>
<bean:define id="processId" name="individualCandidacyProcess" property="externalId"/>

<div class="breadcumbs">
	<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="http://gri.ist.utl.pt/en">NMCI</a> &gt;
	<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="http://gri.ist.utl.pt/en/ist/">Study at <%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%></a> &gt;
	<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href='<%= f("%s/candidacies/erasmus", request.getContextPath()) %>'><bean:message key="title.application.name.mobility" bundle="CANDIDATE_RESOURCES"/></a> &gt;
	<bean:message key="erasmus.title.application.submission" bundle="CANDIDATE_RESOURCES" />
</div>

<h1><bean:write name="application.name"/></h1>
<bean:define id="mobilityProgram" name="individualCandidacyProcessBean" property="mobilityStudentDataBean.selectedMobilityProgram.registrationAgreement.description"/>
<h1><strong><bean:write name="mobilityProgram"/></strong></h1>

<logic:equal name="individualCandidacyProcess" property="isCandidacyProcessWithEidentifer" value="false">
<logic:equal name="individualCandidacyProcess" property="candidacyExecutionInterval.name" value="2011/2012">
	<div class="h_box">
	
		<h2 class="mtop1 mbottom05">Application submission with National Identification Card</h2>
		<%  if(NationalIdCardAvoidanceQuestion.UNANSWERED.equals(individualCandidacyProcess.getCandidacy().getNationalIdCardAvoidanceQuestion())) { %>
			<p>
				<strong>
				The Erasmus web application allowed you to authenticate using your National citizen identification. 
				By using such method you would be able to access all the information of your course (schedules, classes, etc.) 
				before arriving to <%= net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionName().getContent() %> (in fact you would be receiving an email just now to inform on how to proceed), 
				however you choose not to use it. <%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%> would like to know the reason for that choice, please choose one of the available reasons:
				</strong>
			</p>
			
			<fr:form action='<%= mappingPath + ".do?method=answerNationalIdCardAvoidanceQuestion&processId=" + processId %>'>
				<fr:edit id="individualCandidacyProcessBean" name="individualCandidacyProcessBean" visible="false" />
				
				<fr:edit id="individualCandidacyProcessBean-question" name="individualCandidacyProcessBean">
					<fr:schema type="net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityIndividualApplicationProcessBean" bundle="CANDIDATE_RESOURCES">
						<fr:slot name="nationalIdCardAvoidanceQuestion" required="true" layout="radio-postback">
							<fr:property name="excludedValues" value="UNANSWERED" />
							<fr:property name="bundle" value="CANDIDATE_RESOURCES" />
							<fr:property name="destination" value="postback" />
							<fr:property name="classes" value="nobullet"/>
						</fr:slot>
					</fr:schema>
					
					<fr:layout name="tabular">
						<fr:property name="columnClasses" value="somewidth2em,nopadding,tderror1" />
				        <fr:property name="requiredMarkShown" value="false" />
				        <fr:property name="displayLabel" value="false" />
					</fr:layout>
										
					<fr:destination name="invalid" path="<%= mappingPath + ".do?method=answerNationalIdCardAvoidanceQuestionInvalid&processId=" + processId %>" />
					<fr:destination name="postback" path="<%= mappingPath + ".do?method=answerNationalIdCardAvoidanceQuestionPostback&processId=" + processId %>" />
				</fr:edit>

				<% if(NationalIdCardAvoidanceQuestion.OTHER_REASON.equals(individualCandidacyProcessBean.getNationalIdCardAvoidanceQuestion())) { %>
				<fr:edit id="individualCandidacyProcessBean-otherReason" name="individualCandidacyProcessBean">
					<fr:schema type="net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityIndividualApplicationProcessBean" bundle="CANDIDATE_RESOURCES">
						<fr:slot name="idCardAvoidanceOtherReason" required="true" layout="longText" >
							<fr:property name="columnClasses" value="somewidth,nopadding,tderror1" />
							<fr:property name="columns" value="50" />
							<fr:property name="rows" value="4" />
						</fr:slot>
					</fr:schema>

					<fr:layout name="tabular">
				        <fr:property name="columnClasses" value="somewidth3em,nopadding,tderror1" />
				        <fr:property name="requiredMarkShown" value="false" />
				        <fr:property name="displayLabel" value="false" />
					</fr:layout>

					<fr:destination name="invalid" path="<%= mappingPath + ".do?method=answerNationalIdCardAvoidanceQuestionInvalid&processId=" + processId %>" />
				</fr:edit>				
				<% } %>

				<p class="somewidth2em">
					<html:submit><bean:message key="label.submit" bundle="APPLICATION_RESOURCES" /></html:submit>
				</p>
			</fr:form>
		<% } %>
		
		<% if(!NationalIdCardAvoidanceQuestion.UNANSWERED.equals(individualCandidacyProcess.getCandidacy().getNationalIdCardAvoidanceQuestion())) { %>
			<p>
				<strong>
				Thank you for the time and effort you have taken in responding to this inquiry.
				</strong> 
			</p>
		<% } %>
	</div>
</logic:equal>
</logic:equal>


<p>
	<b class="highlight1"><bean:message key="label.process.id" bundle="CANDIDATE_RESOURCES"/>: <bean:write name="individualCandidacyProcess" property="processCode"/></b>
</p>

<logic:equal name="individualCandidacyProcess" property="allRequiredFilesUploaded" value="false">

	<p><span class="infoop2"><bean:message key="message.candidacy.warning.incomplete.process" bundle="CANDIDATE_RESOURCES"/></span></p>
	<p><span class="infoop2"><bean:message key="message.missing.document.files" bundle="CANDIDATE_RESOURCES"/></span></p>
	
	<ul>
		<li><b>Passport photo</b> - The photo will be used to generate <%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%> student card.</li>
		<li><b>Passport or identity card</b></li>
		<li><b>Learning agreement</b> - You're required to download, sign, stamp and reupload the document.</li>
		<li><b>Curriculum vitae</b></li>
		<li><b>Transcript of records</b></li>
		<li><b>Declaration of your english level</b></li>
	</ul>
			
	<p class="mbottom05"><em><bean:message key="message.ist.conditions.note" arg0="<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>" bundle="CANDIDATE_RESOURCES"/></em></p>
</logic:equal>

<% if(!individualCandidacyProcess.getValidatedByGri() || !individualCandidacyProcess.getValidatedByMobilityCoordinator()) { %>

<div class="h_box">
	<h3 class="mtop05">Learning Agreement</h3>
	<p>Please download the Learning Agreement document below.</p>
	<p>Please note that the document must be signed and stamped by your school before you upload.</p>
	<p class="mbottom05"><html:link page="<%= mappingPath + ".do?method=retrieveLearningAgreement&processId=" + processId %>">Download learning agreement</html:link></p>
</div>
<% } %>

<script src="<%= request.getContextPath() + "/javaScript/jquery/jquery.js" %>" type="text/javascript" >
</script>

<logic:equal value="true" name="isApplicationSubmissionPeriodValid">
<fr:form action='<%= mappingPath + ".do" %>' id="editCandidacyForm">
	<input type="hidden" name="method" id="methodForm"/>
	<fr:edit id="individualCandidacyProcessBean" name="individualCandidacyProcessBean" visible="false" />
	<noscript>
		<html:submit onclick="this.form.method.value='prepareEditCandidacyProcess';"><bean:message key="button.edit" bundle="APPLICATION_RESOURCES" /></html:submit>
		<html:submit onclick="this.form.method.value='prepareEditCandidacyDocuments';"><bean:message key="label.edit.candidacy.documents" bundle="CANDIDATE_RESOURCES" /></html:submit>
		<html:cancel><bean:message key="label.back" bundle="APPLICATION_RESOURCES" /></html:cancel>
	</noscript>
	<p class="mtop15">
		<a href="#" onclick="$('#methodForm').attr('value', 'prepareEditCandidacyProcess'); $('#editCandidacyForm').submit();"><bean:message key="button.edit" bundle="APPLICATION_RESOURCES" /> <bean:message key="label.application.lowercase" bundle="CANDIDATE_RESOURCES"/></a> | 
		<a href="#" onclick="$('#methodForm').attr('value', 'prepareEditCandidacyInformation'); $('#editCandidacyForm').submit();"><bean:message key="label.edit.application.educational.background" bundle="CANDIDATE_RESOURCES"/></a> |
		<a href="#" onclick="$('#methodForm').attr('value', 'prepareEditDegreeAndCourses'); $('#editCandidacyForm').submit();"><bean:message key="erasmus.label.edit.degree.and.courses" bundle="CANDIDATE_RESOURCES" /></a> | 
		<a href="#" onclick="$('#methodForm').attr('value', 'prepareEditCandidacyDocuments'); $('#editCandidacyForm').submit();"> <b><bean:message key="label.edit.candidacy.documents" bundle="CANDIDATE_RESOURCES" /></b></a>

<%-- 		
		<logic:equal value="false" name="individualCandidacyProcess" property="isCandidacyProcessWithEidentifer">
			! <a href="#" onclick="$('#methodForm').attr('value', 'prepareBindLinkSubmitedIndividualCandidacyWithStork'); $('#editCandidacyForm').submit();"> <b><bean:message key="label.bind.national.citizen.card" bundle="CANDIDATE_RESOURCES" /></b></a>
		</logic:equal>
--%>

	</p>
</fr:form>
</logic:equal>

<logic:equal value="false" name="isApplicationSubmissionPeriodValid">

<%-- <% if(!individualCandidacyProcess.getValidatedByGri() || !individualCandidacyProcess.getValidatedByMobilityCoordinator()) { %>
<fr:form action='<%= mappingPath + ".do" %>' id="editCandidacyForm">
	<input type="hidden" name="method" id="methodForm"/>
	<fr:edit id="individualCandidacyProcessBean" name="individualCandidacyProcessBean" visible="false" />
	<noscript>
		<html:submit onclick="this.form.method.value='prepareEditCandidacyDocuments';"><bean:message key="label.edit.candidacy.documents" bundle="CANDIDATE_RESOURCES" /></html:submit>
		<html:cancel><bean:message key="label.back" bundle="APPLICATION_RESOURCES" /></html:cancel>
	</noscript>
	<p class="mtop15">
		<a href="#" onclick="$('#methodForm').attr('value', 'prepareEditCandidacyDocuments'); $('#editCandidacyForm').submit();"> <b><bean:message key="label.edit.candidacy.documents" bundle="CANDIDATE_RESOURCES" /></b></a>
	</p>
</fr:form>	
<% } %> --%>
<p><em><bean:message key="message.application.submission.period.ended" bundle="CANDIDATE_RESOURCES"/></em></p>

</logic:equal>



<h2 class="mtop1 mbottom05"><bean:message key="title.personal.data" bundle="CANDIDATE_RESOURCES"/></h2>

<logic:equal name="individualCandidacyProcessBean" property="individualCandidacyProcess.isCandidateWithRoles" value="true">
<fr:view name="individualCandidacyProcessBean" 
	schema="PublicCandidacyProcess.candidacyDataBean.internal.candidate.view">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight thleft"/>
        <fr:property name="columnClasses" value="width175px,,,,"/>
	</fr:layout>
</fr:view>
</logic:equal>

<logic:equal name="individualCandidacyProcessBean" property="individualCandidacyProcess.isCandidateWithRoles" value="false">
<fr:view name="individualCandidacyProcess" property="personalDetails" 
	schema="ErasmusCandidacyProcess.personalData">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight thleft"/>
        <fr:property name="columnClasses" value="width175px,,,,"/>
	</fr:layout>
</fr:view>
</logic:equal>



<h2 class="mtop1 mbottom05"><bean:message key="label.documentation" bundle="CANDIDATE_RESOURCES"/></h2> 

<logic:empty name="individualCandidacyProcess" property="candidacy.documents">
	<p>
		<em><bean:message key="message.documents.empty" bundle="CANDIDATE_RESOURCES"/>.</em>
	</p>
</logic:empty>

<logic:notEmpty name="individualCandidacyProcess" property="activeDocumentFiles">
<table class="tstyle2 thlight thcenter">
	<tr>
		<th><bean:message key="label.candidacy.document.kind" bundle="CANDIDATE_RESOURCES"/></th>
		<th><bean:message key="label.dateTime.submission" bundle="CANDIDATE_RESOURCES"/></th>
		<th><bean:message key="label.document.file.name" bundle="CANDIDATE_RESOURCES"/></th>
	</tr>

	
	<logic:iterate id="documentFile" name="individualCandidacyProcess" property="activeDocumentFiles">
	<tr>
		<td><fr:view name="documentFile" property="candidacyFileType"/></td>
		<td><fr:view name="documentFile" property="uploadTime"/></td>
		<td><fr:view name="documentFile" property="filename"/></td>
	</tr>	
	</logic:iterate>
</table>
</logic:notEmpty>


<logic:equal value="true" name="isApplicationSubmissionPeriodValid">
	<% if(!individualCandidacyProcess.getValidatedByGri() || !individualCandidacyProcess.getValidatedByMobilityCoordinator()) { %>
	<p><a href="#" onclick="javascript:document.getElementById('methodForm').value='prepareEditCandidacyDocuments';document.getElementById('editCandidacyForm').submit();"> <bean:message key="label.edit.candidacy.documents" bundle="CANDIDATE_RESOURCES" /></a></p>
	<% } %>
</logic:equal>

	<h2 class="mtop15 mbottom05"><bean:message key="label.erasmus.home.institution" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

	<fr:view name="individualCandidacyProcess" property="candidacy.mobilityStudentData" schema="ErasmusIndividualCandidacyProcess.home.institution.view">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight thright mtop025"/>
	        <fr:property name="columnClasses" value="width175px,,,,"/>
		</fr:layout>
	</fr:view>


	<h2 class="mtop1 mbottom05"><bean:message key="label.erasmus.current.study" bundle="ACADEMIC_OFFICE_RESOURCES" /></h2>
	<em><bean:message key="label.erasmus.current.study.detailed" bundle="ACADEMIC_OFFICE_RESOURCES" /></em>
	<fr:view name="individualCandidacyProcess" schema="MobilityIndividualApplicationProcess.current.study.view" property="candidacy.mobilityStudentData">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight thright mtop025"/>
	        <fr:property name="columnClasses" value="width175px,,,,"/>
		</fr:layout>
	</fr:view>
	
	<h2 class="mtop1 mbottom05"><bean:message key="label.erasmus.period.of.study" bundle="ACADEMIC_OFFICE_RESOURCES" /></h2>
	<fr:view name="individualCandidacyProcess" schema="MobilityIndividualAppicationProcess.period.of.study.view" property="candidacy.mobilityStudentData">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight thright mtop025"/>
	        <fr:property name="columnClasses" value="width175px,,,,"/>
		</fr:layout>
	</fr:view>


	<h2 class="mtop1 mbottom05"><bean:message key="label.erasmus.courses" bundle="CANDIDATE_RESOURCES"/></h2>

	<table class="tstyle2 thlight thcenter">
	<tr>
		<th><bean:message key="label.erasmus.course" bundle="ACADEMIC_OFFICE_RESOURCES"/></th>
	</tr>
	<logic:iterate id="course" name="individualCandidacyProcess" property="sortedSelectedCurricularCourses" indexId="index">
		<bean:define id="curricularCourseId" name="course" property="externalId" />
	<tr>
		<td>
			<fr:view 	name="course"
						property="nameI18N">
<%-- 				<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>--%>
			</fr:view>
		</td>
	</tr>
	</logic:iterate>
	</table>	

	<h2 class="mtop1 mbottom05"><bean:message key="label.erasmus.appliedSemester" bundle="ACADEMIC_OFFICE_RESOURCES" /></h2>
	<fr:view	name="individualCandidacyProcess"
				property="candidacy.mobilityStudentData"
				schema="MobilityStudentDataBean.applyForSemester.view">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight thright mtop025"/>
	        <fr:property name="columnClasses" value="width175px,,,,"/>
		</fr:layout>
	</fr:view>

	<%-- Observations --%>
	<h2 style="margin-top: 1em;"><bean:message key="label.observations" bundle="CANDIDATE_RESOURCES"/></h2>
	<logic:present name="individualCandidacyProcess" property="candidacy.observations">
		<fr:view name="individualCandidacyProcess" property="candidacy.observations"></fr:view>
	</logic:present>

	<%-- show approved learning agreements--%>

	<p><strong><bean:message key="label.erasmus.approved.learning.agreements" bundle="CANDIDATE_RESOURCES"/></strong></p> 
	
	<logic:empty name="individualCandidacyProcess" property="candidacy.approvedLearningAgreements" >
		<p class="mbottom05"><em><bean:message key="label.erasmus.approved.learning.agreements.empty" bundle="ACADEMIC_OFFICE_RESOURCES" /></em></p>
	</logic:empty>
	
	<logic:equal name="individualCandidacyProcess" property="studentAccepted" value="true">
	<logic:notEmpty name="individualCandidacyProcess" property="candidacy.mostRecentApprovedLearningAgreement" >
		<fr:view name="individualCandidacyProcess" property="candidacy.mostRecentApprovedLearningAgreement">
			<fr:schema type="net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyDocumentFile" bundle="CANDIDATE_RESOURCES">
				<%-- <fr:slot name="filename" key="label.document.file.name" /> --%>
				<fr:slot name="this" key="label.document.file.link" layout="link"/>
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 thlight thright mtop025"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
	</logic:equal>


<div class="mtop2" id="contacts">
	<bean:message key="erasmus.contacts.text" arg0="<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionName().getContent()%>" bundle="CANDIDATE_RESOURCES" />
</div>


