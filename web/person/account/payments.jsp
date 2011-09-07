<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />

<em><bean:message key="label.person.main.title" /> </em>
<h2><bean:message key="label.accounting.person.payments.title" bundle="ACCOUNTING_RESOURCES" /></h2>

<logic:present name="paymentCode">
	<p class="mbottom025">
		<strong><bean:message bundle="ACCOUNTING_RESOURCES" key="label.accounting.micropayments.paymentCode" /> </strong>
	</p>
	<fr:view name="paymentCode">
		<fr:schema bundle="ACCOUNTING_RESOURCES" type="net.sourceforge.fenixedu.domain.accounting.PaymentCode">
			<fr:slot name="entityCode" />
			<fr:slot name="formattedCode" />
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thcenter tdcenter thlight mtop025" />
			<fr:property name="columnClasses" value=",," />
		</fr:layout>
	</fr:view>
</logic:present>

<logic:notEmpty name="payments">
	<p class="mbottom025">
		<strong><bean:message bundle="ACCOUNTING_RESOURCES" key="label.accounting.micropayments.transactions" /> </strong>
	</p>
	<fr:view name="payments">
		<fr:schema type="net.sourceforge.fenixedu.domain.accounting.Entry" bundle="ACCOUNTING_RESOURCES">
			<fr:slot name="description" />
			<fr:slot name="whenRegistered" />
			<fr:slot name="amountWithAdjustment" />
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thlight mtop025 width100 mbottom0" />
			<fr:property name="columnClasses" value=",acenter,aright" />

			<fr:property name="sortBy" value="whenRegistered=asc" />
		</fr:layout>
	</fr:view>

	<table class="tstyle1 tgluetop mtop0 width100 aright">
		<tr>
			<td><span style="padding-right: 5px;"><bean:message bundle="ACCOUNTING_RESOURCES" key="label.accounting.balance" />: </span> <bean:write name="balance" /></td>
		</tr>
	</table>
</logic:notEmpty>

