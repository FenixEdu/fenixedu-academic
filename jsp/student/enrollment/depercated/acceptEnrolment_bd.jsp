<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%-- Verificar se isto funciona quando está empty --%>
<center>
<h4><bean:message key="message.successful.enrolment"/></h4>
<html:form action="/curricularCourseEnrolmentManager.do">
	<html:hidden property="method" value="cancel"/>
	<html:submit styleClass="inputbutton"><bean:message key="label.beginning"/></html:submit>
</html:form>
</center>
