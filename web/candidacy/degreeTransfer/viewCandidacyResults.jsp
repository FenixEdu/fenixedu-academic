<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<em><bean:message key="label.candidacies" bundle="APPLICATION_RESOURCES"/></em>
<h2><bean:message key="label.candidacy.degreeTransfer" bundle="APPLICATION_RESOURCES"/></h2>

<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES">
	<span class="error0"> <bean:write name="message" /> </span>
	<br />
</html:messages>

<bean:define id="processId" name="process" property="idInternal" />
<bean:define id="processName" name="processName" />

<fr:form action='<%= "/caseHandling" + processName + ".do?processId=" + processId.toString() %>'>
	<html:hidden property="method" value="listProcessAllowedActivities" />

	<h3 class="mtop15 mbottom025"><bean:message key="label.candidacy.introduce.results" bundle="APPLICATION_RESOURCES"/></h3>
	<br/>
	<logic:notEmpty name="individualCandidaciesByDegree">
		<html:cancel><bean:message key="label.back" bundle="APPLICATION_RESOURCES" /></html:cancel>
	</logic:notEmpty>
	<br/>
	<br/>
	<logic:iterate id="entry" name="individualCandidaciesByDegree">
	
		<bean:define id="degree" name="entry" property="key"/>
		<bean:define id="individualCandidacyProcesses" name="entry" property="value"/>
		
		<bean:define id="degreeId" name="degree" property="idInternal" />
		<strong><bean:write name="degree" property="presentationName" /></strong>:
		<html:link action='<%= "/caseHandling" + processName + ".do?method=prepareExecuteIntroduceCandidacyResultsForDegree&amp;processId=" + processId.toString() + "&amp;degreeId=" + degreeId.toString() %>' ><bean:message key="label.candidacy.introduce.results" bundle="APPLICATION_RESOURCES" /></html:link>
		<br/>
		<fr:view name="individualCandidacyProcesses" schema="DegreeTransferIndividualCandidacyProcess.introduce.result.view">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 mtop025"/>
			</fr:layout>
		</fr:view>
		<br/>
	</logic:iterate>

	<html:cancel><bean:message key="label.back" bundle="APPLICATION_RESOURCES" /></html:cancel>
</fr:form>
