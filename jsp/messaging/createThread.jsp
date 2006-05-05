<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%> 

<logic:present name="forum">

	<fr:view name="forum" layout="tabular" schema="messaging.viewForuns.forum">

	</fr:view>
	
	<bean:define id="forumId" name="forum" property="idInternal"/>
	
	<fr:create type="net.sourceforge.fenixedu.domain.messaging.ConversationMessage" layout="tabular"
           schema="conversationMessage.create"
           action="<%="/forunsManagement.do?method=createThreadAndMessage&forumId="+forumId %>">

           <fr:hidden slot="forum" name="forum"/>
	</fr:create>


</logic:present>