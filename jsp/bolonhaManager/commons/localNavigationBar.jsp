<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>	

<!-- Import new CSS for this section: #navlateral  -->
<style>@import url(<%= request.getContextPath() %>/CSS/navlateralnew.css);</style>

<logic:present role="BOLONHA_MANAGER">	
	<ul>
		<li>
			<html:link page="/competenceCourses/manageCompetenceCourses.faces">
				<bean:message key="manageCompetencesCourses"/>
			</html:link>
		</li>
		<li>
			<html:link page="/curricularPlans/manageCurricularPlans.faces">
				<bean:message key="manageCurricularPlans"/>
			</html:link>
		</li>
	</ul>	
</logic:present>
