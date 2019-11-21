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
<%@page import="org.joda.time.DateTime"%>
<%@page import="org.fenixedu.academic.domain.student.Registration"%>
<%@page import="org.fenixedu.academic.domain.student.Student"%>
<%@page import="org.fenixedu.academic.domain.treasury.TreasuryBridgeAPIFactory"%>
<%@page import="org.fenixedu.academic.domain.treasury.ITreasuryBridgeAPI"%>
<%@ page isELIgnored="true"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/academic" prefix="academic" %>
<html:xhtml/>

<div style="float: right;">
    <bean:define id="personID" name="student" property="person.username"/>
    <html:img align="middle" src="<%= request.getContextPath() + "/user/photo/" + personID.toString() + "?timestamp=" + new DateTime().toDate().getTime()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES" styleClass="showphoto"/>
</div>

<h2><bean:message key="label.studentPage" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>


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
                    <fr:property name="classes" value="tstyle2 thright thlight mtop0 mbottom0"/>
                    <fr:property name="rowClasses" value="tdhl1,,,,"/>
                </fr:layout>
            </fr:view>
        </td>
    </tr>
</table>


<p class="mvert05">
    <img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
    <html:link page="/student.do?method=viewPersonalData" paramId="studentID" paramName="student" paramProperty="externalId">
        <bean:message key="link.student.viewPersonalData" bundle="ACADEMIC_OFFICE_RESOURCES"/>
    </html:link>

    <academic:allowed operation="EDIT_STUDENT_PERSONAL_DATA" permission="ACADEMIC_OFFICE_PERSONAL_DATA">
        <span class="pleft05">
            <img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
            <html:link page="/student.do?method=prepareEditPersonalData" paramId="studentID" paramName="student" paramProperty="externalId">
                <bean:message key="link.student.editPersonalData" bundle="ACADEMIC_OFFICE_RESOURCES"/>
            </html:link>
        </span>
        <span class="pleft05">
            <img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
            <html:link page="/student.do?method=viewStudentLog" paramId="studentID" paramName="student" paramProperty="externalId">
                <bean:message key="link.executionCourse.log" bundle="APPLICATION_RESOURCES"/>
            </html:link>
        </span>
    </academic:allowed>
</p>

<academic:allowed operation="MANAGE_REGISTRATIONS" permission="ACADEMIC_OFFICE_REGISTRATION_ACCESS">
<h3 class="mtop15 mbottom025"><bean:message key="label.studentRegistrations" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
<fr:view name="student" property="registrations">
    <fr:schema bundle="ACADEMIC_OFFICE_RESOURCES" type="<%= Registration.class.getName() %>">
        <fr:slot name="registrationYear" key="label.registrationYear">
            <fr:property name="format" value="${qualifiedName}" />
        </fr:slot>
        <fr:slot name="startDate" layout="null-as-label" key="label.startDate" >
            <fr:property name="label" value="-"/>   
        </fr:slot>
        
        <fr:slot name="number" layout="null-as-label" key="label.number">
            <property name="label" value="-"/>
        </fr:slot>  
        <fr:slot name="this" key="label.degree" layout="format">
            <fr:property name="format" value="${degreeNameWithDescription} (${degree.code})" />
        </fr:slot>
        
        <fr:slot name="activeStateType" key="label.currentState" />
        <fr:slot name="registrationProtocol.description.content" key="label.registrationAgreement" />
        
        <fr:slot name="numberEnroledCurricularCoursesInCurrentYear" key="label.numberEnroledCurricularCoursesInCurrentYear" />
        <fr:slot name="lastEnrolmentExecutionYear" layout="format" key="label.Registration.lastEnrolmentExecutionYear" >
            <fr:property name="format" value="${qualifiedName}" />
        </fr:slot>
    </fr:schema>
    <fr:layout name="tabular">
        <fr:property name="sortBy" value="registrationYear=desc,startDate=desc"/>

        <fr:property name="classes" value="tstyle1 thlight mtop025 boldlink1"/>
        <fr:property name="columnClasses" value="acenter,acenter nowrap,acenter,tdhl1,,acenter,acenter,acenter,nowrap"/>

        <fr:property name="linkFormat(view)" value="/student.do?method=visualizeRegistration&registrationID=${externalId}" />
        <fr:property name="key(view)" value="link.student.visualizeRegistration"/>
        <fr:property name="bundle(view)" value="ACADEMIC_OFFICE_RESOURCES"/>
        <fr:property name="visibleIf(view)" value="allowedToManageRegistration"/>
        <fr:property name="contextRelative(view)" value="true"/>
    </fr:layout>
</fr:view>
</academic:allowed>

<academic:notAllowed operation="MANAGE_REGISTRATIONS" permission="ACADEMIC_OFFICE_REGISTRATION_ACCESS">
    <academic:allowed operation="VIEW_FULL_STUDENT_CURRICULUM" permission="ACADEMIC_OFFICE_REGISTRATION_ACCESS">
        <h3 class="mtop15 mbottom025">
            <bean:message key="label.studentRegistrations" bundle="ACADEMIC_OFFICE_RESOURCES"/>
        </h3>
        <fr:view name="student" property="registrations" schema="student.registrationDetail.short" >
            <fr:layout name="tabular">
        <fr:property name="sortBy" value="startDate=desc"/>

        <fr:property name="classes" value="tstyle1 thlight mtop025 boldlink1"/>
        <fr:property name="columnClasses" value="acenter,acenter,tdhl1,,acenter,nowrap"/>

        <fr:property name="linkFormat(view)" value="/student.do?method=visualizeRegistration&registrationID=${externalId}" />
        <fr:property name="key(view)" value="link.student.visualizeRegistration"/>
        <fr:property name="bundle(view)" value="ACADEMIC_OFFICE_RESOURCES"/>
        <fr:property name="visibleIf(view)" value="allowedToManageRegistration"/>
        <fr:property name="contextRelative(view)" value="true"/>
        </fr:layout>
        </fr:view>  
    </academic:allowed>
</academic:notAllowed>

<%-- qubExtension --%>
<!-- Student Status -->
<h3 class="mbottom025"><bean:message key="label.statutes" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>

<academic:allowed operation="MANAGE_STATUTES" permission="ACADEMIC_OFFICE_STATUTES">
<p class="mvert05">
    <img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
    <html:link action="/studentStatutes.do?method=prepare" paramName="student" paramProperty="externalId" paramId="studentId">
        <bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.studentStatutes.manage" />
    </html:link>
</p>
</academic:allowed>

<%-- qubExtension --%>
<fr:view name="student" property="currentStatutes">
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
        <fr:property name="classes" value="tstyle4 thlight mtop025 mbottom0"/>
        <fr:property name="columnClasses" value=",tdhl1"/>
        <fr:property name="sortBy" value="studentStatute.beginExecutionPeriod.qualifiedName=desc,studentStatute.endExecutionPeriod.qualifiedName=desc" />
    </fr:layout>
</fr:view>

<!-- Extra Curricular Activities -->
<academic:allowed operation="MANAGE_EXTRA_CURRICULAR_ACTIVITIES" permission="ACADEMIC_OFFICE_ENROLMENTS">
<h3 class="mbottom025"><bean:message key="label.extraCurricularActivities" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
<p class="mvert05">
    <img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
    <html:link action="/studentExtraCurricularActivities.do?method=prepare" paramName="student" paramProperty="externalId" paramId="studentId">
        <bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.extraCurricularActivities.manage" />
    </html:link>
</p>
</academic:allowed>

<!-- Payments -->
<bean:define id="student" name="student" type="org.fenixedu.academic.domain.student.Student" />

<academic:allowed operation="MANAGE_STUDENT_PAYMENTS" permission="TREASURY">
<% if(TreasuryBridgeAPIFactory.implementation().isPersonAccountTreasuryManagementAvailable(student.getPerson())) { %>
<h3 class="mbottom025"><bean:message key="label.payments" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
<p class="mtop05 mbottom15">
    <img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
    <%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><html:link href="<%= request.getContextPath() + TreasuryBridgeAPIFactory.implementation().getPersonAccountTreasuryManagementURL(student.getPerson()) %>" >
        <bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.management" />
    </html:link>
</p>
<% } %>
</academic:allowed>

