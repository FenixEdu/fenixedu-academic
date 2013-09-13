<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml />

<h2><bean:message key="label.create" bundle="MANAGER_RESOURCES" /></h2>

<logic:messagesPresent message="true">
	<ul class="nobullet list6">
		<html:messages id="messages" message="true" bundle="MANAGER_RESOURCES">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
</logic:messagesPresent>

<fr:edit id="paymentCodeMappingBean" name="paymentCodeMappingBean" schema="PaymentCodeMappingBean.create"
		action="/paymentsManagement.do?method=createPaymentCodeMapping">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2" />
		<fr:property name="columnClasses" value=",,tdclear tderror1" />
	</fr:layout>
	<fr:destination name="cancel" path="/paymentsManagement.do?method=prepareViewPaymentCodeMappings" />
	<fr:destination name="invalid" path="/paymentsManagement.do?method=createPaymentCodeMappingInvalid" />
</fr:edit>
