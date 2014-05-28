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
<%@ page isELIgnored="true"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:xhtml />

<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments" /></h2>

<p class="mtop15 mbottom05"><strong><bean:message bundle="MANAGER_RESOURCES" key="label.sibs.outgoing.payment.file.list" /></strong></p>

<logic:empty name="sibsOutgoingPaymentFiles">
	<p><bean:message key="label.sibs.outgoing.payment.files.empty" bundle="MANAGER_RESOURCES" /></p>
</logic:empty>


<logic:notEmpty name="sibsOutgoingPaymentFiles">
<fr:view	name="sibsOutgoingPaymentFiles" >
	<fr:schema bundle="MANAGER_RESOURCES" type="net.sourceforge.fenixedu.domain.accounting.events.export.SIBSOutgoingPaymentFile">
		<fr:slot name="filename" key="label.sibs.outgoing.payment.file.name" />
		<fr:slot name="uploadTime" key="label.sibs.outgoing.payment.upload.time" />
		<fr:slot name="successfulSentDate" key="label.sibs.outgoing.payment.successfulSent" />
		<fr:slot name="this" key="label.sibs.outgoing.payment.file.view" layout="link"/>
		
		<fr:link label="label.sibs.outgoing.payment.successfulSent.view" name="view" 
			link="/exportSIBSPayments.do?method=viewOutgoingPaymentFile&amp;paymentFileId=${externalId}"/>
	</fr:schema>
	<fr:layout name="tabular-sortable">
		<fr:property name="classes" value="tstyle4 tdleftm mtop05" />
		<fr:property name="columnClasses" value=",acenter,aright,aright" />
	
		<fr:property name="sortParameter" value="sortBy"/>
        <fr:property name="sortUrl" value="/exportSIBSPayments.do?method=listOutgoingPaymentsFile" />
	    <fr:property name="sortBy" value="uploadTime=desc"/>
		<fr:property name="sortableSlots" value="filename, uploadTime, successfulSentDateTime" />
		
		<fr:link label="label.sibs.outgoing.payment.file.detail,MANAGER_RESOURCES" name="detail" 
					link="/exportSIBSPayments.do?method=viewOutgoingPaymentFile&amp;paymentFileId=${externalId}"/>
	</fr:layout>
</fr:view>
</logic:notEmpty>

<p class="mtop15 mbottom05"><strong><bean:message bundle="MANAGER_RESOURCES" key="label.sibs.outgoing.payment.file.jobs" /></strong></p>

<logic:empty name="sibsOutgoingPaymentQueueJobs">
	<p><bean:message key="label.sibs.outgoing.payment.queue.jobs.empty" bundle="MANAGER_RESOURCES" /></p>
</logic:empty>

<logic:notEmpty name="sibsOutgoingPaymentQueueJobs">
<fr:view	name="sibsOutgoingPaymentQueueJobs" sortBy="requestDate=desc">
	<fr:schema bundle="MANAGER_RESOURCES" type="net.sourceforge.fenixedu.domain.accounting.events.export.SIBSOutgoingPaymentQueueJob">
		<fr:slot name="requestDate" key="label.sibs.outgoing.payment.queue.job.request.date" />
		<fr:slot name="jobStartTime" key="label.sibs.outgoing.payment.queue.job.start.time" />
		<fr:slot name="jobEndTime" key="label.sibs.outgoing.payment.queue.job.end.time" />
		<fr:slot name="person.name" key="label.sibs.outgoing.payment.queue.job.requestor" />
		<fr:slot name="isNotDoneAndCancelled" key="label.sibs.outgoing.payment.queue.job.cancelled" />
		
	</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 tdleftm mtop05" />
		<fr:property name="columnClasses" value=",acenter,aright,aright" />

		<fr:link label="label.sibs.outgoing.payment.queue.job.cancel,MANAGER_RESOURCES" name="cancel" 
			link="/exportSIBSPayments.do?method=cancelQueueJob&amp;queueJobId=${externalId}" 
			condition="isNotDoneAndNotCancelled"
			confirmation="message.sibs.outgoing.payment.queue.job.cancel.confirmation,MANAGER_RESOURCES"/>
			
	</fr:layout>
</fr:view>
</logic:notEmpty>

<logic:equal value="true" name="canLaunchJob">
	<p>
		<html:link action="/exportSIBSPayments.do?method=prepareCreateOutgoingPaymentsFile">
			<bean:message key="label.sibs.outgoing.payment.queue.job.start" bundle="MANAGER_RESOURCES" />
		</html:link>
	</p>
</logic:equal>
