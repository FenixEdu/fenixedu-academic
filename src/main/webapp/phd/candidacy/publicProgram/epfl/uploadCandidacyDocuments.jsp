<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
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
	<a href="http://www.ist.utl.pt/en/about-IST/global-cooperation/IST-EPFL/">IST-EPFL</a> &gt;
	<bean:message key="title.submit.application" bundle="CANDIDATE_RESOURCES"/>
</div>

<h1><bean:message key="label.phd.epfl.public.candidacy" bundle="PHD_RESOURCES" /></h1>

<%-- ### End of Title ### --%>

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>

<fr:form id="uploadDocumentForm" action="/applications/epfl/phdProgramCandidacyProcess.do" encoding="multipart/form-data">
	<fr:edit id="candidacyBean" name="candidacyBean" visible="false" />
	<input type="hidden" id="methodForm" name="method" value="uploadDocuments" />
	<input type="hidden" id="skipValidationId" name="skipValidation" value="false"/>	
	
	<p><a href="#" onclick="javascript:clearFileInputs();javascript:document.getElementById('skipValidationId').value='true';javascript:document.getElementById('methodForm').value='backToViewCandidacy';javascript:document.getElementById('uploadDocumentForm').submit();">« <bean:message bundle="PHD_RESOURCES" key="label.back"/></a></p>
<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<logic:equal name="canEditCandidacy" value="true">

	<%--  ### Error Messages  ### --%>
	<jsp:include page="/phd/errorsAndMessages.jsp" />
	<%--  ### End of Error Messages  ### --%>
	
	<style>
	.warning0 {
	background-color: #fbf8cc;
	/*color: #805500;*/
	padding: 0.5em 1em;
	}
	</style>

	<h2><bean:message key="title.public.phd.documents" bundle="PHD_RESOURCES" /></h2>
	<logic:messagesPresent message="true" property="validation">
		<div class="warning0 mvert1">
			<p class="mvert05">Please, upload the following documents:</p>
			<ul class="mvert05">
				<html:messages id="messages" message="true" bundle="PHD_RESOURCES" property="validation">
					<li><bean:write name="messages" /></li>
				</html:messages>
			</ul>
		</div>
	</logic:messagesPresent>
	
	<logic:notPresent name="candidacyBean">
		<em><bean:message key="label.php.public.candidacy.hash.not.found" bundle="PHD_RESOURCES"/></em>
	</logic:notPresent>
	
	<logic:present name="candidacyBean">
			<div class="fs_form">
			<fieldset style="display: block;">
				<legend><bean:message bundle="PHD_RESOURCES" key="label.add.document"/></legend>
				<p class="mtop05">
					<em><bean:message key="message.mandatory.fields" bundle="PHD_RESOURCES"/></em><br/>
					<em><bean:message key="message.max.file.sizeAndType.8mb.pdf" bundle="PHD_RESOURCES"/></em>
				</p>
			
				<fr:edit id="documentByType" name="documentByType" schema="Public.PhdCandidacyDocumentUploadBean.edit.with.type">
					<fr:layout name="tabular-editable">
						<fr:property name="classes" value="thlight thleft"/>
						<fr:property name="columnClasses" value="width175px,,tdclear tderror1"/>
						<fr:property name="requiredMarkShown" value="true" />
					</fr:layout>
					<fr:destination name="invalid" path="/applications/epfl/phdProgramCandidacyProcess.do?method=uploadDocumentsInvalid" />
				</fr:edit>
			</fieldset>
			</div>	
			<p><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message bundle="PHD_RESOURCES" key="label.add.document"/></html:submit></p>
	</logic:present>
</logic:equal>
</fr:form>

<br/>

<fr:view name="candidacyProcessDocuments" schema="Public.PhdProgramProcessDocument.view">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight thcenter"/>
	</fr:layout>
</fr:view>
