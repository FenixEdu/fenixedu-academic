<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<html:xhtml/>

<bean:define id="unitID" name="unit" property="externalId"/>
<bean:define id="actionName" name="functionalityAction"/>

<h2><bean:message key="label.newPersistentGroup" bundle="RESEARCHER_RESOURCES"/></h2>

<fr:edit id="createGroup" name="bean" schema="create.persistent.group" action="<%= "/" + actionName + ".do?method=createPersistedGroup&unitId=" + unitID %>">
	<fr:layout>
		<fr:property name="classes" value="tstyle5 thlight thright"/>
		<fr:property name="columnClasses" value=",,tdclear tderror1"/>
	</fr:layout>
</fr:edit>

