<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page isELIgnored="true"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ page import="org.fenixedu.academic.ui.struts.action.candidacy.CandidacyProcessDA.HideCancelledCandidaciesBean" %>

<html:xhtml/>

<bean:define id="processName" name="processName" />
<bean:define id="process" name="process" type="org.fenixedu.academic.domain.candidacyProcess.mobility.MobilityApplicationProcess" />
<bean:define id="processId" name="process" property="externalId" />
<bean:define id="childProcessName" name="childProcessName" />

<em><bean:message key="label.erasmus.candidacy" bundle="APPLICATION_RESOURCES"/></em> | 

<html:link action='<%= "/caseHandling" + processName.toString() + ".do?method=listProcessAllowedActivities&amp;processId=" + processId.toString() %>'>
	<bean:message key="label.back" bundle="APPLICATION_RESOURCES"/>	
</html:link>

<% final String error = (String) request.getAttribute("emailError"); %>

<logic:present name="emailError">
	<br/>
	<br/>
	<div class="infoop5_2">
		<%= error %>
	</div>
</logic:present>

<logic:equal name="sentEmail" value="true">
	<br/>
	<br/>
	<div class="infoop5">
		<bean:message bundle="CANDIDATE_RESOURCES" key="label.email.sent"/>
	</div>
</logic:equal>

<h2><bean:message key="title.application.name.erasmus" bundle="CANDIDATE_RESOURCES" /></h2>
	
	<bean:size id="candidacyProcessesSize" name="candidacyProcesses" />
		
		<logic:present role="role(MANAGER)">
			<ul>
				<li>
					<html:link action='<%= "/caseHandlingMobilityApplicationProcess.do?method=executeSendEmailToMissingShiftsProcesses&amp;processId=" + processId.toString() %>'>
						<bean:message key="label.erasmus.send.email.to.missing.shifts" bundle="ACADEMIC_OFFICE_RESOURCES" />
					</html:link>
				</li>
			</ul>
		</logic:present>
	<p><strong><bean:message key="title.erasmus.application.process.list" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
	
	<%-- show child processes --%>
	<logic:notEmpty name="process" property="childsWithMissingShifts" >
		<fr:view name="process" property="childsWithMissingShifts" schema="ErasmusIndividualCandidacy.missing.shifts.list">
			<fr:layout name="tabular-sortable">
				<fr:property name="classes" value="tstyle4 thcenter thcenter thcenter"/>
				<fr:property name="columnClasses" value="tdcenter, tdcenter, tdcenter, "/>

				<fr:property name="linkFormat(viewProcess)" value='<%= "/caseHandling" + childProcessName.toString() + ".do?method=listProcessAllowedActivities&amp;processId=${externalId}"%>' />
				<fr:property name="key(viewProcess)" value="label.candidacy.show.candidate"/>
				<fr:property name="bundle(viewProcess)" value="APPLICATION_RESOURCES"/>
							
				<fr:property name="sortParameter" value="sortBy"/>
	            <fr:property name="sortUrl" value='<%= "/caseHandling" + processName.toString() + ".do?method=prepareExecuteViewChildProcessWithMissingShifts&amp;processId=" + processId.toString() %>'/>
    	        <fr:property name="sortBy" value="<%= request.getParameter("sortBy") == null ? "candidacyState,candidacyDate=desc" : request.getParameter("sortBy") %>"/>
			</fr:layout>
		</fr:view>
		<bean:size id="childProcessesSize" name="childProcesses" />
		
		<p class="mvert05"><bean:message key="label.numberOfCandidates" bundle="APPLICATION_RESOURCES" />: <strong><%= process.getChildsWithMissingShifts().size() %></strong></p>
		
	</logic:notEmpty>
