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
	<html:select property="campusID" size="1">
		<html:options collection="campuss" property="idInternal" labelProperty="name"/>
	</html:select>

	<html:submit>
		<bean:message key="button.create"/>
	</html:submit>
</html:form>
<br />

<logic:present name="buildings">
	<table>
		<tr>
			<td class="listClasses-header" cellspacing="0" cellpadding="0">
			</td>
			<td class="listClasses-header" cellspacing="0" cellpadding="0">
			</td>
			<td class="listClasses-header" cellspacing="0" cellpadding="0">
			</td>
		<tr>
		<logic:iterate id="building" name="buildings">
			<bean:define id="buildingId" name="building" property="idInternal"/>
			<tr>
				<td class="listClasses" cellspacing="0" cellpadding="0">
					<bean:write name="building" property="name"/>
				</td>
				<td class="listClasses" cellspacing="0" cellpadding="0">
					<html:form action="/manageBuildings">
						<html:hidden property="method" value="editBuilding"/>
						<html:hidden property="page" value="1"/>
						<html:hidden property="buildingID" value="<%= buildingId.toString() %>"/>

						<bean:define id="campusID" type="java.lang.String"><bean:write name="building" property="campus.idInternal"/></bean:define>
						<html:select property="campusID" size="1" value="<%= campusID %>" onchange="this.form.submit();">
							<html:options collection="campuss" property="idInternal" labelProperty="name"/>
						</html:select>
					</html:form>
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

<logic:notPresent name="buildings">
	<bean:message key="message.no.buildings"/>
</logic:notPresent>