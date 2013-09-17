<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.TimeTableType" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app" %>
<br />
<logic:present name="<%=PresentationConstants.ALL_INFO_VIEW_ROOM_SCHEDULE %>" scope="request">
	<logic:iterate id="viewRoomSchedule" name="<%=PresentationConstants.ALL_INFO_VIEW_ROOM_SCHEDULE %>" scope="request" indexId="i">

		<logic:equal name="i" value="0">
			<table width="100%" cellspacing="0">
		</logic:equal>
		<logic:notEqual name="i" value="0">
			<table class="break-before" width="100%" cellspacing="0">
		</logic:notEqual>

		<br/>
		<table width="100%" cellspacing="0">
			<tr>
				<td class="infoselected">
					<strong>
						<bean:write name="academicInterval" property="pathName"/>
					</strong>
		         </td>
		    </tr>
		</table>
		<br /><br />
		
		<bean:define id="infoRoom" name="viewRoomSchedule" property="infoRoom"/>		
        <table width="100%">
        	<tr>
            	<th class="listClasses-header">
                	<bean:message key="property.room.name"/>
                </th>
				<th class="listClasses-header">
					<bean:message key="property.room.type"/>
				</th>
				<th class="listClasses-header">
					<bean:message key="property.room.building"/>
				</th>
				<th class="listClasses-header">
					<bean:message key="property.room.floor"/>
				</th>
				<th class="listClasses-header">
					<bean:message key="property.room.capacity.normal"/>
				</th>
				<th class="listClasses-header">
					<bean:message key="property.room.capacity.exame"/>
				</th>
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
	</logic:iterate>
</logic:present>
<logic:notPresent name="<%=PresentationConstants.ALL_INFO_VIEW_ROOM_SCHEDULE %>" scope="request">
	<span class="error"><!-- Error messages go here --><bean:message key="message.rooms.notExisting"/></span>
</logic:notPresent>