<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em class="invisible"><bean:message key="title.assiduousness" /></em>
<h2><bean:message key="title.showVacations" /></h2>

<logic:present name="yearMonth">
	<bean:define id="month" name="yearMonth" property="month" />
	<bean:define id="year" name="yearMonth" property="year"/>

	<logic:present name="employee">
		<bean:define id="employeeNumber" name="employee" property="employeeNumber" />
		
		<p class="mtop2">
			<span class="error0 mtop0">
				<html:messages id="errorMessage" message="true" property="errorMessage">
					<bean:write name="errorMessage" />
				</html:messages>
			</span>
		</p>

		<p><bean:message key="label.show" />: <html:link
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
		</html:link>, <html:link
			page="<%="/viewEmployeeAssiduousness.do?method=showVacations&month="+month.toString()+"&year="+year.toString()+"&employeeNumber="+employeeNumber.toString()%>">
			<bean:message key="link.vacations" />
		</html:link></p>


		<span class="toprint"><br />
		</span>
		<fr:view name="employee" schema="show.employeeInformation">
			<fr:layout name="tabular">
				<fr:property name="classes" value="showinfo1 thbold" />
			</fr:layout>
		</fr:view>	

	<div class="mvert1 invisible">
	<fr:form action="/viewEmployeeAssiduousness.do?method=showVacations">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method"
			name="employeeForm" property="method" value="showVacations" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.employeeNumber"
			name="employeeForm" property="employeeNumber" value="<%= employeeNumber.toString() %>" />
		<fr:edit id="yearMonth" name="yearMonth" schema="choose.year">
			<fr:layout>
				<fr:property name="classes" value="thlight thright" />
			</fr:layout>
		</fr:edit>
		<p><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"
			styleClass="invisible">
			<bean:message key="button.submit" />
		</html:submit></p>
	</fr:form></div>
</logic:present>
	
	<div class="toprint">
	<p class="bold mbottom0"><bean:define id="month" name="yearMonth"
		property="month" /> <bean:message key="<%=month.toString()%>"
		bundle="ENUMERATION_RESOURCES" /> <bean:write name="yearMonth"
		property="year" /></p>
	<br />
	</div>

	<logic:messagesPresent message="true">
		<html:messages id="message" message="true" property="message">
			<p><span class="error0"><bean:write name="message" /></span></p>
		</html:messages>
	</logic:messagesPresent>


<logic:present name="employeeStatusList">
	<fr:view name="employeeStatusList" schema="show.employeeStatus">
		<fr:layout name="tabular">
			<fr:property name="classes" value="showinfo1 thbold" />
		</fr:layout>
	</fr:view>
</logic:present>

<logic:present name="vacations">
	<logic:empty name="vacations">
		<p>
			<em><bean:message key="message.employee.noVacationsResume" /></em>
		</p>
	</logic:empty>
	<logic:notEmpty name="vacations">
		<fr:view name="vacations" schema="show.vacations">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 printborder" />
				<fr:property name="columnClasses" value="aleft" />
				<fr:property name="headerClasses" value="aleft" />
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
</logic:present>
</logic:present>
<logic:notPresent name="vacations">
	<p>
		<em><bean:message key="message.employee.noVacationsResume" /></em>
	</p>
</logic:notPresent>
