<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<h2><bean:message key="label.editManagers.title" bundle="MANAGER_RESOURCES"/></h2>

<p>
<logic:notEmpty name="site" property="managers">
	<bean:message key="label.webSiteManagers" bundle="MANAGER_RESOURCES"/>:<br/>
	<fr:view name="site" property="managers" schema="showPersonName">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2"/>
			<fr:property name="key(removeManager)" value="label.remove"/>
			<fr:property name="bundle(removeManager)" value="MANAGER_RESOURCES" />
			<fr:property name="link(removeManager)" value="<%= "/manageResearchUnitSite.do?method=removeManager&amp;oid=" + request.getParameter("oid") %>"/>
			<fr:property name="param(removeManager)" value="idInternal/personID"/>
		</fr:layout>
	</fr:view>	

</logic:notEmpty>

<logic:empty name="site" property="managers">
	<bean:message key="label.noWebSiteManagers" bundle="MANAGER_RESOURCES"/>
</logic:empty>
</p>

<fr:form action="<%= "/manageResearchUnitSite.do?method=addManager&amp;oid=" + request.getParameter("oid") %>">
<fr:edit id="addManager" name="personBean" schema="selectPerson" >
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5"/>
	</fr:layout>
</fr:edit>

<html:submit><bean:message key="button.submit" bundle="MANAGER_RESOURCES"/></html:submit>

</fr:form>