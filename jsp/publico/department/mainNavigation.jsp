<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<ul class="treemenu">
	<li>
		<html:link page="<%= "/department/showDepartmentTeachers.faces?selectedDepartmentUnitID=" + request.getAttribute("selectedDepartmentUnitID")%>">
			<bean:message bundle="PUBLIC_DEPARTMENT_RESOURCES" key="department.web.site"/>
		</html:link>
    </li>
	<li>
		<html:link page="<%= "/department/showDepartmentTeachers.faces?selectedDepartmentUnitID=" + request.getAttribute("selectedDepartmentUnitID")%>">
			<bean:message bundle="PUBLIC_DEPARTMENT_RESOURCES" key="department.teachers"/>
		</html:link>
    </li>
	<li>
		<html:link page="<%= "/department/showDepartmentCompetenceCourses.faces?selectedDepartmentUnitID=" + request.getAttribute("selectedDepartmentUnitID")%>">
			<bean:message bundle="PUBLIC_DEPARTMENT_RESOURCES" key="department.competence.courses"/>
		</html:link>
    </li>
</ul>
