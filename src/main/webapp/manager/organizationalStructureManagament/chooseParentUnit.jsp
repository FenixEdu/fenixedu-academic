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
	<h:form>	

		<h:inputHidden binding="#{organizationalStructureBackingBean.unitIDHidden}"/>
		<h:inputHidden binding="#{organizationalStructureBackingBean.chooseUnitIDHidden}"/>	
		
		<h:messages infoClass="success0" errorClass="error0" globalOnly="true" layout="table"/>
		
		<h:outputText styleClass="error" rendered="#{!empty organizationalStructureBackingBean.errorMessage}"
				value="#{bundle[organizationalStructureBackingBean.errorMessage]}<br/>" escape="false"/>
										
		<h:outputText value="<h2>#{bundle['title.chooseUnit']}</h2><br/>" escape="false" />		
		
		<span class="alert alert-info">
			<h:outputText value="<b>#{bundle['message.name']}</b>" escape="false"/>		
			<h:outputText value="#{organizationalStructureBackingBean.unit.name}" escape="false"/>												
		</span>
		
		<h:outputText value="<br/><br/><br/>" escape="false" />	
		
		<h:dataTable value="#{organizationalStructureBackingBean.chooseUnit}" styleClass="table" var="unit"
			 headerClass="listClasses-header" columnClasses="listClasses" rendered="#{!empty organizationalStructureBackingBean.chooseUnit}">
			<h:column>
				<f:facet name="header">
					<h:outputText value="#{bundle['message.unitName']}" />
				</f:facet>				
				<h:outputText value="<strong>#{unit.name}</strong>" escape="false"/>
			</h:column>
			<h:column>
				<f:facet name="header">
					<h:outputText value="#{bundle['message.unitCostCenter']}" />
				</f:facet>				
				<h:outputText value="#{unit.costCenterCode}" escape="false"/>
			</h:column>
			<h:column>
				<f:facet name="header">
					<h:outputText value="#{bundle['message.unitType']}" />
				</f:facet>				
				<h:outputText value="#{bundleEnum[unit.type.name]}" escape="false"/>
			</h:column>
			<h:column>	
				<f:facet name="header">
					<h:outputText value="#{bundle['message.unitBeginDate']}" />
				</f:facet>				
				<h:outputFormat value="{0, date, dd/MM/yyyy}">
					<f:param value="#{unit.beginDate}"/>
				</h:outputFormat>
			</h:column>
			<h:column>	
				<f:facet name="header">
					<h:outputText value="#{bundle['message.unitEndDate']}" />
				</f:facet>								
				<h:outputFormat value="{0, date, dd/MM/yyyy}" rendered="#{!empty unit.endDate}">
					<f:param value="#{unit.endDate}"/>
				</h:outputFormat>					
			</h:column>										
		</h:dataTable>			
				
		<h:outputText value="<br/>" escape="false" />			
		<h:outputText value="<b>#{bundle['title.relation.type']}</b>:" escape="false" />		
		<fc:selectOneMenu value="#{organizationalStructureBackingBean.unitRelationTypeValue}">
			<f:selectItems value="#{organizationalStructureBackingBean.unitRelationTypes}"/>				
		</fc:selectOneMenu>
		<h:outputText value="<br/><br/>" escape="false" />	
						
		<h:outputText value="<br/>" escape="false" />								
		<h:commandButton alt="#{htmlAltBundle['commandButton.choose']}" action="#{organizationalStructureBackingBean.associateParentUnit}" value="#{bundle['button.choose']}" 
			styleClass="inputbutton"/> 		
		<h:commandButton alt="#{htmlAltBundle['commandButton.return']}" action="listAllUnitsToChooseParentUnit" immediate="true" value="#{bundle['label.return']}"
			 styleClass="inputbutton"/>	
				
	</h:form>
</f:view>