<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.TimeTableType" %>
<bean:define id="component" name="siteView" property="component"/>
<bean:define id="infoRoom" name="component" property="infoRoom" />
<bean:define id="lessonList" name="component" property="infoShowOccupation" />
	<div id="invisible"><h2><bean:message key="title.info.room"/></h2></div>
	<br/> 
<html:form action="/viewRoom">
	<bean:define id="room" name="infoRoom" property="nome"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="roomViewer"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.roomName" property="roomName" value="<%=room.toString()%>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>

	<table border="0" cellspacing="0" cellpadding="0">
		<tr>
		    <td nowrap="nowrap" width="125"><bean:message key="property.execution.period"/>:</td>
		    <td nowrap="nowrap">
		        <html:select bundle="HTMLALT_RESOURCES" altKey="select.selectedExecutionPeriodID" property="selectedExecutionPeriodID" size="1" onchange="this.form.submit();">
   					<html:options property="value" labelProperty="label"
						collection="<%= SessionConstants.LABELLIST_EXECUTIONPERIOD %>" />
				</html:select>
		</tr>
		<tr>
		    <td nowrap="nowrap" width="125"><bean:message key="property.week"/>:</td>
		    <td nowrap="nowrap">
		        <html:select bundle="HTMLALT_RESOURCES" altKey="select.indexWeek" property="indexWeek" size="1" onchange="this.form.submit();">
	    			<html:options property="value" labelProperty="label" 
						collection="<%= SessionConstants.LABELLIST_WEEKS%>" />
				</html:select>
			</td>
		</tr>
	</table>
</html:form>
	<logic:present name="infoRoom" >
       	<table class="tab_complex" cellspacing="1" cellpadding="2">
                <tr>
                    <th><bean:message key="property.room.name" /></th>
                    <th><bean:message key="property.room.type" /></th>
                    <th><bean:message key="property.room.building" /></th>
                    <th><bean:message key="property.room.floor" /></th>
					<th><bean:message key="property.room.capacity.normal" /></th>
					<th><bean:message key="property.room.capacity.exame" /></th>
                </tr>
                <tr>
                    <td ><bean:write name="infoRoom" property="nome" /></td>
                    <td ><bean:write name="infoRoom" property="tipo" /></td>
                    <td ><bean:write name="infoRoom" property="edificio" /></td>
					<td ><bean:write name="infoRoom" property="piso" /></td>
                    <td ><bean:write name="infoRoom" property="capacidadeNormal" /></td>
                    <td ><bean:write name="infoRoom" property="capacidadeExame" /></td>
                </tr>
        </table>		
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