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

<bean:define id="academicServiceRequest" name="academicServiceRequest" />
<bean:define id="academicServiceRequestId" name="academicServiceRequestId" />

<fr:view name="academicServiceRequest" >
	<fr:schema bundle="PHD_RESOURCES" type="net.sourceforge.fenixedu.domain.phd.serviceRequests.PhdAcademicServiceRequest">
		<fr:property name="serviceRequestNumberYear" />
		<fr:property name="academicServiceRequestType.localizedName" />
		<fr:property name="requestDate" />
		<fr:property name="activeSituationType" />
		<fr:property name="employee" />
		<fr:property name="creationDate" />
	</fr:schema>
	
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight mtop15" />
	</fr:layout>
</fr:view>

<logic:notEmpty name="academicServiceRequest" property="processingSituationAccepted">
	<html:link action="/phdAcademicServiceRequestManagement.do?method=prepareProcessServiceRequest" paramId="academicServiceRequestId" paramName="academicServiceRequestId">
		<bean:message key="link.net.sourceforge.fenixedu.domain.phd.serviceRequests.PhdAcademicServiceRequest.process" bundle="PHD_RESOURCES" />
	</html:link>
</logic:notEmpty>

<logic:notEmpty name="academicServiceRequest" property="cancelledSituationAccepted">
	<html:link action="/phdAcademicServiceRequestManagement.do?method=prepareCancelServiceRequest" paramId="academicServiceRequestId" paramName="academicServiceRequestId">
		<bean:message key="link.net.sourceforge.fenixedu.domain.phd.serviceRequests.PhdAcademicServiceRequest.cancel" bundle="PHD_RESOURCES" />
	</html:link>
</logic:notEmpty>

<logic:notEmpty name="academicServiceRequest" property="rejectedSituationAccepted">
	<html:link action="/phdAcademicServiceRequestManagement.do?method=prepareRejectServiceRequest" paramId="academicServiceRequestId" paramName="academicServiceRequestId">
		<bean:message key="link.net.sourceforge.fenixedu.domain.phd.serviceRequests.PhdAcademicServiceRequest.reject" bundle="PHD_RESOURCES" />
	</html:link>
</logic:notEmpty>

<logic:notEmpty name="academicServiceRequest" property="concludedSituationAccepted">
	<html:link action="/phdAcademicServiceRequestManagement.do?method=prepareConcludeServiceRequest" paramId="academicServiceRequestId" paramName="academicServiceRequestId">
		<bean:message key="link.net.sourceforge.fenixedu.domain.phd.serviceRequests.PhdAcademicServiceRequest.conclude" bundle="PHD_RESOURCES" />
	</html:link>
</logic:notEmpty>

