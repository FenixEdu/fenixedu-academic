<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>	

<logic:present role="role(BOLONHA_MANAGER)">	
	<ul>

		<li class="navheader"><bean:message key="navigation.competenceCoursesManagement"/></li>
		<li>
			<html:link page="/competenceCourses/competenceCoursesManagement.faces">
				<bean:message key="label.view"/>
			</html:link>
		</li>
		<li>
			<html:link page="/competenceCourses/searchCompetenceCourses.do?method=search">
				<bean:message key="navigation.search.competenceCourses"/>
			</html:link>
		</li>
		<li>
			<html:link page="/competenceCourses/manageVersions.do?method=prepare">
				<bean:message key="label.manage.versions"/>
			</html:link>
		</li>

		
		<li class="navheader"><bean:message key="navigation.curricularPlansManagement"/></li>
		<li>
			<html:link page="/curricularPlans/curricularPlansManagement.faces">
				<bean:message key="label.view"/>
			</html:link>
		</li>


	</ul>	
</logic:present>
