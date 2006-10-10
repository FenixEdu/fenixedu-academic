<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%> 

<h2>
	<bean:message key="manager.announcements.title.label" bundle="MANAGER_RESOURCES"/>
</h2>

<ul>
	<li>
		<html:link action="/announcements/manageUnitAnnouncementBoard.do?method=showTree">
			Unidade	
		</html:link>
	</li>
	<li>
		<html:link action="/announcements/manageExecutionCourseAnnouncementBoard">
			Disciplina Execução
		</html:link>	
	</li>
</ul>
