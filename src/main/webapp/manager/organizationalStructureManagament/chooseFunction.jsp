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
	<f:loadBundle basename="resources/EnumerationResources" var="bundleEnum"/>
	
	<h:form>	

		<h:inputHidden binding="#{organizationalStructureBackingBean.unitIDHidden}"/>
		<h:inputHidden binding="#{organizationalStructureBackingBean.chooseUnitIDHidden}"/>	
		<h:inputHidden binding="#{organizationalStructureBackingBean.functionIDHidden}"/>
										
		<h:outputText value="<h2>#{bundle['title.chooseFunction']}</h2><br/>" escape="false" />		
		
		<span class="alert alert-info">
			<h:outputText value="<b>#{bundle['message.name']}</b>" escape="false"/>		
			<h:outputText value="#{organizationalStructureBackingBean.chooseUnit.name}" escape="false"/>
		</span>
		
		<h:outputText value="<br/><br/><br/>" escape="false" />		
		
		<h:outputText styleClass="error" rendered="#{!empty organizationalStructureBackingBean.errorMessage}"
				value="#{bundle[organizationalStructureBackingBean.errorMessage]}<br/>" escape="false"/>
								
		<h:dataTable value="#{organizationalStructureBackingBean.chooseUnit.functions}" var="function"
			 headerClass="listClasses-header" columnClasses="listClasses" rendered="#{!empty organizationalStructureBackingBean.chooseUnit.functions}">
			<h:column>
				<f:facet name="header">
					<h:outputText value="#{bundle['title.FunctionName']}" />
				</f:facet>				
				<h:outputText value="<strong>#{function.name}</strong>" escape="false"/>				
			</h:column>
			<h:column>
				<f:facet name="header">
					<h:outputText value="#{bundle['message.unitType']}" />
				</f:facet>				
				<h:outputText value="#{bundleEnum[function.functionType.name]}" escape="false"/>
			</h:column>
			<h:column>
				<f:facet name="header">
					<h:outputText value="#{bundle['message.unitBeginDate']}" />
				</f:facet>				
				<h:outputFormat value="{0, date, dd/MM/yyyy}" rendered="#{!empty function.beginDate}">
					<f:param value="#{function.beginDate}"/>
				</h:outputFormat>	
			</h:column>
			<h:column>
				<f:facet name="header">
					<h:outputText value="#{bundle['message.unitEndDate']}" />
				</f:facet>				
				<h:outputFormat value="{0, date, dd/MM/yyyy}" rendered="#{!empty function.endDate}">
					<f:param value="#{function.endDate}"/>
				</h:outputFormat>			
			</h:column>
			<h:column>	
				<f:facet name="header">
					<h:outputText value="#{bundle['message.action']}" />
				</f:facet>								
				<h:commandLink action="#{organizationalStructureBackingBean.associateInherentParentFunction}" value="#{bundle['button.choose']}">
					<f:param id="principalFunctionID" name="principalFunctionID" value="#{function.externalId}"/>
				</h:commandLink>				
			</h:column>
		</h:dataTable>		
		
		<h:outputText value="#{bundle['unit.withoutFunctions']}<br/>" rendered="#{empty organizationalStructureBackingBean.chooseUnit.functions}" 
				styleClass="error" escape="false"/>				
		
		<h:outputText value="<br/>" escape="false" />	
		<h:commandButton alt="#{htmlAltBundle['commandButton.return']}" action="listAllUnits" immediate="true" value="#{bundle['label.return']}" styleClass="inputbutton"/>								
				
	</h:form>
</f:view>