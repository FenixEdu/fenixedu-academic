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
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.candidacy.CandidacyProcessDA.HideCancelledCandidaciesBean" %>

<html:xhtml/>

<em><bean:message key="label.erasmus.candidacy" bundle="APPLICATION_RESOURCES"/></em>
<h2><bean:message key="title.application.name.erasmus" bundle="CANDIDATE_RESOURCES" /></h2>

<bean:define id="erasmusCandidacyProcess" name="erasmusCandidacyProcess" />
<bean:define id="parentProcessId" name="erasmusCandidacyProcess" property="externalId" />

<p>
	<html:link action='<%= "/caseHandlingMobilityApplicationProcess.do?method=listProcesses&amp;parentProcessId=" + parentProcessId.toString() %>'>
		« <bean:message key="label.back" bundle="APPLICATION_RESOURCES"/>	
	</html:link>
</p>

<p><strong><bean:message key="title.net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.reports.ErasmusCandidacyProcessReport.done" bundle="ACADEMIC_OFFICE_RESOURCES" /></strong></p>

<logic:empty name="erasmusCandidacyProcess" property="doneReports">
	<em><bean:message key="message.net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.reports.ErasmusCandidacyProcessReport.done.empty" bundle="ACADEMIC_OFFICE_RESOURCES" /></em>
</logic:empty>

<logic:notEmpty name="erasmusCandidacyProcess" property="doneReports">
	<fr:view name="erasmusCandidacyProcess" property="doneReports">
		<fr:schema bundle="ACADEMIC_OFFICE_RESOURCES" type="net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.reports.ErasmusCandidacyProcessReport">
			<fr:slot name="requestDate" />
			<fr:slot name="person.name" key="label.net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.reports.ErasmusCandidacyProcessReport.person.name" />
		</fr:schema>
		
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thlight thright mtop025"/>
			<fr:property name="sortBy" value="requestDate=desc" />

			 <fr:link label="label.net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.reports.ErasmusCandidacyProcessReport.view,ACADEMIC_OFFICE_RESOURCES" 
			 	name="view" link="/downloadQueuedJob.do?method=downloadFile&id=${externalId}" 
			 	module="" />
		</fr:layout>
		
	</fr:view>
</logic:notEmpty>

<p><strong><bean:message key="title.net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.reports.ErasmusCandidacyProcessReport.undone" bundle="ACADEMIC_OFFICE_RESOURCES" /></strong></p>

<logic:empty name="erasmusCandidacyProcess" property="undoneReports">
	<em><bean:message key="message.net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.reports.ErasmusCandidacyProcessReport.undone.empty" bundle="ACADEMIC_OFFICE_RESOURCES" /></em>
</logic:empty>

<logic:notEmpty name="erasmusCandidacyProcess" property="undoneReports">
	<fr:view name="erasmusCandidacyProcess" property="undoneReports">
		<fr:schema bundle="ACADEMIC_OFFICE_RESOURCES" type="net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.reports.ErasmusCandidacyProcessReport">
			<fr:slot name="requestDate" />
			<fr:slot name="person.name" key="label.net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.reports.ErasmusCandidacyProcessReport.person.name" />
			<fr:slot name="jobStartTime" />
			<fr:slot name="jobEndTime" />
			<fr:slot name="isNotDoneAndCancelled" />
		</fr:schema>	
		
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thlight thright mtop025"/>
			<fr:property name="sortBy" value="requestDate=desc" />
		</fr:layout>
		
		<fr:link label="label.net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.reports.ErasmusCandidacyProcessReport.cancel,ACADEMIC_OFFICE_RESOURCES" 
			name="cancel" link="/erasmusCandidacyProcessReport.do?method=cancelJob&amp;erasmusCandidacyProcessReportId=${externalId}" 
			condition="isNotDoneAndNotCancelled"
			confirmation="label.net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.reports.ErasmusCandidacyProcessReport.cancel.confirmation,ACADEMIC_OFFICE_RESOURCES"/>
	</fr:view>
</logic:notEmpty>

<logic:equal name="erasmusCandidacyProcess" property="ableToLaunchReportGenerationJob" value="true">
	<p>
		<html:link action="/erasmusCandidacyProcessReport.do?method=createNewJob" paramId="erasmusCandidacyProcessId" paramName="erasmusCandidacyProcess" paramProperty="externalId">
			<bean:message key="label.net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.reports.ErasmusCandidacyProcessReport.create" bundle="ACADEMIC_OFFICE_RESOURCES" />
		</html:link>
	</p>
</logic:equal>
