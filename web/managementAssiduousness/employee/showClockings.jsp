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
		
		<bean:define id="employee" name="employee"/>
		<bean:define id="employeeStatusList" name="employeeStatusList"/>
		<bean:define id="yearMonth" name="yearMonth"/>
		<%request.setAttribute("employee", employee);
		request.setAttribute("employeeStatusList", employeeStatusList);
		request.setAttribute("yearMonth", yearMonth);%>
		<jsp:include page="common/consultEmployeeAssiduousnessMenu.jsp">
			<jsp:param name="month" value="<%=month.toString() %>" />
			<jsp:param name="year" value="<%=year.toString() %>" />
			<jsp:param name="yearMonthSchema" value="choose.date" />
			<jsp:param name="method" value="showClockings" />
		</jsp:include>
	</logic:present>
</logic:present>

<logic:messagesPresent message="true">
	<html:messages id="message" message="true">
		<p><span class="error0"><bean:write name="message" /></span></p>
	</html:messages>
</logic:messagesPresent>


<logic:present name="clockings">
	<logic:empty name="clockings">
		<p>
			<em><bean:message key="message.employee.noClocking" /></em>
		</p>
	</logic:empty>
	<logic:notEmpty name="clockings">
	<%net.sourceforge.fenixedu.applicationTier.IUserView user = (net.sourceforge.fenixedu.applicationTier.IUserView) session.getAttribute(pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE);
		if (net.sourceforge.fenixedu.domain.ManagementGroups.isAssiduousnessManagerMember(user.getPerson())) {%>
		<fr:view name="clockings" schema="show.clockingsDaySheet.assiduosunessManager">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 printborder" />
				<fr:property name="columnClasses" value="bgcolor3 acenter,acenter,aleft,aleft" />
				<fr:property name="headerClasses" value="acenter" />
			</fr:layout>
		</fr:view>
	<%}else{ %>
		<fr:view name="clockings" schema="show.clockingsDaySheet">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 printborder" />
				<fr:property name="columnClasses" value="bgcolor3 acenter,acenter,aleft" />
				<fr:property name="headerClasses" value="acenter" />
			</fr:layout>
		</fr:view>	
	<%}%>
	</logic:notEmpty>
</logic:present>
<logic:notPresent name="clockings">
	<p>
		<em><bean:message key="message.employee.noClocking" /></em>
	</p>
</logic:notPresent>
