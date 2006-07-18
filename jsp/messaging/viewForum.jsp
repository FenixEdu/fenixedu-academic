<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:messages id="message" message="true" bundle="MESSAGING_RESOURCES">
	<span class="error">
		<bean:write name="message"/>
	</span>
</html:messages>

<logic:present name="forum">
	<bean:define id="forumId" name="forum" property="idInternal" />	
	<bean:define id="contextPrefix" name="contextPrefix" />

	<h2><bean:message bundle="MESSAGING_RESOURCES" key="label.viewForum.title"/></h2>
	
	<!-- Forum Details -->
	<fr:view name="forum" schema="forum.view-full">
		<fr:layout name="tabular">
	        <fr:property name="classes" value="tstyle5 thlight thright"/>
	    </fr:layout>
	</fr:view>

	<logic:equal name="receivingMessagesByEmail" value="true">
		<bean:message bundle="MESSAGING_RESOURCES" key="label.viewForum.receivingMessagesByEmail"/>
		<html:link action="<%= contextPrefix + "method=emailUnsubscribe"%>" paramId="forumId" paramName="forumId"><bean:message bundle="MESSAGING_RESOURCES" key="link.viewForum.quitSubscription"/></html:link>
	</logic:equal>
	<logic:equal name="receivingMessagesByEmail" value="false">
		<bean:message bundle="MESSAGING_RESOURCES" key="label.viewForum.notReceivingMessagesByEmail"/>
		<html:link action="<%= contextPrefix + "method=emailSubscribe"%>" paramId="forumId" paramName="forumId"><bean:message bundle="MESSAGING_RESOURCES" key="link.viewForum.subscribe"/></html:link>
	</logic:equal>


	<h3><bean:message bundle="MESSAGING_RESOURCES" key="label.viewForum.threads"/></h3>
	
	<!-- Conversation Threads -->
	<logic:equal name="loggedPersonCanWrite" value="true">
		<html:link action="<%= contextPrefix + "method=prepareCreateThreadAndMessage"%>" paramId="forumId" paramName="forum" paramProperty="idInternal">
			<bean:message bundle="MESSAGING_RESOURCES" key="link.viewForum.createThread"/>
		</html:link>
		<br/><br/>
	</logic:equal>

	<logic:empty name="conversationThreads">
		<span class="error"><bean:message bundle="MESSAGING_RESOURCES" key="label.viewForum.noThreads"/></span>
	</logic:empty>
	<logic:notEmpty name="conversationThreads">
		<bean:message bundle="MESSAGING_RESOURCES" key="label.viewForum.page"/>
		<bean:define id="currentPageNumberString"><bean:write name="currentPageNumber"/></bean:define>
		
		<logic:iterate id="pageNumber" name="pageNumbers" type="java.lang.Integer">
			<logic:equal name="currentPageNumberString" value="<%=pageNumber.toString()%>">
				<bean:write name="pageNumber"/>
			</logic:equal>
			<logic:notEqual name="currentPageNumber" value="<%=pageNumber.toString()%>">
				<html:link action="<%= contextPrefix + "method=viewForum&forumId=" + forumId.toString() + "&pageNumber=" + pageNumber%>">
					<bean:write name="pageNumber"/>
				</html:link>			
			</logic:notEqual>
		</logic:iterate>


	
		<fr:view name="conversationThreads" schema="conversationThread.view-full">
			<fr:layout name="tabular">
		        <fr:property name="classes" value="tstyle2"/>
		        <fr:property name="columnClasses" value=",,,acenter"/>
       			<fr:property name="link(view)" value="<%= contextPrefix + "method=viewThread"%>"/>
				<fr:property name="param(view)" value="forum.idInternal/forumId,idInternal/threadId"/>
				<fr:property name="key(view)" value="label.viewForum.viewThread"/>
				<fr:property name="bundle(view)" value="MESSAGING_RESOURCES"/>
		    </fr:layout>
		</fr:view>


<%--
		<bean:message bundle="MESSAGING_RESOURCES" key="label.viewForum.page"/>
		<logic:iterate id="pageNumber" name="pageNumbers" type="java.lang.Integer">
			<logic:equal name="currentPageNumberString" value="<%=pageNumber.toString()%>">
				<bean:write name="pageNumber"/>
			</logic:equal>
			<logic:notEqual name="currentPageNumber" value="<%=pageNumber.toString()%>">
				<bean:define id="forumId" name="forum" property="idInternal" />
				<html:link action="<%= contextPrefix + "method=viewForum&forumId=" + forumId.toString() + "&pageNumber=" + pageNumber%>">
					<bean:write name="pageNumber"/>
				</html:link>			
			</logic:notEqual>	
		</logic:iterate>
--%>

	</logic:notEmpty>


<%--
	<p>Pretende anular a subscri��o deste f�rum? <a href="">Anular Subscri��o</a></p>
	
	<logic:equal name="receivingMessagesByEmail" value="true">
		<strong><bean:message bundle="MESSAGING_RESOURCES" key="label.viewForum.receivingMessagesByEmail"/></strong>
		<html:link action="<%= contextPrefix + "method=emailUnsubscribe"%>" paramId="forumId" paramName="forumId"><bean:message bundle="MESSAGING_RESOURCES" key="link.viewForum.here"/></html:link>
	</logic:equal>
	<logic:equal name="receivingMessagesByEmail" value="false">
		<strong><bean:message bundle="MESSAGING_RESOURCES" key="label.viewForum.notReceivingMessagesByEmail"/></strong>
		<html:link action="<%= contextPrefix + "method=emailSubscribe"%>" paramId="forumId" paramName="forumId"><bean:message bundle="MESSAGING_RESOURCES" key="link.viewForum.here"/></html:link>
	</logic:equal>
--%>
			
</logic:present>