<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<logic:present name="currentExecutionCoursesAnnouncementBoards">	
	<logic:notEmpty name="currentExecutionCoursesAnnouncementBoards">
	
		<bean:define id="contextPrefix" name="contextPrefix" type="java.lang.String"/>
		<bean:define id="currentExecutionCoursesAnnouncementBoards" name="currentExecutionCoursesAnnouncementBoards" type="java.util.Collection<net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard>"/>
		<bean:define id="extraParameters" name="extraParameters"  type="java.lang.String"/>
		<bean:define id="person" name="person" type="net.sourceforge.fenixedu.domain.Person"/>

		<%							
			int indexOfLastSlash = contextPrefix.lastIndexOf("/");
			String prefix = contextPrefix.substring(0,indexOfLastSlash+1);
		%>

		<fr:view name="currentExecutionCoursesAnnouncementBoards" layout="announcements-board-table">
			<fr:layout name="announcements-board-table">
				<fr:property name="classes" value="tstyle2 tdcenter mtop05"/>
				<fr:property name="boardUrl" value="<%= contextPrefix + "method=viewAnnouncements" + "&amp;" + extraParameters +"&amp;announcementBoardId=${idInternal}"%>"/>
				<fr:property name="managerUrl" value="<%= prefix +"manage${class.simpleName}.do?method=prepareEditAnnouncementBoard&" + extraParameters +"&announcementBoardId=${idInternal}" + "&returnAction="+request.getAttribute("returnAction") + "&returnMethod="+request.getAttribute("returnMethod")+"&tabularVersion=true"%>"/>
						<fr:property name="removeFavouriteUrl" value="<%= contextPrefix + "method=removeBookmark" + "&" + extraParameters + "&announcementBoardId=${idInternal}"%>" />
				<fr:property name="addFavouriteUrl"  value="<%= contextPrefix + "method=addBookmark" + "&" + extraParameters + "&announcementBoardId=${idInternal}"%>"/>
				<fr:property name="rssUrl" value="/external/announcementsRSS.do?method=simple&announcementBoardId=${idInternal}"/>
			</fr:layout>
		</fr:view>
		
	</logic:notEmpty>
	<logic:empty name="currentExecutionCoursesAnnouncementBoards">
		<em><bean:message key="view.announcementBoards.noBoardsToCurrentSelection" bundle="MESSAGING_RESOURCES"/></em>
	</logic:empty>
 </logic:present>