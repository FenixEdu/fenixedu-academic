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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<em><bean:message key="title.student.portalTitle" bundle="STUDENT_RESOURCES" /></em>
<h2><bean:message key="label.enrollment.personalData.inquiry" /></h2>

<div class="infoop2">
<p><bean:message key="label.info.dislocatedStudent.inquiry" /></p>
</div>

<p class="mtop2 mbottom05"><bean:message key="label.enrollment.personalData.authorization" arg0="<%=org.fenixedu.academic.domain.organizationalStructure.Unit.getInstitutionName().getContent()%>" /></p>

<fr:form action="/studentEnrollmentManagement.do?method=choosePersonalDataAuthorizationChoice">

    <fr:edit name="student" schema="Student.editPersonalDataAuthorization">
        <fr:schema type="org.fenixedu.academic.domain.student.Student" bundle="APPLICATION_RESOURCES">
            <fr:slot name="personalDataAuthorization" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator" layout="menu-select">
                <fr:property name="providerClass" value="org.fenixedu.academic.ui.renderers.providers.student.PersonalDataAuthorizationProvider" />
            </fr:slot>
        </fr:schema>
        <fr:layout name="tabular">
            <fr:property name="classes" value="tstyle2 thlight thright" />
            <fr:property name="columnClasses" value=",,tdclear tderror1" />
        </fr:layout>
    </fr:edit>


    <div class="infoop">
        <bean:message key="label.data.authorization.information.enrolment" bundle="STUDENT_RESOURCES" />
    </div>

    <p>
        <html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit">
            <bean:message bundle="APPLICATION_RESOURCES" key="label.continue" />
        </html:submit>
    </p>
</fr:form>

<p class="mtop2"><em><bean:message key="label.enrollment.personalData.changes" /></em></p>

