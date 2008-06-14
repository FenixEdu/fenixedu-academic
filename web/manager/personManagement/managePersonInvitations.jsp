<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="invitations.management.title" bundle="MANAGER_RESOURCES"/></h2>

<logic:present role="MANAGER">

	<logic:messagesPresent message="true">
		<p>
			<span class="error0"><!-- Error messages go here -->
				<html:messages id="message" message="true" bundle="MANAGER_RESOURCES">
					<bean:write name="message"/>
				</html:messages>
			</span>
		</p>
	</logic:messagesPresent>

	<p class="infoop2">
		<b><bean:message key="label.name" bundle="MANAGER_RESOURCES"/>:</b> <bean:write name="person" property="name"/><br/>
		<b><bean:message key="label.person.username" bundle="MANAGER_RESOURCES"/></b> <bean:write name="person" property="username"/>
	</p>

	<bean:define id="prepareCreateNewInvitationURL" type="java.lang.String">/invitationsManagement.do?method=prepareCreateNewPersonInvitation&personID=<bean:write name="person" property="idInternal"/></bean:define> 		
	<p><html:link page="<%= prepareCreateNewInvitationURL %>"><bean:message key="label.create.new.invitation" bundle="MANAGER_RESOURCES"/></html:link></p>		
	
	<bean:define id="personID" name="person" property="idInternal" />
	<logic:notEmpty name="person" property="invitationsOrderByDate">
		<fr:view name="person" property="invitationsOrderByDate" schema="ViewPersonInvitations" >
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4"/>
				<fr:property name="rowClasses" value="listClasses"/>					
															
	   			<fr:property name="link(edit)" value="/invitationsManagement.do?method=prepareEditPersonInvitation"/>
	            <fr:property name="param(edit)" value="idInternal/invitationID"/>
		        <fr:property name="key(edit)" value="link.edit.invitation"/>
	            <fr:property name="bundle(edit)" value="MANAGER_RESOURCES"/>
	            <fr:property name="order(edit)" value="0"/>		
	            
	            <fr:property name="link(delete)" value="<%= "/invitationsManagement.do?method=deletePersonInvitation&personID=" + personID  %>"/>
	            <fr:property name="param(delete)" value="idInternal/invitationID"/>
		        <fr:property name="key(delete)" value="label.delete"/>
	            <fr:property name="bundle(delete)" value="MANAGER_RESOURCES"/>
	            <fr:property name="order(delete)" value="1"/>		         					
			</fr:layout>			
		</fr:view>		
	</logic:notEmpty>

</logic:present>