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

<em><bean:message key="label.candidacies" bundle="APPLICATION_RESOURCES"/></em>
<h2><bean:message key="label.candidacy.create" bundle="APPLICATION_RESOURCES"/></h2>

<p class="breadcumbs">
	<span><strong><bean:message key="label.step" bundle="APPLICATION_RESOURCES" /> 1</strong>: <bean:message key="label.candidacy.personalData" bundle="APPLICATION_RESOURCES" /> </span> &gt;
	<span class="actual"><strong><bean:message key="label.step" bundle="APPLICATION_RESOURCES" /> 2</strong>: <bean:message key="label.candidacy.candidacyInformation" bundle="APPLICATION_RESOURCES" /> </span>
</p>

<html:messages id="message" message="true" bundle="CANDIDATE_RESOURCES">
	<span class="error0"> <bean:write name="message" /> </span>
	<br />
</html:messages>

<bean:define id="parentProcessId" name="parentProcess" property="externalId" />

<fr:form action='<%= "/caseHandlingOver23IndividualCandidacyProcess.do?userAction=createCandidacy&parentProcessId=" + parentProcessId.toString() %>' id="over23CandidacyForm">
	<input type="hidden" id="methodId" name="method" value="createNewProcess"/>
	<input type="hidden" id="removeIndexId" name="removeIndex" value=""/>
	<input type="hidden" id="skipValidationId" name="skipValidation" value="false"/>

	<fr:edit id="individualCandidacyProcessBean" name="individualCandidacyProcessBean" visible="false" />

	<logic:notEmpty name="individualCandidacyProcessBean" property="candidacyProcess">
	
		<fr:edit id="individualCandidacyProcessBean.candidacyDate" 
			 name="individualCandidacyProcessBean"
			 schema="Over23IndividualCandidacyProcessBean.candidacyDate">
			<fr:layout name="tabular-editable">
				<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
		        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
			</fr:layout>
			<fr:destination name="invalid" path='<%= "/caseHandlingOver23IndividualCandidacyProcess.do?method=fillCandidacyInformationInvalid&amp;parentProcessId=" + parentProcessId.toString() %>' />
		</fr:edit>
	
		<fr:edit id="individualCandidacyProcessBean.degrees"
			name="individualCandidacyProcessBean"
			schema="Over23IndividualCandidacyProcessBean.degrees">
			<fr:layout name="tabular-editable">
				<fr:property name="classes" value="tstyle4 thlight thright"/>
		        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
			</fr:layout>
			<fr:destination name="invalid" path='<%= "/caseHandlingOver23IndividualCandidacyProcess.do?method=fillCandidacyInformationInvalid&amp;parentProcessId=" + parentProcessId.toString() %>' />
		</fr:edit>
		<html:submit onclick="this.form.skipValidation.value='true';this.form.method.value='addDegreeToCandidacy';return true;"><bean:message key="label.add" bundle="APPLICATION_RESOURCES" /></html:submit>
		
		<logic:empty name="individualCandidacyProcessBean" property="selectedDegrees">
			<br/>
			<br/>
			<em><bean:message key="label.candidacy.over23.no.associated.degrees" bundle="APPLICATION_RESOURCES"/>.<span class="highlight1"><bean:message key="label.candidacy.over23.must.select.at.least.one.degree" bundle="APPLICATION_RESOURCES"/></span>.</em>
			<br/>
		</logic:empty>
		<logic:notEmpty name="individualCandidacyProcessBean" property="selectedDegrees">
			<br/>
			<br/>
			<bean:message key="label.candidacy.choosen.degrees" bundle="APPLICATION_RESOURCES"/>:
			<table class="tstyle1 mtop025">
			<logic:iterate id="degree" name="individualCandidacyProcessBean" property="selectedDegrees">
				<tr>
					<td><bean:write name="degree" property="presentationName" /> - <bean:write name="degree" property="sigla" /></td>
					<td>
						<bean:define id="degreeId" name="degree" property="externalId" />
						<html:hidden property="degreeToDelete" value="<%= degreeId.toString() %>" />
						<html:submit onclick="this.form.skipValidation.value='true';this.form.method.value='removeDegreeFromCandidacy';return true;"><bean:message key="label.remove" bundle="APPLICATION_RESOURCES" /></html:submit>
					</td>
				</tr>
			</logic:iterate>
			</table>
			<br/>
		</logic:notEmpty>		
		
		
		<h2 style="margin-top: 1em;"><bean:message key="title.over23.qualifications" bundle="CANDIDATE_RESOURCES"/></h2>
		
		<h3><bean:message key="label.over23.qualifications.concluded" bundle="CANDIDATE_RESOURCES"/></h3>
		<logic:iterate id="qualification" name="individualCandidacyProcessBean" property="formationConcludedBeanList" indexId="index">
			<bean:define id="qualificationId" name="qualification" property="id"/>
			<bean:define id="designationId"><%= "individualCandidacyProcessBean.habilitation.concluded.designation:" + qualificationId %></bean:define>
			<bean:define id="institutionNameId"><%= "individualCandidacyProcessBean.habilitation.concluded.institutionName:" + qualificationId %></bean:define>
			<bean:define id="beginYearId"><%= "individualCandidacyProcessBean.habilitation.concluded.begin.year:" + qualificationId %></bean:define>
			<bean:define id="endYearId"><%= "individualCandidacyProcessBean.habilitation.concluded.end.year:" + qualificationId %></bean:define>
			<bean:define id="designationErrorId"><%=  "error.individualCandidacyProcessBean.formation.designation:" + qualificationId %></bean:define>
			<bean:define id="institutionErrorId"><%=  "error.individualCandidacyProcessBean.formation.institution:" + qualificationId %></bean:define>
			<bean:define id="durationErrorId"><%=  "error.individualCandidacyProcessBean.formation.duration:" + qualificationId %></bean:define>
			<table class="tstyle5 thlight thleft mbottom0">
				<tr>
					<th style="width: 175px;"><bean:message key="label.over23.qualifications.name" bundle="CANDIDATE_RESOURCES"/>: <span class="red">*</span></th>
					<td>
						<fr:edit 	id='<%= designationId %>' 
									name="qualification"
									schema="PublicCandidacyProcessBean.formation.designation">
							<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
						</fr:edit>
					</td>
					<td rowspan="3">
						<p><a onclick='<%= "document.getElementById(\"skipValidationId\").value=\"true\"; document.getElementById(\"removeIndexId\").value=" + index + "; document.getElementById(\"methodId\").value=\"removeConcludedHabilitationsEntry\"; document.getElementById(\"over23CandidacyForm\").submit();" %>' href="#" >Remover </a></p>
					</td>
					<td class="tdclear">
						<span class="error0"><fr:message for="<%= designationId %>"/></span>
					</td>
				</tr>
				<tr>
					<th><bean:message key="label.over23.school" bundle="CANDIDATE_RESOURCES"/>: <span class="red">*</span></th>
					<td>
						<fr:edit 	id='<%= institutionNameId %>' 
							name="qualification"
							schema="PublicCandidacyProcessBean.formation.institutionUnitName.autoComplete">
							<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
						</fr:edit>					
					</td>
					<td class="tdclear">
						<span class="error0"><fr:message for="<%= institutionNameId %>"/></span>
					</td>					
				</tr>
				<tr>
					<th><bean:message key="label.over23.execution.year.conclusion" bundle="CANDIDATE_RESOURCES"/>: <span class="red">*</span></th>
					<td>
						<fr:edit 	id='<%= endYearId %>'
									name="qualification"
									schema="PublicCandidacyProcessBean.over23.execution.year.conclusion">
							<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
						</fr:edit>
					</td>
					<td class="tdclear">
		                <span class="error0"><fr:message for="<%= endYearId %>"/></span>
					</td>					
				</tr>
			</table>
		</logic:iterate>
		<p class="mtop05 mbottom2"><a onclick="document.getElementById('skipValidationId').value='true'; document.getElementById('methodId').value='addConcludedHabilitationsEntry'; document.getElementById('over23CandidacyForm').submit();" href="#">+ Adicionar</a></p>
		
		<h3><bean:message key="label.over23.qualifications.non.concluded" bundle="CANDIDATE_RESOURCES"/></h3>
		<logic:iterate id="qualification" name="individualCandidacyProcessBean" property="formationNonConcludedBeanList" indexId="index">
			<bean:define id="qualificationId" name="qualification" property="id"/>
			<bean:define id="designationId"><%= "individualCandidacyProcessBean.habilitation.concluded.designation:" + qualificationId %></bean:define>
			<bean:define id="institutionNameId"><%= "individualCandidacyProcessBean.habilitation.concluded.institutionName:" + qualificationId %></bean:define>
			<bean:define id="beginYearId"><%= "individualCandidacyProcessBean.habilitation.concluded.begin.year:" + qualificationId %></bean:define>
			<bean:define id="endYear"><%= "individualCandidacyProcessBean.habilitation.concluded.end.year:" + qualificationId %></bean:define>
			<bean:define id="designationErrorId"><%=  "error.individualCandidacyProcessBean.formation.designation:" + qualificationId %></bean:define>
			<bean:define id="institutionErrorId"><%=  "error.individualCandidacyProcessBean.formation.institution:" + qualificationId %></bean:define>
			<bean:define id="durationErrorId"><%=  "error.individualCandidacyProcessBean.formation.duration:" + qualificationId %></bean:define>
		
			<table class="tstyle5 thlight thleft mbottom0">
				<tr>
					<th style="width: 175px;"><bean:message key="label.over23.qualifications.name" bundle="CANDIDATE_RESOURCES"/>: <span class="red">*</span></th>
					<td>
						<fr:edit 	id='<%= designationId %>' 
									name="qualification"
									schema="PublicCandidacyProcessBean.formation.designation">
							<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
						</fr:edit>
					</td>
					<td rowspan="3">
						<p><a onclick='<%= "document.getElementById(\"skipValidationId\").value=\"true\"; document.getElementById(\"removeIndexId\").value=" + index + "; document.getElementById(\"methodId\").value=\"removeNonConcludedHabilitationsEntry\"; document.getElementById(\"over23CandidacyForm\").submit();" %>' href="#" >Remover </a></p>
					</td>
					<td class="tdclear">
		                <span class="error0"><fr:message for="<%= designationId %>"/></span>
					</td>
				</tr>
				<tr>
					<th><bean:message key="label.over23.school" bundle="CANDIDATE_RESOURCES"/>: <span class="red">*</span></th>
					<td>
						<fr:edit 	id='<%= institutionNameId %>' 
							name="qualification"
							schema="PublicCandidacyProcessBean.formation.institutionUnitName.autoComplete">
							<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
						</fr:edit>					
					</td>
					<td class="tdclear">
		                <span class="error0"><fr:message for="<%= institutionNameId %>"/></span>
					</td>
				</tr>
			</table>
		</logic:iterate>
		<p class="mtop05 mbottom2"><a onclick="document.getElementById('skipValidationId').value='true'; document.getElementById('methodId').value='addNonConcludedHabilitationsEntry'; document.getElementById('over23CandidacyForm').submit();" href="#">+ Adicionar</a></p>

		<h3><bean:message key="label.over23.languages" bundle="CANDIDATE_RESOURCES"/></h3>
		
		<p class="mbottom05"><bean:message key="label.over23.languages.read" bundle="CANDIDATE_RESOURCES"/>&nbsp;<bean:message key="label.delimited.by.commas" bundle="CANDIDATE_RESOURCES"/>:</p>
		<fr:edit 	id='PublicCandidacyProcessBean.over23.languages.read' 
					name="individualCandidacyProcessBean"
					schema="PublicCandidacyProcessBean.over23.languages.read">
			<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
		</fr:edit>					
		
		<p class="mbottom05"><bean:message key="label.over23.languages.write" bundle="CANDIDATE_RESOURCES"/>&nbsp;<bean:message key="label.delimited.by.commas" bundle="CANDIDATE_RESOURCES"/>:</p>
		<fr:edit 	id='PublicCandidacyProcessBean.over23.languages.write' 
					name="individualCandidacyProcessBean"
					schema="PublicCandidacyProcessBean.over23.languages.write">
			<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
		</fr:edit>					

		<p class="mbottom05"><bean:message key="label.over23.languages.speak" bundle="CANDIDATE_RESOURCES"/>&nbsp;<bean:message key="label.delimited.by.commas" bundle="CANDIDATE_RESOURCES"/>:</p>
		<fr:edit 	id='PublicCandidacyProcessBean.over23.languages.speak' 
					name="individualCandidacyProcessBean"
					schema="PublicCandidacyProcessBean.over23.languages.speak">
			<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
		</fr:edit>		

		<h2 style="margin-top: 1em;"><bean:message key="label.over23.disabilities" bundle="CANDIDATE_RESOURCES"/></h2>
		<p><bean:message key="message.over23.disabilities.detail" bundle="CANDIDATE_RESOURCES"/>:</p>
		<fr:edit 	id="individualCandidacyProcessBean.disabilities"
					name="individualCandidacyProcessBean"
					schema="PublicCandidacyProcessBean.over23.disabilities">
			<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
		</fr:edit>
		
		<br/>
		
		<h3><bean:message key="message.is.student.of.utl.network" bundle="CANDIDATE_RESOURCES"/> <span class="red">*</span></h3>
		<fr:edit id="individualCandidacyProcessBean.utlStudent" name="individualCandidacyProcessBean"
			schema="PublicCandidacyProcessBean.utl.student">
			<fr:layout name="flow">
				<fr:property name="labelExcluded" value="true"/>
			</fr:layout>
		</fr:edit>
		
	</logic:notEmpty>

	<p>	
	<html:submit onclick="this.form.skipValidation.value='false';this.form.method.value='createNewProcess'; return true;"><bean:message key="label.create" bundle="APPLICATION_RESOURCES" /></html:submit>
	<html:cancel onclick="this.form.method.value='listProcesses'; return true;"><bean:message key="label.cancel" bundle="APPLICATION_RESOURCES" /></html:cancel>
	</p>
</fr:form>
