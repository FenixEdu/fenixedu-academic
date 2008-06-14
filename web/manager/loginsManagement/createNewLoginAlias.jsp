<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="label.create.new.alias" bundle="MANAGER_RESOURCES"/></h2>

<logic:present role="MANAGER">

	<logic:messagesPresent message="true">
		<p>
		<span class="error0"><!-- Error messages go here -->
			<html:messages id="message" message="true" bundle="MANAGER_RESOURCES">
				<bean:write name="message"/>
			</html:messages>
		</span>
		<p>
	</logic:messagesPresent>	

	<logic:notEmpty name="loginAliasBean">
		<bean:define id="createNewAliasURL" value="/loginsManagement.do?method=createNewLoginAlias"/>		
		<bean:define id="manageAliasURL" type="java.lang.String">/loginsManagement.do?method=prepareManageAlias&personID=<bean:write name="loginAliasBean" property="login.user.person.idInternal"/></bean:define>		
		
		<p class="infoop2">
			<b><bean:message key="label.name" bundle="MANAGER_RESOURCES"/>:</b> <bean:write name="loginAliasBean" property="login.user.person.name"/><br/>
			<b><bean:message key="label.person.username" bundle="MANAGER_RESOURCES"/></b> <bean:write name="loginAliasBean" property="login.user.person.username"/>
		</p>
		
		<logic:equal value="ROLE_TYPE_ALIAS" name="aliasType">			
			<p><b><bean:message key="label.create.new.role.type.alias" bundle="MANAGER_RESOURCES"/></b></p>	
			<p>
				<fr:edit name="loginAliasBean" schema="CreateNewRoleTypeAlias" id="newRoleTypeAliasBeanID" action="<%= createNewAliasURL %>">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle1"/>
				        <fr:property name="columnClasses" value=",,noborder"/>
					</fr:layout>	
					<fr:destination name="cancel" path="<%= manageAliasURL %>"/>				
				</fr:edit> 	
			</p>
		</logic:equal>
		
		<logic:equal value="CUSTOM_ALIAS" name="aliasType">
			<p><b><bean:message key="label.create.new.custom.alias" bundle="MANAGER_RESOURCES"/></b></p>	
			<p>
				<fr:edit name="loginAliasBean" schema="CreateNewCustomAlias" id="newCustomAliasBeanID" action="<%= createNewAliasURL %>">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle1"/>
				        <fr:property name="columnClasses" value=",,noborder"/>
					</fr:layout>
					<fr:destination name="cancel" path="<%= manageAliasURL %>"/>						
				</fr:edit> 	
			</p>
		</logic:equal>		
		
	</logic:notEmpty>
</logic:present>