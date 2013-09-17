<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<h2><bean:message key="label.addFile" bundle="RESEARCHER_RESOURCES"/></h2>

<bean:define id="unitID" name="unit" property="externalId"/>
<bean:define id="actionName" name="functionalityAction"/>

<logic:equal name="unit" property="currentUserAllowedToUploadFiles" value="true">
<fr:edit id="upload" name="fileBean" schema="view.genericFileUpload" action="<%= "/" + actionName + ".do?method=uploadFile&unitId=" + unitID %>">
	<fr:layout>
		<fr:property name="classes" value="tstyle5 thlight thright"/>
		<fr:property name="columnClasses" value=",,tdclear tderror1"/>
	</fr:layout>
	<fr:destination name="cancel" path="<%= "/" + actionName + ".do?method=manageFiles&unitId=" + unitID %>"/>
</fr:edit>
</logic:equal>