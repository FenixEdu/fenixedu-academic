<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
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
	<f:loadBundle basename="ServidorApresentacao/EnumerationResources" var="enumerationBundle"/>
	
	<h:outputText value="#{CompetenceCourseManagement.personDepartment.realName}" style="font-style:italic"/>
	<h:outputText value="<h2>#{bolonhaBundle['setCompetenceCourseLoad']}</h2>" escape="false"/>
	
	<h:outputText value="#{bolonhaBundle['step']} 1: "/>
	<h:outputFormat value="#{bolonhaBundle['create.param']}">
		<f:param value=" #{bolonhaBundle['competenceCourse']}"/>
	</h:outputFormat>
	<h:outputText value=" > "/>
	<h:outputText value="#{bolonhaBundle['step']} 2: #{bolonhaBundle['setCompetenceCourseLoad']}" style="font-weight: bold"/>
	<h:outputText value=" > #{bolonhaBundle['step']} 3: #{bolonhaBundle['setData']}"/>
	<br/>
	<h:outputText styleClass="error" rendered="#{!empty CompetenceCourseManagement.errorMessage}"
		value="#{bolonhaBundle[CompetenceCourseManagement.errorMessage]}<br/>" escape="false"/>
	<br/>
	<h:outputText value="#{bolonhaBundle['department']}: " style="font-weight: bold"/>
	<h:outputText value="#{CompetenceCourseManagement.personDepartment.realName}" style="font-style:italic"/><br/>
	<fc:dataRepeater value="#{CompetenceCourseManagement.competenceCourse.unit.parentUnits}" var="scientificAreaUnit">
		<h:outputText value="#{bolonhaBundle['area']}: " style="font-weight: bold"/>
		<h:outputText value="#{scientificAreaUnit.name} > #{CompetenceCourseManagement.competenceCourse.unit.name}<br/>" escape="false"/>
	</fc:dataRepeater>
	<br/>
	<h:outputText value="#{bolonhaBundle['name']} (pt): " style="font-weight: bold"/>
	<h:outputText value="#{CompetenceCourseManagement.competenceCourse.name}"/><br/>
	<h:outputText value="#{bolonhaBundle['nameEn']} (en): " style="font-weight: bold"/>
	<h:outputText value="#{CompetenceCourseManagement.competenceCourse.nameEn}" /><br/>
	<h:outputText value="#{bolonhaBundle['acronym']}: " style="font-weight: bold"/>
	<h:outputText value="#{CompetenceCourseManagement.competenceCourse.acronym}" /><br/>	
	<h:outputText value="#{bolonhaBundle['type']}: " style="font-weight: bold"/>
	<h:outputText value="#{bolonhaBundle['basic']}<br/>" rendered="#{CompetenceCourseManagement.competenceCourse.basic}" escape="false"/>
	<h:outputText value="#{bolonhaBundle['nonBasic']}<br/>" rendered="#{!CompetenceCourseManagement.competenceCourse.basic}" escape="false"/>
	<br/>
	<h:form>
		<fc:viewState binding="#{CompetenceCourseManagement.viewState}"/>
		<h:outputText escape="false" value="<input id='competenceCourseID' name='competenceCourseID' type='hidden' value='#{CompetenceCourseManagement.competenceCourse.idInternal}'/>"/>
		<h:outputText escape="false" value="<input id='action' name='action' type='hidden' value='#{CompetenceCourseManagement.action}'/>"/>
		
		<h:panelGrid columnClasses="alignRight infocell,infocell" columns="2" border="0">		 
			<h:outputText value="#{bolonhaBundle['regime']}: "/>
			<fc:selectOneMenu value="#{CompetenceCourseManagement.regime}"
					onchange="this.form.submit();" valueChangeListener="#{CompetenceCourseManagement.resetCourseLoad}">
				<f:selectItem itemValue="SEMESTER" itemLabel="#{enumerationBundle['SEMESTER']}"/>
				<f:selectItem itemValue="ANUAL" itemLabel="#{enumerationBundle['ANUAL']}"/>
			</fc:selectOneMenu>
		 			
			<h:outputText value="#{bolonhaBundle['periods']}: "/>
			<h:selectOneRadio value="#{CompetenceCourseManagement.numberOfPeriods}"
					onchange="this.form.submit();" valueChangeListener="#{CompetenceCourseManagement.resetCorrespondentCourseLoad}">
				<f:selectItems value="#{CompetenceCourseManagement.periods}"/>
			</h:selectOneRadio>

			<h:outputText value="#{bolonhaBundle['lessonHours']}: "/>						
			<fc:dataRepeater value="#{CompetenceCourseManagement.courseLoads}" var="courseLoad" rowIndexVar="index">
				<h:outputText value="#{index + 1}º #{bolonhaBundle['semester']}<br/>" escape="false" style="font-weight: bold"
					rendered="#{CompetenceCourseManagement.regime == 'ANUAL' && CompetenceCourseManagement.numberOfPeriods == 2}"/>			
				<h:panelGrid columns="2" rowClasses="backgroundColor,,,,,,,,,backgroundColor">
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

					<%-- empty row --%>
					<h:outputText value=" "/>
					<h:outputText value=" "/>
					
					<h:outputText value="#{bolonhaBundle['autonomousWork']}: " style="font-weight: bold"/>
					<h:panelGroup>
						<h:inputText maxlength="5" size="5" value="#{courseLoad.autonomousWorkHours}"/>
						<h:outputText value=" h/#{bolonhaBundle['lowerCase.semester']}"/>
					</h:panelGroup>
					
					<h:outputText value="#{bolonhaBundle['ectsCredits']}: "/>					
					<h:panelGroup>
						<h:inputText id="ectsCredits" required="true" maxlength="5" size="5" value="#{courseLoad.ectsCredits}"/>
						<h:message styleClass="error" for="ectsCredits" />
					</h:panelGroup>					
				</h:panelGrid>
				<h:outputText value="<hr>" escape="false"/>
			</fc:dataRepeater>
		</h:panelGrid>			
		<br/><hr>
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
</ft:tilesView>