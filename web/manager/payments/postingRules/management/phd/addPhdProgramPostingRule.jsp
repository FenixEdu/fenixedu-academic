<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml />



	<bean:define id="phdProgramId" name="phdProgram"
		property="externalId" />

<logic:present role="MANAGER">
	<h2><bean:message key="label.payments.postingRules.addGratuityPostingRule" bundle="MANAGER_RESOURCES" /></h2>
	
	<span class="error"><!-- Error messages go here --><html:errors bundle="MANAGER_RESOURCES" /></span>
	
	<br/>
<fr:form id="theForm" action="/phdPostingRules.do">
	
	<html:hidden property="method" value="addGratuityPhdPostingRule"/>
	<html:hidden property="phdProgramId" value="<%= phdProgramId.toString() %>"/>

	<fr:edit id="bean" name="bean" schema="addGratuityPhdPostingRule">
			<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
		</fr:layout>
		<fr:schema bundle="MANAGER_RESOURCES" type="net.sourceforge.fenixedu.presentationTier.Action.manager.payments.PhdPostingRulesManagementDA$CreateGratuityPhdBean">
			<fr:slot name="startDate" key="label.phd.gratuity.startDate" required="true" />
			<fr:slot name="endDate"   key="label.phd.gratuity.endDate" />
			<fr:slot name="gratuity" key="label.phd.gratuity.value" required="true" />
			<fr:slot name="fineRate" key="label.phd.gratuity.fine" required="true" />
		</fr:schema>
	</fr:edit>
	
	<h3>Adicionar periodos a regra de pagamento</h3>
	<fr:edit id="period" name="period" schema="addPhdGratuityPeriod">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
		</fr:layout>
		<fr:schema bundle="MANAGER_RESOURCES" type="net.sourceforge.fenixedu.presentationTier.Action.manager.payments.PhdPostingRulesManagementDA$CreateGratuityPhdPRPeriodBean">
			<fr:slot name="periodStartDate" layout="month-day"  key="label.period.start" />
			<fr:slot name="periodEndDate"   layout="month-day"  key="label.period.end" />
			<fr:slot name="limitePaymentDay" layout="month-day" key="label.period.last.day"/>
		</fr:schema>
	</fr:edit>
	
	<html:submit bundle="HTMLALT_RESOURCES" altKey="commandButton.add" onclick="this.form.method.value='addPeriod';this.form.submit()">
		<bean:message bundle="HTMLALT_RESOURCES" key="commandButton.add"/>
	</html:submit>
	
	<logic:notEmpty name="bean" property="periods">
	<html:hidden property="periodToRemove" value="null"/>
	<table class="tstyle4 thlight">
		<tr>
			<th><bean:message bundle="MANAGER_RESOURCES" key="label.period.start" /></th>
			<th><bean:message bundle="MANAGER_RESOURCES" key="label.period.end" /></th>
			<th><bean:message bundle="MANAGER_RESOURCES" key="label.period.last.day" /></th>
			<td>&nbsp;</td>
		</tr>
		<% int i=0; %>
		<logic:iterate id="obj" name="bean" property="periods" >
		<tr>
			<td><bean:write name="obj" property="periodStartString" /> </td>
			<td><bean:write name="obj" property="periodEndString" /></td>
			<td><bean:write name="obj" property="limitePaymentDayString" /></td>
			<td><a href="#" onclick="<%= "var form = document.getElementById('theForm');form.periodToRemove.value = " + i + ";form.method.value = 'removePeriod';form.submit();"  %>" >Apagar</a></td>
			<% i++; %>
		</tr>
		</logic:iterate>
	</table>
	</logic:notEmpty>
	<logic:empty name="bean" property="periods">
		<div  style="margin-top:10px;margin-bottom:10px;">
		<span class="error">
		<bean:message bundle="MANAGER_RESOURCES" key="error.empty.periods"/>
		</span>
		</div>
	</logic:empty>
	
	<p>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit">
		<bean:message bundle="APPLICATION_RESOURCES" key="label.submit"/>
	</html:submit>
	
	</p>
</fr:form>
</logic:present>