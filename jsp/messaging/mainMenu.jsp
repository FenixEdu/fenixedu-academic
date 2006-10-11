<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<style>@import url(<%= request.getContextPath() %>/CSS/navlateralnew.css);</style> <!-- Import new CSS for this section: #navlateral  -->


<ul>
	<li class="navheader"><bean:message bundle="MESSAGING_RESOURCES" key="messaging.menu.announcements.link"/></li>
	<li>
		<html:link page="/announcements/announcementsStartPageHandler.do?method=news">
			<bean:message bundle="MESSAGING_RESOURCES" key="messaging.menu.news.link"/>
		</html:link>
	</li>
	<li>
		<html:link page="/announcements/announcementsStartPageHandler.do?method=start">
			<bean:message bundle="MESSAGING_RESOURCES" key="messaging.menu.favourites.link"/>
		</html:link>			
	</li>			
	<li>
		<html:link page="/announcements/announcementsStartPageHandler.do?method=handleBoardListing">
			<bean:message bundle="MESSAGING_RESOURCES" key="messaging.menu.boards.link"/>
		</html:link>
	</li>
	
	<li class="navheader"><bean:message bundle="MESSAGING_RESOURCES" key="label.navheader.search"  /></li>
	<li>
		<html:link page="/findPerson.do?method=prepareFindPerson">
			<bean:message bundle="MESSAGING_RESOURCES" key="label.person.findPerson"  />
		</html:link>
	</li>
	<li>
		<html:link page="/organizationalStructure/structurePage.faces">
			<bean:message bundle="MESSAGING_RESOURCES" key="label.orgUnit" />
		</html:link>
	</li>
</ul>
