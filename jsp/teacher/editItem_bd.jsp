<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>

<logic:present name="<%= SessionConstants.INFO_ITEM %>" >	
<bean:define id="item" name="<%= SessionConstants.INFO_ITEM %>" />
</logic:present>	
<table align="center">
<html:form action="/editItem">
<tr>
	<td>
		<bean:message key="message.itemName"/>
	</td>
	<td>
		<html:text name="item" property="name"/>
	</td>
</tr>

<tr>
	<td>
		<bean:message key="message.sectionOrder"/>		
	</td>
	<td>
		<html:select property="itemOrder" size="1">
			<bean:define id="items" name="<%= SessionConstants.INFO_SECTION_ITEMS_LIST %>" />
			<html:options collection="items" labelProperty="name" property="itemOrder" />
			<html:option value="-1">(fim)</html:option>
		</html:select>
		
	</td>
</tr>



<tr>
	<td>
		<bean:message key="message.itemUrgent"/>
	</td>
	<td>
		<html:select property="urgent" size="1" >
				
				<html:option value="false">não</html:option>
				<html:option value="true">sim</html:option>
		</html:select>
	</td>
</tr>
<tr>
	<td>
		<bean:message key="message.itemInformation"/>
	</td>
	<td>
		<html:textarea name="item" property="information" rows="8" cols="50"/>
	</td>
</tr>

<html:hidden property="method" value="edit" />
<tr><td> <html:submit styleClass="inputbutton">
		<bean:message key="button.save"/>
	</html:submit>
	</td>
	<td><html:reset  styleClass="inputbutton">
	<bean:message key="label.clear"/>
</html:reset>
	</td>
	</tr>
			
   
</html:form>

</table>