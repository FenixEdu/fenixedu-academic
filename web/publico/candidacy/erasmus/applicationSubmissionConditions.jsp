<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.ChecksumRewriter"%>
<%@ page import="java.util.Locale"%>
<%@ page import="pt.utl.ist.fenix.tools.util.i18n.Language"%>

<html:xhtml/>

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
		<%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href='<%= applicationInformationLinkDefault %>'><bean:write name="application.name"/> </a> &gt;
	<% } else { %>
		<%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href='<%= applicationInformationLinkEnglish %>'><bean:write name="application.name"/> </a> &gt;
	<% } %>
	<bean:message key="title.submit.application" bundle="CANDIDATE_RESOURCES"/>
</div>


<h1><bean:write name="application.name" bundle="CANDIDATE_RESOURCES"/></h1>

<p>
Fill the form in completely and validate it (by submitting it on the web). Make sure you upload all the required documents together and send your application on time. 
In case your application is accepted, all original documents must be delivered at the Registrar's Office before the course starts. 
All documents and diplomas should be in English. Please join a legalized translation of all documents if written in another language. 
Some of your personal data are filled with the information provided by your National Citizen Card.
After the submission a process will be created and will be approved by the Internacional Relations Office and Erasmus Coordinator. 
Some alerts will be generated in your process. These alerts are due to incoherences in your application. 
You must read this alerts and update your application in order to fix it.
</p>

<p>
You should, periodically, consult your process in order to see any alerts and watch your application approval. 
</p>

<p>
<strong>Note:</strong>Please note that in order for your application to be considered valid, this form must be filled in completely and all required documents must be enclosed.
Incomplete forms will not be processed. If you have any question you can contact International Relations Office (GRI). 
</p>


<div class="mtop15" id="contacts">
	<h2>Contacts</h2>
	<p><b><a href="http://gri.ist.utl.pt/">International Relations Office (GRI)</a></b></p>
	<p>
		<strong>IST - Alameda </strong>
		<br>Phone: 218 417 251 / 218 419 155 
		<br>Fax: 218 419 344
	</p>
	<p>
		<strong>IST - Taguspark</strong>
		<br>Phone: 214 233 545
	</p>
	<p><a href="mailto:webmaster@ist.utl.pt">Cristina Sousa</a></p>
</div>

<bean:define id="hash" name="hash"/> 
<fr:form action='<%= mappingPath + ".do?method=prepareCandidacyCreation&hash=" + hash %>'>
	<p class="mtop2"><html:submit><bean:message key="button.continue" bundle="APPLICATION_RESOURCES"/> »</html:submit></p>
</fr:form>

