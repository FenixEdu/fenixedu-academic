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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<html:xhtml/>

<br />
<h2><bean:message key="label.externalUnits.country" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <bean:write name="unitResultBean" property="unit.name"/> </h2>

<bean:define id="unitId">&amp;oid=<bean:write name="unitResultBean" property="unit.externalId"/></bean:define>

<ul class="mtop15">
	<li><html:link page="<%="/externalUnits.do?method=prepareCreateUniversity" + unitId %>"><bean:message key="label.externalUnits.createUniversity" bundle="ACADEMIC_OFFICE_RESOURCES" /></html:link></li>
	<li><html:link page="<%="/externalUnits.do?method=prepareCreateSchool" + unitId %>"><bean:message key="label.externalUnits.createSchool" bundle="ACADEMIC_OFFICE_RESOURCES" /></html:link></li>
	<li><html:link page="<%="/externalUnits.do?method=prepareCreateDepartment" + unitId%>"><bean:message key="label.externalUnits.createDepartment" bundle="ACADEMIC_OFFICE_RESOURCES" /></html:link></li>
	<li><html:link page="<%="/externalUnits.do?method=prepareEditUnit" + unitId %>"><bean:message key="label.externalUnits.editInformation" bundle="ACADEMIC_OFFICE_RESOURCES" /></html:link></li>
	<li><html:link page="<%="/externalUnits.do?method=prepareDeleteUnit" + unitId %>"><bean:message key="label.externalUnits.deleteInformation" bundle="ACADEMIC_OFFICE_RESOURCES" /></html:link></li>
</ul>

<table class="tstyle2">
	<tr><td class="aright"><bean:message key="label.externalUnits.universities" bundle="ACADEMIC_OFFICE_RESOURCES"/>:</td><td><bean:size id="size" name="universities" /> <%= size %></td></tr>
	<tr><td class="aright"><bean:message key="label.externalUnits.schools" bundle="ACADEMIC_OFFICE_RESOURCES"/>:</td><td><bean:size id="size" name="schools" /> <%= size %></td></tr>
	<tr><td class="aright"><bean:message key="label.externalUnits.departments" bundle="ACADEMIC_OFFICE_RESOURCES"/>:</td><td><bean:size id="size" name="departments" /> <%= size %></td></tr>
	<tr><td class="aright"><bean:message key="label.externalUnits.externalCurricularCourses" bundle="ACADEMIC_OFFICE_RESOURCES"/>:</td><td><bean:size id="size" name="externalCurricularCourses" /> <%= size %></td></tr>
</table>



<h3 class="mtop15 mbottom05"><bean:message key="label.externalUnits.universities" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
<logic:notEmpty name="universities">
	<fr:view name="universities" schema="UniversityUnitResultBean.view">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight mvert05"/>
			<fr:property name="columnClasses" value=",acenter,acenter,acenter"/>			
		</fr:layout>
	</fr:view>
</logic:notEmpty>
<logic:empty name="universities">
	<p>
		<em><bean:message key="label.externalUnits.noUniversities" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
	</p>
</logic:empty>


<h3 class="mtop15 mbottom05"><bean:message key="label.externalUnits.schools" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
<logic:notEmpty name="schools">
	<fr:view name="schools" schema="SchoolUnitResultBean.view">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight mvert05"/>
			<fr:property name="columnClasses" value=",acenter,acenter,acenter"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>
<logic:empty name="schools">
	<p>
		<em><bean:message key="label.externalUnits.noSchools" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
	</p>
</logic:empty>


<h3 class="mtop15 mbottom05"><bean:message key="label.externalUnits.departments" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
<logic:notEmpty name="departments">
	<fr:view name="departments" schema="DepartmentUnitResultBean.view">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight mvert05"/>
			<fr:property name="columnClasses" value=",acenter,acenter,acenter"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>
<logic:empty name="departments">
	<p>
		<em><bean:message key="label.externalUnits.noDepartments" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
	</p>
</logic:empty>


<h3 class="mtop15 mbottom05"><bean:message key="label.externalUnits.externalCurricularCourses" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
<logic:notEmpty name="externalCurricularCourses">
	<fr:view name="externalCurricularCourses" schema="ExternalCurricularCourseResultBean.view">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight mvert05"/>
			<fr:property name="columnClasses" value=",acenter,acenter,acenter"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<logic:empty name="externalCurricularCourses">
	<p>
		<em><bean:message key="label.externalUnits.noExternalCurricularCourses" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
	</p>
</logic:empty>
