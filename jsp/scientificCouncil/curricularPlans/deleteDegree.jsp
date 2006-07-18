<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="scientificCouncil.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/ScientificCouncilResources" var="scouncilBundle"/>	
	<f:loadBundle basename="resources/EnumerationResources" var="enumerationBundle"/>

	<h:outputText value="<i>#{DegreeManagement.degree.nome}" escape="false"/>
	<h:outputText value=" (#{DegreeManagement.degree.sigla})</i>" escape="false"/>
	<h:outputFormat value="<h2>#{scouncilBundle['delete.param']}</h2>" escape="false">
		<f:param value="#{scouncilBundle['degree']}" />
	</h:outputFormat>
	<h:form>
		<h:outputText escape="false" value="<input alt='input.degreeId' id='degreeId' name='degreeId' type='hidden' value='#{DegreeManagement.degreeId}'/>"/>

<%--
		<h:outputText value="<b>#{scouncilBundle['degree.data']}:</b><br/>" escape="false"/>
--%>	

		<h:outputText styleClass="error" rendered="#{!empty DegreeManagement.errorMessage}"
			value="#{DegreeManagement.errorMessage}<br/><br/>" escape="false"/>
		
			<h:outputText value="<div class='simpleblock1'/>" escape="false"/>
			<h:outputText value="<p><b>#{scouncilBundle['name']} (pt):</b> " escape="false"/>
			<h:outputText id="name" value="#{DegreeManagement.name}</p>" escape="false"/>
			
			<h:outputText value="<p><b>#{scouncilBundle['name']} (en):</b> " escape="false"/>
			<h:outputText id="nameEn" value="#{DegreeManagement.nameEn}</p>" escape="false"/>
			
			<h:outputText value="<p><b>#{scouncilBundle['acronym']}:</b> " escape="false"/>
			<h:outputText id="acronym" value="#{DegreeManagement.acronym}</p>" escape="false"/>
			
			<h:outputText value="<p><b>#{scouncilBundle['degreeType']}:</b> " escape="false"/>
			<h:outputText id="bolonhaDegreeType" value="#{enumerationBundle[DegreeManagement.bolonhaDegreeType]}</p>" escape="false"/>

			<h:outputText value="<p><b>#{scouncilBundle['ectsCredits']}:</b> " escape="false"/>
			<h:outputText id="ectsCredits" value="#{DegreeManagement.ectsCredits}</p>" escape="false"/>
			<h:outputText value="</div>" escape="false"/>
<%-- 
 			<h:outputText value="<b>#{scouncilBundle['gradeTypes']}:</b> " escape="false"/>
			<h:outputText id="gradeType" value="#{enumerationBundle[DegreeManagement.gradeType]}" escape="false"/>
--%>

		<h:outputText value="<br/><p>" escape="false"/>
		<h:commandButton alt="#{htmlAltBundle['commandButton.confirm']}" styleClass="inputbutton" value="#{scouncilBundle['confirm']}"
			action="#{DegreeManagement.deleteDegree}" onclick="return confirm('#{scouncilBundle['confirm.delete.degree']}')"/>
		<h:commandButton alt="#{htmlAltBundle['commandButton.cancel']}" immediate="true" styleClass="inputbutton" value="#{scouncilBundle['cancel']}"
			action="curricularPlansManagement"/>
		<h:outputText value="</p>" escape="false"/>
	</h:form>

</ft:tilesView>