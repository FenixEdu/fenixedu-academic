<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<h2><bean:message key="title.removeTest"/></h2>

<html:form action="/testsManagement">
<html:hidden property="page" value="1"/>
<html:hidden property="method" value="chooseTestAction"/>
<html:hidden property="testCode" value="<%=(pageContext.findAttribute("testCode")).toString()%>"/>
<html:hidden property="objectCode" value="<%=(pageContext.findAttribute("objectCode")).toString()%>"/>
<table>
	<tr>
		<td><bean:message key="message.chooseDeleteTestAction" /></td>
	</tr>
</table>
<br/>
<br/>
<html:submit styleClass="inputbutton" property="button"><bean:message key="label.yes"/></html:submit>
<html:submit styleClass="inputbutton" property="button"><bean:message key="label.no"/></html:submit>
</html:form>
</logic:notEqual>
