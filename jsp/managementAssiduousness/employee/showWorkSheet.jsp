<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em class="invisible"><bean:message key="title.assiduousness" /></em>
<h2><bean:message key="link.workSheet" /></h2>


<logic:present name="employeeWorkSheet">
	<logic:present name="yearMonth">
		<bean:define id="month" name="yearMonth" property="month" />
		<bean:define id="year" name="yearMonth" property="year" />
		<bean:define id="employee" name="employeeWorkSheet" property="employee"/>
		<bean:define id="employeeNumber" name="employeeWorkSheet" property="employee.employeeNumber" />
		<bean:define id="yearMonth" name="yearMonth"/>
			
		<bean:define id="employeeStatusList" name="employeeStatusList"/>
		<%request.setAttribute("employee", employee);
		request.setAttribute("employeeStatusList", employeeStatusList);
		request.setAttribute("yearMonth", yearMonth);%>
		<jsp:include page="common/consultEmployeeAssiduousnessMenu.jsp">
			<jsp:param name="month" value="<%=month.toString() %>" />
			<jsp:param name="year" value="<%=year.toString() %>" />
			<jsp:param name="yearMonthSchema" value="choose.date" />
			<jsp:param name="method" value="showWorkSheet" />
		</jsp:include>

		<%net.sourceforge.fenixedu.applicationTier.IUserView user = (net.sourceforge.fenixedu.applicationTier.IUserView) session
                    .getAttribute(net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionConstants.U_VIEW);
            if (net.sourceforge.fenixedu.domain.ManagementGroups.isAssiduousnessManagerMember(user.getPerson())) {
                %>
		<logic:equal name="yearMonth" property="isThisYearMonthClosed" value="false">
	       <logic:present name="employeeJustificationFactory">
				<span class="error0 mtop0"><html:messages id="errorMessage" message="true" property="errorMessage">
					<bean:write name="errorMessage" />
					<br />
				</html:messages></span>
				<%request.setAttribute("employee", employee);%>
				<jsp:include page="common/insertEmployeeJustification.jsp">
					<jsp:param name="month" value="<%=month.toString() %>" />
					<jsp:param name="year" value="<%=year.toString() %>" />
				</jsp:include>
			</logic:present>
		</logic:equal>
	 	<%}%>	
	 	
		<logic:messagesPresent message="true">
			<html:messages id="message" message="true" property="message">
				<p><span class="error0"><bean:write name="message" /></span></p>
			</html:messages>
		</logic:messagesPresent>
		
	<logic:empty name="employeeWorkSheet" property="workDaySheetList">
		<p>
			<em><bean:message key="message.employee.noWorkSheet" /></em>
		</p>
	</logic:empty>
	<logic:notEmpty name="employeeWorkSheet" property="workDaySheetList">
		<fr:view name="employeeWorkSheet" property="workDaySheetList"
			schema="show.workDaySheet.management">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 printborder tpadding1" />
				<fr:property name="columnClasses"
					value="bgcolor3 acenter,,acenter,aright,aright,aleft,aleft,acenter invisible" />
				<fr:property name="headerClasses" value="acenter" />
				<%if (net.sourceforge.fenixedu.domain.ManagementGroups.isAssiduousnessManagerMember(user.getPerson())) { %>
                <logic:equal name="yearMonth" property="isThisYearMonthClosed" value="false">
	                <fr:property name="link(justification)" value="<%="/employeeAssiduousness.do?method=prepareCreateEmployeeJustification&correction=JUSTIFICATION&employeeNumber="+employeeNumber.toString()%>" />
					<fr:property name="key(justification)" value="label.justification" />
					<fr:property name="param(justification)" value="date" />
					<fr:property name="bundle(justification)" value="ASSIDUOUSNESS_RESOURCES" />
	                <fr:property name="link(regularization)" value="<%="/employeeAssiduousness.do?method=prepareCreateEmployeeJustification&correction=REGULARIZATION&employeeNumber="+employeeNumber.toString()%>" />
					<fr:property name="key(regularization)" value="label.regularization" />
					<fr:property name="param(regularization)" value="date" />
					<fr:property name="bundle(regularization)" value="ASSIDUOUSNESS_RESOURCES" />
				</logic:equal>
                <%}%>
			</fr:layout>
		</fr:view>

		<logic:notEmpty name="displayCurrentDayNote">
			<bean:message key="message.employee.currentDayIgnored" />
		</logic:notEmpty>

		<logic:present name="employeeWorkSheet" property="totalBalance">
			<p class="mvert05"><bean:message key="label.totalBalance" />: <b><bean:write
				name="employeeWorkSheet" property="totalBalanceString" /></b></p>
		</logic:present>
		<logic:present name="employeeWorkSheet" property="unjustifiedBalance">
			<p class="mvert05"><bean:message key="label.totalUnjustified" />: <b><bean:write
				name="employeeWorkSheet" property="unjustifiedBalanceString" /></b></p>
		</logic:present>
		<logic:present name="employeeWorkSheet" property="complementaryWeeklyRest">
			<p class="mvert05"><bean:message key="label.totalSaturday" />: <b><bean:write name="employeeWorkSheet" property="complementaryWeeklyRestString"/></b></p>
		</logic:present>
		<logic:present name="employeeWorkSheet" property="weeklyRest">
			<p class="mvert05"><bean:message key="label.totalSunday" />: <b><bean:write name="employeeWorkSheet" property="weeklyRestString"/></b></p>
		</logic:present>
		
	</logic:notEmpty>
	</logic:present>
</logic:present>

<logic:notPresent name="employeeWorkSheet">
	<p>
		<em><bean:message key="message.employee.noWorkSheet" /></em>
	</p>
</logic:notPresent>
