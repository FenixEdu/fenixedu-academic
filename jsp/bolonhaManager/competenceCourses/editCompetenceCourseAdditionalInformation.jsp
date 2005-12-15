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
	
	<h:outputText value="#{bolonhaBundle['step']} 1: #{bolonhaBundle['createCompetenceCourse']} > "/>
	<h:outputText value="#{bolonhaBundle['step']} 2: #{bolonhaBundle['setData']}"  style="font-weight: bold"/>
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
	<h:outputText value="#{bolonhaBundle['name']}: " style="font-weight: bold"/>
	<h:outputText value="#{CompetenceCourseManagement.competenceCourse.name}" /><br/>
	<h:outputText value="#{bolonhaBundle['ectsCredits']}: " style="font-weight: bold"/>
	<h:outputText value="#{CompetenceCourseManagement.competenceCourse.ectsCredits}" /><br/>
	<h:outputText value="#{bolonhaBundle['type']}: " style="font-weight: bold"/>
	<h:outputText value="#{bolonhaBundle['basic']}<br/>" rendered="#{CompetenceCourseManagement.competenceCourse.basic}" escape="false"/>
	<h:outputText value="#{bolonhaBundle['nonBasic']}<br/>" rendered="#{!CompetenceCourseManagement.competenceCourse.basic}" escape="false"/>
	<h:outputText value="#{bolonhaBundle['lessonHours']}: " style="font-weight: bold"/>
	<h:outputText value="#{CompetenceCourseManagement.competenceCourse.totalLessonHours} h" /><br/>
	<h:outputText value="#{bolonhaBundle['regime']}: " style="font-weight: bold"/>
	<h:outputText value="#{bolonhaBundle[CompetenceCourseManagement.competenceCourse.regime]}" /><br/>
	<br/>
	<h:form>
		<h:outputText escape="false" value="<input id='scientificAreaUnitID' name='scientificAreaUnitID' type='hidden' value='#{CompetenceCourseManagement.scientificAreaUnit.idInternal}'" />
		<h:outputText escape="false" value="<input id='competenceCourseGroupUnitID' name='competenceCourseGroupUnitID' type='hidden' value='#{CompetenceCourseManagement.competenceCourseGroupUnit.idInternal}'" />
		<h:outputText escape="false" value="<input id='competenceCourseID' name='competenceCourseID' type='hidden' value='#{CompetenceCourseManagement.competenceCourseID}'" /><br/>
		
		<h:outputText value="#{bolonhaBundle['portuguese']}: " />
		<h:panelGrid columnClasses="alignRight infocell,infocell" columns="2" border="0">
			<h:outputText value="#{bolonhaBundle['program']}: " />
			<h:inputTextarea id="program" cols="80" rows="5" value="#{CompetenceCourseManagement.program}"/>
			<h:outputText value="#{bolonhaBundle['objectives']}: " />
			<h:panelGroup>
				<h:outputText value="#{bolonhaBundle['generalObjectives']}: <br/>" escape="false"/>
				<h:inputTextarea id="generalObjectives" cols="80" rows="5" value="#{CompetenceCourseManagement.generalObjectives}"/>
				<h:outputText value="<br/>" escape="false"/>
				<h:outputText value="#{bolonhaBundle['operationalObjectives']}: <br/>" escape="false"/>
				<h:inputTextarea id="operationalObjectives" cols="80" rows="5" value="#{CompetenceCourseManagement.operationalObjectives}"/>
			</h:panelGroup>
			
			<h:outputText value="#{bolonhaBundle['evaluationMethod']}: " />
			<h:inputTextarea id="evaluationMethod" cols="80" rows="5" value="#{CompetenceCourseManagement.evaluationMethod}"/>
			
			<h:outputText value="#{bolonhaBundle['prerequisites']}: " />
			<h:inputTextarea id="prerequisites" cols="80" rows="5" value="#{CompetenceCourseManagement.prerequisites}"/>
			
		</h:panelGrid>
		<br/>
		<h:outputText value="#{bolonhaBundle['english']}: " />
		<h:panelGrid columnClasses="alignRight infocell,infocell" columns="2" border="0">
			<h:outputText value="#{bolonhaBundle['nameEn']}: " />
			<h:inputText id="nameEn" maxlength="100" size="40" value="#{CompetenceCourseManagement.nameEn}"/>
	
			<h:outputText value="#{bolonhaBundle['programEn']}: " />
			<h:inputTextarea id="programEn" cols="80" rows="5" value="#{CompetenceCourseManagement.programEn}"/>
			
			<h:outputText value="#{bolonhaBundle['objectivesEn']}: " />
			<h:panelGroup>
				<h:outputText value="#{bolonhaBundle['generalObjectivesEn']}: <br/>" escape="false"/>
				<h:inputTextarea id="generalObjectivesEn" cols="80" rows="5" value="#{CompetenceCourseManagement.generalObjectivesEn}"/>
				<h:outputText value="<br/>" escape="false"/>
				<h:outputText value="#{bolonhaBundle['operationalObjectivesEn']}: <br/>" escape="false"/>
				<h:inputTextarea id="operationalObjectivesEn" cols="80" rows="5" value="#{CompetenceCourseManagement.operationalObjectivesEn}"/>
			</h:panelGroup>
			
			<h:outputText value="#{bolonhaBundle['evaluationMethodEn']}: " />
			<h:inputTextarea id="evaluationMethodEn" cols="80" rows="5" value="#{CompetenceCourseManagement.evaluationMethodEn}"/>
			
			<h:outputText value="#{bolonhaBundle['prerequisitesEn']}: " />
			<h:inputTextarea id="prerequisitesEn" cols="80" rows="5" value="#{CompetenceCourseManagement.prerequisitesEn}"/>
		</h:panelGrid>
		<br/><hr>
		<h:commandButton styleClass="inputbutton" value="#{bolonhaBundle['submit']}"
			action="#{CompetenceCourseManagement.editCompetenceCourse}"/>
		<h:commandButton immediate="true" styleClass="inputbutton" value="#{bolonhaBundle['cancel']}"
			action="competenceCoursesManagement"/>
	</h:form>
</ft:tilesView>