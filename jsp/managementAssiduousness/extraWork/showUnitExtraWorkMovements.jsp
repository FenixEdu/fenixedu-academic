<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em class="invisible"><bean:message key="title.assiduousness" /></em>
<h2><bean:message key="title.viewUnitExtraWorkAmounts"/></h2>
	

<h3><bean:write name="unitExtraWorkAmountFactory" property="year"/></h3>

<p><strong class="highlight1"><bean:write name="unitExtraWorkAmountFactory" property="unit.presentationName"/></strong></p>

<bean:define id="unitExtraWorkAmountID" name="unitExtraWorkAmountFactory" property="unitExtraWorkAmount.idInternal"/>

<fr:form action="/manageUnitsExtraWorkAmounts.do?method=prepareEditUnitExtraWorkMovement">

<fr:edit id="editMovement" name="unitExtraWorkAmountFactory" visible="false"/>
<bean:size id="size" name="unitExtraWorkAmountFactory" property="unitExtraWorkAmount.unitExtraWorkMovements"/>

<p class="mtop15 mbottom025"><bean:message key="label.addedAmounts" bundle="ASSIDUOUSNESS_RESOURCES"/>:</p>

<table class="tstyle2 thlight mtop025 mbottom05">
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

<p class="mtop05">
	<html:submit><bean:message key="button.confirm" bundle="ASSIDUOUSNESS_RESOURCES"/></html:submit>
</p>

</fr:form>


<fr:form action="/manageUnitsExtraWorkAmounts.do?method=insertNewAmount">
	<fr:edit id="amountFactory" name="unitExtraWorkAmountFactory" visible="false"/>

	<p class="mtop2 mbottom025"><bean:message key="label.addNewAmount" bundle="ASSIDUOUSNESS_RESOURCES"/>:</p>
	
	<fr:edit id="amount" name="unitExtraWorkAmountFactory" schema="insert.unitExtraWorkMovement">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright thmiddle mtop025 mbottom05" />
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>
	</fr:edit>
	
	<p class="mtop5">
		<html:submit><bean:message key="button.insert" bundle="ASSIDUOUSNESS_RESOURCES"/></html:submit>
	</p>
</fr:form>