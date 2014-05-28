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
