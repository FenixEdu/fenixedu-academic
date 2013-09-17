<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

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
