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
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:xhtml />


<h2><bean:message key="label.curricular.course.from.curriculum" bundle="ACADEMIC_OFFICE_RESOURCES" /> <bean:write name="curricularCourse" property="name" /></h2>

<h3><bean:message key="label.student.enrolments" bundle="ACADEMIC_OFFICE_RESOURCES" /></h3>

<logic:empty name="curricularCourse" property="curriculumModules">
	<bean:message key="message.curricular.course.has.no.enrolments" bundle="ACADEMIC_OFFICE_RESOURCES" />
</logic:empty>

<logic:notEmpty name="curricularCourse" property="curriculumModules">	
	<fr:view name="curricularCourse" property="curriculumModules">
		<fr:schema bundle="ACADEMIC_OFFICE_RESOURCES" type="net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumLine">
			<fr:slot name="student.number" key="label.studentNumber" />
			<fr:slot name="moduleTypeName" bundle="ENUMERATION_RESOURCES" />
			<fr:slot name="name" />
			<fr:slot name="executionYear.name" key="label.executionYear" />
			<fr:slot name="executionPeriod.name" key="label.executionPeriod"/>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1" />
			<fr:property name="sortBy" value="executionYear.name" />
		</fr:layout>
	</fr:view>
</logic:notEmpty>
