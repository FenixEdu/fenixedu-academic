<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em class="invisible"><bean:message key="title.assiduousness" /></em>

<logic:present name="yearMonth">
	<logic:present name="assiduousnessStatusHistory">
		<h2><bean:message key="title.showStatus" /></h2>
	</logic:present>
	<logic:notPresent name="assiduousnessStatusHistory">
		<h2><bean:message key="link.createStatus" /></h2>
	</logic:notPresent>

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
	

	
		<p>
		<span class="error0">
			<html:messages id="message" message="true">
				<bean:write name="message" />
			</html:messages>
		</span>
		</p>
		
		<logic:present name="assiduousnessStatusHistory">
			<fr:edit name="assiduousnessStatusHistory" schema="edit.employeeStatus"
				action="<%="/viewEmployeeAssiduousness.do?method=showStatus&month="+month.toString()+"&year="+year.toString()+"&employeeNumber="+employeeNumber.toString()%>">
				<fr:hidden slot="modifiedBy" name="UserView" property="person.employee" />
			</fr:edit>
		</logic:present>
		
		<logic:notPresent name="assiduousnessStatusHistory">
			<logic:present name="beginDate">
				<fr:create id="assiduousnessStatusHistory" schema="create.employeeStatus" 
				type="net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessStatusHistory"
				action="<%="/viewEmployeeAssiduousness.do?method=showStatus&month="+month.toString()+"&year="+year.toString()+"&employeeNumber="+employeeNumber.toString()%>">
					<fr:hidden slot="assiduousness" name="employee" property="assiduousness"/>
					<fr:hidden slot="modifiedBy" name="UserView" property="person.employee" />
					<fr:default slot="beginDate" name="beginDate"/>
				</fr:create>
			</logic:present>
			<logic:notPresent name="beginDate">
				<fr:create id="assiduousnessStatusHistory" schema="create.employeeStatus.beginDate" 
				type="net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessStatusHistory"
				action="<%="/viewEmployeeAssiduousness.do?method=showStatus&month="+month.toString()+"&year="+year.toString()+"&employeeNumber="+employeeNumber.toString()%>">
					<fr:hidden slot="assiduousness" name="employee" property="assiduousness"/>
					<fr:hidden slot="modifiedBy" name="UserView" property="person.employee" />
				</fr:create>
			</logic:notPresent>
		</logic:notPresent>
	</logic:present>
</logic:present>