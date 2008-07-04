<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em class="invisible"><bean:message key="title.assiduousness" /></em>
<h2><bean:message key="title.showStatus" /></h2>

<logic:present name="yearMonth">
	<bean:define id="month" name="yearMonth" property="month" />
	<bean:define id="year" name="yearMonth" property="year" />
	<logic:present name="employee">
		<bean:define id="employeeNumber" name="employee" property="employeeNumber" />
		<bean:define id="employee" name="employee"/>
		<%request.setAttribute("employee", employee);%>
		<jsp:include page="common/consultEmployeeAssiduousnessMenu.jsp">
			<jsp:param name="month" value="<%=month.toString() %>" />
			<jsp:param name="year" value="<%=year.toString() %>" />
			<jsp:param name="yearMonthSchema" value="false" />
		</jsp:include>
	
		<logic:messagesPresent message="true">
			<html:messages id="message" message="true" property="message">
				<p><span class="error0"><bean:write name="message" /></span></p>
			</html:messages>
		</logic:messagesPresent>
	
		<%net.sourceforge.fenixedu.applicationTier.IUserView user = (net.sourceforge.fenixedu.applicationTier.IUserView) session
		                    .getAttribute(pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE);
		            if (net.sourceforge.fenixedu.domain.ManagementGroups.isAssiduousnessManagerMember(user.getPerson())) {
		
		            %>
		<ul class="list5 mtop15">
			<li>
				<html:link page="<%="/employeeAssiduousness.do?method=prepareEditEmployeeStatus&month="+month.toString()+"&year="+year.toString()+"&employeeNumber="+employeeNumber.toString()%>"><bean:message key="link.createStatus" /></html:link>
			</li>
		</ul>
		<%}%>
		<fr:view name="employee" property="assiduousness.assiduousnessStatusHistoriesOrdered" schema="show.employeeStatus">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 thbold tdcenter" />
				<%if (net.sourceforge.fenixedu.domain.ManagementGroups.isAssiduousnessManagerMember(user.getPerson())) {  %>
					<fr:property name="link(edit)" value="<%="/employeeAssiduousness.do?method=prepareEditEmployeeStatus&month="+month.toString()+"&year="+year.toString()%>" />
					<fr:property name="key(edit)" value="label.edit" />
					<fr:property name="param(edit)" value="idInternal" />
					<fr:property name="visibleIf(edit)" value="editable" />
					<fr:property name="link(delete)" value="<%="/employeeAssiduousness.do?method=deleteEmployeeStatus&month="+month.toString()+"&year="+year.toString()%>" />
					<fr:property name="key(delete)" value="label.delete" />
					<fr:property name="param(delete)" value="idInternal" />
					<fr:property name="visibleIf(delete)" value="deletable" />
				<%}%>
			</fr:layout>
		</fr:view>
	</logic:present>
</logic:present>