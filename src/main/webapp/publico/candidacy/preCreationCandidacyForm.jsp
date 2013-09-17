<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

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

<h1><bean:write name="application.name" bundle="CANDIDATE_RESOURCES"/></h1>

<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES" property="captcha.error">
	<p><span class="error0"><bean:write name="message"/></span></p>
</html:messages>

<html:messages id="message" message="true" bundle="CANDIDATE_RESOURCES" property="<%= org.apache.struts.action.ActionMessages.GLOBAL_MESSAGE %>">
	<p><span class="error0"><bean:write name="message"/></span></p>
</html:messages>

<fr:form action='<%= mappingPath + ".do?method=bindEmailWithHashCodeAndSendMailWithLink" %>'>
	<p><bean:message key="message.email.required.begin.process" bundle="CANDIDATE_RESOURCES"/></p>

	
	<bean:message key="label.email" bundle="CANDIDATE_RESOURCES"/>:
	
	<fr:edit id="PublicAccessCandidacy.preCreationForm" 
		 name="candidacyPreCreationBean" 
		 type="net.sourceforge.fenixedu.presentationTier.Action.publico.candidacies.IndividualCandidacyProcessPublicDA$CandidacyPreCreationBean"
		 slot="email">
		 <fr:layout>
		 	<fr:property name="size" value="30"/>
		 	<fr:validator name="net.sourceforge.fenixedu.presentationTier.renderers.validators.RequiredEmailValidator"/>
			<fr:validator name="net.sourceforge.fenixedu.presentationTier.renderers.validators.TextLengthValidator">
				<fr:property name="type" value="character"/>
				<fr:property name="length" value="100"/>
			</fr:validator>
		 </fr:layout>
		 <fr:destination name="invalid" path='<%= mappingPath + ".do?method=preparePreCreationOfCandidacyInvalid" %>'/>
	</fr:edit>
	<span class="error0"><fr:message for="PublicAccessCandidacy.preCreationForm"/></span>
	
	<div class="mtop1 mbottom1">
		<label for="captcha"><bean:message key="label.captcha" bundle="ALUMNI_RESOURCES" />:</label>
		<div class="mbottom05">
			<img src="<%= request.getContextPath() + "/publico/jcaptcha.do" %>" style="border: 1px solid #bbb; padding: 5px;"/>
		</div>
		<span class="color777"><bean:message key="label.captcha.process" bundle="ALUMNI_RESOURCES" /></span><br/>
		<input type="text" name="j_captcha_response" size="30" style="margin-bottom: 1em;"/>
		<br/>
	</div>

	
	<p class="mtop1 mbottom0"><html:submit><bean:message key="button.continue" bundle="APPLICATION_RESOURCES" /> »</html:submit></p>
</fr:form>

