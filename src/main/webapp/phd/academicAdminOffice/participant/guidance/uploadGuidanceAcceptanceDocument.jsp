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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<html:xhtml/>

<%-- ### Title #### --%>
<h2><bean:message key="title.phd.guidance.acceptance.letter.upload" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>

<bean:define id="processId" name="process" property="externalId" />
<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<html:link action="<%= "/phdIndividualProgramProcess.do?method=prepareManageGuidingInformation&amp;processId=" + processId %>">
	<bean:message key="label.back" bundle="PHD_RESOURCES" />
</html:link>
<br/><br/>

<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>

<%--  ### Context Information (e.g. Person Information, Registration Information)  ### --%>
<strong><bean:message  key="label.phd.process" bundle="PHD_RESOURCES"/></strong>
<fr:view schema="PhdIndividualProgramProcess.view.simple" name="process">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight mtop15" />
	</fr:layout>
</fr:view>
<%--  ### End Of Context Information  ### --%>


<%--  ### Operation Area (e.g. Create Candidacy)  ### --%>

<bean:define id="participant" name="guidingBean" property="participant" />
<bean:define id="guidingId" name="participant" property="externalId" />

<fr:form action="<%= String.format("/phdIndividualProgramProcess.do?method=uploadGuidanceAcceptanceLetter&amp;guidingId=%s&amp;processId=%s", guidingId, processId) %>" encoding="multipart/form-data" >
	<fr:edit id="guidingBean" name="guidingBean" visible="false" />
	
	<fr:edit id="guidingBean-upload" name="guidingBean">
		<fr:schema type="org.fenixedu.academic.domain.phd.PhdParticipantBean" bundle="PHD_RESOURCES" >
			<fr:slot name="guidingAcceptanceLetter.file" key="PhdIndividualProgramDocumentType.GUIDER_ACCEPTANCE_LETTER">
				<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator" />
				<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.FileValidator">
					<fr:property name="required" value="true" />
					<fr:property name="maxSize" value="2mb" />
					<fr:property name="acceptedExtensions" value="pdf" />
					<fr:property name="acceptedTypes" value="application/pdf" />
				</fr:validator>
				<fr:property name="fileNameSlot" value="guidingAcceptanceLetter.filename"/>
				<fr:property name="size" value="20"/>
			</fr:slot>				
		</fr:schema>
		
		<fr:destination name="invalid" path="<%= String.format("/phdIndividualProgramProcess.do?method=uploadGuidanceAcceptanceLetterInvalid&amp;guidingId=%s&amp;processId=%s", guidingId, processId) %>" />
		<fr:destination name="cancel" path="<%= String.format("/phdIndividualProgramProcess.do?method=prepareManageGuidingInformation&amp;processId=%s", processId) %>" />
	</fr:edit>
	
	<html:submit><bean:message key="button.submit" bundle="APPLICATION_RESOURCES" /></html:submit>
	<html:cancel><bean:message key="button.cancel" bundle="APPLICATION_RESOURCES" /></html:cancel>

</fr:form>
