<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<bean:define id="processName" name="processName"/>
<logic:equal name="canCreateProcess" value="true">
	<br/>
	<html:link action='<%= "/caseHandling" + processName.toString() + ".do?method=prepareCreateNewProcess"%>'>
		<bean:message key='<%= "link.create.new.process." + processName.toString()%>' bundle="APPLICATION_RESOURCES"/>	
	</html:link>
</logic:equal>

<logic:notEmpty name="processes">
	<br/>
	<br/>
	<fr:view name="processes" schema="caseHandling.list.processes">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 mtop15" />
			<fr:property name="linkFormat(viewProcess)" value='<%= "/caseHandling" + processName.toString() + ".do?method=listProcessAllowedActivities&amp;processId=${idInternal}"%>' />
			<fr:property name="key(viewProcess)" value="link.list.processes"/>
			<fr:property name="bundle(viewProcess)" value="APPLICATION_RESOURCES"/>
		</fr:layout>	
	</fr:view>
</logic:notEmpty>
