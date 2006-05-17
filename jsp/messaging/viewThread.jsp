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
			<logic:present name="messages">
				<%
					java.util.Map parameters = new java.util.HashMap();
					parameters.put("method","viewThread");
					Integer forumId = ((Forum)request.getAttribute("forum")).getIdInternal();
					Integer threadId = ((ConversationThread)request.getAttribute("thread")).getIdInternal();							
					parameters.put("forumId",forumId);
					parameters.put("threadId",threadId);
					request.setAttribute("parameters",parameters);
				%>
			
				<bean:define id="conversationMessages" name="thread" property="conversationMessages" />
					<h2><bean:message bundle="MESSAGING_RESOURCES" key="label.viewThread.title"/></h2>
					
					<html:link action="<%="/messaging/forunsManagement.do?method=viewForum&forumId="+forumId %>">
						<bean:write name="forum" property="name"/>
					</html:link>
					<bean:message bundle="MESSAGING_RESOURCES" key="messaging.breadCrumSeparator.label"/> 
					<bean:write name="thread" property="subject"/> 					
					
					<fr:view name="thread" layout="tabular" schema="conversationThread.view-with-subject-creation-date-and-message-count">
						<fr:layout>
						    <fr:property name="classes" value="style1"/>
				      		<fr:property name="columnClasses" value="listClasses,"/>
						</fr:layout>
					</fr:view>	Integer pageNumber = getPageNumber(request);
			
						
					<logic:notEqual name="showReplyBox" value="true">
						<%
							parameters.put("showReplyBox","true");
							parameters.put("goToLastPage","true");
						%>
						
						<html:link action="/messaging/forunsManagement" name="parameters">
							<bean:message bundle="MESSAGING_RESOURCES" key="link.viewThread.showReplyBox"/>
						</html:link>
					</logic:notEqual>
					
					<logic:equal name="showReplyBox" value="true">
						<%
								parameters.put("showReplyBox","false");
						%>
						
						<fr:create type="net.sourceforge.fenixedu.domain.messaging.ConversationMessage" layout="tabular"
					           schema="conversationMessage.create"
					           action="<%="/messaging/forunsManagement.do?method=createMessage&forumId="+forumId+"&threadId="+threadId+"&showReplyBox=false&goToLastPage=true" %>">
					
					           <fr:hidden slot="creator" name="person"/>
					           <fr:hidden slot="conversationThread" name="thread"/>
					           <fr:destination name="cancel" path="<%="/messaging/forunsManagement.do?method=viewThread&forumId="+forumId+"&threadId="+threadId+"&showReplyBox=false" %>"/>
					           <fr:default slot="body" name="quotationText"/>
						</fr:create>						
					</logic:equal>
					
					<h2><bean:message bundle="MESSAGING_RESOURCES" key="label.viewThread.threads"/></h2>
					<logic:iterate indexId="currentMessageId" id="conversationMessage" name="messages" type="net.sourceforge.fenixedu.domain.messaging.ConversationMessage">
						<html:link linkName="<%=currentMessageId.toString()%>"/>
						<fr:view name="conversationMessage" layout="tabular" schema="conversationMessage.view-with-author-creationDate-and-body">			
						<fr:layout>
								<fr:property name="style" value="width:100%"/>
							    <fr:property name="classes" value="style1"/>
					      		<fr:property name="columnClasses" value="listClasses,"/>
							</fr:layout>
						</fr:view>
						<%
								parameters.clear();
								parameters.put("showReplyBox","true");
								parameters.put("goToLastPage","true");
								parameters.put("forumId",forumId);
								parameters.put("threadId",threadId);
								parameters.put("method","createMessage");
								parameters.put("quotedMessageId",conversationMessage.getIdInternal());
						%>						
						<html:link action="/messaging/forunsManagement" name="parameters">
							<bean:message key="messaging.viewThread.quote" bundle="MESSAGING_RESOURCES"/>
						</html:link>
						<br/>
						<br/>
					</logic:iterate>
													
					<strong><bean:message bundle="MESSAGING_RESOURCES" key="label.viewForum.page"/></strong>&nbsp;
					<bean:define id="currentPageNumberString"><bean:write name="currentPageNumber"/></bean:define>
					<logic:iterate id="pageNumber" name="pageNumbers" type="java.lang.Integer">
						<logic:equal name="currentPageNumberString" value="<%=pageNumber.toString()%>">
							<bean:write name="pageNumber"/>
						</logic:equal>
						<logic:notEqual name="currentPageNumber" value="<%=pageNumber.toString()%>">
							<html:link action="<%="/messaging/forunsManagement.do?method=viewThread&forumId=" + forumId.toString() + "&pageNumber=" + pageNumber +"&threadId="+threadId%>">								
								<bean:write name="pageNumber"/>
							</html:link>			
						</logic:notEqual>
					</logic:iterate>
					
				</logic:present>
		</logic:present>
	</logic:present>
</logic:present>