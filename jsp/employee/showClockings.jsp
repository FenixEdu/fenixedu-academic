<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<em><bean:message key="title.assiduousness" /></em>
<br />
<h2><bean:message key="link.clockings" /></h2>
<br />
<br />
<logic:present name="employee">
	<fr:view name="employee" schema="show.employeeInformation">
		<fr:layout name="tabular">
			<fr:property name="classes" value="examMap" />
		</fr:layout>
	</fr:view>
</logic:present>
<br />
<br />
<logic:present name="yearMonth">
	<fr:form action="/assiduousnessRecords.do?method=showClockings">
		<fr:edit name="yearMonth" schema="choose.date" />
		<br />
		<html:submit>
			<bean:message key="button.submit" />
		</html:submit>
	</fr:form>
</logic:present>
<br />
<br />
<logic:present name="clockings">
	<logic:empty name="clockings">
		<bean:message key="message.employee.noClocking" />
	</logic:empty>
	<logic:notEmpty name="clockings">
		<fr:view name="clockings" schema="show.clockingsDaySheet">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1b" />
				<fr:property name="columnClasses" value="aleft" />
				<fr:property name="headerClasses" value="acenter" />
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
</logic:present>
<logic:notPresent name="clockings">
	<bean:message key="message.employee.noClocking" />
</logic:notPresent>
