<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<em><bean:message key="title.assiduousness" /></em>
<br />
<h2><bean:message key="link.justifications" /></h2>
<br />
<br />
<logic:present name="yearMonth">
	<fr:form action="/assiduousnessRecords.do?method=showJustifications">
		<fr:edit name="yearMonth" schema="choose.date" />
		<br />
		<html:submit>
			<bean:message key="button.submit" />
		</html:submit>
	</fr:form>
</logic:present>
<br />
<br />
<logic:present name="leaves">
	<logic:empty name="leaves">
		<bean:message key="message.employee.noLeaves" />
	</logic:empty>
	<logic:notEmpty name="leaves">
		<fr:view name="leaves" schema="show.leaves">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1b" />
				<fr:property name="columnClasses" value="acenter" />
				<fr:property name="headerClasses" value="acenter" />
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
</logic:present>
<logic:notPresent name="leaves">
	<bean:message key="message.employee.noLeaves" />
</logic:notPresent>
