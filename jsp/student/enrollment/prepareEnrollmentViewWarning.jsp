<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<div  align="center" >
	<span class="error"><html:errors/></span>
	<br />
	<h2 style="text-align:center">
		<bean:message key="title.student.shift.enrollment" />
	</h2>
	<p align="left">
		<bean:message key="message.warning.student.enrolmentClasses" />
	</p>
	<html:form action="/studentShiftEnrolmentManager" >
		<html:hidden property="method" value="start" />
		<html:submit styleClass="inputbutton">
			<bean:message key="button.continue.enrolment"/>
		</html:submit>
	</html:form>
</div>
