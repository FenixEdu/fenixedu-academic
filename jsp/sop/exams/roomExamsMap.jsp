<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>

<h2><bean:message key="title.exams.calendar"/></h2>

<logic:iterate id="infoRoomExamsMap" name="<%= SessionConstants.INFO_EXAMS_MAP %>" scope="request">
	<bean:define id="infoRoomExamsMap" name="infoRoomExamsMap" toScope="request"/>
<h3 class="break-before"> Sala <bean:write name="infoRoomExamsMap" property="infoRoom.nome"/> </h3>

<app:generateNewExamsMap name="infoRoomExamsMap" user="sop" mapType="null"/> 

</logic:iterate>