<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.TimeTableType" %>
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
						<bean:define id="infoExecutionPeriod" name="<%=SessionConstants.INFO_EXECUTION_PERIOD %>" scope="request"/>
						<bean:write name="infoExecutionPeriod" property="name"/> - 
						<bean:write name="infoExecutionPeriod" property="infoExecutionYear.year"/>
					</strong>
		         </td>
		    </tr>
		</table>
		<br />
		<br />
		<bean:define id="infoRoom" name="viewRoomSchedule" property="infoRoom"/>		
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
	   	<bean:define id="lessons" name="viewRoomSchedule" property="roomLessons"/>
		<div align="center"><app:gerarHorario name="lessons" type="<%= TimeTableType.SOP_ROOM_TIMETABLE %>"/></div>
		<br style="page-break-after:always;" />
	</logic:iterate>
</logic:present>
<logic:notPresent name="<%=SessionConstants.ALL_INFO_VIEW_ROOM_SCHEDULE %>" scope="request">
	<span class="error"><bean:message key="message.rooms.notExisting"/></span>
</logic:notPresent>