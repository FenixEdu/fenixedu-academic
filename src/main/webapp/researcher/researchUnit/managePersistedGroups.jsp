<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<jsp:include flush="true" page="../../commons/PersistentMemberGroups/managePersistedGroups.jsp"/>

<bean:define id="unitID" name="unit" property="externalId"/>

<logic:equal name="unit" property="currentUserAbleToDefineGroups" value="true">

	<p class="mtop2 mbottom05">
		<strong><bean:message key="label.publicationManagers" bundle="RESEARCHER_RESOURCES"/></strong>
		(<html:link page="<%= "/researchUnitFunctionalities.do?method=configurePublicationCollaborators&unitId=" + unitID %>"><bean:message key="label.publicationCollaboratorsGroupManagement" bundle="RESEARCHER_RESOURCES"/></html:link>)<br/>
		<span class="color555"><bean:message key="label.publicationManagers.explanation" bundle="RESEARCHER_RESOURCES"/></span>
	</p>

<logic:notEmpty name="unit" property="publicationCollaborators">
	<table class="tstyle2 thlight">
	<tr>
		<th><bean:message key="label.group" bundle="RESEARCHER_RESOURCES"/></th><th><bean:message key="label.members" bundle="RESEARCHER_RESOURCES"/></th>
	</tr>
	<tr>
		<td>
			<bean:message key="label.uploaders.groupName" bundle="RESEARCHER_RESOURCES"/>
		</td>
		<td>
			<fr:view name="unit" property="publicationCollaborators">
				<fr:layout>
					<fr:property name="eachLayout" value="values"/>
					<fr:property name="eachSchema" value="showNickName"/>
				</fr:layout>	
			</fr:view>
		</td>
	</tr>
	</table>
</logic:notEmpty>

<logic:empty name="unit" property="publicationCollaborators">
	<p>
		<em><bean:message key="label.noPublicationCollaborators" bundle="RESEARCHER_RESOURCES"/>.</em>
	</p>
</logic:empty>

</logic:equal>