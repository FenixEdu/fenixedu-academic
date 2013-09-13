<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-tiles" prefix="ft"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-fenix" prefix="fc"%>

<ft:tilesView definition="scientificCouncil.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/ScientificCouncilResources" var="scouncilBundle"/>

	<h:outputText value="<em>#{scouncilBundle['scientificCouncil']}</em>" escape="false"/>



	<h:outputFormat value="<h2>#{scouncilBundle['delete.param']}</h2>"
		escape="false">
		<f:param value="#{scouncilBundle['label.name.specializationArea']}" />
	</h:outputFormat>
	<h:form>
		<h:outputText escape="false" value="<input alt='input.specializationId' id='specializationId' name='specializationId' type='hidden' value='#{DegreeManagement.officialPublicationBean.specializationIdToDelete}'/>"/>
			<h:outputText escape="false"
		value="<input alt='input.degreeId' id='degreeId' name='degreeId' type='hidden' value='#{DegreeManagement.degreeId}'/>" />
	<h:outputText escape="false"
		value="<input alt='input.selectedExecutionYearId' id='selectedExecutionYearId' name='selectedExecutionYearId' type='hidden' value='#{DegreeManagement.selectedExecutionYearId}'/>" />

		<h:messages infoClass="success0" errorClass="error0" layout="table" globalOnly="true"/>

		<h:outputText value="<div class='infoop2'/>" escape="false"/>
		<h:outputText value="<p>#{scouncilBundle['label.name.specializationArea.name']}: " escape="false"/>
		<h:outputText value="<b>#{DegreeManagement.officialPublicationBean.specializationAreaToDelete.name}</b></p>" escape="false"/>


		<h:outputText value="<p class='mtop2'>" escape="false"/>
		<h:commandButton alt="#{htmlAltBundle['commandButton.confirm']}"
			styleClass="inputbutton" value="#{scouncilBundle['confirm']}"
			action="#{DegreeManagement.officialPublicationBean.removeSpecializationAreaToDelete}"
			onclick="return confirm('#{scouncilBundle['confirm.delete.specializationArea']}')" />
		<h:commandButton alt="#{htmlAltBundle['commandButton.cancel']}"
			immediate="true" styleClass="inputbutton"
			value="#{scouncilBundle['cancel']}"
			action="curricularPlansManagement" />
		<h:outputText value="</p>" escape="false"/>

		<h:outputLink
			value="#{DegreeManagement.request.contextPath}/scientificCouncil/curricularPlans/editDegreeOfficialPublication.faces">
			<h:outputFormat value="#{scouncilBundle['return']}" />
			<f:param name="officialPubId" value="#{DegreeManagement.officialPublicationBean.degreeOfficialPublicationGoBack.externalId}" />
			<f:param name="degreeId" value="#{ DegreeManagement.degreeId}" />
		<f:param name="selectedExecutionYearId"
				value="#{ DegreeManagement.selectedExecutionYearId}" />
		</h:outputLink>
	</h:form>


</ft:tilesView>
