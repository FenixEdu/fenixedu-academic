<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>

<style>@import url(<%= request.getContextPath() %>/CSS/dotist_calendars.css);</style>

<style>
.greyBorderClass {
	background-color: #EBECED;
	border-style: solid;
	border-width: 1px;
	border-color: #909090;
	width: 100%
}
.blackBorderClass {
	background-color: #ffffff;
	border-style: solid;
	border-width: 1px;
	border-color: #909090
}
.boldFontClass { 
	font-weight: bold
}
</style>

<ft:tilesView definition="definition.public.mainPageIST" attributeName="body-inline">
	<f:loadBundle basename="resources/PublicDegreeInformation" var="bundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="bundleEnum"/>

	<h:form>
		<h:outputText escape="false" value="<input id='degreeID' name='degreeID' type='hidden' value='#{publicEvaluations.degreeID}'/>"/>

		<fc:breadCrumbs degree="#{publicEvaluations.degree}" trailingCrumb="#{bundle['public.degree.information.label.evaluations']}"/>

		<h:outputText value="#{bundleEnum[publicEvaluations.degree.tipoCurso.name]}"/>
		<h:outputText value="#{bundle['public.degree.information.label.in']}"/>
		<h:outputText value="#{publicEvaluations.degreeName}"/>

		<h:outputText value="<br/><br/>" escape="false"/>

		<h:outputText value="#{bundle['public.degree.curricular.plan.of']}"/>
		<h:selectOneMenu id="degreeCurricularPlanID" value="#{publicEvaluations.degreeCurricularPlanID}"
				onchange="this.form.submit();">
			<f:selectItems value="#{publicEvaluations.degreeCurricularPlanSelectItems}"/>
		</h:selectOneMenu>

		<h:outputText value="<br/><br/>" escape="false"/>

		<h:outputText value="#{bundle['public.execution.period']}"/>
		<h:selectOneMenu id="executionPeriodID" value="#{publicEvaluations.executionPeriodID}"
				onchange="this.form.submit();">
			<f:selectItems value="#{publicEvaluations.executionPeriodSelectItems}"/>
		</h:selectOneMenu>

		<h:outputText value="<br/><br/>" escape="false"/>

		<h:outputText value="#{bundle['public.curricular.year']}"/>
		<h:selectOneMenu id="curricularYearID" value="#{publicEvaluations.curricularYearID}"
				onchange="this.form.submit();">
			<f:selectItem itemLabel="#{bundle['public.curricular.years.all']}" itemValue=""/>
			<f:selectItems value="#{publicEvaluations.curricularYearSelectItems}"/>
		</h:selectOneMenu>

		<h:outputText value="<br/><br/>" escape="false"/>

	 	<fc:fenixCalendar 
		 		begin="#{publicEvaluations.beginDate}" 
		 		end="#{publicEvaluations.endDate}"
		 		editLinkPage="#{publicEvaluations.applicationContext}/publico/viewSite.do"
		 		editLinkParameters="#{publicEvaluations.calendarLinks}"/>

	</h:form>

</ft:tilesView>
