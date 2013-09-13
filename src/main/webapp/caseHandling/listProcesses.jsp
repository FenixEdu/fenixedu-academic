<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

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
			<fr:property name="linkFormat(viewProcess)" value='<%= "/caseHandling" + processName.toString() + ".do?method=listProcessAllowedActivities&amp;processId=${externalId}"%>' />
			<fr:property name="key(viewProcess)" value="link.list.processes"/>
			<fr:property name="bundle(viewProcess)" value="APPLICATION_RESOURCES"/>
		</fr:layout>	
	</fr:view>
</logic:notEmpty>
