<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.ChecksumRewriter"%>
<%@ page import="java.util.Locale"%>
<%@ page import="pt.utl.ist.fenix.tools.util.i18n.Language"%>
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
	<%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="http://gri.ist.utl.pt/en">GRI</a> &gt;
	<%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="http://gri.ist.utl.pt/en/ist/">Study at IST</a> &gt;
	<%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href='<%= f("%s/candidacies/erasmus", request.getContextPath()) %>'><bean:message key="title.application.name.erasmus" bundle="CANDIDATE_RESOURCES"/></a> &gt;
	<bean:message key="erasmus.title.application.submission" bundle="CANDIDATE_RESOURCES" />
</div>

<h1><bean:write name="application.name"/></h1>
<p><span class="success0"><bean:message key="message.application.submited.success" bundle="CANDIDATE_RESOURCES"/>.</span></p>

<bean:define id="endSubmissionDate" name="endSubmissionDate"/>
<bean:message key="erasmus.message.application.submited.detail" bundle="CANDIDATE_RESOURCES" arg0="<%= endSubmissionDate.toString() %>"/>

<bean:define id="individualCandidacyProcess" name="individualCandidacyProcess"/>

<logic:equal name="individualCandidacyProcess" property="allRequiredFilesUploaded" value="false">
<div class="infoop1">
	<p><bean:message key="message.missing.document.files" bundle="CANDIDATE_RESOURCES"/></p>
	<ul>
	<logic:iterate id="missingDocumentFileType" name="individualCandidacyProcess" property="missingRequiredDocumentFiles">
		<li><fr:view name="missingDocumentFileType" property="localizedName"/></li>
	</logic:iterate>
	</ul>

	<bean:define id="individualCandidacyProcess" name="individualCandidacyProcess"/>

</div>
</logic:equal>

<% if(((IndividualCandidacyProcess) individualCandidacyProcess).getPersonalDetails().getPerson().hasRole(RoleType.CANDIDATE)) { %>
<div>
	Username : <%= ((IndividualCandidacyProcess) individualCandidacyProcess).getPersonalDetails().getPerson().getLoginIdentification().getUsername() %>
</div>
<% } %>

<div class="mtop15" id="contacts">
	<bean:message key="erasmus.contacts.text" bundle="CANDIDATE_RESOURCES" />
</div>
