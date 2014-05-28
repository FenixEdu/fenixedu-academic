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

<h2><bean:message bundle="MANAGER_RESOURCES" key="title.gratuity.reports" /></h2>

<fr:form action="/gratuityReports.do?method=listReports">
	<fr:edit id="gratuity.report.bean" name="gratuityReportBean" visible="false" />
	
	<fr:edit id="gratuity.report.bean" name="gratuityReportBean">
		<fr:schema bundle="MANAGER_RESOURCES" type="net.sourceforge.fenixedu.domain.accounting.report.GratuityReportBean">
			<fr:slot name="executionYear" key="label.gratuity.report.execution.year" layout="menu-select-postback">
				<fr:property name="providerClass"
					value="net.sourceforge.fenixedu.presentationTier.renderers.providers.ExecutionYearsProvider" />
				<fr:property name="format" value="${name}" />
				<fr:property name="destination" value="postback" />
			</fr:slot>
		</fr:schema>
		
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 tdleftm mtop05" />
			<fr:property name="columnClasses" value=",acenter,aright,aright" />
		</fr:layout>
		
		<fr:destination name="postback" path="/gratuityReports.do?method=listReports"/>
	</fr:edit>
</fr:form>

<p class="mtop15 mbottom05"><strong><bean:message bundle="MANAGER_RESOURCES" key="label.gratuity.reports-generated.list" /></strong></p>

<logic:empty name="generatedReports">
	<p><bean:message key="label.gratuity.reports.empty" bundle="MANAGER_RESOURCES" /></p>
</logic:empty>

<logic:notEmpty name="generatedReports">
	<fr:view name="generatedReports" >
		<fr:schema bundle="MANAGER_RESOURCES" type="net.sourceforge.fenixedu.domain.accounting.report.GratuityReportQueueJob">
			<fr:slot name="filename" key="label.gratuity.report.file.name" />
			<fr:slot name="requestDate" key="label.gratuity.report.request.date" />
			<fr:slot name="jobEndTime" key="label.gratuity.report.end.date" />
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 tdleftm mtop05" />
			<fr:property name="columnClasses" value=",acenter,aright,aright" />
    	    <fr:property name="sortBy" value="requestDate=desc"/>
			
			<fr:link label="label.gratuity.report.view,MANAGER_RESOURCES" name="view" 
				link="/downloadQueuedJob.do?method=downloadFile&id=${externalId}" module=""/>
		</fr:layout>
	</fr:view>
	
	<fr:view name="eventReports" >
		<fr:schema bundle="MANAGER_RESOURCES" type="net.sourceforge.fenixedu.domain.accounting.report.events.EventReportQueueJob">
			<fr:slot name="filename" key="label.gratuity.report.file.name" />
			<fr:slot name="requestDate" key="label.gratuity.report.request.date" />
			<fr:slot name="jobEndTime" key="label.gratuity.report.end.date" />
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 tdleftm mtop05" />
			<fr:property name="columnClasses" value=",acenter,aright,aright" />
    	    <fr:property name="sortBy" value="requestDate=desc"/>
			
			<fr:link label="label.gratuity.report.view,MANAGER_RESOURCES" name="view" 
				link="/downloadQueuedJob.do?method=downloadFile&id=${externalId}" module=""/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<p class="mtop15 mbottom05"><strong><bean:message bundle="MANAGER_RESOURCES" key="label.gratuity.reports.undone.jobs" /></strong></p>

<logic:empty name="notGeneratedReports">
	<p><bean:message key="label.gratuity.report.queue.jobs.empty" bundle="MANAGER_RESOURCES" /></p>
</logic:empty>

<logic:notEmpty name="notGeneratedReports">
<fr:view	name="notGeneratedReports" sortBy="requestDate=desc">
	<fr:schema bundle="MANAGER_RESOURCES" type="net.sourceforge.fenixedu.domain.accounting.report.GratuityReportQueueJob">
		<fr:slot name="requestDate" key="label.gratuity.report.request.date" />
		<fr:slot name="jobStartTime" key="label.gratuity.report.start.time" />
		<fr:slot name="jobEndTime" key="label.gratuity.report.end.time" />
		<fr:slot name="person.name" key="label.gratuity.report.requestor" />
		<fr:slot name="isNotDoneAndCancelled" key="label.gratuity.report.cancelled" />
		
	</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 tdleftm mtop05" />
		<fr:property name="columnClasses" value=",acenter,aright,aright" />

		<fr:link label="label.gratuity.report.cancel,MANAGER_RESOURCES" name="cancel" 
			link="/gratuityReports.do?method=cancelQueueJob&amp;queueJobId=${externalId}" 
			condition="isNotDoneAndNotCancelled"
			confirmation="message.gratuity.report.queue.job.cancel.confirmation,MANAGER_RESOURCES"/>
			
	</fr:layout>
</fr:view>
</logic:notEmpty>

<logic:equal value="true" name="canRequestReportGeneration">
	<p>
		<html:link action="/gratuityReports.do?method=prepareGenerateReport">
			<bean:message key="label.gratuity.report.queue.job.start" bundle="MANAGER_RESOURCES" />
		</html:link>
	</p>
</logic:equal>
