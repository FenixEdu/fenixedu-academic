<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="bolonhaManager.masterPage" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/BolonhaManagerResources" var="bolonhaBundle"/>
	
	<h:outputText value="#{bolonhaBundle['competenceCourse']}" style="font-style:italic"/>
	<h2><h:outputText value="#{CompetenceCourseManagement.competenceCourse.name}"/></h2>
	
	<h:outputText value="#{bolonhaBundle['activeCurricularPlans']}: " style="font-weight: bold"/><br/>
	<h:panelGroup rendered="#{empty CompetenceCourseManagement.competenceCourse.associatedCurricularCourses}">
		<h:outputText value="(#{bolonhaBundle['noCurricularCourses']})"/>
	</h:panelGroup>
	<h:panelGroup rendered="#{!empty CompetenceCourseManagement.competenceCourse.associatedCurricularCourses}">
		<h:dataTable value="#{CompetenceCourseManagement.competenceCourse.associatedCurricularCourses}" var="curricularCourse">
			<h:column>
				<h:outputText value="#{curricularCourse.name}"/>
			</h:column>
		</h:dataTable>
	</h:panelGroup>
	<br/><br/>
	<h:outputText value="#{bolonhaBundle['department']}: " style="font-weight: bold"/>
		<h:outputText value="#{CompetenceCourseManagement.personDepartmentName}"/><br/>
	<fc:dataRepeater value="#{CompetenceCourseManagement.competenceCourse.unit.parentUnits}" var="scientificAreaUnit">
		<h:outputText value="#{bolonhaBundle['scientificArea']}: " style="font-weight: bold"/>
		<h:outputText value="#{scientificAreaUnit.name}<br/>" escape="false"/>
	</fc:dataRepeater>		
	<h:outputText value="#{bolonhaBundle['group']}: " style="font-weight: bold"/>
		<h:outputText value="#{CompetenceCourseManagement.competenceCourse.unit.name}"/><br/>
	<br/>
	<h:outputText value="#{bolonhaBundle['name']}: " style="font-weight: bold"/>
	<h:outputText value="#{CompetenceCourseManagement.competenceCourse.name}" /><br/>
	<h:outputText value="#{bolonhaBundle['ectsCredits']}: " style="font-weight: bold"/>
	<h:outputText value="#{CompetenceCourseManagement.competenceCourse.ectsCredits}" /><br/>
	<h:outputText value="#{bolonhaBundle['type']}: " style="font-weight: bold"/>
	<h:outputText value="#{bolonhaBundle['basic']}<br/>" rendered="#{CompetenceCourseManagement.competenceCourse.basic}" escape="false"/>
	<h:outputText value="#{bolonhaBundle['nonBasic']}<br/>" rendered="#{!CompetenceCourseManagement.competenceCourse.basic}" escape="false"/>
	<h:outputText value="#{bolonhaBundle['regime']}: " style="font-weight: bold"/>
	<h:outputText value="#{bolonhaBundle[CompetenceCourseManagement.competenceCourse.regime]}" /><br/>
	<br/>	
	<h:outputText style="font-weight: bold" value="#{bolonhaBundle['lessonHours']}: " /><br/>
	<ul>
		<li>
			<h:outputText value="#{bolonhaBundle['theoreticalLesson']}: "/>
			<h:outputText value="#{CompetenceCourseManagement.competenceCourse.theoreticalHours}"/><br/>
		</li>
		<li>
			<h:outputText value="#{bolonhaBundle['problemsLesson']}: "/>
			<h:outputText value="#{CompetenceCourseManagement.competenceCourse.problemsHours}"/><br/>
		</li>
		<li>
			<h:outputText value="#{bolonhaBundle['laboratorialLesson']}: "/>
			<h:outputText value="#{CompetenceCourseManagement.competenceCourse.laboratorialHours}"/><br/>
		</li>
		<li>
			<h:outputText value="#{bolonhaBundle['projectLesson']}: "/>
			<h:outputText value="#{CompetenceCourseManagement.competenceCourse.projectHours}"/><br/>
		</li>
		<li>
			<h:outputText value="#{bolonhaBundle['seminaryLesson']}: "/>
			<h:outputText value="#{CompetenceCourseManagement.competenceCourse.seminaryHours}"/><br/>
		</li>
	</ul>					
	<br/>
	<h:outputText value="#{bolonhaBundle['portuguese']}: " /><br/>
	<h:panelGrid columnClasses="alignRight infocell,infocell" columns="2" border="0" width="80%">
		<h:outputText value="#{bolonhaBundle['program']}: " />
		<h:outputText value="#{CompetenceCourseManagement.competenceCourse.program}" />
		
		<h:outputText value="#{bolonhaBundle['objectives']}: " />
		<h:panelGroup>
			<h:outputText value="#{bolonhaBundle['generalObjectives']}: <br/>" style="font-style:italic" escape="false"/>
			<h:outputText value="#{CompetenceCourseManagement.competenceCourse.generalObjectives}" />
			<h:outputText value="<br/>" escape="false"/>
			<h:outputText value="#{bolonhaBundle['operationalObjectives']}: <br/>" style="font-style:italic" escape="false"/>
			<h:outputText value="#{CompetenceCourseManagement.competenceCourse.operationalObjectives}" />
		</h:panelGroup>
		
		<h:outputText value="#{bolonhaBundle['evaluationMethod']}: " />
		<h:outputText value="#{CompetenceCourseManagement.competenceCourse.evaluationMethod}" />
		
		<h:outputText value="#{bolonhaBundle['prerequisites']}: " />
		<h:outputText value="#{CompetenceCourseManagement.competenceCourse.prerequisites}" />
	</h:panelGrid>
	<br/>
	<h:outputText value="#{bolonhaBundle['english']}: " />
	<h:panelGrid columnClasses="alignRight infocell,infocell" columns="2" border="0" width="80%">
		<h:outputText value="#{bolonhaBundle['nameEn']}: " />
		<h:outputText value="#{CompetenceCourseManagement.competenceCourse.nameEn}" />

		<h:outputText value="#{bolonhaBundle['programEn']}: " />
		<h:outputText value="#{CompetenceCourseManagement.competenceCourse.programEn}" />
		
		<h:outputText value="#{bolonhaBundle['objectivesEn']}: " />
		<h:panelGroup>
			<h:outputText value="#{bolonhaBundle['generalObjectivesEn']}: <br/>" style="font-style:italic" escape="false"/>
			<h:outputText value="#{CompetenceCourseManagement.competenceCourse.generalObjectivesEn}" />
			<h:outputText value="<br/>" escape="false"/>
			<h:outputText value="#{bolonhaBundle['operationalObjectivesEn']}: <br/>" style="font-style:italic" escape="false"/>
			<h:outputText value="#{CompetenceCourseManagement.competenceCourse.operationalObjectivesEn}" />
		</h:panelGroup>
		
		<h:outputText value="#{bolonhaBundle['evaluationMethodEn']}: " />
		<h:outputText value="#{CompetenceCourseManagement.competenceCourse.evaluationMethodEn}" />
		
		<h:outputText value="#{bolonhaBundle['prerequisitesEn']}: " />
		<h:outputText value="#{CompetenceCourseManagement.competenceCourse.prerequisitesEn}" />
	</h:panelGrid>
	<br/><hr>
	<h:form>
		<h:commandLink action="competenceCoursesManagement" value="#{bolonhaBundle['back']}" />
	</h:form>
</ft:tilesView>