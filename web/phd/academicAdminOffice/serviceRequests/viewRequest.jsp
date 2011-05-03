<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<%@page import="net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituationType"%><html:xhtml/>

<bean:define id="phdIndividualProgramProcess" name="phdIndividualProgramProcess" />
<bean:define id="phdIndividualProgramProcessId" name="phdIndividualProgramProcess" property="externalId" /> 

<html:link action="/phdAcademicServiceRequestManagement.do?method=listAcademicServiceRequests" paramId="phdIndividualProgramProcessId" paramName="phdIndividualProgramProcessId">
	<bean:message bundle="PHD_RESOURCES" key="label.back"/>
</html:link>
<br/><br/>

<bean:define id="phdAcademicServiceRequest" name="phdAcademicServiceRequest" />
<bean:define id="phdAcademicServiceRequestId" name="phdAcademicServiceRequest" property="externalId" />

<fr:view name="phdAcademicServiceRequest" >
	<fr:schema bundle="PHD_RESOURCES" type="net.sourceforge.fenixedu.domain.phd.serviceRequests.PhdAcademicServiceRequest">
		<fr:slot name="serviceRequestNumberYear" key="label.net.sourceforge.fenixedu.domain.phd.serviceRequests.PhdAcademicServiceRequest.serviceRequestNumberYear"/>
		<fr:slot name="academicServiceRequestType" key="label.net.sourceforge.fenixedu.domain.phd.serviceRequests.PhdAcademicServiceRequest.academicServiceRequestType" />
		<fr:slot name="requestDate" key="label.net.sourceforge.fenixedu.domain.phd.serviceRequests.PhdAcademicServiceRequest.requestDate" />
		<fr:slot name="activeSituation.academicServiceRequestSituationType" key="label.net.sourceforge.fenixedu.domain.phd.serviceRequests.PhdAcademicServiceRequest.activeSituation" />
		<fr:slot name="creationDate" key="label.net.sourceforge.fenixedu.domain.phd.serviceRequests.PhdAcademicServiceRequest.creationDate" />
	</fr:schema>
	
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight mtop15" />
	</fr:layout>
</fr:view>

<logic:notEmpty name="phdAcademicServiceRequest" property="lastGeneratedDocument">
	<html:link action="/phdAcademicServiceRequestManagement.do?method=downloadLastGeneratedDocument" paramId="phdAcademicServiceRequestId" paramName="phdAcademicServiceRequestId">
		Download document
	</html:link>
</logic:notEmpty> 

<logic:equal name="phdAcademicServiceRequest" property="processingSituationAccepted" value="true">
<p>
	<html:link action="/phdAcademicServiceRequestManagement.do?method=prepareProcessServiceRequest" paramId="phdAcademicServiceRequestId" paramName="phdAcademicServiceRequestId">
		<bean:message key="link.net.sourceforge.fenixedu.domain.phd.serviceRequests.PhdAcademicServiceRequest.process" bundle="PHD_RESOURCES" />
	</html:link>
</p>
</logic:equal>

<logic:equal name="phdAcademicServiceRequest" property="cancelledSituationAccepted" value="true">
<p>
	<html:link action="/phdAcademicServiceRequestManagement.do?method=prepareCancelServiceRequest" paramId="phdAcademicServiceRequestId" paramName="phdAcademicServiceRequestId">
		<bean:message key="link.net.sourceforge.fenixedu.domain.phd.serviceRequests.PhdAcademicServiceRequest.cancel" bundle="PHD_RESOURCES" />
	</html:link>
</p>
</logic:equal>

<logic:equal name="phdAcademicServiceRequest" property="rejectedSituationAccepted" value="true">
<p>
	<html:link action="/phdAcademicServiceRequestManagement.do?method=prepareRejectServiceRequest" paramId="phdAcademicServiceRequestId" paramName="phdAcademicServiceRequestId">
		<bean:message key="link.net.sourceforge.fenixedu.domain.phd.serviceRequests.PhdAcademicServiceRequest.reject" bundle="PHD_RESOURCES" />
	</html:link>
</p>
</logic:equal>

<logic:equal name="phdAcademicServiceRequest" property="concludedSituationAccepted" value="true">
<p>
	<html:link action="/phdAcademicServiceRequestManagement.do?method=prepareConcludeServiceRequest" paramId="phdAcademicServiceRequestId" paramName="phdAcademicServiceRequestId">
		<bean:message key="link.net.sourceforge.fenixedu.domain.phd.serviceRequests.PhdAcademicServiceRequest.conclude" bundle="PHD_RESOURCES" />
	</html:link>
</p>
</logic:equal>
