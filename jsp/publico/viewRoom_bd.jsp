<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="ServidorApresentacao.TagLib.sop.v3.TimeTableType" %>
<bean:define id="component" name="siteView" property="component"/>
<bean:define id="infoRoom" name="component" property="infoRoom" />
<bean:define id="lessonList" name="component" property="infoLessons" />
<bean:define id="executionLabel" name="<%= SessionConstants.EXECUTION_PERIOD %>" scope="request" />
	
<h2 class="greytxt">
	<bean:write name="executionLabel" property="semester" /><bean:message key="label.ordinal.semester.abbr" />
	<bean:write name="executionLabel" property="infoExecutionYear.year" />
</h2>


<logic:present name="infoRoom" >	
	<h2>
		<bean:message key="property.room" />:
		<bean:write name="infoRoom" property="nome" />
	</h2>
		
	<html:hidden property="<%= SessionConstants.EXECUTION_PERIOD %>" value="<%=""+ pageContext.findAttribute("executionLabel") %>" />
	<table class="tab_simple" width="70%" cellspacing="0" cellpadding="2">
		<tr>
			<td class="subheader"><bean:message key="property.room.name" /></td>
			<td class="subheader"><bean:message key="property.room.type" /></td>
             <td class="subheader"><bean:message key="property.room.building" /></td>
             <td class="subheader"><bean:message key="property.room.floor" /></td>
			<td class="subheader"><bean:message key="property.room.capacity.normal" /></td>
			<td class="subheader"><bean:message key="property.room.capacity.exame" /></td>
		</tr>
		<tr>
			<td><bean:write name="infoRoom" property="nome" /></td>
			<td><bean:write name="infoRoom" property="tipo" /></td>
             <td><bean:write name="infoRoom" property="edificio" /></td>
			<td><bean:write name="infoRoom" property="piso" /></td>
             <td><bean:write name="infoRoom" property="capacidadeNormal" /></td>
			<td><bean:write name="infoRoom" property="capacidadeExame" /></td>
		</tr> 
	</table>
		
	<app:gerarHorario name="lessonList" type="<%= TimeTableType.ROOM_TIMETABLE %>"/> 
</logic:present>
	
<logic:notPresent name="infoRoom" >
	<h2><bean:message key="message.public.notfound.room"/></h2>
</logic:notPresent>
