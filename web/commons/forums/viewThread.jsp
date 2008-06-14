<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/collectionPager.tld" prefix="cp" %>

<logic:present name="forum">
	<bean:define id="forumId" name="forum" property="idInternal" />
	<logic:present name="thread">
	<bean:define id="prefix" name="contextPrefix" type="java.lang.String"/>
	<bean:define id="contextPrefix" value="<%= prefix + (prefix.contains("?") ? "&amp;" : "?") %>" type="java.lang.String"/>
	<bean:define id="threadId" name="thread" property="idInternal" />
		<logic:present name="person">
			<logic:present name="messages">			
				<bean:define id="conversationMessages" name="thread" property="conversationMessages" />
					<em><bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.executionCourse.forum" />&nbsp;<bean:write name="forum" property="name"/></em>
					<h2><bean:message bundle="MESSAGING_RESOURCES" key="label.viewThread.title"/></h2>
					
					<p class="mbottom0">
					<html:link action="<%= contextPrefix + "method=viewForum&amp;forumId="+ forumId %>">
						<bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.executionCourse.backToForum" />
					</html:link>
					</p>

					<fr:view name="thread" layout="tabular" schema="conversationThread.view-with-subject-creation-date-and-message-count">
						<fr:layout>
						    <fr:property name="classes" value="tstyle1 thlight thright"/>
						</fr:layout>
					</fr:view>

					<logic:notEqual name="showReplyBox" value="true">
						<logic:equal name="loggedPersonCanWrite" value="true">
							<html:link action="<%= contextPrefix + "method=prepareCreateMessage&amp;forumId="+forumId+"&amp;threadId="+threadId+"&amp;showReplyBox=true"%>">
								<bean:message bundle="MESSAGING_RESOURCES" key="link.viewThread.showReplyBox"/>
							</html:link>
						</logic:equal>
					</logic:notEqual>

					 
					<logic:equal name="showReplyBox" value="true">						
						<fr:create id="createMessage"
								type="net.sourceforge.fenixedu.dataTransferObject.messaging.CreateConversationMessageBean"
					           schema="conversationMessage.create"
					           action="<%= contextPrefix + "method=createMessage&amp;forumId=" + forumId + "&amp;threadId=" + threadId + "&amp;showReplyBox=false" %>">
					           
					            <fr:layout name="tabular">
									<fr:property name="classes" value="thlight mtop05 tstyle5"/>
									<fr:property name="columnClasses" value=",,tdclear tderror1"/>							    
								</fr:layout>
					
								<fr:hidden slot="creator" name="person"/>
								<fr:hidden slot="conversationThread" name="thread"/>
								<logic:present name="quotationText">
				           			<fr:default slot="body" name="quotationText"/>
								</logic:present>
					           <fr:destination name="cancel" path="<%= contextPrefix.toString().replaceAll("&amp;", "&") + "method=viewThread&forumId="+forumId+"&threadId="+threadId+"&showReplyBox=false" %>"/>
						</fr:create>
						<br/>
					</logic:equal>
 
 
   				    <p class="mbottom0">
						<bean:message bundle="MESSAGING_RESOURCES" key="label.viewForum.page"/>: 
						<cp:collectionPages url="<%= "/teacher" + contextPrefix + "method=viewThread&amp;forumId=" + forumId.toString() +"&amp;threadId=" + threadId %>" 
										pageNumberAttributeName="pageNumber" numberOfPagesAttributeName="pageNumbers" numberOfVisualizedPages="10"/>
					</p>
					
					<logic:iterate indexId="currentMessageId" id="conversationMessage" name="messages" type="net.sourceforge.fenixedu.domain.messaging.ConversationMessage">
						<html:link linkName="<%="ID_" + currentMessageId.toString()%>"/>
						<fr:view name="conversationMessage" layout="tabular" schema="conversationMessage.view-with-author-creationDate-and-body">			
							<fr:layout>
								<fr:property name="style" value="width:100%"/>
							    <fr:property name="classes" value="tstyle4 thlight thright mbottom0 thtop forumthread"/>
							    <fr:property name="columnClasses" value="width8em,"/>
							</fr:layout>
						</fr:view>
						<logic:equal name="loggedPersonCanWrite" value="true">
							<bean:define id="quotedMessageId" name="conversationMessage" property="idInternal" />
							<p class="mtop05">
							<html:link action="<%=contextPrefix.toString() + "method=prepareCreateMessage&amp;showReplyBox=true&amp;threadId=" + threadId + "&amp;forumId=" + forumId + "&amp;quoteMessageId=" + quotedMessageId%>"> 
								<bean:message key="messaging.viewThread.quote" bundle="MESSAGING_RESOURCES"/>
							</html:link>
							</p>
						</logic:equal>
					</logic:iterate>

				</logic:present>
		</logic:present>
	</logic:present>
</logic:present>