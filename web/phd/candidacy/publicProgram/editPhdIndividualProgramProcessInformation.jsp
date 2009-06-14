<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<%@page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<%-- ### Title #### --%>
<div class="breadcumbs">
	<a href="http://www.ist.utl.pt">IST</a> &gt;
	<a href="http://www.ist.utl.pt/en/html/ist-epfl/">IST-EPFL</a> &gt;
	<bean:message key="title.submit.application" bundle="CANDIDATE_RESOURCES"/>
</div>

<h1><bean:message key="label.phd.public.candidacy" bundle="PHD_RESOURCES" /></h1>

<%-- ### End of Title ### --%>

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>

<fr:form id="editPhdInformationForm" action="/candidacies/phdProgramCandidacyProcess.do">
	<fr:edit id="candidacyBean" name="candidacyBean" visible="false" />
	<input type="hidden" id="methodId" name="method" value="editPhdIndividualProgramProcessInformation" />
	<input type="hidden" id="removeIndexId" name="removeIndex" value=""/>
	<input type="hidden" id="skipValidationId" name="skipValidation" value="false"/>	
	
	<a href="#" onclick="javascript:document.getElementById('skipValidationId').value='true';javascript:document.getElementById('methodId').value='backToViewCandidacy';document.getElementById('editPhdInformationForm').submit();">« <bean:message bundle="PHD_RESOURCES" key="label.back"/></a>
	<br/>
<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>

<%--
  CHECK: has candidacy period? 
<logic:equal value="true" name="isApplicationSubmissionPeriodValid">
--%>

<p class="mtop15"><span><bean:message key="message.fields.required" bundle="CANDIDATE_RESOURCES"/></span></p>

<logic:notPresent name="candidacyBean">
	<em><bean:message key="label.php.public.candidacy.hash.not.found" bundle="PHD_RESOURCES"/></em>
</logic:notPresent>

<logic:present name="candidacyBean">
	
	<logic:empty name="guidingBean">
		<div class="fs_form">
		<fieldset style="display: block;">
			<legend><bean:message key="label.phd.public.candidacy.createCandidacy.fillCandidacyInformation.edit" bundle="PHD_RESOURCES" /></legend>
			<fr:edit id="individualProcessBean" name="individualProcessBean" schema="Public.PhdIndividualProgramProcessBean.editDetails">		
				<fr:layout name="tabular-editable">
					<fr:property name="classes" value="thlight thleft"/>
					<fr:property name="columnClasses" value="width175px,,tdclear tderror1"/>
					<fr:property name="requiredMarkShown" value="true" />
				</fr:layout>
				<fr:destination name="invalid" path="/candidacies/phdProgramCandidacyProcess.do?method=editPhdIndividualProgramProcessInformationInvalid" />
			</fr:edit>
			</fieldset>
			</div>
			<p><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message bundle="PHD_RESOURCES" key="label.edit"/></html:submit></p>
	</logic:empty>

	<logic:notEmpty name="guidingBean">
		<strong><bean:message key="label.phd.public.candidacy.createCandidacy.fillCandidacyInformation.edit" bundle="PHD_RESOURCES" /></strong>
		
		<fr:view name="candidacyBean" property="candidacyHashCode.individualProgramProcess" schema="Public.PhdIndividualProgramProcess.view">
			<fr:layout name="tabular">
				<fr:property name="classes" value="thlight thleft"/>
		        <fr:property name="columnClasses" value="width175px,,,,"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>

	<h2 style="margin-top: 1em;"><bean:message key="title.public.phd.guidings" bundle="PHD_RESOURCES"/></h2>
	
	<logic:notEmpty name="guidingBean">
		<div class="fs_form">
		<fieldset style="display: block;">

			<fr:edit id="guidingBean" name="guidingBean" schema="Public.PhdProgramGuidingBean.edit">
				<fr:layout name="tabular-editable">
					<fr:property name="classes" value="thlight thleft"/>
					<fr:property name="columnClasses" value="width175px,,tdclear tderror1"/>
					<fr:property name="requiredMarkShown" value="true" />
				</fr:layout>
				<fr:destination name="invalid" path="/candidacies/phdProgramCandidacyProcess.do?method=editPhdIndividualProgramProcessInformationInvalid" />
				<fr:destination name="cancel" path="/candidacies/phdProgramCandidacyProcess.do?method=prepareEditPhdIndividualProgramProcessInformation" />
			</fr:edit>
			</fieldset>
			</div>
			<p><html:submit onclick="document.getElementById('methodId').value='addGuidingToExistingCandidacy';" bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message bundle="PHD_RESOURCES" key="label.add"/></html:submit></p>
	</logic:notEmpty>
	
	<logic:notEmpty name="candidacyBean" property="candidacyHashCode.individualProgramProcess.guidings">
		<logic:iterate id="guiding" name="candidacyBean" property="candidacyHashCode.individualProgramProcess.guidings" indexId="index" >
			<strong><%= index.intValue() + 1 %>.</strong>
			<bean:define id="guidingId" name="guiding" property="externalId" />
			<fr:view name="guiding" schema="Public.PhdProgramGuiding.view">
				<fr:layout name="tabular">
					<fr:property name="classes" value="thlight thleft"/>
			        <fr:property name="columnClasses" value="width175px,,,,"/>
				</fr:layout>
			</fr:view>
			<p class="mtop05"><a onclick='<%= "document.getElementById(\"skipValidationId\").value=\"true\"; document.getElementById(\"removeIndexId\").value=" + guidingId + "; document.getElementById(\"methodId\").value=\"removeGuidingFromExistingCandidacy\"; document.getElementById(\"editPhdInformationForm\").submit();" %>' href="#" >- <bean:message key="label.remove" bundle="PHD_RESOURCES"/></a></p>
		</logic:iterate>
	</logic:notEmpty>
	<p><a onclick='<%= "document.getElementById(\"skipValidationId\").value=\"true\"; document.getElementById(\"methodId\").value=\"prepareAddGuidingToExistingCandidacy\"; document.getElementById(\"editPhdInformationForm\").submit();" %>' href="#" >+ <bean:message key="label.add" bundle="PHD_RESOURCES"/></a></p>
		
</logic:present>
</fr:form>

<br/>
