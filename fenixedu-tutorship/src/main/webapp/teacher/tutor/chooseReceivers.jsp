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
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<html:xhtml/>

<%@page import="net.sourceforge.fenixedu.util.BundleUtil"%>

<h2><bean:message key="title.sendEmail" bundle="APPLICATION_RESOURCES"/></h2>

<em>
	<logic:notPresent name="receiversBean">
		<bean:message key="error.tutor.noActiveTutorships" />
	</logic:notPresent>
</em>

<logic:messagesPresent property="error" message="true">
	<html:messages id="message" property="error" message="true" bundle="MESSAGING_RESOURCES">
		<p><span class="error0"><bean:write name="message"/></span></p>
	</html:messages>
</logic:messagesPresent>

<logic:messagesPresent property="problem" message="true">
    <p>
        <span class="error0">
            <bean:message key="messaging.mail.address.problem.text" bundle="MESSAGING_RESOURCES"/>:
            <html:messages id="message" property="problem" message="true" bundle="MESSAGING_RESOURCES">
                <p class="mtop0 mbottom0"><bean:write name="message"/></p>
            </html:messages>
        </span>
    </p>
</logic:messagesPresent>

<logic:messagesPresent property="warning" message="true">
    <html:messages id="message" property="warning" message="true" bundle="MESSAGING_RESOURCES">
        <p class="mtop1"><span class="warning0"><bean:write name="message"/></span></p>
    </html:messages>
</logic:messagesPresent>

<logic:messagesPresent property="confirmation" message="true">
    <html:messages id="message" property="confirmation" message="true" bundle="MESSAGING_RESOURCES">
        <p><span class="success0"><bean:write name="message"/></span></p>
    </html:messages>
</logic:messagesPresent>

<logic:present name="receiversBean">
	<p>
		<b><bean:message key="label.teacher.tutor.sendMail.chooseReceivers" bundle="APPLICATION_RESOURCES" /></b>
	</p>
	
	<fr:form id="receiversBeanForm" action="/sendMailToTutoredStudents.do?method=prepare">
		<fr:edit id="receiversBean" name="receiversBean">
			<fr:schema bundle="APPLICATION_RESOURCES" type="net.sourceforge.fenixedu.dataTransferObject.teacher.tutor.StudentsByTutorBean">
				<fr:slot name="studentsEntryYear" key="label.studentsEntryYear" layout="menu-select-postback">
					<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.teacher.TutorshipEntryExecutionYearProvider$ActiveTutorshipEntryExecutionYearProviderByTeacher"/> 
					<fr:property name="format" value="${year}"/>
					<fr:property name="defaultText" value="<%= "-- " + BundleUtil.getMessageFromModuleOrApplication("application", "label.view.all") +  " --" %>"/>
				</fr:slot>
			</fr:schema>
			<fr:destination name="postBack" path="/sendMailToTutoredStudents.do?method=prepare"/>
		</fr:edit>
	</fr:form>

	<p class="color888 mvert05"><bean:message key="label.teacher.tutor.sendMail.chooseReceivers.help" bundle="APPLICATION_RESOURCES" /></p>

	<fr:form id="receiversForm" action="/sendMailToTutoredStudents.do?method=prepareCreateMail">
		<fr:edit id="receivers" name="receiversBean" schema="teacher.tutor.chooseEmailReceivers">
		    <fr:layout>
				<fr:property name="displayLabel" value="false"/>
			</fr:layout>
		    <fr:destination name="invalid" path="/sendMailToTutoredStudents.do?method=prepare"/>
		</fr:edit>
		<html:submit ><bean:message key="button.teacher.tutor.select" bundle="APPLICATION_RESOURCES" /></html:submit>
		<html:submit property="selectAll"><bean:message key="button.teacher.tutor.selectAll" bundle="APPLICATION_RESOURCES" /></html:submit>
		<html:submit property="reset"><bean:message key="button.teacher.tutor.clear" bundle="APPLICATION_RESOURCES" /></html:submit>
	</fr:form>
</logic:present>


