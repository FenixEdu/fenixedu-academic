<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers"
	prefix="fr"%>

<html:xhtml />

<h2>Create Department</h2>
	<fr:edit id="department" name="bean" action="/manageAssociatedObjects.do?method=createDepartment">
		<fr:schema bundle="MANAGER_RESOURCES" type="org.fenixedu.academic.ui.struts.action.manager.ManageAssociatedObjects$AssociatedObjectsBean">
			<fr:slot name="active" key="active">
			</fr:slot>
			<fr:slot name="code" key="label.manager.code">
			</fr:slot>
			<fr:slot name="acronym" key="label.acronym">
			</fr:slot>
			<fr:slot name="name" key="label.name">
			</fr:slot>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes"
				value="tstyle5 thleft thlight thmiddle mtop05" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
		</fr:layout>
	</fr:edit>
