<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<h2><bean:message key="titulo.extraWork.money.cost.center" /></h2>
<html:form action="/managementMoneyCostCenter.do">
	<html:hidden property="method" value="updateMoneyValuesByYear"/>
	<html:hidden property="page" value="1"/>	
	<html:hidden property="year" value="<%= request.getAttribute("year").toString() %>"/>		
	<bean:size id="sizeList" name="infoMoneyCostCenterList" />	
	<html:hidden property="size" value="<%= sizeList.toString() %>" />	
	
	<table>
		<tr><td colspan="5" class="infoop"><bean:message key="info.managementAssiduousness.extraWork.money.cost.center.values"/></td></tr>
		<logic:messagesPresent>	
			<tr><td colspan="5" ><br /><br /></td></tr>
			<tr><td colspan="5"><span class="error"><html:errors/></span></td></tr>
		</logic:messagesPresent>
		<tr>
			<td><strong><bean:message key="label.year" />:</strong>&nbsp;<bean:write name="year"/></td>
			<td colspan="4" ><br /><br /></td>					
		</tr>
		<tr><td colspan="5" ><br /><br /></td></tr>
		<tr>
			<td class="listClasses-header" colspan="2"><bean:message key="label.code.cost.center" /></td>
			<td class="listClasses-header"><bean:message key="label.extraWork.money.cost.center.initial" /></td>
			<td class="listClasses-header"><bean:message key="label.money.cost.center.total" /></td>
			<td class="listClasses-header"><bean:message key="label.money.cost.center.spent" /></td>			
		</tr>
		<logic:iterate id="infoMoneyCostCenter" name="infoMoneyCostCenterList" indexId="indice">			
			<bean:define id="isPar">
				<%= indice.intValue() % 2  %>
			</bean:define>
			<logic:equal name="isPar" value="0"><tr class="listClasses"></logic:equal>
			<logic:notEqual name="isPar" value="0"><tr class="listClassesWhite"></logic:notEqual>				
				<td>
					<bean:define id="infoMoneyCostCenterID" name="infoMoneyCostCenter" property="idInternal" />
					<html:hidden name="infoMoneyCostCenter" property="id" indexed="true" value="<%= infoMoneyCostCenterID.toString() %>"/>	
	
					<bean:write name="infoMoneyCostCenter" property="infoCostCenter.code" />				
				</td>
				<td>
					<br /><bean:write name="infoMoneyCostCenter" property="infoCostCenter.departament" />
					<br /><bean:write name="infoMoneyCostCenter" property="infoCostCenter.section1" />
					<br /><bean:write name="infoMoneyCostCenter" property="infoCostCenter.section2" />
					<br />
				</td>
				<td>					
					<logic:notEmpty name="infoMoneyCostCenter" property="initialValue">
						<bean:define id="initialValueCC" name="infoMoneyCostCenter" property="initialValue" />
						<html:text name="infoMoneyCostCenter" property="initialValue" indexed="true" value="<%= initialValueCC.toString() %>"/>
					</logic:notEmpty>
				</td>
				<td>					
					<logic:notEmpty name="infoMoneyCostCenter" property="totalValue">
						<bean:define id="totalValueCC" name="infoMoneyCostCenter" property="totalValue" />
						<html:text name="infoMoneyCostCenter" property="totalValue" indexed="true" value="<%= totalValueCC.toString() %>"/>
					</logic:notEmpty>
				</td>	
				<td>
					<logic:notEmpty name="infoMoneyCostCenter" property="spentValue"><bean:write name="infoMoneyCostCenter" property="spentValue" /></logic:notEmpty>
				</td>
			</tr>		
		</logic:iterate>		
		<tr>
			<td colspan="4">
				<br /><br />
			</td>		
		</tr>
	</table>

	<html:submit styleClass="inputbutton">
		<bean:message key="botao.actualizar"/>
	</html:submit>
</html:form>	