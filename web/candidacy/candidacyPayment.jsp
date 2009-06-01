<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<em><bean:message key="label.candidacies" bundle="APPLICATION_RESOURCES"/></em>
<h2 class="mtop15 mbottom025"><bean:message key="label.payments.management" bundle="APPLICATION_RESOURCES"/></h2>

<bean:define id="processName" name="processName"/>
<bean:define id="processId" name="process" property="idInternal" />

<br/>


<logic:equal name="process" property="isCandidacyInternal" value="true">
	<bean:define id="personId" name="process" property="candidacy.personalDetails.person.idInternal" />
	<html:link action='<%= "payments.do?method=showOperations&amp;personId=" + personId.toString() %>' target="_blank">
		<bean:message key="label.payments.management" bundle="APPLICATION_RESOURCES"/>	
	</html:link>
</logic:equal>

<logic:equal name="process" property="isCandidacyInternal" value="false">
	<bean:message key="label.payments.no.payments"/>
	<bean:message key="message.candidacy.not.bind.person.create.payment" bundle="CANDIDATE_RESOURCES"/>
	
	<p>
	<html:link action='<%= "/caseHandling" + processName.toString() + ".do?method=prepareExecuteBindPersonToCandidacy&amp;processId=" + processId.toString() %>'>
		<bean:message key="label.bind.person" bundle="CANDIDATE_RESOURCES"/>	
	</html:link>
	</p>
	
</logic:equal>

<p>
<html:link action='<%= "/caseHandling" + processName.toString() + ".do?method=listProcessAllowedActivities&amp;processId=" + processId.toString() %>'>
	<bean:message key="label.back" bundle="APPLICATION_RESOURCES"/>	
</html:link>
</p>