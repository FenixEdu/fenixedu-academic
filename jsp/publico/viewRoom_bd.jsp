<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionConstants" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.TimeTableType" %>
<bean:define id="component" name="siteView" property="component"/>
<bean:define id="infoRoom" name="component" property="infoRoom" />
<bean:define id="lessonList" name="component" property="infoShowOccupation" />

	<div id="invisible"><h1><bean:message key="title.info.room"/></h1></div>

<html:form action="/viewRoom">
	<bean:define id="room" name="infoRoom" property="nome"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="roomViewer"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.roomName" property="roomName" value="<%=room.toString()%>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>

	<table class="mvert15">
		<tr>
		    <td nowrap="nowrap" width="125"><bean:message key="property.execution.period"/>:</td>
		    <td nowrap="nowrap">
		        <html:select bundle="HTMLALT_RESOURCES"  property="selectedExecutionPeriodID" size="1" onchange="this.form.submit();">
   					<html:options property="value" labelProperty="label"
						collection="<%= SessionConstants.LABELLIST_EXECUTIONPERIOD %>" />
				</html:select>
				<html:submit styleId="javascriptButtonID" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
					<bean:message key="button.submit"/>
				</html:submit>
		</tr>
		<tr>
		    <td nowrap="nowrap" width="125"><bean:message key="property.week"/>:</td>
		    <td nowrap="nowrap">
		        <html:select bundle="HTMLALT_RESOURCES" property="indexWeek" size="1" onchange="this.form.submit();">
	    			<html:options property="value" labelProperty="label" 
						collection="<%= SessionConstants.LABELLIST_WEEKS%>" />
				</html:select>
				<html:submit styleId="javascriptButtonID2" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
					<bean:message key="button.submit"/>
				</html:submit>
			</td>
		</tr>
	</table>
</html:form>
	<logic:present name="infoRoom" >
       	<table class="tstyle2 thlight tdcenter mvert1">
                <tr>
                    <th><bean:message key="property.room.name" /></th>
                    <th><bean:message key="property.room.type" /></th>
                    <th><bean:message key="property.room.building" /></th>
                    <th><bean:message key="property.room.floor" /></th>
					<th><bean:message key="property.room.capacity.normal" /></th>
					<th><bean:message key="property.room.capacity.exame" /></th>
                </tr>
                <tr>
                    <td><strong><bean:write name="infoRoom" property="nome" /></strong></td>
                    <td><bean:write name="infoRoom" property="tipo" /></td>
                    <td><bean:write name="infoRoom" property="edificio" /></td>
					<td><bean:write name="infoRoom" property="piso" /></td>
                    <td><bean:write name="infoRoom" property="capacidadeNormal" /></td>
                    <td><bean:write name="infoRoom" property="capacidadeExame" /></td>
                </tr>
        </table>		
		<br />
	   	<app:gerarHorario name="lessonList" type="<%= TimeTableType.ROOM_TIMETABLE %>"/> 
	</logic:present>
	<logic:notPresent name="infoRoom" >
		<table align="center">
			<tr>
				<td>
					<span class="error"><!-- Error messages go here --><bean:message key="message.public.notfound.room"/></span>
				</td>
			</tr>
		</table>
	</logic:notPresent>