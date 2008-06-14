<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>
<bean:define id="unitID" name="unit" property="idInternal"/>

<h2><fr:view name="unit" property="name"/> (<fr:view name="unit" property="acronym"/>)</h2>

<ul>
	<li>
		<html:link page="<%= "/sendEmailToResearchUnitGroups.do?method=prepare&unitId=" + unitID %>">
			<bean:message key="label.sendEmailToGroups" bundle="RESEARCHER_RESOURCES"/>
		</html:link>
		<br/>
		<span class="color888">
			<bean:message key="label.sendEmailToGroups.explanation" bundle="RESEARCHER_RESOURCES"/>
		</span>
	</li>
    <logic:equal name="unit" property="currentUserAbleToInsertOthersPublications" value="true">
	<li>
			<html:link page="<%= "/researchUnitFunctionalities.do?method=preparePublications&unitId=" + unitID %>">
						<bean:message key="link.Publications" bundle="RESEARCHER_RESOURCES"/>
			</html:link>
			<br/>
			<span class="color888">
				<bean:message key="label.Publications.explanation" bundle="RESEARCHER_RESOURCES"/>
			</span>
	</li>
	</logic:equal>
	<logic:equal name="unit" property="currentUserAbleToDefineGroups" value="true">
		<li>
			<html:link page="<%= "/researchUnitFunctionalities.do?method=configureGroups&unitId=" + unitID %>">
				<bean:message key="label.configurePersistentGroups" bundle="RESEARCHER_RESOURCES"/>
			</html:link>
			<br/>
			<span class="color888">
				<bean:message key="label.configurePersistentGroups.explanation" bundle="RESEARCHER_RESOURCES"/>
			</span>
		</li>
	</logic:equal>
	<li>
		<html:link page="<%= "/researchUnitFunctionalities.do?method=manageFiles&unitId=" + unitID %>">
			<bean:message key="label.manageFiles" bundle="RESEARCHER_RESOURCES"/>
		</html:link>
		<br/>
		<span class="color888">
			<bean:message key="label.manageFiles.explanation" bundle="RESEARCHER_RESOURCES"/>
		</span>
	</li>

</ul>
