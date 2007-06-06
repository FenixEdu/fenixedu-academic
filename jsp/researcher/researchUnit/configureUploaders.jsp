<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/collectionPager.tld" prefix="cp" %>

<h2><bean:message key="label.uploadersManagement" bundle="RESEARCHER_RESOURCES"/></h2>

<bean:define id="unitID" name="unit" property="idInternal"/>

<logic:equal name="unit" property="currentUserAbleToDefineGroups" value="true">
	<fr:edit name="unit" schema="edit-uploaders">
		<fr:layout>
			<fr:property name="classes" value="tstyle5 thlight thmiddle"/>
			<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
		</fr:layout>
<fr:destination name="success" path="<%= "/researchUnitFunctionalities.do?method=configureGroups&unitId=" + unitID %>"/>
	<fr:destination name="cancel" path="<%= "/researchUnitFunctionalities.do?method=configureGroups&unitId=" + unitID %>"/>
	</fr:edit>
</logic:equal>

