<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>

<em><bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.portal"/></em>
<h2><bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.news.title"/></h2>

<jsp:include flush="true" page="/messaging/context.jsp"/>

<h3><bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.news"/></h3>

<html:form action="/announcements/announcementsStartPageHandler.do" method="get">
	<html:hidden property="method" value="news"/>
	<html:hidden property="recentBoardsTimeSpanSelection"/>
	<table class="tstyle5 thright thlight thmiddle mvert05">
		<tr>
			<th><bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.show"/>:</th>
			<td>
		    <html:select property="howManyAnnouncementsToShow" onchange="this.form.submit();">
		        <html:option value="6">6 <bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.announcements.lowerCase" /></html:option>
	   	        <html:option value="12">12 <bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.announcements.lowerCase" /></html:option>
		        <html:option value="24">24 <bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.announcements.lowerCase" /></html:option>
		    </html:select>
			</td>
		</tr>
		<tr>
			<th><bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.board.view" />:</th>
			<td>
				<html:radio property="boardType" value="BOOKMARKED" onchange="this.form.submit();">
					<bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.bookmarked" />
				</html:radio>
				<br/>
				<html:radio property="boardType" value="INSTITUTIONAL" onchange="this.form.submit();">
					<bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.institutional" />
				</html:radio>
			</td>
		</tr>
	</table>
</html:form>

<jsp:include page="/messaging/announcements/listAnnouncements.jsp" flush="true"/>

<%-- 
<h3 class="mtop2 mbottom05"><bean:message bundle="MESSAGING_RESOURCES" key="label.last.created.boards"/></h3>
<html:form action="/announcements/announcementsStartPageHandler.do" method="get">
	<html:hidden property="method" value="news"/>
	<html:hidden property="howManyAnnouncementsToShow"/>
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
