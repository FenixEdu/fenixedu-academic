<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>


<bean:define id="processName" name="processName"/>
<logic:equal name="canCreateProcess" value="true">
	<html:link action='<%= "/caseHandling" + processName.toString() + ".do?method=prepareCreateNewProcess"%>'>
		<bean:message key='<%= "link.create.new.process." + processName.toString()%>' bundle="APPLICATION_RESOURCES"/>	
	</html:link>
</logic:equal>


<fr:view schema="caseHandling.list.processes" name="processes">
	<fr:layout name="tabular">
		<fr:property name="linkFormat(viewProcess)" value='<%= "/caseHandling" + processName.toString() + ".do?method=listProcessAllowedActivities&amp;processId=${idInternal}"%>' />
		<fr:property name="key(viewProcess)" value="link.list.processes"/>
		<fr:property name="bundle(viewProcess)" value="APPLICATION_RESOURCES"/>
	</fr:layout>	
</fr:view>