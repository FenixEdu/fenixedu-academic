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


<%@ page import="net.sourceforge.fenixedu.domain.candidacyProcess.secondCycle.SecondCycleIndividualCandidacyProcessBean" %>

<html:xhtml/>

<bean:define id="mappingPath" name="mappingPath"/>
<bean:define id="fullPath"><%= request.getContextPath() + "/publico" + mappingPath + ".do" %></bean:define>
<bean:define id="applicationInformationLinkDefault" name="application.information.link.default"/>
<bean:define id="applicationInformationLinkEnglish" name="application.information.link.english"/>

<div class="breadcumbs">
	<a href="<%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %>"><%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%></a> &gt;
	<% 
		Locale locale = I18N.getLocale();
		if(!locale.getLanguage().equals(Locale.ENGLISH.getLanguage())) {
	%>
		<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="<%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %>pt/candidatos/"><bean:message key="title.candidate" bundle="CANDIDATE_RESOURCES"/></a> &gt;
	<% } else { %>
		<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="<%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %>en/prospective-students/"><bean:message key="title.candidate" bundle="CANDIDATE_RESOURCES"/></a> &gt;
	<% } %>

	<% 
		if(!locale.getLanguage().equals(Locale.ENGLISH.getLanguage())) {
	%>
		<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href='<%= applicationInformationLinkDefault %>'><bean:write name="application.name"/> </a> &gt;
	<% } else { %>
		<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href='<%= applicationInformationLinkEnglish %>'><bean:write name="application.name"/> </a> &gt;
	<% } %>
	<bean:message key="title.edit.application.qualifications" bundle="CANDIDATE_RESOURCES"/>
</div>

<h1><bean:write name="application.name"/></h1>

<bean:define id="individualCandidacyProcess" name="individualCandidacyProcessBean" property="individualCandidacyProcess"  />
<bean:define id="individualCandidacyProcessBean" name="individualCandidacyProcessBean" type="SecondCycleIndividualCandidacyProcessBean"  />
<bean:define id="individualCandidacyProcessOID" name="individualCandidacyProcessBean" property="individualCandidacyProcess.OID" />

<p><a href='<%= fullPath + "?method=backToViewCandidacy&individualCandidacyProcess=" + individualCandidacyProcessOID %>'>« <bean:message key="label.back" bundle="CANDIDATE_RESOURCES"/></a></p>

<p><span><bean:message key="message.fields.required" bundle="CANDIDATE_RESOURCES"/></span></p>


<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES" property="error">
	<span class="error0"> <bean:write name="message" /> </span>
	<br />
</html:messages>
<fr:hasMessages for="CandidacyProcess.personalDataBean" type="conversion">
	<ul class="nobullet list6">
		<fr:messages>
			<li><span class="error0"><fr:message/></span></li>
		</fr:messages>
	</ul>
</fr:hasMessages>

<fr:form action='<%= mappingPath + ".do?userAction=editCandidacyQualifications" %>' id="secondCycleCandidacyForm">
		<fr:edit id="individualCandidacyProcessBean" name="individualCandidacyProcessBean" visible="false" >
		</fr:edit>
	
		<input type="hidden" id="methodId" name="method" value="editCandidacyQualifications"/>
		<input type="hidden" id="removeIndexId" name="removeIndex" value=""/>
		<input type="hidden" id="skipValidationId" name="skipValidation" value="false"/>
		<input type="hidden" id="individualCandidacyProcessId" name="individualCandidacyProcess" value="<%= individualCandidacyProcessOID %>"/>
		
		<h2 style="margin-top: 1em;"><bean:message key="title.educational.background" bundle="CANDIDATE_RESOURCES"/></h2>

		<% 
			if(!locale.getLanguage().equals(Locale.ENGLISH.getLanguage())) {
		%>
		<p class="mbottom05"><bean:message key="label.ist.number.if.former.ist.student" arg0="<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>" bundle="CANDIDATE_RESOURCES"/>:</p>
		<div class="flowerror">
			<fr:edit id="individualCandidacyProcessBean.formerStudentIstNumber"
				name="individualCandidacyProcessBean"
				schema="PublicCandidacyProcessBean.second.cycle.former.student.ist.number">
				<fr:layout name="flow">
					<fr:property name="labelExcluded" value="true"/>
				</fr:layout>
			</fr:edit>			
		</div>
		<%
			}
		%>
		
		<h3 style="margin-bottom: 0.5em;"><bean:message key="title.bachelor.degree.owned" bundle="CANDIDATE_RESOURCES"/></h3>


		<p style="margin-bottom: 0.5em;"><bean:message key="label.university.attended.previously" bundle="CANDIDATE_RESOURCES"/>: <span class="red">*</span></p>
		<div class="flowerror">
			<fr:edit id="individualCandidacyProcessBean.institutionUnitName"
				name="individualCandidacyProcessBean"
				schema="PublicCandidacyProcessBean.institutionUnitName.manage">
				<fr:layout name="flow">
					<fr:property name="labelExcluded" value="true"/>
				</fr:layout>
			</fr:edit>
		</div>
		
<table class="tstyle5 thlight thleft mtop1">
	<tr>
		<td style="width: 260px;"><bean:message key="label.university.previously.attended.country" bundle="CANDIDATE_RESOURCES"/>: <span class="red">*</span></td>
		<td>
			<div class="flowerror">
				<fr:edit id="individualCandidacyProcessBean.country"
					name="individualCandidacyProcessBean"
					schema="PublicCandidacyProcessBean.institution.country.manage">
				  	<fr:layout name="flow">
						   <fr:property name="labelExcluded" value="true"/>
					</fr:layout>
				</fr:edit>
			</div>
		</td>
	</tr>
	<tr>
		<td><bean:message key="label.bachelor.degree.previously.enrolled" bundle="CANDIDATE_RESOURCES"/>: <span class="red">*</span></td>
		<td>
			<div class="flowerror">
				<fr:edit id="individualCandidacyProcessBean.degreeDesignation"
					name="individualCandidacyProcessBean"
					schema="PublicCandidacyProcessBean.degreeDesignation.manage">
				  	<fr:layout name="flow">
						   <fr:property name="labelExcluded" value="true"/>
					</fr:layout>
				</fr:edit>
			</div>
		</td>
	</tr>
	<tr>
		<td><bean:message key="label.bachelor.degree.conclusion.date" bundle="CANDIDATE_RESOURCES"/>: <span class="red">*</span></td>
		<td>
			<div class="flowerror">
			<fr:edit id="individualCandidacyProcessBean.conclusionDate"
				name="individualCandidacyProcessBean"
				schema="PublicCandidacyProcessBean.precedent.degree.information.conclusionDate">
			  	<fr:layout name="flow">
					   <fr:property name="labelExcluded" value="true"/>
				</fr:layout>
			</fr:edit>
			</div>
		</td>
	</tr>
	<tr>
		<td><bean:message key="label.bachelor.degree.conclusion.grade" bundle="CANDIDATE_RESOURCES"/>: <span class="red">*</span></td>
		<td>
			<div class="flowerror">
			<fr:edit id="individualCandidacyProcessBean.conclusionGrade"
				name="individualCandidacyProcessBean"
				schema="PublicCandidacyProcessBean.precedent.degree.information.conclusionGrade">
			  	<fr:layout name="flow">
					   <fr:property name="labelExcluded" value="true"/>
				</fr:layout>
			</fr:edit>
			</div>
		</td>
	</tr>
</table>
		
		
			


		<% 
			if(!locale.getLanguage().equals(Locale.ENGLISH.getLanguage())) {
		%>
		
		<h3><bean:message key="title.other.academic.titles" bundle="CANDIDATE_RESOURCES"/></h3>
		<logic:iterate id="academicTitle" name="individualCandidacyProcessBean" property="formationConcludedBeanList" indexId="index">
			<bean:define id="academicTitleId" name="academicTitle" property="id"/>
			<bean:define id="designationId"><%= "individualCandidacyProcessBean.habilitation.concluded.designation:" + academicTitleId %></bean:define>
			<bean:define id="institutionNameId"><%= "individualCandidacyProcessBean.habilitation.concluded.institutionName:" + academicTitleId %></bean:define>
			<bean:define id="beginYearId"><%= "individualCandidacyProcessBean.habilitation.concluded.begin.year:" + academicTitleId %></bean:define>
			<bean:define id="endYearId"><%= "individualCandidacyProcessBean.habilitation.concluded.end.year:" + academicTitleId %></bean:define>
			<bean:define id="conclusionGradeId"><%= "individualCandidacyProcessBean.habilitation.concluded.conclusion.grade:" + academicTitleId %></bean:define>
			
			<table class="tstyle5 thlight thleft mbottom0">
				<tr>
					<th style="width: 260px;"><bean:message key="label.other.academic.titles.program.name" bundle="CANDIDATE_RESOURCES"/>: <span class="red">*</span></th>
					<td>
						<fr:edit 	id='<%= designationId %>' 
									name="academicTitle"
									schema="PublicCandidacyProcessBean.formation.designation">
							<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
						</fr:edit>
					</td>
					<td rowspan="4">
						<p><a onclick='<%= "document.getElementById(\"skipValidationId\").value=\"true\"; document.getElementById(\"removeIndexId\").value=" + index + "; document.getElementById(\"methodId\").value=\"removeConcludedHabilitationsEntry\"; document.getElementById(\"secondCycleCandidacyForm\").submit();" %>' href="#" ><bean:message key="label.remove" bundle="CANDIDATE_RESOURCES"/></a></p>
					</td>
					<td class="tdclear">
						<span class="error0"><fr:message for="<%= designationId %>"/></span>
					</td>
				</tr>
				<tr>
					<th><bean:message key="label.other.academic.titles.institution" bundle="CANDIDATE_RESOURCES"/>: <span class="red">*</span></th>
					<td>
						<fr:edit 	id='<%= institutionNameId %>' 
							name="academicTitle"
							schema="PublicCandidacyProcessBean.formation.institutionUnitName">
							<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
						</fr:edit>					
					</td>
					<td class="tdclear">
						<span class="error0"><fr:message for="<%= institutionNameId %>"/></span>
					</td>					
				</tr>
				<tr>
					<th><bean:message key="label.other.academic.titles.conclusion.date" bundle="CANDIDATE_RESOURCES"/>: <span class="red">*</span></th>
					<td>
						<fr:edit 	id='<%= endYearId %>'
									name="academicTitle"
									schema="PublicCandidacyProcessBean.formation.conclusion.date">
							<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
						</fr:edit>
					</td>
					<td class="tdclear">
						<span class="error0"><fr:message for="<%= endYearId %>"/></span>					
					</td>					
				</tr>
				<tr>
					<th><bean:message key="label.other.academic.titles.conclusion.grade" bundle="CANDIDATE_RESOURCES"/>: <span class="red">*</span></th>
					<td>
						<fr:edit 	id='<%= conclusionGradeId %>'
									name="academicTitle"
									schema="PublicCandidacyProcessBean.formation.conclusion.grade">
							<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
						</fr:edit>
					</td>
					<td class="tdclear">
						<span class="error0"><fr:message for="<%= conclusionGradeId %>"/></span>					
					</td>					
				</tr>
			</table>
		</logic:iterate>
		<p class="mtop05 mbottom2"><a onclick="document.getElementById('skipValidationId').value='true'; document.getElementById('methodId').value='addConcludedHabilitationsEntry'; document.getElementById('secondCycleCandidacyForm').submit();" href="#">+ <bean:message key="label.add" bundle="CANDIDATE_RESOURCES"/></a></p>

		<% 
			}
		%>
		
		<h2 style="margin-top: 1em;"><bean:message key="title.master.second.cycle.course.choice" arg0="<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>" bundle="CANDIDATE_RESOURCES"/></h2>
				
				
		<div class="flowerror mtop1">
			<fr:edit id="individualCandidacyProcessBean.selectedDegree"
				name="individualCandidacyProcessBean"
				schema="PublicCandidacyProcessBean.second.cycle.selectedDegree.manage">
			</fr:edit>
			<a onclick="document.getElementById('skipValidationId').value='true'; document.getElementById('methodId').value='addSelectedDegreesEntry'; document.getElementById('secondCycleCandidacyForm').submit();" href="#"><bean:message key="link.over23.choose.degree" bundle="CANDIDATE_RESOURCES" /></a>
		</div>
		
		<logic:notEmpty name="individualCandidacyProcessBean" property="selectedDegreeList">
			<bean:define id="selectedDegreesSize" value="<%= ((Integer) individualCandidacyProcessBean.getSelectedDegreeList().size()).toString() %>" />
			<ol class="mtop05">
			<logic:iterate id="degree" name="individualCandidacyProcessBean" property="selectedDegreeList" indexId="index">
				<bean:define id="degreeId" name="degree" property="externalId"/>
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
					<logic:greaterThan name="selectedDegreesSize" value="1">
						<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a onclick="<%= "document.getElementById('skipValidationId').value='true'; document.getElementById('removeIndexId').value=" + degreeId + "; document.getElementById('methodId').value='removeSelectedDegreesEntry'; document.getElementById('secondCycleCandidacyForm').submit();" %>" href="#" ><bean:message key="label.over23.remove" bundle="CANDIDATE_RESOURCES"/> </a>
					</logic:greaterThan>
				</li>
			</logic:iterate>
			</ol>
		</logic:notEmpty>
		
		<logic:empty name="individualCandidacyProcessBean" property="selectedDegreeList">
			<p><em><bean:message key="message.second.cycle.selected.degrees.empty" bundle="CANDIDATE_RESOURCES" /></em></p>
		</logic:empty>

		<p style="margin-bottom: 0.5em;"><bean:message key="label.observations" bundle="CANDIDATE_RESOURCES"/>:</p>
		<fr:edit id="individualCandidacyProcessBean.observations"
			name="individualCandidacyProcessBean"
			schema="PublicCandidacyProcessBean.observations">
 		  <fr:layout name="flow">
		    <fr:property name="labelExcluded" value="true"/>
		  </fr:layout>
		</fr:edit>
		
		<p class="mtop15">
			<html:submit onclick="document.getElementById(\"skipValidationId\").value=\"false\"; document.getElementById('methodId').value='editCandidacyQualifications'; this.form.submit();"><bean:message key="button.submit" bundle="APPLICATION_RESOURCES" /></html:submit>
		</p>
</fr:form>

