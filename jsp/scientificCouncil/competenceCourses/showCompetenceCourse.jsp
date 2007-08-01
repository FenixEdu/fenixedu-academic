<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="scientificCouncil.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/ScientificCouncilResources" var="scouncilBundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="enumerationBundle"/>

	<h:outputText value="<em>#{scouncilBundle['competenceCourse']}</em>" escape="false" />
	<h:outputText value="<h2>#{CompetenceCourseManagement.name}</h2>" escape="false"/>

	<h:form>
		<h:outputText escape="false" value="<input alt='input.competenceCourseID' id='competenceCourseID' name='competenceCourseID' type='hidden' value='#{CompetenceCourseManagement.competenceCourseID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.selectedDepartmentUnitID' id='selectedDepartmentUnitID' name='selectedDepartmentUnitID' type='hidden' value='#{CompetenceCourseManagement.selectedDepartmentUnitID}'/>"/>

		<h:selectOneMenu value="#{CompetenceCourseManagement.executionYearID}">
			<f:selectItems value="#{CompetenceCourseManagement.executionYears}" />
		</h:selectOneMenu>

		<h:commandButton alt="#{htmlAltBundle['commandButton.associate']}" value="#{scouncilBundle['button.researchActivity.choose']}"
			action="competenceCourseManagementPostBack"/>	
	</h:form>
	
	<h:outputText value="<ul class='nobullet padding1 indent0 mtop15'>" escape="false"/>
	<h:outputText value="<li><strong>#{scouncilBundle['department']}: </strong>" escape="false"/>
	<h:outputText value="#{CompetenceCourseManagement.competenceCourse.departmentUnit.department.realName}</li>" escape="false"/>
	<fc:dataRepeater value="#{CompetenceCourseManagement.competenceCourse.competenceCourseGroupUnit.parentUnits}" var="scientificAreaUnit">
		<h:outputText value="<li><strong>#{scouncilBundle['area']}: </strong>" escape="false"/>
		<h:outputText value="#{scientificAreaUnit.name} > #{CompetenceCourseManagement.competenceCourse.competenceCourseGroupUnit.name}</li>" escape="false"/>
	</fc:dataRepeater>		
	<h:outputText value="</ul>" escape="false"/>
		
	<h:outputText value="<p class='mtop15 mbottom0'><strong>#{scouncilBundle['activeCurricularPlans']}: </strong></p>" escape="false"/>
	<h:panelGroup rendered="#{empty CompetenceCourseManagement.competenceCourse.associatedCurricularCourses}">
		<h:outputText value="<i>#{scouncilBundle['noCurricularCourses']}</i>" escape="false"/>
	</h:panelGroup>
	<h:panelGroup rendered="#{!empty CompetenceCourseManagement.competenceCourse.associatedCurricularCourses}">
		<h:outputText value="<ul class='mtop0 mbottom3'>" escape="false"/>
		<fc:dataRepeater value="#{CompetenceCourseManagement.competenceCourse.associatedCurricularCourses}" var="curricularCourse">			
			<h:outputText value="<li>" escape="false"/>
			<h:outputLink value="../curricularPlans/viewCurricularPlan.faces" target="_blank">
				<h:outputText value="#{curricularCourse.parentDegreeCurricularPlan.name}" escape="false"/>
				<f:param name="action" value="close"/>
				<f:param name="organizeBy" value="groups"/>
				<f:param name="showRules" value="false"/>
				<f:param name="hideCourses" value="false"/>
				<f:param name="dcpId" value="#{curricularCourse.parentDegreeCurricularPlan.idInternal}"/>
			</h:outputLink>
			<h:outputText value=" > "/>
			<h:outputLink value="../curricularPlans/viewCurricularCourse.faces" target="_blank">
				<h:outputText value="#{curricularCourse.name}" escape="false"/>
				<f:param name="action" value="close"/>
				<f:param name="curricularCourseID" value="#{curricularCourse.idInternal}"/>
			</h:outputLink>
			<h:outputText value="</li>" escape="false"/>
		</fc:dataRepeater>
		<h:outputText value="</ul>" escape="false"/>
	</h:panelGroup>	

	<h:outputText value="<div class='simpleblock3 mtop2'>" escape="false"/>
	<h:outputText value="<p>#{scouncilBundle['state']}: " escape="false"/>
	<h:outputText rendered="#{CompetenceCourseManagement.stage == 'DRAFT'}" value="<span class='highlight1'>#{enumerationBundle[CompetenceCourseManagement.competenceCourse.curricularStage.name]}</span></p>" escape="false"/>
	<h:outputText rendered="#{CompetenceCourseManagement.stage == 'PUBLISHED'}" value="<span class='highlight3'>#{enumerationBundle[CompetenceCourseManagement.competenceCourse.curricularStage.name]}</span></p>" escape="false"/>
	<h:outputText rendered="#{CompetenceCourseManagement.stage == 'APPROVED'}" value="<span class='highlight4'>#{enumerationBundle[CompetenceCourseManagement.competenceCourse.curricularStage.name]}</span></p>" escape="false"/>		
	<h:outputText value="<ul class='nobullet padding1 indent0 mbottom0'>" escape="false"/>	
	<h:outputText value="<li>#{scouncilBundle['name']} (pt):" escape="false"/>
	<h:outputText value="<strong>#{CompetenceCourseManagement.name}</strong></li>" escape="false"/>
	<h:outputText value="<li>#{scouncilBundle['nameEn']} (en): " escape="false"/>
	<h:outputText value="<strong>#{CompetenceCourseManagement.competenceCourse.nameEn}</strong></li>" escape="false" />
	<h:panelGroup rendered="#{!empty CompetenceCourseManagement.acronym}">
		<h:outputText value="<li>#{scouncilBundle['acronym']}: " escape="false"/>
		<h:outputText value="#{CompetenceCourseManagement.acronym}</li>" escape="false"/>
	</h:panelGroup>
	<h:outputText value="<li>#{scouncilBundle['competenceCourseLevel']}: " escape="false"/>
	<h:outputText value="#{enumerationBundle[CompetenceCourseManagement.competenceCourseLevel]}</li>" escape="false" rendered="#{!empty CompetenceCourseManagement.competenceCourseLevel}"/>	
	<h:outputText value="<em>#{scouncilBundle['label.notDefined']}</em></li>" escape="false" rendered="#{empty CompetenceCourseManagement.competenceCourseLevel}"/>	
	<h:outputText value="<li>#{scouncilBundle['type']}: " escape="false"/>
	<h:outputText value="#{scouncilBundle['basic']}</li>" rendered="#{CompetenceCourseManagement.basic}" escape="false"/>
	<h:outputText value="#{scouncilBundle['nonBasic']}</li>" rendered="#{!CompetenceCourseManagement.basic}" escape="false"/>
	<h:outputText value="</ul></div>" escape="false"/>
	
	<h:outputText value="<div class='simpleblock3 mtop2'>" escape="false"/>
	<h:outputText value="<ul class='nobullet padding1 indent0 mbottom0'>" escape="false"/>
	<h:outputText value="<li><strong>#{scouncilBundle['regime']}: </strong>" escape="false"/>
	<h:outputText value="#{enumerationBundle[CompetenceCourseManagement.regime]}</li>" escape="false"/>	
	<h:outputText value="<li><strong>#{scouncilBundle['lessonHours']}: </strong>" escape="false"/>	
	<fc:dataRepeater value="#{CompetenceCourseManagement.sortedCompetenceCourseLoads}" var="competenceCourseLoad" rowCountVar="numberOfElements">
		<h:outputText value="<p class='mbotton0'><em>#{competenceCourseLoad.order}º #{scouncilBundle['semester']}</em></p>" escape="false"
			rendered="#{CompetenceCourseManagement.regime == 'ANUAL' && numberOfElements == 2}"/>
		
		<h:outputText value="<ul class='mvert0'>" escape="false"/>
		<h:outputText value="<li>#{scouncilBundle['theoreticalLesson']}: " escape="false"/>
		<h:outputText value="#{competenceCourseLoad.theoreticalHours} h/#{scouncilBundle['lowerCase.week']}</li>" escape="false"/>

		<h:outputText rendered="#{competenceCourseLoad.problemsHours != 0.0}" value="<li>#{scouncilBundle['problemsLesson']}: " escape="false"/>
		<h:outputText rendered="#{competenceCourseLoad.problemsHours != 0.0}" value="#{competenceCourseLoad.problemsHours} h/#{scouncilBundle['lowerCase.week']}</li>" escape="false"/>

		<h:outputText rendered="#{competenceCourseLoad.laboratorialHours != 0.0}" value="<li>#{scouncilBundle['laboratorialLesson']}: " escape="false"/>
		<h:outputText rendered="#{competenceCourseLoad.laboratorialHours != 0.0}" value="#{competenceCourseLoad.laboratorialHours} h/#{scouncilBundle['lowerCase.week']}</li>" escape="false"/>

		<h:outputText rendered="#{competenceCourseLoad.seminaryHours != 0.0}" value="<li>#{scouncilBundle['seminary']}: " escape="false"/>
		<h:outputText rendered="#{competenceCourseLoad.seminaryHours != 0.0}" value="#{competenceCourseLoad.seminaryHours} h/#{scouncilBundle['lowerCase.week']}</li>" escape="false"/>

		<h:outputText rendered="#{competenceCourseLoad.fieldWorkHours != 0.0}" value="<li>#{scouncilBundle['fieldWork']}: " escape="false"/>
		<h:outputText rendered="#{competenceCourseLoad.fieldWorkHours != 0.0}" value="#{competenceCourseLoad.fieldWorkHours} h/#{scouncilBundle['lowerCase.week']}</li>" escape="false"/>

		<h:outputText rendered="#{competenceCourseLoad.trainingPeriodHours != 0.0}" value="<li>#{scouncilBundle['trainingPeriod']}: " escape="false"/>
		<h:outputText rendered="#{competenceCourseLoad.trainingPeriodHours != 0.0}" value="#{competenceCourseLoad.trainingPeriodHours} h/#{scouncilBundle['lowerCase.week']}</li>" escape="false"/>

		<h:outputText rendered="#{competenceCourseLoad.tutorialOrientationHours != 0.0}" value="<li>#{scouncilBundle['tutorialOrientation']}: " escape="false"/>
		<h:outputText rendered="#{competenceCourseLoad.tutorialOrientationHours != 0.0}" value="#{competenceCourseLoad.tutorialOrientationHours} h/#{scouncilBundle['lowerCase.week']}</li>" escape="false"/>

		<h:outputText rendered="#{competenceCourseLoad.autonomousWorkHours != 0.0}" value="<li>#{scouncilBundle['autonomousWork']}: " escape="false"/>
		<h:outputText rendered="#{competenceCourseLoad.autonomousWorkHours != 0.0}" value="#{competenceCourseLoad.autonomousWorkHours} h/#{scouncilBundle['lowerCase.semester']}</li>" escape="false"/>

		<h:outputText value="<li><strong>#{scouncilBundle['ectsCredits']}: "escape="false"/>
		<h:outputText value="#{competenceCourseLoad.ectsCredits}</strong></li>" escape="false"/>
		<h:outputText value="</ul>" escape="false"/>
	</fc:dataRepeater>	
	<h:outputText value="</li>" escape="false"/>
	<h:outputText value="</ul></div>" escape="false"/>

	<h:outputText value="<div class='simpleblock3 mtop2'>" escape="false"/>
	<h:outputText value="<p class='mbottom0'><em>#{scouncilBundle['portuguese']}: </em></p>" escape="false"/>
	<h:outputText value="<table class='showinfo1 emphasis2'>" escape="false"/>
	<h:outputText value="<tr><th class='aleft'>#{scouncilBundle['objectives']}:</th>" escape="false"/>
	<h:outputText value="<td>" escape="false"/>
	<fc:extendedOutputText value="#{CompetenceCourseManagement.objectives}" linebreak="true"/>
	<h:outputText value="<i>#{scouncilBundle['empty.field']}</i>" escape="false" rendered="#{empty CompetenceCourseManagement.objectives}"/>
	<h:outputText value="</td></tr>" escape="false"/>
	<h:outputText value="<tr><th class='aleft'>#{scouncilBundle['program']}:</th>" escape="false"/>
	<h:outputText value="<td>" escape="false"/>
	<fc:extendedOutputText value="#{CompetenceCourseManagement.program}" linebreak="true"/>
	<h:outputText value="<i>#{scouncilBundle['empty.field']}</i>" escape="false" rendered="#{empty CompetenceCourseManagement.program}"/>
	<h:outputText value="</td></tr>" escape="false"/>
	<h:outputText value="<tr><th class='aleft'>#{scouncilBundle['evaluationMethod']}:</th>" escape="false"/>
	<h:outputText value="<td>" escape="false"/>
	<fc:extendedOutputText value="#{CompetenceCourseManagement.evaluationMethod}" linebreak="true"/>
	<h:outputText value="<i>#{scouncilBundle['empty.field']}</i>" escape="false" rendered="#{empty CompetenceCourseManagement.evaluationMethod}"/>
	<h:outputText value="</td></tr>" escape="false"/>
	<h:outputText value="</table>" escape="false"/>
	
	<h:outputText value="<p class='mbottom0'><em>#{scouncilBundle['english']}: </em></p>" escape="false"/>
	<h:outputText value="<table class='showinfo1 emphasis2'>" escape="false"/>
	<h:outputText value="<tr><th class='aleft'>#{scouncilBundle['objectivesEn']}:</th>" escape="false"/>
	<h:outputText value="<td>" escape="false"/>
	<fc:extendedOutputText value="#{CompetenceCourseManagement.objectivesEn}" linebreak="true"/>
	<h:outputText value="<i>#{scouncilBundle['empty.field']}</i>" escape="false" rendered="#{empty CompetenceCourseManagement.objectivesEn}"/>
	<h:outputText value="</td></tr>" escape="false"/>
	<h:outputText value="<tr><th class='aleft'>#{scouncilBundle['programEn']}:</th>" escape="false"/>
	<h:outputText value="<td>" escape="false"/>
	<fc:extendedOutputText value="#{CompetenceCourseManagement.programEn}" linebreak="true"/>
	<h:outputText value="<i>#{scouncilBundle['empty.field']}</i>" escape="false" rendered="#{empty CompetenceCourseManagement.programEn}"/>
	<h:outputText value="</td></tr>" escape="false"/>
	<h:outputText value="<tr><th class='aleft'>#{scouncilBundle['evaluationMethodEn']}:</th>" escape="false"/>
	<h:outputText value="<td>" escape="false"/>
	<fc:extendedOutputText value="#{CompetenceCourseManagement.evaluationMethodEn}" linebreak="true"/>	
	<h:outputText value="<i>#{scouncilBundle['empty.field']}</i>" escape="false" rendered="#{empty CompetenceCourseManagement.evaluationMethodEn}"/>
	<h:outputText value="</td></tr>" escape="false"/>
	<h:outputText value="</table>" escape="false"/>
	<h:outputText value="</div>" escape="false"/>

	<h:outputText value="<div class='simpleblock3 mtop2'>" escape="false"/>

	<h:outputText value="<p><b>#{scouncilBundle['bibliographicReference']} #{enumerationBundle['MAIN']}</b></p>" escape="false"/>
	<h:panelGroup rendered="#{empty CompetenceCourseManagement.mainBibliographicReferences}">
		<h:outputText value="<i>#{scouncilBundle['noBibliographicReferences']}</i><br/>" escape="false"/>
	</h:panelGroup>	
	<fc:dataRepeater value="#{CompetenceCourseManagement.mainBibliographicReferences}" var="bibliographicReference" rendered="#{!empty CompetenceCourseManagement.mainBibliographicReferences}">
		<h:panelGroup rendered="#{bibliographicReference.type.name == 'MAIN'}">
			<h:outputText value="<ul class='nobullet cboth mbottom2'>" escape="false"/>					
			<h:outputText value="<li><span class='fleft width100px' style='padding-right: 10px;'>#{scouncilBundle['title']}:</span>" escape="false"/>
			<h:outputText value="<a href='#{bibliographicReference.url}'>#{bibliographicReference.title}</a></li>" rendered="#{bibliographicReference.url != 'http://'}" escape="false"/>
			<h:outputText value="#{bibliographicReference.title}</li>" rendered="#{bibliographicReference.url == 'http://'}" escape="false"/>			
			
			<h:outputText value="<li><span class='fleft width100px' style='padding-right: 10px;'>#{scouncilBundle['author']}:</span>" escape="false"/>
			<h:outputText value="<em>#{bibliographicReference.authors}</em></li>" escape="false"/>
			
			<h:outputText value="<li><span class='fleft width100px' style='padding-right: 10px;'>#{scouncilBundle['year']}:</span>" escape="false"/>
			<h:outputText value="#{bibliographicReference.year}</li>" escape="false"/>
			
			<h:outputText value="<li><span class='fleft width100px' style='padding-right: 10px;'>#{scouncilBundle['reference']}:</span>" escape="false"/>
			<h:outputText value="#{bibliographicReference.reference}</li>" escape="false"/>
			
			<h:outputText value="</ul>" escape="false"/>
		</h:panelGroup>
	</fc:dataRepeater>
	
	<h:outputText value="<p class='mtop15'><b>#{scouncilBundle['bibliographicReference']} #{enumerationBundle['SECONDARY']}</b></p>" escape="false"/>
	<h:panelGroup rendered="#{empty CompetenceCourseManagement.secondaryBibliographicReferences}">
		<h:outputText value="<i>#{scouncilBundle['noBibliographicReferences']}</i><br/>" escape="false"/>
	</h:panelGroup>	
	<fc:dataRepeater value="#{CompetenceCourseManagement.secondaryBibliographicReferences}" var="bibliographicReference" rendered="#{!empty CompetenceCourseManagement.secondaryBibliographicReferences}">
		<h:panelGroup rendered="#{bibliographicReference.type.name == 'SECONDARY'}">
			<h:outputText value="<ul class='nobullet cboth mbottom2'>" escape="false"/>					
			<h:outputText value="<li><span class='fleft width100px' style='padding-right: 10px;'>#{scouncilBundle['title']}:</span>" escape="false"/>
			<h:outputText value="<a href='#{bibliographicReference.url}'>#{bibliographicReference.title}</a></li>" rendered="#{bibliographicReference.url != 'http://'}" escape="false"/>
			<h:outputText value="#{bibliographicReference.title}</li>" rendered="#{bibliographicReference.url == 'http://'}" escape="false"/>			
				
			<h:outputText value="<li><span class='fleft width100px' style='padding-right: 10px;'>#{scouncilBundle['author']}:</span>" escape="false"/>
			<h:outputText value="<em>#{bibliographicReference.authors}</em></li>" escape="false"/>
			
			<h:outputText value="<li><span class='fleft width100px' style='padding-right: 10px;'>#{scouncilBundle['year']}:</span>" escape="false"/>
			<h:outputText value="#{bibliographicReference.year}</li>" escape="false"/>
			
			<h:outputText value="<li><span class='fleft width100px' style='padding-right: 10px;'>#{scouncilBundle['reference']}:</span>" escape="false"/>
			<h:outputText value="#{bibliographicReference.reference}</li>" escape="false"/>
			
			<h:outputText value="</ul>" escape="false"/>
		</h:panelGroup>
	</fc:dataRepeater>
	<h:outputText value="</div>" escape="false"/>

	<h:form>
		<h:outputText escape="false" value="<input alt='input.competenceCourseID' id='competenceCourseID' name='competenceCourseID' type='hidden' value='#{CompetenceCourseManagement.competenceCourse.idInternal}'/>"/>
		<h:outputText escape="false" value="<input alt='input.action' id='action' name='action' type='hidden' value='#{CompetenceCourseManagement.action}'/>"/>
		<h:outputText escape="false" value="<input alt='input.selectedDepartmentUnitID' id='selectedDepartmentUnitID' name='selectedDepartmentUnitID' type='hidden' value='#{CompetenceCourseManagement.selectedDepartmentUnitID}'/>"/>
		
		<h:panelGroup rendered="#{!empty CompetenceCourseManagement.action}">
			<h:commandButton alt="#{htmlAltBundle['commandButton.back']}" immediate="true" styleClass="inputbutton" action="competenceCoursesManagement" value="#{scouncilBundle['back']}" />
		</h:panelGroup>
		
		<h:panelGroup rendered="#{empty CompetenceCourseManagement.action}">
			<h:commandButton alt="#{htmlAltBundle['commandButton.close']}" immediate="true" styleClass="inputbutton" onclick="window.close()" value="#{scouncilBundle['close']}" />
		</h:panelGroup>
	</h:form>
</ft:tilesView>