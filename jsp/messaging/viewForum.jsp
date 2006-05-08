<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ page import="net.sourceforge.fenixedu.domain.messaging.Forum" %>

<logic:present name="forum">
	<bean:define id="conversationThreads" name="forum" property="conversationThreads" />
	<h2><bean:message key="label.viewForum.title"/></h2>
	
	<fr:view name="forum" schema="forum.view-with-topics-and-message-count">
		<fr:layout name="tabular">
	        <fr:property name="classes" value="style1"/>
	        <fr:property name="columnClasses" value="listClasses,"/>
	    </fr:layout>
	</fr:view>

	<h2><bean:message key="label.viewForum.threads"/></h2>
	
	<%
	java.util.Map parameters = new java.util.HashMap();
	parameters.put("method","prepareCreateThreadAndMessage");
	parameters.put("forumId",((Forum)request.getAttribute("forum")).getIdInternal());
	request.setAttribute("parameters",parameters);
	%>
	<html:link action="/forunsManagement" name="parameters">
		<bean:message key="link.viewForum.createThread"/>
	</html:link>
		<logic:present name="conversationThreads">
			<fr:view name="conversationThreads" layout="tabular" schema="conversationThread.view-with-subject-creation-date-and-message-count">
				<fr:layout>
				    <fr:property name="classes" value="style1"/>
		      		<fr:property name="columnClasses" value="listClasses,"/>
					<fr:property name="link(view)" value="/forunsManagement.do?method=viewThread"/>
					<fr:property name="param(view)" value="idInternal/threadId,forum.idInternal/forumId,showReplyBox=false"/>
					<fr:property name="key(view)" value="messaging.viewThread.link"/>
				</fr:layout>
			</fr:view>
		</logic:present>
	<html:link action="/forunsManagement" name="parameters">
		<bean:message key="link.viewForum.createThread"/>
	</html:link>
	
</logic:present>