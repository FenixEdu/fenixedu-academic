<%@page import="net.sourceforge.fenixedu.domain.candidacyProcess.secondCycle.SecondCycleIndividualCandidacyProcess"%>
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<%@ page import="java.util.Locale"%>
<%@ page import="pt.utl.ist.fenix.tools.util.i18n.Language"%>
<%@ page import="net.sourceforge.fenixedu.domain.candidacyProcess.degreeChange.DegreeChangeIndividualCandidacyProcess"%>
<%@ page import="net.sourceforge.fenixedu.domain.candidacyProcess.degreeTransfer.DegreeTransferIndividualCandidacyProcess" %>

<html:xhtml/>

<!-- START MAIN PAGE CONTENTS HERE -->
<bean:define id="mappingPath" name="mappingPath"/>
<bean:define id="fullPath"><%= request.getContextPath() + "/publico" + mappingPath + ".do" %></bean:define>
<bean:define id="applicationInformationLinkDefault" name="application.information.link.default"/>
<bean:define id="applicationInformationLinkEnglish" name="application.information.link.english"/>

<div class="breadcumbs">
	<a href="http://www.ist.utl.pt">IST</a> &gt;
	<% 
		Locale locale = Language.getLocale();
		if(!locale.getLanguage().equals(Locale.ENGLISH.getLanguage())) {
	%>
		<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="http://www.ist.utl.pt/pt/candidatos/"><bean:message key="title.candidate" bundle="CANDIDATE_RESOURCES"/></a> &gt;
	<% } else { %>
		<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="http://www.ist.utl.pt/en/prospective-students/"><bean:message key="title.candidate" bundle="CANDIDATE_RESOURCES"/></a> &gt;
	<% } %>
	<% 
		if(!locale.getLanguage().equals(Locale.ENGLISH.getLanguage())) {
	%>
		<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href='<%= applicationInformationLinkDefault %>'><bean:write name="application.name"/> </a> &gt;
	<% } else { %>
		<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href='<%= applicationInformationLinkEnglish %>'><bean:write name="application.name"/> </a> &gt;
	<% } %>
	<bean:message key="title.submit.application" bundle="CANDIDATE_RESOURCES"/>
</div>

<h1><bean:write name="application.name"/></h1>
<p><span class="success0"><bean:message key="message.application.submited.success" bundle="CANDIDATE_RESOURCES"/></span></p>

<bean:define id="endSubmissionDate" name="endSubmissionDate"/>
<bean:message key="message.application.submited.detail" bundle="CANDIDATE_RESOURCES" arg0="<%= endSubmissionDate.toString() %>"/>

<bean:define id="individualCandidacyProcess" name="individualCandidacyProcess" />
<logic:notEmpty name="individualCandidacyProcess" property="associatedPaymentCode">
<p><bean:message key="message.application.sibs.payment.details" bundle="CANDIDATE_RESOURCES"/></p>
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
	<p><em><bean:message key="message.ist.student.obtain.grades.on.fenix" bundle="CANDIDATE_RESOURCES"/></em></p>
	<%
			}
		}
	%>

	<p><bean:message key="message.ist.conditions.note" bundle="CANDIDATE_RESOURCES"/></p>
</div>
</logic:equal>

<% 
	if(!locale.getLanguage().equals(Locale.ENGLISH.getLanguage())) {
%>
<p><em><bean:message key="message.national.candidates.must.send.vat.number.document" bundle="CANDIDATE_RESOURCES"/></em></p>
<% 	} %>

<div class="mtop15"><bean:message key="message.nape.contacts" bundle="CANDIDATE_RESOURCES"/></div>

