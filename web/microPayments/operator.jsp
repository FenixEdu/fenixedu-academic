<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml />

<logic:messagesPresent message="true">
	<p><span class="error0"><!-- Error messages go here --> <html:messages id="message"
		message="true" bundle="ACCOUNTING_RESOURCES">
		<bean:write name="message" />
	</html:messages> </span>
	<p>
</logic:messagesPresent>

<fr:edit id="microPayment" name="microPayment" action="/operator.do?method=createMicroPayment">
	<fr:schema bundle="ACCOUNTING_RESOURCES" type="net.sourceforge.fenixedu.presentationTier.Action.microPayments.MicroPaymentsOperator$MicroPaymentCreationBean">
		<fr:slot name="person" />
		<fr:slot name="unit" layout="menu-select">
			<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.Action.microPayments.MicroPaymentsOperator$MicroPaymentUnitsProvider" />
			<fr:property name="format" value="${presentationName}" />
		</fr:slot>
		<fr:slot name="amount" />
	</fr:schema>
	<fr:layout name="tabular" />
</fr:edit>
