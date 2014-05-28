<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.TimeTableType" %>
<bean:define id="component" name="siteView" property="component"/>
<bean:define id="infoRoom" name="component" property="infoRoom" />
<bean:define id="lessonList" name="component" property="infoShowOccupation" />

<div id="invisible"><h1><bean:message key="link.view.schedule"/></h1></div>

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
						collection="<%= PresentationConstants.LABELLIST_EXECUTIONPERIOD %>" />
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
						collection="<%= PresentationConstants.LABELLIST_WEEKS%>" />
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
                    <td><strong>
                    		<bean:write name="infoRoom" property="name" />
                    </strong></td>
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