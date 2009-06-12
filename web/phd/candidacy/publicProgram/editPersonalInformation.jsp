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
<br/>
<h2><bean:message key="label.phd.public.candidacy.createCandidacy.fillPersonalInformation.edit" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<%-- 
<bean:define id="hash" name="candidacyBean" property="candidacyHashCode.value" />
<html:link action="<%= "/candidacies/phdProgramCandidacyProcess.do?method=viewCandidacy&hash=" + hash %>">
	« <bean:message key="label.back" bundle="PHD_RESOURCES" />	
</html:link>
--%>

<fr:form id="editPersonalInformationForm" action="/candidacies/phdProgramCandidacyProcess.do">
	<fr:edit id="candidacyBean" name="candidacyBean" visible="false" />
	<input type="hidden" id="methodForm" name="method" value="editPersonalInformation" />
	<input type="hidden" id="skipValidationId" name="skipValidation" value="false"/>
	
	<noscript>
		<html:submit onclick="this.form.method.value='prepareEditPersonalInformation';"><bean:message key="label.phd.public.candidacy.createCandidacy.fillPersonalInformation.edit" bundle="PHD_RESOURCES" /></html:submit>
	</noscript>
	<a href="#" onclick="javascript:document.getElementById('skipValidationId').value='true';javascript:document.getElementById('methodForm').value='backToViewCandidacy';document.getElementById('editPersonalInformationForm').submit();">« <bean:message bundle="PHD_RESOURCES" key="label.back"/></a>

<br/><br/>
<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>


<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp?viewStateId=candidacyBean.personBean" />
<%--  ### End of Error Messages  ### --%>

<%--  ### Operation Area ### --%>

<%--
  CHECK: has candidacy period? 
<logic:equal value="true" name="isApplicationSubmissionPeriodValid">
--%>

<logic:notPresent name="candidacyBean">
	<em><bean:message key="label.php.public.candidacy.hash.not.found" bundle="PHD_RESOURCES"/></em>
</logic:notPresent>

<logic:present name="candidacyBean">

	<logic:equal name="isEmployee" value="true">
		<fr:view name="candidacyBean" property="personBean" schema="Public.PhdIndividualProgramProcess.view.person">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight mtop15" />
			</fr:layout>
		</fr:view>
	</logic:equal>

	<logic:equal name="isEmployee" value="false">
			
			<br/>
			<h2><bean:message key="title.personal.data" bundle="CANDIDATE_RESOURCES"/></h2>
		
			<fr:edit id="candidacyBean.personBean" name="candidacyBean" property="personBean" 
				schema="Public.PhdProgramCandidacyProcessBean.editPersonalInformation">
				<fr:layout name="tabular">
						<fr:property name="classes" value="thlight thleft"/>
				        <fr:property name="columnClasses" value="width175px,,tdclear tderror1"/>
						<fr:property name="requiredMarkShown" value="true" />
				</fr:layout>
			
				<fr:destination name="invalid" path="/candidacies/phdProgramCandidacyProcess.do?method=editPersonalInformationInvalid" />
			</fr:edit>
			
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message bundle="PHD_RESOURCES" key="label.edit"/></html:submit>
			<p class="mtop15"><span><bean:message key="message.fields.required" bundle="CANDIDATE_RESOURCES"/></span></p>
	</logic:equal>
</logic:present>
</fr:form>
