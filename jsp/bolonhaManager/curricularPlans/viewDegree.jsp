<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="bolonhaManager.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/ScientificCouncilResources" var="scouncilBundle"/>	
	<f:loadBundle basename="resources/EnumerationResources" var="enumerationBundle"/>

	<h:outputText value="<i>#{ScientificCouncilDegreeManagement.degree.nome}" escape="false"/>
	<h:outputText value=" (#{ScientificCouncilDegreeManagement.degree.acronym})</i>" escape="false"/>
	<h:outputFormat value="<h2>#{scouncilBundle['view.param']}</h2>" escape="false">
		<f:param value="#{scouncilBundle['degree']}" />
	</h:outputFormat>
	<h:form>
		<h:outputText escape="false" value="<input id='degreeId' name='degreeId' type='hidden' value='#{ScientificCouncilDegreeManagement.degreeId}'/>"/>

<%--
		<h:outputText value="<p><b>#{scouncilBundle['degree.data']}:</b><p/><br/>" escape="false"/>
--%>	

		<h:outputText styleClass="error" rendered="#{!empty ScientificCouncilDegreeManagement.errorMessage}" value="<p>#{ScientificCouncilDegreeManagement.errorMessage}<p/><br/>" escape="false"/>

			<h:outputText value="<p><b>#{scouncilBundle['name']} (pt):</b> " escape="false"/>
			<h:outputText id="name" value="#{ScientificCouncilDegreeManagement.name}</p>" escape="false"/>
			
			<h:outputText value="<p><b>#{scouncilBundle['name']} (en):</b> " escape="false"/>
			<h:outputText id="nameEn" value="#{ScientificCouncilDegreeManagement.nameEn}</p>" escape="false"/>
			
			<h:outputText value="<p><b>#{scouncilBundle['acronym']}:</b> " escape="false"/>
			<h:outputText id="acronym" value="#{ScientificCouncilDegreeManagement.acronym}</p>" escape="false"/>
			
			<h:outputText value="<p><b>#{scouncilBundle['degreeType']}:</b> " escape="false"/>
			<h:outputText id="bolonhaDegreeType" value="#{enumerationBundle[ScientificCouncilDegreeManagement.bolonhaDegreeType]}</p>" escape="false"/>

			<h:outputText value="<p><b>#{scouncilBundle['ectsCredits']}:</b> " escape="false"/>
			<h:outputText id="ectsCredits" value="#{ScientificCouncilDegreeManagement.ectsCredits}</p>" escape="false"/>

<%-- 
 			<h:outputText value="<b>#{scouncilBundle['gradeTypes']}:</b> " escape="false"/>
			<h:outputText id="gradeType" value="#{enumerationBundle[ScientificCouncilDegreeManagement.gradeType]}"/>
--%>

		<br/>
		<h:outputText value="<p> " escape="false"/>
		<h:commandButton immediate="true" styleClass="inputbutton" value="#{scouncilBundle['return']}"
			action="curricularPlansManagement"/>
		<h:outputText value="</p> " escape="false"/>
	</h:form>

</ft:tilesView>