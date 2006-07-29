<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<b><bean:message key="link.manage.buildings"/></b>
<br />
<span class="error"><!-- Error messages go here --><html:errors /></span>
<br />

<html:form action="/manageBuildings">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="createBuilding"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>

	<html:text bundle="HTMLALT_RESOURCES" altKey="text.name" property="name" size="35"/>
	<html:select bundle="HTMLALT_RESOURCES" altKey="select.campusID" property="campusID" size="1">
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
			<th class="listClasses-header" cellspacing="0" cellpadding="0">
			</th>
			<th class="listClasses-header" cellspacing="0" cellpadding="0">
			</th>
			<th class="listClasses-header" cellspacing="0" cellpadding="0">
			</th>
		<tr>
		<logic:iterate id="building" name="buildings">
			<bean:define id="buildingId" name="building" property="idInternal"/>
			<tr>
				<td class="listClasses" cellspacing="0" cellpadding="0">
					<bean:write name="building" property="name"/>
				</td>
				<td class="listClasses" cellspacing="0" cellpadding="0">
					<html:form action="/manageBuildings">
						<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="editBuilding"/>
						<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
						<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.buildingID" property="buildingID" value="<%= buildingId.toString() %>"/>

						<bean:define id="campusID" type="java.lang.String"><bean:write name="building" property="campus.idInternal"/></bean:define>
						<html:select bundle="HTMLALT_RESOURCES" altKey="select.campusID" property="campusID" size="1" value="<%= campusID %>" onchange="this.form.submit();">
							<html:options collection="campuss" property="idInternal" labelProperty="name"/>
						</html:select>
						<html:submit styleId="javascriptButtonID" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
							<bean:message key="button.submit"/>
						</html:submit>
					</html:form>
				</td>
				<th class="listClasses-header" cellspacing="0" cellpadding="0">
					<html:link page="<%= "/manageBuildings.do?method=deleteBuilding&amp;buildingId="
            	   				   			+ pageContext.findAttribute("buildingId")
  											%>">
						<bean:message key="link.delete"/>
					</html:link>
				</th>
			<tr>
		</logic:iterate>
	</table>
</logic:present>

<logic:notPresent name="buildings">
	<bean:message key="message.no.buildings"/>
</logic:notPresent>