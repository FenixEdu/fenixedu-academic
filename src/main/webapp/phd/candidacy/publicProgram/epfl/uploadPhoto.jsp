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
	<a href="<%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %>"><%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%></a> &gt;
	<a href="<%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %>en/education/fct-phd-programmes/">FCT Doctoral Programmes</a> &gt;
	<bean:message key="title.submit.application" bundle="CANDIDATE_RESOURCES"/>
</div>

<h1><bean:message key="label.phd.epfl.public.candidacy" bundle="PHD_RESOURCES" /></h1>

<h2><bean:message key="label.photo" bundle="PHD_RESOURCES" /></h2>

<%-- ### End of Title ### --%>

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>

<fr:form id="uploadPhotoForm" action="/applications/epfl/phdProgramCandidacyProcess.do" encoding="multipart/form-data">
	<fr:edit id="candidacyBean" name="candidacyBean" visible="false" />
	<input type="hidden" id="methodForm" name="method" value="uploadPhoto" />
	<input type="hidden" id="skipValidationId" name="skipValidation" value="false"/>	
	
	<a href="#" onclick="javascript:clearFileInputs();javascript:document.getElementById('skipValidationId').value='true';javascript:document.getElementById('methodForm').value='backToViewCandidacy';javascript:document.getElementById('uploadPhotoForm').submit();">« <bean:message bundle="PHD_RESOURCES" key="label.back"/></a>
<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>

<%--
  CHECK: has candidacy period? 
<logic:equal value="true" name="isApplicationSubmissionPeriodValid">
--%>

<p class="mtop15">
	<em><bean:message key="message.mandatory.fields" bundle="PHD_RESOURCES"/></em><br/>
	<em><bean:message key="message.max.photo.file.size" bundle="PHD_RESOURCES"/></em>
</p>

<logic:notPresent name="candidacyBean">
	<em><bean:message key="label.php.public.candidacy.hash.not.found" bundle="PHD_RESOURCES"/></em>
</logic:notPresent>

<logic:present name="candidacyBean">
		<div class="fs_form">
		<fieldset style="display: block;">
			<fr:edit id="uploadPhotoBean" name="uploadPhotoBean" schema="PhotographUploadBean.upload">		
				<fr:layout name="tabular-editable">
					<fr:property name="classes" value="thlight thleft"/>
					<fr:property name="columnClasses" value="width175px,,tdclear tderror1"/>
					<fr:property name="requiredMarkShown" value="true" />
				</fr:layout>
				<fr:destination name="invalid" path="/applications/epfl/phdProgramCandidacyProcess.do?method=uploadPhotoInvalid" />
			</fr:edit>
		</fieldset>
		</div>	
		<p><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message bundle="PHD_RESOURCES" key="label.add"/></html:submit></p>
</logic:present>
</fr:form>
