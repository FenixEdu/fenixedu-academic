<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>

<logic:present role="RESEARCHER">
	<ul>
		<li><html:link page="/viewCurriculum.do"> <bean:message bundle="RESEARCHER_RESOURCES" key="link.viewCurriculum"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</html:link> </li>
		<li><html:link page="/publications/search.do?method=prepareSearchPublication"> <bean:message bundle="RESEARCHER_RESOURCES" key="label.search"/> </html:link>
	</ul>

	<ul style="margin-top: 0.75em;">
		<li><html:link page="/interests/interestsManagement.do?method=prepare"><bean:message bundle="RESEARCHER_RESOURCES" key="link.interestsManagement"/></html:link></li>
		<li><html:link page="/resultPublications/listPublications.do"><bean:message bundle="RESEARCHER_RESOURCES" key="link.Publications"/></html:link></li>
		<li><html:link page="/resultPatents/management.do"><bean:message bundle="RESEARCHER_RESOURCES" key="link.patentsManagement"/></html:link></li>			
		 
  	    <li><html:link page="/activities/activitiesManagement.do?method=listActivities"><bean:message bundle="RESEARCHER_RESOURCES" key="link.activitiesManagement"/></html:link></li>
		
		<%--
		<li class="navheader"><bean:message bundle="RESEARCHER_RESOURCES" key="link.participationsTitle"/></li>
		--%>
		<%-- 
		<li><html:link page="/resultPublications/listPublications.do"><bean:message bundle="RESEARCHER_RESOURCES" key="link.Publications"/></html:link></li>
		<li><html:link page="/projects/projectsManagement.do?method=listProjects"><bean:message bundle="RESEARCHER_RESOURCES" key="link.projectsManagement"/></html:link></li>
		--%>
	</ul>

	<bean:define id="workingResearchUnits" name="UserView" property="person.workingResearchUnits" type="java.util.List"/>

	<logic:notEmpty name="workingResearchUnits">
	<ul>
		<li class="navheader"><bean:message key="label.researchUnits" bundle="RESEARCHER_RESOURCES"/></li>
		<logic:iterate id="unitIter" name="workingResearchUnits">
			<bean:define id="unitID" name="unitIter" property="idInternal" type="java.lang.Integer"/>
			<bean:define id="unitName" name="unitIter" property="name" type="java.lang.String"/>
			<li> 
				<html:link title="<%= unitName %>" page="<%= "/researchUnitFunctionalities.do?method=prepare&unitId=" + unitID %>">
							<bean:write name="unitIter" property="acronym"/>
				</html:link>

				<logic:present name="unit">
					<logic:equal name="unit" property="idInternal" value="<%= unitID.toString() %>">
				<ul>
					<li>
						<html:link page="<%= "/sendEmailToResearchUnitGroups.do?method=prepare&unitId=" + unitID %>">
							<bean:message key="label.sendEmailToGroups" bundle="RESEARCHER_RESOURCES"/>
						 </html:link>
				    </li>	
  			    <logic:equal name="unitIter" property="currentUserAbleToInsertOthersPublications" value="true">
					<li>
						<html:link page="<%= "/researchUnitFunctionalities.do?method=preparePublications&unitId=" + unitID %>">
								<bean:message key="link.Publications" bundle="RESEARCHER_RESOURCES"/>
						</html:link>
					</li>
				</logic:equal>
				<logic:equal name="unitIter" property="currentUserAbleToDefineGroups" value="true">
					<li>
						<html:link page="<%= "/researchUnitFunctionalities.do?method=configureGroups&unitId=" + unitID %>"><bean:message key="label.configurePersistentGroups" bundle="RESEARCHER_RESOURCES"/>
						</html:link>
					</li>
				</logic:equal>
					<li><html:link page="<%= "/researchUnitFunctionalities.do?method=manageFiles&unitId=" + unitID %>"><bean:message key="label.manageFiles" bundle="RESEARCHER_RESOURCES"/></html:link></li>						
			</ul>
				</logic:equal>
			</logic:present>
		</logic:iterate>
	</ul>
	</logic:notEmpty>	
	
</logic:present>
