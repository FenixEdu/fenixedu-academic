<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>

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
	<logic:present name="<%= SessionConstants.CHILDREN_SECTIONS %>">
	<td>		
		<bean:message key="message.sectionOrder"/>		
	</td>
	<td>
		<html:select name="sectionForm" property="sectionOrder">
			<html:options collection="<%= SessionConstants.CHILDREN_SECTIONS %>" property="name"/>
		</html:select>
	</td>
	</logic:present>
	<logic:notPresent name="<%= SessionConstants.CHILDREN_SECTIONS %>">
		<html:hidden property="sectionOrder" value="0"/>
	</logic:notPresent>
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