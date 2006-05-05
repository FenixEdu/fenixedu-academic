<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<logic:present name="forum">

	<fr:view name="forum" layout="tabular" schema="messaging.viewForuns.forum">

	</fr:view>

asas<br>
	<%
	java.util.HashMap parameters = new java.util.HashMap();
	parameters.put("method","prepareCreateThreadAndMessage");
	parameters.put("forumId",request.getParameter("forumId"));
	request.setAttribute("parameters",parameters);
	%>
	
	<html:link action="/forunsManagement" name="parameters">
		papada doce
	</html:link>
	
bla<br>
	getConversationThreads:
	
	<fr:view name="forum" property="conversationThreads" layout="tabular" schema="messaging.viewForuns.forum">

	</fr:view>	


	
	
	
</logic:present>