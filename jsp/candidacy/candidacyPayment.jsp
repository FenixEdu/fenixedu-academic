<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<h2 class="mtop15 mbottom025"><bean:message key="label.payments.management" bundle="APPLICATION_RESOURCES"/></h2>

<bean:define id="processName" name="processName"/>
<bean:define id="processId" name="process" property="idInternal" />
<bean:define id="personId" name="process" property="candidacy.person.idInternal" />

<br/>

<ul>
	<li>
		<html:link action='<%= "payments.do?method=showOperations&amp;personId=" + personId.toString() %>' target="_blank">
			<bean:message key="label.payments.management" bundle="APPLICATION_RESOURCES"/>	
		</html:link>
	</li>
	<li>
		<html:link action='<%= "/caseHandling" + processName.toString() + ".do?method=listProcessAllowedActivities&amp;processId=" + processId.toString() %>'>
			<bean:message key="label.back" bundle="APPLICATION_RESOURCES"/>	
		</html:link>
	</li>
</ul>
