<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<p class="infoselected">
		<b><bean:message key="label.grant.contract.information"/></b><br/>
    	<bean:message key="label.grant.contract.number"/>:&nbsp;<bean:write name="contractNumber"/><br/>
    	<bean:message key="label.grant.type.name"/>:&nbsp;<bean:write name="grantTypeName"/>
	</p>
	
<strong><p align="center"><bean:message key="label.grant.subsidy.edition"/></p></strong><br/>

<html:form action="/editGrantSubsidy" style="display:inline">
	<%-- Presenting errors --%>
	<logic:messagesPresent>
	<span class="error">
		<html:errors/>
	</span><br/>
	</logic:messagesPresent>

	<html:hidden property="method" value="doEdit"/>
	<html:hidden property="page" value="1"/>

	<%-- In case of validation error --%>
	<html:hidden property="contractNumber" value='<%= request.getAttribute("contractNumber").toString() %>' />
	<html:hidden property="grantTypeName" value='<%= request.getAttribute("grantTypeName").toString() %>' />
	
	<%-- grant contract --%>
	<html:hidden property="idGrantContract"/> 
	
	<%-- grant subsidy --%>
	<html:hidden property="idGrantSubsidy"/>

	<%-- grant owner  --%>
	<html:hidden property="idGrantOwner"/>

	<table>
		<tr>
			<td align="left">
				<bean:message key="label.grant.subsidy.value"/>:&nbsp;
			</td>
			<td>
				<html:text property="value"/><bean:message key="label.requiredfield"/>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.subsidy.valueFullName"/>:&nbsp;
			</td>
			<td>
				<html:text property="valueFullName"/>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.subsidy.totalCost"/>:&nbsp;
			</td>
			<td>
				<html:text property="totalCost"/><bean:message key="label.requiredfield"/>
			</td>
		</tr>
	</table>

	<br/>

	<table>
		<tr>
			<td>
				<html:submit styleClass="inputbutton">
					<bean:message key="button.save"/>
				</html:submit>
</html:form>
			</td>
			<td>
				<html:form action="/manageGrantContract" style="display:inline">
				<html:hidden property="method" value="prepareManageGrantContractForm"/>
				<html:hidden property="page" value="1"/>
				<html:hidden property="idInternal" value='<%= request.getAttribute("idGrantOwner").toString() %>'/>
					<html:submit styleClass="inputbutton" style="display:inline">
						<bean:message key="button.cancel"/>
					</html:submit>
				</html:form>
			</td>
		</tr>
	</table>
	
<br/><br/>

<logic:present name="idSubsidy">
	<bean:message key="message.grant.part.manage"/>:&nbsp;
	<html:link page='<%= "/manageGrantPart.do?method=prepareManageGrantPart&amp;idSubsidy=" + request.getAttribute("idSubsidy").toString()%>'>
	<bean:message key="link.manage.grant.part"/>
	</html:link>
</logic:present>