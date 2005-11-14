<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>	

<!-- Import new CSS for this section: #navlateral  -->
<style>@import url(<%= request.getContextPath() %>/CSS/navlateralnew.css);</style>

<logic:present role="DEPARTMENT_MEMBER">
	
	<bean:define id="userView" name="<%= SessionConstants.U_VIEW %>" scope="session"/>
		
	<!-- Temporary solution (until we make expectations available for all departments) DEI Code = 28 -->
	<% String deiCode = "28"; %>
	
	<ul>
		<li>
			<html:link page="/viewDepartmentTeachers/listDepartmentTeachers.faces">
				<bean:message key="link.departmentTeachers"/>
			</html:link>
		</li>
		<logic:equal name="userView" property="person.employee.departmentWorkingPlace.code" value="<%= deiCode %>">
		<li>
			<html:link page="/expectationManagement/viewPersonalExpectation.faces">
				<bean:message key="link.personalExpectationsManagement"/>
			</html:link>
		</li>
		</logic:equal>
	</ul>
	
	<br />
</logic:present>

