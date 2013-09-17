<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:xhtml/>

<em><bean:message key="label.candidacies" bundle="APPLICATION_RESOURCES"/></em>
<h2><bean:message key="label.candidacy.edit" bundle="APPLICATION_RESOURCES"/></h2>

<bean:define id="processId" name="process" property="externalId" />
<bean:define id="processName" name="processName" />


<html:link action='<%= "/caseHandling" + processName.toString() + ".do?method=listProcessAllowedActivities&amp;processId=" + processId.toString() %>'>
	<bean:message key="label.back" bundle="APPLICATION_RESOURCES"/>	
</html:link>
<br/>

<fr:form action='<%="/caseHandling" + processName + ".do?method=executeChangeProcessCheckedState&processId=" + processId.toString() %>' >
 	<fr:edit id="individualCandidacyProcessBean" name="individualCandidacyProcessBean" visible="false"/>
 	
 	<bean:message key="label.process.checked" bundle="CANDIDATE_RESOURCES"/>:
 	<fr:edit id="individualCandidacyProcessBean.processChecked" name="individualCandidacyProcessBean" slot="processChecked"/>
 	
 	<p/>
	<html:submit><bean:message key="button.submit" bundle="APPLICATION_RESOURCES" /></html:submit>		
</fr:form>
 	