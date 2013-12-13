<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

<center>
	<img src="<%= request.getContextPath() %>/images/ID_FenixEdu.png" alt="<bean:message key="logo-fenix" bundle="IMAGE_RESOURCES" />" />
</center>

<ul>

<br/>
<li> 
	<html:link action="/announcements/announcementBoardsManagement?method=chooseBoardType">
		<bean:message bundle="MANAGER_RESOURCES" key="manager.announcements.manageBoard.link" />
	</html:link>
</li>
<li>
	<html:link action="/announcements/announcementBoardsManagement?method=stats">
		<bean:message bundle="MANAGER_RESOURCES" key="manager.announcements.stats.link" />
	</html:link>	
</li>
	<br/>
	<br/>
<li>
	<html:link action="/index.do">
		<bean:message bundle="MANAGER_RESOURCES" key="manager.announcements.stats.backToManagerIndex.link" />
	</html:link>	
</li>
</ul>