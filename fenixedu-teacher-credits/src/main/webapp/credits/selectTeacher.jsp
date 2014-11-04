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
<%@page contentType="text/html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app" %>
<html:xhtml/>

<html:messages id="message" message="true">
	<span class="error">
		<bean:write name="message" filter="false"/>
	</span>
</html:messages>

<logic:present name="teacherBean">
	<fr:edit id="teacherBean" name="teacherBean" action="/credits.do?method=showTeacherCredits">
		<fr:schema bundle="TEACHER_CREDITS_SHEET_RESOURCES" type="org.fenixedu.academic.domain.credits.util.TeacherCreditsBean">
			<fr:slot name="teacher" layout="autoComplete">
				<fr:property name="size" value="80"/>
				<fr:property name="format" value="${person.name} (${person.username})"/>
				<fr:property name="indicatorShown" value="true"/>
				<fr:property name="provider" value="org.fenixedu.academic.service.services.commons.searchers.SearchTeachers"/>
				<fr:property name="args" value="slot=person.name"/>
				<fr:property name="minChars" value="3"/>
				<fr:property name="errorStyleClass" value="error0"/>
				<fr:validator name="pt.ist.fenixWebFramework.rendererExtensions.validators.RequiredAutoCompleteSelectionValidator" />
			</fr:slot>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight mtop15" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
		</fr:layout>
	</fr:edit>
</logic:present>