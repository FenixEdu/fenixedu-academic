<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<html:xhtml/>

<bean:define id="phdIndividualProgramProcess" name="phdIndividualProgramProcess" />
<bean:define id="phdIndividualProgramProcessId" name="phdIndividualProgramProcess" property="externalId" /> 
<fr:form action="<%= "/phdDocumentRequestManagement.do?method=createNewRequest&phdIndividualProgramProcessId=" + phdIndividualProgramProcessId %>">
	<fr:edit id="phd-academic-service-request-create-bean" name="phdAcademicServiceRequestCreateBean" visible="false" />

	<fr:edit id="phd-academic-service-request-create-bean-choose-document-type" name="phdAcademicServiceRequestCreateBean">
		<fr:schema bundle="PHD_RESOURCES" type="net.sourceforge.fenixedu.domain.phd.serviceRequests.PhdDocumentRequestCreateBean">
			<fr:slot name="documentRequestType" required="true" layout="menu-select-postback">
				<fr:property name="providerClass" value="net.sourceforge.fenixedu.domain.phd.serviceRequests.documentRequests.PhdDocumentRequestTypeProvider" />
				<fr:property name="destination" value="postback" />
			</fr:slot>
			<fr:slot name="freeProcessed" />
			<fr:slot name="requestDate" required="true" />
		</fr:schema>
		
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight mtop15" />
		</fr:layout>

		<fr:destination name="postback" path="<%= "/phdDocumentRequestManagement.do?method=createNewRequestPostback&amp;phdIndividualProgramProcessId=" + phdIndividualProgramProcessId %>" />
		<fr:destination name="invalid" path="/phdAcademicServiceRequestManagement.do?method=createNewRequestInvalid" />
		<fr:destination name="cancel" path="<%= "/phdIndividualProgramProcess.do?method=viewProcess&processId=" + phdIndividualProgramProcessId %>" />
	</fr:edit>
	
	<html:submit><bean:message key="label.submit" bundle="PHD_RESOURCES" /></html:submit>
	<html:cancel><bean:message key="label.cancel" bundle="PHD_RESOURCES" /></html:cancel>
</fr:form>
