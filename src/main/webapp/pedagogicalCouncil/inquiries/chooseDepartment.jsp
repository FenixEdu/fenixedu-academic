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
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml />

<h2><bean:message key="title.inquiries.resultsWithDescription" bundle="INQUIRIES_RESOURCES"/></h2>

<p class="mtop2"><bean:message key="label.choose.department" bundle="PEDAGOGICAL_COUNCIL"/></p>

<fr:view name="departments">
	<fr:schema bundle="PEDAGOGICAL_COUNCIL" type="net.sourceforge.fenixedu.domain.organizationalStructure.DepartmentUnit">
		<fr:slot name="name" key="label.teacher.department"/>
	</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1 thlight mtop05"/>
		<fr:property name="linkFormat(view)" value="/viewQucResults.do?method=resumeResults&departmentUnitOID=${externalId}"/>
		<fr:property name="key(view)" value="link.inquiry.viewResults" />
		<fr:property name="bundle(view)" value="INQUIRIES_RESOURCES" />
	</fr:layout>
</fr:view>