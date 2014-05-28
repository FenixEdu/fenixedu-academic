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
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/collection-pager" prefix="cp" %>
<html:xhtml/>

<h2><bean:message bundle="MESSAGING_RESOURCES" key="title.email.sent.emails"/></h2>

<logic:present name="sender">
	<fr:view name="sender" schema="net.sourceforge.fenixedu.domain.util.email.Sender.info">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thlight thleft thtop ulmvert0 ulindent075"/>
			<fr:property name="rowClasses" value=",tdbold"/>
		</fr:layout>
	</fr:view>

	<div class="infoop2">
    	<p class="mvert0"><bean:message bundle="MESSAGING_RESOURCES" key="message.email.send.queue"/></p>
	</div>

	<logic:empty name="sender" property="messages">
		<p>
			<span>
				<bean:message bundle="MESSAGING_RESOURCES" key="message.no.emails.found"/>
			</span>
		</p>
	</logic:empty>
	
	<cp:collectionPages
	url="<%="/messaging/viewSentEmails.do?method=viewSentEmails" + "&amp;senderId=" + request.getAttribute("senderId")%>" 
	pageNumberAttributeName="pageNumber" numberOfPagesAttributeName="numberOfPages" />
	
	<fr:view name="messages" schema="net.sourceforge.fenixedu.domain.util.email.Message.list">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight"/>
			<fr:property name="columnClasses" value=",,aleft,"/>
			<fr:property name="link(view)" value="/viewSentEmails.do?method=viewEmail"/>
			<fr:property name="bundle(view)" value="APPLICATION_RESOURCES"/>
			<fr:property name="key(view)" value="link.view"/>
			<fr:property name="param(view)" value="externalId/messagesId"/>
			<fr:property name="order(view)" value="1"/>
			<fr:property name="sortBy" value="created=desc"/>
		</fr:layout>
	</fr:view>

</logic:present>

<logic:present name="sendersGroups">
	<fr:view name="sendersGroups" schema="net.sourceforge.fenixedu.domain.util.email.Sender.list"  >
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight mtop05"/>
			<fr:property name="columnClasses" value=",,aleft,"/>
			<fr:property name="link(view)" value="/viewSentEmails.do?method=viewSentEmails"/>
			<fr:property name="bundle(view)" value="APPLICATION_RESOURCES"/>
			<fr:property name="key(view)" value="link.view"/>
			<fr:property name="param(view)" value="externalId/senderId"/>
			<fr:property name="order(view)" value="1"/>
		</fr:layout>
	</fr:view>
	
</logic:present>

<logic:present name="sendersGroupsCourses">
	<fr:view name="sendersGroupsCourses" schema="net.sourceforge.fenixedu.domain.util.email.Sender.list.courses"  >
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight mtop05"/>
			<fr:property name="columnClasses" value=",,aleft,"/>
			<fr:property name="link(view)" value="/viewSentEmails.do?method=viewSentEmails"/>
			<fr:property name="bundle(view)" value="APPLICATION_RESOURCES"/>
			<fr:property name="key(view)" value="link.view"/>
			<fr:property name="param(view)" value="externalId/senderId"/>
			<fr:property name="order(view)" value="1"/>
		</fr:layout>
	</fr:view>
</logic:present>

<logic:present name="searchSendersBean">
	<form action="<%= request.getContextPath() + "/messaging/viewSentEmails.do" %>" method="post">
		<html:hidden property="method" value="viewSentEmails"/>

		<fr:edit id="searchSendersBean" name="searchSendersBean" type="net.sourceforge.fenixedu.presentationTier.Action.messaging.SearchSendersBean">
			<fr:schema bundle="MESSAGING_RESOURCES" type="net.sourceforge.fenixedu.presentationTier.Action.messaging.SearchSendersBean">
				<fr:slot name="searchString" bundle="MESSAGING_RESOURCES" key="label.searchString"/>
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thright thlight mtop05 ulnomargin"/>
				<fr:property name="columnClasses" value=",,tdclear tderror1"/>
			</fr:layout>

			<fr:destination name="selectSender" path="/viewSentEmails.do?method=newEmail"/>
			<fr:destination name="cancel" path="/index.do"/>
		</fr:edit>
	</form>

	<bean:define id="searchResult" name="searchSendersBean" property="result"/>
	<logic:notEmpty name="searchResult">
		<fr:view name="searchResult" schema="net.sourceforge.fenixedu.domain.util.email.Sender.list"  >
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight mtop05"/>
				<fr:property name="columnClasses" value=",,aleft,"/>
				<fr:property name="link(view)" value="/viewSentEmails.do?method=viewSentEmails"/>
				<fr:property name="bundle(view)" value="APPLICATION_RESOURCES"/>
				<fr:property name="key(view)" value="link.view"/>
				<fr:property name="param(view)" value="externalId/senderId"/>
				<fr:property name="order(view)" value="1"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
</logic:present>
