<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<table align="center">
<html:form action="/createSection">
<tr>
	<td>
		<bean:message key="message.sectionName"/>
	</td>
	<td>
		<html:text property="name"/>
	</td>
</tr>
<tr>
	<td>
		<bean:message key="message.sectionOrder"/>
	</td>
	<td>
		<html:text property="order"/>
	</td>
</tr>

<html:reset  styleClass="inputbutton">
	<bean:message key="label.clear"/>
</html:reset>			
    <html:submit styleClass="inputbutton">
		<bean:message key="button.save"/>
	</html:submit>
</html:form>
</table>