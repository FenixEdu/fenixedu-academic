<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<bean:define id="unitID" name="unit" property="externalId"/>

<h2><bean:message key="label.editFile" bundle="RESEARCHER_RESOURCES"/></h2>
<bean:define id="actionName" name="functionalityAction"/>

<logic:present name="file">
	<fr:edit id="editFile" name="file" schema="edit.unit.files" action="<%= "/" + actionName + ".do?method=editFile&unitId=" + unitID %>">
		<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thright"/>
				<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>
		<fr:destination name="cancel" path="<%= "/" + actionName + ".do?method=manageFiles&unitId=" + unitID %>"/>
	</fr:edit>
</logic:present>

<logic:notPresent name="file">
	<em><bean:message key="label.unableToEditFile" bundle="RESEARCHER_RESOURCES"/></em>
</logic:notPresent>