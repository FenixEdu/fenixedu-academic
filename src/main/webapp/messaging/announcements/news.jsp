<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="date"%>

<html:xhtml/>

<%--
<bean:define id="userView" name="USER_SESSION_ATTRIBUTE" property="user" />
<logic:notEmpty name="userView" property="person.user.lastLoginHost">
	<logic:notEmpty name="userView" property="person.user.lastLoginDateTime">
		<bean:define id="lastLoginDateTime" type="java.util.Date" name="userView" property="person.user.lastLoginDateTime"/>
		<bean:define id="timestamp" ><%= lastLoginDateTime.getTime() %></bean:define>
		<bean:define id="lastLoginHost" name="userView" property="person.user.lastLoginHost"/>
	    <p class="mtop0 mbottom2" style="float: right;"><span style="background-color: #eee; padding: 0.25em;"><bean:message key="last.login.dateTime"/>&nbsp;<b><date:format pattern="dd-MM-yyyy HH:mm"><bean:write name="timestamp"/></date:format></b> (<bean:write name="lastLoginHost"/>)</span></p>	
	</logic:notEmpty>	    
</logic:notEmpty>
--%>

<em><bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.portal"/></em>
<h2><bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.news.title"/></h2>

<jsp:include flush="true" page="/messaging/context.jsp"/>

<h3><bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.news"/></h3>

<html:form action="/announcements/announcementsStartPageHandler.do">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="news"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.recentBoardsTimeSpanSelection" property="recentBoardsTimeSpanSelection"/>
	<table class="tstyle5 thright thlight thmiddle mvert05">
		<tr>
			<th rowspan="2"><bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.show"/>:</th>
			<td>
		    <html:select property="howManyAnnouncementsToShow" onchange="this.form.submit();">
		        <html:option value="6">6 <bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.announcements.lowerCase" /></html:option>
	   	        <html:option value="12">12 <bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.announcements.lowerCase" /></html:option>
		        <html:option value="24">24 <bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.announcements.lowerCase" /></html:option>
		    </html:select>
		    
				<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.boardType" property="boardType" value="BOOKMARKED" onchange="this.form.submit();">
					<bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.bookmarked" />
				</html:radio>

				<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.boardType" property="boardType" value="INSTITUTIONAL" onchange="this.form.submit();">
					<bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.institutional" />
				</html:radio>
		    
			</td>
		</tr>
	</table>
</html:form>

<jsp:include page="/messaging/announcements/listAnnouncements.jsp" flush="true"/>

<%-- 
<h3 class="mtop2 mbottom05"><bean:message bundle="MESSAGING_RESOURCES" key="label.last.created.boards"/></h3>
<html:form action="/announcements/announcementsStartPageHandler.do">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="news"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.howManyAnnouncementsToShow" property="howManyAnnouncementsToShow"/>
	<e:labelValues id="values" bundle="ENUMERATION_RESOURCES" enumeration="net.sourceforge.fenixedu.presentationTier.Action.messaging.RecentBoardsTimeSpanSelection" /> 
	<table class="tstyle5 mvert05">
		<tr>
			<td>Mostrar canais criados nos últimos:</td>
			<td>
			    <html:select property="recentBoardsTimeSpanSelection" onchange="this.form.submit();">
	        		<html:options collection="values" property="value" labelProperty="label" />
			    </html:select>
		    </td>
	    </tr>
    </table>
</html:form>
<jsp:include page="/messaging/announcements/listAnnouncementBoards.jsp" flush="true"/>
--%>