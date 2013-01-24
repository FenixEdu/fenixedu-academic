<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<em><bean:message key="label.candidacies" bundle="APPLICATION_RESOURCES"/></em>
<h2><bean:write name="process" property="displayName" /></h2>

<bean:define id="process" name="process" type="net.sourceforge.fenixedu.domain.candidacyProcess.secondCycle.SecondCycleCandidacyProcess" />
<bean:define id="processId" name="process" property="idInternal" />
<bean:define id="childProcessName" name="childProcessName" />
<bean:define id="processName" name="processName" />

<%-- show child processes --%>
<logic:present name="childsWithMissingRequiredDocuments" >
	<fr:view name="childsWithMissingRequiredDocuments" schema="SecondCycleIndividualCandidacyProcess.missing.required.documents.list">
		<fr:layout name="tabular-sortable">
			<fr:property name="classes" value="tstyle4 thcenter thcenter thcenter"/>
			<fr:property name="columnClasses" value="tdcenter, tdcenter, tdcenter, "/>

			<fr:property name="linkFormat(viewProcess)" value='<%= "/caseHandling" + childProcessName.toString() + ".do?method=listProcessAllowedActivities&amp;processId=${idInternal}"%>' />
			<fr:property name="key(viewProcess)" value="label.candidacy.show.candidate"/>
			<fr:property name="bundle(viewProcess)" value="APPLICATION_RESOURCES"/>
						
			<fr:property name="sortParameter" value="sortBy"/>
            <fr:property name="sortUrl" value='<%= "/caseHandling" + processName.toString() + ".do?method=listProcessAllowedActivities&amp;processId=" + processId.toString() %>'/>
   	        <fr:property name="sortBy" value="<%= request.getParameter("sortBy") == null ? "candidacyState,candidacyDate=desc" : request.getParameter("sortBy") %>"/>
		</fr:layout>
	</fr:view>
	<bean:size id="childProcessesSize" name="childsWithMissingRequiredDocuments" />
	
	<p class="mvert05"><bean:message key="label.numberOfCandidates" bundle="APPLICATION_RESOURCES" />: <strong><bean:write name="childProcessesSize"/></strong></p>
	
</logic:present>
