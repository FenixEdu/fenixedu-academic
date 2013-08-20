<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.candidacy.CandidacyProcessDA.HideCancelledCandidaciesBean" %>

<html:xhtml/>

<bean:define id="processName" name="processName" />

<em><bean:message key="label.erasmus.candidacy" bundle="APPLICATION_RESOURCES"/></em>

<h2><bean:message key="title.application.name.erasmus" bundle="CANDIDATE_RESOURCES" /></h2>
	
	<bean:define id="process" name="process" type="net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityApplicationProcess" />
	<bean:define id="processId" name="process" property="externalId" />
	<bean:define id="childProcessName" name="childProcessName" />
	<bean:size id="candidacyProcessesSize" name="candidacyProcesses" />
		
		<logic:present role="MANAGER">
			<ul>
				<li>
					<html:link action='<%= "/caseHandlingMobilityApplicationProcess.do?method=executeSendEmailToMissingRequiredDocumentsProcesses&amp;processId=" + processId.toString() %>'>
						<bean:message key="label.erasmus.send.email.to.missing.required.documents" bundle="ACADEMIC_OFFICE_RESOURCES" />
					</html:link>
				</li>
			</ul>
		</logic:present>
	<p><strong><bean:message key="title.erasmus.application.process.list" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
	
	<%-- show child processes --%>
	<logic:notEmpty name="process" property="childsWithMissingRequiredDocuments" >
		<fr:view name="process" property="childsWithMissingRequiredDocuments" schema="ErasmusIndividualCandidacy.missing.required.documents.list">
			<fr:layout name="tabular-sortable">
				<fr:property name="classes" value="tstyle4 thcenter thcenter thcenter"/>
				<fr:property name="columnClasses" value="tdcenter, tdcenter, tdcenter, "/>

				<fr:property name="linkFormat(viewProcess)" value='<%= "/caseHandling" + childProcessName.toString() + ".do?method=listProcessAllowedActivities&amp;processId=${externalId}"%>' />
				<fr:property name="key(viewProcess)" value="label.candidacy.show.candidate"/>
				<fr:property name="bundle(viewProcess)" value="APPLICATION_RESOURCES"/>
							
				<fr:property name="sortParameter" value="sortBy"/>
	            <fr:property name="sortUrl" value='<%= "/caseHandling" + processName.toString() + ".do?method=listProcessAllowedActivities&amp;processId=" + processId.toString() %>'/>
    	        <fr:property name="sortBy" value="<%= request.getParameter("sortBy") == null ? "candidacyState,candidacyDate=desc" : request.getParameter("sortBy") %>"/>
			</fr:layout>
		</fr:view>
		<bean:size id="childProcessesSize" name="childProcesses" />
		
		<p class="mvert05"><bean:message key="label.numberOfCandidates" bundle="APPLICATION_RESOURCES" />: <strong><%= process.getChildsWithMissingRequiredDocuments().size() %></strong></p>
		
	</logic:notEmpty>
