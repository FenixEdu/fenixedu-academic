<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<style>
.alignRight {
	text-align: right;
}
</style>
<ft:tilesView definition="bolonhaManager.masterPage" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/BolonhaManagerResources" var="bolonhaBundle"/>
	<f:loadBundle basename="ServidorApresentacao/EnumerationResources" var="enumerationBundle"/>

	<h:outputFormat value="<h2>#{bolonhaBundle['edit.param']}</h2>" escape="false">
		<f:param value=" #{bolonhaBundle['competenceCourse']}"/>
	</h:outputFormat>
	<h:outputText value="<h3>#{CompetenceCourseManagement.competenceCourse.name}</h3>" escape="false"/>
	<h:outputText value="#{bolonhaBundle['department']}: " style="font-weight: bold"/>
	<h:outputText value="#{CompetenceCourseManagement.personDepartment.realName}" style="font-style:italic"/><br/>
	<fc:dataRepeater value="#{CompetenceCourseManagement.competenceCourse.unit.parentUnits}" var="scientificAreaUnit">
		<h:outputText value="#{bolonhaBundle['area']}: " style="font-weight: bold"/>
		<h:outputText value="#{scientificAreaUnit.name} > #{CompetenceCourseManagement.competenceCourse.unit.name}<br/>" escape="false"/>
	</fc:dataRepeater>
	<br/>	
	<h:messages infoClass="infoMsg" errorClass="error" layout="table"/>
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
	<h:outputLink value="editCompetenceCourse.faces">
		<h:outputText value="#{bolonhaBundle['edit']}"/>
		<f:param name="competenceCourseID" value="#{CompetenceCourseManagement.competenceCourse.idInternal}"/>
	</h:outputLink>
	<br/><br/>
	<h:outputText style="font-weight: bold" value="#{bolonhaBundle['lessonHours']}: <br/>" escape="false"/>
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
	<h:outputLink value="setCompetenceCourseLoad.faces">
		<h:outputText value="#{bolonhaBundle['edit']}"/>
		<f:param name="competenceCourseID" value="#{CompetenceCourseManagement.competenceCourse.idInternal}"/>
		<f:param name="action" value="edit"/>
	</h:outputLink>
	<br/><br/>
	<h:outputText value="#{bolonhaBundle['portuguese']}: <br/>" style="font-style: italic" escape="false"/>
	<h:outputText value="#{bolonhaBundle['objectives']}: <br/>" style="font-weight: bold" escape="false"/>
	<h:outputText value="#{CompetenceCourseManagement.competenceCourse.objectives}<br/>" escape="false"/>
	<h:outputLink value="setCompetenceCourseAdditionalInformation.faces?competenceCourseID=#{CompetenceCourseManagement.competenceCourse.idInternal}&action=edit#objectives">
		<h:outputText value="#{bolonhaBundle['edit']}"/>
	</h:outputLink>
	<br/><br/>
	<h:outputText value="#{bolonhaBundle['program']}: <br/>" style="font-weight: bold" escape="false"/>
	<h:outputText value="#{CompetenceCourseManagement.competenceCourse.program}<br/>" escape="false"/>
	<h:outputLink value="setCompetenceCourseAdditionalInformation.faces?competenceCourseID=#{CompetenceCourseManagement.competenceCourse.idInternal}&action=edit#program">
		<h:outputText value="#{bolonhaBundle['edit']}"/>
	</h:outputLink>
	<br/><br/>
	<h:outputText value="#{bolonhaBundle['english']}: <br/>" style="font-style: italic" escape="false"/>
	<h:outputText value="#{bolonhaBundle['objectivesEn']}: <br/>" style="font-weight: bold" escape="false"/>
	<h:outputText value="#{CompetenceCourseManagement.competenceCourse.objectivesEn}<br/>" escape="false"/>
	<h:outputLink value="setCompetenceCourseAdditionalInformation.faces?competenceCourseID=#{CompetenceCourseManagement.competenceCourse.idInternal}&action=edit#objectivesEn">
		<h:outputText value="#{bolonhaBundle['edit']}"/>
	</h:outputLink>
	<br/><br/>
	<h:outputText value="#{bolonhaBundle['programEn']}: <br/>" style="font-weight: bold" escape="false"/>
	<h:outputText value="#{CompetenceCourseManagement.competenceCourse.programEn}<br/>" escape="false"/>
	<h:outputLink value="setCompetenceCourseAdditionalInformation.faces?competenceCourseID=#{CompetenceCourseManagement.competenceCourse.idInternal}&action=edit#programEn">
		<h:outputText value="#{bolonhaBundle['edit']}"/>
	</h:outputLink>
	<br/><br/><hr>
	<h:form>
	<h:commandLink action="competenceCoursesManagement" value="#{bolonhaBundle['back']}" />
	</h:form>
</ft:tilesView>