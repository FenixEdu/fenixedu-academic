<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml />

<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments" /></h2>

<p class="mtop15 mbottom05"><strong><bean:message bundle="MANAGER_RESOURCES" key="label.sibs.outgoing.payment.file.launch" /></strong></p>

<fr:form action="/exportSIBSPayments.do?method=setSuccessfulSentPaymentsFileDate">
	<fr:edit id="sibs.outgoing.payment.file.data.bean" name="sibsOutgoingPaymentFileDataBean" visible="false" />
	
	<fr:edit id="sibs.outgoing.payment.file.data.bean.edit" name="sibsOutgoingPaymentFileDataBean">
		<fr:schema bundle="MANAGER_RESOURCES" type="net.sourceforge.fenixedu.presentationTier.Action.manager.payments.ExportSIBSPaymentsDA$SIBSOutgoingPaymentFileDataBean">
			<fr:slot name="lastOutgoingPaymentFileSent" key="label.sibs.outgoing.payment.last.successful.sent.payment.file" required="true">
			</fr:slot>
		</fr:schema>
	</fr:edit>
	
	<html:submit ><bean:message key="button.edit" /></html:submit>
</fr:form> 
