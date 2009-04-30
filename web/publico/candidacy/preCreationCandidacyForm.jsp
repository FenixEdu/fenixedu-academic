<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.ChecksumRewriter"%>

<html:xhtml/>

<bean:define id="mappingPath" name="mappingPath"/>
<bean:define id="fullPath"><%= request.getContextPath() + "/publico" + mappingPath + ".do" %></bean:define>

<div class="breadcumbs">
	<a href="http://www.ist.utl.pt">IST</a> &gt;
	<%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="<%= request.getContextPath() + "/candidaturas/introducao" %>"><bean:message key="title.candidate" bundle="CANDIDATE_RESOURCES"/></a> &gt;
	<%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="<%= request.getContextPath() + "/candidaturas/licenciaturas" %>"><bean:message key="title.degrees" bundle="CANDIDATE_RESOURCES"/></a> &gt;
	<a href='<%= fullPath + "?method=beginCandidacyProcessIntro" %>'><bean:write name="application.name"/> </a> &gt;
	<bean:message key="title.submit.application" bundle="CANDIDATE_RESOURCES"/>
</div>

<h1><bean:write name="application.name" bundle="CANDIDATE_RESOURCES"/></h1>

<html:messages id="message" message="true" bundle="CANDIDATE_RESOURCES">
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
	
	<p class="mtop1 mbottom0"><html:submit><bean:message key="button.continue" bundle="APPLICATION_RESOURCES" /> »</html:submit></p>
</fr:form>
