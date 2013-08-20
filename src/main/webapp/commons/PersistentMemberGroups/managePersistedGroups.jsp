<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

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