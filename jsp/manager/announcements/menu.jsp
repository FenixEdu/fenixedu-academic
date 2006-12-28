<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<center>
	<img alt="" src="<%= request.getContextPath() %>/images/logo-fenix.gif" width="100" height="100"/>
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