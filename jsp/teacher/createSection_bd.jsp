<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<span class="error"><html:errors property="error.default" /></span>
<h2><bean:message key="message.createRootSection" /></h2>
<br />
<table>
<html:form action="/createSection">
<html:hidden property="page" value="1"/>	
<tr>
	<td>
		<bean:message key="message.sectionName"/>
	</td>
	<td>
		<html:text property="name" />
			<span class="error"><html:errors property="name"/></span>
	</td>
</tr>
<tr>
	<logic:present name="<%= SessionConstants.CHILDREN_SECTIONS %>">
	<td>		
		<bean:message key="message.sectionOrder"/>		
	</td>
	<td>
		<html:select name="sectionForm" property="sectionOrder">
			<html:option value="-1"><bean:message key="label.end"/></html:option>
			<html:options collection="<%= SessionConstants.CHILDREN_SECTIONS %>" labelProperty="name" property="sectionOrder"/>
			
		</html:select>
		<span class="error"><html:errors property="sectionOrder"/></span>
	</td>
	</logic:present>
	<logic:notPresent name="<%= SessionConstants.CHILDREN_SECTIONS %>">
		<html:hidden property="sectionOrder" value="0"/>
	</logic:notPresent>
</tr>
</table>
<br />
<html:submit styleClass="inputbutton">
<bean:message key="button.save"/>
</html:submit>
<html:reset  styleClass="inputbutton">
<bean:message key="label.clear"/>
</html:reset>			
</html:form>