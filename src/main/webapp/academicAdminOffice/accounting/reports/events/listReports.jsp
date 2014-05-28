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

<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="title.event.reports" /></h2>

<logic:messagesPresent message="true" property="success">
	<div class="mvert15">
		<span class="success0">
			<html:messages id="messages" message="true" bundle="ACADEMIC_OFFICE_RESOURCES" property="success">
				<bean:write name="messages" />
			</html:messages>
		</span>
	</div>
</logic:messagesPresent>

<logic:messagesPresent message="true" property="error">
	<div class="error3 mbottom05" style="width: 700px;">
		<html:messages id="messages" message="true" bundle="ACADEMIC_OFFICE_RESOURCES" property="error">
			<p class="mvert025"><bean:write name="messages" /></p>
		</html:messages>
	</div>
</logic:messagesPresent>


<p><strong><bean:message key="title.event.reports.done" bundle="ACADEMIC_OFFICE_RESOURCES" /></strong></p>

<logic:empty name="doneJobs">
	<p><em><bean:message key="message.event.reports.done.empty" bundle="ACADEMIC_OFFICE_RESOURCES" /></em></p>
</logic:empty>

<p>
	<html:link action="/eventReports.do?method=prepareCreateReportRequest">
		<bean:message key="link.event.reports.request.create" bundle="ACADEMIC_OFFICE_RESOURCES" />
	</html:link>
</p>

<logic:notEmpty name="doneJobs">
	<fr:view name="doneJobs">
		<fr:schema type="net.sourceforge.fenixedu.domain.accounting.report.events.EventReportQueueJob" bundle="ACADEMIC_OFFICE_RESOURCES">
			<fr:slot name="filename" />
			<fr:slot name="person.name" />
			<fr:slot name="requestDate" />
			<fr:slot name="jobEndTime" />
			<fr:slot name="forAdministrativeOffice.unit.name" />
		</fr:schema>
		
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 tdleftm mtop05" />
			<fr:property name="columnClasses" value=",acenter,aright,aright" />
    	    <fr:property name="sortBy" value="requestDate=desc"/>

			<fr:link label="label.view,APPLICATION_RESOURCES" name="view"
				link="/eventReports.do?method=viewRequest&queueJobId=${externalId}" order="1"/>
			
			<fr:link label="label.event.reports.file.download,ACADEMIC_OFFICE_RESOURCES" name="download" 
				link="/downloadQueuedJob.do?method=downloadFile&id=${externalId}" module="" order="2" condition="!debtsReportPresent"/>

			<fr:link label="label.event.reports.file.debts,ACADEMIC_OFFICE_RESOURCES" name="debts" 
				link="${debts.downloadUrl}" hasContext="false" contextRelative="false" order="2" condition="debtsReportPresent"/>

			<fr:link label="label.event.reports.file.exemptions,ACADEMIC_OFFICE_RESOURCES" name="exemptions" 
				link="${exemptions.downloadUrl}" hasContext="false" contextRelative="false" order="2" condition="exemptionsReportPresent"/>

			<fr:link label="label.event.reports.file.transactions,ACADEMIC_OFFICE_RESOURCES" name="transactions" 
				link="${transactions.downloadUrl}" hasContext="false" contextRelative="false" order="2" condition="transactionsReportPresent"/>

			<fr:link label="link.event.reports.file.view.errors,ACADEMIC_OFFICE_RESOURCES" name="errorsFile" 
				link="${errorsFile.downloadUrl}" hasContext="false" contextRelative="false" order="3" condition="errorReportPresent"/>
		</fr:layout>				
	</fr:view>
</logic:notEmpty>

<p><strong><bean:message key="title.event.reports.pending.or.cancelled" bundle="ACADEMIC_OFFICE_RESOURCES" /></strong></p>

<logic:empty name="pendingOrCancelledJobs">
	<p><em><bean:message key="message.event.reports.pending.or.cancelled.empty" bundle="ACADEMIC_OFFICE_RESOURCES" /></em></p>
</logic:empty>

<logic:notEmpty name="pendingOrCancelledJobs">
	<fr:view name="pendingOrCancelledJobs">

		<fr:schema type="net.sourceforge.fenixedu.domain.accounting.report.events.EventReportQueueJob" bundle="ACADEMIC_OFFICE_RESOURCES">
			<fr:slot name="requestDate" />
			<fr:slot name="jobStartTime" />
			<fr:slot name="person.name" />
			<fr:slot name="forAdministrativeOffice.unit.name" />
			<fr:slot name="isNotDoneAndCancelled" />				
		</fr:schema>

		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 tdleftm mtop05" />
			<fr:property name="columnClasses" value=",acenter,aright,aright" />

			<fr:link label="label.view,APPLICATION_RESOURCES" name="view" 
				link="/eventReports.do?method=viewRequest&queueJobId=${externalId}" />

			<fr:link label="label.event.reports.cancel,ACADEMIC_OFFICE_RESOURCES" name="cancel" 
				link="/eventReports.do?method=cancelReportRequest&amp;queueJobId=${externalId}" 
				condition="isNotDoneAndNotCancelled"
				confirmation="message.event.reports.cancel.confirmation,ACADEMIC_OFFICE_RESOURCES"/>
				
		</fr:layout>
		
	</fr:view>
</logic:notEmpty>

