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

<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers"
           prefix="fr" %>

<html:xhtml/>

<h2>Degree Type</h2>
<fr:edit id="bean" name="bean" action="/manageAssociatedObjects.do?method=createDegreeType">
    <fr:schema bundle="ACADEMIC_ADMIN_OFFICE"
               type="org.fenixedu.academic.ui.struts.action.manager.ManageAssociatedObjects$DegreeTypeBean">
        <fr:slot name="name"/>
        <fr:slot name="cycleTypes" layout="option-select" key="label.cycleType">
            <fr:property name="from" value="possibleCycleTypes"/>
            <fr:property name="classes" value="list-unstyled"/>
        </fr:slot>
        <fr:slot name="cyclesToEnrol" layout="option-select">
            <fr:property name="from" value="possibleCycleTypes"/>
            <fr:property name="classes" value="list-unstyled"/>
        </fr:slot>
        <fr:slot name="empty"/>
        <fr:slot name="degree"/>
        <fr:slot name="masterDegree"/>
        <fr:slot name="dea"/>
        <fr:slot name="dfa"/>
    </fr:schema>
    <fr:layout name="tabular">
    </fr:layout>
</fr:edit>
