<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<table align="center">
<html:form action="/editSection">
<tr>
	<td>
		<bean:message key="message.sectionName"/>
	</td>
	<td>
		<html:textarea rows="2" cols="50" name="<%=SessionConstants.INFO_SECTION %>" property="name" />
	</td>
</tr>
<tr>
	<td>
		<bean:message key="message.sectionOrder"/>
	</td>
	<td>
		<html:text size="5" name="<%=SessionConstants.INFO_SECTION %>" property="sectionOrder"/>
	</td>
</tr>

	<html:reset  styleClass="inputbutton">
		<bean:message key="label.clear"/>
	</html:reset>			
    <html:submit styleClass="inputbutton">
		<bean:message key="button.save"/>
	</html:submit>
	<html:hidden property="method" value="edit" />	
</html:form>
</table>