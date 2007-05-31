<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<bean:define id="unitID" name="unit" property="idInternal"/>
<h2><bean:message key="label.manageAccessGroups" bundle="RESEARCHER_RESOURCES"/></h2>

<logic:equal name="unit" property="currentUserAbleToDefineGroups" value="true">
	<ul>
		<li>
			<html:link page="<%= "/researchUnitFunctionalities.do?method=prepare&unitId=" + unitID %>"><bean:message key="label.back" bundle="APPLICATION_RESOURCES"/></html:link>
		</li>
		<li>
			<html:link page="<%= "/researchUnitFunctionalities.do?method=prepareCreatePersistedGroup&unitId=" + unitID %>"><bean:message key="label.createNewPersistedGroup" bundle="RESEARCHER_RESOURCES"/></html:link>
		</li>
	</ul>
	
	<fr:view name="groups" schema="view.persistent.group">
		<fr:layout name="tabular"> 
			<fr:property name="classes" value="tstyle2"/>
			<fr:property name="key(delete)" value="label.delete" />
			<fr:property name="bundle(delete)" value="APPLICATION_RESOURCES" />
			<fr:property name="link(delete)" value="<%= "/researchUnitFunctionalities.do?method=deletePersistedGroup&unitId=" + unitID %>"/>
			<fr:property name="param(delete)" value="idInternal/groupId" />
			<fr:property name="key(edit)" value="label.edit" />
			<fr:property name="bundle(edit)" value="APPLICATION_RESOURCES" />
			<fr:property name="link(edit)" value="<%= "/researchUnitFunctionalities.do?method=prepareEditPersistedGroup&unitId=" + unitID %>"/>
			<fr:property name="param(edit)" value="idInternal/groupId" />
		</fr:layout>
	</fr:view>	
</logic:equal>