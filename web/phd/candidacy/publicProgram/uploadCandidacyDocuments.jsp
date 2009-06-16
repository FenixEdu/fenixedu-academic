<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<script type="text/javascript">
	function clearFileInputs() {
		var tags = document.getElementsByTagName("input");
		for(i=0; i<tags.length; i++) {
			var tag = tags[i];
			if (tag.type == "file") {
				tag.parentNode.innerHTML = tag.parentNode.innerHTML;
			}
		}       
	}
</script>

<%-- ### Title #### --%>
<div class="breadcumbs">
	<a href="http://www.ist.utl.pt">IST</a> &gt;
	<a href="http://www.ist.utl.pt/en/html/ist-epfl/">IST-EPFL</a> &gt;
	<bean:message key="title.submit.application" bundle="CANDIDATE_RESOURCES"/>
</div>

<h1><bean:message key="label.phd.public.candidacy" bundle="PHD_RESOURCES" /></h1>

<h2><bean:message key="label.phd.public.candidacy.createCandidacy.updloadDocuments" bundle="PHD_RESOURCES" /></h2>


<%-- ### End of Title ### --%>

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>

<fr:form id="uploadDocumentForm" action="/candidacies/phdProgramCandidacyProcess.do" encoding="multipart/form-data">
	<fr:edit id="candidacyBean" name="candidacyBean" visible="false" />
	<input type="hidden" id="methodForm" name="method" value="uploadDocuments" />
	<input type="hidden" id="skipValidationId" name="skipValidation" value="false"/>	
	
	<a href="#" onclick="javascript:clearFileInputs();javascript:document.getElementById('skipValidationId').value='true';javascript:document.getElementById('methodForm').value='backToViewCandidacy';javascript:document.getElementById('uploadDocumentForm').submit();">« <bean:message bundle="PHD_RESOURCES" key="label.back"/></a>
<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<logic:equal name="canEditCandidacy" value="true">

	<%--  ### Error Messages  ### --%>
	<jsp:include page="/phd/errorsAndMessages.jsp" />
	<%--  ### End of Error Messages  ### --%>
	
	<p class="mtop15">
		<em><bean:message key="message.fields.required" bundle="CANDIDATE_RESOURCES"/></em><br/>
		<em><bean:message key="message.max.file.size" bundle="PHD_RESOURCES"/></em>
	</p>
	
	<logic:notPresent name="candidacyBean">
		<em><bean:message key="label.php.public.candidacy.hash.not.found" bundle="PHD_RESOURCES"/></em>
	</logic:notPresent>
	
	<logic:present name="candidacyBean">
			<div class="fs_form">
			<fieldset style="display: block;">
				<fr:edit id="documentByType" name="documentByType" schema="Public.PhdCandidacyDocumentUploadBean.edit.with.type">
					<fr:layout name="tabular-editable">
						<fr:property name="classes" value="thlight thleft"/>
						<fr:property name="columnClasses" value="width175px,,tdclear tderror1"/>
						<fr:property name="requiredMarkShown" value="true" />
					</fr:layout>
					<fr:destination name="invalid" path="/candidacies/phdProgramCandidacyProcess.do?method=uploadDocumentsInvalid" />
				</fr:edit>
			</fieldset>
			</div>	
			<p><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message bundle="PHD_RESOURCES" key="label.add"/></html:submit></p>
	</logic:present>
</logic:equal>
</fr:form>

<br/>

<fr:view name="candidacyProcessDocuments" schema="Public.PhdProgramCandidacyProcessDocument.view">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight thcenter"/>
	</fr:layout>
</fr:view>
