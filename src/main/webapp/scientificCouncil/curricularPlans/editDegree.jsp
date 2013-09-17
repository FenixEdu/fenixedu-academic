<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-tiles" prefix="ft"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-fenix" prefix="fc"%>

<ft:tilesView definition="scientificCouncil.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/ScientificCouncilResources" var="scouncilBundle"/>

	<h:outputText value="<em>#{scouncilBundle['scientificCouncil']}</em>" escape="false"/>
	<h:outputFormat value="<h2>#{scouncilBundle['edit.param']}</h2>" escape="false">
		<f:param value="#{scouncilBundle['degree']}" />
	</h:outputFormat>

	<h:form>
		<h:outputText escape="false" value="<input alt='input.degreeId' id='degreeId' name='degreeId' type='hidden' value='#{DegreeManagement.degreeId}'/>"/>
		<h:outputText escape="false" value="<input alt='input.selectedExecutionYearId' id='selectedExecutionYearId' name='selectedExecutionYearId' type='hidden' value='#{DegreeManagement.selectedExecutionYearId}'/>"/>
		
		<h:outputText styleClass="error0" rendered="#{!empty DegreeManagement.errorMessage}"
			value="#{DegreeManagement.errorMessage}<br/>" escape="false"/>
		<h:messages infoClass="success0" errorClass="error0" layout="table" globalOnly="true"/>			


		<h:outputText value="<p><strong>#{DegreeManagement.degree.nome}</strong></p>" escape="false"/>

		<h:outputText value="<table class='tstyle5 thlight thright'>" escape="false"/>


		<h:outputText value="<th><span class='required'>*</span> #{scouncilBundle['label.execution.year']}</th><td>" escape="false"/>
		<h:panelGroup>
			<h:selectOneMenu id="selectedExecutionYearId" value="#{DegreeManagement.selectedExecutionYearId}"  valueChangeListener="#{DegreeManagement.onChangeExecutionYear}" onchange="this.form.submit()">
				<f:selectItems value="#{DegreeManagement.openExecutionYears}" />
			</h:selectOneMenu>
		</h:panelGroup>
		<h:outputText value="</td>" escape="false"/>
		<h:outputText value="</tr>" escape="false"/>
		<h:outputText value="<tr>" escape="false"/>
		<h:outputText value="<th><span class='required'>*</span> #{scouncilBundle['name']} (pt):</th><td>" escape="false"/>
		<h:panelGroup>
			<h:inputText alt="#{htmlAltBundle['inputText.name']}" id="name" binding="#{DegreeManagement.nameInputComponent}" maxlength="100" size="60" disabled="#{DegreeManagement.ableToEditName == false}"/>
			<h:message for="name" errorClass="error0" rendered="#{empty DegreeManagement.errorMessage}"/>
		</h:panelGroup>
		<h:outputText value="</td>" escape="false"/>
		<h:outputText value="</tr>" escape="false"/>

		<h:outputText value="<tr>" escape="false"/>
		<h:outputText value="<th><span class='required'>*</span> #{scouncilBundle['name']} (en):</th><td>" escape="false"/>
		<h:panelGroup>
			<h:inputText alt="#{htmlAltBundle['inputText.nameEn']}" id="nameEn" binding="#{DegreeManagement.nameEnInputComponent}" maxlength="100" size="60" disabled="#{DegreeManagement.ableToEditName == false}"/>
			<h:message for="nameEn" errorClass="error0" rendered="#{empty DegreeManagement.errorMessage}"/>
		</h:panelGroup>
		<h:outputText value="</td>" escape="false"/>
		<h:outputText value="</tr>" escape="false"/>

		<h:outputText value="</table><br/>" escape="false"/>
		<h:outputText value="<table class='tstyle5 thlight thright'>" escape="false"/>
		
		<h:outputText value="<tr>" escape="false"/>
		<h:outputText value="<th><span class='required'>*</span> #{scouncilBundle['acronym']}:</th><td>" escape="false"/>
		<h:panelGroup>
			<h:inputText alt="#{htmlAltBundle['inputText.acronym']}" id="acronym" value="#{DegreeManagement.acronym}" maxlength="9" size="9"/>
			<h:message for="acronym" errorClass="error0" rendered="#{empty DegreeManagement.errorMessage}"/>
		</h:panelGroup>
		<h:outputText value="</td>" escape="false"/>
		<h:outputText value="</tr>" escape="false"/>

		<h:outputText value="<tr>" escape="false"/>
		<h:outputText value="<th><span class='required'>*</span> #{scouncilBundle['degreeType']}:</th><td>" escape="false"/>
		<h:panelGroup>
			<h:selectOneMenu id="bolonhaDegreeType" value="#{DegreeManagement.bolonhaDegreeType}" onchange="this.form.submit();" disabled="#{DegreeManagement.degree.degreeCurricularPlansCount > 0}">
				<f:selectItems value="#{DegreeManagement.bolonhaDegreeTypes}" />
			</h:selectOneMenu>
			<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'/>" escape="false"/>
			<h:message for="bolonhaDegreeType" errorClass="error0" rendered="#{empty DegreeManagement.errorMessage}"/>
		</h:panelGroup>
		<h:outputText value="</td>" escape="false"/>
		<h:outputText value="</tr>" escape="false"/>

		<h:outputText value="<tr>" escape="false"/>
		<h:outputText value="<th>#{scouncilBundle['ectsCredits']}:</th><td>" escape="false"/>
		<h:panelGroup>
			<h:inputText alt="#{htmlAltBundle['inputText.ectsCredits']}" id="ectsCredits" value="#{DegreeManagement.ectsCredits}" maxlength="5" size="4"/>
			<h:message for="ectsCredits" errorClass="error0" rendered="#{empty DegreeManagement.errorMessage}"/>
		</h:panelGroup>
		<h:outputText value="</td>" escape="false"/>
		<h:outputText value="</tr>" escape="false"/>

		<h:outputText value="<tr>" escape="false"/>
		<h:outputText value="<th>#{scouncilBundle['prevailingScientificArea']}:</th><td>" escape="false"/>
		<h:panelGroup>
			<h:inputText alt="#{htmlAltBundle['inputText.prevailingScientificArea']}" id="prevailingScientificArea" value="#{DegreeManagement.prevailingScientificArea}" maxlength="100" size="60"/>
			<h:message for="prevailingScientificArea" errorClass="error0" rendered="#{empty DegreeManagement.errorMessage}"/>
		</h:panelGroup>
		<h:outputText value="</td>" escape="false"/>
		<h:outputText value="</tr>" escape="false"/>

		<h:outputText value="<tr>" escape="false"/>
		<h:outputText value="<th></th><td><span class='smalltxt'>#{scouncilBundle['mandatory.fields']}</td>" escape="false"/>
		<h:outputText value="</tr>" escape="false"/>

		<h:outputText value="</table>" 
			escape="false" />

		<h:outputText
			value="<p><strong>#{scouncilBundle['label.degree.officialPublications']}</strong></p>"
			escape="false" />
			
			
		
		<h:outputText value="<table class='tstyle5 thlight thright'>"
			escape="false" />
			<h:outputText value="<tr>" escape="false" />
			<h:outputText value="<th> Publicação Oficial</th>" escape="false" />
			<h:outputText value="<th> Data de Publicação</th>" escape="false" />
			<h:outputText value="<th> Area de Especialização</th>" escape="false" />
			<h:outputText value="</tr>" escape="false" />
			
		<fc:dataRepeater value="#{DegreeManagement.officialPublicationsBeanPrettyPrints}"
			var="publicationsPrettyPrint"
			rendered="#{!empty DegreeManagement.officialPublicationsBeanPrettyPrints}" rowIndexVar="index">


			<h:outputText value="<tr>" escape="false" />
			<h:outputText value="<td> #{publicationsPrettyPrint.officialReference}</td>" escape="false"/>
			<h:outputText value="<td> #{publicationsPrettyPrint.date}</td>" escape="false"/>

			<h:outputText value="<td> #{publicationsPrettyPrint.specializationsAreas}</td>" escape="false"/>
			<h:outputText value="<td>" escape="false" />

			<h:outputLink
				value="#{DegreeManagement.request.contextPath}/scientificCouncil/curricularPlans/editOfficialPublication.do">
				<h:outputFormat value="#{scouncilBundle['delete']}" />
				<f:param name="method" value="prepareDeleteDegreeOfficialPublication" />
				<f:param name="officialPubId" value="#{ publicationsPrettyPrint.degreeOfficialPublication.externalId}" />
				<f:param name="degreeId" value="#{ DegreeManagement.degreeId}" />
				<f:param name="selectedExecutionYearId" value="#{ DegreeManagement.selectedExecutionYearId}" />
			</h:outputLink>
			<h:outputText value=", " escape="false"/>
			<h:outputLink
				value="#{DegreeManagement.request.contextPath}/scientificCouncil/curricularPlans/editOfficialPublication.do?method=preparePubs&officialPubId=#{ publicationsPrettyPrint.degreeOfficialPublication.externalId}">
				<h:outputFormat value="#{scouncilBundle['edit']}" />
			</h:outputLink>

			<h:outputText value="</td>" escape="false" />
			<h:outputText value="</tr>" escape="false" />
			
			
		</fc:dataRepeater>
		

		
		<h:outputText value="</table>" escape="false"/>
		
		
			
	
		
<h:outputLink
			value="#{DegreeManagement.request.contextPath}/scientificCouncil/curricularPlans/createDegreeOfficialPublication.faces">
			<h:outputFormat value="#{scouncilBundle['label.create.officialPublication']}"/>
				<f:param name="degreeId" value="#{DegreeManagement.degreeId}" />
				<f:param name="selectedExecutionYearId" value="#{ DegreeManagement.selectedExecutionYearId}" />
		</h:outputLink>	

		<h:outputText value="<p>" escape="false"/>
		<h:outputText value="<br/>" escape="false" />
		<h:commandButton alt="#{htmlAltBundle['commandButton.save']}" styleClass="inputbutton" value="#{scouncilBundle['button.save']}"
			action="#{DegreeManagement.editDegree}" />
		<h:commandButton alt="#{htmlAltBundle['commandButton.cancel']}" immediate="true" styleClass="inputbutton" value="#{scouncilBundle['cancel']}"
			action="curricularPlansManagement"/>
		<h:outputText value="<p>" escape="false"/>

		


	</h:form>

</ft:tilesView>
