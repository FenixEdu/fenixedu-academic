<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="bolonhaManager.masterPage" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/BolonhaManagerResources" var="bolonhaBundle"/>
	<f:loadBundle basename="ServidorApresentacao/EnumerationResources" var="enumerationBundle"/>
	
	<h:outputText value="#{bolonhaBundle['competenceCourse']}" style="font-style:italic"/>
	<h2><h:outputText value="#{CompetenceCourseManagement.competenceCourse.name}"/></h2>
	
	<h:outputText value="#{bolonhaBundle['activeCurricularPlans']}: " style="font-weight: bold"/><br/>
	<h:panelGroup rendered="#{empty CompetenceCourseManagement.competenceCourse.associatedCurricularCourses}">
		<h:outputText value="(#{bolonhaBundle['noCurricularCourses']})"/>
	</h:panelGroup>
	<h:panelGroup rendered="#{!empty CompetenceCourseManagement.competenceCourse.associatedCurricularCourses}">
		<fc:dataRepeater value="#{CompetenceCourseManagement.competenceCourse.associatedCurricularCourses}" var="curricularCourse">			
			<h:outputLink value="../curricularPlans/viewCurricularPlan.faces" target="_blank">
				<h:outputText value="#{curricularCourse.parentDegreeCurricularPlan.name}"/>
				<f:param name="degreeCurricularPlanID" value="#{curricularCourse.parentDegreeCurricularPlan.idInternal}"/>
				<f:param name="action" value="viewccm"/>
			</h:outputLink>
			<h:outputText value=" > "/>
			<h:outputLink value="../curricularPlans/viewCurricularCourse.faces" target="_blank">
				<h:outputText value="#{curricularCourse.name}"/>
				<f:param name="curricularCourseID" value="#{curricularCourse.idInternal}"/>
				<f:param name="action" value="viewccm"/>
			</h:outputLink>
			<h:outputText value="<br/>" escape="false"/>
		</fc:dataRepeater>
	</h:panelGroup>
	<br/><br/>
	<h:outputText value="#{bolonhaBundle['department']}: " style="font-weight: bold"/>
	<h:outputText value="#{CompetenceCourseManagement.personDepartment.realName}"/><br/>
	<fc:dataRepeater value="#{CompetenceCourseManagement.competenceCourse.unit.parentUnits}" var="scientificAreaUnit">
		<h:outputText value="#{bolonhaBundle['area']}: " style="font-weight: bold"/>
		<h:outputText value="#{scientificAreaUnit.name} > #{CompetenceCourseManagement.competenceCourse.unit.name}"/>
	</fc:dataRepeater>		
	<br/><br/>
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
	<h:outputText style="font-weight: bold" value="#{bolonhaBundle['lessonHours']}: " /><br/>
	<h:outputText value="#{bolonhaBundle['regime']}: " style="font-weight: bold"/>
	<h:outputText value="#{enumerationBundle[CompetenceCourseManagement.competenceCourse.regime.name]}<br/><br/>" style="font-weight: bold" escape="false"/>
	<fc:dataRepeater value="#{CompetenceCourseManagement.sortedCompetenceCourseLoads}" var="competenceCourseLoad" rowCountVar="numberOfElements">
		<h:outputText value="#{competenceCourseLoad.order}º #{bolonhaBundle['semester']}<br/>" escape="false"
			rendered="#{CompetenceCourseManagement.competenceCourse.regime.name == 'ANUAL' && numberOfElements == 2}"/>
		<h:outputText value="- #{bolonhaBundle['theoreticalLesson']}: " style="font-weight: bold"/>
		<h:outputText value="#{competenceCourseLoad.theoreticalHours} h/#{bolonhaBundle['lowerCase.week']}<br/>" escape="false"/>

		<h:outputText value="- #{bolonhaBundle['problemsLesson']}: " style="font-weight: bold"/>
		<h:outputText value="#{competenceCourseLoad.problemsHours} h/#{bolonhaBundle['lowerCase.week']}<br/>" escape="false"/>

		<h:outputText value="- #{bolonhaBundle['laboratorialLesson']}: " style="font-weight: bold"/>
		<h:outputText value="#{competenceCourseLoad.laboratorialHours} h/#{bolonhaBundle['lowerCase.week']}<br/>" escape="false"/>

		<h:outputText value="- #{bolonhaBundle['seminary']}: " style="font-weight: bold"/>
		<h:outputText value="#{competenceCourseLoad.seminaryHours} h/#{bolonhaBundle['lowerCase.week']}<br/>" escape="false"/>

		<h:outputText value="- #{bolonhaBundle['fieldWork']}: " style="font-weight: bold"/>
		<h:outputText value="#{competenceCourseLoad.fieldWorkHours} h/#{bolonhaBundle['lowerCase.week']}<br/>" escape="false"/>

		<h:outputText value="- #{bolonhaBundle['trainingPeriod']}: " style="font-weight: bold"/>
		<h:outputText value="#{competenceCourseLoad.trainingPeriodHours} h/#{bolonhaBundle['lowerCase.week']}<br/>" escape="false"/>

		<h:outputText value="- #{bolonhaBundle['tutorialOrientation']}: " style="font-weight: bold"/>
		<h:outputText value="#{competenceCourseLoad.tutorialOrientationHours} h/#{bolonhaBundle['lowerCase.week']}<br/>" escape="false"/>

		<h:outputText value="- #{bolonhaBundle['autonomousWork']}: " style="font-weight: bold"/>
		<h:outputText value="#{competenceCourseLoad.autonomousWorkHours} h/#{bolonhaBundle['lowerCase.week']}<br/><br/>" escape="false"/>

		<h:outputText value="- #{bolonhaBundle['ectsCredits']}: " style="font-weight: bold" escape="false"/>
		<h:outputText value="#{competenceCourseLoad.ectsCredits}<br/><br/>" escape="false"/>
	</fc:dataRepeater>	
	<br/>
	<h:outputText value="#{bolonhaBundle['portuguese']}: " /><br/>
	<h:panelGrid columnClasses="alignRight infocell,infocell" columns="2" border="0">
		<h:outputText value="#{bolonhaBundle['objectives']}: "/>
		<h:outputText value="#{CompetenceCourseManagement.competenceCourse.objectives}"/>
		
		<h:outputText value="#{bolonhaBundle['program']}: "/>
		<h:outputText value="#{CompetenceCourseManagement.competenceCourse.program}"/>
		
		<h:outputText value="#{bolonhaBundle['evaluationMethod']}: "/>
		<h:outputText value="#{CompetenceCourseManagement.competenceCourse.evaluationMethod}"/>
	</h:panelGrid>
	<br/>
	<h:outputText value="#{bolonhaBundle['english']}: "/>
	<h:panelGrid columnClasses="alignRight infocell,infocell" columns="2" border="0">
		<h:outputText value="#{bolonhaBundle['objectivesEn']}: "/>
		<h:outputText value="#{CompetenceCourseManagement.competenceCourse.objectivesEn}"/>
		
		<h:outputText value="#{bolonhaBundle['programEn']}: "/>
		<h:outputText value="#{CompetenceCourseManagement.competenceCourse.programEn}"/>	
		
		<h:outputText value="#{bolonhaBundle['evaluationMethodEn']}: " />
		<h:outputText value="#{CompetenceCourseManagement.competenceCourse.evaluationMethodEn}" />
	</h:panelGrid>
	<br/><hr>
	<h:form>
		<h:panelGroup rendered="#{empty CompetenceCourseManagement.action}">
			<h:commandLink action="competenceCoursesManagement" value="#{bolonhaBundle['back']}" />
		</h:panelGroup>
	</h:form>
</ft:tilesView>