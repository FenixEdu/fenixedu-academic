<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em class="invisible"><bean:message key="title.assiduousness" /></em>
<h2><bean:message key="label.schedule" /></h2>


<logic:present name="yearMonth">
	<logic:present name="employee">
		<bean:define id="month" name="yearMonth" property="month" />
		<bean:define id="year" name="yearMonth" property="year" />
		<bean:define id="employeeNumber" name="employee" property="employeeNumber" />
		
		<bean:define id="employee" name="employee"/>
		<bean:define id="yearMonth" name="yearMonth"/>
		<%
			request.setAttribute("employee", employee);
			request.setAttribute("yearMonth", yearMonth);
		%>
		<jsp:include page="common/consultEmployeeAssiduousnessMenu.jsp">
			<jsp:param name="month" value="<%=month.toString() %>" />
			<jsp:param name="year" value="<%=year.toString() %>" />
			<jsp:param name="yearMonthSchema" value="false" />
		</jsp:include>

		<span class="error0">
			<html:errors/>
			<html:messages id="message" message="true">
				<bean:write name="message" />
				<br />
			</html:messages>
		</span>

		<logic:present name="schedule">
			<bean:define id="schema" value="edit.schedule.dates" />
			<logic:equal name="schedule" property="canChangeBeginDate" value="false">
				<bean:define id="schema" value="edit.schedule.endDate" />
			</logic:equal>
			<fr:edit id="dates" name="schedule" schema="<%=schema.toString()%>" layout="tabular"
				action="<%="/viewEmployeeAssiduousness.do?method=showSchedule&month="+month.toString()+"&year="+year.toString()+"&employeeNumber="+employeeNumber.toString()%>">
				<fr:layout>
					<fr:property name="classes" value="tstyle5 thlight thright thmiddle"/>
					<fr:property name="columnClasses" value=",,tdclear tderror1"/>
				</fr:layout>
			</fr:edit>
		</logic:present>
	</logic:present>
</logic:present>
