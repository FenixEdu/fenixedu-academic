<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<h2><bean:message key="title.showClockings" /></h2>
<br />
<br />
<logic:present name="clockings">
	<logic:empty name="clockings">
		<bean:message key="message.employee.noClocking" />
	</logic:empty>
	<logic:notEmpty name="clockings">
		<fr:view name="clockings" schema="show.clocking">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1b" />
				<fr:property name="columnClasses" value="acenter" />
				<fr:property name="headerClasses" value="acenter" />
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
</logic:present>
<logic:notPresent name="clockings">
	<bean:message key="message.employee.noClocking" />
</logic:notPresent>
