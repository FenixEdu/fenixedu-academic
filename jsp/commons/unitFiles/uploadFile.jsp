<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h2><bean:message key="label.addFile" bundle="RESEARCHER_RESOURCES"/></h2>

<bean:define id="unitID" name="unit" property="idInternal"/>

<fr:edit id="upload" name="fileBean" schema="view.genericFileUpload" action="<%= "/researchUnitFunctionalities.do?method=uploadFile&unitId=" + unitID %>">
	<fr:layout>
		<fr:property name="classes" value="tstyle5"/>
		<fr:property name="columnClasses" value=",,tdclear tderror1"/>
	</fr:layout>
	<fr:destination name="cancel" path="<%= "/researchUnitFunctionalities.do?method=manageFiles&unitId=" + unitID %>"/>
</fr:edit>