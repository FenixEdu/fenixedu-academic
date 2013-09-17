<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<%@page import="net.sourceforge.fenixedu.domain.phd.PhdProgramDocumentUploadBean"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessBean"%>
<%@page import="pt.ist.fenixWebFramework.renderers.validators.FileValidator"%><html:xhtml/>

<bean:define id="processId" name="process" property="externalId" />
<bean:define id="individualProcessId" name="process" property="individualProgramProcess.externalId" />

<%-- ### Title #### --%>
<h2><bean:message key="label.phd.ratify.final.thesis" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<html:link action="<%= "/phdIndividualProgramProcess.do?method=viewProcess&amp;processId=" + individualProcessId.toString() %>">
	<bean:message bundle="PHD_RESOURCES" key="label.back"/>
</html:link>
<br/><br/>
<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>


<%--  ### Context Information (e.g. Person Information, Registration Information)  ### --%>
<strong><bean:message  key="label.phd.process" bundle="PHD_RESOURCES"/></strong>
<fr:view schema="AcademicAdminOffice.PhdIndividualProgramProcess.view" name="process" property="individualProgramProcess">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight mtop15" />
	</fr:layout>
</fr:view>

<%--  ### End Of Context Information  ### --%>

<%--  ### Operation Area (e.g. Create Candidacy)  ### --%>

<fr:form action="<%= "/phdThesisProcess.do?processId=" + processId.toString() %>" encoding="multipart/form-data">
	<input type="hidden" name="method" />
	
	<fr:edit id="thesisProcessBean" name="thesisProcessBean" visible="false" />
	
	<fr:edit id="thesisProcessBean.edit" name="thesisProcessBean">
		<fr:schema bundle="PHD_RESOURCES" type="<%= PhdThesisProcessBean.class.getName() %>">
			<fr:slot name="whenFinalThesisRatified" required="true" />
		</fr:schema>
	
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
		</fr:layout>
	</fr:edit>
	
	<fr:edit id="thesisProcessBean.edit.documents" name="thesisProcessBean" property="documents">
	
		<fr:schema bundle="PHD_RESOURCES" type="<%= PhdProgramDocumentUploadBean.class.getName() %>">
			<fr:slot name="type" readOnly="true" key="label.net.sourceforge.fenixedu.domain.phd.PhdProgramDocumentUploadBean.type" layout="phd-enum-renderer" />
			<fr:slot name="file" key="label.net.sourceforge.fenixedu.domain.phd.PhdProgramDocumentUploadBean.file">
				<fr:validator name="<%= FileValidator.class.getName() %>" />
				<fr:property name="fileNameSlot" value="filename"/>
				<fr:property name="size" value="20"/>
			</fr:slot>
		</fr:schema>
	
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
		</fr:layout>
		
		<fr:destination name="invalid" path="<%= "/phdThesisProcess.do?method=ratifyFinalThesisInvalid&processId=" + processId.toString() %>" />

	</fr:edit>

	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='ratifyFinalThesis';"><bean:message bundle="PHD_RESOURCES" key="label.submit"/></html:submit>
	<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" onclick="this.form.method.value='viewIndividualProgramProcess';"><bean:message bundle="PHD_RESOURCES" key="label.cancel"/></html:cancel>	

</fr:form>

<%--  ### End of Operation Area  ### --%>
