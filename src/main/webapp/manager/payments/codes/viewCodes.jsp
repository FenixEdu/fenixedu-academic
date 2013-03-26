<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />

<h2><bean:message key="label.paymentCodeMappings" bundle="MANAGER_RESOURCES" /></h2>

<logic:messagesPresent message="true">
	<ul class="nobullet list6">
		<html:messages id="messages" message="true" bundle="MANAGER_RESOURCES">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
</logic:messagesPresent>

<fr:edit id="paymentCodeMappingBean" name="paymentCodeMappingBean" schema="PaymentCodeMappingBean.search"
		action="/paymentsManagement.do?method=viewPaymentCodeMappings">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2" />
		<fr:property name="columnClasses" value=",,tdclear tderror1" />
	</fr:layout>
	<fr:destination name="cancel"  path="/index.do"/>
</fr:edit>

<br/>
<logic:present name="paymentCodeMappingBean" property="executionInterval">
	<bean:define id="executionIntervalId" name="paymentCodeMappingBean" property="executionInterval.externalId" />
	<html:link page='<%= "/paymentsManagement.do?method=prepareCreatePaymentCodeMapping&amp;executionIntervalOid=" + executionIntervalId %>'><bean:message key="label.create" bundle="MANAGER_RESOURCES" /></html:link>
	<br/>
</logic:present>

<logic:empty name="paymentCodeMappings">
	<em><strong><bean:message key="label.paymentCodeMappings.empty.list" bundle="MANAGER_RESOURCES" /></strong></em>
</logic:empty>

<logic:notEmpty name="paymentCodeMappings">
	

	<fr:view name="paymentCodeMappings" schema="PaymentCodeMapping.view">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2" />

			<fr:property name="linkFormat(delete)" value="/paymentsManagement.do?method=deletePaymentCodeMapping&amp;paymentCodeMappingOid=${externalId}" />
			<fr:property name="key(delete)" value="label.delete"/>
			<fr:property name="bundle(delete)" value="MANAGER_RESOURCES"/>
			<fr:property name="confirmationKey(delete)" value="label.paymentCodeMapping.confirm.delete.message" />
			<fr:property name="confirmationBundle(delete)" value="MANAGER_RESOURCES" />
		</fr:layout>
	</fr:view>
</logic:notEmpty>
