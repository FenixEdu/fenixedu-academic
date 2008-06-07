<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em class="invisible"><bean:message key="title.assiduousness" /></em>
<h2><bean:message key="link.justifications" /></h2>
<%net.sourceforge.fenixedu.applicationTier.IUserView user = (net.sourceforge.fenixedu.applicationTier.IUserView) session.getAttribute(pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE);%>
<logic:present name="yearMonth">
	<bean:define id="yearMonth" name="yearMonth"/>
	<bean:define id="month" name="yearMonth" property="month" />
	<bean:define id="year" name="yearMonth" property="year"/>

	<logic:present name="employee">
		<bean:define id="employee" name="employee"/>
		<bean:define id="employeeNumber" name="employee" property="employeeNumber" />
		
		<bean:define id="employeeStatusList" name="employeeStatusList"/>
		<%request.setAttribute("employee", employee);
		request.setAttribute("employeeStatusList", employeeStatusList);
		request.setAttribute("yearMonth", yearMonth);
		request.setAttribute("showJustificationsLinks", "showJustificationsLinks");%>
		<jsp:include page="common/consultEmployeeAssiduousnessMenu.jsp">
			<jsp:param name="month" value="<%=month.toString() %>" />
			<jsp:param name="year" value="<%=year.toString() %>" />
			<jsp:param name="yearMonthSchema" value="choose.date" />
			<jsp:param name="method" value="showJustifications" />
		</jsp:include>
		<% if (net.sourceforge.fenixedu.domain.ManagementGroups.isAssiduousnessManagerMember(user.getPerson())) {%>
			<logic:equal name="yearMonth" property="isThisYearMonthClosed" value="false">
				<logic:present name="employeeJustificationFactory">
					<p class="mtop2">
						<span class="error0 mtop0">
							<html:messages id="errorMessage" message="true" property="errorMessage">
								<bean:write name="errorMessage" />
							</html:messages>
						</span>
					</p>
					<%request.setAttribute("employee", employee);%>
					<jsp:include page="common/insertEmployeeJustification.jsp">
						<jsp:param name="month" value="<%=month.toString() %>" />
						<jsp:param name="year" value="<%=year.toString() %>" />
					</jsp:include>
				</logic:present>
			</logic:equal>
		<%}%>
	</logic:present>

	<logic:messagesPresent message="true">
		<html:messages id="message" message="true" property="message">
			<p><span class="error0"><bean:write name="message" /></span></p>
		</html:messages>
	</logic:messagesPresent>
	<logic:present name="justifications">
		<logic:empty name="justifications">
			<p>
				<em><bean:message key="message.employee.noJustifications" /></em>
			</p>
		</logic:empty>
		<logic:notEmpty name="justifications">
			<fr:view name="justifications" schema="show.justifications.management">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle1 printborder" />
					<fr:property name="columnClasses" value="acenter" />
					<fr:property name="headerClasses" value="acenter" />
					<%if (net.sourceforge.fenixedu.domain.ManagementGroups.isAssiduousnessManagerMember(user.getPerson())) {%>
					<logic:equal name="yearMonth" property="isThisYearMonthClosed" value="false">
		                <fr:property name="link(edit)" value="<%="/employeeAssiduousness.do?method=prepareEditEmployeeJustification&month="+month.toString()+"&year="+year.toString()%>" />
						<fr:property name="key(edit)" value="label.edit" />
						<fr:property name="param(edit)" value="idInternal" />
						<fr:property name="bundle(edit)" value="ASSIDUOUSNESS_RESOURCES" />
						<fr:property name="link(delete)" value="<%="/employeeAssiduousness.do?method=deleteEmployeeJustification&month="+month.toString()+"&year="+year.toString()%>" />
						<fr:property name="key(delete)" value="label.delete" />
						<fr:property name="param(delete)" value="idInternal" />
						<fr:property name="bundle(delete)" value="ASSIDUOUSNESS_RESOURCES" />
					</logic:equal>
	                <%}%>
				</fr:layout>
			</fr:view>
		</logic:notEmpty>
	</logic:present>
</logic:present>
<logic:notPresent name="justifications">
	<p>
		<em><bean:message key="message.employee.noJustifications" /></em>
	</p>
</logic:notPresent>