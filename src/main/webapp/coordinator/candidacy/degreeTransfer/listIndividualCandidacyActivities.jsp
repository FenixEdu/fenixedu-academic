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
<bean:define id="degreeCurricularPlanID" name="degreeCurricularPlanID"/>

<bean:define id="processName" name="processName" />
<bean:define id="parentProcessId" name="parentProcess" property="externalId" />
<bean:define id="individualCandidacyProcess" name="process"/>

<jsp:include page="/coordinator/context.jsp" />

<logic:notEmpty name="process">
	<h2><bean:write name="process" property="displayName" /> </h2>
</logic:notEmpty>

<html:link action='<%= "/caseHandling" + processName.toString() + ".do?method=listProcesses&amp;parentProcessId=" + parentProcessId.toString()  + "&amp;degreeCurricularPlanID=" + degreeCurricularPlanID.toString() %>'>
	« <bean:message key="label.back" bundle="APPLICATION_RESOURCES"/>	
</html:link>
<br/>

<logic:notEmpty name="process">
	<bean:define id="processId" name="process" property="externalId" />
	
	<logic:notEmpty name="activities">
		<%-- list process activities --%>
		<ul>
		<logic:iterate id="activity" name="activities">
			<bean:define id="activityName" name="activity" property="class.simpleName" />
			<li>
				<html:link action='<%= "/caseHandling" + processName.toString() + ".do?method=prepareExecute" + activityName.toString() + "&amp;processId=" + processId.toString() + "&amp;degreeCurricularPlanID=" + degreeCurricularPlanID.toString()%>'>
					<bean:message name="activity" property="class.name" bundle="CASE_HANDLING_RESOURCES" />
				</html:link>
			</li>
		</logic:iterate>
		</ul>
	</logic:notEmpty>	
	
	<%-- student information --%>
	<logic:notEmpty name="process" property="personalDetails.student">
		<br/>
		<strong><bean:message key="label.studentDetails" bundle="APPLICATION_RESOURCES"/>:</strong>
		<fr:view name="process" property="personalDetails.student" schema="student.show.number.information">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
	
	<%-- show candidacy information --%>
	<br />
	<strong><bean:message key="label.candidacy.data" bundle="APPLICATION_RESOURCES"/>:</strong>
	<fr:view name="process" schema='<%= processName.toString() +  ".view" %>'>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
	        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
		</fr:layout>
	</fr:view>

	<h3><bean:message key="title.other.academic.titles" bundle="CANDIDATE_RESOURCES"/></h3>
	<logic:empty name="process" property="candidacy.concludedFormationList">
		<p><em><bean:message key="message.other.academic.titles.empty" bundle="CANDIDATE_RESOURCES"/>.</em></p>	
	</logic:empty>
		
	<logic:notEmpty name="process" property="candidacy.concludedFormationList">
		<table class="tstyle2 thlight thcenter">
		<tr>
			<th><bean:message key="label.other.academic.titles.program.name" bundle="CANDIDATE_RESOURCES"/></th>
			<th><bean:message key="label.other.academic.titles.institution" bundle="CANDIDATE_RESOURCES"/></th>
			<th><bean:message key="label.other.academic.titles.conclusion.date" bundle="CANDIDATE_RESOURCES"/></th>
			<th><bean:message key="label.other.academic.titles.conclusion.grade" bundle="CANDIDATE_RESOURCES"/></th>
		</tr>
		<logic:iterate id="academicTitle" name="process" property="candidacy.concludedFormationList" indexId="index">
		<tr>
			<td>
				<fr:view name="academicTitle" property="designation"/>
			</td>
			<td>
				<fr:view name="academicTitle" property="institution.name"/>
			</td>
			<td>
				<fr:view name="academicTitle" property="year"/>
			</td>
			<td>
				<fr:view name="academicTitle" property="conclusionGrade"/>
			</td>
		</tr>
		</logic:iterate>
		</table>
	</logic:notEmpty>

	<br />
	<br />
	<strong>Informação de seriação:</strong>
	<fr:view name="seriesGrade" >
		<fr:schema bundle="APPLICATION_RESOURCES" type="net.sourceforge.fenixedu.domain.candidacyProcess.degreeTransfer.DegreeTransferIndividualCandidacyResultBean">
			<fr:slot name="degree.name" key="label.candidacy.degree">
			</fr:slot>
			<fr:slot name="professionalExperience" key="label.candidacy.professionalExperience">
			</fr:slot>
			<fr:slot name="affinity" key="label.candidacy.affinity">
			</fr:slot>
			<fr:slot name="degreeNature" key="label.candidacy.degreeNature">
			</fr:slot>
			<fr:slot name="approvedEctsRate" key="label.candidacy.grade">
			</fr:slot>
			<fr:slot name="gradeRate" key="label.candidacy.interviewGrade">
			</fr:slot>
			<fr:slot name="seriesCandidacyGrade" key="label.candidacy.seriesGrade">
			</fr:slot>
			<fr:slot name="state" key="label.candidacy.state">
			</fr:slot>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
	        <fr:property name="columnClasses" value="width12em,,"/>
		</fr:layout> 
	</fr:view>

	
	<%-- show person information --%>
	<br />
	<strong><bean:message key="label.candidacy.personalData" bundle="APPLICATION_RESOURCES" />:</strong>
	<fr:view name="process" property="personalDetails" schema="CandidacyProcess.personalData">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
	        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
		</fr:layout>
	</fr:view>
	
	<%-- show person address information --%>
	<logic:notEmpty name="process" property="personalDetails">
		<fr:view name="process" property="personalDetails" schema="CandidacyProcess.personPhysicalAddress">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
	        	<fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>

	<%-- Observations --%>
	<h2 style="margin-top: 1em;"><bean:message key="label.observations" bundle="CANDIDATE_RESOURCES"/>:</h2>
	<fr:view name="process"
		property="candidacy.observations">
	</fr:view>

	<%-- Payment Code --%>
	<logic:notEmpty name="individualCandidacyProcess" property="associatedPaymentCode">
	<p><bean:message key="message.sibs.payment.code" bundle="CANDIDATE_RESOURCES"/></p>
	<table>
		<tr>
			<td><strong><bean:message key="label.sibs.entity.code" bundle="CANDIDATE_RESOURCES"/></strong></td>
			<td><bean:write name="sibsEntityCode"/></td>
		</tr>
		<tr>
			<td><strong><bean:message key="label.sibs.payment.code" bundle="CANDIDATE_RESOURCES"/></strong></td>
			<td><fr:view name="individualCandidacyProcess" property="associatedPaymentCode.formattedCode"/></td>
		</tr>
		<tr>
			<td><strong><bean:message key="label.sibs.amount" bundle="CANDIDATE_RESOURCES"/></strong></td>
			<td><fr:view name="individualCandidacyProcess" property="associatedPaymentCode.minAmount"/></td>
		</tr>
	</table>
	</logic:notEmpty>

	<%-- show documents--%>
	<br/>
	<h2 style="margin-top: 1em;"><bean:message key="label.documentation" bundle="CANDIDATE_RESOURCES"/></h2> 
	
	<logic:empty name="individualCandidacyProcess" property="candidacy.documents">
		<p><em><bean:message key="message.documents.empty" bundle="CANDIDATE_RESOURCES"/>.</em></p>
	</logic:empty>
	
	<logic:notEmpty name="individualCandidacyProcess" property="candidacy.documents">
	<table class="tstyle4 thlight thcenter">
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

</logic:notEmpty>
