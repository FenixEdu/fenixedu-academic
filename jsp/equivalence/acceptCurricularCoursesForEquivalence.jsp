<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ page import="org.apache.struts.Globals" %>
<%-- Verificar se isto funciona quando está empty --%>
<center>
<h4><bean:message key="message.successful.equivalence"/></h4>

<bean:define id="path" type="java.lang.String" scope="request" property="path" name="<%= Globals.MAPPING_KEY %>"/>

<html:form action="<%= path %>">
	<html:hidden property="method" value="begin"/>
	<html:submit styleClass="inputbutton"><bean:message key="button.back.to.begining"/></html:submit>
</html:form>
</center>
