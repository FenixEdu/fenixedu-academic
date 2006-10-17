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
	    <li><html:link page="/documentRequestsManagement.do?method=showOperations"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="link.documentRequestsManagement" /></html:link></li>
	</ul>

	<ul>
		<li class="navheader"><bean:message key="link.documentsOperations" bundle="ACADEMIC_OFFICE_RESOURCES"/></li>
		<li><html:link action="/documentRequestsManagement.do?method=prepareCreateDocumentRequest"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.documentRequestsManagement.createDocumentRequest" /></html:link></li>
		<li><html:link action="/documentRequestsManagement.do?method=prepareSearch"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.documentRequestsManagement.searchDocumentRequests" /></html:link></li>
	</ul>

</logic:present>
