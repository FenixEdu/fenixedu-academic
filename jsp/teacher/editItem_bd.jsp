<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<table align="center">
<html:form action="/editItem">

<tr>
	<td>
		<bean:message key="message.itemInformation"/>
	</td>
	<td>
		<html:text property="information"/>
	</td>
</tr>
<tr>
	<td>
		<bean:message key="message.itemOrder"/>
	</td>
	<td>
		<html:text property="itemOrder"/>
	</td>
</tr>

<tr>
	<td>
		<bean:message key="message.itemName"/>
	</td>
	<td>
		<html:text property="itemName"/>
	</td>
</tr>

<tr>
	<td>
		<bean:message key="message.itemUrgent"/>
	</td>
	<td>
		<html:text property="urgent"/>
	</td>
</tr>


<html:hidden property="method" value="edit" />

<html:reset  styleClass="inputbutton">
	<bean:message key="label.clear"/>
</html:reset>			
    <html:submit styleClass="inputbutton">
		<bean:message key="button.save"/>
	</html:submit>
</html:form>

</table>