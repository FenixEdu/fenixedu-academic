<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<table class="infoop">
	<tr>
		<td rowspan=5><p class="emphasis-box">i</p></td>
		<td><strong><bean:message key="label.list.byCriteria"/></strong><br/></td>		
	</tr>
	<tr><td>&nbsp;</td></tr>
	<tr>
		<td><bean:message key="message.grant.list.grantcontractbycriteria.resume"/></td>
	</tr>
	<tr>
		<td><bean:message key="message.grant.list.grantcontractbycriteria.options"/></td>
	</tr>
	<tr>
		<td><bean:message key="message.grant.list.grantcontractbycriteria.optionsselected"/></td>
	</tr>
</table><br/>


<html:form action="/listGrantContractByCriteria" style="display:inline">

	<%-- Presenting errors --%>
	<logic:messagesPresent>
	<span class="error"><!-- Error messages go here -->
		<html:errors/>
	</span><br/>
	</logic:messagesPresent>

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="prepareListGrantContractByCriteria"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.spanNumber" property="spanNumber" value="1"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.orderBy" property="orderBy" value="orderByNumber"/>
	


	<h3>Filtrar</h3>

			
				<table>
					<tr>
						<td>
							<bean:message key="label.list.byCriteria.grant.owner.radio.all"/>:&nbsp;
						</td>
						<td>
							<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.filterType" name="listGrantContractByCriteriaForm" property="filterType"  value="1"/>
						</td>
		           </tr>
				   <tr>
				   		<td>
							<bean:message key="label.list.byCriteria.grant.owner.radio.justActive"/>:&nbsp;
						</td>
						<td>
							<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.filterType" name="listGrantContractByCriteriaForm" property="filterType"  value="2"/>
						</td>
                   </tr>
				   <tr>
				   		<td>
							<bean:message key="label.list.byCriteria.grant.owner.radio.justDesactive"/>:&nbsp;
						</td>
						<td>
							<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.filterType" name="listGrantContractByCriteriaForm" property="filterType"  value="3"/>			
			            </td>
					</tr>
				</table>
		
				<table>
					<tr>
						<td align="left">
							<bean:message key="label.list.byCriteria.grant.owner.grantType"/>:&nbsp;
						</td>
						<td>
						<html:select bundle="HTMLALT_RESOURCES" altKey="select.grantTypeId" property="grantTypeId">
							<html:options collection="grantTypeList" property="idInternal" labelProperty="sigla"/>
						</html:select>
						</td>
					</tr>
				</table>
	
<div class="lfloat mtop2">
	<p><b>Deve optar por:</b></p>
	<p>a) Filtrar as listas por "Data Início/Fim"</p>
	<p><label><bean:message key="label.list.byCriteria.grant.owner.dateBegin"/>:</label><html:text bundle="HTMLALT_RESOURCES" altKey="text.beginContract" property="beginContract" size="10"/>&nbsp;<bean:message key="label.dateformat"/></p>
	<p><label><bean:message key="label.list.byCriteria.grant.owner.dateEnd"/>:</label><html:text bundle="HTMLALT_RESOURCES" altKey="text.endContract" property="endContract" size="10"/>&nbsp;<bean:message key="label.dateformat"/></p>
	<p class="mtop1">b) ou por "Activas ï¿½ data"</p>
	<p><label><bean:message key="label.list.byCriteria.grant.owner.validToDate"/>:</label><html:text bundle="HTMLALT_RESOURCES" altKey="text.validToTheDate" property="validToTheDate" size="10"/>&nbsp;<bean:message key="label.dateformat"/></p>
</div>

<p>
		<%-- Search button --%>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
			<bean:message key="button.search"/>
		</html:submit>
</p>

</html:form>