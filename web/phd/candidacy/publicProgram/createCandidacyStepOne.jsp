<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<%-- ### Title #### --%>
<div class="breadcumbs">
	<a href="http://www.ist.utl.pt">IST</a> &gt;
	<a href="http://www.ist.utl.pt/en/html/ist-epfl/">IST-EPFL</a> &gt;
	<bean:message key="title.submit.application" bundle="CANDIDATE_RESOURCES"/>
</div>

<h1><bean:message key="label.phd.public.candidacy" bundle="PHD_RESOURCES" /></h1>

<%-- ### End of Title ### --%>

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<jsp:include page="/phd/candidacy/publicProgram/createCandidacyStepsBreadcrumb.jsp?step=1"></jsp:include>

<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp?viewStateId=candidacyBean.personBean" />
<%--  ### End of Error Messages  ### --%>

<%-- <p><em><bean:message key="message.max.file.size" bundle="CANDIDATE_RESOURCES"/></em></p> --%>

<%--  ### Operation Area ### --%>

<logic:notPresent name="candidacyBean">
	<em><bean:message key="label.php.public.candidacy.hash.not.found" bundle="PHD_RESOURCES"/></em>
</logic:notPresent>

<logic:present name="candidacyBean">

	<fr:form action="/candidacies/phdProgramCandidacyProcess.do?method=createCandidacyStepTwo">
		<fr:edit id="candidacyBean" name="candidacyBean" visible="false" />
		
		<br/>
		<h2><bean:message key="title.personal.data" bundle="CANDIDATE_RESOURCES"/></h2>
	
		<fr:edit id="candidacyBean.personBean" name="candidacyBean" property="personBean" 
			schema="Public.PhdProgramCandidacyProcessBean.editPersonalInformation">
			<fr:layout name="tabular">
					<fr:property name="classes" value="thlight thleft"/>
			        <fr:property name="columnClasses" value="width175px,,tdclear tderror1"/>
					<fr:property name="requiredMarkShown" value="true" />
			</fr:layout>
		
			<fr:destination name="invalid" path="/candidacies/phdProgramCandidacyProcess.do?method=createCandidacyStepOneInvalid" />
		</fr:edit>
		
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message bundle="PHD_RESOURCES" key="label.continue"/></html:submit>
		<p class="mtop15"><span><bean:message key="message.fields.required" bundle="CANDIDATE_RESOURCES"/></span></p>
		
	</fr:form>
	
</logic:present>
