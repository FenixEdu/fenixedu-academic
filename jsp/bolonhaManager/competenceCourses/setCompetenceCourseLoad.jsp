<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<style>
table.nospace label {
width: auto;
}
</style>
<ft:tilesView definition="bolonhaManager.masterPage" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/BolonhaManagerResources" var="bolonhaBundle"/>
	<f:loadBundle basename="ServidorApresentacao/EnumerationResources" var="enumerationBundle"/>
	
	<h:outputText value="<em>#{CompetenceCourseManagement.personDepartment.realName}</em>" escape="false"/>
	<h:outputText value="<h2>#{bolonhaBundle['setCompetenceCourseLoad']}</h2>" escape="false"/>
	
	<h:outputText value="<p class='breadcumbs'><span>#{bolonhaBundle['step']} 1: </strong>" escape="false"/>
	<h:outputFormat value="#{bolonhaBundle['create.param']}</span>" escape="false">
		<f:param value=" #{bolonhaBundle['competenceCourse']}"/>
	</h:outputFormat>
	<h:outputText value=" > "/>
	<h:outputText value="<span class='actual'><strong>#{bolonhaBundle['step']} 2:</strong> #{bolonhaBundle['setCompetenceCourseLoad']}</span>" escape="false"/>
	<h:outputText value=" > <span><strong>#{bolonhaBundle['step']} 3:</strong> #{bolonhaBundle['setData']}</span>" escape="false"/>
	<h:outputText value="</p>" escape="false"/>
	<h:messages infoClass="infoMsg" errorClass="error0" layout="table"/>

	<h:outputText value="<ul class='nobullet padding1 indent0 mtop3'>" escape="false"/>
	<h:outputText value="<li><strong>#{bolonhaBundle['department']}: </strong>" escape="false"/>
	<h:outputText value="#{CompetenceCourseManagement.personDepartment.realName}</li>" escape="false"/>
	<fc:dataRepeater value="#{CompetenceCourseManagement.competenceCourse.unit.parentUnits}" var="scientificAreaUnit">
		<h:outputText value="<li><strong>#{bolonhaBundle['area']}: </strong>" escape="false"/>
		<h:outputText value="#{scientificAreaUnit.name} > #{CompetenceCourseManagement.competenceCourse.unit.name}</li>" escape="false"/>
	</fc:dataRepeater>		
	<h:outputText value="</ul>" escape="false"/>
	
	<h:outputText value="<div class='simpleblock3 mtop2'>" escape="false"/>
	<h:outputText value="<ul class='nobullet padding1 indent0 mbottom0'>" escape="false"/>	
	<h:outputText value="<li><strong>#{bolonhaBundle['name']} (pt): </strong>" escape="false"/>
	<h:outputText value="#{CompetenceCourseManagement.competenceCourse.name}</li>" escape="false"/>
	<h:outputText value="<li><strong>#{bolonhaBundle['nameEn']} (en): </strong>" escape="false"/>
	<h:outputText value="#{CompetenceCourseManagement.competenceCourse.nameEn}</li>" escape="false" />
	<h:outputText value="<li><strong>#{bolonhaBundle['acronym']}: </strong>" escape="false"/>
	<h:outputText value="#{CompetenceCourseManagement.competenceCourse.acronym}</li>" escape="false"/>	
	<h:outputText value="<li><strong>#{bolonhaBundle['type']}: </strong>" escape="false"/>
	<h:outputText value="#{bolonhaBundle['basic']}</li>" rendered="#{CompetenceCourseManagement.competenceCourse.basic}" escape="false"/>
	<h:outputText value="#{bolonhaBundle['nonBasic']}</li>" rendered="#{!CompetenceCourseManagement.competenceCourse.basic}" escape="false"/>
	<h:outputText value="</ul></div>" escape="false"/>
	
	<h:outputText value="<div class='simpleblock4'>" escape="false"/>
	<h:outputText value="<h4 class='first'>#{bolonhaBundle['setCompetenceCourseLoad']}: </h4>" escape="false"/>	
	<h:form>
		<fc:viewState binding="#{CompetenceCourseManagement.viewState}"/>
		<h:outputText escape="false" value="<input id='competenceCourseID' name='competenceCourseID' type='hidden' value='#{CompetenceCourseManagement.competenceCourse.idInternal}'/>"/>
		<h:outputText escape="false" value="<input id='action' name='action' type='hidden' value='#{CompetenceCourseManagement.action}'/>"/>
		
		<h:outputText value="<fieldset class='lfloat'>" escape="false"/>

		<h:outputText value="<p><label>#{bolonhaBundle['regime']}: </label>" escape="false"/>
		<fc:selectOneMenu value="#{CompetenceCourseManagement.regime}"
				onchange="this.form.submit();" valueChangeListener="#{CompetenceCourseManagement.resetCourseLoad}">
			<f:selectItem itemValue="SEMESTRIAL" itemLabel="#{enumerationBundle['SEMESTRIAL']}"/>
			<f:selectItem itemValue="ANUAL" itemLabel="#{enumerationBundle['ANUAL']}"/>
		</fc:selectOneMenu>
		<h:outputText value="</p>" escape="false"/>

		<h:outputText value="<p><label>#{bolonhaBundle['periods']}: </label>" escape="false"/>
		<h:selectOneRadio value="#{CompetenceCourseManagement.numberOfPeriods}" styleClass="nospace"
				onchange="this.form.submit();" valueChangeListener="#{CompetenceCourseManagement.resetCorrespondentCourseLoad}">
			<f:selectItems value="#{CompetenceCourseManagement.periods}"/>
		</h:selectOneRadio>
		<h:outputText value="</p>" escape="false"/>
				
		<h:dataTable value="#{CompetenceCourseManagement.courseLoads}" var="courseLoad">
			<h:column>								
				<h:outputText value="<p><label>#{bolonhaBundle['lessonHours']}: </label>" escape="false"/>				
				<h:panelGrid columns="2" style="background-color: #fff; padding: 0.5em; border: 1px solid #ccc;"
						rendered="#{courseLoad.action != 'delete'}">
					<f:facet name="header">					
						<h:outputText value="<strong>#{courseLoad.order}º #{bolonhaBundle['semester']}</strong>" escape="false"
							rendered="#{CompetenceCourseManagement.regime == 'ANUAL' && CompetenceCourseManagement.numberOfPeriods == 2}"/>		
					</f:facet>			
					
					<h:outputText value="#{bolonhaBundle['contactLessonHours']}: "/>
					<h:outputText value=" "/>
		
					<h:outputText value="#{bolonhaBundle['theoreticalLesson']}: "/>
					<h:panelGroup>
						<h:inputText maxlength="5" size="5" value="#{courseLoad.theoreticalHours}"/>
						<h:outputText value=" h/#{bolonhaBundle['lowerCase.week']}"/>
					</h:panelGroup>
					
					<h:outputText value="#{bolonhaBundle['problemsLesson']}: "/>
					<h:panelGroup>
						<h:inputText maxlength="5" size="5" value="#{courseLoad.problemsHours}"/>
						<h:outputText value=" h/#{bolonhaBundle['lowerCase.week']}"/>
					</h:panelGroup>	
					
					<h:outputText value="#{bolonhaBundle['laboratorialLesson']}: "/>
					<h:panelGroup>
						<h:inputText maxlength="5" size="5" value="#{courseLoad.laboratorialHours}"/>
						<h:outputText value=" h/#{bolonhaBundle['lowerCase.week']}"/>
					</h:panelGroup>
					
					<h:outputText value="#{bolonhaBundle['seminary']}: "/>
					<h:panelGroup>
						<h:inputText maxlength="5" size="5" value="#{courseLoad.seminaryHours}"/>
						<h:outputText value=" h/#{bolonhaBundle['lowerCase.week']}"/>
					</h:panelGroup>		
					
					<h:outputText value="#{bolonhaBundle['fieldWork']}: "/>
					<h:panelGroup>
						<h:inputText maxlength="5" size="5" value="#{courseLoad.fieldWorkHours}"/>
						<h:outputText value=" h/#{bolonhaBundle['lowerCase.week']}"/>
					</h:panelGroup>		
					
					<h:outputText value="#{bolonhaBundle['trainingPeriod']}: "/>
					<h:panelGroup>
						<h:inputText maxlength="5" size="5" value="#{courseLoad.trainingPeriodHours}"/>
						<h:outputText value=" h/#{bolonhaBundle['lowerCase.week']}"/>
					</h:panelGroup>
					
					<h:outputText value="#{bolonhaBundle['tutorialOrientation']}: "/>
					<h:panelGroup>
						<h:inputText maxlength="5" size="5" value="#{courseLoad.tutorialOrientationHours}"/>
						<h:outputText value=" h/#{bolonhaBundle['lowerCase.week']}"/>
					</h:panelGroup>
					
					<h:outputText value="#{bolonhaBundle['autonomousWork']}: " style="font-weight: bold"/>
					<h:panelGroup>
						<h:inputText maxlength="5" size="5" value="#{courseLoad.autonomousWorkHours}"/>
						<h:outputText value=" h/#{bolonhaBundle['lowerCase.semester']}"/>
					</h:panelGroup>
				</h:panelGrid>
				<h:outputText value="<p><label>#{bolonhaBundle['ectsCredits']}: </label>" escape="false"/>
				<h:inputText id="ectsCredits" required="true" maxlength="5" size="5" value="#{courseLoad.ectsCredits}"/>
				<h:message styleClass="error0" for="ectsCredits" />
				<h:outputText value="</p>" escape="false"/>
			</h:column>
		</h:dataTable>
		<h:outputText value="</p></fieldset>" escape="false"/>

		<h:panelGroup rendered="#{CompetenceCourseManagement.action == 'create'}">
			<h:commandButton styleClass="inputbutton" value="#{bolonhaBundle['submit']}" action="#{CompetenceCourseManagement.createCompetenceCourseLoad}"/>
		</h:panelGroup>
		<h:panelGroup rendered="#{CompetenceCourseManagement.action == 'edit'}">
			<h:commandButton styleClass="inputbutton" value="#{bolonhaBundle['edit']}" action="#{CompetenceCourseManagement.editCompetenceCourseLoad}"/>
		</h:panelGroup>		
		<h:panelGroup rendered="#{CompetenceCourseManagement.action == 'create'}">
			<h:commandButton immediate="true" styleClass="inputbutton" value="#{bolonhaBundle['cancel']}" action="competenceCoursesManagement"/>			
		</h:panelGroup>
		<h:panelGroup rendered="#{CompetenceCourseManagement.action == 'edit'}">
			<h:commandButton immediate="true" styleClass="inputbutton" value="#{bolonhaBundle['cancel']}" action="editCompetenceCourseMainPage"/>			
		</h:panelGroup>
	</h:form>
	<h:outputText value="</div>" escape="false"/>
</ft:tilesView>