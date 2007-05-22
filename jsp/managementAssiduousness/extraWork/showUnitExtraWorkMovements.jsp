<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em class="invisible"><bean:message key="title.assiduousness" /></em>
<h2><bean:message key="title.viewUnitExtraWorkAmounts" /></h2>
	
<p>
<h3><bean:write name="unitExtraWorkAmountFactory" property="year"/></h3>
<h3><bean:write name="unitExtraWorkAmountFactory" property="unit.presentationName"/></h3>

<bean:define id="unitExtraWorkAmountID" name="unitExtraWorkAmountFactory" property="unitExtraWorkAmount.idInternal"/>

<fr:form action="/manageUnitsExtraWorkAmounts.do?method=prepareEditUnitExtraWorkMovement">
<fr:edit id="editMovement" name="unitExtraWorkAmountFactory" visible="false"/>
<bean:size id="size" name="unitExtraWorkAmountFactory" property="unitExtraWorkAmount.unitExtraWorkMovements"/>
<table class="tstyle5 thleft thlight">
	<tr>
		<th><bean:message key="label.date" bundle="ASSIDUOUSNESS_RESOURCES"/></th>
		<th><bean:message key="label.amount" bundle="ASSIDUOUSNESS_RESOURCES"/></th>
		<td class="tdclear"/>
	</tr>
	<logic:iterate indexId="iter" id="unitExtraWorkMovement" name="unitExtraWorkAmountFactory" property="orderedMovements">
	<tr>
		<td><fr:view name="unitExtraWorkMovement" property="date"/></td>
		<logic:equal name="size" value="<%= new Integer(iter +1).toString() %>">
			<td>
				<fr:edit id="amountEdit" name="unitExtraWorkMovement" schema="edit.unitExtraWorkMovement">
					<fr:layout name="flow">
						<fr:property name="labelExcluded" value="true"/>
						<fr:property name="hideValidators" value="true"/>
					</fr:layout>
				</fr:edit>
			</td>
			<td class="tdclear">
				<fr:hasMessages for="amountEdit">
					<p><span class="error0"><fr:message for="amountEdit" show="message"/></span></p>
				</fr:hasMessages>
			</td>
		</logic:equal>
		<logic:notEqual name="size" value="<%= new Integer(iter +1).toString() %>">
			<td><fr:view name="unitExtraWorkMovement" property="amount"/></td>
		</logic:notEqual>
	</tr>
	</logic:iterate>
</table>

<html:submit><bean:message key="button.confirm" bundle="ASSIDUOUSNESS_RESOURCES"/></html:submit>
</fr:form>
</p>

<fr:form action="/manageUnitsExtraWorkAmounts.do?method=insertNewAmount">
	<fr:edit id="amountFactory" name="unitExtraWorkAmountFactory" visible="false"/>
	<p>
	<strong><bean:message key="label.addNewAmount" bundle="ASSIDUOUSNESS_RESOURCES"/></strong>
	<br>
	<fr:edit id="amount" name="unitExtraWorkAmountFactory" schema="insert.unitExtraWorkMovement"/>
	</p>
	<html:submit><bean:message key="button.insert" bundle="ASSIDUOUSNESS_RESOURCES"/></html:submit>
</fr:form>