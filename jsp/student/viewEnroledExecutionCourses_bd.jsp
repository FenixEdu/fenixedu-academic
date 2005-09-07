<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>	

<logic:present name="infoExecutionCourses"> 

<html:form action="/viewExecutionCourseProjects" method="get">
	<h2><bean:message key="link.title.groupEnrolment"/></h2>
	<logic:empty name="infoExecutionCourses">
		<strong><bean:message key="message.executionCourses.not.available"/></strong>
		<p class="error"><html:errors/></p>
	</logic:empty>

	<logic:notEmpty name="infoExecutionCourses">
		<p class="infoop"><bean:message key="label.student.viewEnroledExecutionCourses.description" /></p>
		<span class="error"><html:errors/></span>
		<br />
 		<p><strong><bean:message key="title.ChooseExecutionCourse"/>:</strong></p>
		<html:select property="executionCourseCode" size="1">
    	<html:options collection="infoExecutionCourses" property="value" labelProperty="label"/>
    	</html:select>	
		<br />
		<br />
		<p><html:submit styleClass="inputbutton"><bean:message key="button.choose"/></html:submit></p>
	</logic:notEmpty>
</html:form>	

</logic:present>


<logic:notPresent name="infoExecutionCourses">
	
	<h2><bean:message key="message.executionCourses.not.available"/></h2>
	<br />
	<span class="error"><html:errors/></span>
	<br />
	
</logic:notPresent>



   
