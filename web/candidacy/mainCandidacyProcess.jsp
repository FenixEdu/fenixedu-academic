<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<bean:define id="processName" name="processName" />

<%-- no candidacy process for current year --%>
<logic:empty name="process">
	<h2><bean:message key="label.candidacies" bundle="APPLICATION_RESOURCES"/></h2>
	
	<bean:message key="label.candidacy.no.candidacies.for.current.year" bundle="APPLICATION_RESOURCES" />
	: <logic:equal name="canCreateProcess" value="true">
		<html:link action='<%= "/caseHandling" + processName.toString() + ".do?method=prepareCreateNewProcess"%>'>
		<bean:message key='<%= "link.create.new.process." + processName.toString()%>' bundle="APPLICATION_RESOURCES"/>	
		</html:link>
	</logic:equal>
	<%-- previous years candidacy --%>
	<br/>
	<br/>
	<html:form action='<%= "/caseHandling" + processName.toString() + ".do?method=intro" %>'>
		<html:select bundle="HTMLALT_RESOURCES" property="executionIntervalId">
			<html:option value=""><!-- w3c complient --></html:option>
			<html:options collection="executionIntervals" property="idInternal" labelProperty="name"/>
		</html:select>
		<html:submit><bean:message key="label.choose"/> </html:submit>
	</html:form>
	<br/>
	<br/>
</logic:empty>

<%-- candidacy process of current year --%>
<logic:notEmpty name="process">
	<em><bean:message key="label.candidacies" bundle="APPLICATION_RESOURCES"/></em>
	<h2><bean:write name="process" property="displayName" /> </h2>

	<bean:define id="processId" name="process" property="idInternal" />
	<bean:define id="childProcessName" name="childProcessName" />
	
	<%-- show execution intervals  --%>
	<bean:define id="executionIntervalId" name="executionIntervalId" />
	<bean:size id="executionIntervalsSize" name="executionIntervals" />

	<logic:greaterThan name="executionIntervalsSize" value="1">
		<br/>
		<html:form action='<%= "/caseHandling" + processName.toString() + ".do?method=intro" %>'>
			<html:select bundle="HTMLALT_RESOURCES" property="executionIntervalId" value="<%= executionIntervalId.toString() %>">
				<html:option value=""><!-- w3c complient --></html:option>
				<html:options collection="executionIntervals" property="idInternal" labelProperty="name"/>
			</html:select>
			<html:submit><bean:message key="label.choose"/> </html:submit>
		</html:form>
		<br/>
	</logic:greaterThan>

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
				<html:link action='<%= "/caseHandling" + processName.toString() + ".do?method=prepareExecute" + activityName.toString() + "&amp;processId=" + processId.toString()%>'>
					<bean:message name="activity" property="class.name" bundle="CASE_HANDLING_RESOURCES" />
				</html:link>
			</li>
		</logic:iterate>
		</ul>
	</logic:notEmpty>
	
	<%-- create child process --%>
	<logic:equal name="canCreateChildProcess" value="true">
		<br/>
		<html:link action='<%= "/caseHandling" + childProcessName.toString() + ".do?method=prepareCreateNewProcess&amp;parentProcessId=" + processId.toString() %>'>
			+ <bean:message key='<%= "link.create.new.process." + childProcessName.toString()%>' bundle="APPLICATION_RESOURCES"/>	
		</html:link>
	</logic:equal>
	
	<%-- show child processes --%>
	<logic:notEmpty name="childProcesses">
		<br/>
		<fr:view name="childProcesses" schema="IndividualCandidacyProcess.list.processes">
			<fr:layout name="tabular-sortable">
				<fr:property name="classes" value="tstyle4 thcenter thcenter thcenter"/>
				<fr:property name="columnClasses" value="tdcenter, tdcenter, tdcenter, "/>

				<fr:property name="linkFormat(viewProcess)" value='<%= "/caseHandling" + childProcessName.toString() + ".do?method=listProcessAllowedActivities&amp;processId=${idInternal}"%>' />
				<fr:property name="key(viewProcess)" value="label.candidacy.show.candidate"/>
				<fr:property name="bundle(viewProcess)" value="APPLICATION_RESOURCES"/>
				<fr:property name="visibleIfNot(viewProcess)" value="candidacyCancelled" />
							
				<fr:property name="sortParameter" value="sortBy"/>
	            <fr:property name="sortUrl" value='<%= "/caseHandling" + processName.toString() + ".do?method=intro&amp;processId=" + processId.toString() + "&amp;executionIntervalId=" + executionIntervalId.toString() %>'/>
    	        <fr:property name="sortBy" value="<%= request.getParameter("sortBy") == null ? "candidacyState,candidacyDate=desc" : request.getParameter("sortBy") %>"/>
			</fr:layout>
		</fr:view>
		<bean:size id="childProcessesSize" name="childProcesses" />
		<em>(<bean:message key="label.Total" bundle="APPLICATION_RESOURCES" />: <strong><bean:write name="childProcessesSize" /></strong>)</em>
	</logic:notEmpty>
	
</logic:notEmpty>
