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

<html:xhtml/>

<!-- START MAIN PAGE CONTENTS HERE -->

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
		<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="<%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %>pt/candidatos/candidaturas/licenciaturas/"><bean:message key="title.degrees" bundle="CANDIDATE_RESOURCES"/></a> &gt;
	<% } else { %>
			<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="<%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %>en/prospective-students/admissions/bachelor/"><bean:message key="title.degrees" bundle="CANDIDATE_RESOURCES"/></a> &gt;
	<% } %>
				
	<% 
		if(!locale.getLanguage().equals(Locale.ENGLISH.getLanguage())) {
	%>
		<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href='<%= applicationInformationLinkDefault %>'><bean:write name="application.name"/> </a> &gt;
	<% } else { %>
		<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href='<%= applicationInformationLinkEnglish %>'><bean:write name="application.name"/> </a> &gt;
	<% } %>
	<bean:message key="label.edit.candidacy.documents" bundle="CANDIDATE_RESOURCES"/>
</div>

<h1><bean:write name="application.name"/></h1>

<logic:messagesPresent message="true" property="error">
	<div class="error3 mbottom05" style="width: 700px;">
		<html:messages id="messages" message="true" bundle="CANDIDATE_RESOURCES" property="error">
			<p class="mvert025"><bean:write name="messages" /></p>
		</html:messages>
	</div>
</logic:messagesPresent>

<h2 style="margin-top: 1em;"><bean:message key="label.documentation" bundle="CANDIDATE_RESOURCES"/></h2>

<p><em><bean:message key="message.max.file.size" bundle="CANDIDATE_RESOURCES"/></em></p>
<p><em><bean:message key="message.candidacy.upload.pdf.documents" bundle="CANDIDATE_RESOURCES"/></em></p>

<bean:define id="individualCandidacyProcess" name="candidacyDocumentUploadBean" property="individualCandidacyProcess"/>
<bean:define id="individualCandidacyProcessOID" name="individualCandidacyProcess" property="OID"/>
<fr:form action='<%= mappingPath + ".do?method=editCandidacyDocuments" %>' encoding="multipart/form-data">
	<fr:edit id="individualCandidacyProcessBean.document.file"
		name="candidacyDocumentUploadBean" 
		schema="PublicCandidacyProcessBean.degreetransfer.documentUpload.edit">
		<fr:layout>
			<fr:property name="classes" value="tstyle5 thlight thleft"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>
	</fr:edit>
	<html:submit><bean:message key="button.submit" bundle="APPLICATION_RESOURCES" /></html:submit>		
</fr:form>

<logic:notEmpty name="individualCandidacyProcess" property="activeDocumentFiles">
<table class="tstyle2 thlight thcenter mtop15">
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

<p><a href='<%= fullPath + "?method=backToViewCandidacy&individualCandidacyProcess=" + individualCandidacyProcessOID %>'>« <bean:message key="label.back" bundle="CANDIDATE_RESOURCES"/></a></p>

