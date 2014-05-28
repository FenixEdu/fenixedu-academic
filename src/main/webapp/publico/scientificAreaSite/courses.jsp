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
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<html:xhtml/>

<h1 class="mbottom03 cnone"><fr:view name="actual$site" property="unitNameWithAcronym"/></h1>
<h2 class="mtop15"><bean:message key="property.courses"/></h2>



<bean:define id="scientificAreaUnit" name="unit" type="net.sourceforge.fenixedu.domain.organizationalStructure.ScientificAreaUnit"/>


<bean:define id="departmentId" value="<%= String.valueOf(scientificAreaUnit.getDepartmentUnit().getExternalId()) %>" type="java.lang.String"/>

<logic:iterate id="courseGroupUnit" name="courseGroupUnits">

	<p>
	<h2 class="mtop1 mbottom0 greytxt"><strong><fr:view name="courseGroupUnit" property="name"/></strong></h2>
	<fr:view name="courseGroupUnit" property="competenceCourses">
	
		<fr:layout>
			<fr:property name="eachLayout" value="values"/>
			<fr:property name="eachSchema" value="view.competence.courses"/>
		</fr:layout>
		<fr:destination name="view.competence.course" path="<%= "/department/showCompetenceCourse.faces?action=ccm&competenceCourseID=${externalId}&selectedDepartmentUnitID=" + departmentId %>"/>
	</fr:view>
	</p>
</logic:iterate>