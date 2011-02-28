<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>



<ft:tilesView definition="scientificCouncil.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/ScientificCouncilResources" var="scouncilBundle"/>

	<h:outputText value="<em>#{scouncilBundle['scientificCouncil']}</em>" escape="false"/>


	<h:outputFormat value="<h2>#{scouncilBundle['delete.param']}</h2>"
		escape="false">
		<f:param value="#{scouncilBundle['degree.officialPublication']}" />
	</h:outputFormat>
	<h:form>
	
		<h:outputText escape="false"
		value="<input alt='input.officialPubId' id='officialPubId' name='officialPubId' type='hidden' value='#{DegreeManagement.officialPublicationBean.officialPubId}'/>" />
		
			<h:outputText escape="false"
		value="<input alt='input.degreeId' id='degreeId' name='degreeId' type='hidden' value='#{DegreeManagement.degreeId}'/>" />
	<h:outputText escape="false"
		value="<input alt='input.selectedExecutionYearId' id='selectedExecutionYearId' name='selectedExecutionYearId' type='hidden' value='#{DegreeManagement.selectedExecutionYearId}'/>" />

		<h:messages infoClass="success0" errorClass="error0" layout="table" globalOnly="true"/>

		<h:outputText value="<div class='infoop2'/>" escape="false"/>
		<h:outputText value="<p>#{scouncilBundle['reference']}: " escape="false"/>
		<h:outputText value="<b>#{DegreeManagement.officialPublicationBean.degreeOfficialPublication.officialReference}</b></p>" escape="false"/>


		<h:outputText value="<p class='mtop2'>" escape="false"/>
		<h:commandButton alt="#{htmlAltBundle['commandButton.confirm']}"
			styleClass="inputbutton" value="#{scouncilBundle['confirm']}"
			action="#{DegreeManagement.officialPublicationBean.removeOfficialPublication}"
			onclick="return confirm('#{scouncilBundle['confirm.delete.officialPublication']}')" />
		<h:commandButton alt="#{htmlAltBundle['commandButton.cancel']}" immediate="true" styleClass="inputbutton" value="#{scouncilBundle['cancel']}"
			action="editDegree" />
		<h:outputText value="</p>" escape="false"/>
	</h:form>


</ft:tilesView>
