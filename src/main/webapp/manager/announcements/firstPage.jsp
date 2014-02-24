<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

<h2><bean:message key="manager.announcements.title.label" bundle="MANAGER_RESOURCES"/></h2>

<table>
	<tr>
		<td class="well">
			<bean:message bundle="MANAGER_RESOURCES" key="manager.announcements.welcome"/>		
		</td>
	</tr>
</table>


<br/>
<div class="col-lg-offset-4">
	       <html:link styleClass="btn btn-primary" action="/announcements/manageUnitAnnouncementBoard.do?method=showTree">
	               <bean:message bundle="MANAGER_RESOURCES" key="manager.announcements.manageBoard.link" />
	       </html:link>

	       <html:link styleClass="btn btn-primary" action="/announcements/announcementBoardsManagement?method=stats">
	               <bean:message bundle="MANAGER_RESOURCES" key="manager.announcements.stats.link" />
	       </html:link>    

</div>