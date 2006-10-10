<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>

<em>Portal de Comunicação</em>
<h2>Favoritos</h2>

<jsp:include flush="true" page="/messaging/context.jsp"/>

<h3 class="mtop2 mbottom05">Canais Favoritos</h3>
<jsp:include page="/messaging/announcements/listBookmarkedAnnouncementBoards.jsp" flush="true"/>

<h3 class="mtop2 mbottom05">Canais Institucionais</h3>
<jsp:include page="/messaging/announcements/listMandatoryAnnouncementBoards.jsp" flush="true"/>

<logic:notEmpty name="currentExecutionCoursesAnnouncementBoards">
	<h3 class="mtop2 mbottom05">Canais das Disciplinas em Curso</h3>
	<jsp:include page="/messaging/announcements/listCurrentExecutionCoursesAnnouncementBoards.jsp" flush="true"/>
</logic:notEmpty>
