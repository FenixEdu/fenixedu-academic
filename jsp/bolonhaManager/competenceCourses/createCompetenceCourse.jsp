<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<style>
<!--
.alignRight {
	text-align: right;
}
-->
</style>
<ft:tilesView definition="bolonhaManager.masterPage" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/BolonhaManagerResources" var="bolonhaBundle"/>
	
	<h2><h:outputText value="#{bolonhaBundle['createCompetenceCourse']}"/></h2> 	
	
	<h:outputText value="#{bolonhaBundle['step']} 1: #{bolonhaBundle['createCompetenceCourse']}" style="font-weight: bold"/>
	<h:outputText value=" > #{bolonhaBundle['step']} 2: #{bolonhaBundle['setData']}"/>
	<br/>
	<h:outputText styleClass="error" rendered="#{!empty CompetenceCourseManagement.errorMessage}"
			value="#{bolonhaBundle[CompetenceCourseManagement.errorMessage]}<br/>" escape="false"/>
	<br/>
	<h:outputText value="#{bolonhaBundle['department']}: " style="font-weight: bold"/>
		<h:outputText value="#{CompetenceCourseManagement.personDepartmentName}"/><br/>
	<h:outputText value="#{bolonhaBundle['scientificArea']}:" style="font-weight: bold"/>
		<h:outputText value="#{CompetenceCourseManagement.scientificAreaUnit.name}"/><br/>
	<h:outputText value="#{bolonhaBundle['group']}: " style="font-weight: bold"/>
		<h:outputText value="#{CompetenceCourseManagement.competenceCourseGroupUnit.name}"/><br/>
	<br/>
	<h:form>
		<h:outputText escape="false" value="<input id='scientificAreaUnitID' name='scientificAreaUnitID' type='hidden' value='#{CompetenceCourseManagement.scientificAreaUnit.idInternal}'"/>
		<h:outputText escape="false" value="<input id='competenceCourseGroupUnitID' name='competenceCourseGroupUnitID' type='hidden' value='#{CompetenceCourseManagement.competenceCourseGroupUnit.idInternal}'"/>
		<h:panelGrid columnClasses="alignRight infocell,infocell" columns="2" border="0">
			<h:outputText value="#{bolonhaBundle['name']}: "/>
			<h:panelGroup>
				<h:inputText id="name" required="true" maxlength="100" size="40" value="#{CompetenceCourseManagement.name}"/>
				<h:message styleClass="error" for="name" />
			</h:panelGroup>
			
			<h:outputText value="#{bolonhaBundle['ectsCredits']}: "/>
			<h:panelGroup>
				<h:inputText id="ectsCredits" required="true" maxlength="5" size="5" value="#{CompetenceCourseManagement.ectsCredits}"/>
				<h:message styleClass="error" for="ectsCredits" />
			</h:panelGroup>
			
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
				<h:outputText value="." />
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