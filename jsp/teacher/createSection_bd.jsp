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
		<html:textarea rows="2" cols="50" property="name" />
	</td>
</tr>
<tr>
	<td>
		<bean:message key="message.sectionOrder"/>
	</td>
	<td>
		<html:text size="5" property="order"/>
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
</html:form>
</table>