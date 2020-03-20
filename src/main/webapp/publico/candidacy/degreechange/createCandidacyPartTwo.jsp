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
<%@page import="org.fenixedu.academic.domain.Installation"%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ page import="org.fenixedu.commons.i18n.I18N"%>
<%@ page import="java.util.Locale"%>
<%@page import="org.fenixedu.academic.domain.candidacyProcess.IndividualCandidacyProcessWithPrecedentDegreeInformationBean.PrecedentDegreeType" %>

<html:xhtml/>

<bean:define id="mappingPath" name="mappingPath"/>
<bean:define id="fullPath"><%= request.getContextPath() + "/publico" + mappingPath + ".do" %></bean:define>
<bean:define id="applicationInformationLinkDefault" name="application.information.link.default"/>
<bean:define id="applicationInformationLinkEnglish" name="application.information.link.english"/>

<div class="breadcumbs">
	<a href="<%= org.fenixedu.academic.domain.Installation.getInstance().getInstituitionURL() %>"><%=org.fenixedu.academic.domain.organizationalStructure.Unit.getInstitutionAcronym()%></a> &gt;
	<% 
		Locale locale = I18N.getLocale();
		if(!locale.getLanguage().equals(Locale.ENGLISH.getLanguage())) {
	%>
		<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="<%= org.fenixedu.academic.domain.Installation.getInstance().getInstituitionURL() %>pt/candidatos/"><bean:message key="title.candidate" bundle="CANDIDATE_RESOURCES"/></a> &gt;
	<% } else { %>
		<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="<%= org.fenixedu.academic.domain.Installation.getInstance().getInstituitionURL() %>en/prospective-students/"><bean:message key="title.candidate" bundle="CANDIDATE_RESOURCES"/></a> &gt;
	<% } %>

	<% 
		if(!locale.getLanguage().equals(Locale.ENGLISH.getLanguage())) {
	%>
		<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="<%= org.fenixedu.academic.domain.Installation.getInstance().getInstituitionURL() %>pt/candidatos/candidaturas/licenciaturas/"><bean:message key="title.degrees" bundle="CANDIDATE_RESOURCES"/></a> &gt;
	<% } else { %>
			<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="<%= org.fenixedu.academic.domain.Installation.getInstance().getInstituitionURL() %>en/prospective-students/admissions/bachelor/"><bean:message key="title.degrees" bundle="CANDIDATE_RESOURCES"/></a> &gt;
	<% } %>

	<% 
		if(!locale.getLanguage().equals(Locale.ENGLISH.getLanguage())) {
	%>
		<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href='<%= applicationInformationLinkDefault %>'><bean:write name="application.name"/> </a> &gt;
	<% } else { %>
		<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href='<%= applicationInformationLinkEnglish %>'><bean:write name="application.name"/> </a> &gt;
	<% } %>
	<bean:message key="title.submit.application" bundle="CANDIDATE_RESOURCES"/>
</div>

<h1><bean:write name="application.name"/></h1>

<p class="steps">
	<span><bean:message key="label.step.one.personal.details" bundle="CANDIDATE_RESOURCES"/></span> &gt; 
	<span class="actual"><bean:message key="label.step.two.habilitations.document.files" bundle="CANDIDATE_RESOURCES"/></span>
</p>

<p class="mtop15"><span><bean:message key="message.fields.required" bundle="CANDIDATE_RESOURCES"/></span></p>

<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES" property="captcha.error">
	<p><span class="error0"><bean:write name="message"/></span></p>
</html:messages>

<html:messages id="message" message="true" bundle="CANDIDATE_RESOURCES" property="error">
	<p><span class="error0"><bean:write name="message"/></span></p>
</html:messages>

<html:messages id="message" message="true" bundle="CANDIDATE_RESOURCES" property="candidacyMessages">
	<p><span class="error0"><bean:write name="message"/></span></p>
</html:messages>

<fr:hasMessages for="CandidacyProcess.personalDataBean" type="conversion">
	<ul class="nobullet list6">
		<fr:messages>
			<li><span class="error0"><fr:message/></span></li>
		</fr:messages>
	</ul>
</fr:hasMessages>

<bean:define id="processName" name="processName" />

<fr:form id="candidacyForm" action='<%= mappingPath + ".do?userAction=createCandidacy" %>' encoding="multipart/form-data">
		<fr:edit id="individualCandidacyProcessBean" name="individualCandidacyProcessBean" visible="false" />
		
		<input type="hidden" id="methodId" name="method" value="submitCandidacy"/>
		<input type="hidden" id="removeIndexId" name="removeIndex" value=""/>
		<input type="hidden" id="skipValidationId" name="skipValidation" value="false"/>

		<logic:empty name="individualCandidacyProcessBean" property="precedentDegreeType">
			<p><bean:message key="message.candidacy.last.registration" bundle="CANDIDATE_RESOURCES"/></p>
			<ul>
				<li><html:radio onclick="this.form.method.value='fillInternalPrecedentInformation'; this.form.skipValidation.value='true'; this.form.submit();" name="individualCandidacyProcessBean" property="precedentDegreeType" value="<%=  PrecedentDegreeType.INSTITUTION_DEGREE.getName() %>" ><bean:message key="label.last.registration.in.ist" arg0="<%=org.fenixedu.academic.domain.organizationalStructure.Unit.getInstitutionName().getContent()%>" bundle="CANDIDATE_RESOURCES"/></html:radio></li>
				<li><html:radio onclick="this.form.method.value='fillExternalPrecedentInformation'; this.form.skipValidation.value='true'; this.form.submit();" name="individualCandidacyProcessBean" property="precedentDegreeType" value="<%=  PrecedentDegreeType.EXTERNAL_DEGREE.getName() %>" ><bean:message key="label.last.registration.external.institution" bundle="CANDIDATE_RESOURCES"/></html:radio></li>
			</ul>
		</logic:empty>

		<logic:notEmpty name="individualCandidacyProcessBean" property="precedentDegreeType">
		
			<h2 class="mtop1"><bean:message key="title.educational.background" bundle="CANDIDATE_RESOURCES"/></h2>
	
			<logic:equal name="individualCandidacyProcessBean" property="externalPrecedentDegreeType" value="true">
				<p style="margin-bottom: 0.5em;"><bean:message key="label.university.enrolled" bundle="CANDIDATE_RESOURCES"/>: <span class="red">*</span></p>
				<div class="flowerror_public_hide">
				<fr:edit id="individualCandidacyProcessBean.institutionUnitName"
					name="individualCandidacyProcessBean"
					schema="PublicCandidacyProcessBean.institutionUnitName.manage">
					<fr:layout name="flow">
						<fr:property name="labelExcluded" value="true"/>
					</fr:layout>
				</fr:edit>
				</div>
				
				<p style="margin-bottom: 0.5em;"><bean:message key="label.degree.enrolled" bundle="CANDIDATE_RESOURCES"/>: <span class="red">*</span></p>
				<div class="flowerror_public_hide">
				<fr:edit id="individualCandidacyProcessBean.degreeDesignation"
					name="individualCandidacyProcessBean"
					schema="PublicCandidacyProcessBean.degreeDesignation.manage">
				  	<fr:layout name="flow">
						   <fr:property name="labelExcluded" value="true"/>
					</fr:layout>
				</fr:edit>
				</div>
				
				<p style="margin-bottom: 0.5em;"><bean:message key="label.numberOfPreviousYearEnrolmentsInPrecedentDegree" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <span class="red">*</span></p>
				<div class="flowerror_public_hide">
				<fr:edit id="individualCandidacyProcessBean.numberOfPreviousYearEnrolmentsInPrecedentDegree"
					name="individualCandidacyProcessBean">
					<fr:schema type="org.fenixedu.academic.domain.candidacyProcess.IndividualCandidacyProcessBean" bundle="ACADEMIC_OFFICE_RESOURCES">
						<fr:slot name="numberOfPreviousYearEnrolmentsInPrecedentDegree" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
	    					<fr:property name="size" value="4"/>
							<fr:property name="maxLength" value="2"/>
						</fr:slot>
					</fr:schema>
				  	<fr:layout name="flow">
						   <fr:property name="labelExcluded" value="true"/>
					</fr:layout>
				</fr:edit>
				</div>
			</logic:equal>				

			<logic:equal name="individualCandidacyProcessBean" property="externalPrecedentDegreeType" value="false">
				<fr:view name="individualCandidacyProcessBean" property="precedentStudentCurricularPlan" schema="IndividualCandidacyPrecendentDegreeInformation.StudentCurricularPlan.view">
					<fr:layout name="tabular">
					</fr:layout>
				</fr:view>
			</logic:equal>

						 
			<% 
				if(!locale.getLanguage().equals(Locale.ENGLISH.getLanguage())) {
			%>
			
			<p class="mtop15 mbottom05"><strong><bean:message key="title.other.academic.titles" bundle="CANDIDATE_RESOURCES"/></strong></p>
			<logic:iterate id="academicTitle" name="individualCandidacyProcessBean" property="formationConcludedBeanList" indexId="index">
				<bean:define id="academicTitleId" name="academicTitle" property="id"/>
				<bean:define id="designationId"><%= "individualCandidacyProcessBean.habilitation.concluded.designation:" + academicTitleId %></bean:define>
				<bean:define id="institutionNameId"><%= "individualCandidacyProcessBean.habilitation.concluded.institutionName:" + academicTitleId %></bean:define>
				<bean:define id="beginYearId"><%= "individualCandidacyProcessBean.habilitation.concluded.begin.year:" + academicTitleId %></bean:define>
				<bean:define id="endYearId"><%= "individualCandidacyProcessBean.habilitation.concluded.end.year:" + academicTitleId %></bean:define>
				<bean:define id="conclusionGradeId"><%= "individualCandidacyProcessBean.habilitation.concluded.conclusion.grade:" + academicTitleId %></bean:define>
				
				<table class="tstyle5 thlight thleft mtop0 mbottom0">
					<tr>
						<th><bean:message key="label.other.academic.titles.program.name" bundle="CANDIDATE_RESOURCES"/>: <span class="red">*</span></th>
						<td>
							<div class="flowerror_public_hide">
								<fr:edit 	id='<%= designationId %>' 
											name="academicTitle"
											schema="PublicCandidacyProcessBean.formation.designation">
									<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
								</fr:edit>
							</div>
						</td>
						<td rowspan="4">
							<p><a onclick='<%= "document.getElementById(\"skipValidationId\").value=\"true\"; document.getElementById(\"removeIndexId\").value=" + index + "; document.getElementById(\"methodId\").value=\"removeConcludedHabilitationsEntry\"; document.getElementById(\"candidacyForm\").submit();" %>' href="#" ><bean:message key="label.remove" bundle="CANDIDATE_RESOURCES"/></a></p>
						</td>
						<td class="tdclear">
							<span class="error0"><fr:message for="<%= designationId %>"/></span>
						</td>
					</tr>
					<tr>
						<th><bean:message key="label.other.academic.titles.institution" bundle="CANDIDATE_RESOURCES"/>: <span class="red">*</span></th>
						<td>
							<div class="flowerror_public_hide">
								<fr:edit 	id='<%= institutionNameId %>' 
									name="academicTitle"
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
						<th><bean:message key="label.other.academic.titles.conclusion.date" bundle="CANDIDATE_RESOURCES"/>: <span class="red">*</span></th>
						<td>
							<div class="flowerror_public_hide">
								<fr:edit 	id='<%= endYearId %>'
											name="academicTitle"
											schema="PublicCandidacyProcessBean.formation.conclusion.date">
									<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
								</fr:edit>
								dd/mm/aaaa
							</div>
						</td>
						<td class="tdclear">
							<span class="error0"><fr:message for="<%= endYearId %>"/></span>					
						</td>					
					</tr>
					<tr>
						<th><bean:message key="label.other.academic.titles.conclusion.grade" bundle="CANDIDATE_RESOURCES"/>: <span class="red">*</span></th>
						<td>
							<div class="flowerror_public_hide">
								<fr:edit 	id='<%= conclusionGradeId %>'
											name="academicTitle"
											schema="PublicCandidacyProcessBean.formation.conclusion.grade">
									<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
								</fr:edit>
							</div>
						</td>
						<td class="tdclear">
							<span class="error0"><fr:message for="<%= conclusionGradeId %>"/></span>					
						</td>					
					</tr>
				</table>
			</logic:iterate>
			<p class="mtop05 mbottom2"><a onclick="document.getElementById('skipValidationId').value='true'; document.getElementById('methodId').value='addConcludedHabilitationsEntry'; document.getElementById('candidacyForm').submit();" href="#">+ <bean:message key="label.add" bundle="CANDIDATE_RESOURCES"/></a></p>
	
			<% 
				}		
			%>
	
			<h2 style="margin-top: 1em;"><bean:message key="title.master.degree.change.course.choice" arg0="<%=org.fenixedu.academic.domain.organizationalStructure.Unit.getInstitutionAcronym()%>" bundle="CANDIDATE_RESOURCES"/></h2>
				<div class="flowerror mtop1">
				<fr:edit id="individualCandidacyProcessBean.selectedDegree"
					name="individualCandidacyProcessBean"
					schema="PublicCandidacyProcessBean.degree.change.selectedDegree.manage">
					<fr:layout name="flow">
					  <fr:property name="labelExcluded" value="true"/>
					</fr:layout>
				</fr:edit>
				<span class="red">*</span>
			</div>
	
			<p style="margin-bottom: 0.5em;"><bean:message key="label.observations" bundle="CANDIDATE_RESOURCES"/>:</p>
			<div class="flowerror">
			<fr:edit id="individualCandidacyProcessBean.observations"
				name="individualCandidacyProcessBean"
				schema="PublicCandidacyProcessBean.observations">
	 		  <fr:layout name="flow">
			    <fr:property name="labelExcluded" value="true"/>
			  </fr:layout>
			</fr:edit>
			</div>

			<h3><bean:message key="message.is.student.of.utl.network" bundle="CANDIDATE_RESOURCES"/> <span class="red">*</span></h3>
			<div>
			<fr:edit id="individualCandidacyProcessBean.utlStudent" name="individualCandidacyProcessBean"
				schema="PublicCandidacyProcessBean.utl.student">
				<fr:layout name="flow">
					<fr:property name="labelExcluded" value="true"/>
				</fr:layout>
			</fr:edit>
			</div>

			<h2 style="margin-top: 1em;"><bean:message key="title.degree.change.honor.declaration" bundle="CANDIDATE_RESOURCES"/></h2>
			<p><bean:message key="message.degree.change.honor.declaration.detail" bundle="CANDIDATE_RESOURCES"/></p>
			<p>
				<fr:edit 	id="individualCandidacyProcessBean.honor.declaration"
							name="individualCandidacyProcessBean"
							schema="PublicCandidacyProcessBean.honor.agreement">
					<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
				</fr:edit>
				<bean:message key="label.degree.change.honor.declaration" bundle="CANDIDATE_RESOURCES"/> <span class="red">*</span>
			</p>
	
			<p><em><bean:message key="message.ist.conditions.note" arg0="<%=org.fenixedu.academic.domain.organizationalStructure.Unit.getInstitutionAcronym()%>" bundle="CANDIDATE_RESOURCES"/></em></p>
	
			<div class="mtop15"><bean:message key="message.nape.contacts" bundle="CANDIDATE_RESOURCES"/></div>
	
			<p class="mtop2">
				<html:submit onclick="document.getElementById('skipValidationId').value='false'; document.getElementById('methodId').value='submitCandidacy'; this.form.submit();"><bean:message key="button.submit" bundle="APPLICATION_RESOURCES" /> <bean:message key="label.application" bundle="CANDIDATE_RESOURCES"/></html:submit>
				<html:submit onclick="document.getElementById('skipValidationId').value='false'; document.getElementById('methodId').value='backCandidacyCreation'; this.form.submit();"><bean:message key="label.back" bundle="APPLICATION_RESOURCES" /></html:submit>
			</p>
		</logic:notEmpty>		
</fr:form>

