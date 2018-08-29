<%@ page import="org.fenixedu.academic.util.StudentPersonalDataAuthorizationChoice" %>
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
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:xhtml />

<style>
    #main-content-wrapper div.col-sm-10 ul {
        list-style: none;
    }

    .job-platform-info {
        margin-top: 20px;
    }
</style>

<logic:present name="student">
    <h2><bean:message key="title.student.dataShareAuthorizations" bundle="STUDENT_RESOURCES" /></h2>

    <html:link action="/studentDataShareAuthorization.do?method=viewAuthorizationHistory">
        <bean:message key="link.student.dataAuthorizationHistory" bundle="STUDENT_RESOURCES" />
    </html:link>

    <logic:messagesPresent message="true">
        <div class="success3" style="margin-top: 10px"><html:messages id="messages" message="true" bundle="STUDENT_RESOURCES">
            <span><bean:write name="messages" /></span>
        </html:messages></div>
    </logic:messagesPresent>

    <fr:form action="/studentDataShareAuthorization.do?method=saveAuthorization">
        <fr:edit id="studentDataShareAuthorization" name="student">
            <fr:schema bundle="STUDENT_RESOURCES" type="org.fenixedu.academic.domain.student.Student">
                <fr:slot name="personalDataAuthorization" key="label.student.dataShareAuthorization" layout="radio-postback"
                    required="true">
                    <fr:property name="defaultOptionHidden" value="true" />
                    <fr:property name="includedValues" value="<%= StudentPersonalDataAuthorizationChoice.activeValues() %>" />
                </fr:slot>
            </fr:schema>
            <fr:layout name="tabular">
                <fr:property name="classes" value="tstyle2 thlight thright" />
                <fr:property name="columnClasses" value=",,tdclear tderror1" />
            </fr:layout>
        </fr:edit>
    </fr:form>
    
    <div class="infoop">
    	<bean:message key="label.data.authorization.information" bundle="STUDENT_RESOURCES" />
    </div>

    <div class="infoop job-platform-info">
        <bean:message key="label.data.authorization.information.job_platform_info" bundle="STUDENT_RESOURCES" />
    </div>
</logic:present>