<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<h2><bean:message key="label.edit.login.period" bundle="MANAGER_RESOURCES"/></h2>

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
		
	<logic:notEmpty name="loginPeriod">
		<p class="infoop2">
			<b><bean:message key="label.name" bundle="MANAGER_RESOURCES"/>:</b> <bean:write name="loginPeriod" property="login.user.person.name"/><br/>
			<b><bean:message key="label.person.username" bundle="MANAGER_RESOURCES"/></b> <bean:write name="loginPeriod" property="login.user.person.username"/>
		</p>
		
		<bean:define id="managePeriodsURL" type="java.lang.String">/loginsManagement.do?method=prepareManageLoginTimeIntervals&personID=<bean:write name="loginPeriod" property="login.user.person.externalId"/></bean:define>								
		<fr:edit id="editLoginPeridoID" name="loginPeriod" schema="EditLoginPeriod" action="<%= managePeriodsURL %>">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1"/>
			    <fr:property name="columnClasses" value=",,noborder"/>
			</fr:layout>
			<fr:destination name="cancel" path="<%= managePeriodsURL %>"/>	
		</fr:edit>	
					
	</logic:notEmpty>		
</logic:present>	