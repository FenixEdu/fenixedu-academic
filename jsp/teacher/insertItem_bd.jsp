<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<table align="center">
<html:form action="/insertItem">

	<td>
		<bean:message key="message.itemName"/>
	</td>
	<td>
		<html:textarea rows="2" cols="50" property="itemName" />
	</td>
</tr>
<tr>
	<td>
		<bean:message key="message.itemOrder"/>
	</td>
	<td>
		<html:text size="5" property="itemOrder"/>
	</td>
</tr>
<tr>
	<td>
		<bean:message key="message.itemInformation"/>
	</td>
	<td>
		<html:textarea rows="2" cols="50" property="information"/>
	</td>
</tr>
<tr>
	<td>
		<bean:message key="message.itemUrgent"/>
	</td>
	<td>
		<html:text size="5" property="urgent"/>
	</td>
</tr>
<tr>
<td>
	<html:reset  styleClass="inputbutton">
		<bean:message key="label.clear"/>
	</html:reset>			
</td>
<td>
	<html:submit styleClass="inputbutton">
		<bean:message key="button.save"/>
	</html:submit>
</td>
</tr>

<html:hidden property="method" value="insert" />
</table>
</html:form>
