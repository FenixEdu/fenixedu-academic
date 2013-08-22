<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml />

<logic:present role="(role(MANAGER) | role(OPERATOR))">

	<h2><bean:message bundle="MANAGER_RESOURCES" key="title.edit.execution.period" /></h2>
	
	<logic:notEmpty name="executionPeriod">
		<html:messages id="message" message="true" bundle="MANAGER_RESOURCES">
			<p><span class="error"><!-- Error messages go here --><bean:write name="message" /></span></p>
		</html:messages>
		<fr:edit name="executionPeriod" schema="EditExecutionPeriodSchema" action="/manageExecutionPeriods.do?method=prepare">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight"/>
			</fr:layout>				
			<fr:destination name="cancel" path="/manageExecutionPeriods.do?method=prepare"/>
		</fr:edit>

	</logic:notEmpty>

</logic:present>