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

<%@ page import="net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyDocumentFile" %>

<html:xhtml/>

<bean:define id="mappingPath" name="mappingPath"/>
<bean:define id="fullPath"><%= request.getContextPath() + "/publico" + mappingPath + ".do" %></bean:define>

<div class="breadcumbs">
	<a href="<%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %>"><%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%></a> &gt;
	<a href="<%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %>pt/candidatos/"><bean:message key="title.candidate" bundle="CANDIDATE_RESOURCES"/></a> &gt;
	<a href="<%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %>pt/candidatos/candidaturas/"><bean:message key="title.degrees" bundle="CANDIDATE_RESOURCES"/></a> &gt;
	<a href="<%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %>pt/candidatos/candidaturas/licenciaturas/maiores_23/"><bean:write name="application.name"/> </a> &gt;
	<bean:message key="title.view.candidacy.process" bundle="CANDIDATE_RESOURCES"/>
</div>

<h1><bean:write name="application.name"/></h1>


<logic:equal name="individualCandidacyProcessBean" property="individualCandidacyProcess.allRequiredFilesUploaded" value="false">
<div class="h_box_alt">
	<div class="lightbulb">
		<p><bean:message key="message.missing.document.files" bundle="CANDIDATE_RESOURCES"/></p>
		<ul>
			<logic:iterate id="missingDocumentFileType" name="individualCandidacyProcessBean" property="individualCandidacyProcess.missingRequiredDocumentFiles">
				<li><fr:view name="missingDocumentFileType"/></li>
			</logic:iterate>
		</ul>
		<p><bean:message key="message.ist.conditions.note" arg0="<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>" bundle="CANDIDATE_RESOURCES"/></p>
	</div>
</div>
</logic:equal>


<logic:equal value="true" name="isApplicationSubmissionPeriodValid">
<fr:form action='<%= mappingPath + ".do" %>' id="editCandidacyForm">
	<input type="hidden" name="method" id="methodForm"/>
	<fr:edit id="individualCandidacyProcessBean" name="individualCandidacyProcessBean" visible="false" />
	<noscript>
	<html:submit onclick="this.form.method.value='prepareEditCandidacyProcess';"><bean:message key="button.edit" bundle="APPLICATION_RESOURCES" /></html:submit>
	<html:submit onclick="this.form.method.value='prepareEditCandidacyDocuments';"><bean:message key="button.edit" bundle="APPLICATION_RESOURCES" /> Documentos</html:submit>
	<html:cancel><bean:message key="label.back" bundle="APPLICATION_RESOURCES" /></html:cancel>
	</noscript>
	
	<a href="#" onclick="javascript:document.getElementById('methodForm').value='prepareEditCandidacyProcess';document.getElementById('editCandidacyForm').submit();"><bean:message key="label.edit.application" bundle="CANDIDATE_RESOURCES"/></a> | 
	<a href="#" onclick="javascript:document.getElementById('methodForm').value='prepareEditCandidacyQualifications';document.getElementById('editCandidacyForm').submit();"><bean:message key="label.edit.application.qualifications" bundle="CANDIDATE_RESOURCES"/></a> |
	<a href="#" onclick="javascript:document.getElementById('methodForm').value='prepareEditCandidacyDocuments';document.getElementById('editCandidacyForm').submit();"><bean:message key="label.edit.documents" bundle="CANDIDATE_RESOURCES"/></a>
</fr:form>
</logic:equal>

<p style="margin-bottom: 0.5em;">
	<b><bean:message key="label.process.id" bundle="CANDIDATE_RESOURCES"/></b>: <bean:write name="individualCandidacyProcess" property="processCode"/>
</p>

<h2 style="margin-top: 1em;"><bean:message key="title.personal.data" bundle="CANDIDATE_RESOURCES"/></h2>

<fr:view name="individualCandidacyProcessBean" 
	schema="PublicCandidacyProcess.candidacyDataBean">
	<fr:layout name="tabular">
		<fr:property name="classes" value="thlight thleft"/>
        <fr:property name="columnClasses" value="width175px,,,"/>
	</fr:layout>
</fr:view>

<h2 style="margin-top: 1em;"><bean:message key="title.over23.qualifications" bundle="CANDIDATE_RESOURCES"/></h2>


<h3 style="margin-bottom: 0.5em;"><bean:message key="label.over23.qualifications.concluded" bundle="CANDIDATE_RESOURCES"/></h3>

<logic:empty name="individualCandidacyProcessBean" property="formationConcludedBeanList">
	<p class="mtop05"><em><bean:message key="label.over23.has.no.qualifications" bundle="CANDIDATE_RESOURCES"/>.</em></p>	
</logic:empty>

<logic:notEmpty name="individualCandidacyProcessBean" property="formationConcludedBeanList">
	<table class="tstyle2 thlight thleft">
	<tr>
		<th><bean:message key="label.over23.qualifications.name" bundle="CANDIDATE_RESOURCES"/></th>
		<th><bean:message key="label.over23.school" bundle="CANDIDATE_RESOURCES"/></th>
		<th><bean:message key="label.over23.execution.year.conclusion" bundle="CANDIDATE_RESOURCES"/></th>
	</tr>
	<logic:iterate id="qualification" name="individualCandidacyProcessBean" property="formationConcludedBeanList" indexId="index">
	<tr>
		<td>
			<fr:view 	name="qualification"
						schema="PublicCandidacyProcessBean.formation.designation">
				<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
			</fr:view>
		</td>
		<td>
			<fr:view name="qualification"
				schema="PublicCandidacyProcessBean.formation.institutionUnitName.view">
				<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
			</fr:view>	
		</td>
		<td>
			<fr:view 	name="qualification"
						schema="PublicCandidacyProcessBean.over23.execution.year.conclusion">
				<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
			</fr:view>
		</td>
	</tr>
	</logic:iterate>
	</table>
</logic:notEmpty>

<h3 style="margin-bottom: 0.5em;"><bean:message key="label.over23.qualifications.non.concluded" bundle="CANDIDATE_RESOURCES"/></h3>

<logic:empty name="individualCandidacyProcessBean" property="formationNonConcludedBeanList">
	<p class="mtop05"><em><bean:message key="label.over23.has.no.qualifications" bundle="CANDIDATE_RESOURCES"/>.</em></p>	
</logic:empty>

<logic:notEmpty name="individualCandidacyProcessBean" property="formationNonConcludedBeanList">
<logic:iterate id="qualification" name="individualCandidacyProcessBean" property="formationNonConcludedBeanList" indexId="index">
	<table class="thlight thleft">
		<tr>
			<th><bean:message key="label.over23.qualifications.name" bundle="CANDIDATE_RESOURCES"/>:</th>
			<td>
				<fr:view 	name="qualification"
							schema="PublicCandidacyProcessBean.formation.designation">
					<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
				</fr:view>
			</td>
		</tr>
		<tr>
			<th><bean:message key="label.over23.school" bundle="CANDIDATE_RESOURCES"/>:</th>
			<td>
				<fr:view name="qualification"
					schema="PublicCandidacyProcessBean.formation.institutionUnitName.view">
					<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
				</fr:view>					
			</td>
		</tr>
	</table>
</logic:iterate>
</logic:notEmpty>

<h3><bean:message key="label.over23.languages" bundle="CANDIDATE_RESOURCES"/></h3>
<p>
	<bean:message key="label.over23.languages.read" bundle="CANDIDATE_RESOURCES"/>: 
	<fr:view 	name="individualCandidacyProcessBean"
				schema="PublicCandidacyProcessBean.over23.languages.read">
		<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
	</fr:view>
</p>

<p>
	<bean:message key="label.over23.languages.write" bundle="CANDIDATE_RESOURCES"/>: 
	<fr:view 	name="individualCandidacyProcessBean"
				schema="PublicCandidacyProcessBean.over23.languages.write">
		<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
	</fr:view>
</p>

<p>
	<bean:message key="label.over23.languages.speak" bundle="CANDIDATE_RESOURCES"/>: 
	<fr:view 	name="individualCandidacyProcessBean"
				schema="PublicCandidacyProcessBean.over23.languages.speak">
		<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
	</fr:view>
</p>


<h3 style="margin-top: 1em;"><bean:message key="title.over23.bachelor.first.cycle.choice" arg0="<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>" bundle="CANDIDATE_RESOURCES"/></h3>
<ol class="mtop05">
	<logic:iterate id="degree" name="individualCandidacyProcessBean" property="selectedDegrees" indexId="index">
		<li>				
			<fr:view name="degree" >
			    <fr:schema type="net.sourceforge.fenixedu.domain.Degree" bundle="APPLICATION_RESOURCES">
					<fr:slot name="nameI18N" key="label.degree.name" />
					<fr:slot name="sigla" key="label.sigla" />
				</fr:schema>			
				<fr:layout name="flow">
					<fr:property name="labelExcluded" value="true"/>
				</fr:layout> 
			</fr:view>
		</li>
	</logic:iterate>
</ol>

<logic:notEmpty name="individualCandidacyProcessBean" property="disabilities">
<h3 style="margin-top: 1em;"><bean:message key="label.over23.disabilities" bundle="CANDIDATE_RESOURCES"/></h3>
<fr:view 	name="individualCandidacyProcessBean"
			schema="PublicCandidacyProcessBean.over23.disabilities">
	<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
</fr:view>
</logic:notEmpty>

<logic:notEmpty name="individualCandidacyProcess" property="associatedPaymentCode">
<p><bean:message key="message.application.sibs.payment.details" bundle="CANDIDATE_RESOURCES"/></p>
<table>
	<tr>
		<td><bean:message key="label.sibs.entity.code" bundle="CANDIDATE_RESOURCES"/></td>
		<td><bean:write name="sibsEntityCode"/></td>
	</tr>
	<tr>
		<td><bean:message key="label.sibs.payment.code" bundle="CANDIDATE_RESOURCES"/></td>
		<td><fr:view name="individualCandidacyProcess" property="associatedPaymentCode.formattedCode"/></td>
	</tr>
	<tr>
		<td><bean:message key="label.sibs.amount" bundle="CANDIDATE_RESOURCES"/></td>
		<td><fr:view name="individualCandidacyProcess" property="associatedPaymentCode.minAmount"/></td>
	</tr>
</table>
</logic:notEmpty>

<h2 style="margin-top: 1em;"><bean:message key="label.documentation" bundle="CANDIDATE_RESOURCES"/></h2> 

<bean:define id="individualCandidacyProcess" name="individualCandidacyProcessBean" property="individualCandidacyProcess"/>

<logic:empty name="individualCandidacyProcess" property="candidacy.documents">
	<p><em><bean:message key="label.no.documents.associated" bundle="CANDIDATE_RESOURCES"/>.</em></p>
</logic:empty>

<logic:notEmpty name="individualCandidacyProcess" property="candidacy.documents">
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

