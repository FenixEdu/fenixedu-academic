<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>


<logic:present name="<%= SessionConstants.INFO_EXAMS_KEY %>"  >

	<bean:message key="property.courses"/>: <br/>
	<logic:iterate id="infoDisciplinaExecucao" name="<%= SessionConstants.INFO_EXAMS_KEY %>" property="infoExecutionCourses" scope="session">
   			<strong><jsp:getProperty name="infoDisciplinaExecucao" property="nome" /></strong>
			<br/>
	</logic:iterate>
	<br/><bean:message key="property.degrees"/>: <br/>
	<logic:iterate id="infoDegree" name="<%= SessionConstants.INFO_EXAMS_KEY %>" property="infoDegrees" scope="session">
			<strong><jsp:getProperty name="infoDegree" property="nome" /></strong>
			<br/>
	</logic:iterate>

</logic:present>		
	