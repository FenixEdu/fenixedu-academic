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
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:xhtml />

<logic:present name="student">
    <h2><bean:message key="title.student.dataShareAuthorizations" bundle="STUDENT_RESOURCES" /></h2>

    <html:link action="/studentDataShareAuthorization.do?method=manageAuthorizations">
        <bean:message key="link.back" bundle="COMMON_RESOURCES" />
    </html:link>

    <fr:view name="student" property="studentDataShareAuthorizationSet">
        <fr:schema bundle="STUDENT_RESOURCES" type="net.sourceforge.fenixedu.domain.student.StudentDataShareAuthorization">
            <fr:slot name="since" key="label.student.dataShareAuthorizationDate" />
            <fr:slot name="authorizationChoice" key="label.student.dataShareAuthorization" />
        </fr:schema>
        <fr:layout name="tabular">
            <fr:property name="classes" value="tstyle1 mtop1 tdcenter mtop05" />
            <fr:property name="sortBy" value="since=desc" />
        </fr:layout>
    </fr:view>
</logic:present>