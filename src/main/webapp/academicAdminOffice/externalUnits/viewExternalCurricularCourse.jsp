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
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<html:xhtml/>

<br />
<h2><bean:message key="label.externalUnits.externalCurricularCourse" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <bean:write name="externalCurricularCourseBean" property="externalCurricularCourse.name"/> </h2>

<fr:view name="externalCurricularCourseBean" schema="AbstractExternalUnitResultBean.view-breadCrumbs-path">
	<fr:layout name="flow">
		<fr:property name="labelExcluded" value="true" />
	</fr:layout>
</fr:view>

<bean:define id="externalCurricularCourseId">&amp;oid=<bean:write name="externalCurricularCourseBean" property="externalCurricularCourse.externalId" /></bean:define>

<ul class="mtop15">
	<li><html:link page="<%="/externalUnits.do?method=prepareEditExternalCurricularCourse" + externalCurricularCourseId %>"><bean:message key="label.externalUnits.editInformation" bundle="ACADEMIC_OFFICE_RESOURCES" /></html:link></li>
	<li><html:link page="<%="/externalUnits.do?method=prepareDeleteExternalCurricularCourse" + externalCurricularCourseId %>"><bean:message key="label.externalUnits.deleteInformation" bundle="ACADEMIC_OFFICE_RESOURCES" /></html:link></li>
</ul>

<table class="tstyle2">
	<tr><td class="aright"><bean:message key="label.externalUnits.externalEnrolments" bundle="ACADEMIC_OFFICE_RESOURCES"/>:</td><td><bean:size id="size" name="externalEnrolments" /> <%= size %></td></tr>
</table>


<h3 class="mtop15 mbottom05"><bean:message key="label.externalUnits.externalEnrolments" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
<logic:notEmpty name="externalEnrolments">
	<fr:view name="externalEnrolments" schema="ExternalEnrolment.view">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight mvert05"/>
			<fr:property name="columnClasses" value="acenter,acenter,aleft,acenter,acenter,acenter,acenter"/>
			<fr:property name="linkFormat(edit)" value="/externalUnits.do?method=prepareEditExternalEnrolment&oid=${externalId}" />
			<fr:property name="key(edit)" value="label.edit"/>
			<fr:property name="bundle(edit)" value="ACADEMIC_OFFICE_RESOURCES"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<logic:empty name="externalEnrolments">
	<p>
		<em><bean:message key="label.externalUnits.noExternalEnrolments" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
	</p>
</logic:empty>
