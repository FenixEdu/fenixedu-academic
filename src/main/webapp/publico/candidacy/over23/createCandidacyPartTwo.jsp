<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@page import="org.fenixedu.academic.domain.candidacyProcess.CandidacyProcess"%>
<%@page import="org.fenixedu.academic.domain.Installation"%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>


<html:xhtml/>

<bean:define id="mappingPath" name="mappingPath"/>
<bean:define id="fullPath"><%= request.getContextPath() + "/publico" + mappingPath + ".do" %></bean:define>

<div class="breadcumbs">
	<a href="<%= org.fenixedu.academic.domain.Installation.getInstance().getInstituitionURL() %>"><%=org.fenixedu.academic.domain.organizationalStructure.Unit.getInstitutionAcronym()%></a> &gt;
	<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="<%= request.getContextPath() + "/candidaturas/introducao" %>"><bean:message key="title.candidate" bundle="CANDIDATE_RESOURCES"/></a> &gt;
	<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="<%= request.getContextPath() + "/candidaturas/licenciaturas" %>"><bean:message key="title.degrees" bundle="CANDIDATE_RESOURCES"/></a> &gt;
	<a href='<%= fullPath + "?method=beginCandidacyProcessIntro" %>'><bean:write name="application.name"/> </a> &gt;
	<bean:message key="title.submit.application" bundle="CANDIDATE_RESOURCES"/>
</div>

<h1><bean:write name="application.name"/></h1>

<p class="steps">
	<span><bean:message key="label.step.one.personal.details" bundle="CANDIDATE_RESOURCES"/></span> &gt; 
	<span class="actual"><bean:message key="label.step.two.habilitations.document.files" bundle="CANDIDATE_RESOURCES"/></span>
</p>

<p class="mtop15"><bean:message key="message.fields.required" bundle="CANDIDATE_RESOURCES"/></p>

<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES" property="captcha.error">
	<span class="error0"><bean:write name="message"/></span>
</html:messages>

<html:messages id="message" message="true" bundle="CANDIDATE_RESOURCES" property="error">
	<p><span class="error0"><bean:write name="message"/></span></p>
</html:messages>

<fr:hasMessages for="CandidacyProcess.personalDataBean" type="conversion">
	<ul class="nobullet list6">
		<fr:messages>
			<li><span class="error0"><fr:message/></span></li>
		</fr:messages>
	</ul>
</fr:hasMessages>

<fr:form id="over23CandidacyForm" action='<%= mappingPath + ".do?userAction=createCandidacy" %>' encoding="multipart/form-data">

		<fr:edit id="individualCandidacyProcessBean" name="individualCandidacyProcessBean" visible="false" />
		
		<input type="hidden" id="methodId" name="method" value="submitCandidacy"/>
		<input type="hidden" id="removeIndexId" name="removeIndex" value=""/>
		<input type="hidden" id="skipValidationId" name="skipValidation" value="false"/>
		
		
		
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
						<div class="flowerror_public_hide">
						<fr:edit 	id='<%= designationId %>' 
									name="qualification"
									schema="PublicCandidacyProcessBean.formation.designation">
							<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
						</fr:edit>
						</div>
					</td>
					<td rowspan="3">
						<p><a onclick='<%= "document.getElementById(\"skipValidationId\").value=\"true\"; document.getElementById(\"removeIndexId\").value=" + index + "; document.getElementById(\"methodId\").value=\"removeConcludedHabilitationsEntry\"; document.getElementById(\"over23CandidacyForm\").submit();" %>' href="#" ><bean:message key="label.remove" bundle="CANDIDATE_RESOURCES" /> </a></p>
					</td>
					<td class="tdclear">
						<span class="error0"><fr:message for="<%= designationId %>"/></span>
					</td>
				</tr>
				<tr>
					<th><bean:message key="label.over23.school" bundle="CANDIDATE_RESOURCES"/>: <span class="red">*</span></th>
					<td>
						<div class="flowerror_public_hide">
						<fr:edit 	id='<%= institutionNameId %>' 
							name="qualification"
							schema="PublicCandidacyProcessBean.formation.institutionUnitName">
							<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
						</fr:edit>
						</div>
					</td>
					<td class="tdclear">
						<span class="error0"><fr:message for="<%= institutionNameId %>"/></span>
					</td>					
				</tr>
				<tr>
					<th><bean:message key="label.over23.execution.year.conclusion" bundle="CANDIDATE_RESOURCES"/>: <span class="red">*</span></th>
					<td>
						<div class="flowerror_public_hide">
						<fr:edit 	id='<%= endYearId %>'
									name="qualification"
									schema="PublicCandidacyProcessBean.over23.execution.year.conclusion">
							<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
						</fr:edit>
						</div>
					</td>
					<td class="tdclear">
		                <span class="error0"><fr:message for="<%= endYearId %>"/></span>
					</td>					
				</tr>
			</table>
		</logic:iterate>
		<p class="mtop05 mbottom2"><a onclick="document.getElementById('skipValidationId').value='true'; document.getElementById('methodId').value='addConcludedHabilitationsEntry'; document.getElementById('over23CandidacyForm').submit();" href="#"><bean:message key="label.add" bundle="CANDIDATE_RESOURCES" /></a></p>
		
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
						<div class="flowerror_public_hide">
						<fr:edit 	id='<%= designationId %>' 
									name="qualification"
									schema="PublicCandidacyProcessBean.formation.designation">
							<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
						</fr:edit>
						</div>
					</td>
					<td rowspan="3">
						<p><a onclick='<%= "document.getElementById(\"skipValidationId\").value=\"true\"; document.getElementById(\"removeIndexId\").value=" + index + "; document.getElementById(\"methodId\").value=\"removeNonConcludedHabilitationsEntry\"; document.getElementById(\"over23CandidacyForm\").submit();" %>' href="#" ><bean:message key="label.remove" bundle="CANDIDATE_RESOURCES" /> </a></p>
					</td>
					<td class="tdclear">
		                <span class="error0"><fr:message for="<%= designationId %>"/></span>
					</td>
				</tr>
				<tr>
					<th><bean:message key="label.over23.school" bundle="CANDIDATE_RESOURCES"/>: <span class="red">*</span></th>
					<td>
						<div class="flowerror_public_hide">
						<fr:edit 	id='<%= institutionNameId %>' 
							name="qualification"
							schema="PublicCandidacyProcessBean.formation.institutionUnitName">
							<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
						</fr:edit>
						</div>
					</td>
					<td class="tdclear">
		                <span class="error0"><fr:message for="<%= institutionNameId %>"/></span>
					</td>
				</tr>
			</table>
		</logic:iterate>
		<p class="mtop05 mbottom2"><a onclick="document.getElementById('skipValidationId').value='true'; document.getElementById('methodId').value='addNonConcludedHabilitationsEntry'; document.getElementById('over23CandidacyForm').submit();" href="#"><bean:message key="label.add" bundle="CANDIDATE_RESOURCES" /></a></p>




		<h3><bean:message key="label.over23.languages" bundle="CANDIDATE_RESOURCES"/></h3>
		
		<p class="mbottom05"><bean:message key="label.over23.languages.read" bundle="CANDIDATE_RESOURCES"/>&nbsp;<bean:message key="label.delimited.by.commas" bundle="CANDIDATE_RESOURCES"/>:</p>
		<div class="flowerror_public_hide">
		<fr:edit 	id='PublicCandidacyProcessBean.over23.languages.read' 
					name="individualCandidacyProcessBean"
					schema="PublicCandidacyProcessBean.over23.languages.read">
			<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
		</fr:edit>
		</div>	
		
		<p class="mbottom05"><bean:message key="label.over23.languages.write" bundle="CANDIDATE_RESOURCES"/>&nbsp;<bean:message key="label.delimited.by.commas" bundle="CANDIDATE_RESOURCES"/>:</p>
		<div class="flowerror_public_hide">
		<fr:edit 	id='PublicCandidacyProcessBean.over23.languages.write' 
					name="individualCandidacyProcessBean"
					schema="PublicCandidacyProcessBean.over23.languages.write">
			<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
		</fr:edit>
		</div>	

		<p class="mbottom05"><bean:message key="label.over23.languages.speak" bundle="CANDIDATE_RESOURCES"/>&nbsp;<bean:message key="label.delimited.by.commas" bundle="CANDIDATE_RESOURCES"/>:</p>
		<div class="flowerror_public_hide">
		<fr:edit 	id='PublicCandidacyProcessBean.over23.languages.speak' 
					name="individualCandidacyProcessBean"
					schema="PublicCandidacyProcessBean.over23.languages.speak">
			<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
		</fr:edit>
		</div>	
		
		
		<h2 style="margin-top: 1em;"><bean:message key="title.over23.bachelor.first.cycle.choice" arg0="<%=org.fenixedu.academic.domain.organizationalStructure.Unit.getInstitutionAcronym()%>" bundle="CANDIDATE_RESOURCES"/></h2>
		
		<p class="mbottom05"><bean:message key="message.over23.choose.degree.instruction" bundle="CANDIDATE_RESOURCES" /></p>
		<fr:edit 	id='PublicCandidacyProcessBean.degrees'
					name="individualCandidacyProcessBean"
					schema="PublicCandidacyProcessBean.over23.degrees">
			<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
		</fr:edit> <span class="red">*</span>
		<p class="mtop05"><a onclick="document.getElementById('skipValidationId').value='true'; document.getElementById('methodId').value='addSelectedDegreesEntry'; document.getElementById('over23CandidacyForm').submit();" href="#"><bean:message key="link.over23.choose.degree" bundle="CANDIDATE_RESOURCES" /></a></p>
				
		<logic:notEmpty name="individualCandidacyProcessBean" property="selectedDegrees">
			<ol class="mtop05">
			<logic:iterate id="degree" name="individualCandidacyProcessBean" property="selectedDegrees" indexId="index">
				<li>
					<fr:view name="degree" >
					    <fr:schema type="org.fenixedu.academic.domain.Degree" bundle="APPLICATION_RESOURCES">
							<fr:slot name="nameI18N" key="label.degree.name" />
							<fr:slot name="sigla" key="label.sigla" />
						</fr:schema>					
						<fr:layout name="flow">
							<fr:property name="labelExcluded" value="true"/>
						</fr:layout> 
					</fr:view> | 
					<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a onclick="<%= "document.getElementById('skipValidationId').value='true'; document.getElementById('removeIndexId').value=" + index + "; document.getElementById('methodId').value='removeSelectedDegreesEntry'; document.getElementById('over23CandidacyForm').submit();" %>" href="#" ><bean:message key="label.over23.remove" bundle="CANDIDATE_RESOURCES"/> </a>
				</li>
			</logic:iterate>
			</ol>
		</logic:notEmpty>
	
	

	
		<h2 style="margin-top: 1em;"><bean:message key="label.over23.disabilities" bundle="CANDIDATE_RESOURCES"/></h2>
		<p class="mbottom05"><bean:message key="message.over23.disabilities.detail" bundle="CANDIDATE_RESOURCES"/>:</p>
		<div class="flowerror_public_hide">
		<fr:edit 	id="individualCandidacyProcessBean.disabilities"
					name="individualCandidacyProcessBean"
					schema="PublicCandidacyProcessBean.over23.disabilities">
			<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
		</fr:edit>
		</div>
		
		
		<h2 style="margin-top: 1em;"><bean:message key="title.over23.honor.declaration" bundle="CANDIDATE_RESOURCES"/></h2>
		<p>
		<%  final CandidacyProcess process = (CandidacyProcess) request.getAttribute("parentProcess");
			if (process == null || process.getHonorAgreement() == null) {
		%>
				<bean:message key="message.over23.honor.declaration.detail" bundle="CANDIDATE_RESOURCES"/>
		<%	    
			} else {
		%>
				<%= process.getHonorAgreement().getContent() %>
		<%	    
			}
		%>
		</p>
		<p>
			<fr:edit 	id="individualCandidacyProcessBean.honor.declaration"
						name="individualCandidacyProcessBean"
						schema="PublicCandidacyProcessBean.over23.honor.agreement">
				<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
			</fr:edit>
			<bean:message key="label.over23.honor.declaration" bundle="CANDIDATE_RESOURCES"/> <span class="red">*</span>
		</p>

		<p><em><bean:message key="message.ist.conditions.note" arg0="<%=org.fenixedu.academic.domain.organizationalStructure.Unit.getInstitutionAcronym()%>" bundle="CANDIDATE_RESOURCES"/></em></p>		

		<div class="mtop15"><bean:message key="message.nape.contacts" bundle="CANDIDATE_RESOURCES"/></div>
		
		<p class="mtop15">		
			<html:submit onclick="document.getElementById('skipValidationId').value='false'; document.getElementById('methodId').value='submitCandidacy'; this.form.submit();"><bean:message key="button.submit" bundle="APPLICATION_RESOURCES" /> <bean:message key="label.application" bundle="CANDIDATE_RESOURCES"/></html:submit>
			<html:submit onclick="document.getElementById('skipValidationId').value='false'; document.getElementById('methodId').value='backCandidacyCreation'; this.form.submit();"><bean:message key="label.back" bundle="APPLICATION_RESOURCES" /></html:submit>
		</p>
</fr:form>


