<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<%@page import="net.sourceforge.fenixedu.domain.phd.PhdProgramProcessDocument"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.PhdProgramDocumentUploadBean"%>
<%@page import="pt.ist.fenixWebFramework.renderers.validators.FileValidator"%>

<html:xhtml/>

<bean:define id="processId" name="process" property="externalId" />

<span class="compress">
<html:link action="/phdThesisProcess.do?method=downloadThesisDocumentsToFeedback" paramId="processId" paramName="processId">
	<bean:message key="label.phd.documents.download.all" bundle="PHD_RESOURCES" />
</html:link>
</span>

<fr:view name="thesisDocuments">
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


<logic:notEmpty name="thesisProcessBean" property="juryElement.lastFeedbackDocument">
	<br/>
	<strong><bean:message key="label.phd.jury.report.feedback.document.previous" bundle="PHD_RESOURCES" />:</strong>
	<fr:view name="thesisProcessBean" property="juryElement.lastFeedbackDocument" layout="link" />
	<br/>
</logic:notEmpty>

<strong><bean:message key="label.phd.public.document" bundle="PHD_RESOURCES" /></strong>
<logic:equal name="process" property="waitingForJuryReporterFeedback" value="true">
	<br/>
	<fr:form action="<%= "/phdThesisProcess.do?processId=" + processId.toString() %>" encoding="multipart/form-data">
		<input type="hidden" name="method" />
		
		<fr:edit id="thesisProcessBean" name="thesisProcessBean" visible="false" />
		
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
			
		</fr:edit>
	
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='juryReportFeedbackUpload';"><bean:message bundle="PHD_RESOURCES" key="label.submit"/></html:submit>
		<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" onclick="this.form.method.value='viewIndividualProgramProcess';"><bean:message bundle="PHD_RESOURCES" key="label.cancel"/></html:cancel>	
	</fr:form>
</logic:equal>
