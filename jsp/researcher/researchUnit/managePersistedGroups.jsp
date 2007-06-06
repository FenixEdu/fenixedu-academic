<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<jsp:include flush="true" page="../../commons/PersistentMemberGroups/managePersistedGroups.jsp"/>

<bean:define id="unitID" name="unit" property="idInternal"/>

<logic:equal name="unit" property="currentUserAbleToDefineGroups" value="true">


	<p class="mtop2 mbottom05">
		<strong><bean:message key="label.publicationManagers" bundle="RESEARCHER_RESOURCES"/></strong>
		(<html:link page="<%= "/researchUnitFunctionalities.do?method=configurePublicationCollaborators&unitId=" + unitID %>"><bean:message key="label.publicationCollaboratorsGroupManagement" bundle="RESEARCHER_RESOURCES"/></html:link>)<br/>
		<span class="color555"><bean:message key="label.publicationManagers.explanation" bundle="RESEARCHER_RESOURCES"/></span>
	</p>
	
<logic:notEmpty name="unit" property="publicationCollaborators">
	<fr:view name="unit" property="publicationCollaborators">
		<fr:layout>
			<fr:property name="eachLayout" value="values"/>
			<fr:property name="eachSchema" value="showNickName"/>
		</fr:layout>	
	</fr:view>
</logic:notEmpty>

<logic:empty name="unit" property="publicationCollaborators">
	<bean:message key="label.noPublicationCollaborators" bundle="RESEARCHER_RESOURCES"/>
</logic:empty>

</logic:equal>