<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-tiles" prefix="ft"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-fenix" prefix="fc"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>

<ft:tilesView definition="scientificCouncil.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/ScientificCouncilResources" var="scouncilBundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="enumerationBundle"/>

	<fc:dataRepeater rendered="#{!empty CompetenceCourseManagement.departmentCompetenceCourses}" value="#{CompetenceCourseManagement.departmentCompetenceCourses}" var="competenceCourse">

		<h:outputText value="<em>#{scouncilBundle['competenceCourse']}</em>" escape="false" />
		<h:outputText value="<h2>#{competenceCourse.name}</h2>" escape="false"/>

		<h:outputText value="<ul class='nobullet padding1 indent0 mtop15'>" escape="false"/>
		<h:outputText value="<li><strong>#{scouncilBundle['department']}: </strong>" escape="false"/>
		<h:outputText value="#{competenceCourse.departmentUnit.department.realName}</li>" escape="false"/>
		<fc:dataRepeater value="#{competenceCourse.competenceCourseGroupUnit.parentUnits}" var="scientificAreaUnit">
			<h:outputText value="<li><strong>#{scouncilBundle['area']}: </strong>" escape="false"/>
			<h:outputText value="#{scientificAreaUnit.name} > #{competenceCourse.competenceCourseGroupUnit.name}</li>" escape="false"/>
		</fc:dataRepeater>		
		<h:outputText value="</ul>" escape="false"/>

		<h:outputText value="<p class='mtop15 mbottom0'><strong>#{scouncilBundle['activeCurricularPlans']}: </strong></p>" escape="false"/>
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
					<f:param name="dcpId" value="#{curricularCourse.parentDegreeCurricularPlan.externalId}"/>
				</h:outputLink>
				<h:outputText value=" > "/>
				<h:outputLink value="../curricularPlans/viewCurricularCourse.faces" target="_blank">
					<h:outputText value="#{curricularCourse.name}" escape="false"/>
					<f:param name="action" value="close"/>
					<f:param name="curricularCourseID" value="#{curricularCourse.externalId}"/>
				</h:outputLink>
				<h:outputText value="</li>" escape="false"/>
			</fc:dataRepeater>
			<h:outputText value="</ul>" escape="false"/>
		</h:panelGroup>	
	
		<h:outputText value="<div class='simpleblock3 mtop2'>" escape="false"/>
		<h:outputText value="<p>#{scouncilBundle['state']}: " escape="false"/>
		<h:outputText value="<span class='highlight1'>#{enumerationBundle[competenceCourse.curricularStage.name]}</span></p>" escape="false"/>
		<h:outputText value="<ul class='nobullet padding1 indent0 mbottom0'>" escape="false"/>	
		<h:outputText value="<li>#{scouncilBundle['name']} (pt): </strong>" escape="false"/>
		<h:outputText value="<strong>#{competenceCourse.name}</strong></li>" escape="false"/>
		<h:outputText value="<li>#{scouncilBundle['nameEn']} (en): </strong>" escape="false"/>
		<h:outputText value="<strong>#{competenceCourse.nameEn}</strong></li>" escape="false" />
		<h:panelGroup rendered="#{!empty competenceCourse.acronym}"> 
			<h:outputText value="<li>#{scouncilBundle['acronym']}: </strong>" escape="false"/>
			<h:outputText value="#{competenceCourse.acronym}</li>" escape="false"/>
		</h:panelGroup>
		<h:outputText value="<li>#{scouncilBundle['competenceCourseLevel']}: " escape="false"/>
		<h:outputText value="#{enumerationBundle[competenceCourse.competenceCourseLevel]}</li>" escape="false" rendered="#{!empty competenceCourse.competenceCourseLevel}"/>	
		<h:outputText value="<em>#{scouncilBundle['label.notDefined']}</em></li>" escape="false" rendered="#{empty competenceCourse.competenceCourseLevel}"/>
		<h:outputText value="<li>#{scouncilBundle['type']}: " escape="false"/>
		<h:outputText value="#{scouncilBundle['basic']}</li>" rendered="#{competenceCourse.basic}" escape="false"/>
		<h:outputText value="#{scouncilBundle['nonBasic']}</li>" rendered="#{!competenceCourse.basic}" escape="false"/>
		<h:outputText value="</ul></div>" escape="false"/>
		
		<h:outputText value="<div class='simpleblock3 mtop2'>" escape="false"/>
		<h:outputText value="<ul class='nobullet padding1 indent0 mbottom0'>" escape="false"/>
		<h:outputText value="<li>#{scouncilBundle['regime']}: " escape="false"/>
		<h:outputText value="#{enumerationBundle[competenceCourse.regime.name]}</li>" escape="false"/>	
		<h:outputText value="<li>#{scouncilBundle['lessonHours']}: " escape="false"/>	

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
	
			<h:outputText value="<li><strong>#{scouncilBundle['ectsCredits']}: " escape="false"/>
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
		<fc:extendedOutputText value="#{competenceCourse.objectives}" linebreak="true"/>
		<h:outputText value="<i>#{scouncilBundle['empty.field']}</i>" escape="false" rendered="#{empty competenceCourse.objectives}"/>
		<h:outputText value="</td></tr>" escape="false"/>
		<h:outputText value="<tr><th class='aleft'>#{scouncilBundle['program']}:</th>" escape="false"/>
		<h:outputText value="<td>" escape="false"/>
		<fc:extendedOutputText value="#{competenceCourse.program}" linebreak="true"/>
		<h:outputText value="<i>#{scouncilBundle['empty.field']}</i>" escape="false" rendered="#{empty competenceCourse.program}"/>
		<h:outputText value="</td></tr>" escape="false"/>
		<h:outputText value="<tr><th class='aleft'>#{scouncilBundle['evaluationMethod']}:</th>" escape="false"/>
		<h:outputText value="<td>" escape="false"/>
		<fc:extendedOutputText value="#{competenceCourse.evaluationMethod}" linebreak="true"/>
		<h:outputText value="<i>#{scouncilBundle['empty.field']}</i>" escape="false" rendered="#{empty competenceCourse.evaluationMethod}"/>
		<h:outputText value="</td></tr>" escape="false"/>
		<h:outputText value="</table>" escape="false"/>
		
		<h:outputText value="<p class='mbottom0'><em>#{scouncilBundle['english']}: </em></p>" escape="false"/>
		<h:outputText value="<table class='showinfo1 emphasis2'>" escape="false"/>
		<h:outputText value="<tr><th class='aleft'>#{scouncilBundle['objectivesEn']}:</th>" escape="false"/>
		<h:outputText value="<td>" escape="false"/>
		<fc:extendedOutputText value="#{competenceCourse.objectivesEn}" linebreak="true"/>
		<h:outputText value="<i>#{scouncilBundle['empty.field']}</i>" escape="false" rendered="#{empty competenceCourse.objectivesEn}"/>
		<h:outputText value="</td></tr>" escape="false"/>
		<h:outputText value="<tr><th class='aleft'>#{scouncilBundle['programEn']}:</th>" escape="false"/>
		<h:outputText value="<td>" escape="false"/>
		<fc:extendedOutputText value="#{competenceCourse.programEn}" linebreak="true"/>
		<h:outputText value="<i>#{scouncilBundle['empty.field']}</i>" escape="false" rendered="#{empty competenceCourse.programEn}"/>
		<h:outputText value="</td></tr>" escape="false"/>
		<h:outputText value="<tr><th class='aleft'>#{scouncilBundle['evaluationMethodEn']}:</th>" escape="false"/>
		<h:outputText value="<td>" escape="false"/>
		<fc:extendedOutputText value="#{competenceCourse.evaluationMethodEn}" linebreak="true"/>	
		<h:outputText value="<i>#{scouncilBundle['empty.field']}</i>" escape="false" rendered="#{empty competenceCourse.evaluationMethodEn}"/>
		<h:outputText value="</td></tr>" escape="false"/>
		<h:outputText value="</table>" escape="false"/>
		<h:outputText value="</div>" escape="false"/>

		<h:outputText value="<div class='simpleblock3 mtop2'>" escape="false"/>

		<h:outputText value="<p><b>#{scouncilBundle['bibliographicReference']} #{enumerationBundle['MAIN']}</b></p>" escape="false"/>

		<h:panelGroup rendered="#{empty competenceCourse.bibliographicReferences || empty competenceCourse.bibliographicReferences.bibliographicReferencesList}">
			<h:outputText value="<i>#{scouncilBundle['noBibliographicReferences']}</i><br/>" escape="false"/>
		</h:panelGroup>

		<fc:dataRepeater value="#{competenceCourse.bibliographicReferences.bibliographicReferencesList}" var="bibliographicReference" rendered="#{!empty competenceCourse.bibliographicReferences}">
			<h:panelGroup rendered="#{bibliographicReference.type.name == 'MAIN'}">
				<h:outputText value="<ul class='nobullet cboth mbottom2'>" escape="false"/>					
				<h:outputText value="<li><span class='fleft width100px' style='padding-right: 10px;'>#{scouncilBundle['title']}:</span>" escape="false"/>
				<h:outputText value="<a href='#{bibliographicReference.url}'>#{bibliographicReference.title}</a></li>" escape="false"/>
				
				<h:outputText value="<li><span class='fleft width100px' style='padding-right: 10px;'>#{scouncilBundle['author']}:</span>" escape="false"/>
				<h:outputText value="<em>#{bibliographicReference.authors}</em></li>" escape="false"/>
				
				<h:outputText value="<li><span class='fleft width100px' style='padding-right: 10px;'>#{scouncilBundle['year']}:</span>" escape="false"/>
				<h:outputText value="#{bibliographicReference.year}</li>" escape="false"/>
				
				<h:outputText value="<li><span class='fleft width100px' style='padding-right: 10px;'>#{scouncilBundle['reference']}:</span>" escape="false"/>
				<h:outputText value="#{bibliographicReference.reference}</li>" escape="false"/>
				
				<h:outputText value="</ul>" escape="false"/>
			</h:panelGroup>
		</fc:dataRepeater>
		
		<h:outputText value="<p class='mtop2'><b>#{scouncilBundle['bibliographicReference']} #{enumerationBundle['SECONDARY']}</b></p>" escape="false"/>
		
		<h:panelGroup rendered="#{empty competenceCourse.bibliographicReferences || empty competenceCourse.bibliographicReferences.bibliographicReferencesList}">
			<h:outputText value="<i>#{scouncilBundle['noBibliographicReferences']}</i><br/>" escape="false"/>
		</h:panelGroup>
		
		<fc:dataRepeater value="#{competenceCourse.bibliographicReferences.bibliographicReferencesList}" var="bibliographicReference" rendered="#{!empty competenceCourse.bibliographicReferences}">
			<h:panelGroup rendered="#{bibliographicReference.type.name == 'SECONDARY'}">
				<h:outputText value="<ul class='nobullet cboth mbottom2'>" escape="false"/>					
				<h:outputText value="<li><span class='fleft width100px' style='padding-right: 10px;'>#{scouncilBundle['title']}:</span>" escape="false"/>
				<h:outputText value="<a href='#{bibliographicReference.url}'>#{bibliographicReference.title}</a></li>" escape="false"/>
					
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
		<h:outputText value="<div class='mvert2 break-before'></div><hr class='invisible mvert3'/>" escape="false"/>
	</fc:dataRepeater>

	<h:panelGroup rendered="#{empty CompetenceCourseManagement.departmentCompetenceCourses}" >
		<h:outputText value="<h2>#{CompetenceCourseManagement.selectedDepartmentUnit.department.realName}</h2>" escape="false"/>
		<h:outputText value="<em>#{scouncilBundle['no.competence.courses']}</em>" escape="false" />
	</h:panelGroup>

</ft:tilesView>
