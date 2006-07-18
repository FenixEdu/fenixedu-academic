<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView locale="<%=request.getAttribute(org.apache.struts.Globals.LOCALE_KEY).toString()%>" definition="definition.public.department" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>
	<style type="text/css">@import "<%= request.getContextPath() %>/CSS/transitional.css";</style>
	
	<f:loadBundle basename="resources/BolonhaManagerResources" var="bolonhaBundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="enumerationBundle"/>
	<f:loadBundle basename="resources/ScientificCouncilResources" var="scouncilBundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="enumerationBundle"/>
	<f:loadBundle basename="resources/PublicDepartmentResources" var="publicDepartmentBundle"/>
	<f:loadBundle basename="resources/GlobalResources" var="globalBundle"/>

	<h:outputLink value="#{globalBundle['institution.url']}" >
		<h:outputText value="#{globalBundle['institution.name.abbreviation']}"/>
	</h:outputLink>
	&nbsp;&gt;&nbsp;
	<h:outputLink target="_blank" value="#{globalBundle['institution.url']}#{globalBundle['link.institution.structure']}">
		<h:outputText value="#{publicDepartmentBundle['structure']}"/>
	</h:outputLink>
	&nbsp;&gt;&nbsp;
	<h:outputLink value="showDepartments.faces">
		<h:outputText value="#{publicDepartmentBundle['academic.units']}"/>
	</h:outputLink>
	&nbsp;&gt;&nbsp;
	<h:outputText rendered="#{empty CompetenceCourseManagement.competenceCourse.departmentUnit.webAddress}" value="#{CompetenceCourseManagement.competenceCourse.departmentUnit.department.realName}"/>
	<h:outputLink rendered="#{!empty CompetenceCourseManagement.competenceCourse.departmentUnit.webAddress}" value="#{CompetenceCourseManagement.competenceCourse.departmentUnit.webAddress}" target="_blank">
		<h:outputText value="#{CompetenceCourseManagement.competenceCourse.departmentUnit.department.realName}"/>
	</h:outputLink>
	&nbsp;&gt;&nbsp;
	<h:outputLink value="../department/showDepartmentCompetenceCourses.faces">
		<h:outputText value="#{publicDepartmentBundle['department.courses']}"/>
		<f:param name="selectedDepartmentUnitID" value="#{CompetenceCourseManagement.competenceCourse.departmentUnit.idInternal}"/>
	</h:outputLink>
	&nbsp;&gt;&nbsp;
	<h:outputText rendered="#{!CompetenceCourseManagement.renderInEnglish}" value="#{CompetenceCourseManagement.competenceCourse.name}"/>
	<h:outputText rendered="#{CompetenceCourseManagement.renderInEnglish}" value="#{CompetenceCourseManagement.competenceCourse.nameEn}"/>
	
	<h:outputText value="<br/><br/><h1>" escape="false"/>
	<h:outputText rendered="#{!CompetenceCourseManagement.renderInEnglish}" value="#{CompetenceCourseManagement.competenceCourse.name}"/>
	<h:outputText rendered="#{CompetenceCourseManagement.renderInEnglish}" value="#{CompetenceCourseManagement.competenceCourse.nameEn}"/>
	<h:outputText value="(#{CompetenceCourseManagement.competenceCourse.acronym})"/>
	<h:outputText value="</h1>" escape="false"/>

	<h:outputText value="<br/><h2 class='arrow_bullet'>#{bolonhaBundle['area']}</h2>" escape="false"/>
	<fc:dataRepeater value="#{CompetenceCourseManagement.competenceCourse.competenceCourseGroupUnit.parentUnits}" var="scientificAreaUnit">
		<h:outputText value="<p style='margin-left: 0px;'>#{scientificAreaUnit.name} > #{CompetenceCourseManagement.competenceCourse.competenceCourseGroupUnit.name}</p>" escape="false"/>
	</fc:dataRepeater>

	<h:outputText value="<h2 class='arrow_bullet'>#{bolonhaBundle['activeCurricularPlans']}</h2>" escape="false"/>
	<h:panelGroup rendered="#{empty CompetenceCourseManagement.competenceCourse.associatedCurricularCourses}">
		<h:outputText value="<p style='margin-left: 0px;'><i>#{bolonhaBundle['noCurricularCourses']}</i></p>" escape="false"/>
	</h:panelGroup>
	<h:panelGroup rendered="#{!empty CompetenceCourseManagement.competenceCourse.associatedCurricularCourses}">
		<fc:dataRepeater value="#{CompetenceCourseManagement.competenceCourse.associatedCurricularCourses}" var="curricularCourse">
			<h:outputText value="<p style='margin-left: 0px;'>" escape="false"/>
			<h:outputLink value="../showDegreeSite.do" >
				<h:outputText value="#{curricularCourse.parentDegreeCurricularPlan.name}"/>
				<f:param name="method" value="showCurricularPlan"/>
				<f:param name="degreeID" value="#{curricularCourse.parentDegreeCurricularPlan.degree.idInternal}"/>
				<f:param name="degreeCurricularPlanID" value="#{curricularCourse.parentDegreeCurricularPlan.idInternal}"/>
			</h:outputLink>
			<h:outputText value=" > "/>
			<h:outputLink value="../degreeSite/viewCurricularCourse.faces">
				<h:outputText rendered="#{!CompetenceCourseManagement.renderInEnglish}" value="#{curricularCourse.name}" escape="false"/>
				<h:outputText rendered="#{CompetenceCourseManagement.renderInEnglish}" value="#{curricularCourse.nameEn}" escape="false"/>
				<f:param name="degreeID" value="#{curricularCourse.parentDegreeCurricularPlan.degree.idInternal}"/>
				<f:param name="degreeCurricularPlanID" value="#{curricularCourse.parentDegreeCurricularPlan.idInternal}"/>
				<f:param name="curricularCourseID" value="#{curricularCourse.idInternal}"/>
			</h:outputLink>
			<h:outputText value="</i></p>" escape="false"/>
		</fc:dataRepeater>
	</h:panelGroup>	

	<h:outputText rendered="#{!empty CompetenceCourseManagement.competenceCourse.competenceCourseLevel}" value="<h2 class='arrow_bullet'>#{bolonhaBundle['competenceCourseLevel']}</h2>" escape="false"/>
	<h:outputText rendered="#{!empty CompetenceCourseManagement.competenceCourse.competenceCourseLevel}" value="<p style='margin-left: 0px;'>#{enumerationBundle[CompetenceCourseManagement.competenceCourse.competenceCourseLevel]}</p>" escape="false"/>
		
	<h:outputText value="<h2 class='arrow_bullet'>#{bolonhaBundle['type']}</h2>" escape="false"/>
	<h:outputText rendered="#{CompetenceCourseManagement.competenceCourse.basic}" value="<p style='margin-left: 0px;'>#{bolonhaBundle['basic']}</p>" escape="false"/>
	<h:outputText rendered="#{!CompetenceCourseManagement.competenceCourse.basic}" value="<p style='margin-left: 0px;'>#{bolonhaBundle['nonBasic']}</p>" escape="false"/>
		
	<h:outputText value="<h2 class='arrow_bullet'>#{bolonhaBundle['regime']}</h2>" escape="false"/>
	<h:outputText value="<p style='margin-left: 0px;'>#{enumerationBundle[CompetenceCourseManagement.competenceCourse.regime.name]}</p>" escape="false"/>

	<!-- LESSON HOURS -->
	<h:outputText value="<h2 class='arrow_bullet'>#{bolonhaBundle['lessonHours']}</h2>" escape="false"/>
	<fc:dataRepeater value="#{CompetenceCourseManagement.sortedCompetenceCourseLoads}" var="competenceCourseLoad" rowCountVar="numberOfElements">
		<h:outputText value="<p style='margin-top: 30px; margin-left: 0px;'><strong><em>#{competenceCourseLoad.order}º #{bolonhaBundle['semester']}</em></strong></p>" escape="false"
			rendered="#{CompetenceCourseManagement.competenceCourse.regime.name == 'ANUAL' && numberOfElements == 2}"/>
		
		<h:outputText value="<p style='margin-left: 0px;'>#{bolonhaBundle['theoreticalLesson']}: " escape="false"/>
		<h:outputText value="#{competenceCourseLoad.theoreticalHours} h/#{bolonhaBundle['lowerCase.week']}</p>" escape="false"/>

		<h:outputText rendered="#{competenceCourseLoad.problemsHours != 0.0}" value="<p style='margin-left: 0px;'>#{bolonhaBundle['problemsLesson']}: " escape="false"/>
		<h:outputText rendered="#{competenceCourseLoad.problemsHours != 0.0}" value="#{competenceCourseLoad.problemsHours} h/#{bolonhaBundle['lowerCase.week']}</p>" escape="false"/>

		<h:outputText rendered="#{competenceCourseLoad.laboratorialHours != 0.0}" value="<p style='margin-left: 0px;'>#{bolonhaBundle['laboratorialLesson']}: " escape="false"/>
		<h:outputText rendered="#{competenceCourseLoad.laboratorialHours != 0.0}" value="#{competenceCourseLoad.laboratorialHours} h/#{bolonhaBundle['lowerCase.week']}</p>" escape="false"/>

		<h:outputText rendered="#{competenceCourseLoad.seminaryHours != 0.0}" value="<p style='margin-left: 0px;'>#{bolonhaBundle['seminary']}: " escape="false"/>
		<h:outputText rendered="#{competenceCourseLoad.seminaryHours != 0.0}" value="#{competenceCourseLoad.seminaryHours} h/#{bolonhaBundle['lowerCase.week']}</p>" escape="false"/>

		<h:outputText rendered="#{competenceCourseLoad.fieldWorkHours != 0.0}" value="<p style='margin-left: 0px;'>#{bolonhaBundle['fieldWork']}: " escape="false"/>
		<h:outputText rendered="#{competenceCourseLoad.fieldWorkHours != 0.0}" value="#{competenceCourseLoad.fieldWorkHours} h/#{bolonhaBundle['lowerCase.week']}</p>" escape="false"/>

		<h:outputText rendered="#{competenceCourseLoad.trainingPeriodHours != 0.0}" value="<p style='margin-left: 0px;'>#{bolonhaBundle['trainingPeriod']}: " escape="false"/>
		<h:outputText rendered="#{competenceCourseLoad.trainingPeriodHours != 0.0}" value="#{competenceCourseLoad.trainingPeriodHours} h/#{bolonhaBundle['lowerCase.week']}</p>" escape="false"/>

		<h:outputText rendered="#{competenceCourseLoad.tutorialOrientationHours != 0.0}" value="<p style='margin-left: 0px;'>#{bolonhaBundle['tutorialOrientation']}: " escape="false"/>
		<h:outputText rendered="#{competenceCourseLoad.tutorialOrientationHours != 0.0}" value="#{competenceCourseLoad.tutorialOrientationHours} h/#{bolonhaBundle['lowerCase.week']}</p>" escape="false"/>

		<h:outputText rendered="#{competenceCourseLoad.autonomousWorkHours != 0.0}" value="<p style='margin-left: 0px;'>#{bolonhaBundle['autonomousWork']}: " escape="false"/>
		<h:outputText rendered="#{competenceCourseLoad.autonomousWorkHours != 0.0}" value="#{competenceCourseLoad.autonomousWorkHours} h/#{bolonhaBundle['lowerCase.semester']}</p>" escape="false"/>

		<h:outputText value="<p style='margin-left: 0px;'><strong>#{bolonhaBundle['ectsCredits']}: "escape="false"/>
		<h:outputText value="#{competenceCourseLoad.ectsCredits}</strong></p>" escape="false"/>
	</fc:dataRepeater>	

	<!-- OBJECTIVES -->
	<h:panelGroup rendered="#{!empty CompetenceCourseManagement.competenceCourse.objectives}">
		<h:outputText value="<h2 class='arrow_bullet'>#{bolonhaBundle['objectives']}</h2>" escape="false"/>
		<h:outputText value="<p style='margin-left: 0px;'>" escape="false"/>
		<fc:extendedOutputText rendered="#{!CurricularCourseManagement.renderInEnglish}" value="#{CompetenceCourseManagement.competenceCourse.objectives}" linebreak="true"/>
		<fc:extendedOutputText rendered="#{CurricularCourseManagement.renderInEnglish}" value="#{CompetenceCourseManagement.competenceCourse.objectivesEn}" linebreak="true"/>		
		<h:outputText value="</p>" escape="false"/>
	</h:panelGroup>
	
	<!-- PROGRAM -->
	<h:panelGroup rendered="#{!empty CompetenceCourseManagement.competenceCourse.program}">
		<h:outputText value="<h2 class='arrow_bullet'>#{bolonhaBundle['program']}</h2>" escape="false"/>
		<h:outputText value="<p style='margin-left: 0px;'>" escape="false"/>
		<fc:extendedOutputText rendered="#{!CurricularCourseManagement.renderInEnglish}" value="#{CompetenceCourseManagement.competenceCourse.program}" linebreak="true"/>
		<fc:extendedOutputText rendered="#{CurricularCourseManagement.renderInEnglish}" value="#{CompetenceCourseManagement.competenceCourse.programEn}" linebreak="true"/>		
		<h:outputText value="</p>" escape="false"/>
	</h:panelGroup>
	
	<!-- EVALUATION METHOD -->
	<h:panelGroup rendered="#{!empty CompetenceCourseManagement.competenceCourse.evaluationMethod}">
		<h:outputText value="<h2 class='arrow_bullet'>#{bolonhaBundle['evaluationMethod']}</h2>" escape="false"/>
		<h:outputText value="<p style='margin-left: 0px;'>" escape="false"/>
		<fc:extendedOutputText rendered="#{!CurricularCourseManagement.renderInEnglish}" value="#{CompetenceCourseManagement.competenceCourse.evaluationMethod}" linebreak="true"/>
		<fc:extendedOutputText rendered="#{CurricularCourseManagement.renderInEnglish}" value="#{CompetenceCourseManagement.competenceCourse.evaluationMethodEn}" linebreak="true"/>
		<h:outputText value="</p>" escape="false"/>
	</h:panelGroup>
	
	
	<!-- BIBLIOGRAPHIC REFERENCE -->
	<h:outputText value="<h2 class='arrow_bullet'>#{bolonhaBundle['bibliographicReference']}</h2>" escape="false"/>
	<h:outputText value="<h2 class='greytxt' style='margin-top: 10px; margin-left: 0px;'>#{enumerationBundle['MAIN']}</h2>" escape="false"/>
	<h:panelGroup rendered="#{empty CompetenceCourseManagement.mainBibliographicReferences}">
		<h:outputText value="<p style='margin-left: 0px;'><i>#{bolonhaBundle['noBibliographicReferences']}</i></p>" escape="false"/>
	</h:panelGroup>
	<fc:dataRepeater value="#{CompetenceCourseManagement.mainBibliographicReferences}" var="bibliographicReference" rendered="#{!empty CompetenceCourseManagement.mainBibliographicReferences}">
		<h:panelGroup rendered="#{bibliographicReference.type.name == 'MAIN'}">
			<h:outputText value="<div class='lfloat mbottom2'>" escape="false"/>
			<h:outputText value="<p class='mvert0'>" escape="false"/>
			<h:outputText value="<label>#{bolonhaBundle['title']}:</label>" escape="false"/>
			<h:outputText rendered="#{bibliographicReference.url != 'http://'}" value="<a href='#{bibliographicReference.url}'>#{bibliographicReference.title}</a>" escape="false"/>
			<h:outputText rendered="#{bibliographicReference.url == 'http://'}" value="#{bibliographicReference.title}" escape="false"/>
			<h:outputText value="</p>" escape="false"/>
			
			<h:outputText value="<p class='mvert0'>" escape="false"/>
			<h:outputText value="<label>#{bolonhaBundle['author']}:</label>" escape="false"/>
			<h:outputText value="<em>#{bibliographicReference.authors}</em>" escape="false"/>
			<h:outputText value="</p>" escape="false"/>			
			
			<h:outputText value="<p class='mvert0'>" escape="false"/>
			<h:outputText value="<label>#{bolonhaBundle['year']}:</label>" escape="false"/>
			<h:outputText value="#{bibliographicReference.year}" escape="false"/>
			<h:outputText value="</p>" escape="false"/>

			<h:outputText value="<p class='mvert0'>" escape="false"/>			
			<h:outputText value="<label>#{bolonhaBundle['reference']}:</label>" escape="false"/>
			<h:outputText value="#{bibliographicReference.reference}" escape="false"/>
			<h:outputText value="</p>" escape="false"/>
			<h:outputText value="</div>" escape="false"/>			
		</h:panelGroup>
	</fc:dataRepeater>
	
	<h:outputText value="<h2 class='greytxt' style='margin-top: 10px; margin-left: 0px;'>#{enumerationBundle['SECONDARY']}</h2>" escape="false"/>
	<h:panelGroup rendered="#{empty CompetenceCourseManagement.secondaryBibliographicReferences}">
		<h:outputText value="<p style='margin-left: 0px;'><i>#{bolonhaBundle['noBibliographicReferences']}</i></p>" escape="false"/>
	</h:panelGroup>	
	<fc:dataRepeater value="#{CompetenceCourseManagement.secondaryBibliographicReferences}" var="bibliographicReference" rendered="#{!empty CompetenceCourseManagement.secondaryBibliographicReferences}">
		<h:panelGroup rendered="#{bibliographicReference.type.name == 'SECONDARY'}">
			<h:outputText value="<div class='lfloat mbottom2'>" escape="false"/>
			<h:outputText value="<p class='mvert0'>" escape="false"/>
			<h:outputText value="<label>#{bolonhaBundle['title']}:</label>" escape="false"/>
			<h:outputText rendered="#{bibliographicReference.url != 'http://'}" value="<a href='#{bibliographicReference.url}'>#{bibliographicReference.title}</a>" escape="false"/>
			<h:outputText rendered="#{bibliographicReference.url == 'http://'}" value="#{bibliographicReference.title}" escape="false"/>
			<h:outputText value="</p>" escape="false"/>
			
			<h:outputText value="<p class='mvert0'>" escape="false"/>
			<h:outputText value="<label>#{bolonhaBundle['author']}:</label>" escape="false"/>
			<h:outputText value="<em>#{bibliographicReference.authors}</em>" escape="false"/>
			<h:outputText value="</p>" escape="false"/>	
			
			<h:outputText value="<p class='mvert0'>" escape="false"/>
			<h:outputText value="<label>#{bolonhaBundle['year']}:</label>" escape="false"/>
			<h:outputText value="#{bibliographicReference.year}" escape="false"/>
			<h:outputText value="</p>" escape="false"/>

			<h:outputText value="<p class='mvert0'>" escape="false"/>			
			<h:outputText value="<label>#{bolonhaBundle['reference']}:</label>" escape="false"/>
			<h:outputText value="#{bibliographicReference.reference}" escape="false"/>
			<h:outputText value="</p>" escape="false"/>
			<h:outputText value="</div>" escape="false"/>			
		</h:panelGroup>
	</fc:dataRepeater>

	<h:form>
		<h:outputText escape="false" value="<input alt='input.competenceCourseID' id='competenceCourseID' name='competenceCourseID' type='hidden' value='#{CompetenceCourseManagement.competenceCourse.idInternal}'/>"/>
 		<h:outputText escape="false" value="<input alt='input.executionPeriodOID' id='executionPeriodOID' name='executionPeriodOID' type='hidden' value='#{CurricularCourseManagement.executionPeriodOID}'/>"/>
	</h:form>

</ft:tilesView>
