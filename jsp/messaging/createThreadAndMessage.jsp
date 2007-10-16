<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%> 

<logic:present name="forum">
	<bean:define id="contextPrefix" name="contextPrefix" />

	<em><bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.executionCourse.forum" />&nbsp;<bean:write name="forum" property="name"/></em>
	<h2><bean:message bundle="MESSAGING_RESOURCES" key="label.createThreadAndMessage.title"/></h2>
	
	<fr:view name="forum" layout="tabular" schema="forum.view-full">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight thright"/>
		</fr:layout>
	</fr:view>
	
	<logic:messagesPresent message="true">
		<p>
			<html:messages id="messages" message="true" bundle="MESSAGING_RESOURCES">
				<span class="error"><bean:write name="messages" /></span>
			</html:messages>
		</p>
	</logic:messagesPresent>
	
	<bean:define id="forumId" name="forum" property="idInternal"/>
	
	<fr:create id="createThreadAndMessage"
			type="net.sourceforge.fenixedu.dataTransferObject.messaging.CreateConversationThreadAndMessageBean" 
           	schema="conversationThreadAndMessage.create"
           	action="<%= contextPrefix + "method=createThreadAndMessage&amp;forumId="+forumId+"&amp;goToLastPage=true" %>">

			<fr:layout name="tabular">
				<fr:property name="classes" value="thlight mtop05 tstyle5"/>
				<fr:property name="columnClasses" value=",,tdclear tderror1"/>
			</fr:layout>           	

           <fr:hidden slot="creator" name="person"/>
           <fr:hidden slot="forum" name="forum"/>
           <fr:destination name="cancel" path="<%= contextPrefix.toString().replace("&amp;", "&") + "method=viewForum&forumId="+forumId%>"/>
           <fr:destination name="exception" path="<%= contextPrefix.toString().replace("&amp;", "&") + "method=viewForum&forumId="+forumId%>"/>
	</fr:create>


</logic:present>