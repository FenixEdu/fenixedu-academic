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
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>

<logic:present name="<%= PresentationConstants.MASTER_DEGREE %>"  >
	<bean:define id="infoExecutionDegree" name="<%= PresentationConstants.MASTER_DEGREE %>"/>
	<bean:define id="infoExecutionDegreeID" name="infoExecutionDegree" property="externalId"/>
	<bean:define id="degreeCurricularPlanID" name="degreeCurricularPlanID" scope="request"/>
	<br/>
	<ul>
		<li class="navheader">
			<bean:message key="label.coordinator.studentInformation"/>
		</li>
		<li>
			<html:link page="<%= "/viewStudentCurriculum.do?method=prepare&amp;executionDegreeId=" + infoExecutionDegreeID.toString() + "&degreeCurricularPlanID=" + degreeCurricularPlanID %>">
			    <bean:message key="link.student.curriculum" /></html:link>
		</li>
	</ul>
</logic:present>
