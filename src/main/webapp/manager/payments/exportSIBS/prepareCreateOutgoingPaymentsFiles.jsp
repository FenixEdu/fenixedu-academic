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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

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
