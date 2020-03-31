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
<%@page import="java.util.Collection"%>
<%@page import="java.util.stream.Collectors"%>
<%@page import="org.fenixedu.academic.domain.degreeStructure.CompetenceCourseInformationChangeRequest"%>
<%@page import="org.fenixedu.academic.service.services.bolonhaManager.CompetenceCourseManagementAccessControl"%>
<%@page import="org.fenixedu.academic.domain.CompetenceCourse"%>
<%@ page isELIgnored="true"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>

<h2><bean:message key="label.version.manage" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></h2>

<table class="tstyle1 thlight table">
	<tr>
		<th><bean:message key="label.name" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></th>
		<th><bean:message key="label.draftCompetenceCourseInformationChangeRequestsCount" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></th>
		<th><bean:message key="label.competenceCourseInformationChangeRequestsCount" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></th>
	</tr>
	<c:forEach items="${departments}" var="department">
		<bean:define id="department" name="department" type="org.fenixedu.academic.domain.Department"/>
		<%
        int draftChangeRequestsCount = 0;
    	int changeRequestsCount = 0;
    	if(department.getDepartmentUnit() != null){
    	    final Collection<CompetenceCourse> courses = CompetenceCourse.findByUnit(department.getDepartmentUnit(), true).collect(Collectors.toSet());
	        for (CompetenceCourse course : courses) {
	            for (CompetenceCourseInformationChangeRequest changeRequest : course.getCompetenceCourseInformationChangeRequestsSet()) {
	                if (changeRequest.getApproved() == null && CompetenceCourseManagementAccessControl.isLoggedPersonAllowedToApproveChangeRequestsPredicate(changeRequest)) {
	                    draftChangeRequestsCount++;
	                }
	                if (CompetenceCourseManagementAccessControl.isLoggedPersonAllowedToApproveChangeRequestsPredicate(changeRequest)) {
			            changeRequestsCount++;
	                }
	            }            
	        }
    	}
		%>
		<tr>
			<td>
				<html:link action="competenceCourses/manageVersions.do?method=displayRequest" paramId="departmentID" paramName="department" paramProperty="externalId">
					<c:out value="${department.name}"/>
				</html:link>
			</td>
			<td class="acenter"><%= draftChangeRequestsCount %></td>
			<td class="acenter"><%= changeRequestsCount %></td>
		</tr>		
	</c:forEach>
</table>
