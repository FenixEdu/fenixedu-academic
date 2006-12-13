<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">
	<ul>
		<li class="navheader"><bean:message key="link.studentOperations" bundle="ACADEMIC_OFFICE_RESOURCES"/></li>
		<li><html:link page="/createStudent.do?method=prepareCreateStudent"><bean:message key="link.studentOperations.createStudent" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:link></li>
		<li><html:link page="/students.do?method=prepareSearch"><bean:message key="link.studentOperations.viewStudents" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:link></li>
	</ul>

	<ul>
		<li class="navheader"><bean:message key="academic.services" bundle="ACADEMIC_OFFICE_RESOURCES"/></li>
		<li><html:link action="/documentRequestsManagement.do?method=prepareSearch&academicSituationType=NEW"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="new.requests" /></html:link></li>
		<li><html:link action="/documentRequestsManagement.do?method=prepareSearch&academicSituationType=PROCESSING"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="processing.requests" /></html:link></li>
	</ul>

	<ul>
		<li class="navheader"><bean:message key="label.masterDegree.administrativeOffice.contributor" bundle="APPLICATION_RESOURCES"/></li>
		<li><html:link page="/createContributorDispatchAction.do?method=prepare"><bean:message key="link.masterDegree.administrativeOffice.createContributor" bundle="APPLICATION_RESOURCES" /></html:link></li>
		<li><html:link page="/visualizeContributors.do?method=prepare&action=visualize&page=0"><bean:message key="link.masterDegree.administrativeOffice.visualizeContributor" bundle="APPLICATION_RESOURCES" /></html:link></li>
		<li><html:link page="/editContributors.do?method=prepare&action=edit&page=0"><bean:message key="link.masterDegree.administrativeOffice.editContributor" bundle="APPLICATION_RESOURCES" /></html:link></li>
	</ul>
	
	<ul>
		<li class="navheader"><bean:message key="label.payments" bundle="ACADEMIC_OFFICE_RESOURCES"/></li>
		<li><html:link page="/pricesManagement.do?method=viewPrices"><bean:message key="link.pricesManagement" bundle="ACADEMIC_OFFICE_RESOURCES" /></html:link></li>
	</ul>
    
</logic:present>
