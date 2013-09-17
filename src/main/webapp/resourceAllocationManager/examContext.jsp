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
	