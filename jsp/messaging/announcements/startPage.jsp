<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<em><bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.portal"/></em>
<h2><bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.bookmarked"/></h2>

<jsp:include flush="true" page="/messaging/context.jsp"/>

<div class="mtop2">
<h3 class="mbottom05"><bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.bookmarked.boards"/></h3>
<jsp:include flush="true" page="/messaging/announcements/listBookmarkedAnnouncementBoards.jsp" />
</div>

<logic:notEmpty name="currentExecutionCoursesAnnouncementBoards">
	<h3 class="mbottom05"><bean:message key="label.messaging.my.executionCourse.boards"/></h3>
	<jsp:include page="/messaging/announcements/listCurrentExecutionCoursesAnnouncementBoards.jsp" flush="true"/>
</logic:notEmpty>
