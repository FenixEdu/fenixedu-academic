<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<jsp:include flush="true" page="/messaging/context.jsp"/>

<logic:present name="announcements">
	<bean:define id="contextPrefix" name="contextPrefix" type="java.lang.String"/>
	<bean:define id="extraParameters" name="extraParameters" />
	<bean:define id="announcementBoard" name="announcementBoard" type="net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard"/>
	<bean:define id="announcementBoardId" name="announcementBoard" property="idInternal"/>		
	<bean:define id="person" name="person" type="net.sourceforge.fenixedu.domain.Person"/>

	<em><bean:message key="messaging.announcements.title.label" bundle="MESSAGING_RESOURCES"/></em>
	<h2><bean:write name="announcementBoard" property="name"/>

	<span title="Really Simple Syndication" style="font-weight: normal; font-size: 0.7em;">
		 <%
			java.util.Map parameters = new java.util.HashMap();
			parameters.put("method","simple");
			parameters.put("announcementBoardId",announcementBoard.getIdInternal());
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

	
	<ul class="list5">
			<%
				if (announcementBoard.hasWriter(person))
				{
			%>
			<li>
				<html:link action="<%= contextPrefix + "method=addAnnouncement&announcementBoardId="+announcementBoardId+"&"+extraParameters%>">
					Criar Anúncio
				</html:link>
			</li>				
			<%
				}
			%>
	</ul>

	<jsp:include page="/messaging/announcements/listAnnouncements.jsp" flush="true"/>

</logic:present>