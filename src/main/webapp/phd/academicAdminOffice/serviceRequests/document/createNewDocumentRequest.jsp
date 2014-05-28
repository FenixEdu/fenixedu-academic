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

<%@ page import="net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequestType" %>

<html:xhtml/>

<bean:define id="phdIndividualProgramProcess" name="phdIndividualProgramProcess" />
<bean:define id="phdIndividualProgramProcessId" name="phdIndividualProgramProcess" property="externalId" /> 

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp?viewStateId=phd-academic-service-request-create-bean" />
<%--  ### End of Error Messages  ### --%>


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

<p><strong><bean:message key="label.phd.academic.service.requests.create" bundle="PHD_RESOURCES" /></strong></p>

<bean:define id="phdAcademicServiceRequestCreateBean" name="phdAcademicServiceRequestCreateBean" />

<logic:equal name="phdAcademicServiceRequestCreateBean" property="documentRequestType" value="<%= DocumentRequestType.REGISTRY_DIPLOMA_REQUEST.name() %>">
	<jsp:include page="/phd/academicAdminOffice/serviceRequests/document/diploma/createRegistryDiplomaRequest.jsp" />
</logic:equal>

<logic:equal name="phdAcademicServiceRequestCreateBean" property="documentRequestType" value="<%= DocumentRequestType.DIPLOMA_SUPPLEMENT_REQUEST.name() %>">
	<jsp:include page="/phd/academicAdminOffice/serviceRequests/document/diploma/createDiplomaSupplementRequest.jsp" />
</logic:equal>

<logic:equal name="phdAcademicServiceRequestCreateBean" property="documentRequestType" value="<%= DocumentRequestType.DIPLOMA_REQUEST.name() %>">
	<jsp:include page="/phd/academicAdminOffice/serviceRequests/document/diploma/createDiplomaRequest.jsp" />
</logic:equal>

<logic:equal name="phdAcademicServiceRequestCreateBean" property="documentRequestType" value="<%= DocumentRequestType.PHD_FINALIZATION_CERTIFICATE.name() %>">
	<jsp:include page="/phd/academicAdminOffice/serviceRequests/document/certificate/createPhdFinalizationCertificateRequest.jsp" />
</logic:equal>

<logic:empty name="phdAcademicServiceRequestCreateBean" property="documentRequestType">
	<fr:form action="<%= "/phdDocumentRequestManagement.do?method=createNewRequest&phdIndividualProgramProcessId=" + phdIndividualProgramProcessId %>">
		<fr:edit id="phd-academic-service-request-create-bean" name="phdAcademicServiceRequestCreateBean" visible="false" />
	
		<fr:edit id="phd-academic-service-request-create-bean-choose-document-type" name="phdAcademicServiceRequestCreateBean">
			<fr:schema bundle="PHD_RESOURCES" type="net.sourceforge.fenixedu.domain.phd.serviceRequests.PhdAcademicServiceRequestCreateBean">
				<fr:slot name="documentRequestType" required="true" layout="menu-select-postback">
					<fr:property name="providerClass" value="net.sourceforge.fenixedu.domain.phd.serviceRequests.documentRequests.PhdDocumentRequestTypeProvider" />
					<fr:property name="destination" value="postback" />
				</fr:slot>
			</fr:schema>
			
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight mtop15" />
			</fr:layout>
	
			<fr:destination name="postback" path="<%= "/phdDocumentRequestManagement.do?method=createNewRequestPostback&amp;phdIndividualProgramProcessId=" + phdIndividualProgramProcessId %>" />
		</fr:edit>
	</fr:form>
</logic:empty>
