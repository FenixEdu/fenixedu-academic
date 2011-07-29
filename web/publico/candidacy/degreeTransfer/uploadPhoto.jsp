<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.ChecksumRewriter"%>
<%@ page import="pt.utl.ist.fenix.tools.util.i18n.Language"%>
<%@ page import="java.util.Locale"%>

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
		<%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="http://www.ist.utl.pt/pt/candidatos/"><bean:message key="title.candidate" bundle="CANDIDATE_RESOURCES"/></a> &gt;
	<% } else { %>
		<%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="http://www.ist.utl.pt/en/prospective-students/"><bean:message key="title.candidate" bundle="CANDIDATE_RESOURCES"/></a> &gt;
	<% } %>

	<% 
		if(!locale.getLanguage().equals(Locale.ENGLISH.getLanguage())) {
	%>
		<%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="http://www.ist.utl.pt/pt/candidatos/candidaturas/licenciaturas/"><bean:message key="title.degrees" bundle="CANDIDATE_RESOURCES"/></a> &gt;
	<% } else { %>
			<%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="http://www.ist.utl.pt/en/prospective-students/admissions/bachelor/"><bean:message key="title.degrees" bundle="CANDIDATE_RESOURCES"/></a> &gt;
	<% } %>
				
	<% 
		if(!locale.getLanguage().equals(Locale.ENGLISH.getLanguage())) {
	%>
		<%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href='<%= applicationInformationLinkDefault %>'><bean:write name="application.name"/> </a> &gt;
	<% } else { %>
		<%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href='<%= applicationInformationLinkEnglish %>'><bean:write name="application.name"/> </a> &gt;
	<% } %>
	<bean:message key="label.edit.candidacy.documents" bundle="CANDIDATE_RESOURCES"/>
</div>

<h1><bean:write name="application.name"/></h1>

<h2 style="margin-top: 1em;"><bean:message key="label.edit.candidacy.upload.photo" bundle="CANDIDATE_RESOURCES"/></h2>

<p><em><bean:message key="message.max.file.size" bundle="CANDIDATE_RESOURCES"/></em></p>

<bean:define id="individualCandidacyProcess" name="candidacyDocumentUploadBean" property="individualCandidacyProcess"/>
<bean:define id="individualCandidacyProcessOID" name="individualCandidacyProcess" property="OID"/>
<fr:form action='<%= mappingPath + ".do?method=uploadPhoto" %>' encoding="multipart/form-data">
	<fr:edit id="individualCandidacyProcessBean.document.file"
		name="candidacyDocumentUploadBean">
		<fr:schema type="net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcessDocumentUploadBean" bundle="CANDIDATE_RESOURCES">
		  <fr:slot name="stream" key="label.candidacy.document.file">
		    <fr:property name="fileNameSlot" value="fileName"/>
		    <fr:property name="fileSizeSlot" value="fileSize"/>
			<fr:validator class="pt.ist.fenixWebFramework.renderers.validators.FileValidator">
				<fr:property name="maxSize" value="3698688"/>
				<fr:property name="acceptedExtensions" value="png,jpg" />
				<fr:property name="acceptedTypes" value="image/png, image/jpeg" />
			</fr:validator>
		  </fr:slot>
		</fr:schema>		
		<fr:layout>
			<fr:property name="classes" value="tstyle5 thlight thleft"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>
	</fr:edit>
	<html:submit><bean:message key="button.submit" bundle="APPLICATION_RESOURCES" /></html:submit>		
</fr:form>

<p><a href='<%= fullPath + "?method=backToViewCandidacy&individualCandidacyProcess=" + individualCandidacyProcessOID %>'>« <bean:message key="label.back" bundle="CANDIDATE_RESOURCES"/></a></p>

