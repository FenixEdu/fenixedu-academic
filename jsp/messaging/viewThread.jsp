<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ page import="net.sourceforge.fenixedu.domain.messaging.Forum" %>
<%@ page import="net.sourceforge.fenixedu.domain.messaging.ConversationThread" %>


<logic:present name="forum">
	<bean:define id="forumId" name="forum" property="idInternal" />
	<logic:present name="thread">
	<bean:define id="contextPrefix" name="contextPrefix" />
	<bean:define id="threadId" name="thread" property="idInternal" />
		<logic:present name="person">
			<logic:present name="messages">			
				<bean:define id="conversationMessages" name="thread" property="conversationMessages" />
					<h2><bean:message bundle="MESSAGING_RESOURCES" key="label.viewThread.title"/></h2>
					
					<html:link action="<%= contextPrefix + "method=viewForum&forumId="+ forumId %>">
						<bean:write name="forum" property="name"/>
					</html:link>
					<bean:message bundle="MESSAGING_RESOURCES" key="messaging.breadCrumSeparator.label"/> 
					<bean:write name="thread" property="subject"/>
					
					<br/><br/>		
					
					<fr:view name="thread" layout="tabular" schema="conversationThread.view-with-subject-creation-date-and-message-count">
						<fr:layout>
						    <fr:property name="classes" value="style1"/>
				      		<fr:property name="columnClasses" value="listClasses,"/>
						</fr:layout>
					</fr:view>
					
					<h2><bean:message bundle="MESSAGING_RESOURCES" key="label.viewThread.Messages"/></h2>

					<logic:notEqual name="showReplyBox" value="true">
						<logic:equal name="loggedPersonCanWrite" value="true">
							<html:link action="<%= contextPrefix + "method=prepareCreateMessage&forumId="+forumId+"&threadId="+threadId+"&showReplyBox=true&goToLastPage=true"%>">
								<bean:message bundle="MESSAGING_RESOURCES" key="link.viewThread.showReplyBox"/>
							</html:link>
						</logic:equal>
					</logic:notEqual>
										
					<logic:equal name="showReplyBox" value="true">						
						<fr:create id="createMessage"
								type="net.sourceforge.fenixedu.dataTransferObject.messaging.CreateConversationMessageBean" layout="tabular"
					           schema="conversationMessage.create"
					           action="<%= contextPrefix + "method=createMessage&forumId="+forumId+"&threadId="+threadId+"&showReplyBox=false&goToLastPage=true" %>">
					
								<fr:hidden slot="creator" name="person"/>
								<fr:hidden slot="conversationThread" name="thread"/>
								<logic:present name="quotationText">
				           			<fr:default slot="body" name="quotationText"/>
								</logic:present>
					           <fr:destination name="cancel" path="<%= contextPrefix + "method=viewThread&forumId="+forumId+"&threadId="+threadId+"&showReplyBox=false" %>"/>
						</fr:create>
					</logic:equal>
					
					<br/><br/>
				
					<bean:define id="currentPageNumberString"><bean:write name="currentPageNumber"/></bean:define>	
					<strong><bean:message bundle="MESSAGING_RESOURCES" key="label.viewForum.page"/></strong>&nbsp;
					<logic:iterate id="pageNumber" name="pageNumbers" type="java.lang.Integer">
						<logic:equal name="currentPageNumberString" value="<%=pageNumber.toString()%>">
							<bean:write name="pageNumber"/>
						</logic:equal>
						<logic:notEqual name="currentPageNumber" value="<%=pageNumber.toString()%>">
							<html:link action="<%= contextPrefix +"method=viewThread&forumId=" + forumId.toString() + "&pageNumber=" + pageNumber +"&threadId="+threadId%>">								
								<bean:write name="pageNumber"/>
							</html:link>			
						</logic:notEqual>
					</logic:iterate>
					<br/><br/>
								
					
					<logic:iterate indexId="currentMessageId" id="conversationMessage" name="messages" type="net.sourceforge.fenixedu.domain.messaging.ConversationMessage">
						<html:link linkName="<%=currentMessageId.toString()%>"/>
						<fr:view name="conversationMessage" layout="tabular" schema="conversationMessage.view-with-author-creationDate-and-body">			
						<fr:layout>
								<fr:property name="style" value="width:100%"/>
							    <fr:property name="classes" value="style1"/>
					      		<fr:property name="columnClasses" value="listClasses,"/>
							</fr:layout>
						</fr:view>
						<logic:equal name="loggedPersonCanWrite" value="true">
							<bean:define id="quotedMessageId" name="conversationMessage" property="idInternal" />
							<html:link action="<%=contextPrefix.toString() + "method=prepareCreateMessage&showReplyBox=true&goToLastPage=true&threadId=" + threadId + "&forumId=" + forumId + "&quotedMessageId=" + quotedMessageId%>"> 
								<bean:message key="messaging.viewThread.quote" bundle="MESSAGING_RESOURCES"/>
							</html:link>
						</logic:equal>
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
							<html:link action="<%= contextPrefix + "method=viewThread&forumId=" + forumId.toString() + "&pageNumber=" + pageNumber +"&threadId="+threadId%>">								
								<bean:write name="pageNumber"/>
							</html:link>			
						</logic:notEqual>
					</logic:iterate>
					
				</logic:present>
		</logic:present>
	</logic:present>
</logic:present>