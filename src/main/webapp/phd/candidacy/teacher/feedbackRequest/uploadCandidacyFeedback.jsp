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

<%@page import="net.sourceforge.fenixedu.domain.phd.PhdProgramProcessDocument"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.PhdProgramDocumentUploadBean"%>
<%@page import="pt.ist.fenixWebFramework.renderers.validators.FileValidator"%>

<html:xhtml/>

<logic:equal name="isTeacher" value="true">

<bean:define id="individualProcessId" name="process" property="individualProgramProcess.externalId" />

<%-- ### Title #### --%>
<h2><bean:message key="label.phd.candidacy.feedback.teacher" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>


<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<html:link action="<%= "/phdIndividualProgramProcess.do?method=viewProcess&processId=" + individualProcessId.toString() %>">
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

<bean:define id="processId" name="process" property="externalId" />

<logic:greaterEqual name="sharedDocuments" value="2">

	<span class="compress">
	<html:link action="/phdCandidacyFeedbackRequest.do?method=candidacyFeedbackDocumentsDownload" paramId="processId" paramName="processId">
		<bean:message key="label.phd.documents.download.all" bundle="PHD_RESOURCES" />
	</html:link>
	</span>

</logic:greaterEqual>

<fr:view name="sharedDocuments">
	<fr:schema bundle="PHD_RESOURCES" type="<%= PhdProgramProcessDocument.class.getName() %>">
		<fr:slot name="documentType" readOnly="true">
			<fr:property name="bundle" value="PHD_RESOURCES" />
		</fr:slot>
		<fr:slot name="this" layout="link" />
	</fr:schema>

	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight mtop15" />
		<fr:property name="sortBy" value="uploadTime=asc"/>
	</fr:layout>
</fr:view>


<logic:notEmpty name="lastFeedbackDocument">
	<br/>
	<strong><bean:message key="label.phd.jury.report.feedback.document.previous" bundle="PHD_RESOURCES" />:</strong>
	<fr:view name="lastFeedbackDocument" layout="link" />
	<br/>
</logic:notEmpty>

<logic:equal name="canUploadDocuments" value="true">
	<fr:form action="<%= "/phdCandidacyFeedbackRequest.do?processId=" + processId.toString() %>" encoding="multipart/form-data">
		<input type="hidden" name="method" />
		
		<br/>
		<strong><bean:message key="label.phd.candidacy.candidacy.review.document" bundle="PHD_RESOURCES" /></strong>
		<fr:edit id="documentBean" name="documentBean" >
			<fr:schema bundle="PHD_RESOURCES" type="<%= PhdProgramDocumentUploadBean.class.getName() %>">
				<fr:slot name="type" readOnly="true" layout="phd-enum-renderer" />
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
			
		</fr:edit>
	
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='uploadCandidacyFeedback';"><bean:message bundle="PHD_RESOURCES" key="label.submit"/></html:submit>
		<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" onclick="this.form.method.value='viewIndividualProgramProcess';"><bean:message bundle="PHD_RESOURCES" key="label.cancel"/></html:cancel>	
	</fr:form>
</logic:equal>

<%--  ### End of Operation Area  ### --%>

</logic:equal>