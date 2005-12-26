<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="scientificCouncil.masterPage" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/ScientificCouncilResources" var="scouncilBundle"/>	
	<f:loadBundle basename="ServidorApresentacao/EnumerationResources" var="enumerationBundle"/>

	<h:outputText value="<i>#{ScientificCouncilDegreeManagement.degree.nome}" escape="false"/>
	<h:outputText value=" (#{ScientificCouncilDegreeManagement.degree.acronym})</i>" escape="false"/>
	<h:outputFormat value="<h2>#{scouncilBundle['delete.param']}</h2>" escape="false">
		<f:param value="#{scouncilBundle['degree']}" />
	</h:outputFormat>
	<h:form>
		<h:outputText escape="false" value="<input id='degreeId' name='degreeId' type='hidden' value='#{ScientificCouncilDegreeManagement.degreeId}'"/><br/>
		
		<h:outputText value="<b>#{scouncilBundle['degree.data']}:</b><br/><br/>" escape="false"/>
		
		<h:outputText styleClass="error" rendered="#{!empty ScientificCouncilDegreeManagement.errorMessage}"
			value="#{ScientificCouncilDegreeManagement.errorMessage}<br/><br/>" escape="false"/>
		
		<h:panelGrid styleClass="infoselected" columns="2" border="0">
			<h:outputText value="<b>#{scouncilBundle['name']} (pt):</b> " escape="false"/>
			<h:outputText id="name" value="#{ScientificCouncilDegreeManagement.name}"/>
			
			<h:outputText value="<b>#{scouncilBundle['name']} (en):</b> " escape="false"/>
			<h:outputText id="nameEn" value="#{ScientificCouncilDegreeManagement.nameEn}" />
			
			<h:outputText value="<b>#{scouncilBundle['acronym']}:</b> " escape="false"/>
			<h:outputText id="acronym" value="#{ScientificCouncilDegreeManagement.acronym}"/>
			
			<h:outputText value="<b>#{scouncilBundle['degreeType']}:</b> " escape="false"/>
			<h:outputText id="bolonhaDegreeType" value="#{enumerationBundle[ScientificCouncilDegreeManagement.bolonhaDegreeType]}"/>

			<h:outputText value="<b>#{scouncilBundle['ectsCredits']}:</b> " escape="false"/>
			<h:outputText id="ectsCredits" value="#{ScientificCouncilDegreeManagement.ectsCredits}"/>

<%-- 
 			<h:outputText value="<b>#{scouncilBundle['gradeTypes']}:</b> " escape="false"/>
			<h:outputText id="gradeType" value="#{enumerationBundle[ScientificCouncilDegreeManagement.gradeType]}"/>
--%>
		</h:panelGrid>
		<br/>
		<h:commandButton styleClass="inputbutton" value="#{scouncilBundle['confirm']}"
			action="#{ScientificCouncilDegreeManagement.deleteDegree}" onclick="return confirm('#{scouncilBundle['confirm.delete.degree']}')"/>
		<h:commandButton immediate="true" styleClass="inputbutton" value="#{scouncilBundle['cancel']}"
			action="curricularPlansManagement"/>
	</h:form>

</ft:tilesView>