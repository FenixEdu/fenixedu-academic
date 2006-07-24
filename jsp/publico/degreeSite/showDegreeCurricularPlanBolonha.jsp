<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>

<ft:tilesView locale="<%=request.getAttribute(org.apache.struts.Globals.LOCALE_KEY).toString()%>" definition="definition.public.mainPageIST" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>
	<style>@import "<%= request.getContextPath() %>/CSS/transitional.css";</style>
	<f:loadBundle basename="resources/ApplicationResources" var="applicationBundle"/>
	<f:loadBundle basename="resources/GlobalResources" var="globalBundle"/>
	<f:loadBundle basename="resources/PublicDegreeInformation" var="publicDegreeInfoBundle"/>
	<f:loadBundle basename="resources/BolonhaManagerResources" var="bolonhaBundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="enumerationBundle"/>
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>
	
	<h:outputLink value="#{globalBundle['institution.url']}" >
		<h:outputText value="#{globalBundle['institution.name.abbreviation']}"/>
	</h:outputLink>
	&nbsp;&gt;&nbsp;
	<h:outputLink value="#{globalBundle['institution.url']}#{globalBundle['link.institution']}" >
		<h:outputText value="#{publicDegreeInfoBundle['public.degree.information.label.education']}"/>
	</h:outputLink>
	&nbsp;&gt;&nbsp;
	<h:outputLink value="../showDegreeSite.do" >
		<h:outputText value="#{CurricularCourseManagement.degreeCurricularPlan.degree.sigla}"/>
		<f:param name="method" value="showDescription"/>
		<f:param name="degreeID" value="#{CurricularCourseManagement.degreeCurricularPlan.degree.idInternal}"/>
	</h:outputLink>
	&nbsp;&gt;&nbsp;
	<h:outputLink value="../showDegreeSite.do" >
		<h:outputText value="#{CurricularCourseManagement.degreeCurricularPlan.name}"/>
		<f:param name="method" value="showCurricularPlan"/>
		<f:param name="degreeID" value="#{CurricularCourseManagement.degreeCurricularPlan.degree.idInternal}"/>
		<f:param name="degreeCurricularPlanID" value="#{CurricularCourseManagement.degreeCurricularPlanID}"/>
	</h:outputLink>
	&nbsp;&gt;&nbsp;
	<h:outputText value="#{publicDegreeInfoBundle['public.degree.information.label.curriculum']}"/>

	<h:outputText value="<br/><br/>" escape="false"/>
	
	<h:outputFormat value="<p><h1>#{enumerationBundle[CurricularCourseManagement.degreeCurricularPlan.degree.bolonhaDegreeType.name]}" escape="false"/>
	<h:outputFormat value="#{publicDegreeInfoBundle['public.degree.information.label.in']}"/>
	<h:outputFormat value="#{CurricularCourseManagement.degreeLocaleSensitiveName}"/>
	<h:outputFormat value="</h1></p>" escape="false"/>

	<h:outputFormat value="<h2 class='greytxt'>#{publicDegreeInfoBundle['public.degree.information.label.curricularPlan']}" escape="false"/>
	<h:outputFormat value="#{CurricularCourseManagement.degreeCurricularPlan.name}"/>
	<h:outputFormat value="</h2>" escape="false"/>

	<h:outputFormat value="<h2 class='arrow_bullet'>#{publicDegreeInfoBundle['public.degree.information.label.curriculum']}</h2>" escape="false"/>

	<h:panelGroup rendered="#{!CurricularCourseManagement.degreeCurricularPlan.approved}">
		<h:outputText value="<p><em>#{applicationBundle['error.curricularPlanNotApproved']}</em><p>" escape="false"/>
	</h:panelGroup>

	<h:form>

		<h:panelGroup rendered="#{CurricularCourseManagement.degreeCurricularPlan.approved}">

			<h:panelGroup rendered="#{empty CurricularCourseManagement.degreeCurricularPlan.executionDegrees}">
				<h:outputText value="<p><em>#{applicationBundle['error.curricularPlanHasNoExecutionDegrees']}</em><p>" escape="false"/>
			</h:panelGroup>
		
			<h:panelGroup rendered="#{!empty CurricularCourseManagement.degreeCurricularPlan.executionDegrees}">
				<h:outputText value="<div style='margin-left: 25px;'>" escape="false"/>
					<h:outputText value="<p>#{publicDegreeInfoBundle['public.degree.information.label.executionYear']}: " escape="false"/>
					<h:selectOneMenu value="#{CurricularCourseManagement.executionYearID}" onchange="this.form.submit();">
						<f:selectItems value="#{CurricularCourseManagement.executionYearItems}" />
					</h:selectOneMenu>
					<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'>" escape="false"/>
					<h:outputText value="</p>" escape="false"/>
					<h:panelGroup rendered="#{!empty CurricularCourseManagement.degreeCurricularPlan.degreeStructure.childs}">
						<h:outputText value="<p>#{bolonhaBundle['view.structure.organized.by']}: " escape="false"/>
						<h:outputLink value="showDegreeCurricularPlanBolonha.faces" rendered="#{CurricularCourseManagement.organizeBy == 'years'}">
							<h:outputText value="#{bolonhaBundle['groups']}" />
							<f:param name="degreeID" value="#{CurricularCourseManagement.degreeCurricularPlan.degree.idInternal}"/>
							<f:param name="degreeCurricularPlanID" value="#{CurricularCourseManagement.degreeCurricularPlanID}"/>
							<f:param name="organizeBy" value="groups"/>
							<f:param name="showRules" value="#{CurricularCourseManagement.showRules}"/>
							<f:param name="hideCourses" value="#{CurricularCourseManagement.hideCourses}"/>
							<f:param name="action" value="#{CurricularCourseManagement.action}"/>
						</h:outputLink>
						<h:outputText value="<span class='highlight3'>#{bolonhaBundle['groups']}</span>" rendered="#{CurricularCourseManagement.organizeBy == 'groups'}" escape="false"/>
						<h:outputText value=" , " escape="false"/>
						<h:outputLink value="showDegreeCurricularPlanBolonha.faces" rendered="#{CurricularCourseManagement.organizeBy == 'groups'}">
							<h:outputText value="#{bolonhaBundle['year']}/#{bolonhaBundle['semester']}" />
							<f:param name="degreeID" value="#{CurricularCourseManagement.degreeCurricularPlan.degree.idInternal}"/>
							<f:param name="degreeCurricularPlanID" value="#{CurricularCourseManagement.degreeCurricularPlanID}"/>
							<f:param name="organizeBy" value="years"/>
							<f:param name="showRules" value="#{CurricularCourseManagement.showRules}"/>
							<f:param name="hideCourses" value="#{CurricularCourseManagement.hideCourses}"/>
							<f:param name="action" value="#{CurricularCourseManagement.action}"/>
						</h:outputLink>
						<h:outputText value="<span class='highlight3'>#{bolonhaBundle['year']}/#{bolonhaBundle['semester']}</span>" rendered="#{CurricularCourseManagement.organizeBy == 'years'}" escape="false"/>
						<h:outputText value="</p>" escape="false"/>
					</h:panelGroup>
			
					<h:panelGroup rendered="#{!empty CurricularCourseManagement.degreeCurricularPlan.root.childContexts}">	
						<h:outputText value="<p>" escape="false"/>
						<h:outputText value="#{bolonhaBundle['curricularRules']}: " escape="false"/>
						<h:outputLink value="showDegreeCurricularPlanBolonha.faces" rendered="#{CurricularCourseManagement.showRules == 'false'}">
							<h:outputText value="#{bolonhaBundle['show']}" />
							<f:param name="degreeID" value="#{CurricularCourseManagement.degreeCurricularPlan.degree.idInternal}"/>
							<f:param name="degreeCurricularPlanID" value="#{CurricularCourseManagement.degreeCurricularPlanID}"/>
							<f:param name="organizeBy" value="#{CurricularCourseManagement.organizeBy}"/>
							<f:param name="showRules" value="true"/>
							<f:param name="hideCourses" value="#{CurricularCourseManagement.hideCourses}"/>
							<f:param name="action" value="#{CurricularCourseManagement.action}"/>		
						</h:outputLink>
						<h:outputText value="<span class='highlight3'>#{bolonhaBundle['show']}</span>" rendered="#{CurricularCourseManagement.showRules == 'true'}" escape="false"/>
						<h:outputText value=" , " escape="false"/>
						<h:outputLink value="showDegreeCurricularPlanBolonha.faces" rendered="#{CurricularCourseManagement.showRules == 'true'}">
							<h:outputText value="#{bolonhaBundle['hide']}" />
							<f:param name="degreeID" value="#{CurricularCourseManagement.degreeCurricularPlan.degree.idInternal}"/>
							<f:param name="degreeCurricularPlanID" value="#{CurricularCourseManagement.degreeCurricularPlanID}"/>
							<f:param name="organizeBy" value="#{CurricularCourseManagement.organizeBy}"/>
							<f:param name="showRules" value="false"/>
							<f:param name="hideCourses" value="#{CurricularCourseManagement.hideCourses}"/>
							<f:param name="action" value="#{CurricularCourseManagement.action}"/>
						</h:outputLink>
						<h:outputText value="<span class='highlight3'>#{bolonhaBundle['hide']}</span>" rendered="#{CurricularCourseManagement.showRules == 'false'}" escape="false"/>
						<h:outputText value="</p>" escape="false"/>
					</h:panelGroup>
				
					<h:panelGroup rendered="#{!empty CurricularCourseManagement.degreeCurricularPlan.root.childContexts && CurricularCourseManagement.organizeBy == 'groups'}">
						<h:outputText value="<p>" escape="false"/>
						<h:outputText value="#{bolonhaBundle['curricularCourses']}: " escape="false"/>
						<h:outputLink value="showDegreeCurricularPlanBolonha.faces" rendered="#{CurricularCourseManagement.hideCourses == 'true'}">
							<h:outputText value="#{bolonhaBundle['show']}" />
							<f:param name="degreeID" value="#{CurricularCourseManagement.degreeCurricularPlan.degree.idInternal}"/>
							<f:param name="degreeCurricularPlanID" value="#{CurricularCourseManagement.degreeCurricularPlanID}"/>
							<f:param name="organizeBy" value="#{CurricularCourseManagement.organizeBy}"/>
							<f:param name="showRules" value="#{CurricularCourseManagement.showRules}"/>
							<f:param name="hideCourses" value="false"/>
							<f:param name="action" value="#{CurricularCourseManagement.action}"/>
						</h:outputLink>
						<h:outputText value="<span class='highlight3'>#{bolonhaBundle['show']}</span>" rendered="#{CurricularCourseManagement.hideCourses == 'false'}" escape="false"/>
						<h:outputText value=" , " escape="false"/>
						<h:outputLink value="showDegreeCurricularPlanBolonha.faces" rendered="#{CurricularCourseManagement.hideCourses == 'false'}">
							<h:outputText value="#{bolonhaBundle['hide']}" />
							<f:param name="degreeID" value="#{CurricularCourseManagement.degreeCurricularPlan.degree.idInternal}"/>
							<f:param name="degreeCurricularPlanID" value="#{CurricularCourseManagement.degreeCurricularPlanID}"/>
							<f:param name="organizeBy" value="#{CurricularCourseManagement.organizeBy}"/>
							<f:param name="showRules" value="#{CurricularCourseManagement.showRules}"/>
							<f:param name="hideCourses" value="true"/>
							<f:param name="action" value="#{CurricularCourseManagement.action}"/>
						</h:outputLink>
						<h:outputText value="<span class='highlight3'>#{bolonhaBundle['hide']}</span>" rendered="#{CurricularCourseManagement.hideCourses == 'true'}" escape="false"/>
						<h:outputText value="</p>" escape="false"/>
					</h:panelGroup>
			
				<h:outputText value="</div>" escape="false"/>
				
				<h:outputText value="<br/>" escape="false"/>
				<fc:degreeCurricularPlanRender 
					dcp="#{CurricularCourseManagement.degreeCurricularPlan}" 
					organizeBy="<%=request.getParameter("organizeBy")%>"
					showRules="<%=request.getParameter("showRules")%>"
					hideCourses="<%=request.getParameter("hideCourses")%>"
					executionYear="#{CurricularCourseManagement.executionYear}"/>
			</h:panelGroup>
	
		</h:panelGroup>

		<h:outputText escape="false" value="<input alt='input.degreeID' id='degreeID' name='degreeID' type='hidden' value='#{DegreeManagementBackingBean.degreeId}'/>"/>
		<h:outputText escape="false" value="<input alt='input.degreeCurricularPlanID' id='degreeCurricularPlanID' name='degreeCurricularPlanID' type='hidden' value='#{CurricularCourseManagement.degreeCurricularPlanID}'/>"/>
 		<h:outputText escape="false" value="<input alt='input.executionYearID' id='executionYearID' name='executionYearID' type='hidden' value='#{CurricularCourseManagement.executionYearID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.organizeBy' id='organizeBy' name='organizeBy' type='hidden' value='#{CurricularCourseManagement.organizeBy}'/>"/>
		<h:outputText escape="false" value="<input alt='input.showRules' id='showRules' name='showRules' type='hidden' value='#{CurricularCourseManagement.showRules}'/>"/>
		<h:outputText escape="false" value="<input alt='input.hideCourses' id='hideCourses' name='hideCourses' type='hidden' value='#{CurricularCourseManagement.hideCourses}'/>"/>
		<h:outputText escape="false" value="<input alt='input.action' id='action' name='action' type='hidden' value='#{CurricularCourseManagement.action}'/>"/>
	</h:form>

</ft:tilesView>
