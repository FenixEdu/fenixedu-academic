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
<%@ page import="java.util.ArrayList" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>

<logic:present name="publico.infoRooms" >
  <h2><bean:message key="title.chooseRoom"/></h2>
  <br/>
  <span class="error"><!-- Error messages go here --><html:errors /></span>	
		<html:form action="/viewRoom.do" method="get">
			<html:hidden alt="<%=PresentationConstants.EXECUTION_PERIOD_OID%>" property="<%=PresentationConstants.EXECUTION_PERIOD_OID%>" value="<%= ""+request.getAttribute(PresentationConstants.EXECUTION_PERIOD_OID)%>" />
			
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.name" name="roomForm" property="name"/>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.building" name="roomForm" property="building"/>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.floor" name="roomForm" property="floor"/>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.type" name="roomForm" property="type"/>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.capacityNormal" name="roomForm" property="capacityNormal"/>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.capacityExame" name="roomForm" property="capacityExame"/>

			<table border='0' cellpadding='10' cellspacing='1' width="60%">		
                <tr>
                    <th class="listClasses-header" width="10%">&nbsp;</th>
                    <th class="listClasses-header" width="15%">Nome</th>
                    <th class="listClasses-header" width="10%">Tipo</th>                    
                    <th class="listClasses-header">Capacidade Normal</th>

                </tr>
			
			<logic:iterate id="infoRoom" name="publico.infoRooms" indexId="infoRoomIndex" >
                <tr>
                    <td class="listClasses">
	                    <html:radio bundle="HTMLALT_RESOURCES" altKey="radio.roomName" idName="infoRoom" property="roomName" value="nome"/>
                    </td>
                    <td class="listClasses">
	                    <bean:write name="infoRoom" property="nome"/>
                    </td>
                    <td class="listClasses">
	                    <bean:write name="infoRoom" property="tipo"/>
                    </td>
                    
                    <td class="listClasses">
                    	<bean:write name="infoRoom" property="capacidadeNormal"/>
                    </td>
                </tr>
			</logic:iterate>
		</table>
		<br/>
		<bean:define id="infoExecutionPeriodCode" name="objectCode" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode" property="objectCode" value="<%= infoExecutionPeriodCode.toString() %>" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method"  property="method" value="roomViewer" />	
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
			<bean:message key="label.choose"/>
		</html:submit>
	</html:form>
	</logic:present>
	
	<logic:notPresent name="publico.infoRooms" >
		<table align="center" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td>
					<span class="error"><!-- Error messages go here --><bean:message key="message.public.notfound.rooms"/></span>
				</td>
			</tr>
		</table>
	</logic:notPresent>