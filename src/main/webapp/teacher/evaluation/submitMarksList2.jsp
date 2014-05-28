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
	<f:loadBundle basename="resources/HtmlaltResources" var="htmlAltBundle"/>

	<f:loadBundle basename="resources/ApplicationResources" var="bundle"/>

	<h:outputFormat value="<h2>#{bundle['label.submit.listMarks']}</h2>" escape="false"/>

	<h:form>
		<fc:viewState binding="#{evaluationManagementBackingBean.viewState}"/>
		<h:inputHidden binding="#{evaluationManagementBackingBean.executionCourseIdHidden}" />
		<h:inputHidden binding="#{evaluationManagementBackingBean.evaluationIdHidden}" />

		<h:panelGrid styleClass="infoop" columns="1">
			<h:outputText value="#{bundle['label.submitMarks.evaluationDate.instructions']}" escape="false"/>
		</h:panelGrid>
		<h:outputText styleClass="error" rendered="#{!empty evaluationManagementBackingBean.errorMessage}"
				value="#{evaluationManagementBackingBean.errorMessage}<br/>" escape="false" />		
		<h:messages styleClass="error" layout="table"/>
		<h:outputText value="<br/>" escape="false"/>
		<h:outputText value="#{bundle['label.submitMarks.examDate']}:"/>
		<h:inputText alt="#{htmlAltBundle['inputText.submitEvaluationDateTextBoxValue']}" required="true" binding="#{evaluationManagementBackingBean.submitEvaluationDateTextBox}"  value="#{evaluationManagementBackingBean.submitEvaluationDateTextBoxValue}" size="12" maxlength="10">
			<fc:dateValidator format="dd/MM/yyyy" strict="false"/>
		</h:inputText>
		<h:outputText value="#{bundle['message.dateFormat']}<br/><br/>" escape="false"/>
		<h:commandButton alt="#{htmlAltBundle['commandButton.submit']}" styleClass="inputbutton" action="#{evaluationManagementBackingBean.submitMarks2}" value="#{bundle['button.submit']}"/>
		<h:commandButton alt="#{htmlAltBundle['commandButton.cancel']}" immediate="true" action="enterSubmitMarksList" styleClass="inputbutton" value="#{bundle['button.cancel']}"/>
	</h:form> 
</f:view>
