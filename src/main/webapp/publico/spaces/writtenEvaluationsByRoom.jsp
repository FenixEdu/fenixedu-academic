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
				<h:outputText value="<td>#{calendarLinks.key.spaceBuilding.name}</td>" escape="false"/>
				<h:outputText value="<td>#{calendarLinks.key.piso}</td>" escape="false"/>
				<h:outputText value="<td>#{calendarLinks.key.tipo.name}</td>" rendered="#{calendarLinks.key.tipo != null}" escape="false"/>
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