<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="date"%>

<html:xhtml/>

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