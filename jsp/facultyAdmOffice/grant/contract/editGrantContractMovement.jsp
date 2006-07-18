<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<strong><p align="center"><bean:message key="label.grant.contract.movement.edition"/></p></strong><br/>

<html:form action="/editGrantContractMovement" style="display:inline">

	<%-- Presenting errors --%>
	<logic:messagesPresent>
	<span class="error">
		<html:errors/>
	</span><br/>
	</logic:messagesPresent>

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="doEdit"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.idInternal" property="idInternal"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.grantContractId" property="grantContractId"/>

	<table>
		<tr>
			<td align="left">
				<bean:message key="label.grant.contract.movement.location"/>:&nbsp;
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.location" property="location" size="15"/><bean:message key="label.requiredfield"/>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.contract.movement.departureDate"/>:&nbsp;
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.departureDate" property="departureDate" size="10"/><bean:message key="label.requiredfield"/>
				&nbsp;
				<bean:message key="label.dateformat"/>
			</td>
		</tr>
        <tr>
			<td align="left">
				<bean:message key="label.grant.contract.movement.arrivalDate"/>:&nbsp;
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.arrivalDate" property="arrivalDate" size="10"/><bean:message key="label.requiredfield"/>
				&nbsp;
				<bean:message key="label.dateformat"/>
			</td>
		</tr>
        
	</table>

	<br/>

	<table>
		<tr>
			<td>
				<%-- Save button (edit/create) --%>
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
					<bean:message key="button.save"/>
				</html:submit>
</html:form>
			</td>
			<td>
				<html:form action="/manageGrantContractMovement" style="display:inline">
					<%-- button cancel --%>
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="prepareManageGrantContractMovement"/>
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.idContract" property="idContract" value='<%= request.getAttribute("idContract").toString() %>'/>
					<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" style="display:inline">
						<bean:message key="button.cancel"/>
					</html:submit>
				</html:form>
			</td>
		</tr>
	</table>