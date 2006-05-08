<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ page import="net.sourceforge.fenixedu.domain.messaging.Forum" %>
<%@ page import="net.sourceforge.fenixedu.domain.messaging.ConversationThread" %>

<logic:present name="forum">
	<logic:present name="thread">
		<logic:present name="person">
			<bean:define id="conversationMessages" name="thread" property="conversationMessages" />
				<h2><bean:message key="label.viewThread.title"/> <bean:write name="thread" property="subject"/></h2>
				<h3><bean:message key="label.viewThread.viewingForum"/> <bean:write name="forum" property="name"/></h3>
				<fr:view name="thread" layout="tabular" schema="conversationThread.view-with-subject-creation-date-and-message-count">
					<fr:layout>
					    <fr:property name="classes" value="style1"/>
			      		<fr:property name="columnClasses" value="listClasses,"/>
					</fr:layout>
				</fr:view>
		
					<%
						java.util.Map parameters = new java.util.HashMap();
						parameters.put("method","viewThread");
						Integer forumId = ((Forum)request.getAttribute("forum")).getIdInternal();
						parameters.put("forumId",forumId);
						parameters.put("threadId",((ConversationThread)request.getAttribute("thread")).getIdInternal());
						request.setAttribute("parameters",parameters);
					%>
				<logic:equal name="showReplyBox" value="false">
					<%
						parameters.put("showReplyBox","true");
					%>
					
					<html:link action="/forunsManagement" name="parameters">
						<bean:message key="link.viewThread.showReplyBox"/>
					</html:link>
				</logic:equal>
				
				<logic:equal name="showReplyBox" value="true">
					<%
							parameters.put("showReplyBox","false");
					%>
					<html:link action="/forunsManagement" name="parameters">
						<bean:message key="link.viewThread.hideReplyBox"/>
					</html:link>
					
					<fr:create type="net.sourceforge.fenixedu.domain.messaging.ConversationMessage" layout="tabular"
				           schema="conversationMessage.create"
				           action="<%="/forunsManagement.do?method=viewForum&forumId="+forumId %>">
				
				           <fr:hidden slot="creator" name="person"/>
				           <fr:hidden slot="conversationThread" name="thread"/>
					</fr:create>
					
					<html:link action="/forunsManagement" name="parameters">
						<bean:message key="link.viewThread.hideReplyBox"/>
					</html:link>
				</logic:equal>
				
				<h2><bean:message key="label.viewThread.threads"/></h2>
				<logic:iterate id="conversationMessage" name="conversationMessages" type="net.sourceforge.fenixedu.domain.messaging.ConversationMessage">
					<fr:view name="conversationMessage" layout="tabular" schema="conversationMessage.view-with-author-creationDate-and-body">			
					<fr:layout>
							<fr:property name="style" value="width:100%"/>
						    <fr:property name="classes" value="style1"/>
				      		<fr:property name="columnClasses" value="listClasses,"/>
						</fr:layout>
					</fr:view>
				</logic:iterate>
		</logic:present>
	</logic:present>
</logic:present>