<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>

<ft:tilesView locale="<%=request.getAttribute(org.apache.struts.Globals.LOCALE_KEY).toString()%>" definition="definition.public.mainPageIST" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/BolonhaManagerResources" var="bolonhaBundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="enumerationBundle"/>
	<f:loadBundle basename="resources/GlobalResources" var="globalBundle"/>
	<f:loadBundle basename="resources/PublicDegreeInformation" var="publicDegreeInfoBundle"/>

	<h:outputLink value="#{globalBundle['institution.url']}" >
		<h:outputText value="#{globalBundle['institution.name.abbreviation']}"/>
	</h:outputLink>
	&nbsp;&gt;&nbsp;
	<h:outputLink value="#{globalBundle['institution.url']}#{globalBundle['link.institution']}" >
		<h:outputText value="#{publicDegreeInfoBundle['public.degree.information.label.education']}"/>
	</h:outputLink>
	&nbsp;&gt;&nbsp;

	<h:outputText value="<a href='../showDegreeSite.do?method=showDescription&amp;degreeID=#{CurricularCourseManagement.degreeCurricularPlan.degree.idInternal}'>#{CurricularCourseManagement.degreeCurricularPlan.degree.sigla}</a>" escape="false"/>

	&nbsp;&gt;&nbsp;
    <h:outputText value="<a href='../showDegreeSite.do?method=showCurricularPlan&amp;degreeID=#{CurricularCourseManagement.degreeCurricularPlan.degree.idInternal}&amp;degreeCurricularPlanID=#{CurricularCourseManagement.degreeCurricularPlanID}'>#{CurricularCourseManagement.degreeCurricularPlan.name}</a>" escape="false"/>
	
	&nbsp;&gt;&nbsp;

     <h:outputText rendered="#{!empty CurricularCourseManagement.organizeBy}" value="<a href='showDegreeCurricularPlanBolonha.faces?degreeID=#{CurricularCourseManagement.degreeCurricularPlan.degree.idInternal}&amp;degreeCurricularPlanID=#{CurricularCourseManagement.degreeCurricularPlanID}&amp;executionPeriodID=#{CurricularCourseManagement.executionYearID}&amp;organizeBy=#{CurricularCourseManagement.organizeBy}&amp;showRules=#{CurricularCourseManagement.showRules}&amp;hideCourses=#{CurricularCourseManagement.hideCourses}'>#{publicDegreeInfoBundle['public.degree.information.label.curriculum']}</a>" escape="false"/>	

     <h:outputText rendered="#{empty CurricularCourseManagement.organizeBy}" value="<a href='showDegreeCurricularPlanBolonha.faces?degreeID=#{CurricularCourseManagement.degreeCurricularPlan.degree.idInternal}&amp;degreeCurricularPlanID=#{CurricularCourseManagement.degreeCurricularPlanID}&amp;executionPeriodID=#{CurricularCourseManagement.executionYearID}&amp;organizeBy=groups&amp;showRules=false&amp;hideCourses=false'>#{publicDegreeInfoBundle['public.degree.information.label.curriculum']}</a>" escape="false"/>	 	

	&nbsp;&gt;&nbsp;
	<h:outputText rendered="#{!CurricularCourseManagement.renderInEnglish}" value="#{CurricularCourseManagement.curricularCourse.name}"/>
	<h:outputText rendered="#{CurricularCourseManagement.renderInEnglish}" value="#{CurricularCourseManagement.curricularCourse.nameEn}"/>

	<h:outputText value="<br/><br/>" escape="false"/>

 	<h:outputFormat value="<h1>#{enumerationBundle[CurricularCourseManagement.degreeCurricularPlan.degree.degreeType.name]}" escape="false"/>
	<h:outputFormat value="#{publicDegreeInfoBundle['public.degree.information.label.in']}"/>
 	<h:outputFormat value="#{CurricularCourseManagement.degreeLocaleSensitiveName}"/>
	<h:outputFormat value="</h1>" escape="false"/>

	<h:outputFormat value="<h2 class='greytxt'>#{publicDegreeInfoBundle['public.degree.information.label.curricularPlan']} #{CurricularCourseManagement.degreeCurricularPlan.name}</h2>" escape="false"/>
	<h:outputText value="<br/>" escape="false"/>
	<h:outputFormat value="<h2 class='greytxt'>" escape="false"/>
	<h:outputText rendered="#{!CurricularCourseManagement.renderInEnglish}" value="#{CurricularCourseManagement.curricularCourse.name}"/>
	<h:outputText rendered="#{CurricularCourseManagement.renderInEnglish}" value="#{CurricularCourseManagement.curricularCourse.nameEn}"/>
	<h:outputText rendered="#{!empty CurricularCourseManagement.curricularCourse.acronym}" value=" (#{CurricularCourseManagement.curricularCourse.acronym})"/>
	<h:outputFormat value="</h2>" escape="false"/>

	<!-- LATERAL NAVIGATION -->
	<!-- COMPETENCE COURSE -->
	<h:outputText value="<div class='col_right'><table class='box' cellspacing='0'>" escape="false"
		rendered="#{CurricularCourseManagement.selectedCurricularCourseType == 'NORMAL_COURSE'} || #{!empty CurricularCourseManagement.curricularCourse.associatedExecutionCourses}"/>
	<h:panelGroup rendered="#{CurricularCourseManagement.selectedCurricularCourseType == 'NORMAL_COURSE'}">
		<h:outputText value="<tr><td class='box_header'>" escape="false"/>
		<h:outputText value="<strong>#{bolonhaBundle['competenceCourse']}</strong></td></tr>" escape="false"/>
		<h:outputText value="<tr><td class='box_cell'>" escape="false"/>
			
			<h:outputText rendered="#{!CurricularCourseManagement.renderInEnglish}" value="<a href='../department/showCompetenceCourse.faces?competenceCourseID=#{CurricularCourseManagement.curricularCourse.competenceCourse.idInternal}&amp;selectedDepartmentUnitID=#{CurricularCourseManagement.curricularCourse.competenceCourse.departmentUnit.idInternal}'>#{CurricularCourseManagement.curricularCourse.name}</a>" escape="false"/> 

			<h:outputText rendered="#{CurricularCourseManagement.renderInEnglish}" value="<a href='../department/showCompetenceCourse.faces?competenceCourseID=#{CurricularCourseManagement.curricularCourse.competenceCourse.idInternal}&amp;selectedDepartmentUnitID=#{CurricularCourseManagement.curricularCourse.competenceCourse.departmentUnit.idInternal}'>#{CurricularCourseManagement.curricularCourse.nameEn}</a>" escape="false"/> 			
						
			<h:outputText value=" #{publicDegreeInfoBundle['public.degree.information.label.from.masculine']} "/>
			<h:outputLink value="../department/showDepartmentCompetenceCourses.faces">
				<h:outputText value="#{CurricularCourseManagement.curricularCourse.competenceCourse.departmentUnit.name}"/>
				<f:param name="selectedDepartmentUnitID" value="#{CurricularCourseManagement.curricularCourse.competenceCourse.departmentUnit.idInternal}"/>
			</h:outputLink>
		<h:outputText value="</td></tr>" escape="false"/>
	</h:panelGroup>
	<!-- EXECUTION COURSES -->
	<h:panelGroup rendered="#{!empty CurricularCourseManagement.curricularCourse.executionCoursesWithPublicSites}">
		<h:outputText value="<tr><td class='box_header'>" escape="false"/>
		<h:outputText value="<strong>#{publicDegreeInfoBundle['public.degree.information.label.courses']}</strong></td></tr>" escape="false"/>
		<fc:dataRepeater value="#{CurricularCourseManagement.curricularCourse.associatedExecutionCourses}" var="executionCourse">
			<h:outputText value="<tr><td class='box_cell'>" escape="false"/>

			<h:outputText value="<a href='../executionCourse.do?method=firstPage&amp;executionCourseID=#{executionCourse.idInternal}'>#{executionCourse.nome} (#{executionCourse.executionPeriod.executionYear.year} - #{executionCourse.executionPeriod.name})</a>" escape="false"/>
			<h:outputText value="</td></tr>" escape="false"/>
		</fc:dataRepeater>
	</h:panelGroup>
	<h:outputText value="</table></div>" escape="false"
		rendered="#{CurricularCourseManagement.selectedCurricularCourseType == 'NORMAL_COURSE'} || #{!empty CurricularCourseManagement.curricularCourse.associatedExecutionCourses}"/>

	<!-- CONTEXT -->
	<h:outputText value="<h2 class='arrow_bullet'>#{publicDegreeInfoBundle['public.degree.information.label.scope']}</h2>" escape="false"/>
	<fc:dataRepeater value="#{CurricularCourseManagement.curricularCourse.parentContexts}" var="context">
		<h:outputText value="<div class=''>" escape="false"/>
		<h:outputText value="<p>#{publicDegreeInfoBundle['public.group']}: " escape="false"/>
		<h:outputText value="#{context.parentCourseGroup.oneFullName}</p>" escape="false"/>
		<h:outputText value="<p class='mvert05'>#{publicDegreeInfoBundle['public.curricular.period']}: " escape="false"/>
		<h:outputText value="#{context.curricularPeriod.fullLabel}</p>" escape="false"/>
		<h:outputText value="</div>" escape="false"/>
	</fc:dataRepeater>

	<!-- WEIGHT -->
	<h:panelGroup rendered="#{!empty CurricularCourseManagement.curricularCourse.weigth && CurricularCourseManagement.curricularCourse.weigth != 0.0}">
		<h:outputText value="<h2 class='arrow_bullet'>#{bolonhaBundle['weight']}</h2>" escape="false"/>
		<h:outputText value="<p>" escape="false"/>
		<h:outputText value="#{CurricularCourseManagement.curricularCourse.weigth} (#{bolonhaBundle['for.average.grade.calculus']})"/>
		<h:outputText value="</p>" escape="false"/>
	</h:panelGroup>
	
	<!-- PRE-REQUISITES -->
	<h:panelGroup rendered="#{!empty CurricularCourseManagement.curricularCourse.prerequisites}">
		<h:outputText value="<h2 class='arrow_bullet'>#{bolonhaBundle['prerequisites']}</h2>" escape="false"/>
		<h:outputText value="<p>" escape="false"/>
		<fc:extendedOutputText rendered="#{!CurricularCourseManagement.renderInEnglish}" value="#{CurricularCourseManagement.curricularCourse.prerequisites}" linebreak="true"/>
		<fc:extendedOutputText rendered="#{CurricularCourseManagement.renderInEnglish}" value="#{CurricularCourseManagement.curricularCourse.prerequisitesEn}" linebreak="true"/>		
		<h:outputText value="</p>" escape="false"/>
	</h:panelGroup>
	
	<!-- OBJECTIVES -->
	<h:panelGroup rendered="#{!empty CurricularCourseManagement.curricularCourse.objectives}">
		<h:outputText value="<h2 class='arrow_bullet'>#{bolonhaBundle['objectives']}</h2>" escape="false"/>
		<h:outputText value="<p>" escape="false"/>
		<fc:extendedOutputText rendered="#{!CurricularCourseManagement.renderInEnglish}" value="#{CurricularCourseManagement.curricularCourse.objectives}" linebreak="true"/>
		<fc:extendedOutputText rendered="#{CurricularCourseManagement.renderInEnglish}" value="#{CurricularCourseManagement.curricularCourse.objectivesEn}" linebreak="true"/>		
		<h:outputText value="</p>" escape="false"/>
	</h:panelGroup>
	
	<!-- PROGRAM -->
	<h:panelGroup rendered="#{!empty CurricularCourseManagement.curricularCourse.program}">
		<h:outputText value="<h2 class='arrow_bullet'>#{bolonhaBundle['program']}</h2>" escape="false"/>
		<h:outputText value="<p>" escape="false"/>
		<fc:extendedOutputText rendered="#{!CurricularCourseManagement.renderInEnglish}" value="#{CurricularCourseManagement.curricularCourse.program}" linebreak="true"/>
		<fc:extendedOutputText rendered="#{CurricularCourseManagement.renderInEnglish}" value="#{CurricularCourseManagement.curricularCourse.programEn}" linebreak="true"/>		
		<h:outputText value="</p>" escape="false"/>
	</h:panelGroup>
	
	<!-- EVALUATION METHOD -->
	<h:panelGroup rendered="#{!empty CurricularCourseManagement.curricularCourse.evaluationMethod}">
		<h:outputText value="<h2 class='arrow_bullet'>#{bolonhaBundle['evaluationMethod']}</h2>" escape="false"/>
		<h:outputText value="<p>" escape="false"/>
		<fc:extendedOutputText rendered="#{!CurricularCourseManagement.renderInEnglish}" value="#{CurricularCourseManagement.curricularCourse.evaluationMethod}" linebreak="true"/>
		<fc:extendedOutputText rendered="#{CurricularCourseManagement.renderInEnglish}" value="#{CurricularCourseManagement.curricularCourse.evaluationMethodEn}" linebreak="true"/>
		<h:outputText value="</p>" escape="false"/>
	</h:panelGroup>
	
	<h:form>
		<h:outputText escape="false" value="<input alt='input.degreeID' id='degreeID' name='degreeID' type='hidden' value='#{CurricularCourseManagement.curricularCourse.parentDegreeCurricularPlan.idInternal}'/>"/>
		<h:outputText escape="false" value="<input alt='input.degreeCurricularPlanID' id='degreeCurricularPlanID' name='degreeCurricularPlanID' type='hidden' value='#{CurricularCourseManagement.curricularCourse.parentDegreeCurricularPlan.idInternal}'/>"/>
 		<h:outputText escape="false" value="<input alt='input.executionPeriodOID' id='executionPeriodOID' name='executionPeriodOID' type='hidden' value='#{CurricularCourseManagement.executionPeriodOID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.organizeBy' id='organizeBy' name='organizeBy' type='hidden' value='#{CurricularCourseManagement.organizeBy}'/>"/>
		<h:outputText escape="false" value="<input alt='input.showRules' id='showRules' name='showRules' type='hidden' value='#{CurricularCourseManagement.showRules}'/>"/>
		<h:outputText escape="false" value="<input alt='input.hideCourses' id='hideCourses' name='hideCourses' type='hidden' value='#{CurricularCourseManagement.hideCourses}'/>"/>
		<h:outputText escape="false" value="<input alt='input.action' id='action' name='action' type='hidden' value='#{CurricularCourseManagement.action}'/>"/>
	</h:form>

</ft:tilesView>
