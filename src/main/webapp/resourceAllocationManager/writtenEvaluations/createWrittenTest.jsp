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

<fp:select actionClass="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.exams.MainExamsDA" />

<f:view>
	<f:loadBundle basename="resources/HtmlaltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/ResourceAllocationManagerResources" var="bundleSOP"/>
	<f:loadBundle basename="resources/ApplicationResources" var="bundle"/>
		
	<h:outputFormat value="<h2>#{bundle['title.evaluation.create.writtenEvaluation']}</h2>" rendered="#{SOPEvaluationManagementBackingBean.evaluationTypeClassname == 'net.sourceforge.fenixedu.domain.WrittenTest'}" escape="false">
		<f:param value="#{bundle['label.written.test']}" />
	</h:outputFormat>
	<h:outputFormat value="<h2>#{bundle['title.evaluation.create.writtenEvaluation']}</h2>" rendered="#{SOPEvaluationManagementBackingBean.evaluationTypeClassname == 'net.sourceforge.fenixedu.domain.Exam'}" escape="false">
		<f:param value="#{bundle['label.exam']}" />
	</h:outputFormat>

	<h:form>
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.executionCourseIdHidden}" />
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.academicIntervalHidden}"/>
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.executionDegreeIdHidden}" />
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.calendarPeriodHidden}"/>
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.dayHidden}"/>
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.monthHidden}"/>
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.yearHidden}"/>
		<h:inputHidden value="#{SOPEvaluationManagementBackingBean.evaluationTypeClassname}"/>
		<fc:viewState binding="#{SOPEvaluationManagementBackingBean.viewState}" />
		<h:outputText escape="false" value="<input alt='input.academicInterval' id='academicInterval' name='academicInterval' type='hidden' value='#{SOPEvaluationManagementBackingBean.academicInterval}'/>"/>
		<h:outputText escape="false" value="<input alt='input.curricularYearIDsParameterString' id='curricularYearIDsParameterString' name='curricularYearIDsParameterString' type='hidden' value='#{SOPEvaluationManagementBackingBean.curricularYearIDsParameterString}'/>"/>

		<h:outputText value="<div class='infoop2 mtop05'>" escape="false"/>
			<h:outputText value="#{bundleSOP['property.academicInterval']}: <b>#{SOPEvaluationManagementBackingBean.academicIntervalLabel}</b><br/>" escape="false"/>
			<h:outputText value="#{bundleSOP['property.context.degree']}: <b>#{SOPEvaluationManagementBackingBean.executionDegreeLabel}</b><br/>" escape="false"/>
			<h:outputText value="#{bundleSOP['property.context.curricular.year']}: <b>#{SOPEvaluationManagementBackingBean.curricularYearIDsParameterString}</b><br/>" escape="false"/>
			<h:outputText value="#{bundleSOP['property.courses']}: " escape="false"/>
			
			<h:outputText value="<ul class='mbottom05'>" escape="false"/>
			<fc:dataRepeater value="#{SOPEvaluationManagementBackingBean.associatedExecutionCourses}" var="associatedExecutionCourseID">
				<h:outputText value="<li>#{SOPEvaluationManagementBackingBean.associatedExecutionCoursesNames[associatedExecutionCourseID]}</li>" escape="false"/>
			</fc:dataRepeater>
			<h:outputText value="</ul>" escape="false"/>
			
			<h:outputText rendered="#{empty SOPEvaluationManagementBackingBean.associatedExecutionCourses}" value="<b>#{bundleSOP['label.no.associated.curricular.courses']}</b>" escape="false"/>		
		<h:outputText value="</div>" escape="false"/>
	
		<h:outputText value="<p class='mvert15'>" escape="false"/>
			<h:outputText styleClass="error" rendered="#{!empty SOPEvaluationManagementBackingBean.errorMessage}"
				value="#{bundleSOP[SOPEvaluationManagementBackingBean.errorMessage]}"/>
			<h:messages showSummary="true" errorClass="error" rendered="#{empty SOPEvaluationManagementBackingBean.errorMessage}"/>
		<h:outputText value="</p>" escape="false"/>
		
		<h:panelGrid columnClasses="infocell" columns="2" border="0">
			<h:panelGroup>
				<h:outputText value="#{bundle['label.date']}" escape="false"/>
			</h:panelGroup>
			<h:panelGroup>
				<h:inputText alt="#{htmlAltBundle['inputText.day']}" required="true" maxlength="2" size="2" value="#{SOPEvaluationManagementBackingBean.day}">
					<f:validateLongRange minimum="1" maximum="31" />
				</h:inputText>
				<h:outputText value=" / "/>
				<h:inputText alt="#{htmlAltBundle['inputText.month']}" required="true" maxlength="2" size="2" value="#{SOPEvaluationManagementBackingBean.month}">
					<f:validateLongRange minimum="1" maximum="12" />
				</h:inputText>
				<h:outputText value=" / "/>
				<h:inputText alt="#{htmlAltBundle['inputText.year']}" required="true" maxlength="4" size="4" value="#{SOPEvaluationManagementBackingBean.year}"/>
				<h:outputText value=" <i>#{bundle['label.date.instructions.small']}</i>" escape="false"/>
			</h:panelGroup>
			<h:panelGroup>
				<h:outputText value="#{bundle['label.beginning']} " escape="false"/>
			</h:panelGroup>
			<h:panelGroup>
				<h:inputText alt="#{htmlAltBundle['inputText.beginHour']}" required="true" maxlength="2" size="2" value="#{SOPEvaluationManagementBackingBean.beginHour}">
					<f:validateLongRange minimum="0" maximum="23" />
				</h:inputText>
				<h:outputText value=" : "/>
				<h:inputText alt="#{htmlAltBundle['inputText.beginMinute']}" required="true" maxlength="2" size="2" value="#{SOPEvaluationManagementBackingBean.beginMinute}">
					<f:validateLongRange minimum="0" maximum="59" />
				</h:inputText>
				<h:outputText value=" <i>#{bundle['label.hour.instructions']}</i>" escape="false"/>
			</h:panelGroup>
			<h:panelGroup>
				<h:outputText value="#{bundle['label.end']} " escape="false"/>
			</h:panelGroup>
			<h:panelGroup>
				<h:inputText alt="#{htmlAltBundle['inputText.endHour']}" required="true" maxlength="2" size="2" value="#{SOPEvaluationManagementBackingBean.endHour}">
					<f:validateLongRange minimum="0" maximum="23" />
				</h:inputText>
				<h:outputText value=" : "/>
				<h:inputText alt="#{htmlAltBundle['inputText.endMinute']}" required="true" maxlength="2" size="2" value="#{SOPEvaluationManagementBackingBean.endMinute}">
					<f:validateLongRange minimum="0" maximum="59" />
				</h:inputText>
				<h:outputText value=" <i>#{bundle['label.hour.instructions']}</i>" escape="false"/>
			</h:panelGroup>
			<h:panelGroup>
				<h:outputText value="#{bundle['label.description']}" escape="false"
						rendered="#{SOPEvaluationManagementBackingBean.evaluationTypeClassname == 'net.sourceforge.fenixedu.domain.WrittenTest'}"/>
				<h:outputText value="#{bundle['property.exam.season']}" escape="false"
						rendered="#{SOPEvaluationManagementBackingBean.evaluationTypeClassname == 'net.sourceforge.fenixedu.domain.Exam'}"/>
			</h:panelGroup>
			<h:panelGroup>
				<h:inputText alt="#{htmlAltBundle['inputText.description']}" required="true" maxlength="120" size="15" value="#{SOPEvaluationManagementBackingBean.description}"
						rendered="#{SOPEvaluationManagementBackingBean.evaluationTypeClassname == 'net.sourceforge.fenixedu.domain.WrittenTest'}"/>
				<h:selectOneMenu value="#{SOPEvaluationManagementBackingBean.season}"
						rendered="#{SOPEvaluationManagementBackingBean.evaluationTypeClassname == 'net.sourceforge.fenixedu.domain.Exam'}">
					<f:selectItems value="#{SOPEvaluationManagementBackingBean.seasonLabels}" />
				</h:selectOneMenu>
			</h:panelGroup>
		</h:panelGrid>


		<h:outputText value="<p class='mtop15'>" escape="false"/>
			<h:commandLink action="associateRoomToWrittenEvaluation">
				<h:outputText value="#{bundleSOP['property.exam.associateRooms']}" escape="false"/>
				<f:param name="academicInterval" value="#{SOPEvaluationManagementBackingBean.academicInterval}" />
			</h:commandLink>
		<h:outputText value="<br/>" escape="false"/>
			<h:commandLink action="associateExecutionCourseToWrittenEvaluation">
				<h:outputText value="#{bundleSOP['property.exam.associate']}" escape="false"/>
				<f:param name="academicInterval" value="#{SOPEvaluationManagementBackingBean.academicInterval}" />
			</h:commandLink>
		<h:outputText value="</p>" escape="false"/>


		<h:outputText value="<p class='mvert15'>" escape="false"/>
		<h:outputFormat value="<b>#{bundleSOP['lable.associated.rooms']}:</b> <br/><em>#{SOPEvaluationManagementBackingBean.associatedRooms}</em><br/>" rendered="#{SOPEvaluationManagementBackingBean.evaluationTypeClassname == 'net.sourceforge.fenixedu.domain.WrittenTest'}" escape="false">
			<f:param value="#{bundle['label.written.test']}"/>
		</h:outputFormat>
		<h:outputText value="</p>" escape="false"/>
		
		<h:outputText value="<p class='mvert15'>" escape="false"/>
		<h:outputFormat value="<b>#{bundleSOP['lable.associated.rooms']}:</b> <br/><em>#{SOPEvaluationManagementBackingBean.associatedRooms}</em><br/>" rendered="#{SOPEvaluationManagementBackingBean.evaluationTypeClassname == 'net.sourceforge.fenixedu.domain.Exam'}" escape="false">
			<f:param value="#{bundle['label.exam']}"/>
		</h:outputFormat>
		<h:outputText value="</p>" escape="false"/>


		<h:outputFormat value="<b>#{bundleSOP['lable.associated.curricular.courses']}:</b>" rendered="#{SOPEvaluationManagementBackingBean.evaluationTypeClassname == 'net.sourceforge.fenixedu.domain.WrittenTest'}" escape="false">
			<f:param value="#{bundle['label.written.test']}"/>
		</h:outputFormat>
		<h:outputFormat value="<b>#{bundleSOP['lable.associated.curricular.courses']}:</b>" rendered="#{SOPEvaluationManagementBackingBean.evaluationTypeClassname == 'net.sourceforge.fenixedu.domain.Exam'}" escape="false">
			<f:param value="#{bundle['label.exam']}"/>
		</h:outputFormat>

		<h:dataTable value="#{SOPEvaluationManagementBackingBean.associatedExecutionCourses}" var="associatedExecutionCourseID">
			<h:column>
				<h:outputText value="#{SOPEvaluationManagementBackingBean.associatedExecutionCoursesNames[associatedExecutionCourseID]}" escape="false"/>
				<h:commandLink action="#{SOPEvaluationManagementBackingBean.disassociateExecutionCourse}">
					<h:outputText value="#{bundleSOP['property.exam.dissociate']}" escape="false"/>
					<f:param name="executionCourseToDisassociate" value="#{associatedExecutionCourseID}" />
				</h:commandLink>
				<h:selectManyCheckbox value="#{SOPEvaluationManagementBackingBean.curricularCourseScopesToAssociate[associatedExecutionCourseID]}" layout="pageDirection" >
					<f:selectItems value="#{SOPEvaluationManagementBackingBean.curricularCourseScopesSelectItems[associatedExecutionCourseID]}" />	
				</h:selectManyCheckbox>
				<h:selectManyCheckbox value="#{SOPEvaluationManagementBackingBean.curricularCourseContextToAssociate[associatedExecutionCourseID]}" layout="pageDirection" >
					<f:selectItems value="#{SOPEvaluationManagementBackingBean.curricularCourseContextSelectItems[associatedExecutionCourseID]}" />	
				</h:selectManyCheckbox>
			</h:column>
		</h:dataTable>
		<h:outputText rendered="#{empty SOPEvaluationManagementBackingBean.associatedExecutionCourses}" value="<b>#{bundleSOP['label.no.associated.curricular.courses']}</b><br/>" escape="false"/>		

		<h:outputText value="<br/>" escape="false"/>
		<h:commandButton alt="#{htmlAltBundle['commandButton.create']}" action="#{SOPEvaluationManagementBackingBean.createWrittenEvaluation}" styleClass="inputbutton" value="#{bundle['button.create']}"/>
		<h:commandButton alt="#{htmlAltBundle['commandButton.cancel']}" immediate="true" action="writtenEvaluationCalendar" styleClass="inputbutton" value="#{bundle['button.cancel']}"/>
	</h:form>
</f:view>
