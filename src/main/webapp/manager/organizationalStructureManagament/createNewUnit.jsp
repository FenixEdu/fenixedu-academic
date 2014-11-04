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
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-fenix" prefix="fc"%>
<%@ taglib uri="http://fenixedu.org/taglib/jsf-portal" prefix="fp"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>

<fp:select actionClass="org.fenixedu.academic.ui.struts.action.manager.ManagerApplications$OrganizationalStructurePage" />

<f:view>
	<f:loadBundle basename="resources/HtmlaltResources" var="htmlAltBundle"/>
	
	<f:loadBundle basename="resources/ManagerResources" var="bundle"/>
		

		<h:outputText value="<h2>#{bundle['link.new.unit2']}</h2><br/>" escape="false" />
	
		<h:outputText styleClass="error" rendered="#{!empty organizationalStructureBackingBean.errorMessage}"
				value="#{bundle[organizationalStructureBackingBean.errorMessage]}<br/>" escape="false"/>
		
	<h:form styleClass="form-horizontal well">	

		<div class="form-group">
			<label class="col-sm-3 control-label">${bundle['message.name']}</label>
			<h:panelGroup styleClass="col-sm-6">
				<h:inputText alt="#{htmlAltBundle['inputText.unitName']}" id="name" required="true" size="60" value="#{organizationalStructureBackingBean.unitName}" styleClass="form-control" />
			</h:panelGroup>
			<span class="col-sm-3">
				<h:message for="name" styleClass="error"/>
			</span>
		</div>

		<div class="form-group">
			<label class="col-sm-3 control-label">${bundle['message.name.en']}</label>
			<h:panelGroup styleClass="col-sm-6">
				<h:inputText alt="#{htmlAltBundle['inputText.unitName']}" id="nameEn" required="false" size="60" value="#{organizationalStructureBackingBean.unitName}" styleClass="form-control" />
			</h:panelGroup>
			<span class="col-sm-3">
				<h:message for="nameEn" styleClass="error"/>
			</span>
		</div>

		<div class="form-group">
			<label class="col-sm-3 control-label">${bundle['message.name.card']}</label>
			<h:panelGroup styleClass="col-sm-6">
				<h:inputText alt="#{htmlAltBundle['inputText.unitName']}" id="nameCard" required="false" size="42" maxlength="42" value="#{organizationalStructureBackingBean.unitNameCard}" styleClass="form-control" />
			</h:panelGroup>
			<span class="col-sm-3">
				<h:message for="nameCard" styleClass="error"/>
			</span>
		</div>

		<div class="form-group">
			<label class="col-sm-3 control-label">${bundle['message.costCenter']}</label>
			<h:panelGroup styleClass="col-sm-2">
				<h:inputText alt="#{htmlAltBundle['inputText.unitCostCenter']}" id="costCenter" size="10" value="#{organizationalStructureBackingBean.unitCostCenter}" styleClass="form-control">
					<fc:regexValidator regex="[0-9]*"/>
				</h:inputText>
			</h:panelGroup>
			<span class="col-sm-6">
				<h:message for="costCenter" styleClass="error"/>
			</span>
		</div>	

		<div class="form-group">
			<label class="col-sm-3 control-label">${bundle['message.acronym']}</label>
			<h:panelGroup styleClass="col-sm-2">
				<h:inputText alt="#{htmlAltBundle['inputText.unitAcronym']}" size="10" value="#{organizationalStructureBackingBean.unitAcronym}" styleClass="form-control" />
			</h:panelGroup>
		</div>

		<div class="form-group">
			<label class="col-sm-3 control-label">${bundle['message.webAddress']}</label>
			<h:panelGroup styleClass="col-sm-6">
				<h:inputText alt="#{htmlAltBundle['inputText.unitWebAddress']}" size="30" value="#{organizationalStructureBackingBean.unitWebAddress}" styleClass="form-control" />		
			</h:panelGroup>
		</div>

		<div class="form-group">
			<label class="col-sm-3 control-label">${bundle['message.initialDate']}</label>
			<h:panelGroup styleClass="col-sm-2">
				<h:inputText alt="#{htmlAltBundle['inputText.unitBeginDate']}" maxlength="10" id="beginDate" required="true" size="10" value="#{organizationalStructureBackingBean.unitBeginDate}" styleClass="form-control">
					<fc:dateValidator format="dd/MM/yyyy" strict="true"/>
				</h:inputText>
			</h:panelGroup>
			<span class="col-lg-3">
				<h:outputText value="#{bundle['date.format']}"/>&nbsp;
				<h:message for="beginDate" styleClass="error"/>
			</span>
		</div>

		<div class="form-group">
			<label class="col-sm-3 control-label">${bundle['message.endDate']}</label>
			<h:panelGroup styleClass="col-sm-2">
				<h:inputText alt="#{htmlAltBundle['inputText.unitEndDate']}" maxlength="10" id="endDate" size="10" value="#{organizationalStructureBackingBean.unitEndDate}" styleClass="form-control">
					<fc:dateValidator format="dd/MM/yyyy" strict="true"/>
				</h:inputText>
			</h:panelGroup>
			<span class="col-lg-3">
				<h:outputText value="#{bundle['date.format']}"/>&nbsp;
				<h:message for="endDate" styleClass="error"/>
			</span>
		</div>

		<div class="form-group">
			<label class="col-sm-3 control-label">${bundle['message.canBeResponsibleOfSpaces']}</label>
			<div class="col-sm-9">
				<div class="checkbox">
					<h:selectBooleanCheckbox value="#{organizationalStructureBackingBean.canBeResponsibleOfSpaces}" /> 
				</div>
			</div>
		</div>

		<div class="form-group">
			<label class="col-sm-3 control-label">${bundle['message.unitClassification']}</label>
			<h:panelGroup styleClass="col-sm-6">
				<fc:selectOneMenu value="#{organizationalStructureBackingBean.unitClassificationName}" styleClass="form-control">
					<f:selectItems value="#{organizationalStructureBackingBean.validUnitClassifications}"/>				
				</fc:selectOneMenu>
			</h:panelGroup>
		</div>

		<div class="form-group">
			<label class="col-sm-3 control-label">${bundle['message.department']}</label>
			<h:panelGroup styleClass="col-sm-6">
				<fc:selectOneMenu value="#{organizationalStructureBackingBean.departmentID}" styleClass="form-control">
					<f:selectItems value="#{organizationalStructureBackingBean.departments}"/>				
				</fc:selectOneMenu>
			</h:panelGroup>
		</div>

				<div class="form-group">
			<label class="col-sm-3 control-label">${bundle['message.degree']}</label>
			<h:panelGroup styleClass="col-sm-6">
				<fc:selectOneMenu value="#{organizationalStructureBackingBean.degreeID}" styleClass="form-control">
					<f:selectItems value="#{organizationalStructureBackingBean.degrees}"/>				
				</fc:selectOneMenu>
			</h:panelGroup>
		</div>


		<div class="form-group">
			<label class="col-sm-3 control-label">${bundle['message.administrativeOffice']}</label>
			<h:panelGroup styleClass="col-sm-6">
				<fc:selectOneMenu value="#{organizationalStructureBackingBean.administrativeOfficeID}" styleClass="form-control">
					<f:selectItems value="#{organizationalStructureBackingBean.administrativeOffices}"/>				
				</fc:selectOneMenu>
			</h:panelGroup>
		</div>
			
		<div class="form-group">
			<label class="col-sm-3 control-label">${bundle['message.campus']}</label>
			<h:panelGroup styleClass="col-sm-6">
				<fc:selectOneMenu value="#{organizationalStructureBackingBean.campusID}" styleClass="form-control">
					<f:selectItems value="#{organizationalStructureBackingBean.campuss}"/>				
				</fc:selectOneMenu>
			</h:panelGroup>
		</div>
			
		<div class="form-group">
			<label class="col-sm-3 control-label">${bundle['title.relation.type']}</label>
			<h:panelGroup styleClass="col-sm-6">
				<fc:selectOneMenu value="#{organizationalStructureBackingBean.unitRelationTypeValue}" styleClass="form-control">
					<f:selectItems value="#{organizationalStructureBackingBean.unitRelationTypes}"/>				
				</fc:selectOneMenu>
			</h:panelGroup>
		</div>

		<div class="form-group">
			<label class="col-sm-3 control-label">${bundle['message.unitType']}</label>
			<h:panelGroup styleClass="col-sm-6">
				<fc:selectOneMenu value="#{organizationalStructureBackingBean.unitTypeName}" styleClass="form-control">
					<f:selectItems value="#{organizationalStructureBackingBean.validUnitType}"/>				
				</fc:selectOneMenu>
			</h:panelGroup>
		</div>

		<h:outputText value="<br/>" escape="false" />	
		<h:panelGrid columns="2">
			<h:commandButton alt="#{htmlAltBundle['commandButton.unit2']}" action="#{organizationalStructureBackingBean.createTopUnit}" value="#{bundle['link.new.unit2']}" styleClass="btn btn-info"/>				
			<h:commandButton alt="#{htmlAltBundle['commandButton.return']}" action="listAllUnits" immediate="true" value="#{bundle['label.return']}" styleClass="btn btn-default"/>								
		</h:panelGrid>		
				
	</h:form>
	
</f:view>