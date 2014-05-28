<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
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
	<a href="<%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %>"><%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%></a> &gt;
	<a href="<%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %>en/education/fct-phd-programmes/">FCT Doctoral Programmes</a> &gt;
	<bean:message key="title.submit.application" bundle="CANDIDATE_RESOURCES"/>
</div>

<h1><bean:message key="label.phd.epfl.public.candidacy" bundle="PHD_RESOURCES" /></h1>

<%-- ### End of Title ### --%>

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<jsp:include page="/phd/candidacy/publicProgram/epfl/createCandidacyStepsBreadcrumb.jsp?step=3"></jsp:include>

<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp?viewStateId=candidacyBean.curriculumVitae" />
<%--  ### End of Error Messages  ### --%>

<%--  ### Operation Area ### --%>

<fr:form id="candidacyForm" action="/applications/epfl/phdProgramCandidacyProcess.do" encoding="multipart/form-data" >

	<fr:edit id="candidacyBean" name="candidacyBean" visible="false" />

	<input type="hidden" id="methodId" name="method" value="createCandidacy"/>
	<input type="hidden" id="removeIndexId" name="removeIndex" value=""/>
	<input type="hidden" id="skipValidationId" name="skipValidation" value="false"/>

	<p class="mtop2">
		<em><bean:message key="message.mandatory.fields" bundle="PHD_RESOURCES"/></em><br/>
		<em><bean:message key="message.max.file.size" bundle="PHD_RESOURCES"/></em>
	</p>	

	<div class="fs_form">
	<fieldset style="display: block;">
		<legend><bean:message  key="label.phd.public.candidacy.createCandidacy.updloadDocuments" bundle="PHD_RESOURCES"/></legend>
	
		<fr:edit id="candidacyBean.curriculumVitae" name="candidacyBean" property="curriculumVitae" 
				 schema="Public.PhdCandidacyDocumentUploadBean.curriculum.vitae">
			<fr:layout name="tabular">
					<fr:property name="classes" value="thlight thleft"/>
			        <fr:property name="columnClasses" value="width175px,,tdclear tderror1"/>
				<fr:property name="requiredMarkShown" value="true" />
			</fr:layout>
			<fr:destination name="invalid" path="/applications/epfl/phdProgramCandidacyProcess.do?method=createCandidacyStepThreeInvalid" />
		</fr:edit>
	
		<fr:edit id="candidacyBean.identificationDocument" name="candidacyBean" property="identificationDocument" 
				 schema="Public.PhdCandidacyDocumentUploadBean.identificationDocument">
			<fr:layout name="tabular">
					<fr:property name="classes" value="thlight thleft"/>
			        <fr:property name="columnClasses" value="width175px,,tdclear tderror1"/>
				<fr:property name="requiredMarkShown" value="true" />
			</fr:layout>
			<fr:destination name="invalid" path="/applications/epfl/phdProgramCandidacyProcess.do?method=createCandidacyStepThreeInvalid" />
		</fr:edit>
	
		<fr:edit id="candidacyBean.motivationLetter" name="candidacyBean" property="motivationLetter" 
				 schema="Public.PhdCandidacyDocumentUploadBean.motivationLetter">
			<fr:layout name="tabular">
					<fr:property name="classes" value="thlight thleft"/>
			        <fr:property name="columnClasses" value="width175px,,tdclear tderror1"/>
				<fr:property name="requiredMarkShown" value="true" />
			</fr:layout>
			<fr:destination name="invalid" path="/applications/epfl/phdProgramCandidacyProcess.do?method=createCandidacyStepThreeInvalid" />
		</fr:edit>
	
		<fr:edit id="candidacyBean.socialSecurityDocument" name="candidacyBean" property="socialSecurityDocument" 
				 schema="Public.PhdCandidacyDocumentUploadBean.socialSecurityDocument">
			<fr:layout name="tabular">
					<fr:property name="classes" value="thlight thleft"/>
			        <fr:property name="columnClasses" value="width175px,,tdclear tderror1"/>
				<fr:property name="requiredMarkShown" value="true" />
			</fr:layout>
			<fr:destination name="invalid" path="/applications/epfl/phdProgramCandidacyProcess.do?method=createCandidacyStepThreeInvalid" />
		</fr:edit>
	
		<fr:edit id="candidacyBean.researchPlan" name="candidacyBean" property="researchPlan" 
				 schema="Public.PhdCandidacyDocumentUploadBean.researchPlan">
			<fr:layout name="tabular">
					<fr:property name="classes" value="thlight thleft"/>
			        <fr:property name="columnClasses" value="width175px,,tdclear tderror1"/>
				<fr:property name="requiredMarkShown" value="true" />
			</fr:layout>
			<fr:destination name="invalid" path="/applications/epfl/phdProgramCandidacyProcess.do?method=createCandidacyStepThreeInvalid" />
		</fr:edit>
	
		<fr:edit id="candidacyBean.dissertationOrFinalWorkDocument" name="candidacyBean" property="dissertationOrFinalWorkDocument" 
				 schema="Public.PhdCandidacyDocumentUploadBean.dissertationOrFinalWorkDocument">
			<fr:layout name="tabular">
					<fr:property name="classes" value="thlight thleft"/>
			        <fr:property name="columnClasses" value="width175px,,tdclear tderror1"/>
				<fr:property name="requiredMarkShown" value="true" />
			</fr:layout>
			<fr:destination name="invalid" path="/applications/epfl/phdProgramCandidacyProcess.do?method=createCandidacyStepThreeInvalid" />
		</fr:edit>
	</fieldset>
	</div>

	<logic:notEmpty name="candidacyBean" property="habilitationCertificateDocuments">
		<div class="fs_form">
		<fieldset style="display: block;">
			<legend><bean:message key="label.phd.public.documents.habilitationCertificateDocuments" bundle="PHD_RESOURCES"/></legend>
	
			<logic:iterate id="document" name="candidacyBean" property="habilitationCertificateDocuments" indexId="index">
				<strong><%= index.intValue() + 1 %>.</strong> <em><bean:write name="document" property="remarks"/></em>
				<fr:edit id="<%= "candidacyBean.habilitationCertificateDocument" + index %>" name="document" schema="Public.PhdCandidacyDocumentUploadBean.habilitationCertificateDocument">
					<fr:layout name="tabular">
						<fr:property name="classes" value="thlight thleft"/>
			        	<fr:property name="columnClasses" value="width175px,,tdclear tderror1"/>
						<fr:property name="requiredMarkShown" value="true" />
					</fr:layout>
					<fr:destination name="invalid" path="/applications/epfl/phdProgramCandidacyProcess.do?method=createCandidacyStepThreeInvalid" />
				</fr:edit>
				<%-- 
				<p class="mtop05"><a onclick='<%= "javascript:clearFileInputs();javascript:document.getElementById(\"skipValidationId\").value=\"true\";javascript:document.getElementById(\"removeIndexId\").value=" + index + ";javascript:document.getElementById(\"methodId\").value=\"removeHabilitationCertificateDocument\";javascript:document.getElementById(\"candidacyForm\").submit();" %>' href="#" >- <bean:message key="label.remove" bundle="PHD_RESOURCES"/></a></p>
				--%>
			</logic:iterate>
		</fieldset>
		</div>
	</logic:notEmpty>
	<%-- 
	<p><a onclick='<%= "javascript:clearFileInputs();javascript:document.getElementById(\"skipValidationId\").value=\"true\";javascript:document.getElementById(\"methodId\").value=\"addHabilitationCertificateDocument\";javascript:document.getElementById(\"candidacyForm\").submit();" %>' href="#" >+ <bean:message key="label.add" bundle="PHD_RESOURCES"/></a></p>
	--%>

	<logic:notEmpty name="candidacyBean" property="phdGuidingLetters">
		<div class="fs_form">
		<fieldset style="display: block;">
			<legend><bean:message key="label.phd.public.documents.phdGuidingLetters" bundle="PHD_RESOURCES"/></legend>

			<logic:iterate id="guiding" name="candidacyBean" property="phdGuidingLetters" indexId="index">
				<strong><%= index.intValue() + 1 %>.</strong>
				<fr:edit id="<%= "candidacyBean.phdGuidingLetter" + index %>" name="guiding" schema="Public.PhdCandidacyDocumentUploadBean.edit">
					<fr:layout name="tabular">
						<fr:property name="classes" value="thlight thleft"/>
			        	<fr:property name="columnClasses" value="width175px,,tdclear tderror1"/>
						<fr:property name="requiredMarkShown" value="true" />
					</fr:layout>
					<fr:destination name="invalid" path="/applications/epfl/phdProgramCandidacyProcess.do?method=createCandidacyStepThreeInvalid" />
				</fr:edit>
			</logic:iterate>
		</fieldset>
		</div>
	</logic:notEmpty>

	<p class="mtop15">
		<html:submit bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" onclick="<%= "javascript:clearFileInputs();javascript:document.getElementById('skipValidationId').value='true';javascript:document.getElementById('methodId').value='returnCreateCandidacyStepTwo';javascript:document.getElementById('candidacyForm').submit();" %>">« <bean:message bundle="PHD_RESOURCES" key="label.back"/></html:submit>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message bundle="PHD_RESOURCES" key="label.phd.public.submit.candidacy"/></html:submit>
	</p>
</fr:form>
