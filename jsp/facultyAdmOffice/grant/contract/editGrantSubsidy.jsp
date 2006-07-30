<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<p class="infoselected">
		<b><bean:message key="label.grant.contract.information"/></b><br/>
    	<bean:message key="label.grant.contract.contractnumber"/>:&nbsp;<bean:write name="contractNumber"/><br/>
    	<bean:message key="label.grant.contract.type"/>:&nbsp;<bean:write name="grantTypeName"/>
	</p>
	
<strong><p align="center"><bean:message key="label.grant.subsidy.edition"/></p></strong><br/>

<html:form action="/editGrantSubsidy" style="display:inline">
	<%-- Presenting errors --%>
	<logic:messagesPresent>
	<span class="error"><!-- Error messages go here -->
		<html:errors/>
	</span><br/>
	</logic:messagesPresent>

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="doEdit"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>

	<%-- In case of validation error --%>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.contractNumber" property="contractNumber" value='<%= request.getAttribute("contractNumber").toString() %>' />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.grantTypeName" property="grantTypeName" value='<%= request.getAttribute("grantTypeName").toString() %>' />
	
	<%-- grant contract --%>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.idContract" property="idContract"/> 
	
	<%-- grant subsidy --%>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.idGrantSubsidy" property="idGrantSubsidy"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.state" property="state"/>

	<%-- grant owner  --%>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.idGrantOwner" property="idGrantOwner"/>

	<table>
		<tr>
			<td align="left">
				<bean:message key="label.grant.subsidy.value"/>:&nbsp;
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.value" property="value"/><bean:message key="label.requiredfield"/>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.subsidy.valueFullName"/>:&nbsp;
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.valueFullName" property="valueFullName" size="50"/>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.subsidy.totalCost"/>:&nbsp;
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.totalCost" property="totalCost"/><bean:message key="label.requiredfield"/>
			</td>
		</tr>
		
		<tr>
			<td align="left">
				<bean:message key="label.grant.subsidy.dateBeginSubsidy"/>:&nbsp;
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.dateBeginSubsidy" property="dateBeginSubsidy" size="10"/>&nbsp;<bean:message key="label.dateformat"/>
				<bean:message key="label.requiredfield"/>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.subsidy.dateEndSubsidy"/>:&nbsp;
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.dateEndSubsidy" property="dateEndSubsidy" size="10"/>&nbsp;<bean:message key="label.dateformat"/>
				<bean:message key="label.requiredfield"/>
			</td>
		</tr>
	</table>

	<br/>

	<table>
		<tr>
			<td>
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
					<bean:message key="button.save"/>
				</html:submit>
</html:form>
			</td>
			<td>
				<html:form action="/manageGrantSubsidy" style="display:inline">
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="prepareManageGrantSubsidyForm"/>
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.idContract" property="idContract" value='<%= request.getAttribute("idContract").toString() %>'/>
					<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" style="display:inline">
						<bean:message key="button.cancel"/>
					</html:submit>
				</html:form>
			</td>
		</tr>
	</table>
	
<br/><br/>
