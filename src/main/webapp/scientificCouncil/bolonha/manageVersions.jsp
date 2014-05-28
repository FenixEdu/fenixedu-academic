<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page isELIgnored="true"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<h2><bean:message key="label.version.manage" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></h2>

<fr:view name="departments" schema="view.departments.with.requests">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1 thlight"/>
		<fr:property name="columnClasses" value=",acenter,acenter"/>
		<fr:property name="sortBy" value="name"/>
	</fr:layout>
	<fr:destination name="viewDepartmentRequests" path="/competenceCourses/manageVersions.do?method=displayRequest&departmentID=${externalId}"/>
</fr:view>

