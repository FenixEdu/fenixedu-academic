<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>

<ft:tilesView definition="definition.sop.examsPage" attributeName="body-inline">
			
	<script type="text/javascript">
		<!--
			function setCheckBoxValue(value) {
				elements = document.getElementsByTagName('input');
				for (i = 0; i < elements.length; i++) {
					if (elements[i].type == 'checkbox') {
						elements[i].checked = value;	
					}
				}
			}
		//-->
	</script>		
	
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/ResourceAllocationManagerResources" var="bundle"/>
	
	<h:outputFormat value="<em class='printhidden'>#{bundle['link.writtenEvaluationManagement']}</em>" escape="false"/>
	<h:outputFormat value="<h2 class='printhidden'>#{bundle['link.writtenEvaluation.by.room']}</h2>" escape="false"/>
				
	<h:panelGroup rendered="#{writtenEvaluationsByRoom.roomsToDisplayMap == null}">	
		
		<h:outputFormat value="<p>#{bundle['message.writtenEvaluation.by.room']}</p>" escape="false"/>
		
		<h:form>
			<h:outputText escape="false" value="<input alt='input.submittedForm' id='submittedForm' name='submittedForm' type='hidden' value='true'/>"/>
			<h:outputText escape="false" value="<input alt='input.executionPeriodOID' id='executionPeriodOID' name='executionPeriodOID' type='hidden' value='#{writtenEvaluationsByRoom.executionPeriodOID}'/>"/>
	
			<h:outputText value="<table class='tstyle5 thlight thright thmiddle mtop05 mbottom05'>" escape="false"/>
			<h:outputText value="<tr><th>" escape="false"/>
				<h:outputText value="#{bundle['property.room.name']}:"/>
			<h:outputText value="</th><td>" escape="false"/>
				<h:inputText alt="#{htmlAltBundle['inputText.name']}" value="#{writtenEvaluationsByRoom.name}"/>
			<h:outputText value="</td></tr>" escape="false"/>
	
			<h:outputText value="<tr><th>" escape="false"/>
				<h:outputText value="#{bundle['property.room.building']}:"/>
			<h:outputText value="</th><td>" escape="false"/>
				<h:selectOneMenu id="building" value="#{writtenEvaluationsByRoom.building}">
					<f:selectItem itemLabel="" itemValue=""/>
					<f:selectItems value="#{writtenEvaluationsByRoom.buildingSelectItems}"/>
				</h:selectOneMenu>
			<h:outputText value="</td></tr>" escape="false"/>
	
			<h:outputText value="<tr><th>" escape="false"/>
				<h:outputText value="#{bundle['property.room.floor']}:"/>
			<h:outputText value="</th><td>" escape="false"/>
				<h:inputText alt="#{htmlAltBundle['inputText.floor']}" value="#{writtenEvaluationsByRoom.floor}" size="3"/>
			<h:outputText value="</td></tr>" escape="false"/>
			
			<h:outputText value="<tr><th>" escape="false"/>
				<h:outputText value="#{bundle['property.room.type']}:"/>
			<h:outputText value="</th><td>" escape="false"/>	
				<h:selectOneMenu id="type" value="#{writtenEvaluationsByRoom.type}">
					<f:selectItem itemLabel="" itemValue=""/>
					<f:selectItems value="#{writtenEvaluationsByRoom.roomTypeSelectItems}"/>
				</h:selectOneMenu>
			<h:outputText value="</td></tr>" escape="false"/>
			
			<h:outputText value="<tr><th>" escape="false"/>
				<h:outputText value="#{bundle['property.room.capacity.normal']}:"/>
			<h:outputText value="</th><td>" escape="false"/>
				<h:inputText alt="#{htmlAltBundle['inputText.normalCapacity']}" value="#{writtenEvaluationsByRoom.normalCapacity}" size="3"/>
			<h:outputText value="</td></tr>" escape="false"/>
			
			<h:outputText value="<tr><th>" escape="false"/>
				<h:outputText value="#{bundle['property.room.capacity.exame']}:"/>
			<h:outputText value="</th><td>" escape="false"/>
				<h:inputText alt="#{htmlAltBundle['inputText.examCapacity']}" value="#{writtenEvaluationsByRoom.examCapacity}" size="3"/>
			<h:outputText value="</td></tr>" escape="false"/>
	
			<h:outputText value="<tr><th>" escape="false"/>
				<h:outputText value="#{bundle['property.startDate']}:"/>
			<h:outputText value="</th><td>" escape="false"/>
				<h:panelGroup>
					<h:inputText alt="#{htmlAltBundle['inputText.start.date']}" value="#{writtenEvaluationsByRoom.startDate}" size="10"/>
					<h:outputText value=" dd/MM/yyyy"/>
				</h:panelGroup>
			<h:outputText value="</td></tr>" escape="false"/>
			
			<h:outputText value="<tr><th>" escape="false"/>
				<h:outputText value="#{bundle['property.endDate']}:"/>
			<h:outputText value="</th><td>" escape="false"/>
				<h:panelGroup>
					<h:inputText alt="#{htmlAltBundle['inputText.start.date']}" value="#{writtenEvaluationsByRoom.endDate}" size="10"/>
					<h:outputText value=" dd/MM/yyyy"/>
				</h:panelGroup>
			<h:outputText value="</td></tr>" escape="false"/>
			
			<h:outputText value="</table>" escape="false"/>
			
			<h:outputText value="<p class='mtop05'>" escape="false"/>
				<h:commandButton alt="#{htmlAltBundle['commandButton.search']}" styleClass="inputbutton" value="#{bundle['label.search']}"/>
			<h:outputText value="</p>" escape="false"/>
		</h:form>
		

		<h:panelGroup rendered="#{writtenEvaluationsByRoom.rooms != null}">
			
			<h:outputFormat value="<p class='mtop15'>#{bundle['label.founded.rooms']}</p>" escape="false" />		
			
			<h:form id="form">							
							
				<h:outputText escape="false" value='<p><a href="javascript:setCheckBoxValue(true)"> #{bundle["button.selectAll"]}</a>' />
				<h:outputText escape="false" value=' | '/>						
				<h:outputText escape="false" value='<a href="javascript:setCheckBoxValue(false)"> #{bundle["button.selectNone"]}</a></p>' />				
					
				<h:outputText escape="false" value="<input alt='input.executionPeriodOID' id='executionPeriodOID' name='executionPeriodOID' type='hidden' value='#{writtenEvaluationsByRoom.executionPeriodOID}'/>"/>
				<h:outputText escape="false" value="<input alt='input.startDate' id='startDate' name='startDate' type='hidden' value='#{writtenEvaluationsByRoom.startDate}'/>"/>
				<h:outputText escape="false" value="<input alt='input.endDate' id='endDate' name='endDate' type='hidden' value='#{writtenEvaluationsByRoom.endDate}'/>"/>
											
				<h:dataTable value="#{writtenEvaluationsByRoom.rooms}" var="room" styleClass="tstyle4 tdcenter">
					<h:column>
						<f:facet name="header"></f:facet>
						<h:outputText escape="false" value="<input alt='input.selectedRoomIDs' id='selectedRoomIDs' name='selectedRoomIDs' type='checkbox' value='#{room.idInternal}'/>"/>
					</h:column>
					<h:column>
						<f:facet name="header">
							<h:outputText value="#{bundle['property.room.name']}"/>
						</f:facet>
						<h:outputText value="#{room.spaceInformation.identification}"/>
					</h:column>
					<h:column>
						<f:facet name="header">
							<h:outputText value="#{bundle['property.room.building']}"/>
						</f:facet>
						<h:outputText value="#{room.spaceBuilding.spaceInformation.name}"/>
					</h:column>
					<h:column>
						<f:facet name="header">
							<h:outputText value="#{bundle['property.room.floor']}"/>
						</f:facet>
						<h:outputText value="#{room.spaceFloor.spaceInformation.presentationName}"/>
					</h:column>
					<h:column>
						<f:facet name="header">
							<h:outputText value="#{bundle['property.room.type']}"/>
						</f:facet>
						<h:outputText value="#{room.tipo.name}" rendered="#{room.tipo != null}"/>
					</h:column>
					<h:column>
						<f:facet name="header">
							<h:outputText value="#{bundle['property.room.capacity.normal']}"/>
						</f:facet>
						<h:outputText value="#{room.capacidadeNormal}"/>
					</h:column>
					<h:column>
						<f:facet name="header">
							<h:outputText value="#{bundle['property.room.capacity.exame']}"/>
						</f:facet>
						<h:outputText value="#{room.capacidadeExame}"/>
					</h:column>
				</h:dataTable>
				<h:commandButton alt="#{htmlAltBundle['commandButton.search']}" rendered="#{writtenEvaluationsByRoom.rooms != null}" styleClass="inputbutton" value="#{bundle['label.search']}"/>
			</h:form>
		</h:panelGroup>
	</h:panelGroup>	
				
	<fc:dataRepeater value="#{writtenEvaluationsByRoom.writtenEvaluationCalendarLinksEntryList}" var="calendarLinks" rowIndexVar="index">
		<h:panelGroup>

			<h:outputText rendered="#{index == 0}" value="<table class='tstyle4 tdcenter'>" escape="false"/>
			<h:outputText rendered="#{index > 0}" value="<table class='tstyle4 tdcenter break-before'>" escape="false"/>			
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
			 		begin="#{writtenEvaluationsByRoom.calendarBegin}" 
		 			end="#{writtenEvaluationsByRoom.calendarEnd}"
					editLinkPage="editWrittenTest.faces"
		 			editLinkParameters="#{calendarLinks.value}"
		 			extraLines="true"/>
		</h:panelGroup>
	</fc:dataRepeater>
		
</ft:tilesView>