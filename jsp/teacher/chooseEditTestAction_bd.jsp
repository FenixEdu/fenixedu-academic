<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<h2><bean:message key="label.edit"/></h2>

<html:form action="/testEdition">
<html:hidden property="page" value="1"/>
<html:hidden property="method" value="chooseTestAction"/>
<html:hidden property="testCode" value="<%=(pageContext.findAttribute("testCode")).toString()%>"/>
<html:hidden property="objectCode" value="<%=(pageContext.findAttribute("objectCode")).toString()%>"/>
<table>
	<bean:define id="distributedTestListSize" value="<%=(pageContext.findAttribute("distributedTestListSize")).toString()%>"/>
	<% if (!distributedTestListSize.equals("0")) { %>
		<tr>
			<td><b><bean:message key="message.testDistributed"/></b></td>
		</tr>
	<%}%>
	<tr>
		<td><bean:message key="message.chooseEditTestAction" /></td>
	</tr>
</table>
<br/>
<br/>
<html:submit styleClass="inputbutton" property="button"><bean:message key="label.change"/></html:submit>
<html:submit styleClass="inputbutton" property="button"><bean:message key="label.save"/></html:submit>
<html:submit styleClass="inputbutton" property="button"><bean:message key="button.cancel"/></html:submit>
</html:form>
</logic:notEqual>
