<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<jsp:include flush="true" page="/messaging/context.jsp"/>

<logic:present name="announcements">
	<bean:define id="contextPrefix" name="contextPrefix" type="java.lang.String"/>
	<bean:define id="extraParameters" name="extraParameters" />
	<bean:define id="announcementBoard" name="announcementBoard" type="net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard"/>
	<bean:define id="announcementBoardId" name="announcementBoard" property="externalId"/>		
	<bean:define id="person" name="person" type="net.sourceforge.fenixedu.domain.Person"/>

	<em><bean:message key="messaging.announcements.title.label" bundle="MESSAGING_RESOURCES"/></em>
	<h2><bean:write name="announcementBoard" property="name"/>

	<span title="Really Simple Syndication" style="font-weight: normal; font-size: 0.7em;">
		 <%
			java.util.Map parameters = new java.util.HashMap();
			parameters.put("method","simple");
			parameters.put("announcementBoardId",announcementBoard.getExternalId());
			request.setAttribute("parameters",parameters);
			if (announcementBoard.getReaders() == null)
			{
			%>
			(<html:link module="" action="/external/announcementsRSS" name="parameters">RSS<%--<img src="<%= request.getContextPath() %>/images/rss_ico.png"/>--%></html:link>)
			<%
			}
			%>							
	</span>
	
	</h2>

	<jsp:include page="/webSiteManager/listAnnouncements.jsp" flush="true"/>

</logic:present>