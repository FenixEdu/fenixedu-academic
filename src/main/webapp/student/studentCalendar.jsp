<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://fenixedu.org/taglib/jsf-portal" prefix="fp"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-fenix" prefix="fc"%>

<fp:select actionClass="net.sourceforge.fenixedu.presentationTier.Action.student.ViewStudentCalendar" />

<f:view>
	<f:loadBundle basename="resources/HtmlaltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/StudentResources" var="bundle"/>

	<h:outputText value="<h2>#{bundle['link.title.calendar']}</h2>" escape="false"/>
			
		<h:form>
			<fc:viewState binding="#{studentCalendar.viewState}"/>

			<h:panelGrid styleClass="tstyle5" columns="2">
				<h:outputText value="#{bundle['label.choose.degreeCurricularPlan']}:"/>
				<h:panelGroup>
					<h:selectOneMenu id="registrationID" value="#{studentCalendar.registrationID}"
							onchange="this.form.submit();" valueChangeListener="#{studentCalendar.resetExecutionCourses}">
						<f:selectItems value="#{studentCalendar.registrationsSelectItems}"/>
					</h:selectOneMenu>
				</h:panelGroup>
	
				<h:outputText value="#{bundle['label.execution.period']}:"/>
				<h:panelGroup>
					<h:selectOneMenu id="executionPeriodID" value="#{studentCalendar.executionPeriodID}"
							onchange="this.form.submit();" valueChangeListener="#{studentCalendar.resetExecutionCourses}">
						<f:selectItems value="#{studentCalendar.executionPeriodSelectItems}"/>
					</h:selectOneMenu>
				</h:panelGroup>
	
				<h:outputText value="#{bundle['label.execution.course']}:"/>
				<h:selectOneMenu id="executionCourseID" value="#{studentCalendar.executionCourseID}"
						onchange="this.form.submit();" valueChangeListener="#{studentCalendar.resetExecutionCourse}">
					<f:selectItem itemLabel="#{bundle['label.exceution.courses.all']}" itemValue=""/>
					<f:selectItems value="#{studentCalendar.executionCourseSelectItems}"/>
				</h:selectOneMenu>
	
				<h:outputText value="#{bundle['label.evaluation.type']}:"/>
				<h:selectOneMenu id="evaluationTypeClassname" value="#{studentCalendar.evaluationTypeClassname}"
						onchange="this.form.submit();">
					<f:selectItem itemLabel="#{bundle['label.evaluation.type.all']}" itemValue=""/>
					<f:selectItem itemLabel="#{bundle['label.evaluation.shortname.exam']}" itemValue="net.sourceforge.fenixedu.domain.Exam"/>
					<f:selectItem itemLabel="#{bundle['label.evaluation.shortname.test']}" itemValue="net.sourceforge.fenixedu.domain.WrittenTest"/>
					<f:selectItem itemLabel="#{bundle['label.evaluation.shortname.project']}" itemValue="net.sourceforge.fenixedu.domain.Project"/>
				</h:selectOneMenu>
			</h:panelGrid>
			<h:outputText value="<br/>" escape="false"/>
			
			<h:panelGroup rendered="#{empty studentCalendar.calendarStartDate || empty studentCalendar.calendarEndDate}">
				<h:outputText value="<em>#{bundle['error.noAttendsForStudent']}</em>" escape="false"/>
			</h:panelGroup>
	
			<h:panelGroup rendered="#{!empty studentCalendar.calendarStartDate && !empty studentCalendar.calendarEndDate}">		
			 	<fc:fenixCalendar
				 		begin="#{studentCalendar.calendarStartDate}"
				 		end="#{studentCalendar.calendarEndDate}"
				 		editLinkPage="#{studentCalendar.applicationContext}/publico/executionCourse.do"
				 		editLinkParameters="#{studentCalendar.calendarLinks}"/>
			</h:panelGroup>
		</h:form>

</f:view>
