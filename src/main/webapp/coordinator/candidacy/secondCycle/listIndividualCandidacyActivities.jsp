<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml/>

<bean:define id="processName" name="processName" />
<bean:define id="parentProcessId" name="parentProcess" property="externalId" />
<bean:define id="individualCandidacyProcess" name="process"/>
<bean:define id="degreeCurricularPlanID" name="degreeCurricularPlanID"/>
<bean:define id="candidacy" name="individualCandidacyProcess" property="candidacy" />
<em><bean:message key="label.candidacies" bundle="APPLICATION_RESOURCES"/></em>
<logic:notEmpty name="process">
	<h2><bean:write name="process" property="displayName" /> </h2>
</logic:notEmpty>


<bean:define id="degreeCurricularPlanID" name="degreeCurricularPlanID"/>
<html:link href='<%= request.getContextPath() + "/coordinator/caseHandling" + processName.toString() + ".do?method=listProcesses&parentProcessId=" + parentProcessId.toString() + "&degreeCurricularPlanID=" + degreeCurricularPlanID.toString() %>'>
	« <bean:message key="label.back" bundle="APPLICATION_RESOURCES"/>	
</html:link>


<logic:notEmpty name="process">
	<bean:define id="processId" name="process" property="externalId" />
	
	
	<logic:notEmpty name="activities">
		<%-- list process activities --%>
		<ul>
		<logic:iterate id="activity" name="activities">
			<bean:define id="activityName" name="activity" property="class.simpleName" />
			<li>
				<html:link action='<%= "/caseHandling" + processName.toString() + ".do?method=prepareExecute" + activityName.toString() + "&amp;degreeCurricularPlanID=" + degreeCurricularPlanID.toString() +  "&amp;processId=" + processId.toString()%>'>
					<bean:message name="activity" property="class.name" bundle="CASE_HANDLING_RESOURCES" />
				</html:link>
			</li>
		</logic:iterate>
		</ul>
	</logic:notEmpty>
	
	
	<%-- original application --%>
	<logic:notEmpty name="process" property="originalIndividualCandidacyProcess">
		
		<bean:define id="originalProcessId" name="process" property="originalIndividualCandidacyProcess.externalId" />

		<div class="infoop-blue">
			Esta candidatura foi transferida de um processo de candidaturas anterior. Para ver a candidatura original clique  
			<html:link page="<%= "/caseHandlingSecondCycleIndividualCandidacyProcess.do?method=listProcessAllowedActivities&processId=" + originalProcessId %>">
				aqui.
			</html:link>
		</div>
		
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
	
	<br />
	<strong><bean:message key="label.candidacy.seriesGrade.information" bundle="APPLICATION_RESOURCES" />:</strong>
	<logic:notEmpty name="seriesGrade">
		<fr:view name="seriesGrade" >
			<fr:schema bundle="APPLICATION_RESOURCES" type="net.sourceforge.fenixedu.domain.candidacyProcess.secondCycle.SecondCycleIndividualCandidacySeriesGrade">
				<fr:slot name="degree.name" key="label.candidacy.degree">
				</fr:slot>
				<fr:slot name="professionalExperience" key="label.candidacy.professionalExperience">
				</fr:slot>
				<fr:slot name="affinity" key="label.candidacy.affinity">
				</fr:slot>
				<fr:slot name="degreeNature" key="label.candidacy.degreeNature">
				</fr:slot>
				<fr:slot name="candidacyGrade" key="label.candidacy.grade">
				</fr:slot>
				<fr:slot name="interviewGrade" key="label.candidacy.interviewGrade">
				</fr:slot>
				<fr:slot name="seriesCandidacyGrade" key="label.candidacy.seriesGrade">
				</fr:slot>
				<fr:slot name="state" key="label.candidacy.state">
				</fr:slot>
				<fr:slot name="notes" key="label.candidacy.notes">
				</fr:slot>
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
		        <fr:property name="columnClasses" value="width12em,,"/>
			</fr:layout> 
		</fr:view>
	</logic:notEmpty>
	<logic:empty name="seriesGrade">
		</br><bean:message key="label.candidacy.seriesGrade.notPresent" bundle="APPLICATION_RESOURCES" />
	</logic:empty>
	
	
	<logic:present role="MANAGER">
	<%-- show public candidacy access information --%>
	<h3 style="margin-top: 1em;"><bean:message key="title.public.candidacy.information.access" bundle="CANDIDATE_RESOURCES" />:</h3>
	
	<fr:view name="process" property="candidacyHashCode" schema="PublicCandidacyHashCode.view" >
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
	        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
		</fr:layout>	
	</fr:view>
	</logic:present>

	<h3 style="margin-top: 1em;"><bean:message key="title.public.candidacy.information.email" bundle="CANDIDATE_RESOURCES" />:</h3>
	
	<fr:view name="process" property="candidacyHashCode">
		<fr:schema bundle="CANDIDATE_RESOURCES" type="net.sourceforge.fenixedu.domain.candidacyProcess.DegreeOfficePublicCandidacyHashCode">
			<fr:slot name="email" key="label.email" />
		</fr:schema>
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
	
	<%-- show person information --%>
	<br />
	<strong><bean:message key="label.candidacy.personalData" bundle="APPLICATION_RESOURCES" />:</strong>
	<fr:view name="process" property="personalDetails" schema="CandidacyProcess.personalData">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
	        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
		</fr:layout>
	</fr:view>

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
			<td><fr:view name="individualCandidacyProcess" property="candidacy.event.amountToPay"/></td>
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
