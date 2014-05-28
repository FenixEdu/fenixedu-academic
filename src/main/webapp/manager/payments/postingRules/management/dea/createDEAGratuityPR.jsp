<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page isELIgnored="true"%>
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<h2><bean:message
	key="label.payments.postingRules.createDEAGratuityPR"
	bundle="MANAGER_RESOURCES" /></h2>

<br />

<fr:hasMessages for="installmentEditor">
	<ul>
		<fr:messages>
			<li><fr:message/></li>
		</fr:messages>
	</ul>
</fr:hasMessages>

<logic:messagesPresent message="true" property="installment">
	<ul class="nobullet list6">
		<html:messages id="messages" message="true" property="installment" bundle="MANAGER_RESOURCES">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
</logic:messagesPresent>

<logic:messagesPresent message="true" property="<%=org.apache.struts.action.ActionMessages.GLOBAL_MESSAGE%>">
	<ul class="nobullet list6">
		<html:messages id="messages" message="true" property="<%=org.apache.struts.action.ActionMessages.GLOBAL_MESSAGE%>" bundle="APPLICATION_RESOURCES">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
</logic:messagesPresent>


<bean:define id="degreeCurricularPlanId" name="degreeCurricularPlan" property="externalId" />

<fr:form action="<%= "/postingRules.do?degreeCurricularPlanId=" + degreeCurricularPlanId %>">
	<input type="hidden" name="method" value="" />

	<fr:edit id="paymentPlanEditor" name="paymentPlanEditor" >
		<fr:schema type="net.sourceforge.fenixedu.dataTransferObject.accounting.paymentPlan.PaymentPlanBean"
			bundle="APPLICATION_RESOURCES">
			<fr:slot name="executionYear" layout="menu-select-postback" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
				<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.ExecutionYearsProvider" />
				<fr:property name="format" value="${year}" />
				<fr:property name="destination" value="changeExecutionYearPostback"/>
			</fr:slot>
		</fr:schema>
		
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight thcenter mtop05" />
			<fr:destination name="invalid" path="/postingRules.do?method=createDEAGratuityPRInvalid" />
			<fr:destination name="changeExecutionYearPostback" path="/postingRules.do?method=changeExecutionYearForDEAGratuityPR"/>
		</fr:layout>
	</fr:edit>
	
	<br/><br/>
	
	<fr:edit id="installmentEditor" name="installmentEditor">
		<fr:schema type="net.sourceforge.fenixedu.dataTransferObject.accounting.paymentPlan.InstallmentBean"
			bundle="APPLICATION_RESOURCES">
			<fr:slot name="amount" />
			<fr:slot name="startDate" />
			<fr:slot name="endDate" />
		</fr:schema>
		
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight thcenter mtop05" />
			<fr:property name="columnClasses" value="width14em,width35em,tdclear tderror1"/>
		</fr:layout>
		<fr:destination name="invalid" path="/postingRules.do?method=createDEAGratuityPRInvalid"/>
	</fr:edit>		
			
	<br/><br/>

	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"
		onclick="this.form.method.value='createDEAGratuityPR';">
		<bean:message bundle="APPLICATION_RESOURCES" key="label.create" />
	</html:submit>
	<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel"
		onclick="this.form.method.value='showPostGraduationDegreeCurricularPlanPostingRules';">
		<bean:message bundle="APPLICATION_RESOURCES" key="label.cancel" />
	</html:cancel>

</fr:form>
