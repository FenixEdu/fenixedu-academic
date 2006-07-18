<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<table class="infoop">
<tr>
	<td rowspan=5><p class="emphasis-box">i</p></td>
	<td><b><bean:message key="label.grant.owner.stats"/></b></td>		
</tr>
<tr><td>&nbsp;</td></tr>
<tr>
	<td><bean:message key="message.grant.stat.grantcontractbycriteria.resume"/></td>
</tr>
<tr>
	<td><bean:message key="message.grant.stat.grantcontractbycriteria.options"/></td>
</tr>
<tr>
	<td><bean:message key="message.grant.stat.grantcontractbycriteria.optionsselected"/></td>
</tr>
</table><br/>


<html:form action="/grantOwnerStats" style="display:inline">

<%-- Presenting errors --%>
<logic:messagesPresent>
<span class="error">
	<html:errors/>
</span><br/>
</logic:messagesPresent>

<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="doStat"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>

<table>
	<tr>
		<td colspan="2">
			<table>
				<tr>
					<td>
						<bean:message key="label.stat.byCriteria.grant.owner.radio.all"/>:&nbsp;
					</td>
					<td>
						<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.filterType" name="grantOwnerStatsForm" property="filterType"  value="1"/>
					</td>
	           </tr>
			   <tr>
			   		<td>
						<bean:message key="label.stat.byCriteria.grant.owner.radio.justActive"/>:&nbsp;
					</td>
					<td>
						<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.filterType" name="grantOwnerStatsForm" property="filterType"  value="2"/>
					</td>
               </tr>
			   <tr>
			   		<td>
						<bean:message key="label.stat.byCriteria.grant.owner.radio.justDesactive"/>:&nbsp;
					</td>
					<td>
						<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.filterType" name="grantOwnerStatsForm" property="filterType"  value="3"/>			
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
			<bean:message key="label.stat.byCriteria.grant.owner.dateBegin"/>:&nbsp;
		</td>
		<td colspan="2">
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.beginContract" property="beginContract" size="10"/>&nbsp;<bean:message key="label.dateformat"/>
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.stat.byCriteria.grant.owner.dateEnd"/>:&nbsp;
		</td>
		<td colspan="2">
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.endContract" property="endContract" size="10"/>&nbsp;<bean:message key="label.dateformat"/>
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.stat.byCriteria.grant.contract.grantType"/>:&nbsp;
		</td>
		<td>
			<html:select bundle="HTMLALT_RESOURCES" altKey="select.grantType" property="grantType">
				<html:options collection="grantTypeList" property="idInternal" labelProperty="sigla"/>
			</html:select>
		</td>
	</tr>
</table>
<br/>
<table>
	<tr>
		<td>
			<%-- Search button --%>
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
				<bean:message key="button.stat"/>
			</html:submit>
</html:form>
		</td>
	</tr>
</table>
