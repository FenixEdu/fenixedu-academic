<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<bean:define id="phdIndividualProgramProcess" name="phdIndividualProgramProcess" />
<bean:define id="phdIndividualProgramProcessId" name="phdIndividualProgramProcess" property="externalId" /> 

<html:link action="/phdIndividualProgramProcess.do?method=" paramId="processId" paramName="process" paramProperty="externalId">
	<bean:message bundle="PHD_RESOURCES" key="label.back"/>
</html:link>
<br/><br/>

<strong><bean:message  key="label.phd.process" bundle="PHD_RESOURCES"/></strong>
<fr:view schema="AcademicAdminOffice.PhdIndividualProgramProcess.view" name="phdIndividualProgramProcess">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight mtop15" />
	</fr:layout>
</fr:view>

<strong><bean:message key="label.phd.academic.service.requests" bundle="PHD_RESOURCES" /></strong>

<fr:view name="phdIndividualProgramProcess.phdAcademicServiceRequests" >
	<fr:schema bundle="PHD_RESOURCES" type="net.sourceforge.fenixedu.domain.phd.serviceRequests.PhdAcademicServiceRequest">
		<fr:property name="serviceRequestNumberYear" />
		<fr:property name="when" />
		<fr:property name="academicServiceRequestType.localizedName" />
		<fr:property name="requestDate" />
		<fr:property name="activeSituation" />
	</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight mtop15" />
	</fr:layout>
	
	<fr:link name="view" link="/phdAcademicServiceRequestManagement.do?method=viewAcademicRequest&phdAcademicServiceRequestId=${externalId}" label="link.net.sourceforge.fenixedu.domain.phd.serviceRequests.PhdAcademicServiceRequest.view,PHD_RESOURCES"/>
</fr:view>
