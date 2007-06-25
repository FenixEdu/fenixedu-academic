<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@page import="net.sourceforge.fenixedu.domain.PedagogicalCouncilSite"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<logic:present role="PEDAGOGICAL_COUNCIL">
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
			<bean:message bundle="PEDAGOGICAL_COUNCIL" key="title.pedagogicalCouncil.site"/>
		</li>
		<li>
			<%
				request.setAttribute("site", PedagogicalCouncilSite.getSite());
			%>
			
			<logic:notEmpty name="site">
				<bean:define id="unitId" name="site" property="unit.idInternal"/>
				
				<html:link module="/publico" action="<%= "/pedagogicalCouncil/viewSite.do?method=presentation&amp;unitID=" + unitId %>" target="_blank">
					<bean:message bundle="PEDAGOGICAL_COUNCIL" key="link.site.view"/>
				</html:link>
			</logic:notEmpty>
		</li>
		<li>
			<html:link page="/manageSitePermissions.do?method=chooseManagers">
				<bean:message bundle="PEDAGOGICAL_COUNCIL" key="link.site.manage.managers"/>
			</html:link>
		</li>
	</ul>
</logic:present>
