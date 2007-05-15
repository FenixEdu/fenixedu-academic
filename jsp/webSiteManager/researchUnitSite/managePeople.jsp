<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<html:xhtml/>

<h2><bean:message key="label.managePeople.title" bundle="WEBSITEMANAGER_RESOURCES"/></h2>
<bean:define id="siteID" name="site" property="idInternal"/>

<logic:messagesPresent message="true">
	<html:messages id="messages" message="true" bundle="WEBSITEMANAGER_RESOURCES">
	<p>
		<span class="error0"><bean:write name="messages" /></span>
	</p>
	</html:messages>
</logic:messagesPresent>

<fr:view name="site" property="unit.researchContracts" schema="view.researchContract">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1"/>
		<fr:property name="sortBy" value="beginDate"/>
		<fr:property name="key(remove)" value="label.remove" />
		<fr:property name="bundle(remove)" value="WEBSITEMANAGER_RESOURCES" />
		<fr:property name="link(remove)" value="<%= "/manageResearchUnitSite.do?method=removePerson&oid=" + siteID %>"/>
		<fr:property name="param(remove)" value="idInternal/cid" />
	</fr:layout>
</fr:view>

<fr:edit id="createPersonContract" name="bean" schema="create.researchContract" action="<%= "/manageResearchUnitSite.do?method=addPerson&oid=" + siteID %>">
	<fr:layout>
		<fr:property name="classes" value="tstyle5 thlight thright"/>
		<fr:property name="columnClasses" value=",,tdclear tderror1"/>
	</fr:layout>
	<fr:destination name="invalid" path="/manageResearchUnitSite.do?method=managePeople"/>
</fr:edit>