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

<h2><bean:message key="logins.time.intervals.management.title" bundle="MANAGER_RESOURCES"/></h2>

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

	<logic:notEmpty name="person">
	
		<p class="infoop2">
			<b><bean:message key="label.name" bundle="MANAGER_RESOURCES"/>:</b> <bean:write name="person" property="name"/><br/>
			<b><bean:message key="label.person.username" bundle="MANAGER_RESOURCES"/></b> <bean:write name="person" property="user.username"/>
		</p>
	
		<ul class="mvert15 list5">
			<li>	
				<html:link page="/loginsManagement.do?method=prepareSearchPerson">
					<bean:message key="label.return" bundle="MANAGER_RESOURCES"/>
				</html:link>
			</li>
		</ul>
		
		<p><html:link page="/loginsManagement.do?method=prepareCreateLoginTimeInterval" paramId="userID" paramName="person" paramProperty="user.externalId">
			<bean:message key="label.create.new.login.period" bundle="MANAGER_RESOURCES"/>
		</html:link></p>
		
		<p>	
			<bean:define id="loginPeriods" name="person" property="user.loginPeriod" />
			<logic:notEmpty name="loginPeriods">							
				<fr:view name="loginPeriods">
					<fr:schema type="org.fenixedu.bennu.user.management.UserLoginPeriod" bundle="MANAGER_RESOURCES">
						<fr:slot name="beginDate" />
						<fr:slot name="endDate" />
					</fr:schema>
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle4 thlight tdcenter mtop05"/>
						<fr:property name="columnClasses" value="aleft,,,,"/>   		
						
						<fr:property name="link(edit)" value="/loginsManagement.do?method=prepareEditLoginTimeInterval"/>
			            <fr:property name="param(edit)" value="externalId/periodID"/>
				        <fr:property name="key(edit)" value="link.edit"/>
			            <fr:property name="bundle(edit)" value="MANAGER_RESOURCES"/>
			            <fr:property name="visibleIfNot(edit)" value="closed" />
			            <fr:property name="order(edit)" value="0"/>
			            
						<fr:property name="link(delete)" value="/loginsManagement.do?method=deleteLoginTimeInterval"/>
			            <fr:property name="param(delete)" value="externalId/periodID"/>
				        <fr:property name="key(delete)" value="link.delete"/>
			            <fr:property name="bundle(delete)" value="MANAGER_RESOURCES"/>
			            <fr:property name="visibleIfNot(delete)" value="started" />
			            <fr:property name="order(delete)" value="1"/>	
						
					</fr:layout>			
				</fr:view>				
			</logic:notEmpty>			
		</p>	
	
	</logic:notEmpty>
</logic:present>
