<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<bean:define id="unitID" name="unit" property="idInternal"/>
<bean:define id="actionName" name="functionalityAction"/>

<h2><bean:message key="label.editAccessGroup" bundle="RESEARCHER_RESOURCES"/></h2>

<fr:edit id="editGroup" name="bean" schema="create.persistent.group" action="<%= "/" + actionName + ".do?method=editPersistedGroup&unitId=" + unitID %>">
	<fr:layout>
		<fr:property name="classes" value="tstyle5 thlight thright"/>
		<fr:property name="columnClasses" value=",,tdclear tderror1"/>
	</fr:layout>
</fr:edit>