<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em class="invisible"><bean:message key="title.assiduousness" /></em>
<h2><bean:message key="link.balanceResume" /></h2>

<logic:present name="employeeBalanceResume">
	<logic:present name="yearMonth">
		<bean:define id="month" name="yearMonth" property="month" />
		<bean:define id="year" name="yearMonth" property="year" />
		<bean:define id="employee" name="employeeBalanceResume" property="employee"/>
		<bean:define id="employeeNumber" name="employeeBalanceResume" property="employee.employeeNumber" />
		<bean:define id="yearMonth" name="yearMonth"/>

		<bean:define id="employeeStatusList" name="employeeStatusList"/>
		<%request.setAttribute("employee", employee);
		request.setAttribute("employeeStatusList", employeeStatusList);
		request.setAttribute("yearMonth", yearMonth);%>
		<jsp:include page="common/consultEmployeeAssiduousnessMenu.jsp">
			<jsp:param name="month" value="<%=month.toString() %>" />
			<jsp:param name="year" value="<%=year.toString() %>" />
			<jsp:param name="yearMonthSchema" value="choose.date" />
			<jsp:param name="method" value="showBalanceResume" />
		</jsp:include>

		<logic:messagesPresent message="true">
			<html:messages id="message" message="true" property="message">
				<p><span class="error0"><bean:write name="message" /></span></p>
			</html:messages>
		</logic:messagesPresent>
		
		<logic:present name="employeeBalanceResume" property="monthlyBalance">
			<p class="mvert05"><bean:message key="label.monthlyBalance" />: <b><bean:write name="employeeBalanceResume" property="monthlyBalanceString"/></b></p>
		</logic:present>
		<logic:present name="employeeBalanceResume" property="monthlyBalanceToCompensate">
			<p class="mvert05"><bean:message key="label.monthlyBalanceToCompensate" />: <b><bean:write name="employeeBalanceResume" property="monthlyBalanceToCompensateString"/></b></p>
		</logic:present>
		<logic:present name="employeeBalanceResume" property="finalMonthlyBalance">
			<p class="mvert05"><bean:message key="label.finalMonthlyBalance" />: <b><bean:write name="employeeBalanceResume" property="finalMonthlyBalanceString"/></b></p>
		</logic:present>
    	<logic:present name="employeeBalanceResume" property="anualBalance">
			<p class="mvert05"><bean:message key="label.anualBalance" />: <b><bean:write name="employeeBalanceResume" property="anualBalanceString"/></b></p>
		</logic:present>
		<logic:present name="employeeBalanceResume" property="anualBalanceToCompensate">
			<p class="mvert05"><bean:message key="label.anualBalanceToCompensate" />: <b><bean:write name="employeeBalanceResume" property="anualBalanceToCompensateString"/></b></p>
		</logic:present>
		<logic:present name="employeeBalanceResume" property="finalAnualBalance">
			<p class="mvert05"><bean:message key="label.finalAnualBalance" />: <b><bean:write name="employeeBalanceResume" property="finalAnualBalanceString"/></b></p>
		</logic:present>
		<logic:present name="employeeBalanceResume" property="finalMonthlyBalance">
			<p class="mvert05"><bean:message key="label.futureBalanceToCompensate" />: <b><bean:write name="employeeBalanceResume" property="futureBalanceToCompensateString"/></b></p>
		</logic:present>
	</logic:present>
</logic:present>
