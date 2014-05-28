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

	<h:outputText value="#{adHocEvaluationManagementBackingBean.hackToStoreExecutionCourse}" />
	<jsp:include page="/teacher/evaluation/evaluationMenu.jsp" />

	<f:loadBundle basename="resources/HtmlaltResources" var="htmlAltBundle"/>

	<f:loadBundle basename="resources/ApplicationResources" var="bundle"/>
	
		<h:form>
			<h:inputHidden binding="#{adHocEvaluationManagementBackingBean.executionCourseIdHidden}" />

			<h:outputFormat value="<h2>#{bundle['link.edit.adHocEvaluation']}</h2/>" escape="false"/>
			<%-- ERROR MESSAGE --%>
			<h:outputText styleClass="error" rendered="#{!empty adHocEvaluationManagementBackingBean.errorMessage}"
				value="#{bundle[adHocEvaluationManagementBackingBean.errorMessage]}<br/>" escape="false" />
			
			<h:panelGrid columns="2" styleClass="tstyle5" columnClasses="aright,,"  rowClasses=",,,valigntop">
				<h:panelGroup>
					<h:outputText value="* " style="color: red"/>
					<h:outputText value="#{bundle['label.net.sourceforge.fenixedu.domain.AdHocEvaluation.name']}: " />
				</h:panelGroup>	
				<h:panelGroup>
					<h:inputText alt="#{htmlAltBundle['inputText.name']}" id="name" required="true" maxlength="100" size="35" value="#{adHocEvaluationManagementBackingBean.name}" />			
					<h:message for="name" styleClass="error"/>
				</h:panelGroup>
				<h:panelGroup>
					<h:outputText value="* " style="color: #c00"/>
					<h:outputText value="#{bundle['label.net.sourceforge.fenixedu.domain.AdHocEvaluation.gradeScale']}:" escape="false"/>
				</h:panelGroup>
				<h:selectOneMenu value="#{adHocEvaluationManagementBackingBean.gradeScale}">
					<f:selectItems value="#{adHocEvaluationManagementBackingBean.gradeScaleOptions}"/>
				</h:selectOneMenu>
				<h:outputText value="#{bundle['label.net.sourceforge.fenixedu.domain.AdHocEvaluation.description']}: " />
				<h:inputTextarea rows="4" cols="40" value="#{adHocEvaluationManagementBackingBean.description}" />
			</h:panelGrid>			

			<h:outputText value="* " style="color: red" escape="false"/>
			<h:outputText value="#{bundle['label.neededFields']}"/>

			<h:outputText value="<br/><br/>" escape="false" />			
			<h:inputHidden id="adHocEvaluationID" value="#{adHocEvaluationManagementBackingBean.adHocEvaluationID}" />
			<h:commandButton alt="#{htmlAltBundle['commandButton.submit']}"  action="#{adHocEvaluationManagementBackingBean.editAdHocEvaluation}"
				styleClass="inputbutton" value="#{bundle['button.submit']}" />
			<h:commandButton alt="#{htmlAltBundle['commandButton.cancel']}" immediate="true" action="adHocEvaluationsIndex"
				styleClass="inputbutton" value="#{bundle['button.cancel']}"/>
		</h:form>
</f:view>
</div>
</div>