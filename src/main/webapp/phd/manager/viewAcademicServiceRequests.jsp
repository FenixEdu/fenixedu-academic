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
