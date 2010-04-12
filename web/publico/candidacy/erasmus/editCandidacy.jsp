<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.ChecksumRewriter"%>
<%@ page import="pt.utl.ist.fenix.tools.util.i18n.Language"%>
<%@page import="org.apache.struts.action.ActionMessages" %>
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
	<%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="http://gri.ist.utl.pt/en">NMCI</a> &gt;
	<%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="http://gri.ist.utl.pt/en/ist/">Study at IST</a> &gt;
	<%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href='<%= f("%s/candidacies/erasmus", request.getContextPath()) %>'><bean:message key="title.application.name.erasmus" bundle="CANDIDATE_RESOURCES"/></a> &gt;
	<bean:message key="erasmus.title.application.submission" bundle="CANDIDATE_RESOURCES" />
</div>

<h1><bean:write name="application.name"/></h1>

<bean:define id="individualCandidacyProcess" name="individualCandidacyProcessBean" property="individualCandidacyProcess"/>
<bean:define id="individualCandidacyProcessOID" name="individualCandidacyProcess" property="OID"/>

<p><a href='<%= fullPath + "?method=backToViewCandidacy&individualCandidacyProcess=" + individualCandidacyProcessOID %>'>« <bean:message key="label.back" bundle="CANDIDATE_RESOURCES"/></a></p>

<logic:equal name="individualCandidacyProcessBean" property="individualCandidacyProcess.isCandidateEmployee" value="true">
	<p><bean:message key="message.application.employee.edition.forbidden" bundle="CANDIDATE_RESOURCES"/></p>
</logic:equal>

<logic:equal name="individualCandidacyProcessBean" property="individualCandidacyProcess.isCandidateEmployee" value="false">
<logic:equal name="individualCandidacyProcessBean" property="individualCandidacyProcess.isCandidateWithRoles" value="true">
	<p><bean:message key="message.application.person.with.roles.forbidden" bundle="CANDIDATE_RESOURCES"/></p>
</logic:equal>

<logic:equal name="individualCandidacyProcessBean" property="individualCandidacyProcess.isCandidateWithRoles" value="false">
<%--
<p><span><bean:message key="message.all.fields.are.required" bundle="CANDIDATE_RESOURCES"/></span></p>
--%>
<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES" property="<%= ActionMessages.GLOBAL_MESSAGE %>">
	<span class="error0"> <bean:write name="message" /> </span>
	<br />
</html:messages>
<fr:hasMessages for="CandidacyProcess.personalDataBean" type="conversion">
	<ul class="nobullet list6">
		<fr:messages>
			<li><span class="error0"><fr:message/></span></li>
		</fr:messages>
	</ul>
</fr:hasMessages>

<html:messages id="message" message="true" bundle="CANDIDATE_RESOURCES" property="individualCandidacyMessages">
	<p><span class="error0"><bean:write name="message"/></span></p>
</html:messages>

<fr:form action='<%= mappingPath + ".do?method=editCandidacyProcess" %>' >
	<fr:edit id="individualCandidacyProcessBean" name="individualCandidacyProcessBean" visible="false" />
	
	<h2 style="margin-top: 1em;"><bean:message key="title.personal.data" bundle="CANDIDATE_RESOURCES"/></h2>
	
	<fr:edit id="candidacyProcess.personalDataBean"
		name="individualCandidacyProcessBean"
		property="personBean"
		schema="ErasmusIndividualCandidacyPublicProcess.personalDataBean">
			<fr:layout name="tabular">
				<fr:property name="classes" value="thlight thleft"/>
		        <fr:property name="columnClasses" value=",,tdclear tderror1"/>
			</fr:layout>
		<fr:destination name="invalid" path='<%= mappingPath + ".do?method=executeEditCandidacyPersonalInformationInvalid" %>' />
	</fr:edit>
	
	<p class="mtop15">	
		<html:submit><bean:message key="button.submit" bundle="APPLICATION_RESOURCES" /></html:submit>
	</p>
</fr:form>
</logic:equal>
</logic:equal>
