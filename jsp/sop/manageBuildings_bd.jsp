<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<b><bean:message key="link.manage.buildings"/></b>
<br />
<span class="error"><html:errors/></span>
<br />

<html:form action="/manageBuildings">
	<html:hidden property="method" value="createBuilding"/>
	<html:hidden property="page" value="1"/>

	<html:text property="name" size="35"/>

	<html:submit>
		<bean:message key="button.create"/>
	</html:submit>
</html:form>
<br />

<logic:present name="infoBuildings">
	<table>
		<tr>
			<td class="listClasses-header" cellspacing="0" cellpadding="0">
			</td>
			<td class="listClasses-header" cellspacing="0" cellpadding="0">
			</td>
		<tr>
		<logic:iterate id="infoBuilding" name="infoBuildings">
			<bean:define id="buildingId" name="infoBuilding" property="idInternal"/>
			<tr>
				<td class="listClasses" cellspacing="0" cellpadding="0">
					<bean:write name="infoBuilding" property="name"/>
				</td>
				<td class="listClasses-header" cellspacing="0" cellpadding="0">
					<html:link page="<%= "/manageBuildings.do?method=deleteBuilding&amp;buildingId="
            	   				   			+ pageContext.findAttribute("buildingId")
  											%>">
						<bean:message key="link.delete"/>
					</html:link>
				</td>
			<tr>
		</logic:iterate>
	</table>
</logic:present>

<logic:notPresent name="infoBuildings">
	<bean:message key="message.no.buildings"/>
</logic:notPresent>