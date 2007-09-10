<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@page import="net.sourceforge.fenixedu.domain.PedagogicalCouncilSite"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<logic:present role="PEDAGOGICAL_COUNCIL">
	<%
		request.setAttribute("site", PedagogicalCouncilSite.getSite());
		request.setAttribute("unit", PedagogicalCouncilSite.getSite().getUnit());
	%>

	<ul>
		<li>
			<html:link page="/weeklyWorkLoad.do?method=prepare&amp;page=0">
			    <bean:message key="link.weekly.work.load"/>
			</html:link>
		</li>
		<li class="navheader">
			<bean:message bundle="PEDAGOGICAL_COUNCIL" key="bolonha.process"/>
		</li>
		<li>
			<html:link page="/competenceCourses/competenceCoursesManagement.faces">
				<bean:message bundle="PEDAGOGICAL_COUNCIL" key="navigation.competenceCoursesManagement"/>
			</html:link>
		</li>
		<li>
			<html:link page="/curricularPlans/curricularPlansManagement.faces">
				<bean:message bundle="PEDAGOGICAL_COUNCIL" key="navigation.curricularPlansManagement"/>
			</html:link>
		</li>
		
		<li class="navheader">
			<bean:message key="title.unit.communication.section" bundle="RESEARCHER_RESOURCES"/>
		</li>
		<bean:define id="unitId" name="unit" property="idInternal"/>
		<li>
			<html:link page="<%= "/sendEmail.do?method=prepare&unitId=" + unitId %>">
				<bean:message key="label.sendEmailToGroups" bundle="RESEARCHER_RESOURCES"/>
			</html:link>
		</li>	
		<logic:equal name="unit" property="currentUserAbleToDefineGroups" value="true">
			<li>
				<html:link page="<%= "/pedagogicalCouncilFiles.do?method=configureGroups&unitId=" + unitId %>">
					<bean:message key="label.configurePersistentGroups" bundle="RESEARCHER_RESOURCES"/>
				</html:link>
			</li>
		</logic:equal>
		<li>
			<html:link page="<%= "/pedagogicalCouncilFiles.do?method=manageFiles&unitId=" + unitId %>">
				<bean:message key="label.manageFiles" bundle="RESEARCHER_RESOURCES"/>
			</html:link>
		</li>
		
		<li class="navheader">
			<bean:message key="delegates.section" bundle="PEDAGOGICAL_COUNCIL"/>
		</li>
		<li>
			<html:link page="/electionsPeriodsManagement.do?method=prepare&forwardTo=createEditCandidacyPeriods">
				<bean:message key="link.createEditCandidacyPeriods" bundle="PEDAGOGICAL_COUNCIL"/>
			</html:link>
		</li>
		<li>
			<html:link page="/electionsPeriodsManagement.do?method=prepare&forwardTo=createEditVotingPeriods">
				<bean:message key="link.createEditVotingPeriods" bundle="PEDAGOGICAL_COUNCIL"/>
			</html:link>
		</li>
		<li>
			<html:link page="/electionsPeriodsManagement.do?method=prepare&forwardTo=showCandidacyPeriods">
				<bean:message key="link.showCandidacyPeriods" bundle="PEDAGOGICAL_COUNCIL"/>
			</html:link>
		</li>
		<li>
			<html:link page="/electionsPeriodsManagement.do?method=prepare&forwardTo=showVotingPeriods">
				<bean:message key="link.showVotingPeriods" bundle="PEDAGOGICAL_COUNCIL"/>
			</html:link>
		</li>
		<li>
			<html:link page="/delegatesManagement.do?method=prepare">
				<bean:message key="link.delegatesManagement" bundle="PEDAGOGICAL_COUNCIL"/>
			</html:link>
		</li>
		<li>
			<html:link page="/delegatesManagement.do?method=prepareViewGGAEDelegates">
				<bean:message key="link.delegatesManagement.GGAE" bundle="PEDAGOGICAL_COUNCIL"/>
			</html:link>
		</li>
		<li>
			<html:link page="/findDelegates.do?method=prepare&searchByName=true">
				<bean:message key="link.findDelegates" bundle="PEDAGOGICAL_COUNCIL"/>
			</html:link>
		</li>
	</ul>
</logic:present>
