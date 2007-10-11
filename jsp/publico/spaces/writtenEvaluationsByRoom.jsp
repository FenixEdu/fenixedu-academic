<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>

<ft:tilesView definition="definition.public.findSpaces" attributeName="body-inline">

	<f:loadBundle basename="resources/ResourceAllocationManagerResources" var="bundle"/>				
	
	<h:outputText value="<h2>#{bundle['label.selected.space.written.evaluations']}</h2>" escape="false"/>	
	
	<fc:dataRepeater value="#{publicWrittenEvaluationsByRoom.writtenEvaluationCalendarLinksEntryList}" var="calendarLinks">
	
		<h:panelGroup>
		
			<h:outputText value="<table class='tstyle4 tdcenter'>" escape="false"/>
			<h:outputText value="<tr>" escape="false"/>
				<h:outputText value="<th>#{bundle['property.room.name']}</th>" escape="false"/>
				<h:outputText value="<th>#{bundle['property.room.building']}</th>" escape="false"/>
				<h:outputText value="<th>#{bundle['property.room.floor']}</th>" escape="false"/>
				<h:outputText value="<th>#{bundle['property.room.type']}</th>" escape="false"/>
				<h:outputText value="<th>#{bundle['property.room.capacity.normal']}</th>" escape="false"/>
				<h:outputText value="<th>#{bundle['property.room.capacity.exame']}</th>" escape="false"/>
			<h:outputText value="<tr>" escape="false"/>
			
			<h:outputText value="<tr>" escape="false"/>
				<h:outputText value="<td>#{calendarLinks.key.nome}</td>" escape="false"/>
				<h:outputText value="<td>#{calendarLinks.key.spaceBuilding.name}</td>" escape="false"/>
				<h:outputText value="<td>#{calendarLinks.key.piso}</td>" escape="false"/>
				<h:outputText value="<td>#{calendarLinks.key.tipo.name}</td>" rendered="#{calendarLinks.key.tipo != null}" escape="false"/>
				<h:outputText value="<td>#{calendarLinks.key.capacidadeNormal}</td>" escape="false"/>
				<h:outputText value="<td>#{calendarLinks.key.capacidadeExame}</td>" escape="false"/>
			<h:outputText value="</tr>" escape="false"/>
			<h:outputText value="</table>" escape="false"/>
			
		 	<fc:fenixCalendar 
		 		begin="#{publicWrittenEvaluationsByRoom.calendarBegin}"	
		 		end="#{publicWrittenEvaluationsByRoom.calendarEnd}"
		 		editLinkPage="#{publicWrittenEvaluationsByRoom.contextPath}/publico/executionCourse.do"		 		
		 		editLinkParameters="#{calendarLinks.value}" />
		 			 			
		</h:panelGroup>
		
	</fc:dataRepeater>
		
</ft:tilesView>