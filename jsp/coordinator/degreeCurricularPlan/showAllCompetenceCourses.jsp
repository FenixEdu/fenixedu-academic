<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="coordinatorDegreeCurricularPlan" attributeName="body-inline">
	<f:loadBundle basename="resources/ScientificCouncilResources" var="scouncilBundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="enumerationBundle"/>

	<fc:dataRepeater rendered="#{!empty CurricularCourseManagement.degreeCurricularPlanCompetenceCourses}" value="#{CurricularCourseManagement.degreeCurricularPlanCompetenceCourses}" var="competenceCourse">

		<h:outputText value="<em>#{scouncilBundle['competenceCourse']}</em>" escape="false" />
		<h:outputText value="<h2>#{competenceCourse.name} " escape="false"/>
		<h:outputText rendered="#{!empty competenceCourse.acronym}" value="(#{competenceCourse.acronym})" escape="false"/>
		<h:outputText value="</h2>" escape="false"/>		

		<h:outputText value="<ul class='nobullet padding1 indent0 mtop3'>" escape="false"/>
		<h:outputText value="<li><strong>#{scouncilBundle['department']}: </strong>" escape="false"/>
		<h:outputText value="#{competenceCourse.departmentUnit.department.realName}</li>" escape="false"/>
		<fc:dataRepeater value="#{competenceCourse.competenceCourseGroupUnit.parentUnits}" var="scientificAreaUnit">
			<h:outputText value="<li><strong>#{scouncilBundle['area']}: </strong>" escape="false"/>
			<h:outputText value="#{scientificAreaUnit.name} > #{competenceCourse.competenceCourseGroupUnit.name}</li>" escape="false"/>
		</fc:dataRepeater>		
		<h:outputText value="</ul>" escape="false"/>

		<h:outputText value="<p class='mtop2 mbottom0'><strong>#{scouncilBundle['activeCurricularPlans']}: </strong></p>" escape="false"/>
		<h:panelGroup rendered="#{empty competenceCourse.associatedCurricularCourses}">
			<h:outputText value="<i>#{scouncilBundle['noCurricularCourses']}</i>" escape="false"/>
		</h:panelGroup>
		<h:panelGroup rendered="#{!empty competenceCourse.associatedCurricularCourses}">
			<h:outputText value="<ul class='mtop0 mbottom3'>" escape="false"/>
			<fc:dataRepeater value="#{competenceCourse.associatedCurricularCourses}" var="curricularCourse">			
				<h:outputText value="<li>" escape="false"/>
				<h:outputLink value="../curricularPlans/viewCurricularPlan.faces" target="_blank">
					<h:outputText value="#{curricularCourse.parentDegreeCurricularPlan.name}" escape="false"/>
					<f:param name="action" value="close"/>
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
		<h:outputText value="<p><strong>#{scouncilBundle['state']}: </strong>" escape="false"/>
		<h:outputText value="<span class='highlight1'>#{enumerationBundle[competenceCourse.curricularStage.name]}</span></p>" escape="false"/>
		<h:outputText value="<ul class='nobullet padding1 indent0 mbottom0'>" escape="false"/>	
		<h:outputText value="<li><strong>#{scouncilBundle['name']} (pt): </strong>" escape="false"/>
		<h:outputText value="#{competenceCourse.name}</li>" escape="false"/>
		<h:outputText value="<li><strong>#{scouncilBundle['nameEn']} (en): </strong>" escape="false"/>
		<h:outputText value="#{competenceCourse.nameEn}</li>" escape="false" />
		<h:panelGroup rendered="#{!empty competenceCourse.acronym}"> 
			<h:outputText value="<li><strong>#{scouncilBundle['acronym']}: </strong>" escape="false"/>
			<h:outputText value="#{competenceCourse.acronym}</li>" escape="false"/>
		</h:panelGroup>
		<h:outputText value="<li><strong>#{scouncilBundle['competenceCourseLevel']}: </strong>" escape="false"/>
		<h:outputText value="#{enumerationBundle[competenceCourse.competenceCourseLevel]}</li>" escape="false" rendered="#{!empty competenceCourse.competenceCourseLevel}"/>	
		<h:outputText value="<em>#{scouncilBundle['label.notDefined']}</em></li>" escape="false" rendered="#{empty competenceCourse.competenceCourseLevel}"/>
		<h:outputText value="<li><strong>#{scouncilBundle['type']}: </strong>" escape="false"/>
		<h:outputText value="#{scouncilBundle['basic']}</li>" rendered="#{competenceCourse.basic}" escape="false"/>
		<h:outputText value="#{scouncilBundle['nonBasic']}</li>" rendered="#{!competenceCourse.basic}" escape="false"/>
		<h:outputText value="</ul></div>" escape="false"/>
		
		<h:outputText value="<div class='simpleblock3 mtop2'>" escape="false"/>
		<h:outputText value="<ul class='nobullet padding1 indent0 mbottom0'>" escape="false"/>
		<h:outputText value="<li><strong>#{scouncilBundle['regime']}: </strong>" escape="false"/>
		<h:outputText value="#{enumerationBundle[competenceCourse.regime.name]}</li>" escape="false"/>	
		<h:outputText value="<li><strong>#{scouncilBundle['lessonHours']}: </strong>" escape="false"/>	

		<fc:dataRepeater value="#{competenceCourse.sortedCompetenceCourseLoads}" var="competenceCourseLoad" rowCountVar="numberOfElements">
			<h:outputText value="<p class='mbotton0'><em>#{competenceCourseLoad.order}º #{scouncilBundle['semester']}</em></p>" escape="false"
				rendered="#{competenceCourse.regime.name == 'ANUAL' && numberOfElements == 2}"/>
			
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
		<h:outputText value="<tr><th>#{scouncilBundle['objectives']}:</th>" escape="false"/>
		<h:outputText value="<td>" escape="false"/>
		<fc:extendedOutputText value="#{competenceCourse.objectives}" linebreak="true"/>
		<h:outputText value="<i>#{scouncilBundle['empty.field']}</i>" escape="false" rendered="#{empty competenceCourse.objectives}"/>
		<h:outputText value="</td></tr>" escape="false"/>
		<h:outputText value="<tr><th>#{scouncilBundle['program']}:</th>" escape="false"/>
		<h:outputText value="<td>" escape="false"/>
		<fc:extendedOutputText value="#{competenceCourse.program}" linebreak="true"/>
		<h:outputText value="<i>#{scouncilBundle['empty.field']}</i>" escape="false" rendered="#{empty competenceCourse.program}"/>
		<h:outputText value="</td></tr>" escape="false"/>
		<h:outputText value="<tr><th>#{scouncilBundle['evaluationMethod']}:</th>" escape="false"/>
		<h:outputText value="<td>" escape="false"/>
		<fc:extendedOutputText value="#{competenceCourse.evaluationMethod}" linebreak="true"/>
		<h:outputText value="<i>#{scouncilBundle['empty.field']}</i>" escape="false" rendered="#{empty competenceCourse.evaluationMethod}"/>
		<h:outputText value="</td></tr>" escape="false"/>
		<h:outputText value="</table>" escape="false"/>
		
		<h:outputText value="<p class='mbottom0'><em>#{scouncilBundle['english']}: </em></p>" escape="false"/>
		<h:outputText value="<table class='showinfo1 emphasis2'>" escape="false"/>
		<h:outputText value="<tr><th>#{scouncilBundle['objectivesEn']}:</th>" escape="false"/>
		<h:outputText value="<td>" escape="false"/>
		<fc:extendedOutputText value="#{competenceCourse.objectivesEn}" linebreak="true"/>
		<h:outputText value="<i>#{scouncilBundle['empty.field']}</i>" escape="false" rendered="#{empty competenceCourse.objectivesEn}"/>
		<h:outputText value="</td></tr>" escape="false"/>
		<h:outputText value="<tr><th>#{scouncilBundle['programEn']}:</th>" escape="false"/>
		<h:outputText value="<td>" escape="false"/>
		<fc:extendedOutputText value="#{competenceCourse.programEn}" linebreak="true"/>
		<h:outputText value="<i>#{scouncilBundle['empty.field']}</i>" escape="false" rendered="#{empty competenceCourse.programEn}"/>
		<h:outputText value="</td></tr>" escape="false"/>
		<h:outputText value="<tr><th>#{scouncilBundle['evaluationMethodEn']}:</th>" escape="false"/>
		<h:outputText value="<td>" escape="false"/>
		<fc:extendedOutputText value="#{competenceCourse.evaluationMethodEn}" linebreak="true"/>	
		<h:outputText value="<i>#{scouncilBundle['empty.field']}</i>" escape="false" rendered="#{empty competenceCourse.evaluationMethodEn}"/>
		<h:outputText value="</td></tr>" escape="false"/>
		<h:outputText value="</table>" escape="false"/>
		<h:outputText value="</div>" escape="false"/>

		<h:outputText value="<div class='simpleblock3 mtop2'>" escape="false"/>
		<h:outputText value="<h3 class='mbottom0'>#{scouncilBundle['bibliographicReference']}</h3>" escape="false"/>	
		<h:outputText value="<p><b>#{enumerationBundle['MAIN']}</b></p>" escape="false"/>

		<h:panelGroup rendered="#{empty competenceCourse.bibliographicReferences}">
			<h:outputText value="<i>#{scouncilBundle['noBibliographicReferences']}</i><br/>" escape="false"/>
		</h:panelGroup>	
		<fc:dataRepeater value="#{competenceCourse.bibliographicReferences.bibliographicReferencesList}" var="bibliographicReference" rendered="#{!empty competenceCourse.bibliographicReferences}">
			<h:panelGroup rendered="#{bibliographicReference.type.name == 'MAIN'}">
				<h:outputText value="<ul class='nobullet cboth mbottom2'>" escape="false"/>					
				<h:outputText value="<li><span class='fleft' style='width: 250px;'>#{scouncilBundle['title']}:</span>" escape="false"/>
				<h:outputText value="<a href='#{bibliographicReference.url}'>#{bibliographicReference.title}</a></li>" escape="false"/>
				
				<h:outputText value="<li><span class='fleft' style='width: 250px;'>#{scouncilBundle['author']}:</span>" escape="false"/>
				<h:outputText value="<em>#{bibliographicReference.authors}</em></li>" escape="false"/>
				
				<h:outputText value="<li><span class='fleft' style='width: 250px;'>#{scouncilBundle['year']}:</span>" escape="false"/>
				<h:outputText value="#{bibliographicReference.year}</li>" escape="false"/>
				
				<h:outputText value="<li><span class='fleft' style='width: 250px;'>#{scouncilBundle['reference']}:</span>" escape="false"/>
				<h:outputText value="#{bibliographicReference.reference}</li>" escape="false"/>
				
				<h:outputText value="</ul>" escape="false"/>
			</h:panelGroup>
		</fc:dataRepeater>
		
		<h:outputText value="<p><b>#{enumerationBundle['SECONDARY']}</b></p>" escape="false"/>
		<h:panelGroup rendered="#{empty competenceCourse.bibliographicReferences}">
			<h:outputText value="<i>#{scouncilBundle['noBibliographicReferences']}</i><br/>" escape="false"/>
		</h:panelGroup>	
		<fc:dataRepeater value="#{competenceCourse.bibliographicReferences.bibliographicReferencesList}" var="bibliographicReference" rendered="#{!empty competenceCourse.bibliographicReferences}">
			<h:panelGroup rendered="#{bibliographicReference.type.name == 'SECONDARY'}">
				<h:outputText value="<ul class='nobullet cboth mbottom2'>" escape="false"/>					
				<h:outputText value="<li><span class='fleft' style='width: 250px;'>#{scouncilBundle['title']}:</span>" escape="false"/>
				<h:outputText value="<a href='#{bibliographicReference.url}'>#{bibliographicReference.title}</a></li>" escape="false"/>
					
				<h:outputText value="<li><span class='fleft' style='width: 250px;'>#{scouncilBundle['author']}:</span>" escape="false"/>
				<h:outputText value="<em>#{bibliographicReference.authors}</em></li>" escape="false"/>
				
				<h:outputText value="<li><span class='fleft' style='width: 250px;'>#{scouncilBundle['year']}:</span>" escape="false"/>
				<h:outputText value="#{bibliographicReference.year}</li>" escape="false"/>
				
				<h:outputText value="<li><span class='fleft' style='width: 250px;'>#{scouncilBundle['reference']}:</span>" escape="false"/>
				<h:outputText value="#{bibliographicReference.reference}</li>" escape="false"/>
				
				<h:outputText value="</ul>" escape="false"/>
			</h:panelGroup>
		</fc:dataRepeater>

		<h:outputText value="</div>" escape="false"/>
		<h:outputText value="<div class='mvert2 break-before'></div><hr class='invisible mvert3'/>" escape="false"/>
	</fc:dataRepeater>

	<h:panelGroup rendered="#{empty CurricularCourseManagement.degreeCurricularPlanCompetenceCourses}" >
		<h:outputText value="<em>#{scouncilBundle['no.curricularPlan.competence.courses']}</em>" escape="false" />
	</h:panelGroup>

</ft:tilesView>
