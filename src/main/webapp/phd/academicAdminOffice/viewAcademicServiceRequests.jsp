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
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/phd" prefix="phd" %>

<%@ page import="net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess" %>

	<%-- Academic Services --%>
	
	<h3 class="mtop25 mbottom05 separator2"><bean:message key="academic.services" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
	<bean:define id="process" name="process" scope="request" type="PhdIndividualProgramProcess"/>
	<p>
		<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.documentRequestsManagement.createDocumentRequest"/>:
		<html:link action="/phdDocumentRequestManagement.do?method=prepareCreateNewRequest" paramId="phdIndividualProgramProcessId" paramName="process" paramProperty="externalId">
			<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="certificates"/>
		</html:link>
		<logic:present name="process" property="registration">
		|
		<html:link action="/academicServiceRequestsManagement.do?method=chooseServiceRequestType" paramId="registrationID" paramName="process" paramProperty="registration.externalId">
			<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.serviceRequests"/>
		</html:link>
		</logic:present>

	</p>
	
	<p class="mtop1">
		<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
		<html:link action="/phdAcademicServiceRequestManagement.do?method=viewHistoric" paramId="phdIndividualProgramProcessId" paramName="process" paramProperty="externalId">
			<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="requests.historic"/>
		</html:link>	
	</p>

        <p class="mtop2">
    		<b><bean:message key="newAcademicServiceRequests.title" bundle="ACADEMIC_OFFICE_RESOURCES"/></b>
            <bean:define id="requests" name="process" property="newAcademicServiceRequests" toScope="request"/>
            
            <logic:notEmpty name="requests">
				<jsp:include page="/phd/academicAdminOffice/serviceRequests/eachAcademicServiceRequest.jsp" />
            </logic:notEmpty>
    		<logic:empty name="requests">
    			<p>
    				<em>
    					<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="newAcademicServiceRequests.empty"/>
    				</em>
    			</p>
    		</logic:empty>
        </p>

        <p class="mtop2">
    		<b><bean:message key="processingAcademicServiceRequests.title" bundle="ACADEMIC_OFFICE_RESOURCES"/></b>
            <bean:define id="requests" name="process" property="processingAcademicServiceRequests" toScope="request"/>
            
            <logic:notEmpty name="requests">
				<jsp:include page="/phd/academicAdminOffice/serviceRequests/eachAcademicServiceRequest.jsp" />
            </logic:notEmpty>
    		<logic:empty name="requests">
    			<p>
    				<em>
    					<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="processingAcademicServiceRequests.empty"/>
    				</em>
    			</p>
    		</logic:empty>
        </p>

        <p class="mtop2">
    		<b><bean:message key="toDeliverAcademicServiceRequests.title" bundle="ACADEMIC_OFFICE_RESOURCES"/></b>
            <bean:define id="requests" name="process" property="toDeliverAcademicServiceRequests" toScope="request" />
            
            <logic:notEmpty name="requests">
				<jsp:include page="/phd/academicAdminOffice/serviceRequests/eachAcademicServiceRequest.jsp" />
            </logic:notEmpty>
    		<logic:empty name="requests">
    			<p>
    				<em>
    					<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="toDeliverAcademicServiceRequests.empty" />
    				</em>
    			</p>
    		</logic:empty>
        </p>
