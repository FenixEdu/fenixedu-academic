<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>


<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

<%-- ### Title #### --%>
<em><bean:message  key="label.phd.academicAdminOffice.breadcrumb" bundle="PHD_RESOURCES"/></em>
<h2><bean:message key="label.phd.candidacy.academicAdminOffice.createCandidacy" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<jsp:include page="createCandidacyStepsBreadcrumb.jsp?step=2"></jsp:include>

<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>


<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp?viewStateId=createCandidacyBean.editPersonalInformation.simplified" />
<%--  ### End of Error Messages  ### --%>



<%--  ### Context Information  ### --%>


<%--  ### End Of Context Information  ### --%>



<fr:form action="/phdProgramCandidacyProcess.do">

<input type="hidden" name="method" value="" />

<fr:edit id="createCandidacyBean" name="createCandidacyBean" visible="false" />


<%--  ### Operation Area (e.g. Create Candidacy)  ### --%>

<p class="mtop15 mbottom05"><strong><bean:message  key="label.phd.candidacy.academicAdminOffice.createCandidacy.personalInformation" bundle="PHD_RESOURCES"/></strong></p>
<fr:edit id="createCandidacyBean.editPersonalInformation.simplified"
	name="createCandidacyBean"
	property="personBean"
	schema="PhdProgramCandidacyProcessBean.editPersonalInformation.simplified">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
		<fr:property name="columnClasses" value=",,tdclear tderror1" />
		<fr:destination name="invalid" path="/phdProgramCandidacyProcess.do?method=createCandidacyInvalid" />
	</fr:layout>
	
</fr:edit>

<p class="mtop15 mbottom05"><strong><bean:message  key="label.phd.candidacy.academicAdminOffice.createCandidacy.phdProgramInformation" bundle="PHD_RESOURCES"/></strong></p>
<fr:edit id="createCandidacyBean.editProgramInformation"
	name="createCandidacyBean"
	schema="PhdProgramCandidacyProcessBean.editProgramInformation">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
		<fr:property name="columnClasses" value=",,tdclear tderror1" />
		<fr:destination name="invalid" path="/phdProgramCandidacyProcess.do?method=createCandidacyInvalid" />
	</fr:layout>
	
</fr:edit>


<%--  ### End of Operation Area  ### --%>



<%--  ### Buttons (e.g. Submit)  ### --%>
<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='createCandidacy';"><bean:message bundle="PHD_RESOURCES" key="label.submit"/></html:submit>
<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" onclick="this.form.method.value='cancelCreateCandidacy';"><bean:message bundle="PHD_RESOURCES" key="label.cancel"/></html:cancel>

<%--  ### End of Buttons (e.g. Submit)  ### --%>


</fr:form>

</logic:present>

