<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
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
					<em><bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.executionCourse.forum" />&nbsp;<bean:write name="forum" property="name"/></em>
					<h2><bean:message bundle="MESSAGING_RESOURCES" key="label.viewThread.title"/></h2>
					
					<p class="mbottom0">
						<html:link action="<%= contextPrefix + "method=viewForum&amp;forumId="+ forumId %>">
							<bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.executionCourse.backToForum" />
						</html:link>
					</p>

					<fr:view name="thread" layout="tabular" schema="conversationThread.view-with-subject-creation-date-and-message-count">
						<fr:layout>
						    <fr:property name="classes" value="tstyle2 thlight thright"/>
						</fr:layout>
					</fr:view>

					<logic:notEqual name="showReplyBox" value="true">
						<logic:equal name="loggedPersonCanWrite" value="true">
							<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
							<html:link action="<%= contextPrefix + "method=prepareCreateMessage&amp;forumId="+forumId+"&amp;threadId="+threadId+"&amp;showReplyBox=true&amp;goToLastPage=true"%>">
								<bean:message bundle="MESSAGING_RESOURCES" key="link.viewThread.showReplyBox"/>
							</html:link>
						</logic:equal>
					</logic:notEqual>

					<logic:equal name="showReplyBox" value="true">						
						<fr:create id="createMessage"
								type="net.sourceforge.fenixedu.dataTransferObject.messaging.CreateConversationMessageBean"
					           schema="conversationMessage.create"
					           action="<%= contextPrefix + "method=createMessage&amp;forumId="+forumId+"&amp;threadId="+threadId+"&amp;showReplyBox=false&amp;goToLastPage=true" %>">
					           
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
					<bean:define id="currentPageNumberString"><bean:write name="currentPageNumber"/></bean:define>	
					<bean:message bundle="MESSAGING_RESOURCES" key="label.viewForum.page"/>: 
					<logic:iterate id="pageNumber" name="pageNumbers" type="java.lang.Integer">
						<logic:equal name="currentPageNumberString" value="<%=pageNumber.toString()%>">
							<bean:write name="pageNumber"/>
						</logic:equal>
						<logic:notEqual name="currentPageNumber" value="<%=pageNumber.toString()%>">
							<html:link action="<%= contextPrefix +"method=viewThread&amp;forumId=" + forumId.toString() + "&amp;pageNumber=" + pageNumber +"&amp;threadId="+threadId%>">								
								<bean:write name="pageNumber"/>
							</html:link>			
						</logic:notEqual>
					</logic:iterate>
					</p>								
					
					<logic:iterate indexId="currentMessageId" id="conversationMessage" name="messages" type="net.sourceforge.fenixedu.domain.messaging.ConversationMessage">
						<html:link linkName="<%="ID_" + currentMessageId.toString()%>"/>
						<fr:view name="conversationMessage" layout="tabular" schema="conversationMessage.view-with-author-creationDate-and-body">			
							<fr:layout>
							    <fr:property name="classes" value="tstyle4 thlight thright mbottom0 thtop width100 forumthread"/>
							    <fr:property name="columnClasses" value="width8em,"/>
							</fr:layout>
						</fr:view>
						<logic:equal name="loggedPersonCanWrite" value="true">
							<bean:define id="quotedMessageId" name="conversationMessage" property="idInternal" />
								<table class="width100 gluetop mtop0">
									<tr>
										<th class="width8em"></th>
										<td class="aright">
											<html:link action="<%=contextPrefix.toString() + "method=prepareCreateMessage&amp;showReplyBox=true&amp;goToLastPage=true&amp;threadId=" + threadId + "&amp;forumId=" + forumId + "&amp;quotedMessageId=" + quotedMessageId%>"> 
												<bean:message key="messaging.viewThread.quote" bundle="MESSAGING_RESOURCES"/>
											</html:link>
										</td>
									</tr>
								</table>
						</logic:equal>
					</logic:iterate>

<%--
					<bean:message bundle="MESSAGING_RESOURCES" key="label.viewForum.page"/>
					<bean:define id="currentPageNumberString"><bean:write name="currentPageNumber"/></bean:define>
					<logic:iterate id="pageNumber" name="pageNumbers" type="java.lang.Integer">
						<logic:equal name="currentPageNumberString" value="<%=pageNumber.toString()%>">
							<bean:write name="pageNumber"/>
						</logic:equal>
						<logic:notEqual name="currentPageNumber" value="<%=pageNumber.toString()%>">
<html:link action="<%= contextPrefix + "method=viewThread&amp;forumId=" + forumId.toString() + "&amp;pageNumber=" + pageNumber +"&amp;threadId="+threadId%>">								
								<bean:write name="pageNumber"/>
							</html:link>			
						</logic:notEqual>
					</logic:iterate>
--%>

				</logic:present>
		</logic:present>
	</logic:present>
</logic:present>