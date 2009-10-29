<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em class="invisible"><bean:message key="title.assiduousness" /></em>
<h2><bean:message key="link.showAssiduousnessClosedMonths" /></h2>

<logic:present name="yearMonth">
	<bean:define id="month" name="yearMonth" property="month" />
	<bean:define id="year" name="yearMonth" property="year" />
	<bean:define id="yearMonth" name="yearMonth" />
	<bean:define id="employee" name="employee" />
	<bean:define id="employeeStatusList" name="employeeStatusList" />
	<%request.setAttribute("employee", employee);
	  request.setAttribute("employeeStatusList", employeeStatusList);
	  request.setAttribute("yearMonth", yearMonth);%>
	<jsp:include page="common/consultEmployeeAssiduousnessMenu.jsp">
		<jsp:param name="month" value="<%=month.toString() %>" />
		<jsp:param name="year" value="<%=year.toString() %>" />
		<jsp:param name="yearMonthSchema" value="choose.date" />
		<jsp:param name="method" value="showAllAssiduousnessClosedMonth" />
	</jsp:include>
</logic:present>
		
<logic:messagesPresent message="true">
	<html:messages id="message" message="true" property="message">
		<p><span class="error0"><bean:write name="message" /></span></p>
	</html:messages>
</logic:messagesPresent>

<logic:present name="assiduousnessClosedMonthList">
	<fr:view name="assiduousnessClosedMonthList"
		schema="show.assiduousnessClosedMonth">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 printborder tdleft" />
		</fr:layout>
	</fr:view>
</logic:present>
