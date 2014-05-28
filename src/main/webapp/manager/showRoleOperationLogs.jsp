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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/collection-pager" prefix="cp"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<h2><bean:message bundle="MANAGER_RESOURCES" key="title.roleoperationlog"/></h2>
<br />
<span class="error"><!-- Error messages go here --><html:errors /></span>
<html:messages id="message" message="true" bundle="MANAGER_RESOURCES">
	<span class="error"><!-- Error messages go here -->
		<bean:write name="message"/>
	</span>
</html:messages>


<logic:notEmpty name="domainObjectActionLogs">
	<bean:define id ="URL" value="<%="/manager/manageRoles.do?method=showRoleOperationLogs&username=" + request.getParameter("username")%>"/>
	<logic:present name="roleID">
		<bean:define id ="URL" value="<%="/manager/viewPersonsWithRole.do?method=showRoleOperationLogs&roleID=" + request.getParameter("roleID")%>"/>
	</logic:present>
	
	
	<div class="mtop15">
		<logic:notEqual name="numberOfPages" value="1">
			<bean:message key="label.collectionPager.page" bundle="MANAGER_RESOURCES"/>:
			<cp:collectionPages url="<%= URL %>" numberOfVisualizedPages="11" pageNumberAttributeName="pageNumber" numberOfPagesAttributeName="numberOfPages"/>	
		</logic:notEqual>	
	</div>

	<fr:view name="domainObjectActionLogs" schema="ListRoleOperationLogs">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4"/>
			<fr:property name="columnClasses" value="nowrap smalltxt,smalltxt,nowrap smalltxt,nowrap smalltxt,aleft smalltxt,aleft smalltxt"/>
		</fr:layout>
	</fr:view>		
	
	<logic:notEqual name="numberOfPages" value="1">
		<bean:message key="label.collectionPager.page" bundle="MANAGER_RESOURCES"/>:
		<cp:collectionPages url="<%= URL %>" numberOfVisualizedPages="11" pageNumberAttributeName="pageNumber" numberOfPagesAttributeName="numberOfPages"/>	
	</logic:notEqual>	
</logic:notEmpty>

<logic:empty name="domainObjectActionLogs">			
	<p class="mtop05"><em><bean:message key="label.empty.log" bundle="MANAGER_RESOURCES"/></em></p>		
</logic:empty>