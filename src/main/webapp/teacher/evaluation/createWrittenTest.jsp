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
	<f:loadBundle basename="resources/HtmlaltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/ApplicationResources" var="bundle"/>

	<h:outputText value="<em>#{bundle['message.evaluationElements']}</em>" escape="false" />
	<h:outputFormat value="<h2>#{bundle['title.evaluation.create.writtenEvaluation']}</h2>" escape="false">
		<f:param value="#{bundle['label.written.test']}" />
	</h:outputFormat>

	<h:form>
		<h:inputHidden binding="#{evaluationManagementBackingBean.executionCourseIdHidden}" />
	
		<h:outputText styleClass="error" rendered="#{!empty evaluationManagementBackingBean.errorMessage}"
			value="#{bundle[evaluationManagementBackingBean.errorMessage]}"/>
		<h:messages showSummary="true" errorClass="error" rendered="#{empty evaluationManagementBackingBean.errorMessage}"/>
		
		<h:outputText value="<table class='tstyle5 thright thlight'>" escape="false"/>
			<h:outputText value="<tr>" escape="false"/>
			
				<h:outputText value="<th>" escape="false"/>
					<h:outputText value="#{bundle['label.date']}:" escape="false"/>
				<h:outputText value="</th>" escape="false"/>
				
				<h:outputText value="<td>" escape="false"/>
					<h:inputText alt="#{htmlAltBundle['inputText.day']}" required="true" maxlength="2" size="2" value="#{evaluationManagementBackingBean.day}">
						<f:validateLongRange minimum="1" maximum="31" />
					</h:inputText>
					<h:outputText value=" / "/>
					<h:inputText alt="#{htmlAltBundle['inputText.month']}" required="true" maxlength="2" size="2" value="#{evaluationManagementBackingBean.month}">
						<f:validateLongRange minimum="1" maximum="12" />
					</h:inputText>
					<h:outputText value=" / "/>
					<h:inputText alt="#{htmlAltBundle['inputText.year']}" required="true" maxlength="4" size="4" value="#{evaluationManagementBackingBean.year}"/>
					<h:outputText value=" <i>#{bundle['label.date.instructions.small']}</i>" escape="false"/>
				<h:outputText value="</td>" escape="false"/>
				
			<h:outputText value="</tr>" escape="false"/>
			
			<h:outputText value="<tr>" escape="false"/>
			
				<h:outputText value="<th>" escape="false"/>
					<h:outputText value="#{bundle['label.beginning']}:" escape="false"/>
				<h:outputText value="</th>" escape="false"/>
			
				<h:outputText value="<td>" escape="false"/>
					<h:inputText alt="#{htmlAltBundle['inputText.beginHour']}" required="true" maxlength="2" size="2" value="#{evaluationManagementBackingBean.beginHour}">
						<f:validateLongRange minimum="0" maximum="23" />
					</h:inputText>
					<h:outputText value=" : "/>
					<h:inputText alt="#{htmlAltBundle['inputText.beginMinute']}" required="true" maxlength="2" size="2" value="#{evaluationManagementBackingBean.beginMinute}">
						<f:validateLongRange minimum="0" maximum="59" />
					</h:inputText>
					<h:outputText value=" <i>#{bundle['label.hour.instructions']}</i>" escape="false"/>
				<h:outputText value="</td>" escape="false"/>
				
			<h:outputText value="<tr>" escape="false"/>
			
			<h:outputText value="<tr>" escape="false"/>
				<h:outputText value="<th>" escape="false"/>
					<h:outputText value="#{bundle['label.end']}:" escape="false"/>
				<h:outputText value="</th>" escape="false"/>
			
				<h:outputText value="<td>" escape="false"/>
					<h:inputText alt="#{htmlAltBundle['inputText.endHour']}" required="true" maxlength="2" size="2" value="#{evaluationManagementBackingBean.endHour}">
						<f:validateLongRange minimum="0" maximum="23" />
					</h:inputText>
					<h:outputText value=" : "/>
					<h:inputText alt="#{htmlAltBundle['inputText.endMinute']}" required="true" maxlength="2" size="2" value="#{evaluationManagementBackingBean.endMinute}">
						<f:validateLongRange minimum="0" maximum="59" />
					</h:inputText>
					<h:outputText value=" <i>#{bundle['label.hour.instructions']}</i>" escape="false"/>
				<h:outputText value="</td>" escape="false"/>
			
			<h:outputText value="</tr>" escape="false"/>
			
			<h:outputText value="<tr>" escape="false"/>
			
				<h:outputText value="<th>" escape="false"/>
					<h:outputText value="#{bundle['label.description']}:" escape="false"/>
				<h:outputText value="</th>" escape="false"/>
				
				<h:outputText value="<td>" escape="false"/>
					<h:inputText alt="#{htmlAltBundle['inputText.description']}" required="true" maxlength="120" size="15" value="#{evaluationManagementBackingBean.description}"/>
				<h:outputText value="</td>" escape="false"/>
			
			<h:outputText value="</tr>" escape="false"/>
			
			<h:outputText value="<tr>" escape="false"/>
				<h:outputText value="<th>" escape="false"/>
					<h:outputText value="#{bundle['label.gradeScale']}:" escape="false"/>
				<h:outputText value="</th>" escape="false"/>
				
				<h:outputText value="<td>" escape="false"/>
					<h:selectOneMenu value="#{evaluationManagementBackingBean.gradeScale}">
						<f:selectItems value="#{evaluationManagementBackingBean.gradeScaleOptions}"/>
					</h:selectOneMenu>
				<h:outputText value="</td>" escape="false"/>
			<h:outputText value="</tr>" escape="false"/>

		<h:outputText value="</table>" escape="false"/>
		
		
		<h:outputText value="<p>" escape="false"/>
			<h:commandButton alt="#{htmlAltBundle['commandButton.create']}" action="#{evaluationManagementBackingBean.createWrittenTest}" styleClass="inputbutton" value="#{bundle['button.create']}"/>
			<h:commandButton alt="#{htmlAltBundle['commandButton.cancel']}" immediate="true" action="WrittenTest" styleClass="inputbutton" value="#{bundle['button.cancel']}"/>				
		<h:outputText value="</p>" escape="false"/>
		
	</h:form>
</f:view>
