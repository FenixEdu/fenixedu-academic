<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<strong><p align="center"><bean:message key="label.grant.type.edition"/></p></strong><br/>

<html:form action="/editGrantType" style="display:inline">

	<%-- Presenting errors --%>
	<logic:messagesPresent>
	<span class="error">
		<html:errors/>
	</span><br/>
	</logic:messagesPresent>

	<html:hidden property="method" value="doEdit"/>
	<html:hidden property="page" value="1"/>

	<%-- grant type --%>
	<html:hidden property="idInternal"/>

	<table>
		<tr>
			<td align="left">
				<bean:message key="label.grant.type.name"/>:&nbsp;
			</td>
			<td>
				<html:text property="name" size="100"/><bean:message key="label.requiredfield"/>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.type.sigla"/>:&nbsp;
			</td>
			<td>
				<html:text property="sigla"/><bean:message key="label.requiredfield"/>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.type.minPeriodDays"/>:&nbsp;
			</td>
			<td>
				<html:text property="minPeriodDays" size="10"/>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.type.maxPeriodDays"/>:&nbsp;
			</td>
			<td>
				<html:text property="maxPeriodDays" size="10"/>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.type.indicativeValue"/>:&nbsp;
			</td>
			<td>
				<html:text property="indicativeValue"/>
			</td>
		</tr>

		<tr>
			<td align="left">
				<bean:message key="label.grant.type.source"/>:&nbsp;
			</td>
			<td>
				<html:text property="source" size="40"/>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.type.state"/>:&nbsp;
			</td>
			<td>
				<html:text property="state" size="10"/>&nbsp;<bean:message key="label.dateformat"/>
			</td>
		</tr>
	</table>

	<br/>

	<table>
		<tr>
			<td>
				<%-- Save button (edit/create) --%>
				<html:submit styleClass="inputbutton">
					<bean:message key="button.save"/>
				</html:submit>
</html:form>
			</td>
			<td>
				<html:form action="/manageGrantType" style="display:inline">
					<%-- button cancel --%>
					<html:hidden property="method" value="prepareManageGrantTypeForm"/>
					<html:submit styleClass="inputbutton" style="display:inline">
						<bean:message key="button.cancel"/>
					</html:submit>
				</html:form>
			</td>
		</tr>
	</table>