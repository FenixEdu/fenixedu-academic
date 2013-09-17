<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/collection-pager" prefix="cp" %>

<h2><bean:message key="label.uploadersManagement" bundle="RESEARCHER_RESOURCES"/></h2>
<bean:define id="actionName" name="functionalityAction"/>

<bean:define id="unitID" name="unit" property="externalId"/>

<logic:equal name="unit" property="currentUserAbleToDefineGroups" value="true">
	<fr:edit name="unit" schema="edit-uploaders">
		<fr:layout>
			<fr:property name="classes" value="tstyle5 thlight thmiddle"/>
			<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
		</fr:layout>
	<fr:destination name="success" path="<%= "/" + actionName + ".do?method=configureGroups&unitId=" + unitID %>"/>
	<fr:destination name="cancel" path="<%= "/" + actionName + ".do?method=configureGroups&unitId=" + unitID %>"/>
	</fr:edit>
</logic:equal>

