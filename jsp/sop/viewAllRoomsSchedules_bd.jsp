<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>~
<%@ page import="ServidorApresentacao.TagLib.sop.v3.TimeTableType" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<br />
<logic:present name="<%=SessionConstants.ALL_INFO_VIEW_ROOM_SCHEDULE %>" scope="request">
	<logic:iterate id="viewRoomSchedule" name="<%=SessionConstants.ALL_INFO_VIEW_ROOM_SCHEDULE %>" scope="request">
		<table width="100%" cellspacing="0">
			<tr>
				<td class="infoselected">
					<strong>
						<jsp:getProperty name="<%=SessionConstants.INFO_EXECUTION_PERIOD_KEY %>" property="name"/> -
						<bean:write name="<%=SessionConstants.INFO_EXECUTION_PERIOD_KEY %>" property="infoExecutionYear.year"/>
					</strong>
		         </td>
		    </tr>
		</table>

		<bean:define id="infoRoom" name="viewRoomSchedule" property="infoRoom"/>		
		<bean:write name="infoRoom"/>
        <table width="100%">
        	<tr>
            	<td class="listClasses-header">
                	<bean:message key="property.room.name"/>
                </td>
				<td class="listClasses-header">
					<bean:message key="property.room.type"/>
				</td>
				<td class="listClasses-header">
					<bean:message key="property.room.building"/>
				</td>
				<td class="listClasses-header">
					<bean:message key="property.room.floor"/>
				</td>
				<td class="listClasses-header">
					<bean:message key="property.room.capacity.normal"/>
				</td>
				<td class="listClasses-header">
					<bean:message key="property.room.capacity.exame"/>
				</td>
			</tr>
			<tr>
				<td class="listClasses">
					<bean:write name="infoRoom" property="nome"/>
				</td>
				<td class="listClasses">
					<bean:write name="infoRoom" property="tipo"/>
				</td>
				<td class="listClasses">
					<bean:write name="infoRoom" property="edificio"/>
				</td>
				<td class="listClasses">
					<bean:write name="infoRoom" property="piso"/>
				</td>
				<td class="listClasses">
					<bean:write name="infoRoom" property="capacidadeNormal"/>
				</td>
				<td class="listClasses">
					<bean:write name="infoRoom" property="capacidadeExame"/>
				</td>
			</tr>
		</table>
		<br />
		<br />	
	   	<bean:define id="lessons" name="viewClassSchedule" property="classLessons"/>
		<div align="center"><app:gerarHorario name="lessons" type="<%= TimeTableType.SOP_ROOM_TIMETABLE %>"/></div>
		<br style="page-break-before:always;" />
	</logic:iterate>
</logic:present>
<logic:notPresent name="<%=SessionConstants.ALL_INFO_VIEW_ROOM_SCHEDULE %>" scope="request">
	<span class="error"><bean:message key="message.rooms.notExisting"/></span>
</logic:notPresent>