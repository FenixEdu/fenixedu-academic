<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="scientificCouncil.masterPage" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/ScientificCouncilResources" var="scouncilBundle"/>
	
	<h:outputText value="<i>#{scouncilBundle['scientificCouncil']}</i>" escape="false"/>
	<h:outputText value="<h2>#{scouncilBundle['createDegree']}</h2>" escape="false"/>
	<h:form>
		<h:outputText value="<b>#{scouncilBundle['degree.data']}:</b>" escape="false"/>
		
		<h:outputText styleClass="error" rendered="#{!empty ScientificCouncilDegreeManagement.errorMessage}"
			value="<br/>#{scouncilBundle[ScientificCouncilDegreeManagement.errorMessage]}<br/><br/>" escape="false"/>
		
		<h:panelGrid columnClasses="infocell" columns="2" border="0">
			<h:outputText value="#{scouncilBundle['name']} (pt): " />
			<h:panelGroup>
				<h:inputText id="namePt" value="#{ScientificCouncilDegreeManagement.namePt}" required="true" maxlength="100" size="60"/>
				<h:message for="namePt" showSummary="true" errorClass="error" rendered="#{empty ScientificCouncilDegreeManagement.errorMessage}"/>
			</h:panelGroup>
			
			<h:outputText value="#{scouncilBundle['name']} (en): " />
			<h:inputText value="#{ScientificCouncilDegreeManagement.nameEn}" required="true" maxlength="100" size="60"/>			
			
			<h:outputText value="#{scouncilBundle['acronym']}: " />
			<h:inputText value="#{ScientificCouncilDegreeManagement.code}" required="true" maxlength="100" size="10"/>
			
			<h:outputText value="#{scouncilBundle['degreeType']}: " />
			<h:selectOneMenu required="true" value="#{ScientificCouncilDegreeManagement.bolonhaDegreeType}">
				<f:selectItems value="#{ScientificCouncilDegreeManagement.bolonhaDegreeTypes}" />
			</h:selectOneMenu>
			
			<h:outputText value="#{scouncilBundle['gradeTypes']}: " />
			<h:selectOneMenu required="true" value="#{ScientificCouncilDegreeManagement.gradeType}">
				<f:selectItems value="#{ScientificCouncilDegreeManagement.gradeTypes}" />
			</h:selectOneMenu>
		</h:panelGrid>
		<br/>
		<h:commandButton styleClass="inputbutton" value="#{scouncilBundle['submit']}"
			action="#{ScientificCouncilDegreeManagement.createDegree}"/>
		<h:commandButton immediate="true" styleClass="inputbutton" value="#{scouncilBundle['cancel']}"
			action="curricularPlansManagement"/>
	</h:form>
</ft:tilesView>