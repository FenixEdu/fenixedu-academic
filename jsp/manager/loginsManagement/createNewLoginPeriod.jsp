<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="label.create.new.login.period" bundle="MANAGER_RESOURCES"/></h2>

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
			
	<p class="infoop2">
		<b><bean:message key="label.name" bundle="MANAGER_RESOURCES"/>:</b> <bean:write name="login" property="user.person.name"/><br/>
		<b><bean:message key="label.person.username" bundle="MANAGER_RESOURCES"/></b> <bean:write name="login" property="user.person.username"/>
	</p>
	
	<bean:define id="managePeriodsURL" type="java.lang.String">/loginsManagement.do?method=prepareManageLoginTimeIntervals&personID=<bean:write name="login" property="user.person.idInternal"/></bean:define>								
	<fr:create id="createNewLoginPeriodID" type="net.sourceforge.fenixedu.domain.LoginPeriod" schema="CreateLoginPeriod" action="<%= managePeriodsURL %>">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1"/>
		    <fr:property name="columnClasses" value=",,noborder"/>
		</fr:layout>
		<fr:hidden slot="login" name="login"/>
		<fr:destination name="cancel" path="<%= managePeriodsURL %>"/>	
	</fr:create>				
	
</logic:present>	