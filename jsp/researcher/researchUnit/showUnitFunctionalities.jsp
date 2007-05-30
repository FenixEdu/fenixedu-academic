<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>


<html:xhtml/>

<bean:define id="unitID" name="unit" property="idInternal"/>
<h2><bean:message key="label.showAvailableFunctionalities" bundle="RESEARCHER_RESOURCES"/></h2>

<ul>
	<li><html:link page="<%= "/sendEmailToResearchUnitGroups.do?method=prepare&unitId=" + unitID %>"><bean:message key="label.sendEmailToGroups" bundle="RESEARCHER_RESOURCES"/></html:link></li>	
	<logic:equal name="unit" property="currentUserAbleToDefineGroups" value="true">
	<li><html:link page="<%= "/researchUnitFunctionalities.do?method=configureGroups&unitId=" + unitID %>"><bean:message key="label.configurePersistentGroups" bundle="RESEARCHER_RESOURCES"/></html:link>
	</logic:equal>
	<li><html:link page="<%= "/researchUnitFunctionalities.do?method=manageFiles&unitId=" + unitID %>"><bean:message key="label.manageFiles" bundle="RESEARCHER_RESOURCES"/></html:link></li>
</ul>

