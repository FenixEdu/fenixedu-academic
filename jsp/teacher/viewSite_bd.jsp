<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<logic:present name="<%= SessionConstants.LAST_ANNOUNCEMENT %>" >
	<br><br><br>
	<b><bean:message key="label.lastAnnouncement"/></b><br><br>
	<b><bean:write name="<%= SessionConstants.LAST_ANNOUNCEMENT %>" property="title" /></b><br>
	<bean:write name="<%= SessionConstants.LAST_ANNOUNCEMENT %>" property="information" /><br>
</logic:present>