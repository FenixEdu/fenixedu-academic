<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<em><bean:message key="title.assiduousness" /></em>
<br />
<h2><bean:message key="link.workSheet" /></h2>
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
	<fr:form action="/assiduousnessRecords.do?method=showWorkSheet">
		<fr:edit name="yearMonth" schema="choose.date"/>
		<br />
		<html:submit styleClass="invisible" >
			<bean:message key="button.submit" />
		</html:submit>
	</fr:form>
</logic:present>
<br />
<br />
<logic:present name="workSheet">
	<logic:empty name="workSheet">
		<bean:message key="message.employee.noWorkSheet" />
	</logic:empty>
	<logic:notEmpty name="workSheet">
		<fr:view name="workSheet" schema="show.workDaySheet">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1b" />
				<fr:property name="columnClasses" value="aleft,aleft,aright,aright,aleft,aleft" />
				<fr:property name="headerClasses" value="acenter" />
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
</logic:present>
<logic:notPresent name="workSheet">
	<bean:message key="message.employee.noWorkSheet" />
</logic:notPresent>

<logic:notEmpty name="subtitles">
<em><bean:message key="label.subtitle"/>:</em>
<br/>
	<logic:iterate name="subtitles" id="subtile">
		<bean:write name="subtile"/><br/>
	</logic:iterate>
</logic:notEmpty>