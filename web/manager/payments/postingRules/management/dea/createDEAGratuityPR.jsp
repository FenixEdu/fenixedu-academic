<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="MANAGER">

	<!-- createPaymentPlan.jsp -->

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
	
	
	<bean:define id="degreeCurricularPlanId" name="degreeCurricularPlan" property="idInternal" />

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

</logic:present>
