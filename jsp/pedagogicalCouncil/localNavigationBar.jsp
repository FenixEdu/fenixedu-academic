<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
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
	</ul>
</logic:present>
