<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>

<ft:tilesView definition="scientificCouncil.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/ScientificCouncilResources" var="scouncilBundle"/>

	<h:outputText value="<em>#{scouncilBundle['scientificCouncil']}</em>" escape="false"/>
		<h:outputText value="<em>#{scouncilBundle['scientificCouncil']}</em>" escape="false"/>
			<h:outputText value="<em>#{scouncilBundle['scientificCouncil']}</em>" escape="false"/>
	
	
	
	<h:form>
		<h:outputText escape="false" value="<input alt='input.officialPubId' id='officialPubId' name='officialPubId' type='hidden' value='#{DegreeManagement.officialPublicationBean.officialPubId}'/>"/>

	
		<h:outputFormat value="<h2>#{scouncilBundle['edit.param']}</h2>" escape="false">
		<f:param value="#{DegreeManagement.officialPublicationBean.degreeOfficialPublication.officialReference}" />
		</h:outputFormat>
		
				<h:outputText value="<table class='tstyle5 thlight thright'>"
			escape="false" />
		<fc:dataRepeater value="#{DegreeManagement.officialPublicationBean.degreeOfficialPublication.specializationArea}"
			var="specializationArea"
			rendered="#{!empty DegreeManagement.officialPublicationBean.degreeOfficialPublication.specializationArea}"
			rowIndexVar="index">
			<h:outputText value="</td>" escape="false"/>
		<h:outputText value="</tr>" escape="false"/>

		<h:outputText value="<tr>" escape="false"/>
			<h:outputText value="</td>" escape="false"/>
				<h:outputText value="</tr>" escape="false"/>
			<h:outputText value="<td> #{specializationArea.name}</td>"
				escape="false" />

		</fc:dataRepeater>

		<h:outputText value="</table>" escape="false" />
		
		<h:outputText value="<p>" escape="false"/>
		
		
		<h:outputText value="<th><span class='required'>*</span> #{scouncilBundle['acronym']}:</th><td>" escape="false"/>
		<h:panelGroup>
			<h:inputText alt="#{htmlAltBundle['inputText.acronym']}" value="#{DegreeManagement.officialPublicationBean.newOfficialReferenceName}" maxlength="9" size="9"/>
			<h:message for="acronym" errorClass="error0" rendered="#{empty DegreeManagement.errorMessage}"/>
		</h:panelGroup>
		
		<h:outputText value="<th><span class='required'>*</span> #{scouncilBundle['acronym']}:</th><td>" escape="false"/>
		<h:panelGroup>
			<h:inputText alt="#{htmlAltBundle['inputText.acronym']}" value="#{DegreeManagement.officialPublicationBean.newOfficialReferenceArea}" maxlength="9" size="9"/>
			<h:message for="acronym" errorClass="error0" rendered="#{empty DegreeManagement.errorMessage}"/>
		</h:panelGroup>
		
		<h:commandButton alt="#{htmlAltBundle['commandButton.save']}" styleClass="inputbutton" value="#{scouncilBundle['button.save']}"
			action="#{DegreeManagement.officialPublicationBean.addOfficialReference}"/>
		<h:commandButton alt="#{htmlAltBundle['commandButton.cancel']}" immediate="true" styleClass="inputbutton" value="#{scouncilBundle['cancel']}"
			action="curricularPlansManagement"/>
		<h:outputText value="<p>" escape="false"/>

		<h:outputLink
			value="#{DegreeManagement.request.contextPath}/scientificCouncil/curricularPlans/createDegreeOfficialPublication.faces">
			<h:outputFormat value="#{scouncilBundle['delete']}"/>
				<f:param name="degreeId" value="#{DegreeManagement.degreeId}" />
		</h:outputLink>


	</h:form>

</ft:tilesView>
