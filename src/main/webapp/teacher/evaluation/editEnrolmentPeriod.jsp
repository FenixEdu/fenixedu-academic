<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://fenixedu.org/taglib/jsf-portal" prefix="fp"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<f:view>

	<h:outputText value="#{evaluationManagementBackingBean.hackToStoreExecutionCourse}" />
	<jsp:include page="/teacher/evaluation/evaluationMenu.jsp" />

	<f:loadBundle basename="resources/HtmlaltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/ApplicationResources" var="bundle"/>

	<h:outputFormat value="<h2>#{bundle['title.evaluation.enrollment.period']}</h2>" escape="false"/>
	
	<h:form>
		<h:inputHidden binding="#{evaluationManagementBackingBean.executionCourseIdHidden}" />
		<h:inputHidden binding="#{evaluationManagementBackingBean.evaluationIdHidden}" />
	
		<h:outputText value="<p>" escape="false" />
			<h:outputText value="#{bundle['label.exam']}: " escape="false" rendered="#{evaluationManagementBackingBean.evaluation.class.name == 'org.fenixedu.academic.domain.Exam'}"/>
			<h:outputText value="#{bundle['label.written.test']}: " escape="false" rendered="#{evaluationManagementBackingBean.evaluation.class.name == 'org.fenixedu.academic.domain.WrittenTest'}"/>
			<h:outputText value="<b>#{evaluationManagementBackingBean.evaluation.season}</b> " escape="false" rendered="#{evaluationManagementBackingBean.evaluation.class.name == 'org.fenixedu.academic.domain.Exam'}"/>
			<h:outputText value="<b>#{evaluationManagementBackingBean.evaluation.description}</b> " escape="false" rendered="#{evaluationManagementBackingBean.evaluation.class.name == 'org.fenixedu.academic.domain.WrittenTest'}"/>
	
			<h:outputFormat value="{0, date, dd/MM/yyyy}">
				<f:param value="#{evaluationManagementBackingBean.evaluation.dayDate}"/>
			</h:outputFormat>
			<h:outputText value=" #{bundle['label.at']} " escape="false"/>
			<h:outputFormat value="{0, date, HH:mm}">
				<f:param value="#{evaluationManagementBackingBean.evaluation.beginningDate}"/>
			</h:outputFormat>
		<h:outputText value="</p>" escape="false" />


		<h:outputText styleClass="error" rendered="#{!empty evaluationManagementBackingBean.errorMessage}"
			value="#{bundle[evaluationManagementBackingBean.errorMessage]}"/>
		<h:messages showSummary="true" errorClass="error" rendered="#{empty evaluationManagementBackingBean.errorMessage}"/>


		<h:outputText value="<table class='tstyle5 thlight thright'>" escape="false" />
			<h:outputText value="<tr>" escape="false" />
				<h:outputText value="<th>" escape="false" />
					<h:outputText value="#{bundle['label.exam.enrollment.begin.day']}" escape="false"/>
				<h:outputText value="</th>" escape="false" />
				<h:outputText value="<td>" escape="false" />
					<h:inputText alt="#{htmlAltBundle['inputText.enrolmentBeginDay']}" required="true" maxlength="2" size="2" value="#{evaluationManagementBackingBean.enrolmentBeginDay}">
						<f:validateLongRange minimum="1" maximum="31" />
					</h:inputText>
					<h:outputText value=" / "/>
					<h:inputText alt="#{htmlAltBundle['inputText.enrolmentBeginMonth']}" required="true" maxlength="2" size="2" value="#{evaluationManagementBackingBean.enrolmentBeginMonth}">
						<f:validateLongRange minimum="1" maximum="12" />
					</h:inputText>
					<h:outputText value=" / "/>
					<h:inputText alt="#{htmlAltBundle['inputText.enrolmentBeginYear']}" required="true" maxlength="4" size="4" value="#{evaluationManagementBackingBean.enrolmentBeginYear}"/>
					<h:outputText value=" #{bundle['label.at']} " escape="false"/>
					<h:inputText alt="#{htmlAltBundle['inputText.enrolmentBeginHour']}" required="true" maxlength="2" size="2" value="#{evaluationManagementBackingBean.enrolmentBeginHour}">
						<f:validateLongRange minimum="0" maximum="23" />
					</h:inputText>
					<h:outputText value=" : "/>
					<h:inputText alt="#{htmlAltBundle['inputText.enrolmentBeginMinute']}" required="true" maxlength="2" size="2" value="#{evaluationManagementBackingBean.enrolmentBeginMinute}">
						<f:validateLongRange minimum="0" maximum="59" />
					</h:inputText>
					<h:outputText value=" <i>#{bundle['label.date.instructions']}</i>" escape="false"/>
				<h:outputText value="</td>" escape="false" />
			<h:outputText value="</tr>" escape="false" />
			
			<h:outputText value="<tr>" escape="false" />
				<h:outputText value="<th>" escape="false" />
					<h:outputText value="#{bundle['label.exam.enrollment.end.day']} " escape="false"/>				
				<h:outputText value="</th>" escape="false" />
				<h:outputText value="<td>" escape="false" />
					<h:inputText alt="#{htmlAltBundle['inputText.enrolmentEndDay']}" required="true" maxlength="2" size="2" value="#{evaluationManagementBackingBean.enrolmentEndDay}">
						<f:validateLongRange minimum="1" maximum="31" />
					</h:inputText>
					<h:outputText value=" / "/>
					<h:inputText alt="#{htmlAltBundle['inputText.enrolmentEndMonth']}" required="true" maxlength="2" size="2" value="#{evaluationManagementBackingBean.enrolmentEndMonth}">
						<f:validateLongRange minimum="1" maximum="12" />
					</h:inputText>
					<h:outputText value=" / "/>
					<h:inputText alt="#{htmlAltBundle['inputText.enrolmentEndYear']}" required="true" maxlength="4" size="4" value="#{evaluationManagementBackingBean.enrolmentEndYear}"/>
					<h:outputText value=" #{bundle['label.at']} " escape="false"/>
					<h:inputText alt="#{htmlAltBundle['inputText.enrolmentEndHour']}" required="true" maxlength="2" size="2" value="#{evaluationManagementBackingBean.enrolmentEndHour}">
						<f:validateLongRange minimum="0" maximum="23" />
					</h:inputText>
					<h:outputText value=" : "/>
					<h:inputText alt="#{htmlAltBundle['inputText.enrolmentEndMinute']}" required="true" maxlength="2" size="2" value="#{evaluationManagementBackingBean.enrolmentEndMinute}">
						<f:validateLongRange minimum="0" maximum="59" />
					</h:inputText>
					<h:outputText value=" <i>#{bundle['label.date.instructions']}</i>" escape="false"/>
				<h:outputText value="</td>" escape="false" />
			<h:outputText value="</tr>" escape="false" />
		<h:outputText value="</table>" escape="false" />
		
		
		<h:outputText value="<p>" escape="false" />
		<h:commandButton alt="#{htmlAltBundle['commandButton.define']}" action="#{evaluationManagementBackingBean.editEvaluationEnrolmentPeriod}" value="#{bundle['button.define']}"/>
		<h:commandButton alt="#{htmlAltBundle['commandButton.cancel']}" immediate="true" action="#{evaluationManagementBackingBean.evaluation.class.getSimpleName}" value="#{bundle['button.cancel']}"/>
		<h:outputText value="</p>" escape="false" />
		
		<c:if test="${evaluationManagementBackingBean.enrolmentPeriodDefined}">
			<h:outputText value="<p><br/>" escape="false" />
				<h:outputText value="#{bundle['message.evaluation.period.delete']}" escape="false"/>
			<h:outputText value="</p>" escape="false" />
			<h:outputText value="<p>" escape="false" />
				<h:commandButton alt="#{htmlAltBundle['commandButton.remove']}" action="#{evaluationManagementBackingBean.removeEvaluationEnrolmentPeriod}" value="#{bundle['button.delete']}"/>
			<h:outputText value="</p>" escape="false" />
		</c:if>
	</h:form>
</f:view>
