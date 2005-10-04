<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<logic:present role="DEPARTMENT_MEMBER">
	<strong>&raquo; <bean:message key="link.group.view.title"/></strong>
	<br />		
	<ul>
		<li>
			<html:link page="/viewDepartmentTeachers/chooseExecutionYear.faces">
				<bean:message key="link.departmentTeachers"/>
			</html:link>
		</li>
	</ul>
	<br />
	<strong>&raquo; <bean:message key="link.group.personal.title"/></strong>
	<br />
	<ul>
		<li>
			<html:link page="/expectationManagement/chooseExecutionYear.faces">
				<bean:message key="link.personalExpectationsManagement"/>
			</html:link>
		</li>
	</ul>
	<br />
	<br />
</logic:present>

