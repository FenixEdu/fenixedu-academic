<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="bolonhaManager.masterPage" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/BolonhaManagerResources" var="bolonhaBundle"/>
	<f:loadBundle basename="ServidorApresentacao/EnumerationResources" var="enumerationBundle"/>
	
	<h:outputText value="<em>#{bolonhaBundle['competenceCourse']}</em>" escape="false" />
	<h:outputText value="<h2>#{CompetenceCourseManagement.competenceCourse.name}</h2>" escape="false"/>

	<h:outputText value="<ul class='nobullet padding1 indent0 mtop3'>" escape="false"/>
	<h:outputText value="<li><strong>#{bolonhaBundle['department']}: </strong>" escape="false"/>
	<h:outputText value="#{CompetenceCourseManagement.personDepartment.realName}</li>" escape="false"/>
	<fc:dataRepeater value="#{CompetenceCourseManagement.competenceCourse.unit.parentUnits}" var="scientificAreaUnit">
		<h:outputText value="<li><strong>#{bolonhaBundle['area']}: </strong>" escape="false"/>
		<h:outputText value="#{scientificAreaUnit.name} > #{CompetenceCourseManagement.competenceCourse.unit.name}</li>" escape="false"/>
	</fc:dataRepeater>		
	<h:outputText value="</ul>" escape="false"/>
		
	<h:outputText value="<p class='mtop2 mbottom0'><strong>#{bolonhaBundle['activeCurricularPlans']}: </strong></p>" escape="false"/>
	<h:panelGroup rendered="#{empty CompetenceCourseManagement.competenceCourse.associatedCurricularCourses}">
		<h:outputText value="(#{bolonhaBundle['noCurricularCourses']})"/>
	</h:panelGroup>
	<h:panelGroup rendered="#{!empty CompetenceCourseManagement.competenceCourse.associatedCurricularCourses}">
		<h:outputText value="<ul class='mtop0 mbottom3'>" escape="false"/>
		<fc:dataRepeater value="#{CompetenceCourseManagement.competenceCourse.associatedCurricularCourses}" var="curricularCourse">			
			<h:outputText value="<li>" escape="false"/>
			<h:outputLink value="../curricularPlans/viewCurricularPlan.faces" target="_blank">
				<h:outputText value="#{curricularCourse.parentDegreeCurricularPlan.name}" escape="false"/>
				<f:param name="degreeCurricularPlanID" value="#{curricularCourse.parentDegreeCurricularPlan.idInternal}"/>
				<f:param name="action" value="viewccm"/>
			</h:outputLink>
			<h:outputText value=" > "/>
			<h:outputLink value="../curricularPlans/viewCurricularCourse.faces" target="_blank">
				<h:outputText value="#{curricularCourse.name}" escape="false"/>
				<f:param name="curricularCourseID" value="#{curricularCourse.idInternal}"/>
				<f:param name="action" value="viewccm"/>
			</h:outputLink>
			<h:outputText value="</li>" escape="false"/>
		</fc:dataRepeater>
		<h:outputText value="</ul>" escape="false"/>
	</h:panelGroup>	

	<h:outputText value="<div class='simpleblock3 mtop2'>" escape="false"/>
	<h:outputText value="<p><strong>#{bolonhaBundle['state']}: </strong>" escape="false"/>
	<h:outputText value="#{enumerationBundle[CompetenceCourseManagement.competenceCourse.curricularStage.name]}</p>" escape="false"/>
	<h:outputText value="</div>" escape="false"/>

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
	<h:outputText value="<p class='mtop1'>" escape="false"/>
	<h:outputLink value="editCompetenceCourse.faces">
		<h:outputText value="#{bolonhaBundle['edit']}"/>
		<f:param name="competenceCourseID" value="#{CompetenceCourseManagement.competenceCourse.idInternal}"/>
		<f:param name="action" value="viewccm"/>
	</h:outputLink>
	<h:outputText value="</p>" escape="false"/>
	
	<h:outputText value="<div class='simpleblock3 mtop2'>" escape="false"/>
	<h:outputText value="<ul class='nobullet padding1 indent0 mbottom0'>" escape="false"/>
	<h:outputText value="<li><strong>#{bolonhaBundle['regime']}: </strong>" escape="false"/>
	<h:outputText value="#{enumerationBundle[CompetenceCourseManagement.competenceCourse.regime.name]}</li>" escape="false"/>	
	<h:outputText value="<li><strong>#{bolonhaBundle['lessonHours']}: </strong>" escape="false"/>	
	<fc:dataRepeater value="#{CompetenceCourseManagement.sortedCompetenceCourseLoads}" var="competenceCourseLoad" rowCountVar="numberOfElements">
		<h:outputText value="<p class='mbotton0'><em>#{competenceCourseLoad.order}º #{bolonhaBundle['semester']}</em></p>" escape="false"
			rendered="#{CompetenceCourseManagement.competenceCourse.regime.name == 'ANUAL' && numberOfElements == 2}"/>
		
		<h:outputText value="<ul class='mvert0'>" escape="false"/>
		<h:outputText value="<li>#{bolonhaBundle['theoreticalLesson']}: " escape="false"/>
		<h:outputText value="#{competenceCourseLoad.theoreticalHours} h/#{bolonhaBundle['lowerCase.week']}</li>" escape="false"/>

		<h:outputText value="<li>#{bolonhaBundle['problemsLesson']}: " escape="false"/>
		<h:outputText value="#{competenceCourseLoad.problemsHours} h/#{bolonhaBundle['lowerCase.week']}</li>" escape="false"/>

		<h:outputText value="<li>#{bolonhaBundle['laboratorialLesson']}: " escape="false"/>
		<h:outputText value="#{competenceCourseLoad.laboratorialHours} h/#{bolonhaBundle['lowerCase.week']}</li>" escape="false"/>

		<h:outputText value="<li>#{bolonhaBundle['seminary']}: " escape="false"/>
		<h:outputText value="#{competenceCourseLoad.seminaryHours} h/#{bolonhaBundle['lowerCase.week']}</li>" escape="false"/>

		<h:outputText value="<li>#{bolonhaBundle['fieldWork']}: " escape="false"/>
		<h:outputText value="#{competenceCourseLoad.fieldWorkHours} h/#{bolonhaBundle['lowerCase.week']}</li>" escape="false"/>

		<h:outputText value="<li>#{bolonhaBundle['trainingPeriod']}: " escape="false"/>
		<h:outputText value="#{competenceCourseLoad.trainingPeriodHours} h/#{bolonhaBundle['lowerCase.week']}</li>" escape="false"/>

		<h:outputText value="<li>#{bolonhaBundle['tutorialOrientation']}: " escape="false"/>
		<h:outputText value="#{competenceCourseLoad.tutorialOrientationHours} h/#{bolonhaBundle['lowerCase.week']}</li>" escape="false"/>

		<h:outputText value="<li>#{bolonhaBundle['autonomousWork']}: " escape="false"/>
		<h:outputText value="#{competenceCourseLoad.autonomousWorkHours} h/#{bolonhaBundle['lowerCase.week']}</li>" escape="false"/>

		<h:outputText value="<li><strong>#{bolonhaBundle['ectsCredits']}: "escape="false"/>
		<h:outputText value="#{competenceCourseLoad.ectsCredits}</strong></li>" escape="false"/>
		<h:outputText value="</ul>" escape="false"/>
	</fc:dataRepeater>	
	<h:outputText value="</li>" escape="false"/>
	<h:outputText value="</ul></div>" escape="false"/>
	<h:outputText value="<p class='mtop1'>" escape="false"/>
	<h:outputLink value="setCompetenceCourseLoad.faces">
		<h:outputText value="#{bolonhaBundle['edit']}"/>
		<f:param name="competenceCourseID" value="#{CompetenceCourseManagement.competenceCourse.idInternal}"/>
		<f:param name="action" value="edit"/>
	</h:outputLink>
	<h:outputText value="</p>" escape="false"/>	

	<h:outputText value="<div class='simpleblock3 mtop2'>" escape="false"/>
	<h:outputText value="<p class='mbottom0'><em>#{bolonhaBundle['portuguese']}: </em></p>" escape="false"/>
	<h:outputText value="<table class='showinfo1 highlight2'>" escape="false"/>	
	
	<h:outputText value="<tr><th>#{bolonhaBundle['objectives']}: </th>" escape="false"/>
	<h:outputText value="<td>#{CompetenceCourseManagement.competenceCourse.objectives}</td></tr>" escape="false"/>	
	
	<h:outputText value="<tr><th>#{bolonhaBundle['program']}: </th>" escape="false"/>
	<h:outputText value="<td>#{CompetenceCourseManagement.competenceCourse.program}</td></tr>" escape="false"/>
	
	<h:outputText value="</table><p class='mtop1'>" escape="false"/>
	<h:outputLink value="setCompetenceCourseAdditionalInformation.faces?competenceCourseID=#{CompetenceCourseManagement.competenceCourse.idInternal}&action=edit#portuguese">
		<h:outputText value="#{bolonhaBundle['edit']}"/>
	</h:outputLink>
	<h:outputText value="</p></div>" escape="false"/>
	
	<h:outputText value="<div class='simpleblock3 mtop2'>" escape="false"/>
	<h:outputText value="<p class='mbottom0'><em>#{bolonhaBundle['english']}: </em></p>" escape="false"/>
	<h:outputText value="<table class='showinfo1 highlight2'>" escape="false"/>	
	
	<h:outputText value="<tr><th>#{bolonhaBundle['objectivesEn']}: </th>" escape="false"/>
	<h:outputText value="<td>#{CompetenceCourseManagement.competenceCourse.objectivesEn}</td></tr>" escape="false"/>	
	
	<h:outputText value="<tr><th>#{bolonhaBundle['programEn']}: </th>" escape="false"/>
	<h:outputText value="<td>#{CompetenceCourseManagement.competenceCourse.programEn}</td></tr>" escape="false"/>
	
	<h:outputText value="</table><p class='mtop1'>" escape="false"/>
	<h:outputLink value="setCompetenceCourseAdditionalInformation.faces?competenceCourseID=#{CompetenceCourseManagement.competenceCourse.idInternal}&action=edit#english">
		<h:outputText value="#{bolonhaBundle['edit']}"/>
	</h:outputLink>
	<h:outputText value="</p></div>" escape="false"/>

	<h:form>
		<h:outputText escape="false" value="<input id='action' name='action' type='hidden' value='#{CompetenceCourseManagement.action}'/>"/>
		<h:panelGroup rendered="#{!empty CompetenceCourseManagement.action}">
			<h:commandButton immediate="true" styleClass="inputbutton" action="competenceCoursesManagement" value="#{bolonhaBundle['back']}" />
		</h:panelGroup>
		
		<h:panelGroup rendered="#{empty CompetenceCourseManagement.action}">
			<h:commandButton immediate="true" styleClass="inputbutton" onclick="window.close()" value="#{bolonhaBundle['close']}" />
		</h:panelGroup>
	</h:form>
</ft:tilesView>
