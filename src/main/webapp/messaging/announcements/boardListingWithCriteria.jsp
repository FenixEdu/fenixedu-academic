<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>
<%@ taglib uri="/WEB-INF/collectionPager.tld" prefix="cp"%>

<html:xhtml/>

<em><bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.portal"/></em>
<h2><bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.board.announcements"/></h2>

<jsp:include flush="true" page="/messaging/context.jsp"/>

<html:form action="/announcements/announcementsStartPageHandler.do" >
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="handleBoardListing"/>
	<e:labelValues id="levelValues" bundle="ENUMERATION_RESOURCES" enumeration="net.sourceforge.fenixedu.domain.messaging.AnnouncementBoardAccessLevel" /> 
	<e:labelValues id="typeValues" bundle="ENUMERATION_RESOURCES" enumeration="net.sourceforge.fenixedu.domain.messaging.AnnouncementBoardAccessType" /> 
	
	<p class="mbottom025"><strong><bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.board.filtering"/></strong></p>
	<table class="tstyle5 thlight thright mtop0">
	<tr>
		<td><bean:message key="label.priviledge" bundle="MESSAGING_RESOURCES"/>:</td>
		<td>
		<html:select property="announcementBoardAccessLevel" onchange="this.form.submit();">
	        <html:options collection="levelValues" property="value" labelProperty="label" />
		</html:select>
		</td>
	</tr>
	<tr>
		<td><bean:message key="label.type" bundle="MESSAGING_RESOURCES"/>:</td>
		<td>
	    <html:select property="announcementBoardAccessType" onchange="this.form.submit();">
	        <html:options collection="typeValues" property="value" labelProperty="label" />
	    </html:select>
		</td>
	</tr>
	</table>
	
</html:form>

<h3 class="mbottom05"><bean:message key="label.unitChannels" bundle="MESSAGING_RESOURCES"/></h3>
<logic:present name="unitAnnouncementBoards">	
	<logic:notEmpty name="unitAnnouncementBoards">
		<bean:define id="contextPrefix" name="contextPrefix" type="java.lang.String"/>
		<bean:define id="unitAnnouncementBoards" name="unitAnnouncementBoards" type="java.util.Collection<net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard>"/>
		<bean:define id="extraParameters" name="extraParameters" />
		<bean:define id="person" name="person" type="net.sourceforge.fenixedu.domain.Person"/>

		<%							
			int indexOfLastSlash = contextPrefix.lastIndexOf("/");
			String prefix = contextPrefix.substring(0,indexOfLastSlash+1);
		%>
		
	<bean:message key="label.page" bundle="MESSAGING_RESOURCES"/>:
 	        
       <bean:define id="urlToUnitBoards" type="java.lang.String">/messaging/announcements/announcementsStartPageHandler.do?page=0&amp;method=handleBoardListing&amp;<%=extraParameters%></bean:define>
       <cp:collectionPages url="<%= urlToUnitBoards %>" numberOfVisualizedPages="11" pageNumberAttributeName="pageNumberUnitBoards" numberOfPagesAttributeName="numberOfPagesUnitBoards"/>	
	
	   <fr:view name="unitAnnouncementBoards">
			<fr:layout name="announcements-board-table">
				<fr:property name="classes" value="tstyle2 tdcenter mtop05"/>
				<fr:property name="boardUrl" value="<%= contextPrefix + "method=viewAnnouncements" + "&" + extraParameters +"&announcementBoardId=${idInternal}"%>"/>
				<fr:property name="managerUrl" value="<%= prefix + "manage${class.simpleName}.do?method=prepareEditAnnouncementBoard&" + extraParameters + "&announcementBoardId=${idInternal}&returnAction="+request.getAttribute("returnAction") + "&returnMethod="+request.getAttribute("returnMethod")+"&tabularVersion=true"%>"/>
				<fr:property name="removeFavouriteUrl" value="<%= contextPrefix + "method=removeBookmark" + "&" + extraParameters + "&announcementBoardId=${idInternal}"%>" />
				<fr:property name="addFavouriteUrl"  value="<%= contextPrefix + "method=addBookmark" + "&" + extraParameters + "&announcementBoardId=${idInternal}"%>"/>
				<fr:property name="rssUrl" value="/external/announcementsRSS.do?method=simple&announcementBoardId=${idInternal}"/>
			</fr:layout>
		</fr:view>
			
			
		<bean:message key="label.page" bundle="MESSAGING_RESOURCES"/>			 	       
        <cp:collectionPages url="<%= urlToUnitBoards %>" numberOfVisualizedPages="11" pageNumberAttributeName="pageNumberUnitBoards" numberOfPagesAttributeName="numberOfPagesUnitBoards"/>			       	
        
	</logic:notEmpty>
	<logic:empty name="unitAnnouncementBoards">
	<p>
		<em><bean:message key="label.noChannelsInUnit" bundle="MESSAGING_RESOURCES"/></em>
	</p>
	</logic:empty>
</logic:present>

<h3 class="mtop2 mbottom05"><bean:message key="label.coursesChannels" bundle="MESSAGING_RESOURCES"/></h3>

<logic:present name="executionCourseAnnouncementBoards">	
	<logic:notEmpty name="executionCourseAnnouncementBoards">
		<bean:define id="contextPrefix" name="contextPrefix" type="java.lang.String"/>
		<bean:define id="executionCourseAnnouncementBoards" name="executionCourseAnnouncementBoards" type="java.util.Collection<net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard>"/>
		<bean:define id="extraParameters" name="extraParameters" />
		<bean:define id="person" name="person" type="net.sourceforge.fenixedu.domain.Person"/>

		<%							
			int indexOfLastSlash = contextPrefix.lastIndexOf("/");
			String prefix = contextPrefix.substring(0,indexOfLastSlash+1);
		%>

	<bean:message key="label.page" bundle="MESSAGING_RESOURCES"/>:
 	 
       <bean:define id="urlToExecutionCourses" type="java.lang.String">/messaging/announcements/announcementsStartPageHandler.do?page=0&amp;method=handleBoardListing&amp;<%=extraParameters%></bean:define>
       <cp:collectionPages url="<%= urlToExecutionCourses %>" numberOfVisualizedPages="11" pageNumberAttributeName="pageNumberExecutionCourseBoards" numberOfPagesAttributeName="numberOfPagesExecutionCourseBoards"/>	
	
	   <fr:view name="executionCourseAnnouncementBoards">
			<fr:layout name="announcements-board-table">
				<fr:property name="classes" value="tstyle2 tdcenter mtop05"/>
				<fr:property name="boardUrl" value="<%= contextPrefix + "method=viewAnnouncements" + "&" + extraParameters +"&announcementBoardId=${idInternal}"%>"/>
				<fr:property name="managerUrl" value="<%= prefix + "manage${class.simpleName}.do?method=prepareEditAnnouncementBoard&" + extraParameters + "&announcementBoardId=${idInternal}&returnAction="+request.getAttribute("returnAction") + "&returnMethod="+request.getAttribute("returnMethod")+"&tabularVersion=true"%>"/>
				<fr:property name="removeFavouriteUrl" value="<%= contextPrefix + "method=removeBookmark" + "&" + extraParameters + "&announcementBoardId=${idInternal}"%>" />
				<fr:property name="addFavouriteUrl"  value="<%= contextPrefix + "method=addBookmark" + "&" + extraParameters + "&announcementBoardId=${idInternal}"%>"/>
				<fr:property name="rssUrl" value="/external/announcementsRSS.do?method=simple&announcementBoardId=${idInternal}"/>
			</fr:layout>
		</fr:view>

		<bean:message key="label.page" bundle="MESSAGING_RESOURCES"/>:
	    <cp:collectionPages url="<%= urlToExecutionCourses %>" numberOfVisualizedPages="11" pageNumberAttributeName="pageNumberExecutionCourseBoards" numberOfPagesAttributeName="numberOfPagesExecutionCourseBoards"/>	
	
	</logic:notEmpty>
	<logic:empty name="executionCourseAnnouncementBoards">
		<p>
			<em><bean:message key="label.noCoursesWithSelectedCriteria" bundle="MESSAGING_RESOURCES"/></em>
		</p>
	</logic:empty>
</logic:present>