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
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-fenix" prefix="fc"%>

<fp:select actionClass="org.fenixedu.academic.ui.struts.action.manager.ManagerApplications$CreateExecutionDegree" />

<f:view>
	<f:loadBundle basename="resources/HtmlaltResources" var="htmlAltBundle"/>

	<f:loadBundle basename="resources/ManagerResources" var="managerResources"/>
	
	<h:outputText styleClass="success0" rendered="#{!empty createExecutionDegrees.createdDegreeCurricularPlans}" value="Os seguintes planos curriculares foram criados correctamente:"/>
	<fc:dataRepeater value="#{createExecutionDegrees.createdDegreeCurricularPlans}" var="degreeCurricularPlan">
		<h:outputText value="<p>#{degreeCurricularPlan.name}</p>" escape="false"/>
	</fc:dataRepeater>

	<p>
	<h:messages errorClass="error0" infoClass="success0"/>
	</p>
	
	<h:form>
		<h:inputHidden value="#{createExecutionDegrees.chosenDegreeType}" />
		<fc:viewState binding="#{createExecutionDegrees.viewState}"/>
						
		<h:outputText value="<strong>Planos Curriculares Pré-Bolonha:</strong>" escape="false" />

		<h:panelGroup rendered="#{!empty createExecutionDegrees.degreeCurricularPlansSelectItems.value}">
			<h:selectManyCheckbox value="#{createExecutionDegrees.choosenDegreeCurricularPlansIDs}" layout="pageDirection" >
				<f:selectItems binding="#{createExecutionDegrees.degreeCurricularPlansSelectItems}" />
			</h:selectManyCheckbox>
		</h:panelGroup>		
		<h:panelGroup rendered="#{empty createExecutionDegrees.degreeCurricularPlansSelectItems.value}">
			<h:outputText value="<p><em>Não existem planos curriculares activos</em></p>" escape="false" />
		</h:panelGroup>

		<br/>
		
		<h:outputText value="<strong>Planos Curriculares de Bolonha:</strong>" escape="false" />
		<h:panelGroup rendered="#{!empty createExecutionDegrees.bolonhaDegreeCurricularPlansSelectItems.value}">
			<h:selectManyCheckbox value="#{createExecutionDegrees.choosenBolonhaDegreeCurricularPlansIDs}" layout="pageDirection" >
				<f:selectItems binding="#{createExecutionDegrees.bolonhaDegreeCurricularPlansSelectItems}" />
			</h:selectManyCheckbox>
		</h:panelGroup>
		<h:panelGroup rendered="#{empty createExecutionDegrees.bolonhaDegreeCurricularPlansSelectItems.value}">
			<h:outputText value="<p><em>Não existem planos curriculares activos e aprovados ou publicados</em></p>" escape="false" />
		</h:panelGroup>

		<br/>
		
		<h:panelGroup rendered="#{!empty createExecutionDegrees.degreeCurricularPlansSelectItems.value || !empty createExecutionDegrees.bolonhaDegreeCurricularPlansSelectItems.value}">
			<h:outputText value="<fieldset class='lfloat3'>" escape="false"/>

				<h:outputText value="<div class='simpleblock4'>" escape="false"/>
				<h:outputText value="<p><label>Ano de execução:</label>" escape="false"/>
					<h:selectOneMenu value="#{createExecutionDegrees.choosenExecutionYearID}" onchange="this.form.submit();" valueChangeListener="#{createExecutionDegrees.onChoosenExecutionYearChanged}">
						<f:selectItems value="#{createExecutionDegrees.executionYears}" />
					</h:selectOneMenu>
				<h:outputText value="</p>" escape="false"/>
				<h:outputText value="<p><label>Campus:</label>" escape="false"/>
					<h:selectOneMenu value="#{createExecutionDegrees.campus}" >
						<f:selectItems value="#{createExecutionDegrees.allCampus}" />
					</h:selectOneMenu>
				<h:outputText value="</p>" escape="false"/>
				<h:outputText value="<p><label>Mapa de exames temporário:</label>" escape="false"/>
					<h:selectBooleanCheckbox value="#{createExecutionDegrees.temporaryExamMap}" />		
				<h:outputText value="</p>" escape="false"/>
				<h:outputText value="</div>" escape="false"/>

			<h:outputText value="</fieldset>" escape="false"/>
			
			<h:commandButton alt="#{htmlAltBundle['commandButton.create']}" action="#{createExecutionDegrees.createExecutionDegrees}" value="#{managerResources['label.create']}" styleClass="inputbutton"/>

		</h:panelGroup>
		
		<h:commandButton alt="#{htmlAltBundle['commandButton.return']}" action="back" value="#{managerResources['label.return']}" immediate="true" styleClass="inputbutton"/>
	</h:form>

</f:view>



