<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml />

<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments" /></h2>

<p class="mtop15 mbottom05"><strong><bean:message bundle="MANAGER_RESOURCES" key="label.sibs.outgoing.payment.file.launch" /></strong></p>

<fr:form action="/exportSIBSPayments.do?method=launchOutgoingPaymentsFileCreation">
	<fr:edit id="sibs.outgoing.payment.file.data.bean" name="sibsOutgoingPaymentFileDataBean" visible="false" />
	
	<fr:edit id="sibs.outgoing.payment.file.data.bean.edit" name="sibsOutgoingPaymentFileDataBean">
		<fr:schema bundle="MANAGER_RESOURCES" type="net.sourceforge.fenixedu.presentationTier.Action.manager.payments.ExportSIBSPaymentsDA$SIBSOutgoingPaymentFileDataBean">
			<fr:slot name="lastOutgoingPaymentFileSent" key="label.sibs.outgoing.payment.last.outgoing.successful.sent.payment.file" required="true">
				<fr:property name="format" value="dd/MM/yyyy" />
			</fr:slot>
		</fr:schema>
		
		
		<fr:destination name="invalid" path="/exportSIBSPayments.do?method=launchOutgoingPaymentsFileCreationInvalid"/>
	</fr:edit>
	
	<html:submit ><bean:message key="label.sibs.outgoing.payment.queue.job.create" bundle="MANAGER_RESOURCES" /></html:submit>
</fr:form> 
