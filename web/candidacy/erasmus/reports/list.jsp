<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.candidacy.CandidacyProcessDA.HideCancelledCandidaciesBean" %>

<html:xhtml/>

<em><bean:message key="label.erasmus.candidacy" bundle="APPLICATION_RESOURCES"/></em>
<h2><bean:message key="title.application.name.erasmus" bundle="CANDIDATE_RESOURCES" /></h2>

<bean:define id="erasmusCandidacyProcess" name="erasmusCandidacyProcess" />
<bean:define id="parentProcessId" name="erasmusCandidacyProcess" property="idInternal" />

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
