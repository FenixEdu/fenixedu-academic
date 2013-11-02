<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<%@ page import="java.util.Locale"%>
<%@ page import="pt.utl.ist.fenix.tools.util.i18n.Language"%>

<%!
	static String f(String value, Object ... args) {
    	return String.format(value, args);
	}
%>

<html:xhtml/>

<bean:define id="mappingPath" name="mappingPath"/>
<bean:define id="fullPath"><%= request.getContextPath() + "/publico" + mappingPath + ".do" %></bean:define>
<bean:define id="applicationInformationLinkDefault" name="application.information.link.default"/>
<bean:define id="applicationInformationLinkEnglish" name="application.information.link.english"/>

<div class="breadcumbs">
	<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="http://gri.ist.utl.pt/en">NMCI</a> &gt;
	<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="http://gri.ist.utl.pt/en/ist/">Study at IST</a> &gt;
	<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href='<%= f("%s/candidacies/erasmus", request.getContextPath()) %>'><bean:message key="title.application.name.erasmus" bundle="CANDIDATE_RESOURCES"/></a> &gt;
	<bean:message key="erasmus.title.application.submission" bundle="CANDIDATE_RESOURCES" />
</div>

<h1><bean:write name="application.name" bundle="CANDIDATE_RESOURCES"/></h1>



<bean:message key="erasmus.application.submission.condition.text" bundle="CANDIDATE_RESOURCES" />


<bean:define id="hash" name="hash"/> 
<fr:form action='<%= mappingPath + ".do?method=prepareCandidacyCreation&hash=" + hash %>'>
	<p class="mtop15 mbottom2"><html:submit><bean:message key="erasmus.application.submission.button" bundle="CANDIDATE_RESOURCES" /> »</html:submit></p>
</fr:form>



<div class="h_box" id="contacts">
	<bean:message key="erasmus.contacts.text" arg0="<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionName().getContent()%>" bundle="CANDIDATE_RESOURCES" />
</div>

