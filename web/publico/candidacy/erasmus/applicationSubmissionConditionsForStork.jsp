<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.ChecksumRewriter"%>
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
<div class="breadcumbs">
	<%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="http://gri.ist.utl.pt/en">GRI</a> &gt;
	<%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="http://gri.ist.utl.pt/en/ist/">Study at IST</a> &gt;
	<%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href='<%= f("%s/candidacies/erasmus", request.getContextPath()) %>'><bean:message key="title.application.name.erasmus" bundle="CANDIDATE_RESOURCES"/></a> &gt;
	<bean:message key="erasmus.title.application.submission" bundle="CANDIDATE_RESOURCES" />
</div>
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

<fr:form action='<%= mappingPath + ".do?method=prepareCandidacyCreationForStork" %>'>
	<fr:edit id="individualCandidacyProcessBean" name="individualCandidacyProcessBean" visible="false" />
	
	<p class="mtop2"><html:submit><bean:message key="button.continue" bundle="APPLICATION_RESOURCES"/> »</html:submit></p>
</fr:form>

