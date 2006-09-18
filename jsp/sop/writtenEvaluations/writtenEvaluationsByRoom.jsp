<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>

<ft:tilesView definition="definition.sop.examsPage" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>

<style type="text/css">@import url(<%= request.getContextPath() %>/CSS/dotist_calendars.css);</style>

<style type="text/css">
.greyBorderClass {
	background-color: #EBECED;
	border-style: solid;
	border-width: 1px;
	border-color: #909090;
	width: 100%
}
.blackBorderClass {
	background-color: #ffffff;
	border-style: solid;
	border-width: 1px;
	border-color: #909090
}
.boldFontClass { 
	font-weight: bold
}
.listClasses-header	{
	background: #ccc;
	font-weight: bold;
	text-align: center;
	padding: 5px;
}
.listClasses	{
	background: #EBECED;
	border: 1px solid #ccc;
	text-align: center;
	height: 30px;
}
</style>

	<f:loadBundle basename="resources/ApplicationResourcesSOP" var="bundle"/>
	<h:outputFormat value="<h2>#{bundle['link.writtenEvaluation.by.room']}</h2>" escape="false"/>

	<h:panelGroup rendered="#{writtenEvaluationsByRoom.roomsToDisplayMap == null}">
	<h:outputFormat value="<p>#{bundle['message.writtenEvaluation.by.room']}</p>" escape="false"/>
	<h:form>
		<h:outputText escape="false" value="<input alt='input.submittedForm' id='submittedForm' name='submittedForm' type='hidden' value='true'/>"/>
		<h:outputText escape="false" value="<input alt='input.executionPeriodOID' id='executionPeriodOID' name='executionPeriodOID' type='hidden' value='#{writtenEvaluationsByRoom.executionPeriodOID}'/>"/>

		<h:panelGrid columns="2">
			<h:outputText value="#{bundle['property.room.name']}"/>
			<h:inputText alt="#{htmlAltBundle['inputText.name']}" value="#{writtenEvaluationsByRoom.name}"/>

			<h:outputText value="#{bundle['property.room.building']}"/>
			<h:selectOneMenu id="building" value="#{writtenEvaluationsByRoom.building}">
				<f:selectItem itemLabel="" itemValue=""/>
				<f:selectItems value="#{writtenEvaluationsByRoom.buildingSelectItems}"/>
			</h:selectOneMenu>

			<h:outputText value="#{bundle['property.room.floor']}"/>
			<h:inputText alt="#{htmlAltBundle['inputText.floor']}" value="#{writtenEvaluationsByRoom.floor}" size="3"/>

			<h:outputText value="#{bundle['property.room.type']}"/>
			<h:selectOneMenu id="type" value="#{writtenEvaluationsByRoom.type}">
				<f:selectItem itemLabel="" itemValue=""/>
				<f:selectItems value="#{writtenEvaluationsByRoom.roomTypeSelectItems}"/>
			</h:selectOneMenu>

			<h:outputText value="#{bundle['property.room.capacity.normal']}"/>
			<h:inputText alt="#{htmlAltBundle['inputText.normalCapacity']}" value="#{writtenEvaluationsByRoom.normalCapacity}" size="3"/>

			<h:outputText value="#{bundle['property.room.capacity.exame']}"/>
			<h:inputText alt="#{htmlAltBundle['inputText.examCapacity']}" value="#{writtenEvaluationsByRoom.examCapacity}" size="3"/>

			<h:commandButton alt="#{htmlAltBundle['commandButton.search']}" styleClass="inputbutton" value="#{bundle['label.search']}"/>
		</h:panelGrid>
	</h:form>
	<h:outputFormat value="<br/>" escape="false"/>
	<h:panelGroup rendered="#{writtenEvaluationsByRoom.rooms != null}">
	<h:form>
		<h:outputText escape="false" value="<input alt='input.executionPeriodOID' id='executionPeriodOID' name='executionPeriodOID' type='hidden' value='#{writtenEvaluationsByRoom.executionPeriodOID}'/>"/>
		<h:dataTable value="#{writtenEvaluationsByRoom.rooms}" var="room"
				headerClass="listClasses-header" rowClasses="listClasses" width="80%">
			<h:column>
				<f:facet name="header">
				</f:facet>
				<h:outputText escape="false" value="<input alt='input.selectedRoomIDs' id='selectedRoomIDs' name='selectedRoomIDs' type='checkbox' value='#{room.idInternal}'/>"/>
			</h:column>
			<h:column>
				<f:facet name="header">
				<h:outputText value="#{bundle['property.room.name']}"/>
				</f:facet>
				<h:outputText value="#{room.nome}"/>
			</h:column>
			<h:column>
				<f:facet name="header">
				<h:outputText value="#{bundle['property.room.building']}"/>
				</f:facet>
				<h:outputText value="#{room.building.name}"/>
			</h:column>
			<h:column>
				<f:facet name="header">
				<h:outputText value="#{bundle['property.room.floor']}"/>
				</f:facet>
				<h:outputText value="#{room.piso}"/>
			</h:column>
			<h:column>
				<f:facet name="header">
				<h:outputText value="#{bundle['property.room.type']}"/>
				</f:facet>
				<h:outputText value="#{room.tipo}"/>
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

	<fc:dataRepeater value="#{writtenEvaluationsByRoom.writtenEvaluationCalendarLinksEntryList}" var="calendarLinks">
		<h:panelGroup>
			<h:outputFormat value="<h3 class='break-before'> </h3>" escape="false"/>
			<h:panelGrid columns="6" rowClasses="listClasses-header, listClasses" width="80%">
				<h:outputText value="#{bundle['property.room.name']}"/>
				<h:outputText value="#{bundle['property.room.building']}"/>
				<h:outputText value="#{bundle['property.room.floor']}"/>
				<h:outputText value="#{bundle['property.room.type']}"/>
				<h:outputText value="#{bundle['property.room.capacity.normal']}"/>
				<h:outputText value="#{bundle['property.room.capacity.exame']}"/>

				<h:outputText value="#{calendarLinks.key.nome}"/>
				<h:outputText value="#{calendarLinks.key.building.name}"/>
				<h:outputText value="#{calendarLinks.key.piso}"/>
				<h:outputText value="#{calendarLinks.key.tipo}"/>
				<h:outputText value="#{calendarLinks.key.capacidadeNormal}"/>
				<h:outputText value="#{calendarLinks.key.capacidadeExame}"/>
			</h:panelGrid>
			<h:outputFormat value="<br/>" escape="false"/>
		 	<fc:fenixCalendar 
			 		begin="#{writtenEvaluationsByRoom.calendarBegin}" 
		 			end="#{writtenEvaluationsByRoom.calendarEnd}"
					editLinkPage="editWrittenTest.faces"
		 			editLinkParameters="#{calendarLinks.value}"
			 		/>
		</h:panelGroup>
	</fc:dataRepeater>

</ft:tilesView>