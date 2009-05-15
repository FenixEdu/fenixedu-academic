<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>


<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

<%-- ### Title #### --%>
<em><bean:message  key="label.phd.academicAdminOffice.breadcrumb" bundle="PHD_RESOURCES"/></em>
<h2><bean:message key="label.phd.candidacy.academicAdminOffice.manageCandidacyDocuments" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>


<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<%--
<div class="breadcumbs">
	<span class="actual">Step 1: Step Name</span> > 
	<span>Step N: Step name </span>
</div>
--%>
<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>


<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>


<%--  ### Context Information (e.g. Person Information, Registration Information)  ### --%>
<strong><bean:message  key="label.phd.process" bundle="PHD_RESOURCES"/></strong>
<fr:view schema="PhdProgramCandidacyProcess.view" name="process">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight mtop15" />
	</fr:layout>
</fr:view>
<%--  ### End Of Context Information  ### --%>


<bean:define id="processId" name="process" property="externalId" />
<%--  ### Operation Area (e.g. Create Candidacy)  ### --%>
<fr:form action="/phdProgramCandidacyProcess.do?method=uploadDocuments">
  	<input type="hidden" name="method" value="" />
  	<input type="hidden" name="processId" value="<%= processId.toString()  %>" />
	<fr:edit id="documentsToUpload"
		name="documentsToUpload"
		schema="PhdCandidacyDocumentUploadBean.edit"
		action="/phdProgramCandidacyProcess.do?method=uploadDocuments">
	
		<fr:layout name="tabular-editable">
			<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
			<fr:destination name="invalid" path="/phdProgramCandidacyProcess.do?method=uploadDocumentsInvalid" />
		</fr:layout>
	</fr:edit>

<%--  ### End of Operation Area  ### --%>

<%--  ### Buttons (e.g. Submit)  ### --%>
  <html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='uploadDocuments';"><bean:message bundle="APPLICATION_RESOURCES" key="label.add"/></html:submit>
<%--  ### End of Buttons (e.g. Submit)  ### --%>
</fr:form>

</logic:present>
