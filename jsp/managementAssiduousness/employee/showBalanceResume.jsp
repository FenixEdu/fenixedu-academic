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
		
		
		<p class="mtop15 mbottom05"><strong><bean:message key="label.monthly" /></strong></p>
		<table class="tstyle2 mtop05" style="width: 300px;">
		<logic:present name="employeeBalanceResume" property="monthlyBalance">
			<tr>
			<td><bean:message key="label.monthlyBalance" />:</td>
			<td class="aright"><b><bean:write name="employeeBalanceResume" property="monthlyBalanceString"/></b></td>
			</tr>
		</logic:present>
		<logic:present name="employeeBalanceResume" property="monthlyBalanceToCompensate">
			<tr>
			<td><bean:message key="label.monthlyBalanceToCompensate" />:</td>
			<td class="aright"><b><bean:write name="employeeBalanceResume" property="monthlyBalanceToCompensateString"/></b></td>
			</tr>
		</logic:present>
		<logic:present name="employeeBalanceResume" property="finalMonthlyBalance">
			<tr>
			<td><bean:message key="label.finalMonthlyBalance" />:</td>
			<td class="aright"><b><bean:write name="employeeBalanceResume" property="finalMonthlyBalanceString"/></b></td>
			</tr>
		</logic:present>
		</table>
		
		<p class="mtop15 mbottom05"><strong><bean:message key="label.annual" /></strong></p>
		<table class="tstyle2 mtop05" style="width: 300px;">
    	<logic:present name="employeeBalanceResume" property="anualBalance">
	    	<tr>
			<td><bean:message key="label.anualBalance" />:</td>
			<td class="aright"><b><bean:write name="employeeBalanceResume" property="anualBalanceString"/></b></td>
			</tr>
		</logic:present>
		<logic:present name="employeeBalanceResume" property="anualBalanceToCompensate">
			<tr>
			<td><bean:message key="label.anualBalanceToCompensate" />:</td>
			<td class="aright"><b><bean:write name="employeeBalanceResume" property="anualBalanceToCompensateString"/></b></td>
			</tr>
		</logic:present>
		<logic:present name="employeeBalanceResume" property="finalAnualBalance">
			<tr>
			<td><bean:message key="label.finalAnualBalance" />:</td>
			<td class="aright"><b><bean:write name="employeeBalanceResume" property="finalAnualBalanceString"/></b></td>
			</tr>
		</logic:present>
		</table>
		
		<p class="mtop15 mbottom05"><strong><bean:message key="label.future" /></strong></p>
		<table class="tstyle2 mtop05" style="width: 300px;">
		<logic:present name="employeeBalanceResume" property="finalMonthlyBalance">
			<tr>
			<td><bean:message key="label.futureBalanceToCompensate" />:</td>
			<td class="aright"><b><bean:write name="employeeBalanceResume" property="futureBalanceToCompensateString"/></b></td>
			</tr>
		</logic:present>
		</table>
	</logic:present>
</logic:present>
