<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/collectionPager.tld" prefix="cp" %>
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
	url="<%="/messaging/emails.do?method=viewSentEmails" + "&amp;senderId=" + request.getAttribute("senderId")%>" 
	pageNumberAttributeName="pageNumber" numberOfPagesAttributeName="numberOfPages" />
	
	<fr:view name="messages" schema="net.sourceforge.fenixedu.domain.util.email.Message.list">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight"/>
			<fr:property name="columnClasses" value=",,aleft,"/>
			<fr:property name="link(view)" value="/emails.do?method=viewEmail"/>
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
			<fr:property name="link(view)" value="/emails.do?method=viewSentEmails"/>
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
			<fr:property name="link(view)" value="/emails.do?method=viewSentEmails"/>
			<fr:property name="bundle(view)" value="APPLICATION_RESOURCES"/>
			<fr:property name="key(view)" value="link.view"/>
			<fr:property name="param(view)" value="externalId/senderId"/>
			<fr:property name="order(view)" value="1"/>
		</fr:layout>
	</fr:view>
</logic:present>

<logic:present name="searchSendersBean">
	<form action="<%= request.getContextPath() + "/messaging/emails.do" %>" method="post">
		<html:hidden property="method" value="viewSentEmails"/>

		<fr:edit id="searchSendersBean" name="searchSendersBean" type="net.sourceforge.fenixedu.presentationTier.Action.messaging.SearchSendersBean">
			<fr:schema bundle="MESSAGING_RESOURCES" type="net.sourceforge.fenixedu.presentationTier.Action.messaging.SearchSendersBean">
				<fr:slot name="searchString" bundle="MESSAGING_RESOURCES" key="label.searchString"/>
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thright thlight mtop05 ulnomargin"/>
				<fr:property name="columnClasses" value=",,tdclear tderror1"/>
			</fr:layout>

			<fr:destination name="selectSender" path="/emails.do?method=newEmail"/>
			<fr:destination name="cancel" path="/index.do"/>
		</fr:edit>
	</form>

	<bean:define id="searchResult" name="searchSendersBean" property="result"/>
	<logic:notEmpty name="searchResult">
		<fr:view name="searchResult" schema="net.sourceforge.fenixedu.domain.util.email.Sender.list"  >
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight mtop05"/>
				<fr:property name="columnClasses" value=",,aleft,"/>
				<fr:property name="link(view)" value="/emails.do?method=viewSentEmails"/>
				<fr:property name="bundle(view)" value="APPLICATION_RESOURCES"/>
				<fr:property name="key(view)" value="link.view"/>
				<fr:property name="param(view)" value="externalId/senderId"/>
				<fr:property name="order(view)" value="1"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
</logic:present>
