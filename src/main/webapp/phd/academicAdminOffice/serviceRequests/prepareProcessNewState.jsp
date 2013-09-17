<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<%@page import="net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituationType"%><html:xhtml/>

<bean:define id="phdIndividualProgramProcess" name="phdIndividualProgramProcess" />
<bean:define id="phdIndividualProgramProcessId" name="phdIndividualProgramProcess" property="externalId" /> 
<bean:define id="phdAcademicServiceRequest" name="phdAcademicServiceRequest" />
<bean:define id="phdAcademicServiceRequestId" name="phdAcademicServiceRequest" property="externalId"/>

<html:link action="/phdIndividualProgramProcess.do?method=viewProcess" paramId="processId" paramName="phdIndividualProgramProcessId">
	<bean:message bundle="PHD_RESOURCES" key="label.back"/>
</html:link>
<br/><br/>

<logic:messagesPresent message="true" property="academicAdminOfficeErrors">
	<div class="error3 mbottom05" style="width: 700px;">
		<html:messages id="messages" message="true" bundle="ACADEMIC_OFFICE_RESOURCES" property="academicAdminOfficeErrors">
			<p class="mvert025"><bean:write name="messages" /></p>
		</html:messages>
	</div>
</logic:messagesPresent>

<fr:view name="phdAcademicServiceRequest" >
	<fr:schema bundle="PHD_RESOURCES" type="net.sourceforge.fenixedu.domain.phd.serviceRequests.PhdAcademicServiceRequest">
		<fr:slot name="serviceRequestNumberYear" key="label.net.sourceforge.fenixedu.domain.phd.serviceRequests.PhdAcademicServiceRequest.serviceRequestNumberYear" />
		<fr:slot name="academicServiceRequestType" key="label.net.sourceforge.fenixedu.domain.phd.serviceRequests.PhdAcademicServiceRequest.academicServiceRequestType" />
		<fr:slot name="requestDate" key="label.net.sourceforge.fenixedu.domain.phd.serviceRequests.PhdAcademicServiceRequest.requestDate" />
		<fr:slot name="activeSituation.academicServiceRequestSituationType" key="label.net.sourceforge.fenixedu.domain.phd.serviceRequests.PhdAcademicServiceRequest.activeSituation" />
		<fr:slot name="creationDate" key="label.net.sourceforge.fenixedu.domain.phd.serviceRequests.PhdAcademicServiceRequest.creationDate" />
	</fr:schema>
	
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight mtop15" />
	</fr:layout>
</fr:view>

<bean:define id="phdAcademicServiceRequest" name="phdAcademicServiceRequestBean" />
<bean:define id="situationType" name="phdAcademicServiceRequestBean" property="situationType" />

<bean:define id="method" value="" />

<logic:equal name="situationType" value="<%= AcademicServiceRequestSituationType.PROCESSING.name() %>" >
	<bean:define id="method" value="process" />
</logic:equal>

<logic:equal name="situationType" value="<%= AcademicServiceRequestSituationType.CANCELLED.name() %>" >
	<bean:define id="method" value="cancel" />
</logic:equal>

<logic:equal name="situationType" value="<%= AcademicServiceRequestSituationType.REJECTED.name() %>" >
	<bean:define id="method" value="reject" />
</logic:equal>

<logic:equal name="situationType" value="<%= AcademicServiceRequestSituationType.CONCLUDED.name() %>" >
	<bean:define id="method" value="conclude" />
</logic:equal>

<logic:equal name="situationType" value="<%= AcademicServiceRequestSituationType.RECEIVED_FROM_EXTERNAL_ENTITY.name() %>" >
	<bean:define id="method" value="receive" />
</logic:equal>

<logic:equal name="situationType" value="<%= AcademicServiceRequestSituationType.DELIVERED.name() %>" >
	<bean:define id="method" value="deliver" />
</logic:equal>

<bean:define id="schemaToUse" value="<%= "PhdAcademicServiceRequest.new.situation-" + situationType %>" />
<fr:form action="<%= String.format("/phdAcademicServiceRequestManagement.do?method=%s&phdAcademicServiceRequestId=%s", method, phdAcademicServiceRequestId) %>">
	<fr:edit id="phd-academic-service-request-bean" name="phdAcademicServiceRequestBean" schema="<%= schemaToUse %>">
			
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight mtop15" />
		</fr:layout>	

		<fr:destination name="cancel" path="<%= "/phdIndividualProgramProcess.do?method=viewProcess&processId=" + phdIndividualProgramProcessId %>" />

	</fr:edit>
		
	<html:submit><bean:message key="label.submit" bundle="PHD_RESOURCES" /></html:submit>
	<html:cancel><bean:message key="label.cancel" bundle="PHD_RESOURCES" /></html:cancel>
</fr:form>
