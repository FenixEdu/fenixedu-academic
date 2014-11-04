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
		
		<h:inputHidden binding="#{organizationalStructureBackingBean.unitIDHidden}"/>

		<h:outputText value="<h2>#{bundle['link.new.function2']}</h2><br/>" escape="false" />
	
		<span class="alert alert-info">
			<h:outputText value="<b>#{bundle['message.name']}</b>" escape="false"/>		
			<h:outputText value="#{organizationalStructureBackingBean.unit.name}" escape="false"/>												
		</span>	

		<h:outputText value="<br/><br/><br/>" escape="false" />	
	
		<h:outputText styleClass="error" rendered="#{!empty organizationalStructureBackingBean.errorMessage}"
				value="#{bundle[organizationalStructureBackingBean.errorMessage]}<br/>" escape="false"/>

	<h:form styleClass="form-horizontal well">
		
		<div class="form-group">
			<label class="col-sm-3 control-label">${bundle['title.FunctionName']}</label>
			<h:panelGroup styleClass="col-sm-6">
				<h:inputText alt="#{htmlAltBundle['inputText.functionName']}" id="name" required="true" size="30" value="#{organizationalStructureBackingBean.functionName}" styleClass="form-control"/>
			</h:panelGroup>
			<span class="col-sm-3">
				<h:message for="name" styleClass="error"/>
			</span>
		</div>

		<div class="form-group">
			<label class="col-sm-3 control-label">${bundle['title.FunctionName.en']}</label>
			<h:panelGroup styleClass="col-sm-6">
				<h:inputText alt="#{htmlAltBundle['inputText.functionName']}" id="nameEn" required="true" size="30" value="#{organizationalStructureBackingBean.functionName}" styleClass="form-control"/>
			</h:panelGroup>
			<span class="col-sm-3">
				<h:message for="nameEn" styleClass="error"/>
			</span>
		</div>

		<div class="form-group">
			<label class="col-sm-3 control-label">${bundle['message.initialDate']}</label>
			<h:panelGroup styleClass="col-sm-2">
				<h:inputText alt="#{htmlAltBundle['inputText.functionBeginDate']}" maxlength="10" id="beginDate" required="true" size="10" value="#{organizationalStructureBackingBean.functionBeginDate}" styleClass="form-control">
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
				<h:inputText alt="#{htmlAltBundle['inputText.functionEndDate']}" maxlength="10" id="endDate" size="10" value="#{organizationalStructureBackingBean.functionEndDate}" styleClass="form-control">
					<fc:dateValidator format="dd/MM/yyyy" strict="true"/>
				</h:inputText>
			</h:panelGroup>
			<span class="col-lg-3">
				<h:outputText value="#{bundle['date.format']}"/>&nbsp;
				<h:message for="endDate" styleClass="error"/>
			</span>
		</div>

		<div class="form-group">
			<label class="col-sm-3 control-label">${bundle['message.functionType']}</label>
			<h:panelGroup styleClass="col-sm-6">
				<fc:selectOneMenu value="#{organizationalStructureBackingBean.functionTypeName}" styleClass="form-control">
					<f:selectItems value="#{organizationalStructureBackingBean.validFunctionType}"/>				
				</fc:selectOneMenu>
			</h:panelGroup>
		</div>
				
		<h:outputText value="<br/>" escape="false" />	
		<h:panelGrid columns="2">
			<h:commandButton alt="#{htmlAltBundle['commandButton.function2']}" action="#{organizationalStructureBackingBean.createFunction}" value="#{bundle['link.new.function2']}" styleClass="btn btn-info"/>				
			<h:commandButton alt="#{htmlAltBundle['commandButton.return']}" action="backToUnitDetails" immediate="true" value="#{bundle['label.return']}" styleClass="btn btn-default"/>								
		</h:panelGrid>		
				
	</h:form>
	
</f:view>