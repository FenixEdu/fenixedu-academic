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

<bean:define id="processName" name="processName" />

<h2><bean:message key="label.candidacies" bundle="APPLICATION_RESOURCES"/></h2>

<%-- no candidacy process --%>
<logic:empty name="process">

	<logic:equal name="canCreateProcess" value="true">
		<html:link action='<%= "/caseHandling" + processName.toString() + ".do?method=prepareCreateNewProcess"%>'>
			<bean:message key='<%= "link.create.new.process." + processName.toString() %>' bundle="APPLICATION_RESOURCES"/>	
		</html:link>
	</logic:equal>

	<logic:empty name="executionIntervals">
		<strong><bean:message key="label.candidacy.no.candidacies" bundle="APPLICATION_RESOURCES" /></strong>
	</logic:empty>

	<logic:notEmpty name="executionIntervals">
		<br/>
		<br/>
		<html:form action='<%= "/caseHandling" + processName.toString() + ".do?method=intro" %>'>
			<table class="tstyle4 thlight thright mtop025">
				<tr>
					<th><bean:message key="label.executionYear" bundle="APPLICATION_RESOURCES" /></th>
					<td>
						<html:select bundle="HTMLALT_RESOURCES" property="executionIntervalId" onchange="this.form.submit();">
							<html:option value=""><!-- w3c complient --></html:option>
							<html:options collection="executionIntervals" property="externalId" labelProperty="qualifiedName"/>
						</html:select>
					</td>
				</tr>
				<logic:notEmpty name="candidacyProcesses">
				<tr>
					<th><bean:message key="label.candidacies" bundle="APPLICATION_RESOURCES" /></th>
					<td>
						<html:select bundle="HTMLALT_RESOURCES" property="selectedProcessId">
							<html:option value=""><!-- w3c complient --></html:option>
							<html:options collection="candidacyProcesses" property="externalId" labelProperty="candidacyPeriod.presentationName"/>
						</html:select>
					</td>
				</tr>
				</logic:notEmpty>
				<tr>
					<td> </td>
					<td><html:submit><bean:message key="label.choose" bundle="APPLICATION_RESOURCES"/> </html:submit></td>
				</tr>
			</table>
		</html:form>
		<br/>
		<br/>
	</logic:notEmpty>
</logic:empty>

<%-- candidacy process of current year --%>
<logic:notEmpty name="process">
	<h2><bean:write name="process" property="displayName" /> </h2>

	<bean:define id="processId" name="process" property="externalId" />
	<bean:define id="childProcessName" name="childProcessName" />
	<bean:size id="candidacyProcessesSize" name="candidacyProcesses" />


	<br/>
	<br/>

	<html:form action='<%= "/caseHandling" + processName.toString() + ".do?method=intro" %>'>
		<table class="tstyle4 thlight thright mtop025">
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
				<td> </td>
				<td><html:submit><bean:message key="label.choose" bundle="APPLICATION_RESOURCES"/> </html:submit></td>
			</tr>
		</table>
		
		<p/>		
		<fr:edit id="choose.degree.bean" name="chooseDegreeBean" schema="SecondCycleChooseDegreeBean.selectDegree" >
			<fr:destination name="postback" path="<%= "/caseHandling" + processName.toString() + ".do?method=intro" %>"/>
		</fr:edit>				
		
	</html:form>
	<br/>


	<%-- show main process information --%>
	<fr:view name="process" schema="CandidacyProcess.view">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
	        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
		</fr:layout>
	</fr:view>
	
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
		</ul>
	</logic:notEmpty>
	
	<%-- show child processes --%>
	<logic:notEmpty name="childProcesses">
		<br/>
		
		<fr:view name="childProcesses" schema="IndividualCandidacyProcess.list.processes">
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
		<em>(<bean:message key="label.Total" bundle="APPLICATION_RESOURCES" />: <strong><bean:write name="childProcessesSize" /></strong>)</em>
	</logic:notEmpty>
	
</logic:notEmpty>
