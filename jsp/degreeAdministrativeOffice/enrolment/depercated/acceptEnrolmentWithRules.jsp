<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%-- Verificar se isto funciona quando esta empty --%>
<center>
<h2 align="center"><bean:message key="title.student.enrolment.with.rules"/></h2>
<h4><bean:message key="message.successful.enrolment" bundle="STUDENT_RESOURCES"/></h4>
<html:form action="/functionRedirect.do">
	<html:hidden property="method" value="chooseStudentAndDegreeTypeForEnrolmentWithRules"/>
	<html:submit styleClass="inputbutton"><bean:message key="button.back.to.begining"/></html:submit>
</html:form>
</center>
