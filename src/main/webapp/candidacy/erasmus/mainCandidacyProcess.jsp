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
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.candidacy.CandidacyProcessDA.ChooseDegreeBean" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.candidacy.CandidacyProcessDA.ChooseMobilityProgramBean" %>


<html:xhtml/>

<bean:define id="processName" name="processName" />

<h2><bean:message key="title.application.name.mobility" bundle="CANDIDATE_RESOURCES" /></h2>

<%-- no candidacy process --%>
<logic:empty name="process">

	<logic:equal name="canCreateProcess" value="true">
		<html:link action='<%= "/caseHandling" + processName.toString() + ".do?method=prepareCreateNewProcess"%>'>
			<bean:message key='<%= "link.create.new.process." + processName.toString()%>' bundle="APPLICATION_RESOURCES"/>	
		</html:link>
	</logic:equal>

	<logic:empty name="executionIntervals">
		<p><strong><bean:message key="label.candidacy.no.candidacies" bundle="APPLICATION_RESOURCES" /></strong></p>
	</logic:empty>

	<logic:notEmpty name="executionIntervals">
		<html:form action='<%= "/caseHandling" + processName.toString() + ".do?method=intro" %>'>
			<table class="tstyle5 thlight thright mbottom05">
				<tr>
					<th><bean:message key="label.executionYear" bundle="APPLICATION_RESOURCES" />:</th>
					<td>
						<html:select bundle="HTMLALT_RESOURCES" property="executionIntervalId" onchange="this.form.submit();">
							<html:option value=""><!-- w3c complient --></html:option>
							<html:options collection="executionIntervals" property="externalId" labelProperty="qualifiedName"/>
						</html:select>
					</td>
				</tr>
				<logic:notEmpty name="candidacyProcesses">
				<tr>
					<th><bean:message key="label.candidacies" bundle="APPLICATION_RESOURCES" />:</th>
					<td>
						<html:select bundle="HTMLALT_RESOURCES" property="selectedProcessId">
							<html:option value=""><!-- w3c complient --></html:option>
							<html:options collection="candidacyProcesses" property="externalId" labelProperty="candidacyPeriod.presentationName"/>
						</html:select>
					</td>
				</tr>
				</logic:notEmpty>
			</table>

			<p class="mtop05"><html:submit><bean:message key="label.choose"/></html:submit></p>			
		</html:form>

	</logic:notEmpty>
</logic:empty>

<%-- candidacy process of current year --%>
<logic:notEmpty name="process">

	<bean:define id="processId" name="process" property="externalId" />
	<bean:define id="childProcessName" name="childProcessName" />
	<bean:size id="candidacyProcessesSize" name="candidacyProcesses" />
	
	<bean:define id="degreeBean" name="chooseDegreeBean"/>
	<bean:define id="mobilityProgramBean" name="chooseMobilityProgramBean"/>
	<logic:present role="role(INTERNATIONAL_RELATION_OFFICE)">
	<logic:equal name="canCreateProcess" value="true">
		<p>
			<html:link action='<%= "/caseHandling" + processName.toString() + ".do?method=prepareCreateNewProcess"%>'>
				<bean:message key='<%= "link.create.new.process." + processName.toString()%>' bundle="APPLICATION_RESOURCES"/>	
			</html:link>
		</p>
	</logic:equal>
	</logic:present>

	<html:form action='<%= "/caseHandling" + processName.toString() + ".do?method=intro" %>'>

		<p class="mbottom05"><strong><bean:message key="label.erasmus.filter.applications.by" bundle="CANDIDATE_RESOURCES" /></strong></p>

		<table class="tstyle5 thlight thright mtop025 mbottom05 ulnomargin">
			<tr>
				<th><bean:message key="label.executionYear" bundle="APPLICATION_RESOURCES" /></th>
				<td>
					<html:select bundle="HTMLALT_RESOURCES" property="executionIntervalId" onchange="this.form.submit();">
						<html:option value=""><!-- w3c complient --></html:option>
						<html:options collection="executionIntervals" property="externalId" labelProperty="name"/>
					</html:select>
				</td>
			</tr>
			<logic:notEmpty name="candidacyProcesses">
				<logic:greaterThan name="candidacyProcessesSize" value="1">
					<tr>
						<th><bean:message key="label.candidacies" bundle="APPLICATION_RESOURCES" /></th>
						<td>
							<html:select bundle="HTMLALT_RESOURCES" property="selectedProcessId">
								<html:option value=""><!-- w3c complient --></html:option>
								<html:options collection="candidacyProcesses" property="externalId" labelProperty="candidacyPeriod.presentationName"/>
							</html:select>
						</td>
					</tr>
				</logic:greaterThan>
			</logic:notEmpty>
			<tr>
				<th><bean:message key="label.forSemester" bundle="APPLICATION_RESOURCES" /></th>
				<td><strong><bean:write name="process" property="forSemester.localizedName" /></strong></td>
			</tr>
			<tr>
				<td><bean:message key="label.hide.cancelled.candidacies" bundle="CANDIDATE_RESOURCES"/>?</td>
				<td> 
					<fr:edit id="hide.cancelled.candidacies" name="hideCancelledCandidacies" slot="value">
						<fr:layout name="radio-postback"/>
					</fr:edit>
				</td>
			</tr>					
		</table>
		
		<p><html:submit><bean:message key="label.choose"/></html:submit></p>
		<bean:define id="chooseDegreeBeanSchemaName" name="chooseDegreeBeanSchemaName" type="String" /> 
		<fr:edit id="choose.degree.bean" name="chooseDegreeBean" schema="<%= chooseDegreeBeanSchemaName %>" >
			<fr:destination name="postback" path="<%= "/caseHandling" + processName.toString() + ".do?method=intro" %>"/>
			<fr:layout>
				<fr:property name="classes" value="tstyle5 thlight"/>
				<fr:property name="columnClasses" value=",,tdclear tderror1"/>
			</fr:layout>
		</fr:edit>
		<bean:define id="chooseMobilityProgramBeanSchemaName" name="chooseMobilityProgramBeanSchemaName" type="String" />
		<fr:edit id="choose.mobility.program.bean" name="chooseMobilityProgramBean" schema="<%= chooseMobilityProgramBeanSchemaName %>" >
			<fr:destination name="postback" path="<%= "/caseHandling" + processName.toString() + ".do?method=intro" %>"/>
			<fr:layout>
				<fr:property name="classes" value="tstyle5 thlight"/>
				<fr:property name="columnClasses" value=",,tdclear tderror1"/>
			</fr:layout>
		</fr:edit>
		
	</html:form>
		
	
	<logic:notEmpty name="processActivities">
		<%-- list main process activities --%>
		<ul>
		<logic:iterate id="activity" name="processActivities">
			<bean:define id="activityName" name="activity" property="class.simpleName" />
			<li>
				<logic:notEmpty name="hideCancelledCandidacies">
					<bean:define id="hideCancelledCandidacies" name="hideCancelledCandidacies"/>
	 				<bean:define id="hideCancelledCandidaciesValue" > <%= ((HideCancelledCandidaciesBean) hideCancelledCandidacies).getValue().toString() %></bean:define>
					<html:link action='<%= "/caseHandling" + processName.toString() + ".do?method=prepareExecute" + activityName.toString() + "&amp;processId=" + processId.toString() + "&amp;hideCancelledCandidacies=" + hideCancelledCandidaciesValue %>'>
						<bean:message name="activity" property="class.name" bundle="CASE_HANDLING_RESOURCES" />
					</html:link>
				</logic:notEmpty>
				<logic:empty name="hideCancelledCandidacies">
					<html:link action='<%= "/caseHandling" + processName.toString() + ".do?method=prepareExecute" + activityName.toString() + "&amp;processId=" + processId.toString()  %>'>
						<bean:message name="activity" property="class.name" bundle="CASE_HANDLING_RESOURCES" />
					</html:link>
				</logic:empty>
			</li>
		</logic:iterate>
		<logic:present role="role(INTERNATIONAL_RELATION_OFFICE)">
			<li>
				<html:link action="<%= "/caseHandlingMobilityApplicationProcess.do?method=manageEmailTemplates&processId=" + processId %>">Manage Email Templates</html:link>
			</li>
		</logic:present>
		</ul>
	</logic:notEmpty>
	
	<logic:present role="role(INTERNATIONAL_RELATION_OFFICE)">
		<html:link action="/erasmusCandidacyProcessReport.do?method=list" paramId="erasmusCandidacyProcessId" paramName="process" paramProperty="externalId">
			<bean:message key="label.net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.reports.ErasmusCandidacyProcessReport" bundle="ACADEMIC_OFFICE_RESOURCES" />
		</html:link>
	</logic:present>
	
	<logic:present role="role(INTERNATIONAL_RELATION_OFFICE)">
		<%-- Show processes with not viewed learning agreements --%>
		<p><strong><bean:message key="title.erasmus.not.viewed.approved.learning.agreements.list" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
		<logic:empty name="process" property="processesWithNotViewedApprovedLearningAgreements">
			<p><bean:message key="message.erasmus.not.viewed.approved.learning.agreements.empty" bundle="ACADEMIC_OFFICE_RESOURCES" /></p>
		</logic:empty>
		
		<logic:notEmpty name="process" property="processesWithNotViewedApprovedLearningAgreements">
			<fr:view name="process" property="processesWithNotViewedApprovedLearningAgreements" schema="MobilityIndividualApplicationProcess.show.not.viewed.learning.agreements">
				<fr:layout name="tabular-sortable">
					<fr:property name="classes" value="tstyle4 thcenter thcenter thcenter"/>
					<fr:property name="columnClasses" value="tdcenter, tdcenter, tdcenter, "/>
				
					<fr:property name="sortParameter" value="sortBy"/>
		            <fr:property name="sortUrl" value='<%= "/caseHandling" + processName.toString() + ".do?method=listProcessAllowedActivities&amp;processId=" + processId.toString() %>'/>
	    	        <fr:property name="sortBy" value="<%= request.getParameter("sortBy") == null ? "candidacy.mostRecentApprovedLearningAgreement.uploadTime" : request.getParameter("sortBy") %>"/>
	
					<fr:property name="linkFormat(viewProcess)" value='<%= "/caseHandling" + childProcessName.toString() + ".do?method=listProcessAllowedActivities&amp;processId=${externalId}"%>' />
					<fr:property name="key(viewProcess)" value="label.candidacy.show.candidate"/>
					<fr:property name="bundle(viewProcess)" value="APPLICATION_RESOURCES"/>
					
				</fr:layout>
			</fr:view>
		</logic:notEmpty>
	</logic:present>
	
	<logic:present role="role(INTERNATIONAL_RELATION_OFFICE)">
		<%-- Show processes with not viewed learning agreements --%>
		<p><strong><bean:message key="title.erasmus.not.viewed.erasmus.alerts.process.list" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
		<logic:empty name="process" property="processesWithNotViewedAlerts">
			<p><bean:message key="message.erasmus.not.viewed.erasmus.alerts.process.empty" bundle="ACADEMIC_OFFICE_RESOURCES" /></p>
		</logic:empty>
		
		<logic:notEmpty name="process" property="processesWithNotViewedAlerts">
			<fr:view name="process" property="processesWithNotViewedAlerts" schema="MobilityIndividualApplicationProcess.show.not.viewed.alerts.processes">
				<fr:layout name="tabular-sortable">
					<fr:property name="classes" value="tstyle4 thcenter thcenter thcenter"/>
					<fr:property name="columnClasses" value="tdcenter, tdcenter, tdcenter, "/>
				
					<fr:property name="sortParameter" value="sortBy"/>
		            <fr:property name="sortUrl" value='<%= "/caseHandling" + processName.toString() + ".do?method=listProcessAllowedActivities&amp;processId=" + processId.toString() %>'/>
	    	        <fr:property name="sortBy" value="<%= request.getParameter("sortBy") == null ? "mostRecentAlert.whenCreated" : request.getParameter("sortBy") %>"/>
	
					<fr:property name="linkFormat(viewProcess)" value='<%= "/caseHandling" + childProcessName.toString() + ".do?method=listProcessAllowedActivities&amp;processId=${externalId}"%>' />
					<fr:property name="key(viewProcess)" value="label.candidacy.show.candidate"/>
					<fr:property name="bundle(viewProcess)" value="APPLICATION_RESOURCES"/>
					
				</fr:layout>
			</fr:view>
		</logic:notEmpty>
	</logic:present>
	
	<p><strong><bean:message key="title.erasmus.application.process.list" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
	<%-- create child process --%>
	<logic:present role="role(INTERNATIONAL_RELATION_OFFICE)">
		<logic:equal name="canCreateChildProcess" value="true">
			<html:link action='<%= "/caseHandling" + childProcessName.toString() + ".do?method=prepareCreateNewProcess&amp;parentProcessId=" + processId.toString() %>'>
				+ <bean:message key='<%= "link.create.new.process." + childProcessName.toString()%>' bundle="APPLICATION_RESOURCES"/>	
			</html:link>
		</logic:equal>
	</logic:present>
	
	<%-- show child processes --%>
	<logic:notEmpty name="childProcesses">
	
	<script type="text/javascript" src="<%= request.getContextPath() + "/javaScript/dataTables/media/js/jquery.dataTables.js"%>"></script>

		<script type="text/javascript" charset="utf-8">		
			$(document).ready(function() {
	    		$('.results').dataTable( {
	    			"iDisplayLength": false,
	    			"oLanguage" : {
	    				"sProcessing": "Processing...",
	    				"sLengthMenu": "Mostrar _MENU_ registos",
	    				"sZeroRecords": "No Records",
	    				"sInfo": "_START_ - _END_ of _TOTAL_",
	    				"sInfoEmpty": "0 - 0 de 0",
	    				"sInfoFiltered": "(filtrate of _MAX_ registers)",
	    				"sInfoPostFix": "",
	    				"sSearch": "Search",
	    				"sFirst": "First",
	    				"sPrevious": "Previous",
	    				"sNext": "Next",
	    				"sLast": "Last"},
	    			"aaSorting": false,
	    			"bPaginate" : false
	    		}
			 );
			});
		</script>
		
		<fr:view name="childProcesses">
			<fr:schema type="net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcess" bundle="APPLICATION_RESOURCES">
				<fr:slot name="candidacyDate" key="label.candidacy.date" />
				<fr:slot name="candidacy.mobilityStudentData.selectedOpening.mobilityAgreement.mobilityProgram.registrationAgreement.description" key="label.mobility.program" bundle="ACADEMIC_OFFICE_RESOURCES" />
				<fr:slot name="personalDetails.name" key="label.name" />			
				<fr:slot name="candidacy.mobilityStudentData.selectedOpening.degree.sigla" key="label.Degree" />
				<fr:slot name="processCode" key="label.process.id" bundle="CANDIDATE_RESOURCES"/>
				<fr:slot name="personalDetails.documentIdNumber" key="label.identificationNumber" />
				<fr:slot name="validatedByGri" key="label.erasmus.validated.by.gri" bundle="ACADEMIC_OFFICE_RESOURCES" />
				<fr:slot name="validatedByMobilityCoordinator" key="label.erasmus.validated.by.coordinator" bundle="ACADEMIC_OFFICE_RESOURCES" />
				<fr:slot name="erasmusCandidacyStateDescription" key="label.erasmus.candidacy.state.description" bundle="ACADEMIC_OFFICE_RESOURCES" />
				
				<logic:present role="role(MANAGER)">
					<fr:slot name="isCandidacyProcessWithEidentifer" key="label.erasmus.eidentifier" bundle="ACADEMIC_OFFICE_RESOURCES" />
				</logic:present>
				
			</fr:schema>
		
			<fr:layout name="tabular-sortable"> 
				<fr:property name="classes" value="tstyle4 thcenter thcenter thcenter results"/>
				<fr:property name="columnClasses" value="tdcenter, tdcenter, tdcenter, "/>
				<fr:property name="renderCompliantTable" value="true"/>
				<fr:property name="linkFormat(viewProcess)" value='<%= "/caseHandling" + childProcessName.toString() + ".do?method=listProcessAllowedActivities&amp;processId=${externalId}"%>' /> 
				<fr:property name="key(viewProcess)" value="label.candidacy.show.candidate"/>
				<fr:property name="bundle(viewProcess)" value="APPLICATION_RESOURCES"/>
							
				<fr:property name="sortParameter" value="sortBy"/>
	            <fr:property name="sortUrl" value='<%= "/caseHandling" + processName.toString() + ".do?method=listProcessAllowedActivities&amp;processId=" + processId.toString() + (((ChooseDegreeBean)degreeBean).getDegree() != null ? "&amp;degreeEid=" + ((ChooseDegreeBean)degreeBean).getDegree().getExternalId() : "") + (((ChooseMobilityProgramBean)mobilityProgramBean).getMobilityProgram() != null ? "&amp;mobilityProgramEid=" + ((ChooseMobilityProgramBean)mobilityProgramBean).getMobilityProgram().getExternalId() : "") %>'/>
    	        <fr:property name="sortBy" value="<%= request.getParameter("sortBy") == null ? "candidacyState,candidacyDate=desc" : request.getParameter("sortBy") %>"/> 
			</fr:layout>
		</fr:view>
		<bean:size id="childProcessesSize" name="childProcesses" />
		
		<p class="mvert05"><bean:message key="label.numberOfCandidates" bundle="APPLICATION_RESOURCES" />: <strong><bean:write name="childProcessesSize" /></strong></p>
		
	</logic:notEmpty>
	
</logic:notEmpty>
