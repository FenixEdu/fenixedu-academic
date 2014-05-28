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
<%@ page isELIgnored="true"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/academic" prefix="academic" %>

<%@page import="net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituation"%>
<%@page import="net.sourceforge.fenixedu.domain.serviceRequests.SpecialSeasonRequest"%>

<html:xhtml/>

<bean:define id="academicServiceRequest" name="academicServiceRequest" type="net.sourceforge.fenixedu.domain.serviceRequests.RegistrationAcademicServiceRequest"/>

<h2 class="mbottom1"><bean:write name="academicServiceRequest" property="description"/></h2>

<html:messages id="messages" message="true">
	<p><span class="error0"><bean:write name="messages" bundle="ACADEMIC_OFFICE_RESOURCES"/></span></p>
</html:messages>

<p class="mbottom025"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="request.information"/></strong></p>
<bean:define id="simpleClassName" name="academicServiceRequest" property="class.simpleName" />
<fr:view name="academicServiceRequest" schema="<%= simpleClassName + ".view"%>">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thright thlight mtop025"/>
		<fr:property name="rowClasses" value=",tdhl1,,,,,,,,tdhl1"/>
	</fr:layout>
</fr:view>

<academic:allowed operation="SERVICE_REQUESTS_RECTORAL_SENDING" office="<%= academicServiceRequest.getAdministrativeOffice() %>">
	<logic:notEmpty name="academicServiceRequest" property="rectorateSubmissionBatch">
		<bean:define id="rectorateSubmissionBatch" name="academicServiceRequest" property="rectorateSubmissionBatch" />
		
		<p class="mbottom025"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.rectorateSubmission.batch" /></strong></p> 
	
	    <fr:view name="rectorateSubmissionBatch">
			<fr:schema 
				type="net.sourceforge.fenixedu.domain.serviceRequests.RectorateSubmissionBatch"
				bundle="ACADEMIC_OFFICE_RESOURCES">
			    <fr:slot name="diplomaDocumentRequestCount" key="label.rectorateSubmission.documentRequestCount" />
				<fr:slot name="creation" key="label.rectorateSubmission.creation" />
			    <fr:slot name="creator" key="label.rectorateSubmission.creator" layout="null-as-label">
			        <fr:property name="subLayout" value="values" />
					<fr:property name="subSchema" value="responsible.name"/>
			    </fr:slot>
			    <fr:slot name="range" key="label.rectorateSubmission.registryCodeRange" />			
				<fr:slot name="this" layout="link" key="label.rectorateSubmission.batch">
					<fr:property name="key" value="label.view" />
					<fr:property name="bundle" value="APPLICATION_RESOURCES" />
					<fr:property name="linkFormat" value="/rectorateDocumentSubmission.do?batchOid=${externalId}&method=viewBatch" />
					<fr:property name="blankTarget" value="true" />
					<fr:property name="contextRelative" value="true" />
					<fr:property name="moduleRelative" value="true" />
				</fr:slot>
			</fr:schema>
	
	        <fr:layout name="tabular">
	            <fr:property name="classes" value="tstyle4 tdcenter thlight mtop05" />
	            <fr:property name="sortBy" value="creation=desc" />
	            <fr:property name="link(view)" value="/rectorateDocumentSubmission.do?method=viewBatch" />
	            <fr:property name="key(view)" value="link.rectorateSubmission.viewBatch" />
	            <fr:property name="param(view)" value="externalId/batchOid" />
	            <fr:property name="bundle(view)" value="ACADEMIC_OFFICE_RESOURCES" />
	            <fr:property name="target(view)" value="blank" />
	        </fr:layout>
	    </fr:view>
	</logic:notEmpty>
</academic:allowed>

<logic:equal name="academicServiceRequest" property="downloadPossible" value="true">
	<html:link action="/documentRequestsManagement.do?method=downloadDocument" paramId="documentRequestId" paramName="academicServiceRequest" paramProperty="externalId">
		<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.print"/>
	</html:link>
</logic:equal>

<logic:notEmpty name="serviceRequestSituations">
	<p class="mbottom025"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="request.situation"/></strong></p>
	<fr:view name="serviceRequestSituations">

		<fr:schema bundle="ACADEMIC_OFFICE_RESOURCES" type="<%= AcademicServiceRequestSituation.class.getName() %>">
			<fr:slot name="academicServiceRequestSituationType" key="label.net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest.academicServiceRequestSituationType">
			</fr:slot>
			<fr:slot name="finalSituationDate" key="label.date"/>
			<fr:slot name="creator" key="label.net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest.employee.person.name" layout="null-as-label">
				<fr:property name="label" value=" - "/>
				<fr:property name="subLayout" value="values"/>
				<fr:property name="subSchema" value="responsible.name"/>
			</fr:slot>
			<fr:slot name="justification" key="label.net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest.justification" />
		</fr:schema>

		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight" />
		</fr:layout>
	</fr:view>
</schema>

</logic:notEmpty>

<logic:equal name="canRevertToProcessingState" value="true">
	<fr:form action="/academicServiceRequestsManagement.do?method=revertRequestToProcessingState">
		<input type="hidden" name="academicServiceRequestId" value="<%= academicServiceRequest.getExternalId().toString() %>" />
		<input type="hidden" name="registrationID" value="<%= academicServiceRequest.getExternalId().toString() %>" />
		<input type="hidden" name="backAction" value="<%= request.getParameter("backAction") %>" />
		<input type="hidden" name="backMethod" value="<%= request.getParameter("backMethod") %>" />
		
		<html:submit><bean:message  key="label.academicServiceRequest.revert.to.processing.state" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:submit>
	</fr:form>
</logic:equal>


<br/>
<br/>

<bean:define id="url" name="url" type="java.lang.String"/>
<html:form action="<%=url%>">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.academicServiceRequestId" property="academicServiceRequestId" value="<%= academicServiceRequest.getExternalId().toString() %>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.registrationID" property="registrationID" value="<%= academicServiceRequest.getRegistration().getExternalId().toString() %>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.registrationID" property="registrationID" value="<%= academicServiceRequest.getRegistration().getExternalId().toString() %>"/>
	
	<html:submit><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="back" /></html:submit>
</html:form>

