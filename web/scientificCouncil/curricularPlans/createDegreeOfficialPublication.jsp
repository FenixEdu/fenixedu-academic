<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="scientificCouncil.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/ScientificCouncilResources" var="scouncilBundle"/>
	
	<h:outputText value="<i>#{scouncilBundle['scientificCouncil']}</i>" escape="false"/>
	<h:outputFormat value="<h2>#{scouncilBundle['create.param']}</h2>" escape="false">
		<f:param value="#{scouncilBundle['degree.officialPublication']}"/>
	</h:outputFormat>
	<h:form>

		<h:outputText escape="false"
			value="<input alt='input.degreeId' id='degreeId' name='degreeId' type='hidden' value='#{DegreeManagement.degreeId}'/>" />

		<h:outputText escape="false"
		value="<input alt='input.selectedExecutionYearId' id='selectedExecutionYearId' name='selectedExecutionYearId' type='hidden' value='#{DegreeManagement.selectedExecutionYearId}'/>" />
			
		<h:outputText styleClass="error0" rendered="#{!empty DegreeManagement$OfficialPublicationBean.errorMessage}"
			value="#{DegreeManagement.errorMessage}<br/>" escape="false"/>
		<h:messages infoClass="success0" errorClass="error0" layout="table" globalOnly="true"/>			

		
		<h:outputText value="<table class='tstyle5 thlight thright'>" escape="false"/>

		<h:outputText value="<tr>" escape="false"/>
  
		
		<h:outputText value="<th><span ></span> #{scouncilBundle['name']}:</th><td>" escape="false"/>
		<h:panelGroup>
			<h:inputText alt="#{htmlAltBundle['inputText.nameEn']}" id="officialReference" value="#{DegreeManagement.officialPublicationBean.officialReference}" maxlength="50" size="50"/>
			<h:message for="nameEn" errorClass="error0" rendered="#{empty DegreeManagement.errorMessage}"/>
		</h:panelGroup>
				<h:outputText value="</tr>" escape="false"/>
		
				<h:outputText value="<th><span ></span> #{scouncilBundle['label.degree.officialPublication.creation.date']}:</th><td>" escape="false"/>
		<h:panelGroup>
			<h:inputText alt="#{htmlAltBundle['inputText.nameEn']}" id="date" value="#{DegreeManagement.officialPublicationBean.date}" maxlength="100" size="10"/>
			<h:message for="nameEn" errorClass="error0" rendered="#{empty DegreeManagement.errorMessage}"/>
		</h:panelGroup>



<h:outputText value="</table>" escape="false"/>
		
		<h:commandButton alt="#{htmlAltBundle['commandButton.create']}" styleClass="inputbutton" value="#{scouncilBundle['create']}"
			action="#{DegreeManagement.officialPublicationBean.makeAndInsertDegreeOfficialPublication}"/>
		<h:commandButton alt="#{htmlAltBundle['commandButton.cancel']}" immediate="true" styleClass="inputbutton" value="#{scouncilBundle['cancel']}"
			action="editDegree"/>
		<h:outputText value="</p>" escape="false"/>
	</h:form>
</ft:tilesView>
