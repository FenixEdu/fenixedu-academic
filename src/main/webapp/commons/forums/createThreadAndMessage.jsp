<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%> 
<html:xhtml/>

<logic:present name="forum">
	<bean:define id="prefix" name="contextPrefix" type="java.lang.String"/>
	<bean:define id="contextPrefix" value="<%= prefix + (prefix.contains("?") ? "&amp;" : "?") %>" type="java.lang.String"/>
		
	<h2><bean:message bundle="MESSAGING_RESOURCES" key="label.createThreadAndMessage.title"/></h2>
	
	<fr:view name="forum" layout="tabular" schema="forum.view-full">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thlight thright"/>
		</fr:layout>
	</fr:view>
	
	<logic:messagesPresent message="true">
		<html:messages id="messages" message="true" bundle="MESSAGING_RESOURCES">
			<span class="error"><bean:write name="messages" /></span>
		</html:messages>
		<br/><br/>
	</logic:messagesPresent>
	
	<bean:define id="forumId" name="forum" property="externalId"/>
	
	<fr:create id="createThreadAndMessage"
			type="net.sourceforge.fenixedu.dataTransferObject.messaging.CreateConversationThreadAndMessageBean" 
           	schema="conversationThreadAndMessage.create"
           	action="<%= contextPrefix + "method=createThreadAndMessage&amp;forumId="+forumId %>">

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