<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ page import="java.util.List" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>


<logic:present name="<%= SessionConstants.INFO_EXAMS_KEY %>"  >

	<bean:define id="executionCoursesList" name="<%= SessionConstants.INFO_EXAMS_KEY %>" property="infoExecutionCourses" scope="request"/>
	<bean:define id="degreesList" name="<%= SessionConstants.INFO_EXAMS_KEY %>" property="infoDegrees" scope="request"/>

	<% if (((List) executionCoursesList).size() == 1) { %>
		<bean:message key="property.course"/>
	<% } else {%>
		<bean:message key="property.courses"/>		
	<% } %>: <br/>
	<logic:iterate id="infoDisciplinaExecucao" name="<%= SessionConstants.INFO_EXAMS_KEY %>" property="infoExecutionCourses" scope="request">
   			<strong><jsp:getProperty name="infoDisciplinaExecucao" property="nome" /></strong>
			<br/>
	</logic:iterate>
	<br/>
	<% if (((List) degreesList).size() == 1) { %>
		<bean:message key="property.degree"/>
	<% } else {%>
		<bean:message key="property.degrees"/>
	<% } %>: <br/>	
	<logic:iterate id="infoDegree" name="<%= SessionConstants.INFO_EXAMS_KEY %>" property="infoDegrees" scope="request">
			<strong><jsp:getProperty name="infoDegree" property="tipoCurso" /> em <jsp:getProperty name="infoDegree" property="nome" /></strong>
			<br/>
	</logic:iterate>

</logic:present>		
	