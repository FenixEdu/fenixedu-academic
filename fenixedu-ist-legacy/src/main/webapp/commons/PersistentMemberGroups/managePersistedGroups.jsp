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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<html:xhtml/>

<bean:define id="unitID" name="unit" property="externalId"/>
<bean:define id="actionName" name="functionalityAction"/>

<h2><bean:message key="label.manageAccessGroups" bundle="RESEARCHER_RESOURCES"/></h2>

<logic:equal name="unit" property="currentUserAbleToDefineGroups" value="true">

	<p class="mtop2 mbottom05">
		<strong><bean:message key="label.accessGroup" bundle="RESEARCHER_RESOURCES"/></strong>
		(<html:link page="<%= "/" + actionName + ".do?method=prepareCreatePersistedGroup&unitId=" + unitID %>"><bean:message key="label.createNewPersistedGroup" bundle="RESEARCHER_RESOURCES"/></html:link>)<br/>
		<span class="color555"><bean:message key="label.accessGroup.explanation" bundle="RESEARCHER_RESOURCES"/></span>
	</p>
	
	<logic:empty name="groups"> 
		<p class="mtop05"><em><bean:message key="label.noAccessGroups" bundle="RESEARCHER_RESOURCES"/></em></p>
	</logic:empty>
	
	<logic:notEmpty name="groups"> 
	<fr:view name="groups" schema="view.persistent.group">
		<fr:layout name="tabular"> 
			<fr:property name="classes" value="tstyle2 thlight mtop05"/>
			<fr:property name="key(delete)" value="label.delete" />
			<fr:property name="bundle(delete)" value="APPLICATION_RESOURCES" />
			<fr:property name="link(delete)" value="<%= "/" + actionName + ".do?method=deletePersistedGroup&unitId=" + unitID %>"/>
			<fr:property name="param(delete)" value="externalId/groupId" />
			<fr:property name="key(edit)" value="label.edit" />
			<fr:property name="bundle(edit)" value="APPLICATION_RESOURCES" />
			<fr:property name="link(edit)" value="<%= "/" + actionName + ".do?method=prepareEditPersistedGroup&unitId=" + unitID %>"/>
			<fr:property name="param(edit)" value="externalId/groupId" />
		</fr:layout>
	</fr:view>
	</logic:notEmpty>
	
	<p class="mtop15 mbottom05">
		<strong><bean:message key="label.uploaders" bundle="RESEARCHER_RESOURCES"/></strong>
		(<html:link page="<%= "/" + actionName + ".do?method=configureUploaders&unitId=" + unitID %>"><bean:message key="label.publicationCollaboratorsGroupManagement" bundle="RESEARCHER_RESOURCES"/></html:link>)<br/>
		<span class="color555"><bean:message key="label.uploaders.explanation" bundle="RESEARCHER_RESOURCES"/></span>
	</p>
	
	<logic:empty name="unit" property="allowedPeopleToUploadFiles">
		<p>
			<em><bean:message key="label.noUploadersDefined" bundle="RESEARCHER_RESOURCES"/>.</em>
		</p>
	</logic:empty>
	
	<logic:notEmpty name="unit" property="allowedPeopleToUploadFiles">
		<table class="tstyle2 thlight">
		<tr>
			<th><bean:message key="label.group" bundle="RESEARCHER_RESOURCES"/></th><th><bean:message key="label.members" bundle="RESEARCHER_RESOURCES"/></th>
		</tr>
		<tr>
			<td>
				<bean:message key="label.uploaders.groupName" bundle="RESEARCHER_RESOURCES"/>
			</td>
			<td>
				<fr:view name="unit" property="allowedPeopleToUploadFiles">
					<fr:layout>
						<fr:property name="eachLayout" value="values"/>
						<fr:property name="eachSchema" value="showNickName"/>
					</fr:layout>	
				</fr:view>
			</td>
		</tr>
		</table>
	</logic:notEmpty>
</logic:equal>