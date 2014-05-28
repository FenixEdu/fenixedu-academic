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
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-fenix" prefix="fc"%>

<f:view>
	<h:outputText value="#{adHocEvaluationManagementBackingBean.hackToStoreExecutionCourse}" />
	<jsp:include page="/teacher/evaluation/evaluationMenu.jsp" />

	<f:loadBundle basename="resources/ApplicationResources" var="bundle"/>

	<h:outputText value="<h2>#{bundle['link.adHocEvaluations']}</h2>" escape="false" />
	<h:form>
		<h:inputHidden binding="#{adHocEvaluationManagementBackingBean.executionCourseIdHidden}" />
		
		<%-- ERROR MESSAGE --%>
		<h:outputText styleClass="error" rendered="#{!empty adHocEvaluationManagementBackingBean.errorMessage}"
			value="#{bundle[adHocEvaluationManagementBackingBean.errorMessage]}<br/>" escape="false" />
	
		<h:outputText value="<ul class='mvert15'><li>" escape="false"/>
			<h:commandLink action="enterCreateAdHocEvaluation">
				<h:outputFormat value="#{bundle['link.create.adHocEvaluation']}" escape="false"/>
			</h:commandLink>
		<h:outputText value="</li></ul>" escape="false"/>
		
		<h:panelGroup rendered="#{empty adHocEvaluationManagementBackingBean.associatedAdHocEvaluations}" >
			<h:outputText value="<p><em>" escape="false"/>
			<h:outputText value="#{bundle['message.adHocEvaluations.not.scheduled']}" />
			<h:outputText value="</em></p>" escape="false"/>
		</h:panelGroup>
		<h:panelGroup rendered="#{!empty adHocEvaluationManagementBackingBean.associatedAdHocEvaluations}">
			<fc:dataRepeater  value="#{adHocEvaluationManagementBackingBean.associatedAdHocEvaluations}" var="adHocEvaluation">
					<h:outputText value="<div class='mtop15 mbottom2'>" escape="false" />
					<h:outputText value="<b>#{adHocEvaluation.name}</b>   " escape="false" />
					
					<h:commandLink action="enterEditAdHocEvaluation">
						<f:param id="adHocEvaluationIDToEdit" name="adHocEvaluationID" value="#{adHocEvaluation.externalId}" />
						<h:outputFormat value="#{bundle['link.edit']}"/>
					</h:commandLink>
					<h:outputText value=" | " escape="false"/>
					<h:commandLink action="#{adHocEvaluationManagementBackingBean.deleteAdHocEvaluation}">
						<f:param id="adHocEvaluationIDToDelete" name="adHocEvaluationID" value="#{adHocEvaluation.externalId}" />
						<h:outputFormat value="#{bundle['link.delete']}" />
					</h:commandLink>
					
					<h:outputText value="<p class='indent1 mvert05'>#{bundle['label.students.listMarks']}: " escape="false"/>
					<h:commandLink action="enterShowMarksListOptions">
						<f:param name="evaluationID" value="#{adHocEvaluation.externalId}" />
						<f:param name="executionCourseID" value="#{adHocEvaluationManagementBackingBean.executionCourseID}" />
						<h:outputFormat value="#{bundle['link.teacher.evaluation.grades']}" />
					</h:commandLink>

					<h:outputText value=" | " escape="false"/>
					<h:commandLink action="enterPublishMarks">
						<f:param name="evaluationID" value="#{adHocEvaluation.externalId}" />
						<f:param name="executionCourseID" value="#{adHocEvaluationManagementBackingBean.executionCourseID}" />
						<h:outputFormat value="#{bundle['link.publishMarks']}" />
					</h:commandLink>
					
					<h:outputText value=" <p> " escape="false"/>
					<h:outputText value="#{bundle['label.description']}: " escape="false" rendered="#{!empty adHocEvaluation.description}"/>
					<h:outputText value="#{adHocEvaluation.description}" />
					<h:outputText value=" </p> " escape="false"/>

					<h:outputText value="</div>" escape="false" />
					
			</fc:dataRepeater>
		</h:panelGroup>		
	</h:form>
</f:view>
</div>
</div>
