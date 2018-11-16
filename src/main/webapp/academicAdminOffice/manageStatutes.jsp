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
<%@ page isELIgnored="true"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<html:xhtml/>

<h2><bean:message key="label.studentStatutes.manage" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
    <p>
        <span class="error"><!-- Error messages go here --><bean:write name="message" /></span>
    </p>
</html:messages>


<h3 class="mtop15 mbottom025"><bean:message key="label.studentDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
<table class="mtop025">
    <tr>
        <td>
            <fr:view name="student" schema="student.show.personAndStudentInformation">
                <fr:layout name="tabular">
                    <fr:property name="classes" value="tstyle4 thright thlight mtop0"/>
                    <fr:property name="rowClasses" value="tdhl1,,,,"/>
                </fr:layout>
            </fr:view>
        </td>
        <td style="vertical-align: top; padding-left: 10px;">
            <bean:define id="personID" name="student" property="person.username"/>
            <html:img align="middle" src="<%= request.getContextPath() + "/user/photo/" + personID.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES"/>
        </td>
    </tr>
</table>


<h3 class="mbottom025"><bean:message key="label.studentStatutes" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>

<logic:empty name="student" property="allStatutes">
    <p><em><bean:message key="label.studentStatutes.unavailable" bundle="ACADEMIC_OFFICE_RESOURCES"/>.</em></p>
</logic:empty>

<logic:notEmpty name="student" property="allStatutes">
    <fr:view name="student" property="allStatutes">
        <fr:schema bundle="ACADEMIC_OFFICE_RESOURCES" type="org.fenixedu.academic.dto.student.StudentStatuteBean">
            <fr:slot name="statuteType" key="label.code" >
                <fr:property name="format" value="${code}" /> 
            </fr:slot>
            <fr:slot name="description" key="label.statuteType" />
            <fr:slot name="beginPeriodFormatted" key="label.beginExecutionPeriod" />
            <fr:slot name="studentStatute.beginDate" key="label.beginDate" />
            <fr:slot name="endPeriodFormatted" key="label.endExecutionPeriod" />
            <fr:slot name="studentStatute.endDate" key="label.endDate" />
            <fr:slot name="studentStatute.registration" key="label.registration" >
                <fr:property name="format" value="${degree.presentationNameI18N.content}" /> 
            </fr:slot>
        </fr:schema>
        <fr:layout name="tabular">
            <fr:property name="classes" value="tstyle4 thlight"/>
            <fr:property name="linkFormat(delete)" value="/studentStatutes.do?method=deleteStatute&statuteId=${studentStatute.externalId}" />
            <fr:property name="key(delete)" value="link.student.statute.delete"/>
            <fr:property name="bundle(delete)" value="ACADEMIC_OFFICE_RESOURCES"/>
            <fr:property name="visibleIf(delete)" value="statuteType.explicitCreation"/>
            <fr:property name="contextRelative(delete)" value="true"/>      
            <fr:property name="sortBy" value="studentStatute.beginExecutionPeriod=desc,studentStatute.beginDate=asc,studentStatute.endExecutionPeriod=desc,studentStatute.endDate=asc" />
        </fr:layout>
    </fr:view>
</logic:notEmpty>


<bean:define id="studentID" name="student" property="externalId" />
<bean:define id="studentOID" name="student" property="externalId" />
<bean:define id="schemaID" name="schemaName" />
<h3 class="mtop15 mbottom025"><bean:message key="label.addNewStatute" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>

<logic:equal name="schemaID" value="student.createSeniorStatute">
    <fr:edit name="manageStatuteBean" action="/studentStatutes.do?method=addNewStatute">
        <fr:schema bundle="ACADEMIC_OFFICE_RESOURCES" type="org.fenixedu.academic.dto.student.ManageStudentStatuteBean">
            <fr:slot name="statuteType" layout="menu-select-postback" key="label.statuteType" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
                <fr:property name="providerClass"
                    value="org.fenixedu.academic.ui.renderers.providers.student.StudentStatuteTypeProviderForCreation" />
                <fr:property name="format" value="${code} - ${name.content}" />
                <fr:property name="sortBy" value="name.content=asc" />
                <fr:property name="destination" value="seniorStatutePostBack"/> 
            </fr:slot>
            <fr:slot name="beginExecutionPeriod" layout="menu-select" key="label.beginExecutionPeriod" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator" >
                <fr:property name="providerClass" value="org.fenixedu.academic.ui.renderers.providers.StudentExecutionPeriodsProvider" />
                <fr:property name="format" value="${qualifiedName}" />
            </fr:slot>
            <fr:slot name="endExecutionPeriod" layout="menu-select"  key="label.endExecutionPeriod" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator" >
                <fr:property name="providerClass" value="org.fenixedu.academic.ui.renderers.providers.StudentExecutionPeriodsProvider" />
                <fr:property name="format" value="${qualifiedName}" />
            </fr:slot>
            <fr:slot name="beginDate" key="label.beginDate">
                <fr:property name="size" value="12"/>
                <fr:property name="maxLength" value="10"/>
            </fr:slot>
            <fr:slot name="endDate" key="label.endDate">
                <fr:property name="size" value="12"/>
                <fr:property name="maxLength" value="10"/>
            </fr:slot>
            <fr:slot name="registration" layout="menu-select" key="label.studentRegistrations" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
                <fr:property name="providerClass" value="org.fenixedu.academic.ui.renderers.providers.StudentRegisrationsProvider" />
                <fr:property name="format" value="${degree.presentationName}" />
            </fr:slot>
        </fr:schema>

        <fr:layout name="tabular">
            <fr:property name="classes" value="tstyle4 thright thlight"/>
            <fr:property name="columnClasses" value=",,tdclear tderror1"/>
        </fr:layout>
        <fr:hidden slot="student" name="student" />
        <fr:destination name="seniorStatutePostBack" path="/studentStatutes.do?method=seniorStatutePostBack"/>
        <fr:destination name="invalid" path="<%="/studentStatutes.do?method=invalid&studentOID=" + studentOID + "&schemaName=" + schemaID.toString()%>"/>
        <fr:destination name="cancel" path="<%="/student.do?method=visualizeStudent&studentID=" + studentID%>" />
    </fr:edit>
</logic:equal>

<logic:equal name="schemaID" value="student.createStatutes">
    <fr:edit name="manageStatuteBean" action="/studentStatutes.do?method=addNewStatute">
        <fr:schema bundle="ACADEMIC_OFFICE_RESOURCES" type="org.fenixedu.academic.dto.student.ManageStudentStatuteBean">
            <fr:slot name="statuteType" layout="menu-select-postback" key="label.statuteType" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
                <fr:property name="providerClass"
                    value="org.fenixedu.academic.ui.renderers.providers.student.StudentStatuteTypeProviderForCreation" />
                <fr:property name="format" value="${code} - ${name.content}" />
                <fr:property name="sortBy" value="name.content=asc" />
                <fr:property name="destination" value="seniorStatutePostBack"/> 
            </fr:slot>
            <fr:slot name="beginExecutionPeriod" layout="menu-select" key="label.beginExecutionPeriod" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator" >
                <fr:property name="providerClass" value="org.fenixedu.academic.ui.renderers.providers.StudentExecutionPeriodsProvider" />
                <fr:property name="format" value="${qualifiedName}" />
            </fr:slot>
            <fr:slot name="endExecutionPeriod" layout="menu-select"  key="label.endExecutionPeriod" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator" >
                <fr:property name="providerClass" value="org.fenixedu.academic.ui.renderers.providers.StudentExecutionPeriodsProvider" />
                <fr:property name="format" value="${qualifiedName}" />
            </fr:slot>
            <fr:slot name="beginDate" key="label.beginDate">
                <fr:property name="size" value="12"/>
                <fr:property name="maxLength" value="10"/>
            </fr:slot>
            <fr:slot name="endDate" key="label.endDate">
                <fr:property name="size" value="12"/>
                <fr:property name="maxLength" value="10"/>
            </fr:slot>
        </fr:schema>

        <fr:layout name="tabular">
            <fr:property name="classes" value="tstyle4 thright thlight"/>
            <fr:property name="columnClasses" value=",,tdclear tderror1"/>
        </fr:layout>
        <fr:destination name="seniorStatutePostBack" path="/studentStatutes.do?method=seniorStatutePostBack"/>
        <fr:destination name="invalid" path="<%="/studentStatutes.do?method=invalid&studentOID=" + studentOID + "&schemaName=" + schemaID.toString()%>"/>
        <fr:destination name="cancel" path="<%="/student.do?method=visualizeStudent&studentID=" + studentID%>" />
    </fr:edit>
</logic:equal>
