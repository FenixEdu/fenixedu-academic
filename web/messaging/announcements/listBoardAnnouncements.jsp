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

	<em><bean:message key="messaging.announcements.title.label" bundle="MESSAGING_RESOURCES"/></em>
	<h2><bean:write name="announcementBoard" property="name"/>

	<span title="Really Simple Syndication" style="font-weight: normal; font-size: 0.7em;">
		<!--  w3c complient -->
		 <%
			java.util.Map parameters = new java.util.HashMap();
			parameters.put("method","simple");
			parameters.put("announcementBoardId",announcementBoard.getIdInternal());
			request.setAttribute("parameters",parameters);
			%>

			<logic:notPresent name="announcementBoard" property="readers">
			(<html:link module="" action="/external/announcementsRSS" name="parameters">RSS<%--<img src="<%= request.getContextPath() %>/images/rss_ico.png"/>--%></html:link>)
			</logic:notPresent>
	</span>
	
	</h2>

	<ul>
			<logic:equal name="announcementBoard" property="currentUserWriter" value="true">
			<li>
				<html:link action="<%= contextPrefix + "method=addAnnouncement&amp;announcementBoardId="+announcementBoardId+"&amp;"+extraParameters%>">
					<bean:message key="label.createAnnouncement" bundle="MESSAGING_RESOURCES"/>
				</html:link>
			</li>
			<li>
				<html:link action="<%= contextPrefix + "method=prepareAddFile&amp;announcementBoardId="+announcementBoardId+"&amp;"+extraParameters%>">
					<bean:message key="label.files.management" bundle="MESSAGING_RESOURCES"/>
				</html:link>
			</li>
			</logic:equal>
	</ul>



	<jsp:include page="/messaging/announcements/listAnnouncements.jsp" flush="true"/>

</logic:present>