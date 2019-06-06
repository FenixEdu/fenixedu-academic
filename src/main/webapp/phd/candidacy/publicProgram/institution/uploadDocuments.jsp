<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page isELIgnored="true"%>
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
	<jsp:include page="/phd/candidacy/publicProgram/institution/commonBreadcumbs.jsp" />
	<bean:message key="title.edit.candidacy.upload.documents" bundle="CANDIDATE_RESOURCES"/>
</div>

<h1><bean:message key="label.phd.institution.public.candidacy" bundle="PHD_RESOURCES" /></h1>

<%-- ### End of Title ### --%>

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>

<bean:define id="processId" name="process" property="externalId" />
<bean:define id="hash" name="process" property="candidacyHashCode.value" />
<p>
	<html:link action="/applications/phd/phdProgramApplicationProcess.do?method=viewApplication" paramId="hash" paramName="hash" >
		« <bean:message bundle="PHD_RESOURCES" key="label.back"/>
	</html:link>
</p>

<fr:form id="uploadDocumentForm" action="<%= "/applications/phd/phdProgramApplicationProcess.do?method=uploadDocuments&processId=" + processId %>" encoding="multipart/form-data">
	<fr:edit id="candidacyBean" name="candidacyBean" visible="false" />
	
<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<logic:equal name="canEditCandidacy" value="true">

	<h2><bean:message key="title.public.phd.documents" bundle="PHD_RESOURCES" /></h2>

	<%--  ### Error Messages  ### --%>
	<jsp:include page="/phd/errorsAndMessages.jsp" />
	<%--  ### End of Error Messages  ### --%>
	
	<logic:messagesPresent message="true" property="validation">
		<div class="mvert15">
			<p class="mvert05"><bean:message key="message.phd.candidacy.upload.following.documents" bundle="PHD_RESOURCES" />:</p>
			<ul class="mvert05" style="margin-left: 0; padding-left: 15px;">
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
						<bean:message key="message.max.file.sizeAndType.8mb.pdf" bundle="PHD_RESOURCES"/>
					</p>
				
					<bean:define id="typeValues" value="CV,ID_DOCUMENT,MOTIVATION_LETTER,SOCIAL_SECURITY,RESEARCH_PLAN,HABILITATION_CERTIFICATE_DOCUMENT,PAYMENT_DOCUMENT,TOEFL_LINGUISTICS_CERTIFICATE,GRE_LINGUISTICS_CERTIFICATE"/>
					<logic:equal value="false" name="hasPaymentFees">
						<bean:define id="typeValues" value="CV,ID_DOCUMENT,MOTIVATION_LETTER,SOCIAL_SECURITY,RESEARCH_PLAN,HABILITATION_CERTIFICATE_DOCUMENT,TOEFL_LINGUISTICS_CERTIFICATE,GRE_LINGUISTICS_CERTIFICATE"/>	
					</logic:equal>
					<fr:edit id="documentByType" name="documentByType" >
						<fr:schema type="org.fenixedu.academic.domain.phd.PhdProgramDocumentUploadBean" bundle="PHD_RESOURCES">
							<fr:slot name="type" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
								<fr:property name="includedValues" value="<%= typeValues %>" />
								<fr:property name="bundle" value="PHD_RESOURCES" />
							</fr:slot>
							<fr:slot name="file" key="label.phd.public.document">
								<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator" />
								<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.FileValidator">
									<fr:property name="required" value="true" />
									<fr:property name="maxSize" value="8mb" />
									<fr:property name="acceptedExtensions" value="pdf" />
									<fr:property name="acceptedTypes" value="application/pdf" />
								</fr:validator>
								<fr:property name="fileNameSlot" value="filename"/>
								<fr:property name="size" value="40"/>
							</fr:slot>				
						</fr:schema>
						<fr:layout name="tabular-editable">
							<fr:property name="classes" value="thlight thleft"/>
							<fr:property name="columnClasses" value="width175px,,tdclear tderror1"/>
							<fr:property name="optionalMarkShown" value="true" />
						</fr:layout>
						
						<fr:destination name="cancel" path="<%= "/applications/phd/phdProgramApplicationProcess.do?method=viewApplication&hash=" + hash %>" />
						<fr:destination name="invalid" path="<%= "/applications/phd/phdProgramApplicationProcess.do?method=uploadDocumentsInvalid&processId=" + processId %>" />
					</fr:edit>
					
				</fieldset>
			</div>
			<p>
				<html:submit><bean:message bundle="PHD_RESOURCES" key="label.add.document"/></html:submit>
				<%-- <html:cancel><bean:message bundle="PHD_RESOURCES" key="label.cancel" /></html:cancel> --%> 
			</p>
	</logic:present>
</logic:equal>
</fr:form>

<br/>

<fr:view name="candidacyProcessDocuments" schema="Public.PhdProgramProcessDocument.view">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight thcenter"/>
		<fr:link name="remove" link="<%= "/applications/phd/phdProgramApplicationProcess.do?method=removeDocument&documentId=${externalId}&processId=" + processId %>" label="label.remove,PHD_RESOURCES"/>
	</fr:layout>
</fr:view>
