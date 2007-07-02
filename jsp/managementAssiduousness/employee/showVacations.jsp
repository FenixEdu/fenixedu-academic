<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em class="invisible"><bean:message key="title.assiduousness" /></em>
<h2><bean:message key="title.showVacations" /></h2>

<logic:present name="yearMonth">
	<logic:present name="employee">
		<bean:define id="month" name="yearMonth" property="month" />
		<bean:define id="year" name="yearMonth" property="year" />
		<bean:define id="employeeNumber" name="employee" property="employeeNumber" />
		
		<bean:define id="employee" name="employee"/>
		<bean:define id="employeeStatusList" name="employeeStatusList"/>
		<bean:define id="yearMonth" name="yearMonth"/>
		<%request.setAttribute("employee", employee);
		request.setAttribute("employeeStatusList", employeeStatusList);
		request.setAttribute("yearMonth", yearMonth);%>
		<jsp:include page="common/consultEmployeeAssiduousnessMenu.jsp">
			<jsp:param name="month" value="<%=month.toString() %>" />
			<jsp:param name="year" value="<%=year.toString() %>" />
			<jsp:param name="yearMonthSchema" value="choose.year" />
			<jsp:param name="method" value="showVacations" />
		</jsp:include>
	</logic:present>
</logic:present>
	
<logic:messagesPresent message="true">
	<html:messages id="message" message="true" property="message">
		<p><span class="error0"><bean:write name="message" /></span></p>
	</html:messages>
</logic:messagesPresent>

<logic:present name="vacations">
	<logic:empty name="vacations">
		<p>
			<em><bean:message key="message.employee.noVacationsResume" /></em>
		</p>
	</logic:empty>
	<logic:notEmpty name="vacations">
		<fr:view name="vacations" schema="show.vacations">
			<fr:layout name="nice-details-table">
				<fr:property name="classes" value="tstyle2 thlight thleft printborder" />
				<fr:property name="columnClasses" value="aleft,aright" />
				<fr:property name="labelTerminator" value=":" />
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
</logic:present>
<logic:notPresent name="vacations">
	<p>
		<em><bean:message key="message.employee.noVacationsResume" /></em>
	</p>
</logic:notPresent>
