<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>

<em><bean:message key="label.communicationPortal.header" bundle="MESSAGING_RESOURCES"/></em>
<h2><bean:message key="label.manageBoards" bundle="MESSAGING_RESOURCES"/></h2>

<jsp:include flush="true" page="/messaging/context.jsp"/>

<jsp:include page="/messaging/announcements/listAnnouncementBoards.jsp" flush="true"/>
