<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%-- Verificar se isto funciona quando está empty --%>
<center>
<h4><bean:message key="message.successful.enrolment"/></h4>
<html:form action="/setStartMode.do">
	<html:hidden property="method" value="withoutRules"/>
	<html:submit styleClass="inputbutton"><bean:message key="button.back.to.begining"/></html:submit>
</html:form>
</center>
