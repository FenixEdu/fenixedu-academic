<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<em><bean:message key="label.facultyAdmOffice.portal.name"/></em>
<h2><bean:message key="label.grant.owner.list.byCriteria"/></h2>

<div class="infoop2">
	<p><strong><bean:message key="label.list.byCriteria"/></strong></p>
	<p><bean:message key="message.grant.list.grantcontractbycriteria.resume"/></p>
	<p><bean:message key="message.grant.list.grantcontractbycriteria.options"/></p>
	<p><bean:message key="message.grant.list.grantcontractbycriteria.optionsselected"/></p>
</div>



<html:form action="/listGrantContractByCriteria" style="display:inline">

	<%-- Presenting errors --%>
	<logic:messagesPresent>
		<html:errors/>
	</logic:messagesPresent>

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="prepareListGrantContractByCriteria"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.spanNumber" property="spanNumber" value="1"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.orderBy" property="orderBy" value="orderByNumber"/>
	


	<h3 class="mbottom05">Filtrar</h3>

			
				<table class="tstyle5 thlight thright mtop05">
					<tr>
						<th>
							<bean:message key="label.list.byCriteria.grant.owner.radio.all"/>:&nbsp;
						</th>
						<td>
							<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.filterType" name="listGrantContractByCriteriaForm" property="filterType"  value="1"/>
						</td>
		           </tr>
				   <tr>
				   		<th>
							<bean:message key="label.list.byCriteria.grant.owner.radio.justActive"/>:&nbsp;
						</th>
						<td>
							<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.filterType" name="listGrantContractByCriteriaForm" property="filterType"  value="2"/>
						</td>
                   </tr>
				   <tr>
				   		<th>
							<bean:message key="label.list.byCriteria.grant.owner.radio.justDesactive"/>:&nbsp;
						</th>
						<td>
							<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.filterType" name="listGrantContractByCriteriaForm" property="filterType"  value="3"/>			
			            </td>
					</tr>
				</table>
		
				<table class="tstyle5 thlight thmiddle">
					<tr>
						<th>
							<bean:message key="label.list.byCriteria.grant.owner.grantType"/>:&nbsp;
						</th>
						<td>
							<html:select bundle="HTMLALT_RESOURCES" altKey="select.grantTypeId" property="grantTypeId">
								<html:options collection="grantTypeList" property="idInternal" labelProperty="sigla"/>
							</html:select>
						</td>
					</tr>
				</table>
	


<p><b>Deve optar por:</b></p>
<p class="mbottom05">a) Filtrar as listas por "Data Início/Fim"</p>
<table class="tstyle5 thlight mtop05 thmiddle">
	<tr>
		<th><bean:message key="label.list.byCriteria.grant.owner.dateBegin"/>:</th>
		<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.beginContract" property="beginContract" size="10"/>&nbsp;<bean:message key="label.dateformat"/></td>
	</tr>
	<tr>
		<th><bean:message key="label.list.byCriteria.grant.owner.dateEnd"/>:</th>
		<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.endContract" property="endContract" size="10"/>&nbsp;<bean:message key="label.dateformat"/></td>
	</tr>
</table>

<p class="mtop1 mbottom05">b) ou por "Activas na data"</p>
<table class="tstyle5 thlight mtop05 thmiddle">
	<tr>
		<th><bean:message key="label.list.byCriteria.grant.owner.validToDate"/>:</th>
		<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.validToTheDate" property="validToTheDate" size="10"/>&nbsp;<bean:message key="label.dateformat"/></td>
	</tr>
</table>


<p>
		<%-- Search button --%>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
			<bean:message key="button.search"/>
		</html:submit>
</p>

</html:form>