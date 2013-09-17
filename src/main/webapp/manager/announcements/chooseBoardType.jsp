<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%> 

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
			Disciplina Execu��o
		</html:link>	
	</li>
</ul>
