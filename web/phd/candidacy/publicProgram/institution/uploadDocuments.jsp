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
	<jsp:include page="/phd/candidacy/publicProgram/institution/commonBreadcumbs.jsp" />

	<bean:message key="title.edit.candidacy.upload.documents" bundle="CANDIDATE_RESOURCES"/>
</div>

<h1><bean:message key="label.phd.institution.public.candidacy" bundle="PHD_RESOURCES" /></h1>

<%-- ### End of Title ### --%>

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>

<bean:define id="processId" name="process" property="externalId" />
<fr:form id="uploadDocumentForm" action="<%= "/applications/phd/phdProgramApplicationProcess.do?method=uploadDocuments&processId=" + processId %>" encoding="multipart/form-data">
	<fr:edit id="candidacyBean" name="candidacyBean" visible="false" />
	
	<bean:define id="hash" name="process" property="candidacyHashCode.value" />
	<p>	
	<html:link action="/applications/phd/phdProgramApplicationProcess.do?method=viewApplication" paramId="hash" paramName="hash" >
		« <bean:message bundle="PHD_RESOURCES" key="label.back"/>
	</html:link>
	</p>
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
			<p class="mvert05"><bean:message key="message.phd.candidacy.upload.following.documents" bundle="PHD_RESOURCES" />:</p>
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
					<em><bean:message key="message.max.file.size" bundle="PHD_RESOURCES"/></em>
				</p>
			
				<fr:edit id="documentByType" name="documentByType" schema="Public.PhdCandidacyDocumentUploadBean.edit.with.type">
					<fr:layout name="tabular-editable">
						<fr:property name="classes" value="thlight thleft"/>
						<fr:property name="columnClasses" value="width175px,,tdclear tderror1"/>
						<fr:property name="requiredMarkShown" value="true" />
					</fr:layout>
					
					<fr:destination name="cancel" path="<%= "/applications/phd/phdProgramApplicationProcess.do?method=viewApplication&hash=" + hash %>" />
					<fr:destination name="invalid" path="<%= "/applications/phd/phdProgramApplicationProcess.do?method=uploadDocumentsInvalid&processId=" + processId %>" />
				</fr:edit>
			</fieldset>
			</div>
			<p>
				<html:submit><bean:message bundle="PHD_RESOURCES" key="label.add.document"/></html:submit>
				<html:cancel><bean:message bundle="PHD_RESOURCES" key="label.cancel" /></html:cancel> 
			</p>
	</logic:present>
</logic:equal>
</fr:form>

<br/>

<fr:view name="candidacyProcessDocuments" schema="Public.PhdProgramProcessDocument.view">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight thcenter"/>
	</fr:layout>
</fr:view>
