<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<strong><p align="center"><bean:message key="label.list.byCriteria"/></p></strong><br/>

<html:form action="/listGrantContractByCriteria" style="display:inline">

	<%-- Presenting errors --%>
	<logic:messagesPresent>
	<span class="error">
		<html:errors/>
	</span><br/>
	</logic:messagesPresent>

	<html:hidden property="method" value="prepareListGrantContractByCriteria"/>
	<html:hidden property="page" value="1"/>
	
	<html:hidden property="spanNumber" value="1"/>
	<html:hidden property="orderBy" value="orderByNumber"/>
	
	<table>
		<tr>
			<td colspan="2">
				<table>
					<tr>
						<td>
							<bean:message key="label.list.byCriteria.grant.owner.radio.all"/>:&nbsp;
						</td>
						<td>
							<html:radio name="listGrantContractByCriteriaForm" property="filterType"  value="1"/>
						</td>
		           </tr>
				   <tr>
				   		<td>
							<bean:message key="label.list.byCriteria.grant.owner.radio.justActive"/>:&nbsp;
						</td>
						<td>
							<html:radio name="listGrantContractByCriteriaForm" property="filterType"  value="2"/>
						</td>
                   </tr>
				   <tr>
				   		<td>
							<bean:message key="label.list.byCriteria.grant.owner.radio.justDesactive"/>:&nbsp;
						</td>
						<td>
							<html:radio name="listGrantContractByCriteriaForm" property="filterType"  value="3"/>			
			            </td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td>
				&nbsp;
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.list.byCriteria.grant.owner.dateBegin"/>:&nbsp;
			</td>
			<td colspan="2">
				<html:text property="beginContract" size="10"/>&nbsp;<bean:message key="label.dateformat"/>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.list.byCriteria.grant.owner.dateEnd"/>:&nbsp;
			</td>
			<td colspan="2">
				<html:text property="endContract" size="10"/>&nbsp;<bean:message key="label.dateformat"/>
			</td>
		</tr>
		
	</table>

	<br/>

	<table>
		<tr>
			<td>
				<%-- Search button --%>
				<html:submit styleClass="inputbutton">
					<bean:message key="button.search"/>
				</html:submit>
</html:form>
			</td>
		</tr>
	</table>