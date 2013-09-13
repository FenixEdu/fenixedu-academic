<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<html:xhtml/>

<bean:define id="phdIndividualProgramProcess" name="phdIndividualProgramProcess" />
<bean:define id="phdIndividualProgramProcessId" name="phdIndividualProgramProcess" property="externalId" /> 

<html:link action="/phdIndividualProgramProcess.do?method=viewProcess" paramId="processId" paramName="phdIndividualProgramProcessId">
	<bean:message bundle="PHD_RESOURCES" key="label.back"/>
</html:link>
<br/><br/>

<strong><bean:message  key="label.phd.process" bundle="PHD_RESOURCES"/></strong>
<fr:view schema="AcademicAdminOffice.PhdIndividualProgramProcess.view" name="phdIndividualProgramProcess">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight mtop15" />
	</fr:layout>
</fr:view>

<p><strong><bean:message key="label.phd.academic.service.requests" bundle="PHD_RESOURCES" /></strong></p>

<logic:empty name="phdIndividualProgramProcess" property="phdAcademicServiceRequests" >
	<p><bean:message key="label.phd.academic.service.requests.empty" bundle="PHD_RESOURCES" /></p>
</logic:empty>

<logic:notEmpty name="phdIndividualProgramProcess" property="phdAcademicServiceRequests" >
	<fr:view name="phdIndividualProgramProcess" property="phdAcademicServiceRequests" >
		<fr:schema bundle="PHD_RESOURCES" type="net.sourceforge.fenixedu.domain.phd.serviceRequests.PhdAcademicServiceRequest">
			<fr:slot name="serviceRequestNumberYear" key="label.net.sourceforge.fenixedu.domain.phd.serviceRequests.PhdAcademicServiceRequest.serviceRequestNumberYear" />
			<fr:slot name="description" key="label.net.sourceforge.fenixedu.domain.phd.serviceRequests.PhdAcademicServiceRequest.academicServiceRequestType" />
			<fr:slot name="requestDate" key="label.net.sourceforge.fenixedu.domain.phd.serviceRequests.PhdAcademicServiceRequest.requestDate" />
			<fr:slot name="activeSituation.academicServiceRequestSituationType" key="label.net.sourceforge.fenixedu.domain.phd.serviceRequests.PhdAcademicServiceRequest.activeSituation" />
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight mtop15" />

			<fr:link name="view" link="/phdAcademicServiceRequestManagement.do?method=viewAcademicServiceRequest&phdAcademicServiceRequestId=${externalId}" label="label.view,PHD_RESOURCES" />
		</fr:layout>
		
	</fr:view>
</logic:notEmpty>

<p>
	<html:link action="/phdAcademicServiceRequestManagement.do?method=prepareCreateNewRequest" paramId="phdIndividualProgramProcessId" paramName="phdIndividualProgramProcessId">
		<bean:message key="label.phd.academic.service.requests.declaration.create" bundle="PHD_RESOURCES" />
	</html:link> |
	<html:link action="/phdDocumentRequestManagement.do?method=prepareCreateNewRequest" paramId="phdIndividualProgramProcessId" paramName="phdIndividualProgramProcessId">
		<bean:message key="label.phd.academic.service.requests.document.create" bundle="PHD_RESOURCES" />
	</html:link>
</p>
