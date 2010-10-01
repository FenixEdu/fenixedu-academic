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

<bean:define id="pdhAcademicServiceRequest" name="phdAcademicServiceRequest" />
<bean:define id="phdAcademicServiceRequestId" name="pdhAcademicServiceRequestId" />

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

<bean:define id="phdAcademicServiceRequest" name="phdAcademicServiceRequestBean" />
<bean:define id="situationType" name="phdAcademicServiceRequestBean" property="situationType" />


<bean:define id="method" value="" />
<bean:define id="actionName" value="" />

<logic:equal name="situationType" value="<%= AcademicServiceRequestSituationType.PROCESSING %>" >
	<bean:define id="actionName" value="link.net.sourceforge.fenixedu.domain.phd.serviceRequests.PhdAcademicServiceRequest.process" />
	<bean:define id="method" value="processServiceRequest" />
</logic:equal>

<logic:equal name="situationType" value="<%= AcademicServiceRequestSituationType.CANCELLED %>" >
	<bean:define id="actionName" value="link.net.sourceforge.fenixedu.domain.phd.serviceRequests.PhdAcademicServiceRequest.cancel" />
	<bean:define id="method" value="cancelServiceRequest" />
</logic:equal>

<logic:equal name="situationType" value="<%= AcademicServiceRequestSituationType.REJECTED %>" >
	<bean:define id="actionName" value="link.net.sourceforge.fenixedu.domain.phd.serviceRequests.PhdAcademicServiceRequest.reject" />
	<bean:define id="method" value="rejectServiceRequest" />
</logic:equal>

<logic:equal name="situationType" value="<%= AcademicServiceRequestSituationType.CONCLUDED %>" >
	<bean:define id="actionName" value="link.net.sourceforge.fenixedu.domain.phd.serviceRequests.PhdAcademicServiceRequest.conclude" />
	<bean:define id="method" value="concludeServiceRequest" />
</logic:equal>

<fr:form action="<%= "/phdAcademicServiceRequestManagement.do?method=" + method %>">
	<fr:schema bundle="PHD_RESOURCES" type="net.sourceforge.fenixedu.domain.phd.serviceRequests.PhdAcademicServiceRequestBean">
		<fr:slot name="justification" required="true"/>
		<fr:slot name="whenNewSituationOccured" />
	</fr:schema>
	<fr:edit name="phdAcademicServiceRequestBean">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight mtop15" />
		</fr:layout>	
	</fr:edit>
	
	<fr:destination name="invalid" path="/phdAcademicServiceRequestManagement.do?method=processNewStateInvalid" />
	<fr:destination name="cancel" path="<%= "/phdAcademicServiceRequestManagement.do?method=viewRequest&phdAcademicServiceRequestId=" + phdAcademicServiceRequestId %>"/>
	
	<html:submit><bean:message key="<%= actionName %>" bundle="PHD_RESOURCES" /></html:submit>
	<html:cancel><bean:message key="label.cancel" bundle="PHD_RESOURCES" /></html:cancel>
</fr:form>
