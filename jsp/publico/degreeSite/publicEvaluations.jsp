<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>


<ft:tilesView locale="<%=request.getAttribute(org.apache.struts.Globals.LOCALE_KEY).toString()%>" definition="definition.public.mainPageIST" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/PublicDegreeInformation" var="bundle"/>
	<f:loadBundle basename="resources/ApplicationResources" var="bundleApplication"/>
	<f:loadBundle basename="resources/EnumerationResources" var="bundleEnum"/>

	<h:form>
		<h:outputText escape="false" value="<input alt='input.degreeID' id='degreeID' name='degreeID' type='hidden' value='#{publicEvaluations.degreeID}'/>"/>

		<h:outputText value="<div class='breadcumbs mvert0'>" escape="false"/>
			<fc:breadCrumbs degree="#{publicEvaluations.degree}" trailingCrumb="#{bundle['public.degree.information.label.evaluations']}"/>
		<h:outputText value="</div>" escape="false"/>
				
		<h:outputText value="<h1>" escape="false"/>
		<h:outputText rendered="#{!publicEvaluations.degree.bolonhaDegree}" value="#{bundleEnum[publicEvaluations.degree.tipoCurso.name]}"/>
		<h:outputText rendered="#{publicEvaluations.degree.bolonhaDegree}" value="#{bundleEnum[publicEvaluations.degree.bolonhaDegreeType.name]}"/>
		<h:outputText value="#{bundle['public.degree.information.label.in']}"/>
		<h:outputText value="#{publicEvaluations.degreeName}"/>
		<h:outputText value="</h1>" escape="false"/>
		
		<h:outputFormat value="<h2 class='greytxt'>#{bundle['public.degree.information.label.evaluations']}</h2>" escape="false"/>

		<h:outputText rendered="#{empty publicEvaluations.degree.mostRecentDegreeCurricularPlan}" value="<p><em>#{bundleApplication['error.curricularPlanHasNoExecutionDegreesInNotClosedYears']}</em></p>"escape="false"/>
		<h:panelGroup rendered="#{!empty publicEvaluations.degree.mostRecentDegreeCurricularPlan}">
			<h:outputText value="<p>#{bundle['public.degree.information.label.curricularPlan']}: " escape="false"/>
			<h:selectOneMenu id="degreeCurricularPlanID" value="#{publicEvaluations.degreeCurricularPlanID}"
					onchange="this.form.submit();">
				<f:selectItems value="#{publicEvaluations.degreeCurricularPlanSelectItems}"/>
			</h:selectOneMenu>
			<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'/>" escape="false"/>
			<h:outputText value="</p>" escape="false"/>

			<h:outputText value="<p>#{bundle['public.execution.period']}: " escape="false"/>
			<h:selectOneMenu id="executionPeriodID" value="#{publicEvaluations.executionPeriodID}"
					onchange="this.form.submit();">
				<f:selectItems value="#{publicEvaluations.executionPeriodSelectItems}"/>
			</h:selectOneMenu>
			<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID2' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'/>" escape="false"/>
			<h:outputText value="</p>" escape="false"/>
	
			<h:outputText value="<p>#{bundle['public.curricular.year']}: " escape="false"/>
			<h:selectOneMenu id="curricularYearID" value="#{publicEvaluations.curricularYearID}"
					onchange="this.form.submit();">
				<f:selectItem itemLabel="#{bundle['public.curricular.years.all']}" itemValue=""/>
				<f:selectItems value="#{publicEvaluations.curricularYearSelectItems}"/>
			</h:selectOneMenu>
			<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID3' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'/>" escape="false"/>
			<h:outputText value="</p>" escape="false"/>
	
			<h:outputText value="<br/>" escape="false"/>

		 	<fc:fenixCalendar 
			 		begin="#{publicEvaluations.beginDate}" 
			 		end="#{publicEvaluations.endDate}"
			 		editLinkPage="#{publicEvaluations.applicationContext}/publico/executionCourse.do"
			 		editLinkParameters="#{publicEvaluations.calendarLinks}"/>

		</h:panelGroup>
	</h:form>

</ft:tilesView>
