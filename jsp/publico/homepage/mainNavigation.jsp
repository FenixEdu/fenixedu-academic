<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<ul class="treemenu">
	<logic:present name="homepage">
		<logic:present name="homepage" property="person.employee.currentDepartmentWorkingPlace">
			<bean:define id="departmentUrl" type="java.lang.String">
				<bean:write name="homepage" property="person.employee.currentDepartmentWorkingPlace.departmentUnit.webAddress"/>
			</bean:define>
			<bean:define id="departmentUnitID" type="java.lang.String">
				<bean:write name="homepage" property="person.employee.currentDepartmentWorkingPlace.departmentUnit.idInternal"/>
			</bean:define>
			<li>
				<html:link href="<%=departmentUrl%>">
					<bean:write name="homepage" property="person.employee.currentDepartmentWorkingPlace.acronym"/>
				</html:link>
		    </li>
			<li>
				<html:link href="<%= "department/showDepartmentTeachers.faces?selectedDepartmentUnitID=" + departmentUnitID %>">
					<bean:message bundle="PUBLIC_DEPARTMENT_RESOURCES" key="department.faculty"/>
				</html:link>
		    </li>
			<li>
				<html:link href="<%= "department/showDepartmentCompetenceCourses.faces?selectedDepartmentUnitID=" + departmentUnitID %>">
					<bean:message bundle="PUBLIC_DEPARTMENT_RESOURCES" key="department.courses"/>
				</html:link>
		    </li>
	    </logic:present>
		<logic:notPresent name="homepage" property="person.employee.currentDepartmentWorkingPlace">
			<li>
				<bean:define id="homepageID" name="homepage" property="idInternal"/>
				<html:link page="<%= "/viewHomepage.do?method=show&amp;homepageID=" + homepageID.toString() %>">
					<bean:message bundle="HOMEPAGE_RESOURCES" key="link.homepage.home"/>
				</html:link>
		    </li>
		</logic:notPresent>
    </logic:present>
	<logic:notPresent name="homepage">
		<li>
			<html:link page="/viewHomepage.do?method=listTeachers">
				<bean:message bundle="HOMEPAGE_RESOURCES" key="link.homepage.listings.teachers"/>
			</html:link>
	    </li>
		<li>
			<html:link page="/viewHomepage.do?method=listEmployees">
				<bean:message bundle="HOMEPAGE_RESOURCES" key="link.homepage.listings.employees"/>
			</html:link>
	    </li>
		<li>
			<html:link page="/viewHomepage.do?method=listStudents">
				<bean:message bundle="HOMEPAGE_RESOURCES" key="link.homepage.listings.students"/>
			</html:link>
	    </li>
		<li>
			<html:link page="/viewHomepage.do?method=listAlumni">
				<bean:message bundle="HOMEPAGE_RESOURCES" key="link.homepage.listings.alumni"/>
			</html:link>
	    </li>
    </logic:notPresent>
</ul>

