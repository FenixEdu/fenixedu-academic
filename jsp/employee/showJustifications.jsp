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
	<bean:define id="month" name="yearMonth" property="month"/>
	<bean:message key="<%=month.toString()%>" bundle="ENUMERATION_RESOURCES"/>
	<bean:write name="yearMonth" property="year"/>
	<br/><br/>
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
<logic:present name="justifications">
	<logic:empty name="justifications">
		<bean:message key="message.employee.noJustifications" />
	</logic:empty>
	<logic:notEmpty name="justifications">
		<fr:view name="justifications" schema="show.justifications">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1b" />
				<fr:property name="columnClasses" value="acenter" />
				<fr:property name="headerClasses" value="acenter" />
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
</logic:present>
<logic:notPresent name="justifications">
	<bean:message key="message.employee.noJustifications" />
</logic:notPresent>
