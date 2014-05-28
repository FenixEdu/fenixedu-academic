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

<%@page import="net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituationType"%><html:xhtml/>

<bean:define id="phdIndividualProgramProcess" name="phdIndividualProgramProcess" />
<bean:define id="phdIndividualProgramProcessId" name="phdIndividualProgramProcess" property="externalId" /> 
<bean:define id="phdAcademicServiceRequest" name="phdAcademicServiceRequest" />
<bean:define id="phdAcademicServiceRequestId" name="phdAcademicServiceRequest" property="externalId" />
<bean:define id="batchOid" name="batchOid" />

<html:link action="/rectorateDocumentSubmission.do?method=viewBatch" paramId="batchOid" paramName="batchOid">
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
<bean:define id="batchOid" name="batchOid" />

<fr:form action="<%= String.format("/phdAcademicServiceRequestManagement.do?method=receiveInRectorate&amp;batchOid=%s&amp;phdAcademicServiceRequestId=%s", batchOid, phdAcademicServiceRequestId) %>">
	<fr:edit id="phd-academic-service-request-bean" name="phdAcademicServiceRequestBean" schema="PhdAcademicServiceRequest.new.situation-RECEIVED_FROM_EXTERNAL_ENTITY">
			
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight mtop15" />
		</fr:layout>	

		<fr:destination name="invalid" path="<%= "/phdAcademicServiceRequestManagement.do?method=processNewStateInvalid&phdAcademicServiceRequestId=" + phdAcademicServiceRequestId %>" />
		<fr:destination name="cancel" path="<%= "/rectorateDocumentSubmission.do?method=viewBatch&batchOid=" + batchOid %>" />

	</fr:edit>
		
	<html:submit><bean:message key="label.submit" bundle="PHD_RESOURCES" /></html:submit>
	<html:cancel><bean:message key="label.cancel" bundle="PHD_RESOURCES" /></html:cancel>
</fr:form>
