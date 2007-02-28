<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em class="invisible"><bean:message key="title.assiduousness" /></em>
<h2><bean:message key="link.clockings" /></h2>

<logic:present name="yearMonth">
	<logic:present name="employee">
		<bean:define id="month" name="yearMonth" property="month" />
		<bean:define id="year" name="yearMonth" property="year" />
		<bean:define id="employeeNumber" name="employee" property="employeeNumber" />
		<p><bean:message key="label.show"/>: <html:link
			page="<%="/viewEmployeeAssiduousness.do?method=showWorkSheet&month="+month.toString()+"&year="+year.toString()+"&employeeNumber="+employeeNumber.toString()%>">
			<bean:message key="link.workSheet" />
		</html:link>, <html:link
			page="<%="/viewEmployeeAssiduousness.do?method=showSchedule&month="+month.toString()+"&year="+year.toString()+"&employeeNumber="+employeeNumber.toString()%>">
			<bean:message key="label.schedule" />
		</html:link>, <html:link
			page="<%="/viewEmployeeAssiduousness.do?method=showClockings&month="+month.toString()+"&year="+year.toString()+"&employeeNumber="+employeeNumber.toString()%>">
			<bean:message key="link.clockings" />
		</html:link>, <html:link
			page="<%="/viewEmployeeAssiduousness.do?method=showJustifications&month="+month.toString()+"&year="+year.toString()+"&employeeNumber="+employeeNumber.toString()%>">
			<bean:message key="link.justifications" />
		</html:link></p>
		<span class="toprint"><br />
		</span>
		<fr:view name="employee" schema="show.employeeInformation">
			<fr:layout name="tabular">
				<fr:property name="classes" value="showinfo1 thbold" />
			</fr:layout>
		</fr:view>

	<logic:messagesPresent message="true">
		<html:messages id="message" message="true">
			<p><span class="error0"><bean:write name="message" /></span></p>
		</html:messages>
	</logic:messagesPresent>

	<div class="mvert1 invisible">
	<fr:form action="/viewEmployeeAssiduousness.do?method=showClockings">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method"
				name="employeeForm" property="method" value="showClockings" />
			<html:hidden bundle="HTMLALT_RESOURCES"
				altKey="hidden.employeeNumber" name="employeeForm"
				property="employeeNumber" value="<%=employeeNumber.toString()%>"/>
		<fr:edit id="yearMonth" name="yearMonth" schema="choose.date">
			<fr:layout>
				<fr:property name="classes" value="thlight thright" />
			</fr:layout>
		</fr:edit>
		<p><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"
			styleClass="invisible">
			<bean:message key="button.submit" />
		</html:submit></p>
	</fr:form></div>

	<div class="toprint">
	<p class="bold mbottom0"><bean:define id="month" name="yearMonth"
		property="month" /> <bean:message key="<%=month.toString()%>"
		bundle="ENUMERATION_RESOURCES" /> <bean:write name="yearMonth"
		property="year" /></p>
	<br />
	</div>
	
	<fr:view name="employeeStatusList" schema="show.employeeStatus">
		<fr:layout name="tabular">
			<fr:property name="classes" value="showinfo1 thbold" />
		</fr:layout>
	</fr:view>
	</logic:present>
</logic:present>


<logic:present name="clockings">
	<logic:empty name="clockings">
		<p>
			<em><bean:message key="message.employee.noClocking" /></em>
		</p>
	</logic:empty>
	<logic:notEmpty name="clockings">
		<fr:view name="clockings" schema="show.clockingsDaySheet.assiduosunessManager">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 printborder" />
				<fr:property name="columnClasses"
					value="bgcolor3 acenter,acenter,aleft,aleft" />
				<fr:property name="headerClasses" value="acenter" />
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
</logic:present>
<logic:notPresent name="clockings">
	<p>
		<em><bean:message key="message.employee.noClocking" /></em>
	</p>
</logic:notPresent>
