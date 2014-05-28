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

<f:view>
	<h:outputText value="#{evaluationManagementBackingBean.hackToStoreExecutionCourse}" />
	<jsp:include page="/teacher/evaluation/evaluationMenu.jsp" />

	<f:loadBundle basename="resources/HtmlaltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="enumerationBundle"/>	
	<f:loadBundle basename="resources/ApplicationResources" var="bundle"/>

	<h:outputFormat value="<h2>#{bundle['title.evaluation.manage.marksList']}</h2>" escape="false">
		<f:param value="#{evaluationManagementBackingBean.executionCourse.nome}" />
	</h:outputFormat>
	<h:messages layout="table" errorClass="error"/>
	<h:form>
		<h:outputText value="<input type=hidden name='executionCourseID' value='#{evaluationManagementBackingBean.executionCourse.externalId}'/>" escape="false"/>
		<h:inputHidden binding="#{evaluationManagementBackingBean.executionCourseIdHidden}" />
		<h:inputHidden binding="#{evaluationManagementBackingBean.evaluationIdHidden}" />

		<h:outputText styleClass="error" rendered="#{!empty evaluationManagementBackingBean.errorMessage}"
			value="#{bundle[evaluationManagementBackingBean.errorMessage]}"/>
			
			<h:panelGroup rendered="#{evaluationManagementBackingBean.evaluation.class.name == 'net.sourceforge.fenixedu.domain.onlineTests.OnlineTest'}">
				<h:outputText value="<b>#{bundle['lable.test']}:</b> " escape="false"/>
				<h:outputText value="#{evaluationManagementBackingBean.evaluation.distributedTest.evaluationTitle}, "/>
				<h:outputText value="#{bundle['label.day']}" />
				<h:outputFormat value="{0, date, dd/MM/yyyy}">
					<f:param value="#{evaluationManagementBackingBean.evaluation.distributedTest.beginDateDate}"/>
				</h:outputFormat>
				<h:outputText value=" #{bundle['label.at']}" />
				<h:outputFormat value="{0, date, HH:mm}">
					<f:param value="#{evaluationManagementBackingBean.evaluation.distributedTest.beginHourDate}"/>
				</h:outputFormat>
			</h:panelGroup>

			<h:panelGroup rendered="#{evaluationManagementBackingBean.evaluation.class.name == 'net.sourceforge.fenixedu.domain.WrittenTest'}">
				<h:outputText value="<b>#{bundle['label.written.test']}:</b> " escape="false"/>
				<h:outputText value="#{evaluationManagementBackingBean.evaluation.description}, "/>
				<h:outputText value="#{bundle['label.day']}" />
				<h:outputFormat value="{0, date, dd/MM/yyyy}">
					<f:param value="#{evaluationManagementBackingBean.evaluation.dayDate}"/>
				</h:outputFormat>
				<h:outputText value=" #{bundle['label.at']}" />
				<h:outputFormat value="{0, date, HH:mm}">
					<f:param value="#{evaluationManagementBackingBean.evaluation.beginningDate}"/>
				</h:outputFormat>
			</h:panelGroup>

			<h:panelGroup rendered="#{evaluationManagementBackingBean.evaluation.class.name == 'net.sourceforge.fenixedu.domain.Exam'}">
				<h:outputText value="<b>#{bundle['label.exam']}:</b> " escape="false"/>
				<h:outputText value="#{evaluationManagementBackingBean.evaluation.season}, "/>
				<h:outputText value="#{bundle['label.day']}" />
				<h:outputFormat value="{0, date, dd/MM/yyyy}">
					<f:param value="#{evaluationManagementBackingBean.evaluation.dayDate}"/>
				</h:outputFormat>
				<h:outputText value=" #{bundle['label.at']}" />
				<h:outputFormat value="{0, date, HH:mm}">
					<f:param value="#{evaluationManagementBackingBean.evaluation.beginningDate}"/>
				</h:outputFormat>
			</h:panelGroup>
			
			<h:panelGroup rendered="#{evaluationManagementBackingBean.evaluation.class.name == 'net.sourceforge.fenixedu.domain.AdHocEvaluation'}">
				<h:outputText value="<b>#{bundle['label.adHocEvaluation']}:</b> " escape="false"/>
				<h:outputText value="#{evaluationManagementBackingBean.evaluation.name}, "/>		
				<h:outputText value="<b>#{bundle['label.description']}:</b> " escape="false"/>		
				<h:outputText value="#{evaluationManagementBackingBean.evaluation.description}, "/>
			</h:panelGroup>
			

			<h:outputText value="<div class='infoop2'>" escape="false"/>

				<h:outputText value="<p>" escape="false"/>
					<h:outputText value="#{bundle['label.marksOnline.instructions']}" escape="false"/>
				<h:outputText value="</p>" escape="false"/>
				
				<h:panelGroup rendered="#{!empty evaluationManagementBackingBean.gradeScaleDescription}">
					<h:outputText value="<p>" escape="false"/>
						<h:outputText value="#{bundle['label.marksOnline.currentGradeScale']}" escape=""/>
						<h:outputText value="#{evaluationManagementBackingBean.gradeScaleDescription}" escape="false"/>
					<h:outputText value="</p>" escape="false"/>
				</h:panelGroup>

				<h:outputText value="<p>" escape="false"/>
					<h:commandLink action="enterLoadMarks">
						<f:param name="evaluationIDHidden" value="#{evaluationManagementBackingBean.evaluation.externalId}" />
						<f:param name="executionCourseIDHidden" value="#{evaluationManagementBackingBean.executionCourse.externalId}" />
						<f:param name="evaluationID" value="#{evaluationManagementBackingBean.evaluation.externalId}" />
						<f:param name="executionCourseID" value="#{evaluationManagementBackingBean.executionCourse.externalId}" />
						<h:outputFormat value="#{bundle['label.load.marks']}" />
					</h:commandLink>
				<h:outputText value="</p>" escape="false"/>
			<h:outputText value="</div>" escape="false"/>

			<h:outputText styleClass="warning0" rendered="#{evaluationManagementBackingBean.mixedGrades}" value="#{bundle['message.teacher.edit.marks.mixedGrades']}" />
				
			<h:outputText value="<div><table><tr>" escape="false"/>
				<h:outputText value="<td>" escape="false"/>
					<h:outputText value="#{bundle['label.teacher.marks.filter.evaluation.type']}" escape="false"/>
				<h:outputText value="</td>" escape="false"/>
				
				<h:outputText value="<td>" escape="false"/>
					<h:selectOneMenu id="enrolmentFilter" value="#{evaluationManagementBackingBean.enrolmentTypeFilter}">
						<f:selectItems value="#{evaluationManagementBackingBean.enrolmentTypeFilterOptions}"/>
					</h:selectOneMenu>
					<h:commandButton style="display:none; visibility: hidden;" action="#{evaluationManagementBackingBean.editMarks}" value=""/>
					<h:commandButton styleClass="inputbutton" action="#{evaluationManagementBackingBean.filterByEnrolmentType}" value="#{bundle['button.show']}" />
				<h:outputText value="</td>" escape="false"/>
			<h:outputText value="</tr></table></div>" escape="false"/>
	
			<h:dataTable value="#{evaluationManagementBackingBean.executionCourseAttends}" var="attends" styleClass="tstyle4">
				<h:column>
					<f:facet name="header"><h:outputText value="#{bundle['label.number']}"/></f:facet>
					<h:outputText value="#{attends.registration.number}" />
				</h:column>
				<h:column>
					<f:facet name="header"><h:outputText value="#{bundle['label.name']}"/></f:facet>
					<h:outputText value="#{attends.registration.person.name}" />
				</h:column>
				<h:column>
					<f:facet name="header"><h:outputText value="#{bundle['label.enrolmentEvaluationType']}"/></f:facet>
					<h:outputText rendered="#{attends.enrolment == null}" value="#{bundle['message.notEnroled']}"/>
					<h:outputText rendered="#{attends.enrolment != null}" value="#{enumerationBundle[attends.enrolment.enrolmentEvaluationType]}"/>
				</h:column>
				<h:column>
					<f:facet name="header"><h:outputText value="#{bundle['label.Degree']}"/></f:facet>
					<h:outputText value="#{attends.registration.degreeCurricularPlanName}" />
				</h:column>
				<h:column>
					<f:facet name="header"><h:outputText value="#{bundle['label.registration.state']}"/></f:facet>
					<h:outputText value="#{attends.registration.activeStateType.description}" />
				</h:column>
				<h:column>
					<f:facet name="header"><h:outputText value="#{bundle['label.mark']}"/></f:facet>
					<h:inputText alt="#{htmlAltBundle['inputText.number']}" size="3" maxlength="4" value="#{evaluationManagementBackingBean.marks[attends.externalId]}"/>
				</h:column>
			</h:dataTable>
			
			<br/>
			<br/>
			
			<h:panelGroup rendered="#{!empty evaluationManagementBackingBean.studentsWithImpossibleEnrolments}">
				<h:outputText value="<strong>#{bundle['label.markSheet.studentsWithImpossibleEnrolments']}:</strong>" escape="false"/>
				<h:dataTable value="#{evaluationManagementBackingBean.studentsWithImpossibleEnrolments}" var="student" styleClass="tstyle4">
					<h:column>
						<f:facet name="header"><h:outputText value="#{bundle['label.number']}"/></f:facet>
						<h:outputText value="#{student.number}" />
					</h:column>
					<h:column>
						<f:facet name="header"><h:outputText value="#{bundle['label.name']}"/></f:facet>
						<h:outputText value="#{student.person.name}" />
					</h:column>
					<h:column>
						<f:facet name="header"><h:outputText value="#{bundle['label.Degree']}"/></f:facet>
						<h:outputText value="#{attends.registration.degreeCurricularPlanName}" />
					</h:column>
					<h:column>
						<f:facet name="header"><h:outputText value="#{bundle['label.registration.state']}"/></f:facet>
						<h:outputText value="#{attends.registration.activeStateType.description}" />
					</h:column>
				</h:dataTable>
			</h:panelGroup>

		<h:outputText value="<p>" escape="false"/>
			<h:commandButton alt="#{htmlAltBundle['commandButton.save']}" styleClass="inputbutton" action="#{evaluationManagementBackingBean.editMarks}" value="#{bundle['button.save']}"/>
			<h:commandButton alt="#{htmlAltBundle['commandButton.cancel']}" immediate="true" action="#{evaluationManagementBackingBean.evaluation.class.getSimpleName}" styleClass="inputbutton" value="#{bundle['button.cancel']}"/>				
		<h:outputText value="</p>" escape="false"/>
	</h:form>

</f:view>
</div>
</div>
