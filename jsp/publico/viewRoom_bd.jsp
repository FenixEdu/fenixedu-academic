<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="ServidorApresentacao.TagLib.sop.v3.TimeTableType" %>
	<div id="invisible"><h2><bean:message key="title.info.room"/></h2></div>
	<br/> 
	<logic:present name="publico.infoRoom" >
       	<div id="invisible"><table class="invisible" width="90%">
                <tr>
                    <td class="listClasses-header"><bean:message key="property.room.name" /></td>
                    <td class="listClasses-header"><bean:message key="property.room.type" /></td>
                    <td class="listClasses-header"><bean:message key="property.room.building" /></td>
                    <td class="listClasses-header"><bean:message key="property.room.floor" /></td>
					<td class="listClasses-header"><bean:message key="property.room.capacity.normal" /></td>
					<td class="listClasses-header"><bean:message key="property.room.capacity.exame" /></td>
                </tr>
                <tr>
                    <td class="listClasses"><bean:write name="publico.infoRoom" property="nome" /></td>
                    <td class="listClasses"><bean:write name="publico.infoRoom" property="tipo" /></td>
                    <td class="listClasses"><bean:write name="publico.infoRoom" property="edificio" /></td>
					<td class="listClasses"><bean:write name="publico.infoRoom" property="piso" /></td>
                    <td class="listClasses"><bean:write name="publico.infoRoom" property="capacidadeNormal" /></td>
                    <td class="listClasses"><bean:write name="publico.infoRoom" property="capacidadeExame" /></td>
                </tr>
            </table>
		</div>
		<br />
		<br />
	   	<app:gerarHorario name="lessonList" type="<%= TimeTableType.ROOM_TIMETABLE %>"/> 
	</logic:present>
	<logic:notPresent name="publico.infoRoom" >
		<table align="center">
			<tr>
				<td>
					<span class="error"><bean:message key="message.public.notfound.room"/></span>
				</td>
			</tr>
		</table>
	</logic:notPresent>