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
<%@ page language="java" %>
<%@page import="org.fenixedu.academic.domain.util.email.Sender"%>
<%@page import="org.fenixedu.academic.domain.person.RoleType"%>
<%@page import="org.fenixedu.bennu.core.security.Authenticate"%>
<%@page import="org.fenixedu.bennu.core.domain.User"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<html:xhtml/>

<h2><bean:message bundle="MANAGER_RESOURCES" key="title.send.mail"/></h2>

<logic:present name="errorMessage">
	<p>
		<span class="error0"><bean:write name="errorMessage"/></span>
	</p>
</logic:present>

<% if (Sender.hasAvailableSender()) { %>
<fr:form action="/emails.do">
	<html:hidden property="method" value="sendEmail"/>

	<fr:edit id="emailBean" name="emailBean">
		<fr:schema type="org.fenixedu.academic.domain.util.email.EmailBean" bundle="MESSAGING_RESOURCES">
			<fr:slot name="sender" bundle="MESSAGING_RESOURCES" key="label.fromName" layout="menu-select-postback"
					validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
		        <fr:property name="providerClass" value="org.fenixedu.academic.ui.renderers.providers.EmailSenderProvider" />
				<fr:property name="format" value="${fromName} (${fromAddress})" />
				<fr:property name="destination" value="selectSender"/>
			</fr:slot>
    		<fr:slot name="replyTos" layout="option-select" key="label.replyTos">
        		<fr:property name="providerClass" value="org.fenixedu.academic.ui.renderers.providers.EmailReplyTosProvider" />
        		<fr:property name="eachSchema" value="org.fenixedu.academic.domain.util.email.ReplyTo.selectItem"/>
        		<fr:property name="eachLayout" value="values"/>
        		<fr:property name="classes" value="nobullet noindent"/>
        		<fr:property name="sortBy" value="replyToAddress"/>
    		</fr:slot>
    		<fr:slot name="recipients" layout="option-select" key="label.receiversGroup">
        		<fr:property name="providerClass" value="org.fenixedu.academic.ui.renderers.providers.EmailRecipientsProvider" />
        		<fr:property name="eachSchema" value="org.fenixedu.academic.domain.util.email.Recipient.selectItem"/>
        		<fr:property name="eachLayout" value="values"/>
        		<fr:property name="classes" value="nobullet noindent"/>
        		<fr:property name="sortBy" value="toName"/>
    		</fr:slot>
			<fr:slot name="bccs" bundle="MESSAGING_RESOURCES" key="label.receiversOfCopy">
				<fr:property name="size" value="50" />
			</fr:slot>
			<fr:slot name="subject" bundle="MANAGER_RESOURCES" key="label.email.subject">
				<fr:property name="size" value="50" />
			</fr:slot>
			<fr:slot name="message" bundle="MANAGER_RESOURCES" key="label.email.message" layout="longText">
				<fr:property name="columns" value="80"/>
				<fr:property name="rows" value="10"/>
			</fr:slot>
			<%
				if (RoleType.HTML_CAPABLE_SENDER.isMember(Authenticate.getUser())) {
			%>
					<fr:slot name="htmlMessage" bundle="MESSAGING_RESOURCES" key="label.email.message.html.content" layout="longText">
						<fr:property name="columns" value="80"/>
						<fr:property name="rows" value="10"/>
					</fr:slot>
			<%
				}
			%>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thright thlight mtop05 ulnomargin"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>

		<fr:destination name="selectSender" path="/emails.do?method=newEmail"/>
	</fr:edit>
	<html:submit><bean:message key="label.send" /></html:submit>
</fr:form>
<% } else { %>
	<bean:message bundle="MANAGER_RESOURCES" key="message.not.authorized.to.send.emails"/>
<% } %>
