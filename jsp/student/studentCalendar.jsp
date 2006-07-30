<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>

<ft:tilesView definition="definition.student.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/StudentResources" var="bundle"/>

	<h:form>
		<fc:viewState binding="#{studentCalendar.viewState}"/>
		<h:outputText value="<h2>#{bundle['link.title.calendar']}</h2>" escape="false"/>

		<h:panelGrid styleClass="infoselected" columns="2">
			<h:outputText value="#{bundle['label.execution.period']}"/>
			<h:selectOneMenu id="executionPeriodID" value="#{studentCalendar.executionPeriodID}"
					onchange="this.form.submit();" valueChangeListener="#{studentCalendar.resetExecutionCourses}">
				<f:selectItems value="#{studentCalendar.executionPeriodSelectItems}"/>
			</h:selectOneMenu>
			<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'/>" escape="false"/>

			<h:outputText value="#{bundle['label.execution.course']}"/>
			<h:selectOneMenu id="executionCourseID" value="#{studentCalendar.executionCourseID}"
					onchange="this.form.submit();" valueChangeListener="#{studentCalendar.resetExecutionCourse}">
				<f:selectItem itemLabel="#{bundle['label.exceution.courses.all']}" itemValue=""/>
				<f:selectItems value="#{studentCalendar.executionCourseSelectItems}"/>
			</h:selectOneMenu>

			<h:outputText value="#{bundle['label.evaluation.type']}"/>
			<h:selectOneMenu id="evaluationTypeClassname" value="#{studentCalendar.evaluationTypeClassname}"
					onchange="this.form.submit();">
				<f:selectItem itemLabel="#{bundle['label.evaluation.type.all']}" itemValue=""/>
				<f:selectItem itemLabel="#{bundle['label.evaluation.shortname.exam']}" itemValue="net.sourceforge.fenixedu.domain.Exam"/>
				<f:selectItem itemLabel="#{bundle['label.evaluation.shortname.test']}" itemValue="net.sourceforge.fenixedu.domain.WrittenTest"/>
				<f:selectItem itemLabel="#{bundle['label.evaluation.shortname.project']}" itemValue="net.sourceforge.fenixedu.domain.Project"/>
			</h:selectOneMenu>
		</h:panelGrid>
		<h:outputText value="<br/><br/>" escape="false"/>
		
		<h:panelGroup rendered="#{empty studentCalendar.calendarStartDate || empty studentCalendar.calendarEndDate}">
			<h:outputText value="#{bundle['error.noAttendsForStudent']}" styleClass="success0" />
		</h:panelGroup>

		<h:panelGroup rendered="#{!empty studentCalendar.calendarStartDate && !empty studentCalendar.calendarEndDate}">		
		 	<fc:fenixCalendar
			 		begin="#{studentCalendar.calendarStartDate}"
			 		end="#{studentCalendar.calendarEndDate}"
			 		editLinkPage="#{studentCalendar.applicationContext}/publico/viewSite.do"
			 		editLinkParameters="#{studentCalendar.calendarLinks}"/>
		</h:panelGroup>
	</h:form>
</ft:tilesView>