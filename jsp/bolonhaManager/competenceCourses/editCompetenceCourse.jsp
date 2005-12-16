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
	<h:form>
		<h:outputText value="#{bolonhaBundle['editCompetenceCourse']}" style="font-style:italic"/>
		<h2><h:outputText value="#{CompetenceCourseManagement.competenceCourse.name}"/></h2>	
		<h:panelGrid columnClasses="infocell" columns="2" border="0">
			<h:outputText value="#{bolonhaBundle['state']}: " style="font-weight: bold"/>
			<h:selectOneMenu value="#{CompetenceCourseManagement.stage}">
				<f:selectItems value="#{CompetenceCourseManagement.curricularStageTypes}" />
			</h:selectOneMenu>
		</h:panelGrid>
		<br/>
		<h:outputText value="#{bolonhaBundle['department']}: " style="font-weight: bold"/>
		<h:outputText value="#{CompetenceCourseManagement.personDepartmentName}"/><br/>
		<fc:dataRepeater value="#{CompetenceCourseManagement.competenceCourse.unit.parentUnits}" var="scientificAreaUnit">
			<h:outputText value="#{bolonhaBundle['scientificArea']}: " style="font-weight: bold"/>
			<h:outputText value="#{scientificAreaUnit.name}<br/>" escape="false"/>
		</fc:dataRepeater>		
		<h:outputText value="#{bolonhaBundle['group']}: " style="font-weight: bold"/>
			<h:outputText value="#{CompetenceCourseManagement.competenceCourse.unit.name}"/><br/>
		<br/>
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
		<br/>
		<h:outputText styleClass="error" rendered="#{!empty CompetenceCourseManagement.errorMessage}"
			value="#{bolonhaBundle[CompetenceCourseManagement.errorMessage]}<br/>" escape="false"/>
		<br/>
		<h:outputText escape="false" value="<input id='competenceCourseID' name='competenceCourseID' type='hidden' value='#{CompetenceCourseManagement.competenceCourseID}'"/><br/>
		<h:panelGrid columnClasses="alignRight infocell,infocell" columns="2" border="0" width="80%">
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
				<h:outputText value="XX" />
			</h:panelGrid>
			
			<h:outputText value="#{bolonhaBundle['regime']}: " />
			<h:selectOneMenu  value="#{CompetenceCourseManagement.regime}">
				<f:selectItems value="#{CompetenceCourseManagement.regimeTypes}" />
			</h:selectOneMenu>
		</h:panelGrid>
		<br/>
		<h:outputText value="#{bolonhaBundle['portuguese']}: " />
		<h:panelGrid columnClasses="alignRight infocell,infocell" columns="2" border="0" width="80%">
			<h:outputText value="#{bolonhaBundle['program']}: " />
			<h:inputTextarea cols="80" rows="5" value="#{CompetenceCourseManagement.program}"/>
			
			<h:outputText value="#{bolonhaBundle['objectives']}: " />
			<h:panelGroup>
				<h:outputText value="#{bolonhaBundle['generalObjectives']}: <br/>" escape="false"/>
				<h:inputTextarea cols="80" rows="5" value="#{CompetenceCourseManagement.generalObjectives}"/>
				<h:outputText value="<br/>" escape="false"/>
				<h:outputText value="#{bolonhaBundle['operationalObjectives']}: <br/>" escape="false"/>
				<h:inputTextarea cols="80" rows="5" value="#{CompetenceCourseManagement.operationalObjectives}"/>
			</h:panelGroup>
			
			<h:outputText value="#{bolonhaBundle['evaluationMethod']}: " />
			<h:inputTextarea cols="80" rows="5" value="#{CompetenceCourseManagement.evaluationMethod}"/>
			
			<h:outputText value="#{bolonhaBundle['prerequisites']}: " />
			<h:inputTextarea cols="80" rows="5" value="#{CompetenceCourseManagement.prerequisites}"/>
		</h:panelGrid>
		<br/>
		<h:outputText value="#{bolonhaBundle['english']}: " />
		<h:panelGrid columnClasses="alignRight infocell,infocell" columns="2" border="0" width="80%">
			<h:outputText value="#{bolonhaBundle['nameEn']}: " />
			<h:inputText maxlength="100" size="40" value="#{CompetenceCourseManagement.nameEn}"/>
	
			<h:outputText value="#{bolonhaBundle['programEn']}: " />
			<h:inputTextarea cols="80" rows="5" value="#{CompetenceCourseManagement.programEn}"/>
			
			<h:outputText value="#{bolonhaBundle['objectivesEn']}: " />
			<h:panelGroup>
				<h:outputText value="#{bolonhaBundle['generalObjectivesEn']}: <br/>" escape="false"/>
				<h:inputTextarea cols="80" rows="5" value="#{CompetenceCourseManagement.generalObjectivesEn}"/>
				<h:outputText value="<br/>" escape="false"/>
				<h:outputText value="#{bolonhaBundle['operationalObjectivesEn']}: <br/>" escape="false"/>
				<h:inputTextarea cols="80" rows="5" value="#{CompetenceCourseManagement.operationalObjectivesEn}"/>
			</h:panelGroup>
			
			<h:outputText value="#{bolonhaBundle['evaluationMethodEn']}: " />
			<h:inputTextarea cols="80" rows="5" value="#{CompetenceCourseManagement.evaluationMethodEn}"/>
			
			<h:outputText value="#{bolonhaBundle['prerequisitesEn']}: " />
			<h:inputTextarea cols="80" rows="5" value="#{CompetenceCourseManagement.prerequisitesEn}"/>
		</h:panelGrid>
		<br/><hr>
		<h:messages /><br/>
		<h:commandButton styleClass="inputbutton" value="#{bolonhaBundle['submit']}"
			action="#{CompetenceCourseManagement.editCompetenceCourse}"/>
		<h:commandButton immediate="true" styleClass="inputbutton" value="#{bolonhaBundle['cancel']}"
			action="competenceCoursesManagement"/>
	</h:form>
</ft:tilesView>