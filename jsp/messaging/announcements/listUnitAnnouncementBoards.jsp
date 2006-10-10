<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>

<em>Portal de Comunicação</em>
<h2>Gestão de Boards</h2>

<jsp:include flush="true" page="/messaging/context.jsp"/>

<jsp:include page="/messaging/announcements/listAnnouncementBoards.jsp" flush="true"/>
