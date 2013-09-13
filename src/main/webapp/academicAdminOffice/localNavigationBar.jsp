<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@page import="org.joda.time.LocalDate"%>
<%@page import="pt.ist.fenixWebFramework.security.UserView"%><html:xhtml/>

<%@page import="net.sourceforge.fenixedu.injectionCode.AccessControl" %>

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

	<ul>	
		<li class="navheader"><bean:message key="link.studentOperations" bundle="ACADEMIC_OFFICE_RESOURCES"/></li>
		<li><html:link page="/createStudent.do?method=prepareCreateStudent"><bean:message key="link.studentOperations.createStudent" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:link></li>
	</ul>
	
	<logic:equal name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" property="person.employee.unitCoordinator" value="true">
		<ul>
			<li class="navheader"><bean:message key="label.registeredDegreeCandidacies.first.time.student.registration" bundle="ACADEMIC_OFFICE_RESOURCES"/></li>
			<li><html:link page="/registeredDegreeCandidacies.do?method=view"><bean:message key="label.registeredDegreeCandidacies.first.time.list" bundle="ACADEMIC_OFFICE_RESOURCES" /></html:link></li>
		</ul>
	</logic:equal>

	<logic:equal name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" property="person.employee.unitCoordinator" value="true">
		<ul>
			<li class="navheader"><bean:message key="title.event.reports" bundle="ACADEMIC_OFFICE_RESOURCES"/></li>
			<li><html:link page="/eventReports.do?method=listReports"><bean:message key="title.event.reports" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:link></li>
		</ul>
	</logic:equal>

</logic:present>
