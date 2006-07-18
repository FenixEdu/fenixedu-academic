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
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.IdentityHashMap" %>


<logic:present name="mailingList">
<%
	Map params = new HashMap();
	StringBuilder replyToTopic = new StringBuilder();
%>
	<logic:present name="mailConversation">
		<bean:define id="mailingList" name="mailingList" type="net.sourceforge.fenixedu.domain.cms.messaging.IMailingList"/>
		<bean:define id="mailConversation" name="mailConversation" type="net.sourceforge.fenixedu.domain.cms.messaging.IMailConversation"/>
		<bean:define id="numberOfMails" type="java.lang.Integer" name="mailingList" property="mailMessagesCount"/>	
		
		<h3><bean:message bundle="MANAGER_RESOURCES" bundle="CMS_RESOURCES" key="cms.messaging.mailingList.viewMailingList.title.label"/></h3> 
		<table>
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" bundle="CMS_RESOURCES" key="cms.messaging.mailingList.creating.name.label"/>
			</td>
			<td>
				<%
				params.put("method","viewMailingList");
				params.put("mailingListID",mailingList.getIdInternal());
				request.setAttribute("params",params);
				 %>
				 <html:link  name="params" action="/mailingListManagement" module="/cms">
					 <b><bean:write name="mailingList" property="name"/></b>
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
					replyToTopic = new StringBuilder().append("mailto:").append(alias.getAddress());
					replyToTopic.append("@").append(mailingList.getCms().getConfiguration().getMailingListsHostToUse());
					replyToTopic.append("?subject=").append(mailConversation.getSubject());
					 %>
					 <logic:greaterThan name="index" value="0">
					 	<bean:message bundle="MANAGER_RESOURCES" bundle="CMS_RESOURCES" key="cms.messaging.mailingList.viewMailingList.addresses.separator.label"/>
					 </logic:greaterThan>
					 <html:link  href="<%=replyToTopic.toString()%>">
						<bean:write name="alias" property="address"/>
					</html:link>
				</logic:iterate>
				<%
				replyToTopic = new StringBuilder().append("mailto:").append(mailingList.getAddress());
				replyToTopic.append("@").append(mailingList.getCms().getConfiguration().getMailingListsHostToUse());
				replyToTopic.append("?subject=").append(mailConversation.getSubject());
				 %>				
				<html:link  href="<%=replyToTopic.toString()%>">
					<bean:write name="mailingList" property="address"/>
				</html:link>
			</td>
		</tr>		
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" bundle="CMS_RESOURCES" key="cms.messaging.mailingList.viewMailingList.conversation.title"/>
			</td>
			<td>
					<logic:equal name="mailConversation" property="subject" value="">
						<bean:message bundle="MANAGER_RESOURCES" bundle="CMS_RESOURCES" key="cms.messaging.mailingConversation.mailMessage.noSubject.label"/>
					</logic:equal>
					<logic:notEqual name="mailConversation" property="subject" value="">
						<bean:write name="mailConversation" property="subject"/>
					</logic:notEqual>
			</td>
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" bundle="CMS_RESOURCES" key="cms.messaging.mailingList.messagesNumber.label"/>
			</td>
			<td>
				<bean:write name="mailConversation" property="mailMessagesCount"/>
			</td>
		</tr>				
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" bundle="CMS_RESOURCES" key="cms.messaging.mailingList.size.label"/>
			</td>
			<td>
				<%

					int conversationSize = mailConversation.getSize();
					String unit = "B";
					if (conversationSize > 1000000)
					{
						unit="M"+unit;
						conversationSize = conversationSize /1000000;
					}
					else if (conversationSize > 1000)
					{
						unit="K"+unit;
						conversationSize = conversationSize / 1000;
					}
					String conversationSizeMessage = conversationSize+" "+unit;	
					request.setAttribute("conversationSizeMessage",conversationSizeMessage);
					 %>			
				<bean:write name="conversationSizeMessage"/> <bean:message bundle="MANAGER_RESOURCES" key="cms.aproximate.abbreviation" bundle="CMS_RESOURCES"/>
			</td>
		</tr>
		<logic:greaterThan name="numberOfMails" value="0">	
			<tr>
				<td>
					&nbsp;
				</td>
				<td>
					<%
						params.clear();
						params.put("method","downloadConversationArchive");
						params.put("mailingListID",mailingList.getIdInternal());
						params.put("threadId",mailConversation.getIdInternal());
						request.setAttribute("params",params);
					 %>				
					<html:link  action="/mailingListThreadManagement" module="/cms" name="params">
						<font size="-15">
							<bean:message bundle="MANAGER_RESOURCES" bundle="CMS_RESOURCES" key="cms.messaging.mailingList.viewMailignList.downloadWholeThreadedArchive.label"/>
						</font>
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

	<h4><bean:message bundle="MANAGER_RESOURCES" bundle="CMS_RESOURCES" key="cms.messaging.mailingList.viewMailingList.firstMessage.label"/></h4>
	<bean:define id="firstMessage" name="mailConversation" property="firstMessage" type="net.sourceforge.fenixedu.domain.cms.messaging.IMailMessage"/>
	
	<table width="100%" class="style1">
			<tr>
				<th class="listClasses-header" width="10%"><bean:message bundle="MANAGER_RESOURCES" bundle="CMS_RESOURCES" key="cms.messaging.mailingList.viewThread.mailMessage.senders.label"/></th>		
				<th width="80%" class="listClasses-header"><bean:message bundle="MANAGER_RESOURCES" bundle="CMS_RESOURCES" key="cms.messaging.mailingList.viewMailingList.message.label"/></th>
				<th class="listClasses-header" width="10%"><bean:message bundle="MANAGER_RESOURCES" bundle="CMS_RESOURCES" key="cms.messaging.mailingList.viewMailingList.conversatioCreationDate.label"/></th>
			</tr>
			<tr>			
				<td class="listClasses" valign="top">
					<email:writeSender name="firstMessage" bundle="CMS_RESOURCES"/>
				</td>
				<td class="listClasses" style="text-align:left" valign="top"> 
					<email:write name="firstMessage" filter="true" bundle="CMS_RESOURCES"/>
				</td>						
				<td class="listClasses" valign="top">
					<dt:format pattern="dd/MM/yyyy HH:mm"><bean:write name="firstMessage" property="creationDate.time"/></dt:format>
				</td>						
			</tr>
			
	</table>

	<h4>Respostas</h4>
	<%
		StringBuilder targetUrl = new StringBuilder();
		Collection<Integer> allMessagesToExpand = new ArrayList<Integer>();
		targetUrl.append("/mailingListThreadManagement.do?").append("method=messagesVisibilityManagement");
		targetUrl.append("&mailingListID=").append(mailingList.getIdInternal());
		targetUrl.append("&threadId=").append(mailConversation.getIdInternal());
		Iterator<MailMessage> messagesIterator = mailConversation.getMailMessagesIterator();
		while (messagesIterator.hasNext())
		{
			MailMessage currentMessage = messagesIterator.next();
			targetUrl.append("&expandedMessages=").append(currentMessage.getIdInternal());
			allMessagesToExpand.add(currentMessage.getIdInternal());
			if (!messagesIterator.hasNext())
				targetUrl.append("#").append(currentMessage.getIdInternal());
		}
		
		request.setAttribute("alreadyExpanded",allMessagesToExpand.toArray());
	 %>	
	<%
	replyToTopic = new StringBuilder().append("mailto:").append(mailingList.getAddress());
	replyToTopic.append("@").append(mailingList.getCms().getConfiguration().getMailingListsHostToUse());
	replyToTopic.append("?subject=").append(mailConversation.getSubject());
	 %>
	 <html:link  page="<%=targetUrl.toString()%>" module="/cms">
	 	<bean:message bundle="MANAGER_RESOURCES" bundle="CMS_RESOURCES" key="cms.messaging.mailingList.viewMailingList.expandAll.link"/>
	 </html:link>
	<logic:equal name="mailingList" property="closed" value="false">	 
		<ul>
			<li><html:link  href="<%=replyToTopic.toString()%>"><bean:message bundle="MANAGER_RESOURCES" bundle="CMS_RESOURCES" key="cms.messaging.mailingList.mailMessage.replyToTopic.link"/></html:link></li>
		</ul>
	</logic:equal>	
	<table width="100%" class="style1">
			<tr>
				<th class="listClasses-header" width="10%"><bean:message bundle="MANAGER_RESOURCES" bundle="CMS_RESOURCES" key="cms.messaging.mailingList.viewThread.mailMessage.senders.label"/></th>		
				<th width="80%" class="listClasses-header"><bean:message bundle="MANAGER_RESOURCES" bundle="CMS_RESOURCES" key="cms.messaging.mailingList.viewMailingList.message.label"/></th>
				<th class="listClasses-header" width="10%"><bean:message bundle="MANAGER_RESOURCES" bundle="CMS_RESOURCES" key="cms.messaging.mailingList.viewMailingList.conversatioCreationDate.label"/></th>
			</tr>
		<logic:iterate id="mailMessage" name="mailConversation" property="mailMessagesIterator" type="net.sourceforge.fenixedu.domain.cms.messaging.IMailMessage">
			<logic:notEqual name="firstMessage" property="idInternal" value="<%=mailMessage.getIdInternal().toString()%>">
				<a name="<%=mailMessage.getIdInternal()%>"/>
				<tr>
						<%
						Integer[] expandedMessages = (Integer[]) request.getAttribute("expandedMessages");
						Integer currentMailMessageID = mailMessage.getIdInternal();
						boolean messageAlreadyExpanded = false;
						if (expandedMessages != null)
						{
							Integer[] newExpandedMessages = new Integer[expandedMessages.length+1];
							messageAlreadyExpanded = false;
							for(int i=0; i< expandedMessages.length;i++)
							{
								newExpandedMessages[i] = expandedMessages[i];
								if (currentMailMessageID.equals(expandedMessages[i]))
								{
									messageAlreadyExpanded=true;
									break;
								}
							}
							if (!messageAlreadyExpanded)
							{
								newExpandedMessages[expandedMessages.length]=currentMailMessageID;								
							}
							else // lets remove id from array so the message is collapsed
							{
								newExpandedMessages = new Integer[expandedMessages.length-1];								
								for(int i=0,j=0; i< expandedMessages.length;i++)
								{
									if (!currentMailMessageID.equals(expandedMessages[i]))
									{
										newExpandedMessages[j] = expandedMessages[i];
										j++;
									}
								}															
							}
							expandedMessages = newExpandedMessages;
						}
						else
						{
							expandedMessages = new Integer[]{currentMailMessageID};
						}						
	
						targetUrl = new StringBuilder();
						targetUrl.append("/mailingListThreadManagement.do?").append("method=messagesVisibilityManagement");
						targetUrl.append("&mailingListID=").append(mailingList.getIdInternal());
						targetUrl.append("&threadId=").append(mailConversation.getIdInternal());
						for(int i=0;i<expandedMessages.length;i++)
						{
							targetUrl.append("&expandedMessages=").append(expandedMessages[i]);
						}
						targetUrl.append("#").append(mailMessage.getIdInternal());
						request.setAttribute("alreadyExpanded",messageAlreadyExpanded);
						%>					
				
						<logic:equal name="alreadyExpanded" value="true">
							<td class="listClasses" valign="top">
								<p><html:link  module="/cms" page="<%=targetUrl.toString()%>"><bean:message bundle="MANAGER_RESOURCES" bundle="CMS_RESOURCES" key="cms.messaging.mailingList.viewMailingList.hide.link"/></html:link></p>
								<email:writeSender name="mailMessage" addressSeparator="<br/>"/>
								<p><html:link  module="/cms" page="<%=targetUrl.toString()%>"><bean:message bundle="MANAGER_RESOURCES" bundle="CMS_RESOURCES" key="cms.messaging.mailingList.viewMailingList.hide.link"/></html:link></p>
							</td>														
						</logic:equal>												
						
						<logic:equal name="alreadyExpanded" value="false">
							<td class="listClasses">
								<html:link  module="/cms" page="<%=targetUrl.toString()%>">
									<email:senderDigest name="mailMessage"/>
								</html:link>
							</td>																
						</logic:equal>				
						<logic:equal name="alreadyExpanded" value="true">
							<td class="listClasses" style="text-align:left" valign="top" width="100%"> 
								<html:link  module="/cms" page="<%=targetUrl.toString()%>"><bean:message bundle="MANAGER_RESOURCES" bundle="CMS_RESOURCES" key="cms.messaging.mailingList.viewMailingList.hide.link"/></html:link>						
								<email:write name="mailMessage" filter="true" bundle="CMS_RESOURCES"/>
								<html:link  module="/cms" page="<%=targetUrl.toString()%>"><bean:message bundle="MANAGER_RESOURCES" bundle="CMS_RESOURCES" key="cms.messaging.mailingList.viewMailingList.hide.link"/></html:link>
							</td>														
						</logic:equal>												
						
						<logic:equal name="alreadyExpanded" value="false">
							<td class="listClasses" style="text-align:left" valign="top"> 
								<html:link  module="/cms" page="<%=targetUrl.toString()%>">
									<email:bodyDigest name="mailMessage" filter="true" chars="20" bundle="CMS_RESOURCES"/>
								</html:link>
								<email:writeAttachments name="mailMessage" bundle="CMS_RESOURCES"/>
							</td>														
						</logic:equal>
						<td class="listClasses" valign="top">
							<dt:format pattern="dd/MM/yyyy HH:mm"><bean:write name="mailMessage" property="creationDate.time"/></dt:format>
						</td>																							
				</tr>
			</logic:notEqual>
		</logic:iterate>
		</table>
		<logic:equal name="mailingList" property="closed" value="false">	 
			<ul>
				<li><html:link  href="<%=replyToTopic.toString()%>"><bean:message bundle="MANAGER_RESOURCES" bundle="CMS_RESOURCES" key="cms.messaging.mailingList.mailMessage.replyToTopic.link"/></html:link></li>
			</ul>
		</logic:equal>
	</logic:present>
</logic:present>			