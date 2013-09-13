<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ page import="pt.utl.ist.fenix.tools.util.i18n.Language"%>
<%@ page import="java.util.Locale"%>


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
	<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href='<%= f("%s/candidacies/erasmus", request.getContextPath()) %>'><bean:message key="title.application.name.mobility" bundle="CANDIDATE_RESOURCES"/></a> &gt;
	<bean:message key="erasmus.title.application.submission" bundle="CANDIDATE_RESOURCES" />
</div>

<h1><bean:write name="application.name"/></h1>
<bean:define id="mobilityProgram" name="individualCandidacyProcessBean" property="mobilityStudentDataBean.selectedMobilityProgram.registrationAgreement.description"/>
<h1><strong><bean:write name="mobilityProgram"/></strong></h1>

<p class="steps">
	<span><bean:message key="mobility.label.step.one.personal.details" bundle="CANDIDATE_RESOURCES"/></span> >
	<span><bean:message key="mobility.label.step.two.educational.background" bundle="CANDIDATE_RESOURCES" /></span> >
	<span><bean:message key="mobility.label.step.three.mobility.program" bundle="CANDIDATE_RESOURCES" /></span> >
	<span><bean:message key="mobility.label.step.four.degree.and.subjects" bundle="CANDIDATE_RESOURCES" /></span> >
	<span class="actual"><bean:message key="mobility.label.step.five.honour.declaration" bundle="CANDIDATE_RESOURCES" /></span> 
</p>

<%--
<p class="mtop15"><span><bean:message key="message.fields.required" bundle="CANDIDATE_RESOURCES"/></span></p>
--%>

<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES" property="captcha.error">
	<p><span class="error0"><bean:write name="message"/></span></p>
</html:messages>

<html:messages id="message" message="true" bundle="CANDIDATE_RESOURCES" property="error">
	<p><span class="error0"><bean:write name="message"/></span></p>
</html:messages>

<fr:hasMessages for="CandidacyProcess.personalDataBean" type="conversion">
	<ul class="nobullet list6">
		<fr:messages>
			<li><span class="error0"><fr:message/></span></li>
		</fr:messages>
	</ul>
</fr:hasMessages>

<fr:form id="secondCycleCandidacyForm" action='<%= mappingPath + ".do?userAction=createCandidacy" %>' encoding="multipart/form-data">
	<input type="hidden" id="methodId" name="method" />

	<fr:edit id="individualCandidacyProcessBean" name="individualCandidacyProcessBean" visible="false" />

	<h2 style="margin-top: 1em;"><bean:message key="title.degree.change.honor.declaration" bundle="CANDIDATE_RESOURCES"/></h2>

	<p><bean:message key="message.degree.change.honor.declaration.detail" bundle="CANDIDATE_RESOURCES"/>.</p>

	<p>
		<fr:edit 	id="individualCandidacyProcessBean.honor.declaration"
					name="individualCandidacyProcessBean"
					schema="PublicCandidacyProcessBean.honor.agreement">
			<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
		</fr:edit>
		<b><bean:message key="label.degree.change.honor.declaration" bundle="CANDIDATE_RESOURCES"/></b>
	</p>

	<p><em><bean:message key="message.ist.conditions.note" bundle="CANDIDATE_RESOURCES"/></em></p>
	
	<p>
		<html:submit onclick="this.form.method.value='submitCandidacy'; return true;"><bean:message key="label.continue" bundle="APPLICATION_RESOURCES" /></html:submit>
		<%--
		<html:cancel onclick="this.form.method.value='listProcesses'; return true;"><bean:message key="label.cancel" bundle="APPLICATION_RESOURCES" /></html:cancel>
		--%>
	</p>
		
</fr:form>
