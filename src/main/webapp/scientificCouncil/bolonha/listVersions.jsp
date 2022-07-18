<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@page import="org.fenixedu.academic.service.services.bolonhaManager.CompetenceCourseManagementAccessControl"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<h2><bean:message key="label.list.versions" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></h2>

<ul>
	<li>
		<html:link page="/competenceCourses/manageVersions.do?method=prepare"><bean:message key="label.back" bundle="APPLICATION_RESOURCES"/></html:link>
	</li>
</ul>
<logic:notEmpty name="changeRequests">
	<table class="tstyle2 thlight">
	<tr>
		<th class="nowrap"><bean:message key="label.year" bundle="BOLONHA_MANAGER_RESOURCES"/></th>
<%-- 		<th><bean:message key="label.semester" bundle="BOLONHA_MANAGER_RESOURCES"/></th> --%>
		<th><bean:message key="label.competenceCourse" bundle="BOLONHA_MANAGER_RESOURCES"/></th>
		<th><bean:message key="label.modificationRequestedBy" bundle="BOLONHA_MANAGER_RESOURCES"/></th>
		<th><bean:message key="label.modificationsAnalisedBy" bundle="BOLONHA_MANAGER_RESOURCES"/></th>
		<th><bean:message key="label.status" bundle="BOLONHA_MANAGER_RESOURCES"/></th>
		<th></th>
	</tr>
	
	<logic:iterate id="changeRequest" name="changeRequests" type="org.fenixedu.academic.domain.degreeStructure.CompetenceCourseInformationChangeRequest">
		<% if(CompetenceCourseManagementAccessControl.isLoggedPersonAllowedToApproveChangeRequestsPredicate(changeRequest)) { %>
		<bean:define id="changeRequestID" name="changeRequest" property="externalId"/>
		<bean:define id="competenceCourseID" name="changeRequest" property="competenceCourse.externalId"/>
		<tr>
			<td><fr:view name="changeRequest" property="executionPeriod.executionYear.year"/></td>			
<%-- 			<td class="nowrap"><fr:view name="changeRequest" property="executionPeriod.name"/></td> --%>
			<td><fr:view name="changeRequest" property="competenceCourse.name"/></td>
			<td><fr:view name="changeRequest" property="requester.name"/></td>
			<td class="acenter">
				<fr:view name="changeRequest" property="analizedBy" type="org.fenixedu.academic.domain.Person">
					<fr:layout name="null-as-label">
						<fr:property name="label" value="-"/>
						<fr:property name="subLayout" value="values"/>
						<fr:property name="subSchema" value="responsiblePerson.name"/>
					</fr:layout>
				</fr:view>
			</td>
			<td class="<%= (changeRequest.getApproved() == null ? "draft" : (changeRequest.getApproved() ? "approved" : "rejected")) %>">
				<fr:view name="changeRequest" property="status"/>
			</td>
			<td class="nowrap">			
				<html:link page="<%= "/competenceCourses/manageVersions.do?method=viewVersion&changeRequestID=" + changeRequestID + "&departmentID=" + request.getParameter("departmentID") %>">
					<bean:message key="label.generic.check" bundle="APPLICATION_RESOURCES"/>
				</html:link>
				
				<logic:notPresent name="changeRequest" property="approved">
					,
					<html:link page="<%= "/competenceCourses/manageVersions.do?method=approveRequest&changeRequestID=" + changeRequestID + "&departmentID=" + request.getParameter("departmentID") %>">
						<bean:message key="label.approve" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/>
					</html:link>
					,
					<html:link page="<%= "/competenceCourses/manageVersions.do?method=rejectRequest&changeRequestID=" + changeRequestID + "&departmentID=" + request.getParameter("departmentID") %>">
						<bean:message key="label.reject" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/>
					</html:link>
				</logic:notPresent>		
				
			</td>			
		</tr>
		<% } %>
	</logic:iterate>
	</table>
</logic:notEmpty>

<logic:empty name="changeRequests">
	<p>
		<em><bean:message key="label.no.request.for.department" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></em>
	</p>
</logic:empty>