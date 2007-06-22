<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em class="invisible"><bean:message key="title.assiduousness" /></em>
<h2><bean:message key="title.associateExceptionSchedule" /></h2>

<bean:define id="month" name="yearMonth" property="month" />
<bean:define id="year" name="yearMonth" property="year" />
<bean:define id="employeeNumber" name="employeeExceptionScheduleBean" property="employee.employeeNumber" />

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
</html:link></p>

<span class="toprint"><br />
</span>
<fr:view name="employeeExceptionScheduleBean" property="employee" schema="show.employeeInformation">
	<fr:layout name="tabular">
		<fr:property name="classes" value="showinfo1 thbold" />
	</fr:layout>
</fr:view>

<%--
<div class="infoop2 mbottom15">
<logic:iterate id="workWeekScheduleBean" name="employeeScheduleBean" property="employeeWorkWeekScheduleList">
	<p>
	<span style="padding-right: 1em;">
	<bean:message key="label.week"/>: <bean:write name="workWeekScheduleBean" property="workWeekNumber"/>
	</span>
	<logic:notEmpty name="workWeekScheduleBean" property="workWeekByCheckedBox">
		<bean:size id="realSize" name="workWeekScheduleBean" property="workWeekByCheckedBox.days"/>
		<bean:define id="size" value="<%= new Integer(realSize -1).toString() %>"/>
		<bean:message key="label.days"/>:
			<logic:iterate indexId="index" id="day" name="workWeekScheduleBean" property="workWeekByCheckedBox.days">
				<bean:message name="day" property="name" bundle="ENUMERATION_RESOURCES"/><logic:notEqual name="index" value="<%= size %>">,	</logic:notEqual>
			</logic:iterate>
		</p>
	</logic:notEmpty>
</logic:iterate>
</div>
--%>

<span class="error0">
	<html:errors/>
	<html:messages id="message" message="true">
		<bean:write name="message" />
		<br />
	</html:messages>
</span>
	
<fr:form action="/employeeExceptionSchedule.do?method=associateExceptionWorkSchedule">

	<fr:edit id="dates" name="employeeExceptionScheduleBean" schema="edit.employeeExceptionScheduleBean.dates" layout="tabular">
		<fr:layout>
			<fr:property name="classes" value="tstyle5 thlight thright thmiddle"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>
	</fr:edit>
	
	<logic:notEmpty name="employeeExceptionScheduleBean" property="schedule">
		<p><html:submit property="changeDates"><bean:message key="button.changeDates" bundle="ASSIDUOUSNESS_RESOURCES"/></html:submit></p>
	</logic:notEmpty>
	<logic:empty name="employeeExceptionScheduleBean" property="schedule">
		<span class="hidden"><html:submit/></span>
	</logic:empty>

	<bean:message key="label.chooseSchedule" bundle="ASSIDUOUSNESS_RESOURCES"/>:
	<fr:edit id="employeeExceptionScheduleBean" name="employeeExceptionScheduleBean">
		<fr:layout name="pages">
			<fr:property name="classes" value="tstyle1 printborder tdleft"/>
			<fr:property name="paged" value="false"/>
			<fr:property name="subSchema" value="show.workScheduleType"/>
			<fr:property name="buttonLabel" value="button.submit"/>
			<fr:property name="bundle" value="ASSIDUOUSNESS_RESOURCES"/>
		</fr:layout>
	</fr:edit>
</fr:form>