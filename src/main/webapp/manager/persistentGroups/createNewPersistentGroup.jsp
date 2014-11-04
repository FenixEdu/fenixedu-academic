<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<html:xhtml/>

<h2><bean:message key="label.create.new.persistent.group" bundle="MANAGER_RESOURCES"/></h2>

<logic:present role="role(MANAGER)">

	<logic:messagesPresent message="true">
		<p>
			<span class="error0"><!-- Error messages go here -->
				<html:messages id="message" message="true" bundle="MANAGER_RESOURCES">
					<bean:write name="message"/>
				</html:messages>
			</span>
		<p>
	</logic:messagesPresent>	
		
	<logic:empty name="persistentGroup">		
		<fr:create type="org.fenixedu.academic.domain.accessControl.PersistentGroupMembers"
			 action="/accessControlPersistentGroupsManagement.do?method=listAllGroups" schema="ViewPersistentGroup">
			<fr:layout name="tabular">      			
		   		<fr:property name="classes" value="tstyle4 thlight mtop05"/>
		   		<fr:property name="columnClasses" value="aleft,,,,"/>
		   	</fr:layout>			
		</fr:create>
	</logic:empty>
	
	<logic:notEmpty name="persistentGroup">
	
		<ul class="mvert15 mtop15 mbottom15">
			<li>
				<html:link page="/accessControlPersistentGroupsManagement.do?method=listAllGroups">		
					<bean:message bundle="MANAGER_RESOURCES" key="label.return"/>
				</html:link>
			</li>		
		</ul>
		
		<logic:notEmpty name="persistentGroup" property="persons">	
			<bean:define id="persistentGroup" name="persistentGroup" type="org.fenixedu.academic.domain.accessControl.PersistentGroupMembers"/>
			
			<p><strong><bean:message key="label.persistent.group.members" bundle="MANAGER_RESOURCES"/></strong></p>	
			<fr:view name="persistentGroup" property="persons" schema="PersonInfoWithUsername">
				<fr:layout name="tabular">      			
			   		<fr:property name="classes" value="tstyle4 thlight mtop05"/>
			   		<fr:property name="columnClasses" value="aleft,,,,"/>
			   		
			   		<fr:property name="link(delete)" value="<%= "/accessControlPersistentGroupsManagement.do?method=removePersistentGroupMember&persistentGroupID=" + persistentGroup.getExternalId() %>"/>
		            <fr:property name="param(delete)" value="externalId/personID"/>
			        <fr:property name="key(delete)" value="link.remove"/>
		            <fr:property name="bundle(delete)" value="MANAGER_RESOURCES"/>
		            <fr:property name="order(delete)" value="0"/>  
			   	</fr:layout>
			</fr:view>		
		</logic:notEmpty>
		
		<bean:define id="prepareEditURL">/accessControlPersistentGroupsManagement.do?method=prepareEditPersistentGroup&persistentGroupID=<bean:write name="persistentGroup" property="externalId"/></bean:define>
		
		<p><strong><bean:message key="label.edit.persistent.group" bundle="MANAGER_RESOURCES"/></strong></p>			
		<fr:form action="<%= prepareEditURL %>">
			<fr:edit id="editPersistentGroup" name="persistentGroup" schema="EditPersistentGroup">
				<fr:layout name="tabular">      			
			   		<fr:property name="classes" value="tstyle4 thlight mtop05"/>
			   		<fr:property name="columnClasses" value="aleft,,,,"/>
			   	</fr:layout>
			</fr:edit>
			<html:submit><bean:message key="label.manager.save.modifications" bundle="MANAGER_RESOURCES"/></html:submit>
		</fr:form>	
		
		<p class="mtop25"><strong><bean:message key="label.add.member.to.persistent.group" bundle="MANAGER_RESOURCES"/></strong></p>		
		<fr:form action="<%= prepareEditURL %>">
			<fr:edit id="addNewMemberToPersistentGroup" name="persistentGroup" schema="AddNewMemberToPersistentGroup">				
				<fr:layout name="tabular">      			
			   		<fr:property name="classes" value="tstyle4 thlight mtop05"/>
			   		<fr:property name="columnClasses" value="aleft,,,,"/>
			   	</fr:layout>
			</fr:edit>
			<html:submit><bean:message key="button.add" bundle="MANAGER_RESOURCES"/></html:submit>
		</fr:form>					
	</logic:notEmpty>
		
</logic:present>	