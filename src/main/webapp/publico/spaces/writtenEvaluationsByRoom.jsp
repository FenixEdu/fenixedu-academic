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
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-tiles" prefix="ft"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="c"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-fenix" prefix="fc"%>

<ft:tilesView definition="definition.public.findSpaces" attributeName="body-inline">

	<f:loadBundle basename="resources/ResourceAllocationManagerResources" var="bundle"/>				
	
	<h:outputText value="<h1>#{bundle['label.selected.space.written.evaluations']}</h1>" escape="false"/>	
	
	<fc:dataRepeater value="#{publicWrittenEvaluationsByRoom.writtenEvaluationCalendarLinksEntryList}" var="calendarLinks">
	
		<h:panelGroup>
		
			<h:outputText value="<table class='tstyle2 thlight tdcenter'>" escape="false"/>
			<h:outputText value="<tr>" escape="false"/>
				<h:outputText value="<th>#{bundle['property.room.name']}</th>" escape="false"/>
				<h:outputText value="<th>#{bundle['property.room.building']}</th>" escape="false"/>
				<h:outputText value="<th>#{bundle['property.room.floor']}</th>" escape="false"/>
				<h:outputText value="<th>#{bundle['property.room.type']}</th>" escape="false"/>
				<h:outputText value="<th>#{bundle['property.room.capacity.normal']}</th>" escape="false"/>
				<h:outputText value="<th>#{bundle['property.room.capacity.exame']}</th>" escape="false"/>
			<h:outputText value="<tr>" escape="false"/>
			
			<h:outputText value="<tr>" escape="false"/>
				<h:outputText value="<td><strong>#{calendarLinks.key.nome}</strong></td>" escape="false"/>
				<h:outputText value="<td>#{calendarLinks.key.edificio}</td>" escape="false"/>
				<h:outputText value="<td>#{calendarLinks.key.piso}</td>" escape="false"/>
				<h:outputText value="<td>#{calendarLinks.key.tipo}</td>" rendered="#{calendarLinks.key.tipo != null}" escape="false"/>
				<h:outputText value="<td>#{calendarLinks.key.capacidadeNormal}</td>" escape="false"/>
				<h:outputText value="<td>#{calendarLinks.key.capacidadeExame}</td>" escape="false"/>
			<h:outputText value="</tr>" escape="false"/>
			<h:outputText value="</table>" escape="false"/>
			
			<h:outputText value="<br/>" escape="false"/>
			
		 	<fc:fenixCalendar 
		 		begin="#{publicWrittenEvaluationsByRoom.calendarBegin}"	
		 		end="#{publicWrittenEvaluationsByRoom.calendarEnd}"
		 		editLinkPage="#{publicWrittenEvaluationsByRoom.contextPath}/publico/executionCourse.do"		 		
		 		editLinkParameters="#{calendarLinks.value}" />
		 			 			
		</h:panelGroup>
		
	</fc:dataRepeater>
		
</ft:tilesView>
