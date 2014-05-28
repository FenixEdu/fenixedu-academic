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

<f:view>

	<h:outputText value="#{evaluationManagementBackingBean.hackToStoreExecutionCourse}" />
	<jsp:include page="/teacher/evaluation/evaluationMenu.jsp" />

	<f:loadBundle basename="resources/HtmlaltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/ApplicationResources" var="bundle"/>

	<h:form>
	
		<h:inputHidden binding="#{evaluationManagementBackingBean.executionCourseIdHidden}" />
		<h:inputHidden binding="#{evaluationManagementBackingBean.evaluationIdHidden}" />
		
		<h:outputText value="<h2>#{bundle['label.students.enrolled']}</h2>" escape="false" />

		<h:outputText value="<table class='tstyle2 thlight thright'>" escape="false" />
			<h:outputText value="<tr>" escape="false" />
				<h:outputText value="<th>" escape="false" />
					<h:outputText value="#{bundle['label.exam']}: " rendered="#{evaluationManagementBackingBean.evaluation.class.name == 'net.sourceforge.fenixedu.domain.Exam'}"/>
					<h:outputText value="#{bundle['label.written.test']}: " rendered="#{evaluationManagementBackingBean.evaluation.class.name == 'net.sourceforge.fenixedu.domain.WrittenTest'}"/>
				<h:outputText value="</th>" escape="false" />
				<h:outputText value="<td>" escape="false" />
					<h:outputText value="<strong>" escape="false" />
						<h:outputText value="#{evaluationManagementBackingBean.evaluation.season}" rendered="#{evaluationManagementBackingBean.evaluation.class.name == 'net.sourceforge.fenixedu.domain.Exam'}"/>
						<h:outputText value="#{evaluationManagementBackingBean.evaluation.description}" rendered="#{evaluationManagementBackingBean.evaluation.class.name == 'net.sourceforge.fenixedu.domain.WrittenTest'}"/>
					<h:outputText value="</strong>" escape="false" />
				<h:outputText value="</td>" escape="false" />
			<h:outputText value="</tr>" escape="false" />
			
			<h:outputText value="<tr>" escape="false" />
				<h:outputText value="<th>" escape="false" />
					<h:outputText value="#{bundle['label.day']}: "/>
				<h:outputText value="</th>" escape="false" />
				<h:outputText value="<td>" escape="false" />
					<h:outputFormat value="{0, date, dd/MM/yy}">
						<f:param value="#{evaluationManagementBackingBean.evaluation.dayDate}"/>
					</h:outputFormat>
				<h:outputText value="</td>" escape="false" />
			<h:outputText value="</tr>" escape="false" />
			
			<h:outputText value="<tr>" escape="false" />
				<h:outputText value="<th>" escape="false" />
					<h:outputText value="#{bundle['label.beginning']}: "/>
				<h:outputText value="</th>" escape="false" />
				<h:outputText value="<td>" escape="false" />
					<h:outputFormat value="{0, date, HH:mm}">
						<f:param value="#{evaluationManagementBackingBean.evaluation.beginningDate}"/>
					</h:outputFormat>
				<h:outputText value="</td>" escape="false" />
			<h:outputText value="</tr>" escape="false" />
			
			<h:outputText value="<tr>" escape="false" />
				<h:outputText value="<th>" escape="false" />
					<h:outputText value="#{bundle['label.number.students.enrolled']}: "/>	
				<h:outputText value="</th>" escape="false" />
				<h:outputText value="<td>" escape="false" />
					<h:outputText value="#{evaluationManagementBackingBean.evaluation.writtenEvaluationEnrolmentsCount}" />
				<h:outputText value="</td>" escape="false" />
			<h:outputText value="</tr>" escape="false" />
			
		<h:outputText value="</table>" escape="false" />
		
		<h:panelGroup rendered="#{!empty evaluationManagementBackingBean.writtenEvaluationEnrolments}">

			<h:graphicImage id="image" alt="Excel" url="/images/excel.gif" />
			<h:outputText value="&nbsp;" escape="false" />
			<fc:commandLink value="#{bundle['link.exportToExcel']}" action="#{evaluationManagementBackingBean.exportStudentsEnroledToExcel}"/>

			<h:outputText value="<table class='tstyle4'>" escape="false" />
				<h:outputText value="<tr>" escape="false" />
					<h:outputText value="<th>" escape="false" />
						<h:outputText value="#{bundle['label.number']}"/>
					<h:outputText value="</th>" escape="false" />
					<h:outputText value="<th>" escape="false" />
						<h:outputText value="#{bundle['label.name']}"/>
					<h:outputText value="</th>" escape="false" />
					<h:outputText value="<th>" escape="false" />
						<h:outputText value="#{bundle['label.room']}"/>
					<h:outputText value="</th>" escape="false" />
					<h:outputText value="<th>" escape="false" />
						<h:outputText value="#{bundle['label.degree.name']}"/>
					<h:outputText value="</th>" escape="false" />
				<h:outputText value="</tr>" escape="false" />

				<fc:dataRepeater value="#{evaluationManagementBackingBean.writtenEvaluationEnrolments}" var="writtenEvaluationEnrolment">
					<h:outputText value="<tr>" escape="false" />
						<h:outputText value="<td>" escape="false" />
							<h:outputText value="#{writtenEvaluationEnrolment.student.number}" />
						<h:outputText value="</td>" escape="false" />
						<h:outputText value="<td>" escape="false" />
							<h:outputText value="#{writtenEvaluationEnrolment.student.person.name}" />
						<h:outputText value="</td>" escape="false" />
						<h:outputText value="<td>" escape="false" />
							<h:outputText value="#{(!empty writtenEvaluationEnrolment.room) ? writtenEvaluationEnrolment.room.name : \" \"}" />
						<h:outputText value="</td>" escape="false" />
						<h:outputText value="<td>" escape="false" />
							<h:outputText value="#{writtenEvaluationEnrolment.student.degree.name}" />
						<h:outputText value="</td>" escape="false" />
					<h:outputText value="</tr>" escape="false" />
				</fc:dataRepeater>
				
			<h:outputText value="</table>" escape="false" />
			
		</h:panelGroup>

		<h:outputText value="<p>" escape="false" />
			<h:commandButton alt="#{htmlAltBundle['commandButton.goBack']}" action="#{evaluationManagementBackingBean.evaluation.class.getSimpleName}" value="#{bundle['link.goBack']}" styleClass="inputButton"  />
		<h:outputText value="</p>" escape="false" />
	</h:form>
</f:view>

</div>
</div>
