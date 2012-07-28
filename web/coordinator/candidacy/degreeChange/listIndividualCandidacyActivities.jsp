<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<bean:define id="processName" name="processName" />
<bean:define id="parentProcessId" name="parentProcess" property="idInternal" />
<bean:define id="individualCandidacyProcess" name="process"/>v
<bean:define id="degreeCurricularPlanID" name="degreeCurricularPlanID"/>

<em><bean:message key="label.candidacies" bundle="APPLICATION_RESOURCES"/></em>
<logic:notEmpty name="process">
	<h2><bean:write name="process" property="displayName" /> </h2>
</logic:notEmpty>

<html:link action='<%= "/caseHandling" + processName.toString() + ".do?method=listProcesses&amp;parentProcessId=" + parentProcessId.toString() + "&amp;degreeCurricularPlanID=" + degreeCurricularPlanID.toString() %>'>
	« <bean:message key="label.back" bundle="APPLICATION_RESOURCES"/>	
</html:link>
<br/>

<logic:notEmpty name="process">
	<bean:define id="processId" name="process" property="idInternal" />
	
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
