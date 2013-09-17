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

	<h2>
		<bean:message key="messaging.announcements.title.label" bundle="MESSAGING_RESOURCES"/>
	</h2>

	<div>
	<h3 style="display: inline;"><bean:write name="announcementBoard" property="name"/></h3>
	<span title="Really Simple Syndication">
		 <%
			java.util.Map parameters = new java.util.HashMap();
			parameters.put("method","simple");
			parameters.put("announcementBoardId",announcementBoard.getExternalId());
			request.setAttribute("parameters",parameters);
			%>
			<logic:notPresent name="announcementBoard" property="readers">
			(<html:link module="" action="/external/announcementsRSS" name="parameters">RSS<%--<img src="<%= request.getContextPath() %>/images/rss_ico.png"/>--%></html:link>)
			</logic:notPresent>
	</span>
	</div>
	
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

	<jsp:include page="/teacher/executionCourse/announcements/listAnnouncements.jsp" flush="true"/>

</logic:present>