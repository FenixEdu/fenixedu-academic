<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
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

	<h2>
		<bean:message key="messaging.announcements.title.label" bundle="MESSAGING_RESOURCES"/>
	</h2>

	<div>
	<h3 style="display: inline;"><bean:write name="announcementBoard" property="name"/></h3>
	<span title="Really Simple Syndication">
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
	</div>
	
	<ul>
			<%
				if (announcementBoard.hasWriter(person))
				{
			%>
			<li>
				<html:link action="<%= contextPrefix + "method=addAnnouncement&amp;announcementBoardId="+announcementBoardId+"&amp;"+extraParameters%>">
					<bean:message key="label.createAnnouncement" bundle="MESSAGING_RESOURCES"/>
				</html:link>
			</li>				
			<%
				}
			%>
	</ul>

	<jsp:include page="/teacher/executionCourse/announcements/listAnnouncements.jsp" flush="true"/>

</logic:present>