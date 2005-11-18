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

<ft:tilesView definition="definition.student.masterPage" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/StudentResources" var="bundle"/>

	<h:form>
		<h:outputText value="<h2>#{bundle['link.title.calendar']}</h2>" escape="false"/>

		<h:panelGrid styleClass="infoselected" columns="2">
			<h:outputText value="#{bundle['label.execution.period']}"/>
			<h:selectOneMenu id="executionPeriodID" value="#{studentCalendar.executionPeriodID}"
					onchange="this.form.submit();">
				<f:selectItems value="#{studentCalendar.executionPeriodSelectItems}"/>
			</h:selectOneMenu>

			<h:outputText value="#{bundle['label.execution.course']}"/>
			<h:selectOneMenu id="executionCourseID" value="#{studentCalendar.executionCourseID}"
					onchange="this.form.submit();">
				<f:selectItem itemLabel="#{bundle['label.exceution.courses.all']}" itemValue=""/>
				<f:selectItems value="#{studentCalendar.executionCourseSelectItems}"/>
			</h:selectOneMenu>

			<h:outputText value="#{bundle['label.evaluation.type']}"/>
			<h:selectOneMenu id="evaluationTypeClassname" value="#{studentCalendar.evaluationTypeClassname}"
					onchange="this.form.submit();">
				<f:selectItem itemLabel="#{bundle['label.evaluation.type.all']}" itemValue=""/>
				<f:selectItem itemLabel="#{bundle['label.evaluation.shortname.exam']}" itemValue="net.sourceforge.fenixedu.domain.Exam"/>
				<f:selectItem itemLabel="#{bundle['label.evaluation.shortname.test']}" itemValue="net.sourceforge.fenixedu.domain.WrittenTest"/>
			</h:selectOneMenu>
		</h:panelGrid>

		<h:outputText value="<br/><br/>" escape="false"/>

	 	<fc:fenixCalendar
		 		begin="#{studentCalendar.calendarStartDate}"
		 		end="#{studentCalendar.calendarEndDate}"
		 		editLinkPage="#{studentCalendar.applicationContext}/publico/viewSite.do"
		 		editLinkParameters="#{studentCalendar.calendarLinks}"/>
	</h:form>

</ft:tilesView>