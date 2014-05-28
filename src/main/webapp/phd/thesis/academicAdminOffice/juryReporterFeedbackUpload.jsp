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
<%@ page isELIgnored="true"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<%@page import="net.sourceforge.fenixedu.domain.phd.PhdProgramProcessDocument"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.PhdProgramDocumentUploadBean"%>
<%@page import="pt.ist.fenixWebFramework.renderers.validators.FileValidator"%>


<%@page import="net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessBean"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.Action.phd.thesis.providers.PhdJuryReportersProvider"%><html:xhtml/>

<bean:define id="processId" name="process" property="externalId" />

<%-- ### Title #### --%>
<h2><bean:message key="label.phd.thesis.jury.feedback.upload.document" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>


<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<html:link action="<%= "/phdThesisProcess.do?method=viewIndividualProgramProcess&processId=" + processId %>">
	<bean:message bundle="PHD_RESOURCES" key="label.back"/>
</html:link>
<br/><br/>
<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Error Messages  ### --%>
	<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>


<%--  ### Context Information (e.g. Person Information, Registration Information)  ### --%>
	<strong><bean:message  key="label.phd.process" bundle="PHD_RESOURCES"/></strong>
	<fr:view schema="PhdIndividualProgramProcess.view.resume" name="process" property="individualProgramProcess">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight mtop15" />
		</fr:layout>
	</fr:view>
<%--  ### End Of Context Information  ### --%>

<br/>

<%--  ### Operation Area (e.g. Create Candidacy)  ### --%>

	<fr:form action="<%= "/phdThesisProcess.do?method=juryReportFeedbackUpload&processId=" + processId.toString() %>" encoding="multipart/form-data">
		<fr:edit id="thesisProcessBean" name="thesisProcessBean" visible="false" />
		
		<fr:edit name="thesisProcessBean">
			<fr:schema bundle="PHD_RESOURCES" type="<%= PhdThesisProcessBean.class.getName() %>">
				<fr:slot name="juryElement" required="true" layout="menu-select-postback">
					<fr:property name="providerClass" value="<%= PhdJuryReportersProvider.class.getName() %>" />
					<fr:property name="format" value="${nameWithTitle}" />
					<fr:property name="destination" value="postback" />
				</fr:slot>
			</fr:schema>

			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
				<fr:property name="columnClasses" value=",,tdclear tderror1" />
			</fr:layout>
			
			<fr:destination name="postback" path="<%= "/phdThesisProcess.do?method=juryReportFeedbackUploadPostback&processId=" + processId.toString() %>" />
		</fr:edit>
		
		<br />
		
		<logic:notEmpty name="thesisProcessBean" property="juryElement">
			<logic:notEmpty name="thesisProcessBean" property="juryElement.lastFeedbackDocument">
				<strong><bean:message key="label.phd.jury.report.feedback.document.previous" bundle="PHD_RESOURCES" />:</strong>
				<fr:view name="thesisProcessBean" property="juryElement.lastFeedbackDocument" layout="link" />
				<br/>
			</logic:notEmpty>
		</logic:notEmpty>
		
		<strong><bean:message key="label.phd.public.document" bundle="PHD_RESOURCES" /></strong>
	
		<br/>
		
		<fr:edit id="thesisProcessBean.edit.documents" name="thesisProcessBean" property="documents">
			<fr:schema bundle="PHD_RESOURCES" type="<%= PhdProgramDocumentUploadBean.class.getName() %>">
				<fr:slot name="type" readOnly="true">
					<fr:property name="bundle" value="PHD_RESOURCES" />
				</fr:slot>
				<fr:slot name="file" required="true">
					<fr:validator name="<%= FileValidator.class.getName() %>" />
					<fr:property name="fileNameSlot" value="filename"/>
					<fr:property name="size" value="20"/>
				</fr:slot>
			</fr:schema>
		
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
				<fr:property name="columnClasses" value=",,tdclear tderror1" />
			</fr:layout>
			
			<fr:destination name="invalid" path="<%= "/phdThesisProcess.do?method=juryReportFeedbackUploadInvalid&processId=" + processId.toString() %>" />			
			<fr:destination name="cancel" path="<%= "/phdThesisProcess.do?method=viewIndividualProgramProcess&processId=" + processId.toString() %>" />
		</fr:edit>
	
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message bundle="PHD_RESOURCES" key="label.submit"/></html:submit>
		<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel"><bean:message bundle="PHD_RESOURCES" key="label.cancel"/></html:cancel>	
	</fr:form>


<%--  ### End of Operation Area  ### --%>
