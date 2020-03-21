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
<%@page import="org.fenixedu.academic.domain.candidacyProcess.secondCycle.SecondCycleIndividualCandidacyProcess"%>
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<%@ page import="java.util.Locale"%>
<%@ page import="org.fenixedu.commons.i18n.I18N"%>
<%@ page import="org.fenixedu.academic.domain.candidacyProcess.degreeChange.DegreeChangeIndividualCandidacyProcess"%>
<%@ page import="org.fenixedu.academic.domain.candidacyProcess.degreeTransfer.DegreeTransferIndividualCandidacyProcess" %>

<html:xhtml/>

<!-- START MAIN PAGE CONTENTS HERE -->
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
		<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href='<%= applicationInformationLinkDefault %>'><bean:write name="application.name"/> </a> &gt;
	<% } else { %>
		<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href='<%= applicationInformationLinkEnglish %>'><bean:write name="application.name"/> </a> &gt;
	<% } %>
	<bean:message key="title.submit.application" bundle="CANDIDATE_RESOURCES"/>
</div>

<h1><bean:write name="application.name"/></h1>
<p><span class="success0"><bean:message key="message.application.submited.success" bundle="CANDIDATE_RESOURCES"/></span></p>

<bean:define id="endSubmissionDate" name="endSubmissionDate"/>
<bean:message key="message.application.submited.detail" bundle="CANDIDATE_RESOURCES" arg0="<%= endSubmissionDate.toString() %>"/>

<bean:define id="individualCandidacyProcess" name="individualCandidacyProcess" />
<logic:notEmpty name="individualCandidacyProcess" property="associatedPaymentCode">
	<%  final CandidacyProcess process = (CandidacyProcess) request.getAttribute("parentProcess"); %>
<table>
	<tr>
		<td>
			<p>
				<bean:message key="message.application.sibs.payment.details" bundle="CANDIDATE_RESOURCES"/>
			</p>
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
		</td>		
		<td>
			<% if (process != null && process.getAlternantePaymentMethod() != null) { %>
				<%= process.getAlternantePaymentMethod().getContent() %>
			<% } %>
		</td>		
	</tr>
</table>

</logic:notEmpty>

<logic:equal name="individualCandidacyProcess" property="allRequiredFilesUploaded" value="false">
<div class="infoop1">
	<p><bean:message key="message.missing.document.files" bundle="CANDIDATE_RESOURCES"/></p>
	<ul>
	<logic:iterate id="missingDocumentFileType" name="individualCandidacyProcess" property="missingRequiredDocumentFiles">
		<li><fr:view name="missingDocumentFileType" property="localizedName"/></li>
	</logic:iterate>
	</ul>

	<bean:define id="individualCandidacyProcess" name="individualCandidacyProcess"/>
	<%
		if(!locale.getLanguage().equals(Locale.ENGLISH.getLanguage())) {
			if(individualCandidacyProcess instanceof DegreeChangeIndividualCandidacyProcess || individualCandidacyProcess instanceof DegreeTransferIndividualCandidacyProcess) {
	%>
	<p><em><bean:message key="message.ist.student.obtain.grades.on.fenix" arg0="<%=org.fenixedu.academic.domain.organizationalStructure.Unit.getInstitutionAcronym()%>" bundle="CANDIDATE_RESOURCES"/></em></p>
	<%
			}
		}
	%>

	<p><bean:message key="message.ist.conditions.note" arg0="<%=org.fenixedu.academic.domain.organizationalStructure.Unit.getInstitutionAcronym()%>" bundle="CANDIDATE_RESOURCES"/></p>

</div>
</logic:equal>

<% 
	if(!locale.getLanguage().equals(Locale.ENGLISH.getLanguage())) {
%>
<p><em><bean:message key="message.national.candidates.must.send.vat.number.document" bundle="CANDIDATE_RESOURCES"/></em></p>
<% 	} %>

<div class="mtop15"><bean:message key="message.nape.contacts" bundle="CANDIDATE_RESOURCES"/></div>

