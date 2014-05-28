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

<fr:form action="<%= "/phdAcademicServiceRequestManagement.do?method=createNewRequest&phdIndividualProgramProcessId=" + phdIndividualProgramProcessId %>">
	<fr:edit id="phd-academic-service-request-create-bean" name="phdAcademicServiceRequestCreateBean">
		<fr:schema bundle="PHD_RESOURCES" type="net.sourceforge.fenixedu.domain.phd.serviceRequests.PhdAcademicServiceRequestCreateBean">
			<fr:slot name="requestType" required="true" layout="menu-select">
				<fr:property name="providerClass" value="net.sourceforge.fenixedu.domain.phd.serviceRequests.PhdAcademicServiceRequestTypeProvider" />
			</fr:slot>
			<fr:slot name="requestDate" required="true" />
		</fr:schema>
		
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight mtop15" />
		</fr:layout>

		<fr:destination name="invalid" path="/phdAcademicServiceRequestManagement.do?method=createNewRequestInvalid" />
		<fr:destination name="cancel" path="<%= "/phdAcademicServiceRequestManagement.do?method=listAcademicServiceRequests&phdIndividualProgramProcessId=" + phdIndividualProgramProcessId %>" />
	</fr:edit>
	
	<html:submit><bean:message key="label.submit" bundle="PHD_RESOURCES" /></html:submit>
	<html:cancel><bean:message key="label.cancel" bundle="PHD_RESOURCES" /></html:cancel>
</fr:form>
