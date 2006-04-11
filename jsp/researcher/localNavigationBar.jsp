<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>


<!-- Import new CSS for this section: #navlateral  -->
<style>@import url(<%= request.getContextPath() %>/CSS/navlateralnew.css);</style>

<logic:present role="RESEARCHER">
	<ul>
		<li>
			<html:link page="/viewCurriculum.do?method=prepare">
				<bean:message bundle="RESEARCHER_RESOURCES" key="link.viewCurriculum"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			</html:link>
		</li>
		<li class="navheader"><bean:message bundle="RESEARCHER_RESOURCES" key="link.managementTitle"/></li>
		<li class="sub">
			<ul>
			<li><html:link page="/interests/interestsManagement.do?method=prepare"><bean:message bundle="RESEARCHER_RESOURCES" key="link.interestsManagement"/></html:link></li>
			<li><html:link page="/events/eventsManagement.do?method=showEvents"><bean:message bundle="RESEARCHER_RESOURCES" key="link.eventsManagement"/></html:link></li>
			<li><html:link page="/projects/projectsManagement.do?method=listProjects"><bean:message bundle="RESEARCHER_RESOURCES" key="link.projectsManagement"/></html:link></li>
			</ul>
		</li>
	</ul>
	
	<br />
</logic:present>
