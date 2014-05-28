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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<h2><bean:message key="label.create.new.login.period" bundle="MANAGER_RESOURCES"/></h2>

<logic:present role="(role(MANAGER) | role(OPERATOR))">

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
		<b><bean:message key="label.name" bundle="MANAGER_RESOURCES"/>:</b> <bean:write name="user" property="person.name"/><br/>
		<b><bean:message key="label.person.username" bundle="MANAGER_RESOURCES"/></b> <bean:write name="user" property="username"/>
	</p>
	
	<bean:define id="managePeriodsURL" type="java.lang.String">/loginsManagement.do?method=prepareManageLoginTimeIntervals&personID=<bean:write name="user" property="person.externalId"/></bean:define>								
	<fr:create id="createNewLoginPeriodID" type="org.fenixedu.bennu.user.management.UserLoginPeriod" schema="CreateUserLoginPeriod" action="<%= managePeriodsURL %>">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1"/>
		    <fr:property name="columnClasses" value=",,noborder"/>
		</fr:layout>
		<fr:hidden slot="user" name="user"/>
		<fr:destination name="cancel" path="<%= managePeriodsURL %>"/>	
	</fr:create>
	
</logic:present>