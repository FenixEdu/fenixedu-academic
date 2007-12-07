<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/collectionPager.tld" prefix="cp" %>
<html:xhtml/>

<html:messages id="message" message="true" bundle="MESSAGING_RESOURCES">
	<span class="error0">
		<bean:write name="message"/>
	</span>
</html:messages>

<logic:present name="forum">
	<bean:define id="forumId" name="forum" property="idInternal" />	
	<bean:define id="prefix" name="contextPrefix" type="java.lang.String"/>
	<bean:define id="contextPrefix" value="<%= prefix + (prefix.contains("?") ? "&amp;" : "?") %>" type="java.lang.String"/>
	<bean:define id="module" name="module"/>
	
	<h2><bean:message bundle="MESSAGING_RESOURCES" key="label.viewForum.title"/></h2>
	
	<!-- Forum Details -->
	<fr:view name="forum" schema="forum.view-full">
		<fr:layout name="tabular">
	        <fr:property name="classes" value="tstyle5 thlight thright"/>
	    </fr:layout>
	</fr:view>

	<logic:equal name="receivingMessagesByEmail" value="true">
		<span style="color: #888;">
			<bean:message bundle="MESSAGING_RESOURCES" key="label.viewForum.receivingMessagesByEmail"/>
			<html:link action="<%= contextPrefix + "method=emailUnsubscribe"%>" paramId="forumId" paramName="forumId"><bean:message bundle="MESSAGING_RESOURCES" key="link.viewForum.quitSubscription"/></html:link>
		</span>
	</logic:equal>
	<logic:equal name="receivingMessagesByEmail" value="false">
		<span style="color: #888;">
			<bean:message bundle="MESSAGING_RESOURCES" key="label.viewForum.notReceivingMessagesByEmail"/>
			<html:link action="<%= contextPrefix + "method=emailSubscribe"%>" paramId="forumId" paramName="forumId"><bean:message bundle="MESSAGING_RESOURCES" key="link.viewForum.subscribe"/></html:link>
		</span>
	</logic:equal>


	<h3 class="mtop2"><bean:message bundle="MESSAGING_RESOURCES" key="label.viewForum.threads"/></h3>
	
	<!-- Conversation Threads -->
	<logic:equal name="loggedPersonCanWrite" value="true">
		<p>
			<span class="gen-button">
			<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
			<html:link action="<%= contextPrefix + "method=prepareCreateThreadAndMessage"%>" paramId="forumId" paramName="forum" paramProperty="idInternal" styleClass="gen-button">
				<bean:message bundle="MESSAGING_RESOURCES" key="link.viewForum.createThread"/>
			</html:link>
			</span>
		</p>
	</logic:equal>

	<logic:empty name="conversationThreads">
		<p class="mvert15">
			<em class="error0"><bean:message bundle="MESSAGING_RESOURCES" key="label.viewForum.noThreads"/></em>
		</p>
	</logic:empty>
	
	<logic:notEmpty name="conversationThreads">
		<bean:message bundle="MESSAGING_RESOURCES" key="label.viewForum.page"/>
		
		<cp:collectionPages url="<%= module + contextPrefix + "method=viewForum&amp;forumId=" + forumId %>" 
			pageNumberAttributeName="pageNumber" numberOfPagesAttributeName="pageNumbers" numberOfVisualizedPages="10"/>
		
		<fr:view name="conversationThreads" schema="conversationThread.view-full">
			<fr:layout name="tabular">
		        <fr:property name="classes" value="tstyle2"/>
		        <fr:property name="columnClasses" value=",,,acenter"/>
       			<fr:property name="link(view)" value="<%= contextPrefix + "method=viewThread"%>"/>
				<fr:property name="param(view)" value="forum.idInternal/forumId,idInternal/threadId"/>
				<fr:property name="key(view)" value="label.viewForum.viewThread"/>
				<fr:property name="bundle(view)" value="MESSAGING_RESOURCES"/>
		    </fr:layout>
		    
		    <fr:destination name="viewForum" path="<%= contextPrefix + "method=viewThread&amp;forumId=${forum.idInternal}&amp;threadId=${idInternal}" %>"/>
		</fr:view>
	</logic:notEmpty>
			
</logic:present>