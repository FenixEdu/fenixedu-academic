<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<h2><bean:message key="logins.time.intervals.management.title" bundle="MANAGER_RESOURCES"/></h2>

<logic:present role="MANAGER,OPERATOR">

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
				<html:link page="/loginsManagement.do?method=prepareManageAlias" paramId="personID" paramName="login" paramProperty="user.person.externalId">
					<bean:message key="link.manage.login.alias" bundle="MANAGER_RESOURCES"/>
				</html:link>			
			</li>
		</ul>
		
		<p><html:link page="/loginsManagement.do?method=prepareCreateLoginTimeInterval" paramId="loginID" paramName="login" paramProperty="externalId">
			<bean:message key="label.create.new.login.period" bundle="MANAGER_RESOURCES"/>
		</html:link></p>
		
		<p>	
			<bean:define id="loginPeriods" name="login" property="loginPeriodsWithoutInvitationPeriods" />
			<logic:notEmpty name="loginPeriods">							
				<fr:view name="loginPeriods" schema="ViewLoginPeriod" >
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle4 thlight tdcenter mtop05"/>
						<fr:property name="columnClasses" value="aleft,,,,"/>   		
						
						<fr:property name="link(edit)" value="/loginsManagement.do?method=prepareEditLoginTimeInterval"/>
			            <fr:property name="param(edit)" value="externalId/loginPeriodID"/>
				        <fr:property name="key(edit)" value="link.edit"/>
			            <fr:property name="bundle(edit)" value="MANAGER_RESOURCES"/>
			            <fr:property name="order(edit)" value="0"/>	
			            
						<fr:property name="link(delete)" value="/loginsManagement.do?method=deleteLoginTimeInterval"/>
			            <fr:property name="param(delete)" value="externalId/loginPeriodID"/>
				        <fr:property name="key(delete)" value="link.delete"/>
			            <fr:property name="bundle(delete)" value="MANAGER_RESOURCES"/>
			            <fr:property name="order(delete)" value="1"/>	
						
					</fr:layout>			
				</fr:view>				
			</logic:notEmpty>			
		</p>	
	
	</logic:notEmpty>
</logic:present>
