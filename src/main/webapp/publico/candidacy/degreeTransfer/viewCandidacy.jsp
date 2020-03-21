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
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ page import="org.fenixedu.commons.i18n.I18N"%>
<%@ page import="java.util.Locale"%>
<%@ page import="org.fenixedu.bennu.io.servlet.FileDownloadServlet"%>
<%@ page import="org.fenixedu.bennu.io.domain.GenericFile"%>

<html:xhtml/>

<script src="<%= request.getContextPath() + "/javaScript/jquery/jquery.js" %>" type="text/javascript"></script>

<script type="text/javascript">
	jQuery.noConflict();
	
	jQuery(document).ready(function() {
		jQuery('#photo').load(function() {
	    var maxWidth = 150; // Max width for the image
	    var maxHeight = 150;    // Max height for the image
	    var ratio = 0;  // Used for aspect ratio
	    var width = jQuery(this).width();    // Current image width
	    var height = jQuery(this).height();  // Current image height
		
	    if(width >= height) {
		    // Check if the current width is larger than the max
		    if(width >= maxWidth){
		        ratio = maxWidth / width;   // get ratio for scaling image
		        jQuery(this).css("width", maxWidth); // Set new width
		        jQuery(this).css("height", height * ratio);  // Scale height based on ratio
		        height = height * ratio;    // Reset height to match scaled image
		    }
		
		    // Check if current height is larger than max
		    if(height > maxHeight){
		        ratio = maxHeight / height; // get ratio for scaling image
		        jQuery(this).css("height", maxHeight);   // Set new height
		        jQuery(this).css("width", width * ratio);    // Scale width based on ratio
		        width = width * ratio;    // Reset width to match scaled image
		    }
		} else {
		    // Check if current height is larger than max
		    if(height > maxHeight){
		        ratio = maxHeight / height; // get ratio for scaling image
		        jQuery(this).css("height", maxHeight);   // Set new height
		        jQuery(this).css("width", width * ratio);    // Scale width based on ratio
		        width = width * ratio;    // Reset width to match scaled image
		    }			

		    // Check if the current width is larger than the max
		    if(width >= maxWidth){
		        ratio = maxWidth / width;   // get ratio for scaling image
		        jQuery(this).css("width", maxWidth); // Set new width
		        jQuery(this).css("height", height * ratio);  // Scale height based on ratio
		        height = height * ratio;    // Reset height to match scaled image
		    }
		
		}
	})
	});
	
</script>

<bean:define id="mappingPath" name="mappingPath"/>
<bean:define id="fullPath"><%= request.getContextPath() + "/publico" + mappingPath + ".do" %></bean:define>
<bean:define id="applicationInformationLinkDefault" name="application.information.link.default"/>
<bean:define id="applicationInformationLinkEnglish" name="application.information.link.english"/>

<bean:define id="individualCandidacyProcess" name="individualCandidacyProcessBean" property="individualCandidacyProcess"/>

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
	<bean:message key="title.view.candidacy.process" bundle="CANDIDATE_RESOURCES"/>
</div>

<h1><bean:write name="application.name"/></h1>

<logic:equal name="individualCandidacyProcess" property="allRequiredFilesUploaded" value="false">
<div class="h_box_alt">
	<div class="lightbulb">
		<p><bean:message key="message.missing.document.files" bundle="CANDIDATE_RESOURCES"/></p>
		<ul>
			<logic:iterate id="missingDocumentFileType" name="individualCandidacyProcess" property="missingRequiredDocumentFiles">
				<li><fr:view name="missingDocumentFileType" property="localizedName"/></li>
			</logic:iterate>
		</ul>

		<p><bean:message key="message.ist.conditions.note" arg0="<%=org.fenixedu.academic.domain.organizationalStructure.Unit.getInstitutionAcronym()%>" bundle="CANDIDATE_RESOURCES"/></p>
	</div>
</div>
</logic:equal>

<%  
	if(!locale.getLanguage().equals(Locale.ENGLISH.getLanguage())) {		
%>

<logic:equal name="individualCandidacyProcess" property="candidacyHasVatDocument" value="false">
	<p><em><bean:message key="message.national.candidates.must.send.vat.number.document" bundle="CANDIDATE_RESOURCES"/></em></p>
</logic:equal>

<% } %>		

<logic:equal value="true" name="isApplicationSubmissionPeriodValid">
<fr:form action='<%= mappingPath + ".do" %>' id="editCandidacyForm">
	<input type="hidden" name="method" id="methodForm"/>
	<fr:edit id="individualCandidacyProcessBean" name="individualCandidacyProcessBean" visible="false" />
	<noscript>
		<html:submit onclick="this.form.method.value='prepareEditCandidacyProcess';"><bean:message key="button.edit" bundle="APPLICATION_RESOURCES" /></html:submit>
		<html:submit onclick="this.form.method.value='prepareEditCandidacyDocuments';"><bean:message key="label.edit.candidacy.documents" bundle="CANDIDATE_RESOURCES" /></html:submit>
		<html:cancel><bean:message key="label.back" bundle="APPLICATION_RESOURCES" /></html:cancel>
	</noscript>
	
	<a href="#" onclick="javascript:document.getElementById('methodForm').value='prepareEditCandidacyProcess';document.getElementById('editCandidacyForm').submit();"><bean:message key="button.edit" bundle="APPLICATION_RESOURCES" /> <bean:message key="label.application.lowercase" bundle="CANDIDATE_RESOURCES"/></a> | 
	<a href="#" onclick="javascript:document.getElementById('methodForm').value='prepareUploadPhoto';document.getElementById('editCandidacyForm').submit();"><bean:message key="label.individual.candidacy.upload.photo" bundle="CANDIDATE_RESOURCES"/></a> |
	<a href="#" onclick="javascript:document.getElementById('methodForm').value='prepareEditCandidacyQualifications';document.getElementById('editCandidacyForm').submit();"><bean:message key="label.edit.candidacy" bundle="CANDIDATE_RESOURCES"/></a> |
	<a href="#" onclick="javascript:document.getElementById('methodForm').value='prepareEditCandidacyDocuments';document.getElementById('editCandidacyForm').submit();"> <bean:message key="label.edit.candidacy.documents" bundle="CANDIDATE_RESOURCES" /></a>
</fr:form>
</logic:equal>

<p style="margin-bottom: 0.5em;">
	<b><bean:message key="label.process.id" bundle="CANDIDATE_RESOURCES"/></b>: <bean:write name="individualCandidacyProcess" property="processCode"/>
</p>

<h2 style="margin-top: 1em;"><bean:message key="title.personal.data" bundle="CANDIDATE_RESOURCES"/></h2>

<logic:equal name="individualCandidacyProcessBean" property="individualCandidacyProcess.isCandidateWithRoles" value="true">
<fr:view name="individualCandidacyProcessBean" 
	schema="PublicCandidacyProcess.candidacyDataBean.internal.candidate.view">
	<fr:layout name="tabular">
		<fr:property name="classes" value="thlight thleft"/>
        <fr:property name="columnClasses" value="width175px,,,,"/>
	</fr:layout>
</fr:view>
</logic:equal>

<logic:equal name="individualCandidacyProcessBean" property="individualCandidacyProcess.isCandidateWithRoles" value="false">
<fr:view name="individualCandidacyProcessBean" 
	schema="PublicCandidacyProcess.candidacyDataBean">
	<fr:layout name="tabular">
		<fr:property name="classes" value="thlight thleft"/>
        <fr:property name="columnClasses" value="width175px,,,,"/>
	</fr:layout>
</fr:view>
</logic:equal>

<table>
	<tr>
		<td class="width175px"><bean:message key="label.photo" bundle="CANDIDATE_RESOURCES"/>:</td>
		<td>
			<logic:present name="individualCandidacyProcess" property="photo">
			<bean:define id="photo" name="individualCandidacyProcess" property="photo"/>
			<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><img src="<%= FileDownloadServlet.getDownloadUrl((GenericFile)photo) %>" id="photo"/>
			</logic:present>
			
			<logic:notPresent name="individualCandidacyProcess" property="photo">
				<em><bean:message key="message.does.not.have.photo" bundle="CANDIDATE_RESOURCES"/></em>
			</logic:notPresent>
		</td>
	</tr>
</table>

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
		<td><fr:view name="individualCandidacyProcess" property="candidacy.event.originalAmountToPay"/></td>
	</tr>
</table>
</logic:notEmpty>


<h2 style="margin-top: 1em;"><bean:message key="title.educational.background" bundle="CANDIDATE_RESOURCES"/></h2>

<h3 style="margin-bottom: 0.5em;"><bean:message key="message.degree.change.last.enrollment" bundle="CANDIDATE_RESOURCES"/></h3>


<logic:equal name="individualCandidacyProcess" property="candidacy.refactoredPrecedentDegreeInformation.candidacyExternal" value="true">
<table class="tdtop">
<tr>
	<td><bean:message key="label.university.attended.previously" bundle="CANDIDATE_RESOURCES"/>:</td>
	<td>
		<fr:view name="individualCandidacyProcessBean"
			schema="PublicCandidacyProcessBean.institutionUnitName.view">
			<fr:layout name="flow">
				<fr:property name="labelExcluded" value="true"/>
			</fr:layout>
		</fr:view>
	</td>
</tr>
</table>
<table>
<tr>
	<td><bean:message key="label.university.previously.attended.country" bundle="CANDIDATE_RESOURCES"/>:</td>
	<td>
		<fr:view name="individualCandidacyProcessBean"
			schema="PublicCandidacyProcessBean.institution.country.manage">
			<fr:layout name="flow">
				<fr:property name="labelExcluded" value="true"/>
			</fr:layout>
		</fr:view>
	</td>
</tr>
<tr>
	<td><bean:message key="label.bachelor.degree.previously.enrolled" bundle="CANDIDATE_RESOURCES"/>:</td>
	<td>
		<fr:view name="individualCandidacyProcessBean"
			schema="PublicCandidacyProcessBean.degreeDesignation.manage">
			<fr:layout name="flow">
				<fr:property name="labelExcluded" value="true"/>
			</fr:layout>
		</fr:view>
	</td>
</tr>
</table>
</logic:equal>

<logic:equal name="individualCandidacyProcess" property="candidacy.refactoredPrecedentDegreeInformation.candidacyExternal" value="false">
	<fr:view name="individualCandidacyProcess" property="candidacy.refactoredPrecedentDegreeInformation.studentCurricularPlan" schema="IndividualCandidacyPrecendentDegreeInformation.StudentCurricularPlan.view">
		<fr:layout name="tabular">
		</fr:layout>
	</fr:view>
</logic:equal>



<% 
	if(!locale.getLanguage().equals(Locale.ENGLISH.getLanguage())) {
%>

<h3><bean:message key="title.other.academic.titles" bundle="CANDIDATE_RESOURCES"/></h3>
<logic:empty name="individualCandidacyProcessBean" property="formationConcludedBeanList">
	<p><em><bean:message key="message.other.academic.titles.empty" bundle="CANDIDATE_RESOURCES"/>.</em></p>	
</logic:empty>


<logic:notEmpty name="individualCandidacyProcessBean" property="formationConcludedBeanList">
	<table class="tstyle2 thlight thcenter">
	<tr>
		<th><bean:message key="label.other.academic.titles.program.name" bundle="CANDIDATE_RESOURCES"/></th>
		<th><bean:message key="label.other.academic.titles.institution" bundle="CANDIDATE_RESOURCES"/></th>
		<th><bean:message key="label.other.academic.titles.conclusion.date" bundle="CANDIDATE_RESOURCES"/></th>
		<th><bean:message key="label.other.academic.titles.conclusion.grade" bundle="CANDIDATE_RESOURCES"/></th>
	</tr>
	<logic:iterate id="academicTitle" name="individualCandidacyProcessBean" property="formationConcludedBeanList" indexId="index">
	<tr>
		<td>
			<fr:view 	name="academicTitle"
						schema="PublicCandidacyProcessBean.formation.designation">
				<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
			</fr:view>
		</td>
		<td>
			<fr:view name="academicTitle"
				schema="PublicCandidacyProcessBean.formation.institutionUnitName.view">
				<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
			</fr:view>	
		</td>
		<td>
			<fr:view 	name="academicTitle"
						schema="PublicCandidacyProcessBean.formation.conclusion.date">
				<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
			</fr:view> 
		</td>
		<td>
			<fr:view 	name="academicTitle"
						schema="PublicCandidacyProcessBean.formation.conclusion.grade">
				<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
			</fr:view> 
		</td>
	</tr>
	</logic:iterate>
	</table>
</logic:notEmpty>

<%
	}
%>

<h2 style="margin-top: 1em;"><bean:message key="title.master.degree.change.course.choice" arg0="<%=org.fenixedu.academic.domain.organizationalStructure.Unit.getInstitutionAcronym()%>" bundle="CANDIDATE_RESOURCES"/></h2>

<p><fr:view name="individualCandidacyProcessBean"
			schema="PublicCandidacyProcessBean.degree.change.selectedDegree.view">
	<fr:layout name="flow">
		<fr:property name="labelExcluded" value="true"/>
	</fr:layout>
</fr:view></p>


<%-- Observations --%>
<h2 style="margin-top: 1em;"><bean:message key="label.observations" bundle="CANDIDATE_RESOURCES"/>:</h2>
<fr:view name="individualCandidacyProcess"
	property="candidacy.observations">
</fr:view>


<h2 style="margin-top: 1em;"><bean:message key="label.documentation" bundle="CANDIDATE_RESOURCES"/></h2> 


<logic:empty name="individualCandidacyProcess" property="candidacy.documents">
	<p><em><bean:message key="message.documents.empty" bundle="CANDIDATE_RESOURCES"/>.</em></p>
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
		<td><fr:view name="documentFile" property="creationDate"/></td>
		<td><fr:view name="documentFile" property="filename"/></td>
	</tr>	
	</logic:iterate>
</table>
</logic:notEmpty>

<div class="mtop15"><bean:message key="message.nape.contacts" bundle="CANDIDATE_RESOURCES"/></div>


