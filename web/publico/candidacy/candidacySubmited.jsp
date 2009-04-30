<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.ChecksumRewriter"%>

<html:xhtml/>

<!-- START MAIN PAGE CONTENTS HERE -->
<bean:define id="mappingPath" name="mappingPath"/>
<bean:define id="fullPath"><%= request.getContextPath() + "/publico" + mappingPath + ".do" %></bean:define>

<div class="breadcumbs">
	<a href="http://www.ist.utl.pt">IST</a> &gt;
	<%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="<%= request.getContextPath() + "/candidaturas/introducao" %>"><bean:message key="title.candidate" bundle="CANDIDATE_RESOURCES"/></a> &gt;
	<%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="<%= request.getContextPath() + "/candidaturas/licenciaturas" %>"><bean:message key="title.degrees" bundle="CANDIDATE_RESOURCES"/></a> &gt;
	<a href='<%= fullPath + "?method=beginCandidacyProcessIntro" %>'><bean:write name="application.name"/> </a> &gt;
	<bean:message key="title.submit.application" bundle="CANDIDATE_RESOURCES"/>
</div>

<h1><bean:write name="application.name"/></h1>
<p><span class="success0"><bean:message key="message.application.submited.success" bundle="CANDIDATE_RESOURCES"/>.</span></p>

<bean:define id="endSubmissionDate" name="endSubmissionDate"/>
<bean:message key="message.application.submited.detail" bundle="CANDIDATE_RESOURCES" arg0="<%= endSubmissionDate.toString() %>"/>

<p><bean:message key="message.any.question.application.submission" bundle="CANDIDATE_RESOURCES" />.</p>

<logic:equal name="individualCandidacyProcess" property="allRequiredFilesUploaded" value="false">
<div class="infoop1">
	<p><bean:message key="message.missing.document.files" bundle="CANDIDATE_RESOURCES"/></p>
	<ul>
	<logic:iterate id="missingDocumentFileType" name="individualCandidacyProcess" property="missingRequiredDocumentFiles">
		<li><fr:view name="missingDocumentFileType" property="localizedName"/></li>
	</logic:iterate>
	</ul>
	<p><bean:message key="message.ist.conditions.note" bundle="CANDIDATE_RESOURCES"/></p>
</div>
</logic:equal>

<div class="mtop15"><bean:message key="message.nape.contacts" bundle="CANDIDATE_RESOURCES"/></div>

