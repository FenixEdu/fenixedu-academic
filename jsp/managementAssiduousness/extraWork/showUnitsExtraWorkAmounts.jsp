<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em class="invisible"><bean:message key="title.assiduousness" /></em>
<h2><bean:message key="title.viewUnitExtraWorkAmounts" /></h2>


<fr:form action="/manageUnitsExtraWorkAmounts.do?method=chooseYear">
	<table>
		<tr>
			<td>
				<fr:edit id="year" name="year" schema="choose.year">
					<fr:layout>
						<fr:property name="classes" value="thlight thright" />
						<fr:property name="columnClasses" value=",,tdclear tderror1"/>
					</fr:layout>
				</fr:edit>	
			</td>
			<td>
				<html:submit><bean:message key="button.confirm" bundle="ASSIDUOUSNESS_RESOURCES"/></html:submit>
			</td>
		</tr>
	</table>
</fr:form>

<bean:define id="choosenYear"><bean:write name="year" property="year"/></bean:define>
<html:link page="/manageUnitsExtraWorkAmounts.do?method=prepareCreateUnitExtraWorkAmount" paramId="year" paramName="choosenYear">
	<bean:message key="link.insertNewUnitAmount"/>
</html:link>

<p>
<logic:present name="createUnitExtraWorkAmount">
	<fr:edit id="createUnitExtraWorkAmount" name="createUnitExtraWorkAmount" schema="edit.unitExtraWorkAmount" 
				action="/manageUnitsExtraWorkAmounts.do?method=createUnitExtraWorkAmount">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright" />
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>
		<fr:destination name="invalid" path="<%= "/manageUnitsExtraWorkAmounts.do?method=prepareCreateUnitExtraWorkAmount&ampyear=" +choosenYear%>"/>
		<fr:destination name="cancel" path="/manageUnitsExtraWorkAmounts.do?method=chooseYear"/>
	</fr:edit>	
</logic:present>
</p>

<h3 class="mtop2 mbottom025"><bean:write name="year" property="year"/></h3>	
<p class="mtop025">
<fr:view name="unitExtraWorkAmountList" schema="show.unitExtraWorkAmount">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1 thleft thlight mtop0"/>
		<fr:property name="link(viewExtraWorkMovements)" value="/manageUnitsExtraWorkAmounts.do?method=prepareEditUnitExtraWorkMovement&edit=false" />
		<fr:property name="key(viewExtraWorkMovements)" value="link.details" />
		<fr:property name="param(viewExtraWorkMovements)" value="idInternal/unitExtraWorkAmountID" />
		<fr:property name="bundle(viewExtraWorkMovements)" value="ASSIDUOUSNESS_RESOURCES" />
	</fr:layout>
</fr:view>
</p>