<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>

<ft:tilesView definition="departmentAdmOffice.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/DepartmentAdmOfficeResources" var="bundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="bundleEnumeration"/>
	
	<h:outputText value="<h2>#{bundle['label.teacherExpectationDefinitionPeriodManagement.title']}</h2>" escape="false" style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;" />
	
	<h:form>
		<fc:viewState binding="#{teacherExpectationDefinitionPeriodManagement.viewState}"/>
		<h:inputHidden value="#{teacherExpectationDefinitionPeriodManagement.selectedExecutionYearID}" />
		<h:inputHidden value="#{teacherExpectationDefinitionPeriodManagement.teacherExpectationDefinitionPeriodID}" />
		
		<h:outputText styleClass="error" rendered="#{!empty teacherExpectationDefinitionPeriodManagement.errorMessage}"
				value="#{bundle[teacherExpectationDefinitionPeriodManagement.errorMessage]}<br/>" escape="false" />
				
	
		<h:panelGrid columns="3" styleClass="infoop">
			<!-- Start date -->
			<h:outputText value="<strong>#{bundle['label.teacherExpectationDefinitionPeriodManagement.startDate']}&nbsp;<i>(#{bundle['label.teacherExpectationDefinitionPeriodManagement.dateInstructions']}):</i></strong>" escape="false"/>
			<h:inputText alt="#{htmlAltBundle['inputText.startDateString']}" id="startDate" required="true" size="10" value="#{teacherExpectationDefinitionPeriodManagement.startDateString}">
				<fc:regexValidator regex="([1-9]|0[1-9]|[12][0-9]|3[01])[/]([1-9]|0[1-9]|1[012])[/](19|20)\d\d"/>
			</h:inputText>
			<h:message for="startDate" styleClass="error" />
			
			<h:outputText value="<strong>#{bundle['label.teacherExpectationDefinitionPeriodManagement.endDate']}&nbsp;<i>(#{bundle['label.teacherExpectationDefinitionPeriodManagement.dateInstructions']}):</i></strong>" escape="false"/>
			<h:inputText alt="#{htmlAltBundle['inputText.endDateString']}" id="endDate" required="true" size="10" value="#{teacherExpectationDefinitionPeriodManagement.endDateString}">
				<fc:regexValidator regex="([1-9]|0[1-9]|[12][0-9]|3[01])[/]([1-9]|0[1-9]|1[012])[/](19|20)\d\d"/>
			</h:inputText>
			<h:message for="endDate" styleClass="error" />
		</h:panelGrid>
		
		<h:outputText value="<br/>" escape="false"/>

		<h:panelGrid columns="2">
			<h:commandButton alt="#{htmlAltBundle['commandButton.update']}" action="#{teacherExpectationDefinitionPeriodManagement.updatePeriod}" value="#{bundle['button.update']}" styleClass="inputbutton" />
			<h:commandButton alt="#{htmlAltBundle['commandButton.cancel']}" action="cancel" value="#{bundle['button.cancel']}" styleClass="inputbutton" immediate="true" />
		</h:panelGrid>		

	</h:form>

</ft:tilesView>