<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="bolonhaManager.masterPage" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/BolonhaManagerResources" var="bolonhaBundle"/>
	
	<h2><h:outputText value="#{bolonhaBundle['createCompetenceCourse']}"/></h2> 	
	
	<h:outputText value="#{bolonhaBundle['step']} 1: #{bolonhaBundle['createCompetenceCourse']}" style="font-weight: bold"/>
	<h:outputText value=" > #{bolonhaBundle['step']} 2: #{bolonhaBundle['setData']}"/>
	<br/>
	<h:outputText styleClass="error" rendered="#{!empty CompetenceCourseManagement.errorMessage}"
			value="#{bundle[CompetenceCourseManagement.errorMessage]}"/>
	<br/>
	<h:outputText value="#{bolonhaBundle['department']}: " style="font-weight: bold"/>
		<h:outputText value="#{CompetenceCourseManagement.personDepartmentName}"/><br/>
	<h:outputText value="#{bolonhaBundle['scientificArea']}:" style="font-weight: bold"/>
		<h:outputText value="#{CompetenceCourseManagement.scientificArea.name}"/><br/>
	<h:outputText value="#{bolonhaBundle['group']}: " style="font-weight: bold"/>
		<h:outputText value="#{CompetenceCourseManagement.competenceCourseGroup.name}"/><br/>
	<br/>
	<h:form>
		<h:outputText escape="false" value="<input id='scientificAreaID' name='scientificAreaID' type='hidden' value='#{CompetenceCourseManagement.scientificArea.idInternal}'"/>
		<h:outputText escape="false" value="<input id='competenceCourseGroupID' name='competenceCourseGroupID' type='hidden' value='#{CompetenceCourseManagement.competenceCourseGroup.idInternal}'"/>
		<h:panelGrid columnClasses="infocell" columns="2" border="0">
			<h:outputText value="#{bolonhaBundle['name']}: "/>
			<h:inputText required="true" maxlength="100" size="40" value="#{CompetenceCourseManagement.name}"/>
			
			<h:outputText value="#{bolonhaBundle['ectsCredits']}: "/>
			<h:inputText required="true" maxlength="5" size="5" value="#{CompetenceCourseManagement.ectsCredits}"/>
			
			<h:outputText value="#{bolonhaBundle['basic']}: "/>
			<h:selectBooleanCheckbox value="#{CompetenceCourseManagement.basic}"></h:selectBooleanCheckbox>
			
			<h:outputText value="#{bolonhaBundle['lessonHours']}: " />
			<h:panelGrid columns="2">
				<h:outputText value="#{bolonhaBundle['theoreticalLesson']}: "/>
				<h:panelGroup>
					<h:inputText maxlength="5" size="5" value="#{CompetenceCourseManagement.theoreticalHours}"/>
					<h:outputText value=" h"/>
				</h:panelGroup>
				
				<h:outputText value="#{bolonhaBundle['problemsLesson']}: "/>
				<h:panelGroup>
					<h:inputText maxlength="5" size="5" value="#{CompetenceCourseManagement.problemsHours}"/>
					<h:outputText value=" h"/>
				</h:panelGroup>			
				
				<h:outputText value="#{bolonhaBundle['laboratorialLesson']}: "/>
				<h:panelGroup>
					<h:inputText maxlength="5" size="5" value="#{CompetenceCourseManagement.labHours}"/>	
					<h:outputText value=" h"/>
				</h:panelGroup>					
				
				<h:outputText value="#{bolonhaBundle['projectLesson']}: "/>
				<h:panelGroup>
					<h:inputText maxlength="5" size="5" value="#{CompetenceCourseManagement.projectHours}"/>
					<h:outputText value=" h"/>
				</h:panelGroup>					
				
				<h:outputText value="#{bolonhaBundle['seminaryLesson']}: "/>
				<h:panelGroup>
					<h:inputText maxlength="5" size="5" value="#{CompetenceCourseManagement.seminaryHours}"/>
					<h:outputText value=" h"/>
				</h:panelGroup>
				
				<h:outputText value="#{bolonhaBundle['total']}: " />
				<h:outputText value="XX" />
			</h:panelGrid>
			
			<h:outputText value="#{bolonhaBundle['regime']}: " />
			<h:selectOneMenu  value="#{CompetenceCourseManagement.regime}">
				<f:selectItems value="#{CompetenceCourseManagement.regimeTypes}" />
			</h:selectOneMenu>
		</h:panelGrid>
		<br/><hr>
		<h:commandButton styleClass="inputbutton" value="#{bolonhaBundle['submit']}"
			action="#{CompetenceCourseManagement.createCompetenceCourse}"/>
		<h:commandButton immediate="true" styleClass="inputbutton" value="#{bolonhaBundle['cancel']}"
			action="competenceCoursesManagement"/>
	</h:form>
</ft:tilesView>