<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<%@ taglib uri="/WEB-INF/email.tld" prefix="email" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="net.sourceforge.fenixedu.domain.cms.messaging.IMailMessage" %>
<%@ page import="javax.mail.internet.InternetAddress" %>
<%@ page import="org.apache.struts.util.MessageResources" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>

<%
		Map params = new HashMap();
		StringBuilder replyToList = new StringBuilder();
%>

<logic:present name="mailingList">
	<bean:define id="mailingList" name="mailingList" type="net.sourceforge.fenixedu.domain.cms.messaging.IMailingList"/>
	<bean:define id="numberOfConversations" type="java.lang.Integer" name="mailingList" property="mailConversationsCount"/>
	
	<h3><bean:message bundle="MANAGER_RESOURCES" bundle="CMS_RESOURCES" key="cms.messaging.mailingList.viewMailingList.title.label"/></h3> 
	<table>
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" bundle="CMS_RESOURCES" key="cms.messaging.mailingList.creating.name.label"/>
			</td>
			<td>
				 <b><bean:write name="mailingList" property="name"/></b>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" bundle="CMS_RESOURCES" key="cms.messaging.mailingList.creating.description.label"/>
			</td>
			<td>
				<bean:write name="mailingList" property="description"/>
			</td>
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" bundle="CMS_RESOURCES" key="cms.messaging.mailingList.conversationsNumber.label"/>
			</td>
			<td>
				<bean:write name="mailingList" property="mailConversationsCount"/>
			</td>
		</tr>		
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" bundle="CMS_RESOURCES" key="cms.messaging.mailingList.messagesNumber.label"/>
			</td>
			<td>
				<bean:write name="mailingList" property="mailMessagesCount"/>
			</td>
		</tr>				
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" bundle="CMS_RESOURCES" key="cms.messaging.mailingList.size.label"/>
			</td>
			<td>
				<%
					params.clear();
					params.put("method","downloadMLArchive");
					params.put("mailingListID",mailingList.getIdInternal());
					params.put("threaded","false");	
					int mailingListSize = mailingList.getSize();
					String unit = "B";
					if (mailingListSize > 1000000)
					{
						unit="M"+unit;
						mailingListSize = mailingListSize /1000000;
					}
					else if (mailingListSize > 1000)
					{
						unit="K"+unit;
						mailingListSize = mailingListSize / 1000;
					}
					request.setAttribute("params",params);
					String mailingListSizeMessage = mailingListSize+" "+unit;
					request.setAttribute("mailingListSizeMessage",mailingListSizeMessage);
				%>
				<bean:write name="mailingListSizeMessage"/> <bean:message bundle="MANAGER_RESOURCES" key="cms.aproximate.abbreviation" bundle="CMS_RESOURCES"/>
			</td>
		</tr>
		<logic:greaterThan name="numberOfConversations" value="0">				
			<bean:define id="numberOfMails" type="java.lang.Integer" name="mailingList" property="mailMessagesCount"/>	
			<tr>
				<td>
					&nbsp;
				</td>
				<td>
					<html:link  action="/mailingListManagement" module="/cms" name="params">
						<font size="-15">
							<bean:message bundle="MANAGER_RESOURCES" bundle="CMS_RESOURCES" key="cms.messaging.mailingList.viewMailignList.downloadWholeArchive.label"/>
						</font>
					</html:link>
				</td>
			</tr>										
			<tr>
				<td>
					&nbsp;
				</td>
				<td>
					<%
					params.clear();
					params.put("method","downloadMLArchive");
					params.put("mailingListID",mailingList.getIdInternal());
					params.put("threaded","true");	
					request.setAttribute("params",params);
					 %>				
					<html:link  action="/mailingListManagement" module="/cms" name="params">
						<font size="-15">
							<bean:message bundle="MANAGER_RESOURCES" bundle="CMS_RESOURCES" key="cms.messaging.mailingList.viewMailignList.downloadWholeThreadedArchive.label"/>
						</font>
					</html:link>
				</td>
			</tr>
			<tr>
				<td>
					<bean:message bundle="MANAGER_RESOURCES" bundle="CMS_RESOURCES" key="cms.messaging.mailingList.viewMailingList.addresses.label"/>
				</td>
				<td>
					<logic:iterate indexId="index" name="mailingList" id="alias" property="aliasesIterator" type="net.sourceforge.fenixedu.domain.cms.infrastructure.IMailAddressAlias">
						<%
						replyToList = new StringBuilder().append("mailto:").append(alias.getAddress());
						replyToList.append("@").append(mailingList.getCms().getConfiguration().getMailingListsHostToUse());
						 %>
						 <html:link  href="<%=replyToList.toString()%>">
							 <bean:write name="alias" property="address"/>
						</html:link>
						<logic:greaterThan name="index" value="0">
						 	<bean:message bundle="MANAGER_RESOURCES" bundle="CMS_RESOURCES" key="cms.messaging.mailingList.viewMailingList.addresses.separator.label"/>
						 </logic:greaterThan>
					</logic:iterate>
					<%
					replyToList = new StringBuilder().append("mailto:").append(mailingList.getAddress());
					replyToList.append("@").append(mailingList.getCms().getConfiguration().getMailingListsHostToUse());
					 %>				
					<html:link  href="<%=replyToList.toString()%>">
							 <bean:write name="mailingList" property="address"/>
					</html:link>
				</td>
			</tr>					
			<tr>
				<td>
					<bean:message bundle="MANAGER_RESOURCES" bundle="CMS_RESOURCES" key="cms.messaging.mailingList.viewMailingList.listsState.label"/>
				</td>
				<td>
					<logic:equal name="mailingList" property="closed" value="true">
						<bean:message bundle="MANAGER_RESOURCES" bundle="CMS_RESOURCES" key="cms.messaging.mailingList.viewMailingList.inactive.label"/>
					</logic:equal>
					<logic:equal name="mailingList" property="closed" value="false">
						<bean:message bundle="MANAGER_RESOURCES" bundle="CMS_RESOURCES" key="cms.messaging.mailingList.viewMailingList.active.label"/>
					</logic:equal>					
				</td>
			</tr>		
		</logic:greaterThan>						
			<tr>
				<td>
					<bean:message bundle="MANAGER_RESOURCES" bundle="CMS_RESOURCES" key="cms.userGroupsManagement.viewMailingList.membersOnly.label"/>
				</td>
				<td>
					<logic:equal name="mailingList" property="membersOnly" value="true">
						<bean:message bundle="MANAGER_RESOURCES" bundle="CMS_RESOURCES" key="cms.messaging.yes"/>
					</logic:equal>
					<logic:equal name="mailingList" property="membersOnly" value="false">
						<bean:message bundle="MANAGER_RESOURCES" bundle="CMS_RESOURCES" key="cms.messaging.no"/>
					</logic:equal>					
				</td>
			</tr>
				<td>
					<bean:message bundle="MANAGER_RESOURCES" bundle="CMS_RESOURCES" key="cms.userGroupsManagement.viewMailingList.replyToList.label"/>					
				</td>				
				<td>
					<logic:equal name="mailingList" property="replyToList" value="true">
						<bean:message bundle="MANAGER_RESOURCES" bundle="CMS_RESOURCES" key="cms.messaging.yes"/>
					</logic:equal>
					<logic:equal name="mailingList" property="replyToList" value="false">
						<bean:message bundle="MANAGER_RESOURCES" bundle="CMS_RESOURCES" key="cms.messaging.no"/>
					</logic:equal>					
				</td>							
			</tr>							
	</table>
	
	<h4><bean:message bundle="MANAGER_RESOURCES" bundle="CMS_RESOURCES" key="cms.messaging.mailingList.viewMailingList.conversations.title"/></h4>
	<%
	replyToList = new StringBuilder().append("mailto:").append(mailingList.getAddress());
	replyToList.append("@").append(mailingList.getCms().getConfiguration().getMailingListsHostToUse());
	 %>

	<logic:equal name="mailingList" property="closed" value="false">
		<ul>
			<li><html:link  href="<%=replyToList.toString()%>"><bean:message bundle="MANAGER_RESOURCES" key="cms.messaging.mailingList.createNewConversation" bundle="CMS_RESOURCES"/></html:link></li>
		</ul>
	</logic:equal>						 
	
	<table width="100%" class="style1">
		<tr>
			<th class="listClasses-header" width="40%"><bean:message bundle="MANAGER_RESOURCES" bundle="CMS_RESOURCES" key="cms.messaging.mailingList.viewMailingList.subject.label"/></th>
			<th class="listClasses-header" width="40%"><bean:message bundle="MANAGER_RESOURCES" bundle="CMS_RESOURCES" key="cms.messaging.mailingList.viewMailingList.conversationStarter.label"/></th>		
			<th class="listClasses-header" width="20%"><bean:message bundle="MANAGER_RESOURCES" bundle="CMS_RESOURCES" key="cms.messaging.mailingList.viewMailingList.conversatioCreationDate.label"/></th>					
			<th class="listClasses-header" width="0%"><bean:message bundle="MANAGER_RESOURCES" bundle="CMS_RESOURCES" key="cms.messaging.mailingList.viewMailingList.messages.label"/></th>							

		</tr>		
		<logic:iterate id="conversation" name="mailingList" property="mailConversationsIterator" type="net.sourceforge.fenixedu.domain.cms.messaging.IMailConversation">
			<tr>
				<bean:define id="firstMessage" name="conversation" property="firstMessage" type="net.sourceforge.fenixedu.domain.cms.messaging.IMailMessage"/>
										
				<td class="listClasses" style="text-align:left">
					<%
						params.clear();
						params.put("method","viewThread");
						params.put("threadId",conversation.getIdInternal());
						params.put("mailingListID",mailingList.getIdInternal());
						request.setAttribute("params",params);
					 %>			
						<html:link  action="/mailingListThreadManagement" module="/cms" name="params">
							<logic:equal name="conversation" property="subject" value="">
								<bean:message bundle="MANAGER_RESOURCES" bundle="CMS_RESOURCES" key="cms.messaging.mailingConversation.mailMessage.noSubject.label"/>
							</logic:equal>
							<logic:notEqual name="conversation" property="subject" value="">
								<bean:write name="conversation" property="subject"/>
							</logic:notEqual>
						</html:link>		
				</td>
				<td class="listClasses" style="text-align:left">
					<email:writeSender name="firstMessage" addressSeparator=", "/>		
				</td>				
				<td class="listClasses" >
					<dt:format pattern="dd/MM/yyyy HH:mm"><bean:write name="firstMessage" property="creationDate.time"/></dt:format>
				</td>
				<td class="listClasses" width="0%"><bean:write name="conversation" property="mailMessagesCount"/></td>										
			</tr>
		</logic:iterate>
	</table>
	
	<h4><bean:message bundle="MANAGER_RESOURCES" bundle="CMS_RESOURCES" key="cms.messaging.mailingList.viewMailingList.lastMessages.title"/></h4>
	<table width="100%" class="style1">
			<tr>
				<th class="listClasses-header" width="15%"><bean:message bundle="MANAGER_RESOURCES" bundle="CMS_RESOURCES" key="cms.messaging.mailingList.viewMailingList.subject.title"/></th>
				<th class="listClasses-header" width="25%"><bean:message bundle="MANAGER_RESOURCES" bundle="CMS_RESOURCES" key="cms.messaging.mailingList.viewMailingList.conversationStarter.label"/></th>
				<th class="listClasses-header" width="50%"><bean:message bundle="MANAGER_RESOURCES" bundle="CMS_RESOURCES" key="cms.messaging.mailingList.viewMailingList.message.label"/></th>
				<th class="listClasses-header" width="10%"><bean:message bundle="MANAGER_RESOURCES" bundle="CMS_RESOURCES" key="cms.messaging.mailingList.viewMailingList.conversatioCreationDate.label"/></th>
				<th class="listClasses-header" width="0%"><bean:message bundle="MANAGER_RESOURCES" bundle="CMS_RESOURCES" key="cms.messaging.mailingList.viewMailingList.messages.label"/></th>								
			</tr>		
			
		<logic:iterate indexId="mailMessageIndex" id="mailMessage" name="mailingList" property="mailMessagesIterator" type="net.sourceforge.fenixedu.domain.cms.messaging.IMailMessage">
			<logic:lessThan name="mailMessageIndex" value="15">
				<bean:define id="conversationsNumber">
					<%=mailMessage.getMailConversationsCount() %>
				</bean:define>
				<logic:iterate indexId="conversationIndex" id="conversation" name="mailMessage" property="mailConversationsIterator" type="net.sourceforge.fenixedu.domain.cms.messaging.IMailConversation">			
					<tr>
						<td class="listClasses" style="text-align:left">
						<%
							params.clear();
							params.put("method","viewThread");
							params.put("threadId",conversation.getIdInternal());
							params.put("mailingListID",mailingList.getIdInternal());
							request.setAttribute("params",params);
						 %>			
						<html:link  action="/mailingListThreadManagement" module="/cms" name="params">
							<logic:equal name="conversation" property="subject" value="">
								<bean:message bundle="MANAGER_RESOURCES" bundle="CMS_RESOURCES" key="cms.messaging.mailingConversation.mailMessage.noSubject.label"/>
							</logic:equal>
							<logic:notEqual name="conversation" property="subject" value="">
								<bean:write name="conversation" property="subject"/>
							</logic:notEqual>
						</html:link>					
	
						</td>
						<logic:lessThan name="conversationIndex" value="1">
							<td class="listClasses" style="text-align:left" rowspan="<%=conversationsNumber %>">
								<email:writeSender name="mailMessage"/>
							</td>
						</logic:lessThan>
						<%
							params.clear();
							params.put("method","viewThread");
							params.put("threadId",conversation.getIdInternal());
							params.put("mailingListID",mailingList.getIdInternal());
							params.put("messageId",mailMessage.getIdInternal());
							request.setAttribute("params",params);
						 %>
						<logic:lessThan name="conversationIndex" value="1">
							<td class="listClasses" style="text-align:left" rowspan="<%=conversationsNumber %>">
								<html:link  action="/mailingListThreadManagement" module="/cms" name="params" anchor="<%=mailMessage.getIdInternal().toString()%>">
									<email:bodyDigest filter="true" name="mailMessage" bundle="CMS_RESOURCES" chars="10"/>
								</html:link>
								<email:writeAttachments name="mailMessage" bundle="CMS_RESOURCES"/>
							</td>
						</logic:lessThan>						
						<td class="listClasses">
							<dt:format pattern="dd/MM/yyyy HH:mm"><bean:write name="mailMessage" property="creationDate.time"/></dt:format>
						</td>
						<td class="listClasses">
							<bean:write name="conversation" property="mailMessagesCount"/>
						</td>					
					</tr>
				</logic:iterate>
			</logic:lessThan>
		</logic:iterate>
		</table>
	
	
</logic:present>