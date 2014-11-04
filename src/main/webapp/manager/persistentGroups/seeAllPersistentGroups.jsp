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

<h2><bean:message key="label.access.control.persistent.groups.management" bundle="MANAGER_RESOURCES"/></h2>

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

	<p class="mtop15 mbottom15">
		<html:link page="/accessControlPersistentGroupsManagement.do?method=prepareCreateNewGroup">
			<bean:message key="label.create.new.persistent.group" bundle="MANAGER_RESOURCES"/>			
		</html:link>
	</p>
	
	<logic:notEmpty name="persistentGroups">
		
		<fr:view schema="ViewPersistentGroup" name="persistentGroups" >
			<fr:layout name="tabular">      			
	   			<fr:property name="classes" value="tstyle4 thlight tdcenter mtop05"/>
	   			<fr:property name="columnClasses" value="aleft,,,,"/>
	   			
	   			<fr:property name="link(edit)" value="<%= "/accessControlPersistentGroupsManagement.do?method=prepareEditPersistentGroup" %>"/>
	            <fr:property name="param(edit)" value="externalId/persistentGroupID"/>
		        <fr:property name="key(edit)" value="link.edit"/>
	            <fr:property name="bundle(edit)" value="MANAGER_RESOURCES"/>
	            <fr:property name="order(edit)" value="0"/>
	            
	            <fr:property name="link(delete)" value="<%= "/accessControlPersistentGroupsManagement.do?method=deletePersistentGroup" %>"/>
	            <fr:property name="param(delete)" value="externalId/persistentGroupID"/>
		        <fr:property name="key(delete)" value="link.delete"/>
	            <fr:property name="bundle(delete)" value="MANAGER_RESOURCES"/>
	            <fr:property name="order(delete)" value="0"/>                                           	           
	    	</fr:layout>
		</fr:view>
	
	</logic:notEmpty>
	
</logic:present>