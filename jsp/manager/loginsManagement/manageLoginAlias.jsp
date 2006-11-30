<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="alias.management.title" bundle="MANAGER_RESOURCES"/></h2>

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

	<logic:notEmpty name="login">
		
		<p class="infoop2">
			<b><bean:message key="label.name" bundle="MANAGER_RESOURCES"/>:</b> <bean:write name="login" property="user.person.name"/><br/>
			<b><bean:message key="label.person.username" bundle="MANAGER_RESOURCES"/></b> <bean:write name="login" property="user.person.username"/>
		</p>
	
		<ul class="mvert15 list5">
			<li>	
				<html:link page="/loginsManagement.do?method=prepareSearchPerson">
					<bean:message key="label.return" bundle="MANAGER_RESOURCES"/>
				</html:link>
			</li>
			<li>
				<html:link page="/loginsManagement.do?method=prepareManageLoginTimeIntervals" paramId="personID" paramName="login" paramProperty="user.person.idInternal">
					<bean:message key="link.manage.login.time.intervals" bundle="MANAGER_RESOURCES"/>
				</html:link>			
			</li>
			
		</ul>
	
		<%-- Institutional Alias --%>
		<p>	
			<bean:define id="personInstitutionalAlias" name="login" property="institutionalLoginAlias"></bean:define>
			<logic:notEmpty name="personInstitutionalAlias">			
				<p><b><bean:message key="label.institutional.alias" bundle="MANAGER_RESOURCES"/></b></p>
				<fr:view name="personInstitutionalAlias" schema="ViewInstitutionalLoginAlias" >
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle4 thlight tdcenter mtop05"/>
						<fr:property name="columnClasses" value="aleft,,,,"/>   		
					</fr:layout>			
				</fr:view>				
			</logic:notEmpty>			
		</p>
		
		<%-- RoleType Alias --%>
		<p>	
			<p><b><bean:message key="label.role.type.alias" bundle="MANAGER_RESOURCES"/></b></p>
		
			<ul class="mvert15 list5">
				<li>	
					<html:link page="/loginsManagement.do?method=prepareCreateRoleTypeAlias" paramId="loginID" paramName="login" paramProperty="idInternal">
						<bean:message key="label.create.new.alias" bundle="MANAGER_RESOURCES"/>
					</html:link>
				</li>
			</ul>
		
			<bean:define id="personRoleTypeAlias" name="login" property="allRoleLoginAlias"></bean:define>
			<logic:notEmpty name="personRoleTypeAlias">						
				<fr:view name="personRoleTypeAlias" schema="ViewRoleTypeLoginAlias" >
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle4 thlight tdcenter mtop05"/>
						<fr:property name="columnClasses" value="aleft,,,,"/>   		
						
						<fr:property name="link(delete)" value="/loginsManagement.do?method=deleteAlias"/>
			            <fr:property name="param(delete)" value="idInternal/loginAliasID"/>
				        <fr:property name="key(delete)" value="link.delete"/>
			            <fr:property name="bundle(delete)" value="MANAGER_RESOURCES"/>
			            <fr:property name="order(delete)" value="0"/>		     
			          					
					</fr:layout>			
				</fr:view>				
			</logic:notEmpty>			
		</p>
		
		<%-- Custom Alias --%>
		<p>	
			<p><b><bean:message key="label.custom.alias" bundle="MANAGER_RESOURCES"/></b></p>
		
			<ul class="mvert15 list5">
				<li>	
					<html:link page="/loginsManagement.do?method=prepareCreateCustomAlias" paramId="loginID" paramName="login" paramProperty="idInternal">
						<bean:message key="label.create.new.alias" bundle="MANAGER_RESOURCES"/>
					</html:link>
				</li>
			</ul>
		
			<bean:define id="personCustomAlias" name="login" property="allCustomLoginAlias"></bean:define>
			<logic:notEmpty name="personCustomAlias">						
				<fr:view name="personCustomAlias" schema="ViewInstitutionalLoginAlias" >
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle4 thlight tdcenter mtop05"/>
						<fr:property name="columnClasses" value="aleft,,,,"/>   		
											
						<fr:property name="link(edit)" value="/loginsManagement.do?method=prepareEditAlias"/>
			            <fr:property name="param(edit)" value="idInternal/loginAliasID"/>
				        <fr:property name="key(edit)" value="link.edit"/>
			            <fr:property name="bundle(edit)" value="MANAGER_RESOURCES"/>
			            <fr:property name="order(edit)" value="0"/>	
			            
						<fr:property name="link(delete)" value="/loginsManagement.do?method=deleteAlias"/>
			            <fr:property name="param(delete)" value="idInternal/loginAliasID"/>
				        <fr:property name="key(delete)" value="link.delete"/>
			            <fr:property name="bundle(delete)" value="MANAGER_RESOURCES"/>
			            <fr:property name="order(delete)" value="1"/>		     
			          					
					</fr:layout>			
				</fr:view>				
			</logic:notEmpty>			
		</p>
		
	</logic:notEmpty>
</logic:present>
