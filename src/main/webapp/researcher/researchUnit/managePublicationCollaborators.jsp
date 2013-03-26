<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<bean:define id="unitID" name="unit" property="idInternal"/>

<h2><bean:message key="label.publicationCollaborators" bundle="RESEARCHER_RESOURCES"/></h2>

<logic:equal name="unit" property="currentUserAbleToDefineGroups" value="true">
<fr:edit name="unit" schema="edit-publication-collaborators">
	<fr:layout>
		<fr:property name="classes" value="tstyle5 thlight thmiddle"/>
		<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
	</fr:layout>

	<fr:destination name="success" path="<%= "/researchUnitFunctionalities.do?method=configureGroups&unitId=" + unitID %>"/>
	<fr:destination name="cancel" path="<%= "/researchUnitFunctionalities.do?method=configureGroups&unitId=" + unitID %>"/>
</fr:edit>
</logic:equal>

