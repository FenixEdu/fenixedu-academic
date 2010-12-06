<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>

<ft:tilesView definition="scientificCouncil.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/ScientificCouncilResources" var="scouncilBundle"/>

	<h:outputText value="<em>#{scouncilBundle['scientificCouncil']}</em>" escape="false"/>




	<h:outputText escape="false"
		value="<input alt='input.officialPubId' id='officialPubId' name='officialPubId' type='hidden' value='#{DegreeManagement.officialPublicationBean.officialPubId}'/>" />
	<h:outputText escape="false"
		value="<input alt='input.degreeId' id='degreeId' name='degreeId' type='hidden' value='#{DegreeManagement.degreeId}'/>" />
	<h:outputText escape="false"
		value="<input alt='input.selectedExecutionYearId' id='selectedExecutionYearId' name='selectedExecutionYearId' type='hidden' value='#{DegreeManagement.selectedExecutionYearId}'/>" />



	<h:outputText value="<table class='tstyle5 thlight thright'>"
		escape="false" />
	<h:form>
		<h:outputText escape="false"
			value="<input alt='input.officialPubId' id='officialPubId' name='officialPubId' type='hidden' value='#{DegreeManagement.officialPublicationBean.officialPubId}'/>" />
				<h:outputText escape="false"
		value="<input alt='input.degreeId' id='degreeId' name='degreeId' type='hidden' value='#{DegreeManagement.degreeId}'/>" />
	<h:outputText escape="false"
		value="<input alt='input.selectedExecutionYearId' id='selectedExecutionYearId' name='selectedExecutionYearId' type='hidden' value='#{DegreeManagement.selectedExecutionYearId}'/>" />

		<h:panelGroup>
		<h:outputFormat value="<h2>#{scouncilBundle['edit.param']}</h2>" escape="false">
			<f:param value="#{scouncilBundle['degree.officialPublication']}" />
		</h:outputFormat>


		<h:outputText
			value="<p><strong>#{DegreeManagement.officialPublicationBean.degreeOfficialPublication.officialReference}</strong></p>"
			escape="false" />

		
		</h:panelGroup>
		<h:outputText value="<table class='tstyle5 thlight thright'>" escape="false"/>
			<h:outputText value="<th><span class='required'>*</span> #{scouncilBundle['label.edit.name.officialPublication']}</th><td>" escape="false"/>	

		<h:inputText alt="#{htmlAltBundle['inputText.acronym']}"
			value="#{DegreeManagement.officialPublicationBean.newOfficialReference}"
			maxlength="100" size="50" />
		<h:message for="acronym" errorClass="error0"
			rendered="#{empty DegreeManagement.errorMessage}" />
		<h:outputText value="<p>" escape="false" />
		<h:outputText value="</table>" escape="false"/>
		<h:commandButton alt="#{htmlAltBundle['commandButton.save']}"
			styleClass="inputbutton" value="#{scouncilBundle['button.save']}"
			action="#{DegreeManagement.officialPublicationBean.changeOfficialReference}" />
		<h:commandButton alt="#{htmlAltBundle['commandButton.cancel']}"
			immediate="true" styleClass="inputbutton"
			value="#{scouncilBundle['cancel']}"
			action="curricularPlansManagement" />
		<h:outputText value="<p>" escape="false" />
		
	</h:form>

	<h:form>
		<h:outputText escape="false"
			
		value="<input alt='input.officialPubId' id='officialPubId' name='officialPubId' type='hidden' value='#{DegreeManagement.officialPublicationBean.officialPubId}'/>" />
			<h:outputText escape="false"
		value="<input alt='input.degreeId' id='degreeId' name='degreeId' type='hidden' value='#{DegreeManagement.degreeId}'/>" />
	<h:outputText escape="false"
		value="<input alt='input.selectedExecutionYearId' id='selectedExecutionYearId' name='selectedExecutionYearId' type='hidden' value='#{DegreeManagement.selectedExecutionYearId}'/>" />

		
		<h:outputText value="<table class='tstyle5 thlight thright'>" escape="false" />

		<h:outputText value="<tr>" escape="false" />
		<h:outputText value="<th>#{scouncilBundle['label.name.specializationArea']}</th>" escape="false" />
		<h:outputText value="</tr>" escape="false" />


		<fc:dataRepeater value="#{DegreeManagement.officialPublicationBean.degreeOfficialPublication.specializationArea}"
			var="specializationArea"
			rendered="#{!empty DegreeManagement.officialPublicationBean.degreeOfficialPublication.specializationArea}"
			rowIndexVar="index"> 
			<h:outputText value="<tr>" escape="false" />

			<h:outputText value="<td> #{specializationArea.name}</td>"
				escape="false" />
				
			<h:outputText value="<td>" escape="false" />
			<h:outputLink
				value="#{DegreeManagement.request.contextPath}/scientificCouncil/curricularPlans/deleteDegreeSpecializationArea.faces">
				<h:outputFormat value="#{scouncilBundle['delete']}" />
				<f:param name="specializationId" value="#{specializationArea.externalId}" />
				<f:param name="degreeId" value="#{ DegreeManagement.degreeId}" />
				<f:param name="selectedExecutionYearId" value="#{ DegreeManagement.selectedExecutionYearId}" />
			</h:outputLink>
			<h:outputText value="</td>" escape="false" />
			<h:outputText value="</tr>" escape="false" />

		</fc:dataRepeater>
<h:outputText value="<br/>" escape="false" />
		<h:outputText value="</table>" escape="false" />
		
		<h:outputText value="<p>" escape="false"/>
		<h:outputText value="<br/>" escape="false" />
<h:outputText value="<br/>" escape="false" />

		<h:outputText value="<table class='tstyle5 thlight thright'>"
			escape="false" />
		<h:outputText value="<p><strong>#{scouncilBundle['label.edit.degree.specializationArea']}</strong></p>" escape="false"/>
		<h:outputText value="<tr>" escape="false" />
		<h:outputText value="<th><span class='required'>*</span> #{scouncilBundle['label.name.specializationArea.name']}:</th><td>" escape="false"/>
		<h:panelGroup>
			<h:inputText alt="#{htmlAltBundle['inputText.acronym']}" value="#{DegreeManagement.officialPublicationBean.newSpecializationAreaName}" maxlength="124" size="9"/>
			<h:message for="acronym" errorClass="error0" rendered="#{empty DegreeManagement.errorMessage}"/>
		</h:panelGroup>
<h:outputText value="<br/>" escape="false" />
		<h:outputText value="</tr>" escape="false" />
		<h:outputText value="<tr>" escape="false" />
		<h:outputText value="<th><span class='required'>*</span> #{scouncilBundle['label.name.specializationArea.area']}:</th><td>" escape="false"/>
		<h:panelGroup>
			<h:inputText alt="#{htmlAltBundle['inputText.acronym']}" value="#{DegreeManagement.officialPublicationBean.newSpecializationArea}" maxlength="124" size="9"/>
			<h:message for="acronym" errorClass="error0" rendered="#{empty DegreeManagement.errorMessage}"/>
		</h:panelGroup>
		
		<h:outputText value="</tr>" escape="false" />
		<h:outputText value="<p>" escape="false" />
		<h:outputText value="</table>"
			escape="false" />
		<h:commandButton alt="#{htmlAltBundle['commandButton.save']}" styleClass="inputbutton" value="#{scouncilBundle['button.save']}"
			action="#{DegreeManagement.officialPublicationBean.addSpecializationArea}"/>
		<h:commandButton alt="#{htmlAltBundle['commandButton.cancel']}" immediate="true" styleClass="inputbutton" value="#{scouncilBundle['cancel']}"
			action="curricularPlansManagement"/>
		<h:outputText value="<p>" escape="false"/>

		

	</h:form>
	<h:outputLink
		value="#{DegreeManagement.request.contextPath}/scientificCouncil/curricularPlans/editDegree.faces">
		<h:outputFormat value="#{scouncilBundle['return']}" />
		<f:param name="degreeId" value="#{ DegreeManagement.degreeId}" />
		<f:param name="selectedExecutionYearId" value="#{ DegreeManagement.selectedExecutionYearId}" />
	</h:outputLink>

</ft:tilesView>
