<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<bean:define id="processName" name="processName" />
<bean:define id="processId" name="process" property="externalId" />

<strong><bean:write name="process" property="displayName" /></strong>

<logic:notEmpty name="activities">
	<fr:view schema="caseHandling.list.activities" name="activities">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 mtop15" />
			<fr:property name="linkFormat(executeActivity)" value='<%= "/caseHandling" + processName.toString() + ".do?method=prepareExecute${id}&amp;processId=" + processId.toString()%>' />
			<fr:property name="key(executeActivity)" value="link.execute.activity" />
			<fr:property name="bundle(executeActivity)" value="APPLICATION_RESOURCES" />
		</fr:layout>
	</fr:view>
</logic:notEmpty>
<logic:empty name="activities">
	<br/>
	<em><strong><bean:message key="label.no.activities" bundle="APPLICATION_RESOURCES" /></strong></em>
	<br/>
</logic:empty>

<br/>
<html:form action='<%= "/caseHandling" + processName.toString() + ".do?method=listProcesses"%>'>
	<html:cancel><bean:message key='label.back' bundle="APPLICATION_RESOURCES"/></html:cancel>			
</html:form>
