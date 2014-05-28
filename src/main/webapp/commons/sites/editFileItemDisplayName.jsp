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
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app"%>


<bean:define id="actionName" name="siteActionName" />
<bean:define id="contextParam" name="siteContextParam" />
<bean:define id="contextParamValue" name="siteContextParamValue" />
<bean:define id="context" value="<%= contextParam + "=" + contextParamValue %>" />
<bean:define id="sectionID" name="section" property="externalId" />


<h2>
	<bean:message key="label.item" bundle="SITE_RESOURCES" /> 
	<logic:present name="item">
		<fr:view name="item" property="name" />
	</logic:present>
	<logic:notPresent name="item">
		<fr:view name="section" property="name" />
	</logic:notPresent>
</h2>

<bean:define id="url" value="<%=  actionName + "?method=section&sectionID=" + sectionID +"&" + context %>"/>
<logic:present name="item">
	<bean:define id="itemID" name="item" property="externalId" />
	<bean:define id="url" value="<%= url +  "#item-" + itemID %>"/>
</logic:present>


<logic:messagesPresent message="true">
	<html:messages id="messages" message="true" bundle="APPLICATION_RESOURCES">
		<p>
			<span class="error0"><bean:write name="messages" /></span>
		</p>
	</html:messages>
</logic:messagesPresent>


<div class="dinline forminline">
	<fr:form
		action="<%=  url %>">
		<table class="tstyle5 thleft thlight thmiddle">
			<tr>
				<th><bean:message key="label.file" bundle="SITE_RESOURCES"/>:</th>
				<td><fr:view name="fileItem" property="filename"/></td>	
			</tr>
			<tr>
				<th><bean:message key="label.displayName" bundle="SITE_RESOURCES" />:</th>
				<td><fr:edit name="fileItem" slot="displayName" >
							<fr:layout>
		                		<fr:property name="size" value="40"/>
		            		</fr:layout>
						</fr:edit>
				</td>
			</tr>
			<tr>
			<tr>
				<th><bean:message key="label.fileVisible.question" bundle="SITE_RESOURCES"/></th>
				<td><fr:edit name="fileItem" slot="visible"/></td>	
			</tr>
		</table>
		
		
		<html:submit>
			<bean:message key="button.submit" bundle="SITE_RESOURCES" />
		</html:submit>
	</fr:form>
	<fr:form action="<%= url %>">
		<html:submit>
			<bean:message key="button.cancel" bundle="SITE_RESOURCES" />
		</html:submit>
	</fr:form>
</div>

        