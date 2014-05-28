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

<%@ page import="java.util.Locale"%>
<%@ page import="org.fenixedu.commons.i18n.I18N"%>
<%@ page import="net.sourceforge.fenixedu.domain.candidacyProcess.degreeChange.DegreeChangeIndividualCandidacyProcess"%>
<%@ page import="net.sourceforge.fenixedu.domain.candidacyProcess.degreeTransfer.DegreeTransferIndividualCandidacyProcess" %>
<%@ page import="net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcess" %>
<%@ page import="net.sourceforge.fenixedu.domain.person.RoleType" %>


<%@page import="net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcess"%><html:xhtml/>

<%!
	static String f(String value, Object ... args) {
    	return String.format(value, args);
	}
%>

<!-- START MAIN PAGE CONTENTS HERE -->
<bean:define id="mappingPath" name="mappingPath"/>
<bean:define id="fullPath"><%= request.getContextPath() + "/publico" + mappingPath + ".do" %></bean:define>
<bean:define id="applicationInformationLinkDefault" name="application.information.link.default"/>
<bean:define id="applicationInformationLinkEnglish" name="application.information.link.english"/>

<div class="breadcumbs">
	<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="http://gri.ist.utl.pt/en">NCMI</a> &gt;
	<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="http://gri.ist.utl.pt/en/ist/">Study at <%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%></a> &gt;
	<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href='<%= f("%s/candidacies/erasmus", request.getContextPath()) %>'><bean:message key="title.application.name.mobility" bundle="CANDIDATE_RESOURCES"/></a> &gt;
	<bean:message key="erasmus.title.application.submission" bundle="CANDIDATE_RESOURCES" />
</div>

<h1><bean:write name="application.name"/></h1>
<p><span class="success0"><bean:message key="message.application.submited.success" bundle="CANDIDATE_RESOURCES"/></span></p>

<bean:define id="endSubmissionDate" name="endSubmissionDate"/>
<bean:message key="erasmus.message.application.submited.detail" bundle="CANDIDATE_RESOURCES" arg0="<%= endSubmissionDate.toString() %>"/>

<bean:define id="individualCandidacyProcess" name="individualCandidacyProcess"/>

<logic:equal name="individualCandidacyProcess" property="allRequiredFilesUploaded" value="false">
<div class="infoop1">
	
	<p><span class="infoop2"><bean:message key="message.candidacy.warning.incomplete.process" bundle="CANDIDATE_RESOURCES"/></span></p>
	<p><span class="infoop2"><bean:message key="message.missing.document.files" bundle="CANDIDATE_RESOURCES"/></span></p>
	
	<ul class="mbottom15">
		<li><b>Passport photo</b> - The photo will be used to generate <%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%> student card.</li>
		<li><b>Passport or identity card</b></li>
		<li><b>Learning agreement</b> - The learning agreement will be available in your application process page after the form submission. You're required to download, sign, stamp and reupload the document.</li>
		<li><b>Curriculum vitae</b></li>
		<li><b>Transcript of records</b></li>
		<li><b>Declaration of your english level</b></li>
	</ul>
	
	<%--
	<ul>
		<logic:iterate id="missingDocumentFileType" name="individualCandidacyProcess" property="missingRequiredDocumentFiles">
			<li><fr:view name="missingDocumentFileType" property="localizedName"/></li>
		</logic:iterate>
	</ul>
	--%>

	<bean:define id="individualCandidacyProcess" name="individualCandidacyProcess"/>

</div>
</logic:equal>

<p>Please note that the learning agreement must be signed and stamped by your school before you uploaded it.</p>

<div class="h_box" id="contacts">
	<bean:message key="erasmus.contacts.text" arg0="<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionName().getContent()%>" bundle="CANDIDATE_RESOURCES" />
</div>
