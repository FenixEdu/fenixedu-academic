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

<em><bean:message key="label.candidacies" bundle="APPLICATION_RESOURCES"/></em>
<logic:notEmpty name="process">
	<h2><bean:write name="process" property="displayName" /> </h2>
</logic:notEmpty>

<html:link action='<%= "/caseHandling" + processName.toString() + ".do?method=listProcesses&amp;parentProcessId=" + parentProcessId.toString() %>'>
	« <bean:message key="label.back" bundle="APPLICATION_RESOURCES"/>	
</html:link>
<br/>

<logic:notEmpty name="process">
	<bean:define id="processId" name="process" property="externalId" />
		
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

	<h3 style="margin-top: 1em;"><bean:message key="title.over23.qualifications" bundle="CANDIDATE_RESOURCES"/></h3>
	
	<strong><bean:message key="label.over23.qualifications.concluded" bundle="CANDIDATE_RESOURCES"/>:</strong>
	
	<logic:empty name="process" property="candidacy.concludedFormationList">
		<p class="mtop05"><em><bean:message key="label.over23.has.no.qualifications" bundle="CANDIDATE_RESOURCES"/>.</em></p>	
	</logic:empty>
	
	<logic:notEmpty name="process" property="candidacy.concludedFormationList">
		<table class="tstyle4 thlight thleft">
		<tr>
			<th><bean:message key="label.over23.qualifications.name" bundle="CANDIDATE_RESOURCES"/></th>
			<th><bean:message key="label.over23.school" bundle="CANDIDATE_RESOURCES"/></th>
			<th><bean:message key="label.over23.execution.year.conclusion" bundle="CANDIDATE_RESOURCES"/></th>
		</tr>
		<logic:iterate id="qualification" name="process" property="candidacy.concludedFormationList" indexId="index">
		<tr>
			<td>
				<fr:view name="qualification" property="designation"/>
			</td>
			<td>
				<fr:view name="qualification" property="institution.name">
				</fr:view>	
			</td>
			<td>
				<fr:view name="qualification" property="conclusionExecutionYear.year">
				</fr:view>
			</td>
		</tr>
		</logic:iterate>
		</table>
	</logic:notEmpty>
	
	<strong><bean:message key="label.over23.qualifications.non.concluded" bundle="CANDIDATE_RESOURCES"/>:</strong>
	
	<logic:empty name="process" property="candidacy.nonConcludedFormationList">
		<p class="mtop05"><em><bean:message key="label.over23.has.no.qualifications" bundle="CANDIDATE_RESOURCES"/>.</em></p>	
	</logic:empty>
	
	<logic:notEmpty name="process" property="candidacy.nonConcludedFormationList">
		<table class="tstyle4 thlight thleft">
			<tr>
				<th><bean:message key="label.over23.qualifications.name" bundle="CANDIDATE_RESOURCES"/></th>
				<th><bean:message key="label.over23.school" bundle="CANDIDATE_RESOURCES"/></th>
			</tr>
			<logic:iterate id="qualification" name="process" property="candidacy.nonConcludedFormationList" indexId="index">
			<tr>
				<td>
					<fr:view name="qualification" property="designation">
					</fr:view>
				</td>
				<td>
					<fr:view name="qualification" property="institution.name">
					</fr:view>					
				</td>
			</tr>
			</logic:iterate>
		</table>
	</logic:notEmpty>

	
	<%-- show person information --%>
	<br />
	<h3 style="margin-top: 1em;"><bean:message key="label.candidacy.personalData" bundle="APPLICATION_RESOURCES" />:</h3>
	
	<fr:view name="process" property="personalDetails" schema="CandidacyProcess.personalData">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
	        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
		</fr:layout>
	</fr:view>

	<%-- show public candidacy access information --%>
	<%--
	<h3 style="margin-top: 1em;"><bean:message key="title.public.candidacy.information.access" bundle="CANDIDATE_RESOURCES" />:</h3>
	
	<fr:view name="process" property="candidacyHashCode" schema="PublicCandidacyHashCode.view" >
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
	        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
		</fr:layout>	
	</fr:view>
	--%>
	
	<%-- show documents--%>
	<br/>
	<h2 style="margin-top: 1em;"><bean:message key="label.documentation" bundle="CANDIDATE_RESOURCES"/></h2> 
	
	<bean:define id="individualCandidacyProcess" name="process"/>
	 	
	<logic:empty name="individualCandidacyProcess" property="candidacy.documents">
		<p><em><bean:message key="message.documents.empty" bundle="CANDIDATE_RESOURCES"/>.</em></p>
	</logic:empty>
	
	<logic:notEmpty name="individualCandidacyProcess" property="candidacy.documents">
	<table class="tstyle4 thlight thcenter">
		<tr>
			<th><bean:message key="label.candidacy.document.kind" bundle="CANDIDATE_RESOURCES"/></th>
			<th><bean:message key="label.dateTime.submission" bundle="CANDIDATE_RESOURCES"/></th>
			<th><bean:message key="label.document.file.name" bundle="CANDIDATE_RESOURCES"/></th>
			<th></th>
		</tr>
	
		
		<logic:iterate id="documentFile" name="individualCandidacyProcess" property="candidacy.documents">
		<tr>
			<td><fr:view name="documentFile" property="candidacyFileType"/></td>
			<td><fr:view name="documentFile" property="uploadTime"/></td>
			<td><fr:view name="documentFile" property="filename"/></td>
			<td><fr:view name="documentFile" layout="link"/></td>
		</tr>	
		</logic:iterate>
	</table>
	</logic:notEmpty>
	
</logic:notEmpty>
