<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
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
	<h:outputText style="font-weight: bold" value="#{bolonhaBundle['program']}:" /><br/>
	<h:outputText value="-PROGRAM-"/><br/><br/>
	<h:outputText style="font-weight: bold" value="#{bolonhaBundle['bibliography']}:" /><br/>
	<h:outputText value="-BIBLIOGRAPHY-"/><br/>
	<br/><hr>
	<h:form>
		<h:commandLink action="competenceCoursesManagement" value="#{bolonhaBundle['back']}" />
	</h:form>
</ft:tilesView>