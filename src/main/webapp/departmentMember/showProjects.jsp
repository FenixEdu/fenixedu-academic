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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-fenix" prefix="fc"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>

<h2><bean:message key="link.projects" bundle="APPLICATION_RESOURCES"/></h2>
<bean:define name="coursesProjects" id="coursesProjectsMap" type="java.util.Map" scope="request"/>

<logic:iterate id="entry" name="coursesProjectsMap">
	<bean:define id="course" name="entry" property="key"/>
	<bean:define id="courseID" name="course" property="externalId"/>
	<bean:define id="projects" name="entry" property="value"/>
	<div class="mvert25">
		<h3>
			<bean:write name="course" property="name"/>
			<span style="color: gray;">
				<bean:write name="course" property="degreePresentationString"/>
			</span>
		</h3>
		<logic:iterate id="project" name="projects">
			<bean:define id="projectOID" name="project" property="externalId"/>
			<div class='mtop15 mbottom2'>
				<b><bean:write name="project" property="name"/></b>,
				<span class='color888'>
				<bean:message key="label.net.sourceforge.fenixedu.domain.Project.projectBeginDateTime" bundle="APPLICATION_RESOURCES"/>
			</span>
			<bean:write name="project" property="begin" format="dd/MM/yyyy"/>,
			<span class='color888'>
				<bean:message key="label.net.sourceforge.fenixedu.domain.Project.projectEndDateTime" bundle="APPLICATION_RESOURCES"/>
			</span>
			<bean:write name="project" property="end" format="dd/MM/yyyy"/>
			<logic:notEmpty name="project" property="description">
				<p>
					<bean:message key="label.description" bundle="APPLICATION_RESOURCES"/>: 
					<bean:write name="project" property="description"/>
				</p>
			</logic:notEmpty>
			<p>
			<bean:define id="viewProjectURL">
				<%= request.getContextPath() + "/departmentMember/projectSubmissionsManagement.do?method=viewLastProjectSubmissionForEachGroup&amp;executionCourseID=" + courseID + "&amp;projectOID=" + projectOID %>
			</bean:define>
			<html:link href="<%=viewProjectURL%>"> 
				<bean:message key="link.teacher.executionCourseManagement.evaluation.project.viewProjectSubmissions" bundle="APPLICATION_RESOURCES"/>
			</html:link>
			</p>
			</div>		
		</logic:iterate>
	</div>
</logic:iterate>