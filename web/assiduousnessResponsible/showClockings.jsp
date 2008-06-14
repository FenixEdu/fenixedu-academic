<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em class="invisible"><bean:message key="title.assiduousness" bundle="ASSIDUOUSNESS_RESOURCES"/></em>
<h2><bean:message key="link.clockings" bundle="ASSIDUOUSNESS_RESOURCES"/></h2>

<logic:present name="employee">
	<bean:define id="yearMonth" name="yearMonth"/>
	<bean:define id="month" name="yearMonth" property="month" />
	<bean:define id="year" name="yearMonth" property="year" />
	<bean:define id="employee" name="employee"/>
	<bean:define id="employeeNumber" name="employee" property="employeeNumber" />
	<%request.setAttribute("employee", employee);
	request.setAttribute("yearMonth", yearMonth);%>
	<jsp:include page="common/employeeAssiduousnessMenu.jsp">
		<jsp:param name="month" value="<%=month.toString() %>" />
		<jsp:param name="year" value="<%=year.toString() %>" />
	</jsp:include>
</logic:present>

<logic:messagesPresent message="true">
	<html:messages id="message" message="true">
		<p><span class="error0"><bean:write name="message" bundle="ASSIDUOUSNESS_RESOURCES"/></span></p>
	</html:messages>
</logic:messagesPresent>

<logic:present name="clockings">
	<logic:empty name="clockings">
		<p>
			<em><bean:message key="message.employee.noClocking" bundle="ASSIDUOUSNESS_RESOURCES"/></em>
		</p>
	</logic:empty>
	<logic:notEmpty name="clockings">
		<fr:view name="clockings" schema="show.clockingsDaySheet">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 printborder" />
				<fr:property name="columnClasses" value="bgcolor3 acenter,acenter,aleft" />
				<fr:property name="headerClasses" value="acenter" />
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
</logic:present>
<logic:notPresent name="clockings">
	<p>
		<em><bean:message key="message.employee.noClocking" bundle="ASSIDUOUSNESS_RESOURCES"/></em>
	</p>
</logic:notPresent>
