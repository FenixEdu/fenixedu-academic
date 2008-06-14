<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />

<logic:present role="MANAGER">

	<h2><bean:message bundle="MANAGER_RESOURCES" key="title.edit.execution.period" /></h2>
	
	<logic:notEmpty name="executionPeriod">

		<fr:edit name="executionPeriod" schema="EditExecutionPeriodSchema" action="/manageExecutionPeriods.do?method=prepare">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight"/>
			</fr:layout>				
			<fr:destination name="cancel" path="/manageExecutionPeriods.do?method=prepare"/>
		</fr:edit>

	</logic:notEmpty>

</logic:present>