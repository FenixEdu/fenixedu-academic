<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="ServidorApresentacao.TagLib.sop.v3.TimeTableType" %>
<bean:define id="component" name="siteView" property="component"/>
<bean:define id="infoRoom" name="component" property="infoRoom" />
<bean:define id="lessonList" name="component" property="infoShowOccupation" />
	<div id="invisible"><h2><bean:message key="title.info.room"/></h2></div>
	<br/> 
<html:form action="/viewRoom">
	<table border="0" cellspacing="0" cellpadding="0">
			<tr>
		    <td nowrap="nowrap" width="125"><bean:message key="property.week"/>:</td>
		    <td nowrap="nowrap">
		        <html:select property="indexWeek" size="1">
     				<html:options property="value" 
     					labelProperty="label" 
						collection="<%= SessionConstants.LABELLIST_WEEKS%>" />
				</html:select>
			</td>
			<td width="10"></td>
			<td>
				<bean:define id="room" name="infoRoom" property="nome"/>
				<html:hidden property="method" value="roomViewer"/>
				<html:hidden property="roomName" value="<%=room.toString()%>"/>
				<html:hidden property="page" value="1"/>
			    <html:submit styleClass="inputbutton"><bean:message key="label.choose"/>
          		</html:submit>
			</td>
		</tr>
	</table>
</html:form>
	<logic:present name="infoRoom" >
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
                    <td class="listClasses"><bean:write name="infoRoom" property="nome" /></td>
                    <td class="listClasses"><bean:write name="infoRoom" property="tipo" /></td>
                    <td class="listClasses"><bean:write name="infoRoom" property="edificio" /></td>
					<td class="listClasses"><bean:write name="infoRoom" property="piso" /></td>
                    <td class="listClasses"><bean:write name="infoRoom" property="capacidadeNormal" /></td>
                    <td class="listClasses"><bean:write name="infoRoom" property="capacidadeExame" /></td>
                </tr>
            </table>
		</div>
		<br />
		<br />
	   	<app:gerarHorario name="lessonList" type="<%= TimeTableType.ROOM_TIMETABLE %>"/> 
	</logic:present>
	<logic:notPresent name="infoRoom" >
		<table align="center">
			<tr>
				<td>
					<span class="error"><bean:message key="message.public.notfound.room"/></span>
				</td>
			</tr>
		</table>
	</logic:notPresent>