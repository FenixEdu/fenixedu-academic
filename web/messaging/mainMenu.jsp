<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@page import="net.sourceforge.fenixedu.domain.util.email.Sender"%>
<html:xhtml/>

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
		<html:link page="/announcements/boards.do?method=search">
			<bean:message bundle="MESSAGING_RESOURCES" key="messaging.menu.boards.link"/>
		</html:link>			
	</li>
<%--
	<li>
		<html:link page="/announcements/announcementsStartPageHandler.do?method=handleBoardListing">
			<bean:message bundle="MESSAGING_RESOURCES" key="messaging.menu.boards.link"/>
		</html:link>
	</li>
--%>
	<% if (!Sender.getAvailableSenders().isEmpty()) { %>
		<li class="navheader"><bean:message bundle="MESSAGING_RESOURCES" key="label.emails"/></li>
		<% if (Sender.userHasRecipients()) { %>
		<li>
			<html:link page="/emails.do?method=newEmail">
				<bean:message bundle="MESSAGING_RESOURCES" key="label.email.new"/>
			</html:link>
		</li>
		<% } %>
		<li>
			<html:link page="/emails.do?method=viewSentEmails">
				<bean:message bundle="MESSAGING_RESOURCES" key="label.email.sent"/>
			</html:link>
		</li>
	<% } %>
	<li class="navheader"><bean:message bundle="MESSAGING_RESOURCES" key="label.navheader.files"/></li>
	<li>
		<html:link page="/viewFiles.do?method=showSources">
			<bean:message bundle="MESSAGING_RESOURCES" key="label.files.view"  />
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
	<li>
		<html:link page="/protocols.do?method=searchProtocols">
			<bean:message bundle="MESSAGING_RESOURCES" key="label.protocols"  />
		</html:link>
	</li>

	<%-- 		
	<li>
		<html:link page="/searchResearchers.do?method=search"><bean:message key="researcher.experts" bundle="RESEARCHER_RESOURCES"/></html:link>
	</li>
	--%>

<logic:present role="MANAGER">
		<li>
			<html:link page="/profileReport.do?method=viewReports">
				Estatísticas (ainda só disponivel para o manager)
			</html:link>
		</li>
</logic:present>
</ul>


