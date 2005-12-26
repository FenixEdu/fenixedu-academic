<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="scientificCouncil.masterPage" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/ScientificCouncilResources" var="scouncilBundle"/>
	
	<h:outputText value="<i>#{scouncilBundle['scientificCouncil']}</i>" escape="false"/>
	<h:outputFormat value="<h2>#{scouncilBundle['create.param']}</h2>" escape="false">
		<f:param value="#{scouncilBundle['degree']}"/>
	</h:outputFormat>
	<h:form>
		<h:outputText value="<b>#{scouncilBundle['degree.data']}:</b><br/><br/>" escape="false"/>
		
		<h:outputText styleClass="error" rendered="#{!empty ScientificCouncilDegreeManagement.errorMessage}"
			value="#{ScientificCouncilDegreeManagement.errorMessage}<br/>" escape="false"/>
		
		<h:panelGrid columnClasses="infocell" columns="2" border="0">
			<h:outputText value="#{scouncilBundle['name']} (pt): " />
			<h:panelGroup>
				<h:inputText id="name" value="#{ScientificCouncilDegreeManagement.name}" required="true" maxlength="100" size="60"/>
				<h:message for="name" errorClass="error" rendered="#{empty ScientificCouncilDegreeManagement.errorMessage}"/>
			</h:panelGroup>
			
			<h:outputText value="#{scouncilBundle['name']} (en): " />
			<h:panelGroup>
				<h:inputText id="nameEn" value="#{ScientificCouncilDegreeManagement.nameEn}" required="true" maxlength="100" size="60"/>			
				<h:message for="nameEn" errorClass="error" rendered="#{empty ScientificCouncilDegreeManagement.errorMessage}"/>
			</h:panelGroup>
			
			<h:outputText value="#{scouncilBundle['acronym']}: " />
			<h:panelGroup>
				<h:inputText id="acronym" value="#{ScientificCouncilDegreeManagement.acronym}" required="true" maxlength="100" size="10"/>
				<h:message for="acronym" errorClass="error" rendered="#{empty ScientificCouncilDegreeManagement.errorMessage}"/>
			</h:panelGroup>
			
			<h:outputText value="#{scouncilBundle['degreeType']}: " />
			<h:panelGroup>
				<h:selectOneMenu id="bolonhaDegreeType" value="#{ScientificCouncilDegreeManagement.bolonhaDegreeType}">
					<f:selectItems value="#{ScientificCouncilDegreeManagement.bolonhaDegreeTypes}" />
				</h:selectOneMenu>
				<h:message for="bolonhaDegreeType" errorClass="error" rendered="#{empty ScientificCouncilDegreeManagement.errorMessage}"/>
			</h:panelGroup>

			<h:outputText value="#{scouncilBundle['ectsCredits']}: " />
			<h:panelGroup>
				<h:inputText id="ectsCredits" value="#{ScientificCouncilDegreeManagement.ectsCredits}" required="true" maxlength="100" size="10"/>
				<h:message for="ectsCredits" errorClass="error" rendered="#{empty ScientificCouncilDegreeManagement.errorMessage}"/>
			</h:panelGroup>

<%--
 			<h:outputText value="#{scouncilBundle['gradeTypes']}: " />
			<h:panelGroup>
				<h:selectOneMenu id="gradeType" value="#{ScientificCouncilDegreeManagement.gradeType}">
					<f:selectItems value="#{ScientificCouncilDegreeManagement.gradeScales}" />
				</h:selectOneMenu>
				<h:message for="gradeType" errorClass="error" rendered="#{empty ScientificCouncilDegreeManagement.errorMessage}"/>				
			</h:panelGroup>
--%>
		</h:panelGrid>
		<br/>
		<h:commandButton styleClass="inputbutton" value="#{scouncilBundle['create']}"
			action="#{ScientificCouncilDegreeManagement.createDegree}"/>
		<h:commandButton immediate="true" styleClass="inputbutton" value="#{scouncilBundle['cancel']}"
			action="curricularPlansManagement"/>
	</h:form>
</ft:tilesView>