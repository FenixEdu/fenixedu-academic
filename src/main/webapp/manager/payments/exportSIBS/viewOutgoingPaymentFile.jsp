<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml />

<h2><bean:message bundle="MANAGER_RESOURCES" key="label.sibs.outgoing.payment.file.detail" /></h2>

<fr:view	name="sibsPaymentFile" >
	<fr:schema bundle="MANAGER_RESOURCES" type="net.sourceforge.fenixedu.domain.accounting.events.export.SIBSOutgoingPaymentFile">
		<fr:slot name="filename" key="label.sibs.outgoing.payment.file.name" />
		<fr:slot name="uploadTime" key="label.sibs.outgoing.payment.upload.time" />
		<fr:slot name="successfulSentDate" key="label.sibs.outgoing.payment.successfulSent" />
		<fr:slot name="this" key="label.sibs.outgoing.payment.file.view" layout="link"/>
		<fr:slot name="errors" key="label.sibs.outgoing.payment.file.errors" />
	</fr:schema>
	<fr:layout name="tabular">
	</fr:layout>
</fr:view>

<bean:define id="paymentFileId" name="sibsPaymentFile" property="externalId" />
<p>
<html:link action='<%= "/exportSIBSPayments.do?method=prepareSetSuccessfulSentPaymentsFileDate&amp;paymentFileId=" + paymentFileId %>'>
	<bean:message key="label.sibs.outgoing.payment.file.set.successful.sent.date" bundle="MANAGER_RESOURCES" />
</html:link>
</p>
