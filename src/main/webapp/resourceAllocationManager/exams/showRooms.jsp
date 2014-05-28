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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<html:xhtml/>
 
<html:form action="/roomSearch">
		
	<logic:iterate indexId="i" id="room" name="availableRooms" type="net.sourceforge.fenixedu.dataTransferObject.InfoRoom">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.availableRoomsId" property="availableRoomsId" value="<%= room.getExternalId().toString() %>" />
	</logic:iterate>

	<h2>Salas livres dia <bean:write name="<%=PresentationConstants.DATE%>"/>
 		das <bean:write name="<%=PresentationConstants.START_TIME%>"/>
 		às <bean:write name="<%=PresentationConstants.END_TIME%>"/></h2>
 
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="sort"/>
	
	
	<logic:present name="<%=PresentationConstants.AVAILABLE_ROOMS%>">		
		<bean:define id="availableRooms" name="<%=PresentationConstants.AVAILABLE_ROOMS%>"/>
		<table class="tstyle4 showborder">
			<tr>
				<th><html:radio bundle="HTMLALT_RESOURCES" altKey="radio.sortParameter" property="sortParameter" value="name" onclick="this.form.method.value='sort';this.form.submit();"/> Nome</th>
				<th><html:radio bundle="HTMLALT_RESOURCES" altKey="radio.sortParameter" property="sortParameter" value="type" onclick="this.form.method.value='sort';this.form.submit();"/> Tipo</th>
				<th><html:radio bundle="HTMLALT_RESOURCES" altKey="radio.sortParameter" property="sortParameter" value="building" onclick="this.form.method.value='sort';this.form.submit();"/> Edificio</th>
				<th><html:radio bundle="HTMLALT_RESOURCES" altKey="radio.sortParameter" property="sortParameter" value="floor" onclick="this.form.method.value='sort';this.form.submit();"/> Piso</th>
				<th><html:radio bundle="HTMLALT_RESOURCES" altKey="radio.sortParameter" property="sortParameter" value="normal" onclick="this.form.method.value='sort';this.form.submit();"/> Capacidade Normal</th>
				<th><html:radio bundle="HTMLALT_RESOURCES" altKey="radio.sortParameter" property="sortParameter" value="exam" onclick="this.form.method.value='sort';this.form.submit();"/> Capacidade Exame</th>
			</tr>
				<logic:iterate id ="infoRoom" name="availableRooms">
			<tr>
				<td>
					 <bean:write name="infoRoom" property="nome"/>
				</td>
				<td>
					 <bean:write name="infoRoom" property="tipo"/>					 
				</td>
				<td>
					 <bean:write name="infoRoom" property="edificio"/>
				</td>
				<td class="acenter">
					 <bean:write name="infoRoom" property="piso"/>
				</td>
				<td class="aright">
					<bean:write name="infoRoom" property="capacidadeNormal"/> lugares
				</td>
				<td class="aright">
					<bean:write name="infoRoom" property="capacidadeExame"/> lugares
				</td>
			</tr>
				</logic:iterate>
		</table>
	</logic:present>
	<logic:notPresent name="<%=PresentationConstants.AVAILABLE_ROOMS%>">
		Não existem salas disponíveis.
	</logic:notPresent>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.day" property="day" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.month" property="month" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.year" property="year" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.beginningHour" property="beginningHour" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.beginningMinute" property="beginningMinute" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.endHour" property="endHour" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.endMinute" property="endMinute" />

</html:form> 