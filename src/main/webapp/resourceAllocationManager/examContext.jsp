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
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ page import="java.util.List" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>


<logic:present name="<%= PresentationConstants.INFO_EXAMS_KEY %>"  >

	<bean:define id="executionCoursesList" name="<%= PresentationConstants.INFO_EXAMS_KEY %>" property="infoExecutionCourses" scope="request"/>
	<bean:define id="degreesList" name="<%= PresentationConstants.INFO_EXAMS_KEY %>" property="infoDegrees" scope="request"/>

	<% if (((List) executionCoursesList).size() == 1) { %>
		<bean:message key="property.course"/>
	<% } else {%>
		<bean:message key="property.courses"/>		
	<% } %>: <br/>
	<logic:iterate id="infoDisciplinaExecucao" name="<%= PresentationConstants.INFO_EXAMS_KEY %>" property="infoExecutionCourses" scope="request">
   			<strong><jsp:getProperty name="infoDisciplinaExecucao" property="nome" /></strong>
			<br/>
	</logic:iterate>
	<br/>
	<% if (((List) degreesList).size() == 1) { %>
		<bean:message key="property.degree"/>
	<% } else {%>
		<bean:message key="property.degrees"/>
	<% } %>: <br/>	
	<logic:iterate id="infoDegree" name="<%= PresentationConstants.INFO_EXAMS_KEY %>" property="infoDegrees" scope="request">
			<strong><jsp:getProperty name="infoDegree" property="degree.presentationName" /></strong>
			<br/>
	</logic:iterate>

</logic:present>		
	