<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="definition.manager.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/ScientificCouncilResources" var="scouncilBundle"/>	
	<f:loadBundle basename="resources/EnumerationResources" var="enumerationBundle"/>

	<h:outputText value="<i>#{DegreeManagement.degree.nome}" escape="false"/>
	<h:outputText value=" (#{DegreeManagement.degree.sigla})</i>" escape="false"/>
	<h:outputFormat value="<h2>#{scouncilBundle['view.param']}</h2>" escape="false">
		<f:param value="#{scouncilBundle['degree']}" />
	</h:outputFormat>

	<h:messages infoClass="success0" errorClass="error0" layout="table" globalOnly="true"/>

	<h:outputText value="<div class='simpleblock4'> " escape="false"/>
	<h:outputText value="<fieldset class='lfloat'>" escape="false"/>
	<h:outputText value="<p><label>#{scouncilBundle['name']} (pt):</label> " escape="false"/>
	<h:outputText id="name" value="#{DegreeManagement.name}</p>" escape="false"/>
	
	<h:outputText value="<p><label>#{scouncilBundle['name']} (en):</label> " escape="false"/>
	<h:outputText id="nameEn" value="#{DegreeManagement.nameEn}</p>" escape="false"/>
	
	<h:outputText value="<p><label>#{scouncilBundle['acronym']}:</label> " escape="false"/>
	<h:outputText id="acronym" value="#{DegreeManagement.acronym}</p>" escape="false"/>
	
	<h:outputText value="<p><label>#{scouncilBundle['degreeType']}:</label> " escape="false"/>
	<h:outputText id="bolonhaDegreeType" value="#{enumerationBundle[DegreeManagement.bolonhaDegreeType]}</p>" escape="false"/>

	<h:outputText value="<p><label>#{scouncilBundle['ectsCredits']}:</label> " escape="false"/>
	<h:outputText id="ectsCredits" value="#{DegreeManagement.ectsCredits}</p>" escape="false"/>
	<h:outputText value="</fieldset></div>" escape="false"/>

	<h:form>
		<h:outputText escape="false" value="<input alt='input.degreeId' id='degreeId' name='degreeId' type='hidden' value='#{DegreeManagement.degreeId}'/>"/>
		<h:outputText value="<p> " escape="false"/>
		<h:commandButton alt="#{htmlAltBundle['commandButton.return']}" immediate="true" styleClass="inputbutton" value="#{scouncilBundle['return']}"
			action="curricularPlansManagement"/>
		<h:outputText value="</p> " escape="false"/>
	</h:form>

</ft:tilesView>