<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<%@ page import="pt.utl.ist.fenix.tools.util.i18n.Language"%>
<%@ page import="java.util.Locale"%>

<html:xhtml/>

<!-- START MAIN PAGE CONTENTS HERE -->

<bean:define id="mappingPath" name="mappingPath"/>
<bean:define id="fullPath"><%= request.getContextPath() + "/publico" + mappingPath + ".do" %></bean:define>
<bean:define id="applicationInformationLinkDefault" name="application.information.link.default"/>
<bean:define id="applicationInformationLinkEnglish" name="application.information.link.english"/>

<div class="breadcumbs">
	<a href="<%= net.sourceforge.fenixedu.domain.Instalation.getInstance().getInstituitionURL() %>"><%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%></a> &gt;
	<% 
		Locale locale = Language.getLocale();
		if(!locale.getLanguage().equals(Locale.ENGLISH.getLanguage())) {
	%>
		<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="<%= net.sourceforge.fenixedu.domain.Instalation.getInstance().getInstituitionURL() %>pt/candidatos/"><bean:message key="title.candidate" bundle="CANDIDATE_RESOURCES"/></a> &gt;
	<% } else { %>
		<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="<%= net.sourceforge.fenixedu.domain.Instalation.getInstance().getInstituitionURL() %>en/prospective-students/"><bean:message key="title.candidate" bundle="CANDIDATE_RESOURCES"/></a> &gt;
	<% } %>

	<% 
		if(!locale.getLanguage().equals(Locale.ENGLISH.getLanguage())) {
	%>
		<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="<%= net.sourceforge.fenixedu.domain.Instalation.getInstance().getInstituitionURL() %>pt/candidatos/candidaturas/licenciaturas/"><bean:message key="title.degrees" bundle="CANDIDATE_RESOURCES"/></a> &gt;
	<% } else { %>
			<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="<%= net.sourceforge.fenixedu.domain.Instalation.getInstance().getInstituitionURL() %>en/prospective-students/admissions/bachelor/"><bean:message key="title.degrees" bundle="CANDIDATE_RESOURCES"/></a> &gt;
	<% } %>
				
	<% 
		if(!locale.getLanguage().equals(Locale.ENGLISH.getLanguage())) {
	%>
		<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href='<%= applicationInformationLinkDefault %>'><bean:write name="application.name"/> </a> &gt;
	<% } else { %>
		<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href='<%= applicationInformationLinkEnglish %>'><bean:write name="application.name"/> </a> &gt;
	<% } %>
	<bean:message key="label.edit.candidacy.documents" bundle="CANDIDATE_RESOURCES"/>
</div>

<h1><bean:write name="application.name"/></h1>

<h2 style="margin-top: 1em;"><bean:message key="label.edit.candidacy.upload.photo" bundle="CANDIDATE_RESOURCES"/></h2>

<p><em><bean:message key="message.max.file.size" bundle="CANDIDATE_RESOURCES"/></em></p>

<bean:define id="individualCandidacyProcess" name="candidacyDocumentUploadBean" property="individualCandidacyProcess"/>
<bean:define id="individualCandidacyProcessOID" name="individualCandidacyProcess" property="OID"/>
<fr:form action='<%= mappingPath + ".do?method=uploadPhoto" %>' encoding="multipart/form-data">
	<fr:edit id="individualCandidacyProcessBean" name="individualCandidacyProcessBean" visible="false" />
	<fr:edit id="individualCandidacyProcessBean.document.file"
		name="candidacyDocumentUploadBean">
		<fr:schema type="net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcessDocumentUploadBean" bundle="CANDIDATE_RESOURCES">
		  <fr:slot name="stream" key="label.candidacy.document.file">
		    <fr:property name="fileNameSlot" value="fileName"/>
		    <fr:property name="fileSizeSlot" value="fileSize"/>
			<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.FileValidator">
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
		<fr:destination name="invalid" path='<%= mappingPath + ".do?method=uploadPhotoInvalid" %>' />
	</fr:edit>
	<html:submit><bean:message key="button.submit" bundle="APPLICATION_RESOURCES" /></html:submit>		
</fr:form>

<p><a href='<%= fullPath + "?method=backToViewCandidacy&individualCandidacyProcess=" + individualCandidacyProcessOID %>'>« <bean:message key="label.back" bundle="CANDIDATE_RESOURCES"/></a></p>

